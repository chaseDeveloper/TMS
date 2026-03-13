package tms.adminSchedule.controller;



import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tms.main.util.KediAES256Util;
import tms.adminPool.maps.AdminPoolMapper;
import tms.adminSchedule.maps.AdminScheduleMapper;
import tms.cmmn.maps.CmmnMapper;
import tms.main.exception.ResMsgException;
import tms.main.maps.MainMapper;



@Controller
@RequestMapping("/adminSchedule")
public class AdminScheduleController {
	private Log log = LogFactory.getLog(this.getClass());
	
	
	 @Autowired MainMapper mainMapper;
	 
	 @Autowired CmmnMapper cmmnMapper;	
	 
	 @Autowired
	 MessageSource messageSource;
	 
	 @Autowired AdminScheduleMapper adminSchduleMapper;
	 
	 @Autowired AdminPoolMapper adminPoolMapper;	 
	
	
	 @Resource(name = "txManager")
	 private PlatformTransactionManager txManager;
	 
	 @RequestMapping("/adminScheduleList.do")
	 public String adminScheduleList(HttpServletRequest request,RedirectAttributes redirectAttributes) throws Exception {		 	
		  HttpSession session = request.getSession();
		  
		  String di = (String) session.getAttribute("DI");	
		  String clientIp = (String) session.getAttribute("clientIp");
		  //String currentIp = request.getRemoteAddr();
		  
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
					
					 if(role == null || !"A".equals(role)) {				
						 String errorMessage = messageSource.getMessage("msg_108", null, Locale.getDefault());
						 redirectAttributes.addFlashAttribute("message", errorMessage);
						 request.getSession().invalidate();
						 return "redirect:/main/main.do";
					 } 
					 
					 if (ip == null || clientIp == null ) {
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
		  
		  
		  List<Map<String,Object>> poolCdList = adminSchduleMapper.poolCdList();
		  
		  
		  request.setAttribute("poolCdList",poolCdList );
		  
		  return "/adminSchedule/adminScheduleView";
	 }	 
	
	 
	 @RequestMapping(value = "/getScheduleList.do")
	 public void getScheList(HttpServletRequest request,HttpServletResponse response) throws Exception {      	
		 HttpSession session = request.getSession();
			String di = (String) session.getAttribute("DI");		
			String usrId = (String) session.getAttribute("USR_ID");
			String sname = (String) session.getAttribute("NAME");
			String clientIp = (String) session.getAttribute("clientIp");

			if (di == null) {
				 session.invalidate();  // 세션 만료
				throw new ResMsgException("본인인증 이 필요 합니다.");
			}else {
				Map<String, Object> loginInfo = mainMapper.getPoolInfo(di);
				String role = (String) loginInfo.get("ROLE");
				
				 if(role == null || !"A".equals(role)) {	
					 session.invalidate();  // 세션 만료
					 throw new ResMsgException("관리자만 조회 가능합니다.");
				 } 
			}
			
		 HashMap<String,Object> resultMap = new HashMap<String,Object>();		 
		
		 List<Map<String,Object>> list= adminSchduleMapper.getAdminScheList();
		 resultMap.put("result", "S");
		 resultMap.put("list", list);
		 
		 if (clientIp != null && clientIp.contains(",")) {
			    // 쉼표(,)로 분리
			    String[] ipArray = clientIp.split(",");
			    // 첫 번째 IP만 Trim 해서 사용
			    clientIp = ipArray[0].trim();
		 }
		 
		 Map<String, Object> hisparam = new HashMap<String, Object>();
			hisparam.put("MENU_NO", "P0006");
			hisparam.put("MENU_NM", "공모 진단일정");
			hisparam.put("WORK_TYPE", "조회");
			hisparam.put("FUNC_NM", "공모 진단일정 조회");
			hisparam.put("PRIVACY_YN", 'N');
			hisparam.put("RMRK", "공모시스템 진단일정 조회");
			hisparam.put("RESULT_TYPE", 'S');
			hisparam.put("sessUsrNm", sname);
			hisparam.put("usrIp", clientIp);
			cmmnMapper.insertHis(hisparam);
 
		 
		Gson gson = new Gson();
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(gson.toJson(resultMap)); 
	}
	 
	@RequestMapping(value = "/insertData.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> insertData(MultipartHttpServletRequest request) throws ResMsgException, Exception {
		HttpSession session = request.getSession();
	 	 String di = (String) session.getAttribute("DI");
	 	 String susrId = (String) session.getAttribute("USR_ID");
	 	 String sname = (String) session.getAttribute("NAME");
		 String clientIp = (String) session.getAttribute("clientIp");
		 
		Map<String, Object> resultMap = new HashMap<String, Object>();		

		if (di == null) {
			 session.invalidate();  // 세션 만료
			throw new ResMsgException("본인인증 이 필요 합니다.");
		}else {
			Map<String, Object> loginInfo = mainMapper.getPoolInfo(di);
			String role = (String) loginInfo.get("ROLE");
			
			 if(role == null || !"A".equals(role)) {	
				 session.invalidate();  // 세션 만료
				 throw new ResMsgException("관리자만 입력 가능합니다.");
			 } 
		}
		
		Map<String, Object> hisparam = new HashMap<String, Object>();
		
		TransactionStatus txStatus = txManager.getTransaction(new DefaultTransactionDefinition());
		

		try {			
			
			String periodDivCd = request.getParameter("periodDivCd");  
			String evaYear = request.getParameter("evaYear");          
			String poolCd = request.getParameter("poolCd");            
			String poolCdNm = request.getParameter("poolNm");            
			String startDate = request.getParameter("startDate");      
			String startHour = request.getParameter("startHour");      
			String startMinute = request.getParameter("startMinute");  
			String endDate = request.getParameter("endDate");          
			String endHour = request.getParameter("endHour");          
			String endMinute = request.getParameter("endMinute");      
			String statusCd = request.getParameter("statusCd");        

			// 필수 필드 검증
			if (periodDivCd == null || periodDivCd.trim().isEmpty()) {
			    throw new ResMsgException("주기 구분 코드가 입력되지 않았습니다.");
			}
			if (evaYear == null || evaYear.trim().isEmpty()) {
			    throw new ResMsgException("진단 연도가 입력되지 않았습니다.");
			}
			if (poolCd == null || poolCd.trim().isEmpty()) {
			    throw new ResMsgException("공모분야 코드가 입력되지 않았습니다.");
			}
			/*if (poolNm == null || poolNm.trim().isEmpty()) {
			    throw new ResMsgException("공모분야 이름이 입력되지 않았습니다.");
			}*/
			if (startDate == null || startDate.trim().isEmpty()) {
			    throw new ResMsgException("시작 일시가 입력되지 않았습니다.");
			}
			if (startHour == null || startHour.trim().isEmpty()) {
			    throw new ResMsgException("시작 시간(시)이 입력되지 않았습니다.");
			}
			if (startMinute == null || startMinute.trim().isEmpty()) {
			    throw new ResMsgException("시작 시간(분)이 입력되지 않았습니다.");
			}
			if (endDate == null || endDate.trim().isEmpty()) {
			    throw new ResMsgException("종료 일시가 입력되지 않았습니다.");
			}
			if (endHour == null || endHour.trim().isEmpty()) {
			    throw new ResMsgException("종료 시간(시)이 입력되지 않았습니다.");
			}
			if (endMinute == null || endMinute.trim().isEmpty()) {
			    throw new ResMsgException("종료 시간(분)이 입력되지 않았습니다.");
			}
			if (statusCd == null || statusCd.trim().isEmpty()) {
			    throw new ResMsgException("운영 상태 코드가 입력되지 않았습니다.");
			}		
			
			startDate = startDate.replace("-", "");  // 'YYYYMMDD' 형식
			endDate = endDate.replace("-", "");      // 'YYYYMMDD' 형식

			// 시간과 분을 두 자리 수로 포맷팅
			String formattedStartHour = String.format("%02d", Integer.parseInt(startHour));
			String formattedStartMinute = String.format("%02d", Integer.parseInt(startMinute));
			String formattedEndHour = String.format("%02d", Integer.parseInt(endHour));
			String formattedEndMinute = String.format("%02d", Integer.parseInt(endMinute));

			// 최종 문자열 조합
			String startDateTime = startDate + formattedStartHour + formattedStartMinute;
			String endDateTime = endDate + formattedEndHour + formattedEndMinute;
			
			
			Map<String, Object> param = new HashMap<String, Object>();

			param.put("di", di);
			param.put("usrId", susrId);
			param.put("periodDivCd", periodDivCd);
			param.put("evaYear", evaYear);
			param.put("poolCd", poolCd);
			param.put("poolCdNm", poolCdNm);
			param.put("startDateTime", startDateTime);
			param.put("endDateTime", endDateTime);
			param.put("statusCd", statusCd);
			
			
			if(adminSchduleMapper.isExistPoolSchedule(param)){
				throw new ResMsgException("동일한 공모 일정이 존재 합니다.");
			}
			
			adminSchduleMapper.insertPoolSchedule(param);			

			txManager.commit(txStatus);

			resultMap.put("result", "S");
			hisparam.put("RESULT_TYPE", "S");
			hisparam.put("RMRK", "공모시스템 진단일정 저장 성공");

		} catch (Exception e) {

			txManager.rollback(txStatus);
			resultMap.put("result", "F");
			hisparam.put("RESULT_TYPE", "E");
			String errorMessage = "공모시스템 진단일정 저장 실패: " + e.getClass().getSimpleName() + " at " + e.getStackTrace()[0];
			hisparam.put("RMRK", errorMessage);
			
			throw e;
		} finally {

			if (clientIp != null && clientIp.contains(",")) {
			    // 쉼표(,)로 분리
			    String[] ipArray = clientIp.split(",");
			    // 첫 번째 IP만 Trim 해서 사용
			    clientIp = ipArray[0].trim();
		    }
			
			hisparam.put("MENU_NO", "P0006");
			hisparam.put("MENU_NM", "공모 진단일정");
			hisparam.put("WORK_TYPE", "저장");
			hisparam.put("FUNC_NM", "공모 진단일정");
			hisparam.put("PRIVACY_YN", 'N');
			hisparam.put("sessUsrNm", sname);
			hisparam.put("usrIp", clientIp);
			
			cmmnMapper.insertHis(hisparam);
		}

		return resultMap;
	} 
 	
	
	@RequestMapping(value = "/updateData.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateData(MultipartHttpServletRequest request) throws ResMsgException, Exception {
		HttpSession session = request.getSession();

		 String di = (String) session.getAttribute("DI");
		 String sUsrId = (String) session.getAttribute("USR_ID");		
		 String sname = (String) session.getAttribute("NAME");
		 String clientIp = (String) session.getAttribute("clientIp");
		 
		Map<String, Object> resultMap = new HashMap<String, Object>();
	
		
		if (di == null) {
			 session.invalidate();  // 세션 만료
			throw new ResMsgException("본인인증 이 필요 합니다.");
		}else {
			Map<String, Object> loginInfo = mainMapper.getPoolInfo(di);
			String role = (String) loginInfo.get("ROLE");
			
			 if(role == null || !"A".equals(role)) {	
				 session.invalidate();  // 세션 만료
				 throw new ResMsgException("관리자만 입력 가능합니다.");
			 } 
		}
		Map<String, Object> hisparam = new HashMap<String, Object>();
		
		TransactionStatus txStatus = txManager.getTransaction(new DefaultTransactionDefinition());		

		try {			
			
			String periodDivCd = request.getParameter("periodDivCd");  
			String evaYear = request.getParameter("evaYear");          
			String poolCd = request.getParameter("poolCd");            
			String poolCdNm = request.getParameter("poolNm");            
			String startDate = request.getParameter("startDate");      
			String startHour = request.getParameter("startHour");      
			String startMinute = request.getParameter("startMinute");  
			String endDate = request.getParameter("endDate");          
			String endHour = request.getParameter("endHour");          
			String endMinute = request.getParameter("endMinute");      
			String statusCd = request.getParameter("statusCd");        

			// 필수 필드 검증
			if (periodDivCd == null || periodDivCd.trim().isEmpty()) {
			    throw new ResMsgException("주기 구분 코드가 입력되지 않았습니다.");
			}
			if (evaYear == null || evaYear.trim().isEmpty()) {
			    throw new ResMsgException("진단 연도가 입력되지 않았습니다.");
			}
			if (poolCd == null || poolCd.trim().isEmpty()) {
			    throw new ResMsgException("공모분야 코드가 입력되지 않았습니다.");
			}
			/*if (poolNm == null || poolNm.trim().isEmpty()) {
			    throw new ResMsgException("공모분야 이름이 입력되지 않았습니다.");
			}*/
			if (startDate == null || startDate.trim().isEmpty()) {
			    throw new ResMsgException("시작 일시가 입력되지 않았습니다.");
			}
			if (startHour == null || startHour.trim().isEmpty()) {
			    throw new ResMsgException("시작 시간(시)이 입력되지 않았습니다.");
			}
			if (startMinute == null || startMinute.trim().isEmpty()) {
			    throw new ResMsgException("시작 시간(분)이 입력되지 않았습니다.");
			}
			if (endDate == null || endDate.trim().isEmpty()) {
			    throw new ResMsgException("종료 일시가 입력되지 않았습니다.");
			}
			if (endHour == null || endHour.trim().isEmpty()) {
			    throw new ResMsgException("종료 시간(시)이 입력되지 않았습니다.");
			}
			if (endMinute == null || endMinute.trim().isEmpty()) {
			    throw new ResMsgException("종료 시간(분)이 입력되지 않았습니다.");
			}
			if (statusCd == null || statusCd.trim().isEmpty()) {
			    throw new ResMsgException("운영 상태 코드가 입력되지 않았습니다.");
			}
			
			
			
			
			
			startDate = startDate.replace("-", "");  // 'YYYYMMDD' 형식
			endDate = endDate.replace("-", "");      // 'YYYYMMDD' 형식

			// 시간과 분을 두 자리 수로 포맷팅
			String formattedStartHour = String.format("%02d", Integer.parseInt(startHour));
			String formattedStartMinute = String.format("%02d", Integer.parseInt(startMinute));
			String formattedEndHour = String.format("%02d", Integer.parseInt(endHour));
			String formattedEndMinute = String.format("%02d", Integer.parseInt(endMinute));

			// 최종 문자열 조합
			String startDateTime = startDate + formattedStartHour + formattedStartMinute;
			String endDateTime = endDate + formattedEndHour + formattedEndMinute;
			
			
			Map<String, Object> param = new HashMap<String, Object>();

			param.put("di", di);
			param.put("usrId", sUsrId);
			param.put("periodDivCd", periodDivCd);
			param.put("evaYear", evaYear);
			param.put("poolCd", poolCd);
			param.put("poolCdNm", poolCdNm);
			param.put("startDateTime", startDateTime);
			param.put("endDateTime", endDateTime);
			param.put("statusCd", statusCd);			
				
			
			adminSchduleMapper.updatePoolSchedule(param);			

			txManager.commit(txStatus);

			resultMap.put("result", "S");
			hisparam.put("RESULT_TYPE", "S");
			hisparam.put("RMRK", "공모시스템 진단일정 수정 성공");

		} catch (Exception e) {

			txManager.rollback(txStatus);
			resultMap.put("result", "F");
			hisparam.put("RESULT_TYPE", "E");
			String errorMessage = "공모시스템 진단일정 수정 실패: "+ e.getClass().getSimpleName() + " at " + e.getStackTrace()[0];
			hisparam.put("RMRK", errorMessage);
			
			throw e;
		} finally {
			
			if (clientIp != null && clientIp.contains(",")) {
			    // 쉼표(,)로 분리
			    String[] ipArray = clientIp.split(",");
			    // 첫 번째 IP만 Trim 해서 사용
			    clientIp = ipArray[0].trim();
		 }

			hisparam.put("MENU_NO", "P0006");
			hisparam.put("MENU_NM", "공모 진단일정");
			hisparam.put("WORK_TYPE", "수정");
			hisparam.put("FUNC_NM", "공모 진단일정");
			hisparam.put("PRIVACY_YN", 'N');
			hisparam.put("sessUsrNm", sname);
			hisparam.put("usrIp", clientIp);
			
			cmmnMapper.insertHis(hisparam);
		}

		return resultMap;
	}
	
	@RequestMapping(value="/deleteData.do", method= RequestMethod.POST)    
    public void deleteData(HttpServletRequest request,HttpServletResponse response, @RequestParam String deleteData) throws Exception {
		HttpSession session = request.getSession();	    	

		String di = (String) session.getAttribute("DI");
		String sUsrId = (String) session.getAttribute("USR_ID");
		String sname = (String) session.getAttribute("NAME");
		String clientIp = (String) session.getAttribute("clientIp");
		 
		if (di == null) {
			 session.invalidate();  // 세션 만료
			throw new ResMsgException("본인인증 이 필요 합니다.");
		}else {
			Map<String, Object> loginInfo = mainMapper.getPoolInfo(di);
			String role = (String) loginInfo.get("ROLE");
			
			 if(role == null || !"A".equals(role)) {	
				 session.invalidate();  // 세션 만료
				 throw new ResMsgException("관리자만 입력 가능합니다.");
			 } 
		}   	
		
		Map<String, Object> hisparam = new HashMap<String, Object>();
		
		TransactionStatus txStatus = txManager.getTransaction(new DefaultTransactionDefinition());   	
    	
    	
    	Map<String,Object> resultMap = new HashMap<>();
    	Gson gson = new Gson();
    	
    	try {	    				
    		
	    	Type listType = new TypeToken<List<Map<String,Object>>>() {}.getType();		
	    	List<Map<String,Object>> delList = gson.fromJson(deleteData, listType);
	    	
	    	for(Map<String,Object> delMap : delList) {
	    		String periodDivCd = (String) delMap.get("PERIOD_DIV_CD");	
	    		String evaYear = (String) delMap.get("EVA_YEAR");	
	    		String poolCd = (String) delMap.get("POOL_CD");	
	    		
	    		if(periodDivCd == null || periodDivCd.trim().equals("")) {
	    			throw new ResMsgException("삭제할 데이터의 파라미터가 없습니다. 파라미터명: periodDivCd");
	    		}
	    		
	    		if(evaYear == null || evaYear.trim().equals("")) {
	    			throw new ResMsgException("삭제할 데이터의 파라미터가 없습니다. 파라미터명: evaYear");
	    		}
	    		
	    		if(poolCd == null || poolCd.trim().equals("")) {
	    			throw new ResMsgException("삭제할 데이터의 파라미터가 없습니다. 파라미터명: poolCd");
	    		}  	
	    		
	    		
	    		Map<String,Object> param = new HashMap<String, Object>();
	    		param.put("periodDivCd", periodDivCd);
	    		param.put("evaYear", evaYear);
	    		param.put("poolCd", poolCd);
	    		    		 	
	    		adminSchduleMapper.deletePoolSchedule(param);;
	    		
	    	} 		 
	    
	    	txManager.commit(txStatus);
	    	
	    	resultMap.put("result", "S");
			hisparam.put("RESULT_TYPE", "S");
			hisparam.put("RMRK", "공모시스템 진단일정 삭제 성공");

		} catch (Exception e) {

			txManager.rollback(txStatus);
			resultMap.put("result", "F");
			hisparam.put("RESULT_TYPE", "E");
			String errorMessage = "공모시스템 진단일정 삭제 실패: " + e.getClass().getSimpleName() + " at " + e.getStackTrace()[0];
			hisparam.put("RMRK", errorMessage);
			
			throw e;
		} finally {

			if (clientIp != null && clientIp.contains(",")) {
			    // 쉼표(,)로 분리
			    String[] ipArray = clientIp.split(",");
			    // 첫 번째 IP만 Trim 해서 사용
			    clientIp = ipArray[0].trim();
		 }
			
			hisparam.put("MENU_NO", "P0006");
			hisparam.put("MENU_NM", "공모 진단일정");
			hisparam.put("WORK_TYPE", "저장");
			hisparam.put("FUNC_NM", "공모 진단일정");
			hisparam.put("PRIVACY_YN", 'N');
			hisparam.put("sessUsrNm", sname);
			hisparam.put("usrIp", clientIp);
			
			cmmnMapper.insertHis(hisparam);
		}
    	
    	
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
    	response.getWriter().write(gson.toJson(resultMap));        
		
    }
	
	@RequestMapping(value = "/getSchedulePoolList.do")
	public void getSchedulePoolList(HttpServletRequest request,HttpServletResponse response) throws Exception {      	
		 
		HttpSession session = request.getSession();	    	

		String di = (String) session.getAttribute("DI");
		String sUsrId = (String) session.getAttribute("USR_ID");
		String sname = (String) session.getAttribute("NAME");
		String clientIp = (String) session.getAttribute("clientIp");
			

		if (di == null) {
			 session.invalidate();  // 세션 만료
			throw new ResMsgException("본인인증 이 필요 합니다.");
		}else {
			Map<String, Object> loginInfo = mainMapper.getPoolInfo(di);
			String role = (String) loginInfo.get("ROLE");
			
			 if(role == null || !"A".equals(role)) {	
				 session.invalidate();  // 세션 만료
				 throw new ResMsgException("관리자만 사용 가능합니다.");
			 } 
		}   			 
		
		
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		
		String periodDivCd = request.getParameter("periodDivCd");  
		String evaYear = request.getParameter("evaYear");          
		String poolCd = request.getParameter("poolCd");            
		
		if(periodDivCd == null || periodDivCd.trim().equals("")) {
			throw new ResMsgException("조회 데이터의 파라미터가 없습니다. 파라미터명: periodDivCd");
		}
		
		if(evaYear == null || evaYear.trim().equals("")) {
			throw new ResMsgException("조회 데이터의 파라미터가 없습니다. 파라미터명: evaYear");
		}
		
		if(poolCd == null || poolCd.trim().equals("")) {
			throw new ResMsgException("조회 데이터의 파라미터가 없습니다. 파라미터명: poolCd");
		}  	
		
		
		Map<String,Object> paramMap = new HashMap<String, Object>();
		 paramMap.put("role", "U");	
		 paramMap.put("periodDivCd", periodDivCd);
		 paramMap.put("evaYear", evaYear);
		 paramMap.put("poolCd", poolCd);
		 
		List<Map<String,Object>> list= adminSchduleMapper.getAdminPoolList(paramMap);			
		 
		KediAES256Util aes256Util = new KediAES256Util();
		
			
		if(paramMap != null){ // 조회조건 암호화
			String name = (String) paramMap.get("NAME");
			if(name != null && !"".equals(name)) paramMap.put("NAME", aes256Util.aesEncode(name));
			String gender = (String) paramMap.get("GENDER");
			if(gender != null && !"".equals(gender)) paramMap.put("GENDER", aes256Util.aesEncode(gender));
    		String telHp = (String) paramMap.get("TEL_HP");
			if(telHp != null && !"".equals(telHp)) paramMap.put("TEL_HP", aes256Util.aesEncode(telHp));
			String telHome = (String) paramMap.get("TEL_HOME");
			if(telHome != null && !"".equals(telHome)) paramMap.put("TEL_HOME", aes256Util.aesEncode(telHome));
			String birthday = (String) paramMap.get("BIRTHDAY");
			if(birthday != null && !"".equals(birthday)) paramMap.put("BIRTHDAY", aes256Util.aesEncode(birthday));
			String jobNm = (String) paramMap.get("JOB_NM");
			if(jobNm != null && !"".equals(jobNm)) paramMap.put("JOB_NM", aes256Util.aesEncode(jobNm));
			String position = (String) paramMap.get("POSITION");
			if(position != null && !"".equals(position)) paramMap.put("POSITION", aes256Util.aesEncode(position));
			String dept = (String) paramMap.get("DEPT");
			if(dept != null && !"".equals(dept)) paramMap.put("DEPT", aes256Util.aesEncode(dept));
			String telJob = (String) paramMap.get("TEL_JOB");
			if(telJob != null && !"".equals(telJob)) paramMap.put("TEL_JOB", aes256Util.aesEncode(telJob));
		}		 
		 
		 for(Map<String,Object> resultObj : list){ // 조회결과 복호화
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
			
			if(name != null && !"".equals(name)) name = aes256Util.aesDecode(name);
			if(gender != null && !"".equals(gender)) gender = aes256Util.aesDecode(gender);
			if(telHp != null && !"".equals(telHp)) telHp = aes256Util.aesDecode(telHp);
			if(telHome != null && !"".equals(telHome)) telHome = aes256Util.aesDecode(telHome);
			if(birthday != null && !"".equals(birthday)) birthday = aes256Util.aesDecode(birthday);
			if(addrHomeZipcode != null && !"".equals(addrHomeZipcode)) addrHomeZipcode = aes256Util.aesDecode(addrHomeZipcode);
			if(addrHome != null && !"".equals(addrHome)) addrHome = aes256Util.aesDecode(addrHome);
			if(addrHomeDetail != null && !"".equals(addrHomeDetail)) addrHomeDetail = aes256Util.aesDecode(addrHomeDetail);
			if(jobNm != null && !"".equals(jobNm)) jobNm = aes256Util.aesDecode(jobNm);
			if(position != null && !"".equals(position)) position = aes256Util.aesDecode(position);
			if(dept != null && !"".equals(dept)) dept = aes256Util.aesDecode(dept);
			if(email != null && !"".equals(email)) email = aes256Util.aesDecode(email);
			if(addrJobZipcode != null && !"".equals(addrJobZipcode)) addrJobZipcode = aes256Util.aesDecode(addrJobZipcode);
			if(addrJob != null && !"".equals(addrJob)) addrJob = aes256Util.aesDecode(addrJob);
			if(addrJobDetail != null && !"".equals(addrJobDetail)) addrJobDetail = aes256Util.aesDecode(addrJobDetail);
			if(telJob != null && !"".equals(telJob)) telJob = aes256Util.aesDecode(telJob);
			
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
		 
		 
		 log.info(list);
		 resultMap.put("result", "S");
		 resultMap.put("list", list);
		 
		 if (clientIp != null && clientIp.contains(",")) {
			    // 쉼표(,)로 분리
			    String[] ipArray = clientIp.split(",");
			    // 첫 번째 IP만 Trim 해서 사용
			    clientIp = ipArray[0].trim();
		 }
		 
		 Map<String, Object> hisparam = new HashMap<String, Object>();
			hisparam.put("MENU_NO", "P0006");
			hisparam.put("MENU_NM", "공모 지원자정보 조회");
			hisparam.put("WORK_TYPE", "조회");
			hisparam.put("FUNC_NM", "공모 지원자정보 조회");
			hisparam.put("PRIVACY_YN", 'Y');
			String remark = String.format("공모 진단일정 지원자정보 리스트 조회 - 주기: %s, 진단년도: %s, 공모코드: %s", periodDivCd, evaYear, poolCd);
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
	
	@RequestMapping(value = "/getPoolInfo.do")
	public void getList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();	    	

		String di = (String) session.getAttribute("DI");
		String sUsrId = (String) session.getAttribute("USR_ID");
		String sname = (String) session.getAttribute("NAME");
		String clientIp = (String) session.getAttribute("clientIp");		
		
		if (di == null) {
			 session.invalidate();  // 세션 만료
			throw new ResMsgException("본인인증 이 필요 합니다.");
		}else {
			Map<String, Object> loginInfo = mainMapper.getPoolInfo(di);
			String role = (String) loginInfo.get("ROLE");
			
			 if(role == null || !"A".equals(role)) {	
				 session.invalidate();  // 세션 만료
				 throw new ResMsgException("관리자만 사용 가능합니다.");
			 } 
		}   	

		KediAES256Util aes256Util = new KediAES256Util();
		
		String usrId = request.getParameter("usrId");
		String usrDi = request.getParameter("di");
		String periodDivCd = request.getParameter("periodDivCd");
		String evaYear = request.getParameter("evaYear");
		String poolCd = request.getParameter("poolCd");
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("di", usrDi);
		paramMap.put("usrId", usrId);
		paramMap.put("periodDivCd", periodDivCd);
		paramMap.put("evaYear", evaYear);
		paramMap.put("poolCd", poolCd);
		
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
			
			paramMap.put("disq_div", "1");
			List<Map<String, Object>> poolDisq1UnivList = adminPoolMapper.selectPoolDisqUnivList(paramMap);
			paramMap.put("disq_div", "2");
			List<Map<String, Object>> poolDisq2UnivList = adminPoolMapper.selectPoolDisqUnivList(paramMap);

			resultMap.put("poolDegreeList", poolDegreeList);
			resultMap.put("poolFulltimeTeacherList", poolFulltimeTeacherList);
			resultMap.put("poolEvaExp1List", poolEvaExp1List);
			resultMap.put("poolEvaExp2List", poolEvaExp2List);
			resultMap.put("poolDisq1UnivList", poolDisq1UnivList);
			resultMap.put("poolDisq2UnivList", poolDisq2UnivList);

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
		hisparam.put("MENU_NO", "P0006");
		hisparam.put("MENU_NM", "공모 지원자정보 상세조회");
		hisparam.put("WORK_TYPE", "조회");
		hisparam.put("FUNC_NM", "공모 지원자정보 상세조회");
		hisparam.put("PRIVACY_YN", 'Y');
		String remark = String.format("공모 진단일정 지원자정보 상세조회 - 대상자: %s, 조회자: %s", usrName, sname);
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
	
	@RequestMapping(value="/insYData.do", method= RequestMethod.POST)    
    public void insYData(HttpServletRequest request,HttpServletResponse response, @RequestParam String upData) throws Exception {
		HttpSession session = request.getSession();	    	

		String di = (String) session.getAttribute("DI");
		String sUsrId = (String) session.getAttribute("USR_ID");		
		String sname = (String) session.getAttribute("NAME");
		String clientIp = (String) session.getAttribute("clientIp");
		
		TransactionStatus txStatus = txManager.getTransaction(new DefaultTransactionDefinition());
    	
    	Gson gson = new Gson();
		Map<String, Object> hisparam = new HashMap<String, Object>();

    	Map<String,Object> resultMap = new HashMap<>();
    	try {	    	

	    			
			
			
			if (di == null) {
				 session.invalidate();  // 세션 만료
				throw new ResMsgException("본인인증 이 필요 합니다.");
			}else {
				Map<String, Object> loginInfo = mainMapper.getPoolInfo(di);
				String role = (String) loginInfo.get("ROLE");
				
				 if(role == null || !"A".equals(role)) {	
					 session.invalidate();  // 세션 만료
					 throw new ResMsgException("관리자만 입력 가능합니다.");
				 } 
			}   	
	    	
	    	Type listType = new TypeToken<List<Map<String,Object>>>() {}.getType();		
	    	List<Map<String,Object>> delList = gson.fromJson(upData, listType);
	    	
	    	for(Map<String,Object> delMap : delList) {
	    		String periodDivCd = (String) delMap.get("PERIOD_DIV_CD");	
	    		String evaYear = (String) delMap.get("EVA_YEAR");	
	    		String poolCd = (String) delMap.get("POOL_CD");	
	    		String usrDi = (String) delMap.get("DI");
	    		
	    		if(periodDivCd == null || periodDivCd.trim().equals("")) {
	    			throw new ResMsgException("업데이트할 데이터의 파라미터가 없습니다. 파라미터명: periodDivCd");
	    		}
	    		
	    		if(evaYear == null || evaYear.trim().equals("")) {
	    			throw new ResMsgException("업데이트할 데이터의 파라미터가 없습니다. 파라미터명: evaYear");
	    		}
	    		
	    		if(poolCd == null || poolCd.trim().equals("")) {
	    			throw new ResMsgException("업데이트할 데이터의 파라미터가 없습니다. 파라미터명: poolCd");
	    		}  	
	    		if(usrDi == null || usrDi.trim().equals("")) {
	    			throw new ResMsgException("업데이트할 데이터의 파라미터가 없습니다. 파라미터명: usrDi");
	    		}  	
	    		
	    		
	    		Map<String,Object> param = new HashMap<String, Object>();
	    		param.put("periodDivCd", periodDivCd);
	    		param.put("evaYear", evaYear);
	    		param.put("poolCd", poolCd);
	    		param.put("usrDi", usrDi);
	    		param.put("selRslt", 'Y');
	    		    		 	
	    		adminSchduleMapper.updataYNSelect(param);
	    		
	    	} 		 
	    
	    	resultMap.put("result","S");
	    	txManager.commit(txStatus);
	    	hisparam.put("RESULT_TYPE", "S");
			hisparam.put("RMRK", "공모시스템 위원선정 성공");
			
    	} catch(Exception e) {
			txManager.rollback(txStatus);
			resultMap.put("result", "F");
			hisparam.put("RESULT_TYPE", "E");
			String errorMessage = "공모시스템 위원선정 실패: " + e.getClass().getSimpleName() + " at " + e.getStackTrace()[0];
			hisparam.put("RMRK", errorMessage);
			
			throw e;
		} finally {

			if (clientIp != null && clientIp.contains(",")) {
			    // 쉼표(,)로 분리
			    String[] ipArray = clientIp.split(",");
			    // 첫 번째 IP만 Trim 해서 사용
			    clientIp = ipArray[0].trim();
		 }
			
			hisparam.put("MENU_NO", "P0006");
			hisparam.put("MENU_NM", "공모 위원 선정");
			hisparam.put("WORK_TYPE", "저장");
			hisparam.put("FUNC_NM", "공모 위원 선정");
			hisparam.put("PRIVACY_YN", 'N');
			hisparam.put("sessUsrNm", sname);
			hisparam.put("usrIp", clientIp);
			
			cmmnMapper.insertHis(hisparam);
		}
    	
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
    	response.getWriter().write(gson.toJson(resultMap));     
	}
	
	@RequestMapping(value="/insNData.do", method= RequestMethod.POST)    
    public void insNData(HttpServletRequest request,HttpServletResponse response, @RequestParam String upData) throws Exception {
    	
		HttpSession session = request.getSession();	    	

		String di = (String) session.getAttribute("DI");
		String sUsrId = (String) session.getAttribute("USR_ID");		
		String sname = (String) session.getAttribute("NAME");
		String clientIp = (String) session.getAttribute("clientIp");
		
		Map<String, Object> hisparam = new HashMap<String, Object>();
		
		TransactionStatus txStatus = txManager.getTransaction(new DefaultTransactionDefinition());
    	
    	Gson gson = new Gson();
    	
    	Map<String,Object> resultMap = new HashMap<>();
    	try {	    	

	    	
			if (di == null) {
				 session.invalidate();  // 세션 만료
				throw new ResMsgException("본인인증 이 필요 합니다.");
			}else {
				Map<String, Object> loginInfo = mainMapper.getPoolInfo(di);
				String role = (String) loginInfo.get("ROLE");
				
				 if(role == null || !"A".equals(role)) {	
					 session.invalidate();  // 세션 만료
					 throw new ResMsgException("관리자만 입력 가능합니다.");
				 } 
			}   	
	    	
	    	Type listType = new TypeToken<List<Map<String,Object>>>() {}.getType();		
	    	List<Map<String,Object>> delList = gson.fromJson(upData, listType);
	    	
	    	for(Map<String,Object> delMap : delList) {
	    		String periodDivCd = (String) delMap.get("PERIOD_DIV_CD");	
	    		String evaYear = (String) delMap.get("EVA_YEAR");	
	    		String poolCd = (String) delMap.get("POOL_CD");	
	    		String usrDi = (String) delMap.get("DI");
	    		
	    		if(periodDivCd == null || periodDivCd.trim().equals("")) {
	    			throw new ResMsgException("업데이트할 데이터의 파라미터가 없습니다. 파라미터명: periodDivCd");
	    		}
	    		
	    		if(evaYear == null || evaYear.trim().equals("")) {
	    			throw new ResMsgException("업데이트할 데이터의 파라미터가 없습니다. 파라미터명: evaYear");
	    		}
	    		
	    		if(poolCd == null || poolCd.trim().equals("")) {
	    			throw new ResMsgException("업데이트할 데이터의 파라미터가 없습니다. 파라미터명: poolCd");
	    		}  	
	    		if(usrDi == null || usrDi.trim().equals("")) {
	    			throw new ResMsgException("업데이트할 데이터의 파라미터가 없습니다. 파라미터명: usrDi");
	    		}  	
	    		
	    		
	    		Map<String,Object> param = new HashMap<String, Object>();
	    		param.put("periodDivCd", periodDivCd);
	    		param.put("evaYear", evaYear);
	    		param.put("poolCd", poolCd);
	    		param.put("usrDi", usrDi);
	    		param.put("selRslt", 'N');
	    		    		 	
	    		adminSchduleMapper.updataYNSelect(param);
	    		
	    	} 		 
	    
	    	resultMap.put("result","S");
	    	txManager.commit(txStatus);
	    	hisparam.put("RESULT_TYPE", "S");
			hisparam.put("RMRK", "공모시스템 위원미선정 성공");
    	} catch(Exception e) {
			txManager.rollback(txStatus);
			resultMap.put("result", "F");
			hisparam.put("RESULT_TYPE", "E");
			String errorMessage = "공모시스템 위원미선정 실패: " + e.getClass().getSimpleName() + " at " + e.getStackTrace()[0];
			hisparam.put("RMRK", errorMessage);
			
			throw e;
		} finally {

			if (clientIp != null && clientIp.contains(",")) {
			    // 쉼표(,)로 분리
			    String[] ipArray = clientIp.split(",");
			    // 첫 번째 IP만 Trim 해서 사용
			    clientIp = ipArray[0].trim();
		 }
			
			hisparam.put("MENU_NO", "P0006");
			hisparam.put("MENU_NM", "공모 위원 선정");
			hisparam.put("WORK_TYPE", "저장");
			hisparam.put("FUNC_NM", "공모 위원 선정");
			hisparam.put("PRIVACY_YN", 'N');
			hisparam.put("sessUsrNm", sname);
			hisparam.put("usrIp", clientIp);
			
			cmmnMapper.insertHis(hisparam);
		}
    	
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
    	response.getWriter().write(gson.toJson(resultMap));     
	}
	
	
	
	@RequestMapping("/poolPrintMain.do")	
	public String poolPrintMain(@RequestParam String periodDivCd, @RequestParam String evaYear, @RequestParam String poolCd, @RequestParam String di
														, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
		HttpSession session = request.getSession();	
				
		String sDi = (String) session.getAttribute("DI");
		String sUsrId = (String) session.getAttribute("USR_ID");		
		String sname = (String) session.getAttribute("NAME");
		String clientIp = (String) session.getAttribute("clientIp");
		
		KediAES256Util aes256Util = new KediAES256Util();	
		
		if (di == null) {
			 session.invalidate();  // 세션 만료
			throw new ResMsgException("본인인증 이 필요 합니다.");
		}else {
			Map<String, Object> loginInfo = mainMapper.getPoolInfo(di);
			String role = (String) loginInfo.get("ROLE");
			
			 if(role == null || !"A".equals(role)) {	
				 session.invalidate();  // 세션 만료
				 throw new ResMsgException("관리자만 입력 가능합니다.");
			 } 
		}	 
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("periodDivCd", periodDivCd);
		param.put("evaYear", evaYear);
		param.put("poolCd", poolCd);
		param.put("di", di);
		
		Map<String, Object> poolApply= adminSchduleMapper.getAdminPoolPrint(param);
		
		String poolCdNm = (String) poolApply.get("CODE_NM");
		String name = (String) poolApply.get("NAME");
		String univCdNm = (String) poolApply.get("UNIV_CD_NM");
		String position = (String) poolApply.get("POSITION");
		
        Date secPledgeYnDt = (Date) poolApply.get("SEC_PLEDGE_YN_DT");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String ynDt = dateFormat.format(secPledgeYnDt);
        
		String sign = (String) poolApply.get("SIGN_1");
		
		 if (position != null && !"".equals(position))
				position = aes256Util.aesDecode(position);		 
		
		
		model.addAttribute("periodDivCd", periodDivCd);
		model.addAttribute("evaYear", evaYear);
		model.addAttribute("poolCd", poolCd);
		model.addAttribute("poolCdNm",poolCdNm);
		model.addAttribute("name",name);
		model.addAttribute("univCdNm",univCdNm);
		model.addAttribute("position",position);
		model.addAttribute("ynDt",ynDt);
		model.addAttribute("sign",sign);
		
		if (clientIp != null && clientIp.contains(",")) {
		    // 쉼표(,)로 분리
		    String[] ipArray = clientIp.split(",");
		    // 첫 번째 IP만 Trim 해서 사용
		    clientIp = ipArray[0].trim();
	 }
		
		Map<String, Object> hisparam = new HashMap<String, Object>();
		hisparam.put("MENU_NO", "P0006");
		hisparam.put("MENU_NM", "공모 지원자정보 조회");
		hisparam.put("WORK_TYPE", "조회");
		hisparam.put("FUNC_NM", "공모 지원자정보 조회");
		hisparam.put("PRIVACY_YN", 'Y');
		String remark = String.format("공모 진단일정 지원자정보 사인정보 조회 - 주기: %s, 진단년도: %s, 공모코드: %s, 성명: %s", periodDivCd, evaYear, poolCd, name);
		hisparam.put("RMRK", remark);
		hisparam.put("RESULT_TYPE", 'S');
		hisparam.put("sessUsrNm", sname);
		hisparam.put("usrIp", clientIp);
		cmmnMapper.insertHis(hisparam);
		
		
		return "/adminSchedule/poolPrintMain";
	}
	
	@RequestMapping(value = "/exportData.do", method = RequestMethod.GET)
    public void exportData(String pq_filename, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	HttpSession ses = request.getSession(true);
    	String sname = (String) ses.getAttribute("NAME");
		String clientIp = (String) ses.getAttribute("clientIp");
		
        if ( ((String)ses.getAttribute("pq_filename")).equals(pq_filename) ) {
            String contents = (String) ses.getAttribute("pq_data");
            Boolean pq_decode = (Boolean) ses.getAttribute("pq_decode");
            String pq_ext = (String) ses.getAttribute("pq_ext");
            
            String paramFile = pq_filename.substring(0 ,pq_filename.lastIndexOf(pq_ext) -1 );
            String fileName = URLDecoder.decode(new String(Base64.decodeBase64(paramFile)), "UTF-8");            
            log.info("fileName: " + fileName + "." + pq_ext);
            

            byte[] bytes = pq_decode?  Base64.decodeBase64(contents): contents.getBytes(Charset.forName("UTF-8"));
            
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
            hisparam.put("MENU_NO", "P0006");
            hisparam.put("MENU_NM", "공모 지원자정보  리스트 조회");
            hisparam.put("WORK_TYPE", "조회");
            hisparam.put("FUNC_NM", "공모 지원자정보 리스트 조회");
            hisparam.put("PRIVACY_YN", 'Y');
            String remark = String.format("공모시스템 지원자정보 리스트 조회 - 파일 다운로드: %s, 다운로드: %s", fileName, sname);
            hisparam.put("RMRK", remark);
            hisparam.put("RESULT_TYPE", 'S');
            hisparam.put("sessUsrNm", sname);
            hisparam.put("usrIp", clientIp);
            cmmnMapper.insertHis(hisparam);
        }
        
    }
    
    @RequestMapping(value = "/exportData.do", method = RequestMethod.POST)
    public @ResponseBody
    String exportData(String pq_data, String pq_ext, Boolean pq_decode, String pq_filename, HttpServletRequest request) throws IOException {
        
        String[] arr = new String[] {"csv", "xlsx", "htm", "zip", "json"};        
        String filename = pq_filename + "." + pq_ext;

        if(Arrays.asList(arr).contains(pq_ext)){            
            HttpSession ses = request.getSession(true);
            ses.setAttribute("pq_data", pq_data);
            ses.setAttribute("pq_decode", pq_decode);
            ses.setAttribute("pq_filename", filename);
            ses.setAttribute("pq_ext", pq_ext);
        }
        return filename;
    }
}
