package tms.adminPool.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tms.adminPool.service.ScheduleService;
import tms.main.util.KediAES256Util;
import tms.pool.maps.PoolMapper;
import tms.adminPool.maps.AdminPoolMapper;
import tms.cmmn.maps.CmmnMapper;
import tms.cmmn.service.CmmnService;
import tms.main.exception.ResMsgException;
import tms.main.maps.MainMapper;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Controller
@EnableScheduling
@Configuration
@RequestMapping("/adminPool")
public class AdminPoolController {
	private Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	MainMapper mainMapper;

	@Autowired
	CmmnMapper cmmnMapper;
	
    @Autowired
    CmmnService cmmnService;
	
	@Autowired
	MessageSource messageSource;

	@Autowired
	AdminPoolMapper adminPoolMapper;

	@Autowired
	PoolMapper poolMapper;

	@Resource(name = "txManager")
	private PlatformTransactionManager txManager;

	@RequestMapping("/adminPoolList.do")
	public String adminPoolList(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
		HttpSession session = request.getSession();

		String di = (String) session.getAttribute("DI");
		String clientIp = (String) session.getAttribute("clientIp");
		// String currentIp = request.getRemoteAddr();
		// String forwardedIp = request.getHeader("X-Forwarded-For");

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

		return "/adminPool/adminPoolView";
	}

	@RequestMapping(value = "/getAdminPoolList.do")
	public void getPoolList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		String di = (String) session.getAttribute("DI");
		String usrId = (String) session.getAttribute("USR_ID");
		String sname = (String) session.getAttribute("NAME");
		String clientIp = (String) session.getAttribute("clientIp");

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("role", "U");

		KediAES256Util aes256Util = new KediAES256Util();

		if (paramMap != null) { // 조회조건 암호화
			String name = (String) paramMap.get("NAME");
			if (name != null && !"".equals(name))
				paramMap.put("NAME", aes256Util.aesEncode(name));
			String gender = (String) paramMap.get("GENDER");
			if (gender != null && !"".equals(gender))
				paramMap.put("GENDER", aes256Util.aesEncode(gender));
			String telHp = (String) paramMap.get("TEL_HP");
			if (telHp != null && !"".equals(telHp))
				paramMap.put("TEL_HP", aes256Util.aesEncode(telHp));
			String telHome = (String) paramMap.get("TEL_HOME");
			if (telHome != null && !"".equals(telHome))
				paramMap.put("TEL_HOME", aes256Util.aesEncode(telHome));
			String birthday = (String) paramMap.get("BIRTHDAY");
			if (birthday != null && !"".equals(birthday))
				paramMap.put("BIRTHDAY", aes256Util.aesEncode(birthday));
			String jobNm = (String) paramMap.get("JOB_NM");
			if (jobNm != null && !"".equals(jobNm))
				paramMap.put("JOB_NM", aes256Util.aesEncode(jobNm));
			String position = (String) paramMap.get("POSITION");
			if (position != null && !"".equals(position))
				paramMap.put("POSITION", aes256Util.aesEncode(position));
			String dept = (String) paramMap.get("DEPT");
			if (dept != null && !"".equals(dept))
				paramMap.put("DEPT", aes256Util.aesEncode(dept));
			String telJob = (String) paramMap.get("TEL_JOB");
			if (telJob != null && !"".equals(telJob))
				paramMap.put("TEL_JOB", aes256Util.aesEncode(telJob));
		}

		List<Map<String, Object>> list = adminPoolMapper.getAdminPoolList(paramMap);

