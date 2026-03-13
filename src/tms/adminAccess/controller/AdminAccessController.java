package tms.adminAccess.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import tms.adminAccess.maps.AdminAccessMapper;
import tms.main.maps.MainMapper;
import tms.main.util.KediAES256Util;

@Controller
@EnableScheduling
@Configuration
@RequestMapping("/adminAccess")
public class AdminAccessController {
	private Log log = LogFactory.getLog(this.getClass());

	@Autowired
	MainMapper mainMapper;

	@Autowired
	MessageSource messageSource;

	@Autowired
	AdminAccessMapper adminAccessMapper;

	@Resource(name = "txManager")
	private PlatformTransactionManager txManager;

	@RequestMapping("/adminAccessList.do")
	public String adminAccessList(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
		HttpSession session = request.getSession();

		String di = (String) session.getAttribute("DI");
		String clientIp = (String) session.getAttribute("clientIp");

		if (di == null || di.length() != 64) {
			String errorMessage = messageSource.getMessage("msg_102", null, Locale.getDefault());
			redirectAttributes.addFlashAttribute("message", errorMessage);
			return "redirect:/main/main.do";
		} else {
			if (mainMapper.isExistPoolDi(di)) {

				Map<String, Object> loginInfo = mainMapper.getPoolInfo(di);
				String usrId = (String) loginInfo.get("USR_ID");
				String role = (String) loginInfo.get("ROLE");
				String ip = (String) loginInfo.get("IP");

				if (role == null || !"A".equals(role)) {
					String errorMessage = messageSource.getMessage("msg_108", null, Locale.getDefault());
					redirectAttributes.addFlashAttribute("message", errorMessage);
					request.getSession().invalidate();
					return "redirect:/main/main.do";
				}

				if (ip == null || clientIp == null) {
					String errorMessage = "접속 실패: 등록된 IP가 없습니다.";
					redirectAttributes.addFlashAttribute("message", errorMessage);
					request.getSession().invalidate(); // 세션 무효화
					return "redirect:/main/main.do";
				}

				if (!clientIp.equals(ip)) {
					String errorMessage = "접속 실패: 현재 접속한 IP (" + clientIp + ")가 등록된 ip와 일치하지 않습니다.";
					redirectAttributes.addFlashAttribute("message", errorMessage);
					request.getSession().invalidate(); // 세션 무효화
					return "redirect:/main/main.do";
				}

			} else {
				String errorMessage = messageSource.getMessage("msg_105", null, Locale.getDefault());
				redirectAttributes.addFlashAttribute("message", errorMessage);
				request.getSession().invalidate();
				return "redirect:/join/joinStep01.do";
			}
		}

		return "/adminAccess/adminAccessView";
	}

	@RequestMapping(value = "/getAdminAccessList.do")
	public void getAdminAccessList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		String di = (String) session.getAttribute("DI");
		String usrId = (String) session.getAttribute("USR_ID");
		String sname = (String) session.getAttribute("NAME");
		String clientIp = (String) session.getAttribute("clientIp");

		String sStartDate = request.getParameter("startDate");
		String sEndDate = request.getParameter("endDate");
		String sNameEmail = request.getParameter("nameEmail");
		String sRmrk = request.getParameter("rmrk");
		String sLntTimeStr = request.getParameter("lntTime");
		String sMultiSearchStr = request.getParameter("multiSearch");

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		// 파라미터 설정
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sMultiSearchStr", sMultiSearchStr); // true/false 값 설정
		
		// 데이터 조회
		List<Map<String, Object>> list = adminAccessMapper.getAdminAccessList(paramMap);

		// 정규식 패턴 정의
		Pattern subPattern = Pattern.compile("대상자: ([^,]+)");
		Pattern viewerPattern = Pattern.compile("조회자: ([^,]+)");

