package tms.main.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import NiceID.Check.CPClient;
import tms.cmmn.maps.CmmnMapper;
import tms.main.exception.ResMsgException;
import tms.main.maps.MainMapper;
import tms.main.util.KediAES256Util;

@Controller
@RequestMapping("/main")
public class MainController {
	private Log log = LogFactory.getLog(this.getClass());

	@Autowired
	MainMapper mainMapper;

	@Autowired
	CmmnMapper cmmnMapper;

	@Autowired
	MessageSource messageSource;

	@RequestMapping("/main.do")
	public String mainPage(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
		HttpSession session = request.getSession();

		KediAES256Util aes256Util = new KediAES256Util();

		String di = (String) session.getAttribute("DI");
		String name = (String) session.getAttribute("NAME");

		if (di != null) {
			if (mainMapper.isExistPoolDi(di)) {

				Map<String, Object> loginInfo = mainMapper.getPoolInfo(di);

				String usrId = (String) loginInfo.get("USR_ID");
				String role = (String) loginInfo.get("ROLE");

				/* if(name != null && !"".equals(name)) name = aes256Util.aesDecode(name); */

				session.setAttribute("ROLE", role);

				if (usrId != null) {

					session.setAttribute("USR_ID", usrId);
					session.setAttribute("NAME", name);
				}

			} else {

				String errorMessage = messageSource.getMessage("msg_105", null, Locale.getDefault());
				redirectAttributes.addFlashAttribute("message", errorMessage);
				request.getSession().invalidate();
				return "redirect:/join/joinStep01.do";
			}

		}
		return "/main/main";
	}

	@RequestMapping(value = "/selectPoolScheList.do")
	public void getList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("start_no", "1");
		paramMap.put("last_no", "3");

		List<Map<String, Object>> list = mainMapper.selectPoolScheList(paramMap);
		resultMap.put("result", "S");
		resultMap.put("list", list);

		log.info(list);