		for (Map<String, Object> resultObj : list) { // 조회결과 복호화
			String name = (String) resultObj.get("NAME");
			String gender = (String) resultObj.get("GENDER");
			String telHp = (String) resultObj.get("TEL_HP");
			String telHome = (String) resultObj.get("TEL_HOME");
			String birthday = (String) resultObj.get("BIRTHDAY");
			String addrHomeZipcode = (String) resultObj.get("ADDR_HOME_ZIPCODE");
			String addrHome = (String) resultObj.get("ADDR_HOME");
			String addrHomeDetail = (String) resultObj.get("ADDR_HOME_DETAIL");
			String jobNm = (String) resultObj.get("JOB_NM");
			String position = (String) resultObj.get("POSITION");
			String dept = (String) resultObj.get("DEPT");
			String email = (String) resultObj.get("EMAIL");
			String addrJobZipcode = (String) resultObj.get("ADDR_JOB_ZIPCODE");
			String addrJob = (String) resultObj.get("ADDR_JOB");
			String addrJobDetail = (String) resultObj.get("ADDR_JOB_DETAIL");
			String telJob = (String) resultObj.get("TEL_JOB");

			if (name != null && !"".equals(name))
				name = aes256Util.aesDecode(name);
			if (gender != null && !"".equals(gender))
				gender = aes256Util.aesDecode(gender);
			if (telHp != null && !"".equals(telHp))
				telHp = aes256Util.aesDecode(telHp);
			if (telHome != null && !"".equals(telHome))
				telHome = aes256Util.aesDecode(telHome);
			if (birthday != null && !"".equals(birthday))
				birthday = aes256Util.aesDecode(birthday);
			if (addrHomeZipcode != null && !"".equals(addrHomeZipcode))
				addrHomeZipcode = aes256Util.aesDecode(addrHomeZipcode);
			if (addrHome != null && !"".equals(addrHome))
				addrHome = aes256Util.aesDecode(addrHome);
			if (addrHomeDetail != null && !"".equals(addrHomeDetail))
				addrHomeDetail = aes256Util.aesDecode(addrHomeDetail);
			if (jobNm != null && !"".equals(jobNm))
				jobNm = aes256Util.aesDecode(jobNm);
			if (position != null && !"".equals(position))
				position = aes256Util.aesDecode(position);
			if (dept != null && !"".equals(dept))
				dept = aes256Util.aesDecode(dept);
			if (email != null && !"".equals(email))
				email = aes256Util.aesDecode(email);
			if (addrJobZipcode != null && !"".equals(addrJobZipcode))
				addrJobZipcode = aes256Util.aesDecode(addrJobZipcode);
			if (addrJob != null && !"".equals(addrJob))
				addrJob = aes256Util.aesDecode(addrJob);
			if (addrJobDetail != null && !"".equals(addrJobDetail))
				addrJobDetail = aes256Util.aesDecode(addrJobDetail);
			if (telJob != null && !"".equals(telJob))
				telJob = aes256Util.aesDecode(telJob);

			// 성별 필드 'GENDER'의 값을 조건에 따라 변경
			if (gender != null) {
				gender = (gender.equals("M") || gender.equals("1")) ? "남성" : "여성";
			}

			resultObj.put("NAME", name);
			resultObj.put("GENDER", gender);
			resultObj.put("TEL_HP", telHp);
			resultObj.put("TEL_HOME", telHome);
			resultObj.put("BIRTHDAY", birthday);
			resultObj.put("ADDR_HOME_ZIPCODE", addrHomeZipcode);
			resultObj.put("ADDR_HOME", addrHome);
			resultObj.put("ADDR_HOME_DETAIL", addrHomeDetail);
			resultObj.put("JOB_NM", jobNm);
			resultObj.put("POSITION", position);
			resultObj.put("DEPT", dept);
			resultObj.put("EMAIL", email);
			resultObj.put("ADDR_JOB_ZIPCODE", addrJobZipcode);
			resultObj.put("ADDR_JOB", addrJob);
			resultObj.put("ADDR_JOB_DETAIL", addrJobDetail);
			resultObj.put("TEL_JOB", telJob);
		}

		resultMap.put("result", "S");
		resultMap.put("list", list);

		log.info(list);

		if (clientIp != null && clientIp.contains(",")) {
		    // 쉼표(,)로 분리
		    String[] ipArray = clientIp.split(",");
		    // 첫 번째 IP만 Trim 해서 사용
		    clientIp = ipArray[0].trim();
		}
		
		Map<String, Object> hisparam = new HashMap<String, Object>();
		hisparam.put("MENU_NO", "P0007");
		hisparam.put("MENU_NM", "공모 후보자 조회");
		hisparam.put("WORK_TYPE", "조회");
		hisparam.put("FUNC_NM", "공모 후보자 조회");
		hisparam.put("PRIVACY_YN", 'Y');
		hisparam.put("RMRK", "공모시스템 후보자 리스트 화면 조회");
		hisparam.put("RESULT_TYPE", 'S');
		hisparam.put("sessUsrNm", sname);
		hisparam.put("usrIp", clientIp);
		cmmnMapper.insertHis(hisparam);

