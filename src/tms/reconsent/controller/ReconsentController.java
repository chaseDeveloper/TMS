package tms.reconsent.controller;



import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import tms.reconsent.maps.ReconsentMapper;
import tms.main.exception.ResMsgException;
import tms.cmmn.maps.CmmnMapper;
import tms.main.maps.MainMapper;


@Controller
@RequestMapping("/reconsent")
public class ReconsentController {
	private Log log = LogFactory.getLog(this.getClass());
	
	
	 @Autowired MainMapper mainMapper;
	
	 @Autowired
	 CmmnMapper cmmnMapper; 
	 
	 @Autowired
	 MessageSource messageSource;
	 
	 @Autowired ReconsentMapper reconsentMapper;
	 
	 @Resource(name = "txManager")
	 private PlatformTransactionManager txManager;
	 
	 @RequestMapping("/reconsentView.do")
	 public String joinStep01(HttpServletRequest request,RedirectAttributes redirectAttributes) throws Exception {		 	
		  HttpSession session = request.getSession();
		  
		  String di = (String) session.getAttribute("DI");
		  String usrId = (String) session.getAttribute("USR_ID");
		  String sname = (String) session.getAttribute("NAME");
		  String clientIp = (String) session.getAttribute("clientIp");

		  
		  if (di == null || di.length() != 64) {
				String errorMessage = messageSource.getMessage("msg_102", null, Locale.getDefault());
				redirectAttributes.addFlashAttribute("message", errorMessage);
				return "redirect:/main/main.do";
		  }
		  
		  
		  Map<String, Object> loginInfo = mainMapper.getPoolInfo(di);
		  Object privDttmObject = loginInfo.get("PRIV_DTTM");

		  // 날짜 객체를 안전하게 처리하기 위한 코드
		  Timestamp privDttmTimestamp;
		  if (privDttmObject instanceof Timestamp) {
		      privDttmTimestamp = (Timestamp) privDttmObject;
		  } else if (privDttmObject instanceof java.sql.Date) {
		      privDttmTimestamp = new Timestamp(((java.sql.Date) privDttmObject).getTime());
		  } else {
		      throw new IllegalArgumentException("Unsupported date object type");
		  }
		  
		  
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		  String formattedPrivDttm = sdf.format(privDttmTimestamp);
		  
		  request.setAttribute("formattedPrivDttm",formattedPrivDttm );
		  
		  if (clientIp != null && clientIp.contains(",")) {
			    // 쉼표(,)로 분리
			    String[] ipArray = clientIp.split(",");
			    // 첫 번째 IP만 Trim 해서 사용
			    clientIp = ipArray[0].trim();
		 }
		  
		  Map<String, Object> hisparam = new HashMap<String, Object>();
			hisparam.put("MENU_NO", "P0006");
			hisparam.put("MENU_NM", "공모 개인정보처리 재동의");
			hisparam.put("WORK_TYPE", "조회");
			hisparam.put("FUNC_NM", "공모 선정결과 확인");
			hisparam.put("PRIVACY_YN", 'N');
			hisparam.put("RMRK", "공모시스템 개인정보처리 재동의");
			hisparam.put("RESULT_TYPE", 'S');
			hisparam.put("sessUsrNm", sname);
			hisparam.put("usrIp", clientIp);
			cmmnMapper.insertHis(hisparam);
			
		  return "/reconsent/reconsentView";
	 }
	 
	 @RequestMapping(value = "/updateData.do", method = RequestMethod.POST)
		@ResponseBody
		public Map<String, Object> updateData(MultipartHttpServletRequest request) throws ResMsgException, Exception {

			Map<String, Object> resultMap = new HashMap<String, Object>();
		
			HttpSession session = request.getSession();

			String di = (String) session.getAttribute("DI");
			String usrId = (String) session.getAttribute("USR_ID");
		 	String sname = (String) session.getAttribute("NAME");
			String clientIp = (String) session.getAttribute("clientIp");

			 
			if (di == null) {
				throw new ResMsgException("본인인증 이 필요 합니다.");
			}
			
			Map<String, Object> hisparam = new HashMap<String, Object>();
			
			TransactionStatus txStatus = txManager.getTransaction(new DefaultTransactionDefinition());
			

			try {			
				
				String piAgree = request.getParameter("piAgree");  
				
				if (piAgree == null || piAgree.trim().isEmpty() || !piAgree.trim().equals("Y")) {
				    throw new ResMsgException("약관에 동의해야 개인정보처리 재동의가 가능합니다.");
				}
				
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("di", di);				
				
				reconsentMapper.updatePoolPrivYn(param);			

				txManager.commit(txStatus);
				resultMap.put("result", "S");
				hisparam.put("RESULT_TYPE", "S");
				hisparam.put("RMRK", "공모시스템 개인정보처리 재동의 저장 성공");

			} catch (Exception e) {

				txManager.rollback(txStatus);
				resultMap.put("result", "F");
				hisparam.put("RESULT_TYPE", "E");
				String errorMessage = "공모시스템 개인정보처리 재동의 저장 실패: " + e.getClass().getSimpleName() + " at " + e.getStackTrace()[0];
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
				hisparam.put("MENU_NM", "공모 개인정보처리 재동의");
				hisparam.put("WORK_TYPE", "저장");
				hisparam.put("FUNC_NM", "공모 개인정보처리 재동의");
				hisparam.put("PRIVACY_YN", 'N');
				hisparam.put("sessUsrNm", sname);
				hisparam.put("usrIp", clientIp);
				
				cmmnMapper.insertHis(hisparam);
			}

			return resultMap;
		}
	 	 

	 
}