		Gson gson = new Gson();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(gson.toJson(resultMap));
	}

	@RequestMapping(value = "/nice.do")
	public String getPassTestPage(HttpServletRequest request) throws Exception {

		HttpSession session = request.getSession();
		String sParentLocation = (String) session.getAttribute("sParentLocation");

		String type = request.getParameter("type");
		if (type == null) {
			throw new ResMsgException("파라미터가 잘못되었습니다.");
		} else if (type.equals("insert")) {
			sParentLocation = request.getContextPath() + "/join/joinStep03.do";
			session.setAttribute("sParentLocation", sParentLocation);
			session.setAttribute("type", type);
		} else if (type.equals("select")) {
			sParentLocation = request.getContextPath() + "/main/main.do";
			session.setAttribute("sParentLocation", sParentLocation);
			session.setAttribute("type", type);
		}

		// 로컬서버
		// String url = "http://localhost:58083";
		// 개발서버
		// String url = "http://121.191.92.20:33336";
		// 실서버
		String url = "https://necte.kedi.re.kr";

		/*
		 * if(sParentLocation == null) { sParentLocation = request.getContextPath() +
		 * "/join/joinStep03.do"; session.setAttribute("sParentLocation",
		 * sParentLocation); }
		 */

		NiceID.Check.CPClient niceCheck = new NiceID.Check.CPClient();

		String sSiteCode = "CI879"; // NICE로부터 부여받은 사이트 코드
		String sSitePassword = "ZTKZyYEfoBjF "; // NICE로부터 부여받은 사이트 패스워드

		String sRequestNumber = niceCheck.getRequestNO(sSiteCode); // 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로

		String sAuthType = ""; // 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서

		String popgubun = "N"; // Y : 취소버튼 있음 / N : 취소버튼 없음
		String customize = ""; // 없으면 기본 웹페이지 / Mobile : 모바일페이지
		String sGender = ""; // 없으면 기본 선택 값, 0 : 여자, 1 : 남자

		String sReturnUrl = url + request.getContextPath() + "/main/checkplusSuccess.do"; // 성공시 이동될 URL
		String sErrorUrl = url + request.getContextPath() + "/main/checkplusFail.do"; // 실패시 이동될 URL

		// 입력될 plain 데이타를 만든다.
		String sPlainData = "7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber + "8:SITECODE"
				+ sSiteCode.getBytes().length + ":" + sSiteCode + "9:AUTH_TYPE" + sAuthType.getBytes().length + ":"
				+ sAuthType + "7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl + "7:ERR_URL"
				+ sErrorUrl.getBytes().length + ":" + sErrorUrl + "11:POPUP_GUBUN" + popgubun.getBytes().length + ":"
				+ popgubun + "9:CUSTOMIZE" + customize.getBytes().length + ":" + customize + "6:GENDER"
				+ sGender.getBytes().length + ":" + sGender;
		String sMessage = "";
		String sEncData = "";

		int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);
		if (iReturn == 0) {
			sEncData = niceCheck.getCipherData();
		} else if (iReturn == -1) {
			sMessage = "암호화 시스템 에러입니다.";
		} else if (iReturn == -2) {
			sMessage = "암호화 처리오류입니다.";
		} else if (iReturn == -3) {
			sMessage = "암호화 데이터 오류입니다.";
		} else if (iReturn == -9) {
			sMessage = "입력 데이터 오류입니다.";
		} else {
			sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
		}

		request.getSession().setAttribute("REQ_SEQ", sRequestNumber);

		request.setAttribute("sMessage", sMessage);
		request.setAttribute("sEncData", sEncData);

		return "common/nicePopup";
	}

	@RequestMapping(value = "/checkplusSuccess.do")
	public String checkplusSuccess(HttpServletRequest request, @RequestParam Map<String, Object> map) {

		NiceID.Check.CPClient niceCheck = new NiceID.Check.CPClient();

		String sEncodeData = requestReplace((String) map.get("EncodeData"), "encodeData");
		String sReserved1 = requestReplace((String) map.get("param_r1"), "");
		String sReserved2 = requestReplace((String) map.get("param_r2"), "");
		String sReserved3 = requestReplace((String) map.get("param_r3"), "");

		String sSiteCode = "CC284"; // NICE로부터 부여받은 사이트 코드
		String sSitePassword = "442z8I0k8orx "; // NICE로부터 부여받은 사이트 패스워드

		String sCipherTime = ""; // 복호화한 시간
		String sRequestNumber = ""; // 요청 번호
		String sResponseNumber = ""; // 인증 고유번호
		String sAuthType = ""; // 인증 수단
		String sMessage = "";
		String sPlainData = "";
		String sName = "";
		String sDupInfo = "";
		String sConnInfo = "";
		String sBirthDate = "";
		String sGender = "";
		String sMobileNo = "";
		String sMobileCo = "";
		String sNationalInfo = "";
		String clientIp = request.getHeader("X-Forwarded-For");
		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getRemoteAddr();
		}

		int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);

		if (iReturn == 0) {
			sPlainData = niceCheck.getPlainData();
			sCipherTime = niceCheck.getCipherDateTime();

			// 데이타를 추출합니다.
			HashMap mapresult = niceCheck.fnParse(sPlainData);

			sRequestNumber = (String) mapresult.get("REQ_SEQ");
			sResponseNumber = (String) mapresult.get("RES_SEQ");
			sAuthType = (String) mapresult.get("AUTH_TYPE");
			sName = (String) mapresult.get("NAME");
			sBirthDate = (String) mapresult.get("BIRTHDATE");
			sGender = (String) mapresult.get("GENDER");
			sNationalInfo = (String) mapresult.get("NATIONALINFO");
			sDupInfo = (String) mapresult.get("DI");
			sConnInfo = (String) mapresult.get("CI");
			sMobileNo = (String) mapresult.get("MOBILE_NO");
			sMobileCo = (String) mapresult.get("MOBILE_CO");

			HttpSession session = request.getSession();
			String session_sRequestNumber = (String) request.getSession().getAttribute("REQ_SEQ");
			if (!sRequestNumber.equals(session_sRequestNumber)) {
				sMessage = "세션값 불일치 오류입니다.";

			} else {

				session.setAttribute("NAME", sName);
				session.setAttribute("BIRTHDAY", sBirthDate);
				session.setAttribute("GENDER", sGender);
				session.setAttribute("DI", sDupInfo);
				session.setAttribute("CI", sConnInfo);
				session.setAttribute("TEL_HP", sMobileNo);
				session.setAttribute("clientIp", clientIp);

				String type = (String) session.getAttribute("type");

				Map<String, Object> hisparam = new HashMap<String, Object>();
				hisparam.put("MENU_NO", "P0001");
				hisparam.put("MENU_NM", "공모 본인인증");
				hisparam.put("WORK_TYPE", "조회");
				hisparam.put("FUNC_NM", "공모 본인인증");
				hisparam.put("PRIVACY_YN", 'N');

				hisparam.put("RESULT_TYPE", 'S');
				hisparam.put("sessUsrNm", sName);
				hisparam.put("usrIp", clientIp);

				if (type.equals("select")) {
					hisparam.put("RMRK", "공모시스템 로그인 위한 본인인증");
				} else {
					hisparam.put("RMRK", "공모시스템 회원가입 위한 본인인증");
				}
				cmmnMapper.insertHis(hisparam);
			}
		} else if (iReturn == -1) {
			sMessage = "복호화 시스템 오류입니다.";
		} else if (iReturn == -4) {
			sMessage = "복호화 처리 오류입니다.";
		} else if (iReturn == -5) {
			sMessage = "복호화 해쉬 오류입니다.";
		} else if (iReturn == -6) {
			sMessage = "복호화 데이터 오류입니다.";
		} else if (iReturn == -9) {
			sMessage = "입력 데이터 오류입니다.";
		} else if (iReturn == -12) {
			sMessage = "사이트 패스워드 오류입니다.";
		} else {
			sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
		}

		return "common/checkplusSuccess";
	}

	@RequestMapping("checkplusFail.do")
	public String checkplusFail(HttpServletRequest request, @RequestParam Map<String, Object> map) {

		CPClient niceCheck = new CPClient();

		String sEncodeData = requestReplace((String) map.get("EncodeData"), "encodeData");

		String sSiteCode = "";
		String sSitePassword = ""; //

		niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);

		return "common/checkplusFail";
	}

	public String requestReplace(String paramValue, String gubun) {

		String result = "";

		if (paramValue != null) {

			paramValue = paramValue.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

			paramValue = paramValue.replaceAll("\\*", "");
			paramValue = paramValue.replaceAll("\\?", "");
			paramValue = paramValue.replaceAll("\\[", "");
			paramValue = paramValue.replaceAll("\\{", "");
			paramValue = paramValue.replaceAll("\\(", "");
			paramValue = paramValue.replaceAll("\\)", "");
			paramValue = paramValue.replaceAll("\\^", "");
			paramValue = paramValue.replaceAll("\\$", "");
			paramValue = paramValue.replaceAll("'", "");
			paramValue = paramValue.replaceAll("@", "");
			paramValue = paramValue.replaceAll("%", "");
			paramValue = paramValue.replaceAll(";", "");
			paramValue = paramValue.replaceAll(":", "");
			paramValue = paramValue.replaceAll("-", "");
			paramValue = paramValue.replaceAll("#", "");
			paramValue = paramValue.replaceAll("--", "");
			paramValue = paramValue.replaceAll("-", "");
			paramValue = paramValue.replaceAll(",", "");

			if (gubun != "encodeData") {
				paramValue = paramValue.replaceAll("\\+", "");
				paramValue = paramValue.replaceAll("/", "");
				paramValue = paramValue.replaceAll("=", "");
			}

			result = paramValue;

		}
		return result;
	}

	@RequestMapping("/logout.do")
	public String logout(HttpServletRequest request) throws Exception {
		request.getSession().invalidate();
		return "redirect:/main/main.do";
	}
	

