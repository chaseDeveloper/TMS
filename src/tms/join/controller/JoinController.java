package tms.join.controller;



import java.util.HashMap;
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

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import tms.main.util.KediAES256Util;
import tms.cmmn.maps.CmmnMapper;
import tms.join.maps.JoinMapper;
import tms.main.maps.MainMapper;



@Controller
@RequestMapping("/join")
public class JoinController {
	private Log log = LogFactory.getLog(this.getClass());
	
	
	 @Autowired MainMapper mainMapper;
	 	 
	 @Autowired
	 MessageSource messageSource;
	 
	 @Autowired JoinMapper joinMapper;
	 
	 @Autowired CmmnMapper cmmnMapper;	 
	 
	 @RequestMapping("/joinStep01.do")
	 public String joinStep01(HttpServletRequest request,RedirectAttributes redirectAttributes) throws Exception {		 	
		  HttpSession session = request.getSession();
		 		
		  
		  return "/join/joinStep01";
	 }
	 
	 @RequestMapping("/joinStep01Check.do")
	 public String joinStep01Check(HttpServletRequest request,HttpServletResponse response ,RedirectAttributes redirectAttributes) throws Exception {
	     HttpSession session = request.getSession();
	     String piAgreeChecked = request.getParameter("piAgreeChecked");	     
	     
	     
	     if ("1".equals(piAgreeChecked)) {
	         // 동의한 경우, 세션에 동의 값을 저장하고 joinStep02 페이지로 리다이렉트
	         session.setAttribute("piAgreeChecked", "1");
	         return "redirect:/join/joinStep02.do";
	         
	     } else {       
	    	 
	    	 String errorMessage = messageSource.getMessage("msg_101", null, Locale.getDefault());
	    	 redirectAttributes.addFlashAttribute("message", errorMessage);
	         return "redirect:/join/joinStep01.do";
	    	 
	     }
	 }   
	      
	 
	 	 
	 @RequestMapping("/joinStep02.do")
	 public String joinStep02(HttpServletRequest request,RedirectAttributes redirectAttributes) throws Exception {
		 HttpSession session = request.getSession();
		 String piAgreeChecked = (String) session.getAttribute("piAgreeChecked");
		 			 
		 if (!"1".equals(piAgreeChecked)) {
	         
			 String errorMessage = messageSource.getMessage("msg_101", null, Locale.getDefault());
			 redirectAttributes.addFlashAttribute("message", errorMessage);
	         return "redirect:/join/joinStep01.do";
	         
	     } else{
	    	 
	        return "/join/joinStep02";
	     }
		
	 }	 
	 
	 
	 @RequestMapping("/joinStep03.do")
	 public String joinStep03(HttpServletRequest request,RedirectAttributes redirectAttributes) throws Exception {
		  HttpSession session = request.getSession();
		  
		  KediAES256Util aes256Util = new KediAES256Util();		  
		  
		  String di = (String) session.getAttribute("DI");
		  String name = (String) session.getAttribute("NAME");	
		  String sname = (String) session.getAttribute("NAME");	
		  String birthDay = (String) session.getAttribute("BIRTHDAY");
		  String gender = (String) session.getAttribute("GENDER");
		  String telHp = (String) session.getAttribute("TEL_HP");		  
		  String clientIp = (String) session.getAttribute("clientIp");
		  
		  
		  if(name != null && !"".equals(name)) name = aes256Util.aesEncode(name);
		  if(birthDay != null && !"".equals(birthDay)) birthDay = aes256Util.aesEncode(birthDay);
		  if(gender != null && !"".equals(gender)) gender = aes256Util.aesEncode(gender);
		  if(telHp != null && !"".equals(telHp)) telHp = aes256Util.aesEncode(telHp);
		  
		  
		  if(di == null || di.length() != 64) {
			  String errorMessage = messageSource.getMessage("msg_102", null, Locale.getDefault());
			  redirectAttributes.addFlashAttribute("message", errorMessage);
			  return "redirect:/join/joinStep01.do";
		  }	
		  
		  Map<String,Object> param = new HashMap<String,Object>();	
		  param.put("di",di);
		  param.put("name",name);
		  param.put("birthDay",birthDay);
		  param.put("gender",gender);
		  param.put("telHp",telHp);
		 
		  Map<String,Object> hisparam = new HashMap<String,Object>();	
		  hisparam.put("MENU_NO","P0002");
		  hisparam.put("MENU_NM","공모 회원가입");
		  hisparam.put("WORK_TYPE","저장");
		  hisparam.put("FUNC_NM","공모 회원가입");		 
		  hisparam.put("PRIVACY_YN",'N');	 
		  
		  
		  //가입여부 체크
		  if(mainMapper.isExistPoolDi(di)) {
			  String errorMessage = messageSource.getMessage("msg_103", null, Locale.getDefault());
			  redirectAttributes.addFlashAttribute("message", errorMessage);
			  request.getSession().invalidate();
			  return "redirect:/main/main.do";
		  } else {
			  if(joinMapper.isExistPoolTelHp(param)){
				  
				  if (clientIp != null && clientIp.contains(",")) {
					    // 쉼표(,)로 분리
					    String[] ipArray = clientIp.split(",");
					    // 첫 번째 IP만 Trim 해서 사용
					    clientIp = ipArray[0].trim();
				 }
				  
				  Map<String, Object> info = joinMapper.selectPoolInfo(param);
				  String usrId = (String) info.get("USR_ID");
				  param.put("usrId",usrId);
				  param.put("sname",sname);
				  //성명,핸드폰번호 일치한데이터가 있으면 update	
				  joinMapper.updatePoolDegree(param);	
				  joinMapper.updatePoolFulltimeTeacher(param);	
				  joinMapper.updatePoolEvaExp(param);	
				  joinMapper.updatePoolInfo(param);	
				  hisparam.put("RMRK","공모시스템 26년 회원정보 업데이트 가입");
				  hisparam.put("RESULT_TYPE",'S');
				  hisparam.put("sessUsrNm",sname);		  
				  hisparam.put("usrIp",clientIp);
				  cmmnMapper.insertHis(hisparam);
				  
				  return "/join/joinStep03";
			  }else {
				  if (clientIp != null && clientIp.contains(",")) {
					    // 쉼표(,)로 분리
					    String[] ipArray = clientIp.split(",");
					    // 첫 번째 IP만 Trim 해서 사용
					    clientIp = ipArray[0].trim();
				 }
				  
				  param.put("sname",sname);
				  //성명,핸드폰번호 일치 데이터 없을시  insert	 
				  joinMapper.insertPoolInfo(param);
				  hisparam.put("RMRK","공모시스템 신규회원가입");
				  hisparam.put("RESULT_TYPE",'S');
				  hisparam.put("sessUsrNm",sname);		  
				  hisparam.put("usrIp",clientIp);
				  cmmnMapper.insertHis(hisparam);
				  
				  return "/join/joinStep03";
			  }
		  } 	  
	 }
	 
}