		// list의 각 항목을 업데이트
		list.forEach(m -> {
			// HIS_NO 수정
			String hisNo = (String) m.get("HIS_NO");
			if (hisNo != null) {
				m.put("HIS_NO", hisNo.replaceFirst("^H0+", ""));
			}

			// RMRK 값에서 대상자와 조회자 추출
			String rmrk = (String) m.get("RMRK");
			String inptName = (String) m.get("INPT_NM");

			String subName = "";
			String viewerName = "";

			KediAES256Util aes256Util = new KediAES256Util();
			
			// 대상자 추출
			if(rmrk!=null){
				Matcher subMatcher = subPattern.matcher(rmrk);
				if (subMatcher.find()) {
					String extractedSubName = subMatcher.group(1).trim();
					if (!"null".equals(extractedSubName)) {
						subName = extractedSubName;
					}
				}
			}

			if (subName != null) {
				// 대상자가 있으면 이메일 조회
				Map<String, Object> emailParam = new HashMap<>();
				try {
					emailParam.put("name", aes256Util.aesEncode(subName));
					Map<String, Object> emailResult = adminAccessMapper.getSubjectEmail(emailParam);
					if (emailResult != null) {
						String email = (String) emailResult.get("USR_ID");
						m.put("EMAIL", email != null ? email : "");
					}
				} catch (Exception e) {
					System.err.println(e);
				}

				// 조회자 추출
				if(rmrk!=null){
					Matcher viewerMatcher = viewerPattern.matcher(rmrk);
					if (viewerMatcher.find()) {
						String extractedViewerName = viewerMatcher.group(1).trim();
						if (!"null".equals(extractedViewerName) && !extractedViewerName.isEmpty()) {
							viewerName = extractedViewerName;
						}
					}
				}

				// 조회자 정보가 없으면 기본값으로 INPT_NAME 사용
				if (viewerName.isEmpty()) {
				    viewerName = inptName != null ? inptName : "";
				}

				m.put("SUB_NAME", subName);
				m.put("VIEWER_NAME", viewerName);
			}
		});

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		if ((sStartDate == null || sStartDate.isEmpty()) && (sEndDate == null || sEndDate.isEmpty())) {
		    // 날짜 입력이 없으면 필터링하지 않음.
		} else if ((sStartDate == null || sStartDate.isEmpty()) || (sEndDate == null || sEndDate.isEmpty())) {
		    resultMap.put("result", "E");
		    resultMap.put("message", "시작 날짜와 끝 날짜를 모두 입력해야 합니다.");
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(new Gson().toJson(resultMap));
		    return;
		} else {
		    try {
		        Date startDate = dateFormat.parse(sStartDate);
		        Date endDate = dateFormat.parse(sEndDate);
		        list.removeIf(m -> {
		            Object dtObj = m.get("INPT_DT");
		            if (dtObj instanceof String) {
		                String dtStr = (String) dtObj;
		                if (dtStr.length() >= 10) {
		                    try {
		                        Date inputDate = dateFormat.parse(dtStr.substring(0, 10));
		                        return inputDate.before(startDate) || inputDate.after(endDate);
		                    } catch (ParseException e) {
		                        return true;
		                    }
		                }
		            }
		            return true;
		        });
		    } catch (ParseException e) {
		        resultMap.put("result", "E");
		        resultMap.put("message", "날짜 형식이 잘못되었습니다.");
		        response.setContentType("application/json");
		        response.setCharacterEncoding("UTF-8");
		        response.getWriter().write(new Gson().toJson(resultMap));
		        return;
		    }
		}



		// 2. sNameEmail이 이메일 형식이면 EMAIL, 아니면 SUB_NAME 필터링
		if (sNameEmail != null && !sNameEmail.isEmpty()) {
			if (isValidEmail(sNameEmail)) {
				list.removeIf(m -> {
					String email = (String) m.get("EMAIL");
					return email == null || !email.equals(sNameEmail);
				});
			} else {
				list.removeIf(m -> {
					String subName = (String) m.get("SUB_NAME");
					return subName == null || !subName.equals(sNameEmail);
				});
			}
		}

		// 3. sRmrk 필터링
		if (sRmrk != null && !sRmrk.isEmpty()) {
			list.removeIf(m -> {
				String rmrk = (String) m.get("RMRK");
				return rmrk == null || !rmrk.contains(sRmrk);
			});
		}
		
		// 4. sLntTimeStr 값이 true일 때 밤 10시부터 새벽 4시까지 필터링
		if ("true".equals(sLntTimeStr)) {
		    list.removeIf(m -> {
		        String dtStr = (String) m.get("INPT_DT");
		        int hour = Integer.parseInt(dtStr.substring(11, 13));
		        return !(hour >= 0 && hour < 6);
		    });
		}

		resultMap.put("result", "S");
		resultMap.put("list", list);

		Gson gson = new Gson();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(gson.toJson(resultMap));
	}

	private boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		return email.matches(emailRegex);
	}
}