//	@RequestMapping("/adminLoginPage.do")
//	public String adminLoginPage(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
//		HttpSession session = request.getSession();

		// KediAES256Util aes256Util = new KediAES256Util();

		// String di = (String) session.getAttribute("DI");
		// String name = (String) session.getAttribute("NAME");

		/*
		 * if(di != null){ if(mainMapper.isExistPoolDi(di)) {
		 * 
		 * Map<String,Object> loginInfo = mainMapper.getPoolInfo(di);
		 * 
		 * 
		 * String usrId = (String) loginInfo.get("USR_ID"); String role = (String)
		 * loginInfo.get("ROLE");
		 * 
		 * if(name != null && !"".equals(name)) name = aes256Util.aesDecode(name);
		 * 
		 * session.setAttribute("ROLE",role);
		 * 
		 * if(usrId != null) {
		 * 
		 * session.setAttribute("USR_ID",usrId); session.setAttribute("NAME",name); }
		 * 
		 * } else {
		 * 
		 * String errorMessage = messageSource.getMessage("msg_105", null,
		 * Locale.getDefault()); redirectAttributes.addFlashAttribute("message",
		 * errorMessage); request.getSession().invalidate(); return
		 * "redirect:/join/joinStep01.do"; }
		 * 
		 * 
		 * }
		 */
		// String usrId = (String) session.getAttribute("USR_ID");
		
		/*
		 * if(session != null) { Map<String,Object> loginInfo =
		 * mainMapper.getPoolInfo(di); } else {
		 * 
		 * }
		 */
		
//		return "/main/adminLoginPage";
//	}
	
/*
	@RequestMapping(value = "/adminLogin.do", method = RequestMethod.POST)
	public void adminLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> map) throws Exception {
		HttpSession session = request.getSession();
		
		String usrId = (String) map.get("usrId");
		String pw = (String) map.get("pw");
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		if("kedi1234!!".equals(pw)) {
			Map<String,Object> loginInfo = mainMapper.getPoolInfoAdmin(usrId);
			
			if(loginInfo != null) {
				String userId = (String) loginInfo.get("USR_ID");
				String role = (String) loginInfo.get("ROLE");
				String di = (String) loginInfo.get("DI");
				String ci = (String) loginInfo.get("CI");
				String clientIp = request.getHeader("X-Forwarded-For");
				if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
					clientIp = request.getRemoteAddr();
				}
				session.setAttribute("USR_ID",userId); 
				session.setAttribute("ROLE", role);
				session.setAttribute("DI", di);
				session.setAttribute("CI", ci);
				session.setAttribute("clientIp", clientIp);
				
				resultMap.put("result", "S");
				resultMap.put("code", "0");
			} else {
				resultMap.put("result", "F");	
				resultMap.put("code", "1");
			}
		} else {
			resultMap.put("result", "F");	
			resultMap.put("code", "2");
		}

		Gson gson = new Gson();
        response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
    	response.getWriter().write(gson.toJson(resultMap));        
	}
*/

}