		Gson gson = new Gson();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(gson.toJson(resultMap));
	}

	@RequestMapping(value = "/getPoolInfo.do")
	public void getList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		String di = (String) session.getAttribute("DI");
		String sUsrId = (String) session.getAttribute("USR_ID");
		String sname = (String) session.getAttribute("NAME");
		String clientIp = (String) session.getAttribute("clientIp");

		if (di == null) {
			session.invalidate(); // 세션 만료
			throw new ResMsgException("본인인증 이 필요 합니다.");
		} else {
			Map<String, Object> loginInfo = mainMapper.getPoolInfo(di);
			String role = (String) loginInfo.get("ROLE");

			if (role == null || !"A".equals(role)) {
				session.invalidate(); // 세션 만료
				throw new ResMsgException("관리자만 사용 가능합니다.");
			}
		}

		KediAES256Util aes256Util = new KediAES256Util();

		String usrId = request.getParameter("usrId");
		String usrDi = request.getParameter("di");

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("di", usrDi);
		paramMap.put("usrId", usrId);

		Map<String, Object> poolData = adminPoolMapper.getAdminPoolInfo(paramMap);

		if (poolData != null) {
			String name = (String) poolData.get("NAME");
			String gender = (String) poolData.get("GENDER");
			String telHp = (String) poolData.get("TEL_HP");
			String telHome = (String) poolData.get("TEL_HOME");
			String birthday = (String) poolData.get("BIRTHDAY");
			String addrHomeZipcode = (String) poolData.get("ADDR_HOME_ZIPCODE");
			String addrHome = (String) poolData.get("ADDR_HOME");
			String addrHomeDetail = (String) poolData.get("ADDR_HOME_DETAIL");
			String jobNm = (String) poolData.get("JOB_NM");
			String position = (String) poolData.get("POSITION");
			String dept = (String) poolData.get("DEPT");
			String email = (String) poolData.get("EMAIL");

			if (name != null && !"".equals(name))
				name = aes256Util.aesDecode(name);
			if (gender != null && !"".equals(gender))
				gender = aes256Util.aesDecode(gender);
			if (telHp != null && !"".equals(telHp))
				telHp = aes256Util.aesDecode(telHp);
			if (telHome != null && !"".equals(telHome))
				telHome = aes256Util.aesDecode(telHome);
			if (birthday != null && !"".equals(birthday))
				birthday = aes256Util.aesDecode(birthday);
			if (addrHomeZipcode != null && !"".equals(addrHomeZipcode))
				addrHomeZipcode = aes256Util.aesDecode(addrHomeZipcode);
			if (addrHome != null && !"".equals(addrHome))
				addrHome = aes256Util.aesDecode(addrHome);
			if (addrHomeDetail != null && !"".equals(addrHomeDetail))
				addrHomeDetail = aes256Util.aesDecode(addrHomeDetail);
			if (jobNm != null && !"".equals(jobNm))
				jobNm = aes256Util.aesDecode(jobNm);
			if (position != null && !"".equals(position))
				position = aes256Util.aesDecode(position);
			if (dept != null && !"".equals(dept))
				dept = aes256Util.aesDecode(dept);
			if (email != null && !"".equals(email))
				email = aes256Util.aesDecode(email);

			if (gender != null) {
				if (gender.equals("M") || gender.equals("1")) {
					gender = "1";
				} else if (gender.equals("F") || gender.equals("0")) {
					gender = "0";
				}
			}

			poolData.put("NAME", name);
			poolData.put("GENDER", gender);
			poolData.put("TEL_HP", telHp);
			poolData.put("TEL_HOME", telHome);
			poolData.put("BIRTHDAY", birthday);
			poolData.put("ADDR_HOME_ZIPCODE", addrHomeZipcode);
			poolData.put("ADDR_HOME", addrHome);
			poolData.put("ADDR_HOME_DETAIL", addrHomeDetail);
			poolData.put("JOB_NM", jobNm);
			poolData.put("POSITION", position);
			poolData.put("DEPT", dept);
			poolData.put("EMAIL", email);

		}

		if (usrId != null) {

			List<Map<String, Object>> poolDegreeList = adminPoolMapper.selectPoolDegreeList(paramMap);
			List<Map<String, Object>> poolFulltimeTeacherList = adminPoolMapper.selectPoolFulltimeTeacherList(paramMap);
			paramMap.put("typeDiv", "1");
			List<Map<String, Object>> poolEvaExp1List = adminPoolMapper.selectPoolEvaExpList(paramMap);
			paramMap.put("typeDiv", "2");
			List<Map<String, Object>> poolEvaExp2List = adminPoolMapper.selectPoolEvaExpList(paramMap);

			resultMap.put("poolDegreeList", poolDegreeList);
			resultMap.put("poolFulltimeTeacherList", poolFulltimeTeacherList);
			resultMap.put("poolEvaExp1List", poolEvaExp1List);
			resultMap.put("poolEvaExp2List", poolEvaExp2List);

		}

		resultMap.put("poolData", poolData);

		resultMap.put("result", "S");

		log.info(poolData);

		String usrName = (String) poolData.get("NAME");

		if (clientIp != null && clientIp.contains(",")) {
		    // 쉼표(,)로 분리
		    String[] ipArray = clientIp.split(",");
		    // 첫 번째 IP만 Trim 해서 사용
		    clientIp = ipArray[0].trim();
		}
		
		Map<String, Object> hisparam = new HashMap<String, Object>();
		hisparam.put("MENU_NO", "P0007");
		hisparam.put("MENU_NM", "공모 후보자정보 상세조회");
		hisparam.put("WORK_TYPE", "조회");
		hisparam.put("FUNC_NM", "공모 후보자정보 상세조회");
		hisparam.put("PRIVACY_YN", 'Y');
		String remark = String.format("공모시스템 후보자정보 상세조회 - 대상자: %s, 조회자: %s", usrName, sname);
		hisparam.put("RMRK", remark);
		hisparam.put("RESULT_TYPE", 'S');
		hisparam.put("sessUsrNm", sname);
		hisparam.put("usrIp", clientIp);
		cmmnMapper.insertHis(hisparam);

		Gson gson = new Gson();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(gson.toJson(resultMap));
	}

	@RequestMapping(value = "/exportData.do", method = RequestMethod.GET)
	public void exportData(String pq_filename, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession ses = request.getSession(true);
		String sname = (String) ses.getAttribute("NAME");
		String clientIp = (String) ses.getAttribute("clientIp");

		if (((String) ses.getAttribute("pq_filename")).equals(pq_filename)) {
			String contents = (String) ses.getAttribute("pq_data");
			Boolean pq_decode = (Boolean) ses.getAttribute("pq_decode");
			String pq_ext = (String) ses.getAttribute("pq_ext");

			String paramFile = pq_filename.substring(0, pq_filename.lastIndexOf(pq_ext) - 1);
			String fileName = URLDecoder.decode(new String(Base64.decodeBase64(paramFile)), "UTF-8");
			log.info("fileName: " + fileName + "." + pq_ext);

			byte[] bytes = pq_decode ? Base64.decodeBase64(contents) : contents.getBytes(Charset.forName("UTF-8"));

			response.setContentType("application/octet-stream");

			response.setHeader("Content-Disposition",
					"attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "." + pq_ext + "\"");
			response.setContentLength(bytes.length);
			ServletOutputStream out = response.getOutputStream();
			out.write(bytes);

			out.flush();
			out.close();
			
			if (clientIp != null && clientIp.contains(",")) {
			    // 쉼표(,)로 분리
			    String[] ipArray = clientIp.split(",");
			    // 첫 번째 IP만 Trim 해서 사용
			    clientIp = ipArray[0].trim();
			}

			// 데이터베이스 로깅
			Map<String, Object> hisparam = new HashMap<>();
			hisparam.put("MENU_NO", "P0007");
			hisparam.put("MENU_NM", "공모 후보자정보 리스트 조회");
			hisparam.put("WORK_TYPE", "조회");
			hisparam.put("FUNC_NM", "공모 후보자정보 리스트 조회");
			hisparam.put("PRIVACY_YN", 'Y');
			String remark = String.format("공모시스템 후보자정보 리스트 조회 - 파일 다운로드: %s, 다운로드: %s", fileName, sname);
			hisparam.put("RMRK", remark);
			hisparam.put("RESULT_TYPE", 'S');
			hisparam.put("sessUsrNm", sname);
			hisparam.put("usrIp", clientIp);
			cmmnMapper.insertHis(hisparam);
		}

	}

	@RequestMapping(value = "/exportData.do", method = RequestMethod.POST)
	public @ResponseBody String exportData(String pq_data, String pq_ext, Boolean pq_decode, String pq_filename,
			HttpServletRequest request) throws IOException {

		String[] arr = new String[] { "csv", "xlsx", "htm", "zip", "json" };
		String filename = pq_filename + "." + pq_ext;

		if (Arrays.asList(arr).contains(pq_ext)) {
			HttpSession ses = request.getSession(true);
			ses.setAttribute("pq_data", pq_data);
			ses.setAttribute("pq_decode", pq_decode);
			ses.setAttribute("pq_filename", filename);
			ses.setAttribute("pq_ext", pq_ext);
		}
		return filename;
	}

	public void getConsentRequired() throws Exception {
		TransactionStatus txStatus = txManager.getTransaction(new DefaultTransactionDefinition());

		Gson gson = new Gson();
		KediAES256Util aes256Util = new KediAES256Util();

		try {
			backupAndDeleteExpired();

			txManager.commit(txStatus);

			txStatus = txManager.getTransaction(new DefaultTransactionDefinition());

			List<Map<String, Object>> userList = adminPoolMapper.getConsentRequired();

			// 나중에 리스트로 받아야함

			if (userList == null) {
				// throw new ResMsgException("사용자 정보가 없습니다.");
				return;
			}

			for (Map<String, Object> user : userList) {
				String UNIV_NM = (String) user.get("UNIV_NM");
				String UNIV_CD = (String) user.get("UNIV_CD");
				String USR_ID = (String) user.get("USR_ID");
				String NAME = (String) user.get("NAME");
				String ROLE = (String) user.get("ROLE");
				String PRIV_DTTM = new java.text.SimpleDateFormat("yyyy/MM/dd")
						.format((java.sql.Timestamp) user.get("PRIV_DTTM"));

				// if(UNIV_CD != null && !"".equals(UNIV_CD)) UNIV_CD =
				// aes256Util.aesDecode(UNIV_CD);
				// if(USR_ID != null && !"".equals(USR_ID)) USR_ID =
				// aes256Util.aesDecode(USR_ID);
				if (NAME != null && !"".equals(NAME))
					NAME = aes256Util.aesDecode(NAME);
				// if(ROLE != null && !"".equals(ROLE)) ROLE =
				// aes256Util.aesDecode(ROLE);

				Map<String, Object> mailParam = new HashMap<String, Object>();
				mailParam.put("univNm", UNIV_NM);
				mailParam.put("univCd", UNIV_CD);
				mailParam.put("usrId", USR_ID);
				mailParam.put("usrNm", NAME);
				mailParam.put("usrEmail", USR_ID);
				mailParam.put("usrPerm", ROLE);
				mailParam.put("PRIV_DTTM", PRIV_DTTM);
				// mailParam.put("univNm", univNm);
				// mailParam.put("tmpPws", certNum);
				mailParam.put("callingMethod", "sendMailCertNum");

				scheduleService.sendPwdMail(mailParam);
			}

			txManager.commit(txStatus);

		} catch (Exception e) {
			txManager.rollback(txStatus);
			throw e;
		}
	}

	@Transactional // 스프링 트랜잭션
	public void backupAndDeleteExpired() {
		KediAES256Util aes256Util = new KediAES256Util();
		
        // 1) 암호화된 NAME, USR_ID 조회
        try {
			List<Map<String, Object>> encList = adminPoolMapper.selectEncryptedRows();
			
			for (Map<String, Object> row : encList) {
	            String encName = (String) row.get("NAME");
	            String usrId = (String) row.get("USR_ID");
	            
	            // Java에서 복호화
	            String decName = aes256Util.aesDecode(encName); 
	            
	            // INSERT용 파라미터 구성
	            Map<String, Object> param = new HashMap<>();
	            param.put("NAME", decName);
	            param.put("USR_ID", usrId);
	            
	            // 테이블에 INSERT
	            adminPoolMapper.insertDeletedMember(param);
	            
	            //하위 테이블에서 삭제
	            adminPoolMapper.deletePoolApplByUsrId(param);
	            adminPoolMapper.deletePoolDisqUnivByUsrId(param);
	            adminPoolMapper.deletePoolEvaExpByUsrId(param);
	            adminPoolMapper.deletePoolFulltimeTeacherByUsrId(param);
	            adminPoolMapper.deletePoolDegreeByUsrId(param);
			}
			
			// 원본 테이블 DELETE
			adminPoolMapper.deleteExpiredRows();

        } catch (Exception e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
	}

	// 테스트용
/*	@Scheduled(cron = "0 * * * * ?")
	public void scheduleGetConsentRequired() throws Exception {
		getConsentRequired();
	}*/

	// 오전 9마다 반복
//	 @Scheduled(cron = "0 0 9 * * ?")
//	 public void scheduleGetConsentRequired() throws Exception {
//		 getConsentRequired();
//	 }
	 

	@RequestMapping(value = "/sendJoinMail.do")
	public void sendJoinMail(HttpServletRequest request, HttpServletResponse response, @RequestParam String checkedData)
			throws Exception {
		HttpSession session = request.getSession();
		String di = (String) session.getAttribute("DI");
		String sUsrId = (String) session.getAttribute("USR_ID");
		String sname = (String) session.getAttribute("NAME");
		String clientIp = (String) session.getAttribute("clientIp");

		if (di == null) {
			session.invalidate(); // 세션 만료
			throw new ResMsgException("본인인증 이 필요 합니다.");
		} else {
			Map<String, Object> loginInfo = mainMapper.getPoolInfo(di);
			String role = (String) loginInfo.get("ROLE");

			if (role == null || !"A".equals(role)) {
				session.invalidate(); // 세션 만료
				throw new ResMsgException("관리자만 사용 가능합니다.");
			}
		}

		Gson gson = new Gson();

		Type listType = new TypeToken<List<Map<String, Object>>>() {
		}.getType();
		List<Map<String, Object>> userList = gson.fromJson(checkedData, listType);
		
		if (userList == null || userList.isEmpty()) {
			HashMap<String, Object> resultMap = new HashMap<>();
			resultMap.put("result", "F"); 
			resultMap.put("message", "메일을 보낼 대상을 선택해 주세요.");

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(gson.toJson(resultMap));
			return;
		}
		
		 // 날짜 파싱을 위한 포맷 설정
		List<SimpleDateFormat> dateFormats = Arrays.asList(
		        new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH),
		        new SimpleDateFormat("MM월 dd, yyyy", Locale.KOREAN)
		);
		
	    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");

		for (Map<String, Object> user : userList) {

			String UNIV_NM = (String) user.get("UNIV_NM");
			String UNIV_CD = (String) user.get("UNIV_CD");
			String USR_ID = (String) user.get("USR_ID");
			String NAME = (String) user.get("NAME");
			String ROLE = (String) user.get("ROLE");
						
			String rawDate = (String) user.get("PRIV_DTTM");
		    String PRIV_DTTM = null;
		    
		    boolean parsed = false;  // 파싱 성공 여부를 체크할 플래그
		    for (SimpleDateFormat format : dateFormats) {
		        try {
		        	
		            Date date = format.parse(rawDate);
		            PRIV_DTTM = outputFormat.format(date);
		            
		            parsed = true;   // 파싱 성공
		            break;          
		        } catch (ParseException e) {
		        	
		        }
		    }
		    		    
			Map<String, Object> mailParam = new HashMap<String, Object>();
			mailParam.put("univNm", UNIV_NM);
			mailParam.put("univCd", UNIV_CD);
			mailParam.put("usrId", USR_ID);
			mailParam.put("usrNm", NAME);
			mailParam.put("usrEmail", USR_ID);
			mailParam.put("usrPerm", ROLE);
			mailParam.put("PRIV_DTTM", PRIV_DTTM);
			mailParam.put("callingMethod", "sendMailCertNum");

			scheduleService.sendJoinPwdMail(mailParam);
		}

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", "S");

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(gson.toJson(resultMap));
	}
	
    @RequestMapping(value="/pdfPreView.do", method= RequestMethod.GET)  
    public void pdfPreView(HttpServletRequest request, HttpServletResponse response)  throws Exception  {
		HttpSession session = request.getSession();
		String sname = (String) session.getAttribute("NAME");
		String clientIp = (String) session.getAttribute("clientIp");
    	
    	Map<String, Object> hisparam = new HashMap<String, Object>();
		hisparam.put("MENU_NO", "P0007");
		hisparam.put("MENU_NM", "공모 후보자 조회");
		hisparam.put("WORK_TYPE", "조회");
		hisparam.put("FUNC_NM", "공모 후보자 PDF 조회");
		hisparam.put("PRIVACY_YN", 'Y');
		hisparam.put("RMRK", "공모시스템 후보자  PDF 조회");
		hisparam.put("RESULT_TYPE", 'S');
		hisparam.put("sessUsrNm", sname);
		hisparam.put("usrIp", clientIp);
		cmmnMapper.insertHis(hisparam);
    	
    	String fileId = request.getParameter("fileId");
    	log.info("fileId: " + fileId);
    	
    	if(fileId == null) {
    		throw new ResMsgException("fileId 파라미터가 없습니다.");
    	} 
    	
    	String fileSeq = String.format("%04d", 1);
    	
    	Map<String,Object> fileParam = new HashMap<String,Object>();
    	fileParam.put("fileId", fileId);
    	fileParam.put("fileSeq", fileSeq);
    	
    	Map<String,Object> fileInfo = cmmnMapper.getFileInfo(fileParam);
    	if(fileInfo == null) {
    		throw new ResMsgException("삭제할 재직증명서 파일이 없습니다.");
    	}
    	
    	String filePath = (String) fileInfo.get("FILE_PATH");
    	String fileName = (String) fileInfo.get("FILE_NAME");
    	String orgFileName = (String) fileInfo.get("ORG_FILE_NAME");
    	
    	String fullPath = filePath + "/" + fileName;
    	String downloadFileName = "";
    	
    	
		log.info("fullPath: " + fullPath);
		File fFilePath = new File(fullPath);
		if(fFilePath.exists()) {
			
			String userAgent = request.getHeader("User-Agent"); 
			boolean ie = (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1 ); 
			if ( ie ) { 
				downloadFileName = new String(orgFileName.getBytes("windows-949"), "8859_1"); 
			} else if(userAgent.indexOf("Edge") > -1) {
				downloadFileName = new String( URLEncoder.encode(orgFileName, "UTF-8")); 
			} else { 
				downloadFileName = new String(orgFileName.getBytes("UTF-8"), "8859_1");  
			}
			
			response.setHeader("Content-Type" , "application/pdf");
			response.setHeader("Content-Disposition", "inline;filename=\"" + downloadFileName + "\";");				
			response.setHeader("Content-Transfer-Encoding" , "binary");	
			
			response.setHeader("Content-Length", String.valueOf(fFilePath.length()));
			response.setHeader("Pragma", "no-cache;") ; 
			response.setHeader("Expires", "-1") ;
			
			cmmnService.updateHis(request, "S");
			
			BufferedInputStream fin = null;
			BufferedOutputStream outs = null;
			try {
				fin = new BufferedInputStream(new FileInputStream(fFilePath));
				outs = new BufferedOutputStream(response.getOutputStream());
				byte[] buffer = new byte[8192];
				int readCnt = 0;
				while((readCnt = fin.read(buffer)) != -1) {
					outs.write(buffer, 0, readCnt);
				}
			} catch(ClientAbortException e) {
		
			} catch(Exception e) {
				throw e;
			} finally {			
				try {
					if(outs != null)
						outs.close();
					if(fin != null)
						fin.close();	
				} catch(IOException e) {
					log.error("클라이언트에 의한 취소");
				}
			}
			
		} else {
			ServletOutputStream out =response.getOutputStream();
			out.println("<script language=javascript>");
			String msg = new String("alert('파일이 존재하지 않습니다.');");
			out.write(msg.getBytes("UTF-8"));
			out.println("</script>");
		}
	}
}
