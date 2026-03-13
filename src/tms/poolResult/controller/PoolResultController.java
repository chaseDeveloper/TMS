package tms.poolResult.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import tms.cmmn.maps.CmmnMapper;
import tms.main.exception.ResMsgException;
import tms.main.maps.MainMapper;
import tms.main.util.KediAES256Util;
import tms.poolResult.maps.PoolResultMapper;

@Controller
@RequestMapping("/poolResult")
public class PoolResultController {
	private Log log = LogFactory.getLog(this.getClass());

	@Autowired
	MainMapper mainMapper;	

	@Autowired
	PoolResultMapper poolResultMapper;
	
	@Autowired
	CmmnMapper cmmnMapper;	
	
	@Autowired
	MessageSource messageSource;

	@Resource(name = "txManager")
	private PlatformTransactionManager txManager;
	

	@RequestMapping("/poolResult.do")
	public String poolResult(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
		HttpSession session = request.getSession();
		
		String di = (String) session.getAttribute("DI");
		String name = (String) session.getAttribute("NAME");	
		
		if (di == null || di.length() != 64) {
			String errorMessage = messageSource.getMessage("msg_102", null, Locale.getDefault());
			redirectAttributes.addFlashAttribute("message", errorMessage);
			return "redirect:/main/main.do";
		} else {
			if (mainMapper.isExistPoolDi(di)) {

				Map<String, Object> loginInfo = mainMapper.getPoolInfo(di);
				List<Map<String, Object>> degrees = mainMapper.getPoolDegreeInfo(di); 
				List<Map<String, Object>> fulltimes = mainMapper.getPoolFulltimeInfo(di); 

				String usrId = (String) loginInfo.get("USR_ID");
				String role = (String) loginInfo.get("ROLE");
				String univCd = (String) loginInfo.get("UNIV_CD");
				
				 session.setAttribute("ROLE",role);
				 
				boolean validDegrees = true;
				for (Map<String, Object> degree : degrees) {
					 String degCd = (String) degree.get("degCd");
			        	if ("1".equals(degCd)) {
				            if (degree.get("UNIV_CD") == null || ((String) degree.get("UNIV_CD")).isEmpty()) {
				                validDegrees = false;
				                break;
				            }
			        	}    
				}

				boolean validFulltimes = true;
				if (!fulltimes.isEmpty()) {
					for (Map<String, Object> fulltime : fulltimes) {
						// UNIV_CD가 null이거나 비어있는지 확인
						if (fulltime.get("UNIV_CD") == null || ((String) fulltime.get("UNIV_CD")).isEmpty()) {
							validFulltimes = false;
							break;
						}
					}
				} else {
					// 데이터가 없으면 validFulltimes를 true로 유지하거나, 필요에 따라 다른 처리를 할 수
					// 있음
					validFulltimes = true; // 데이터가 없을 경우에 대한 기본 처리
				}

				if (usrId != null && univCd != null && validDegrees && validFulltimes) {
					session.setAttribute("USR_ID", usrId);

				} else {
					String errorMessage = messageSource.getMessage("msg_107", null, Locale.getDefault());
					redirectAttributes.addFlashAttribute("message", errorMessage);
					return "redirect:/pool/poolInfo.do";

				}

			} else {

				String errorMessage = messageSource.getMessage("msg_105", null, Locale.getDefault());
				redirectAttributes.addFlashAttribute("message", errorMessage);
				request.getSession().invalidate();
				return "redirect:/join/joinStep01.do";
			}
		}

		return "/poolResult/poolResult";
	}

	@RequestMapping(value = "/getPoolResultList.do")
	public void poolResultList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		String di = (String) session.getAttribute("DI");		
		String usrId = (String) session.getAttribute("USR_ID");
		String sname = (String) session.getAttribute("NAME");
		String clientIp = (String) session.getAttribute("clientIp");

		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("di", di);
		paramMap.put("usrId", usrId);

		List<Map<String, Object>> list = poolResultMapper.selectPoolResultList(paramMap);
		resultMap.put("result", "S");
		resultMap.put("list", list);

		if (clientIp != null && clientIp.contains(",")) {
		    // 쉼표(,)로 분리
		    String[] ipArray = clientIp.split(",");
		    // 첫 번째 IP만 Trim 해서 사용
		    clientIp = ipArray[0].trim();
	 }
		
		Map<String, Object> hisparam = new HashMap<String, Object>();
		hisparam.put("MENU_NO", "P0005");
		hisparam.put("MENU_NM", "공모 선정결과 확인");
		hisparam.put("WORK_TYPE", "조회");
		hisparam.put("FUNC_NM", "공모 선정결과 확인");
		hisparam.put("PRIVACY_YN", 'N');
		hisparam.put("RMRK", "공모시스템 선정결과 확인 화면 조회");
		hisparam.put("RESULT_TYPE", 'S');
		hisparam.put("sessUsrNm", sname);
		hisparam.put("usrIp", clientIp);
		cmmnMapper.insertHis(hisparam);

		Gson gson = new Gson();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(gson.toJson(resultMap));

	}

	@RequestMapping("/poolPrintMain.do")	
	public String poolPrintMain(@RequestParam("listData") String listData, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
		HttpSession session = request.getSession();
		String di = (String) session.getAttribute("DI");		
		
		KediAES256Util aes256Util = new KediAES256Util();	
		
		ObjectMapper objectMapper = new ObjectMapper();
	    Map<String, Object> mapData = objectMapper.readValue(listData, new TypeReference<Map<String, Object>>() {});
		
	    Map<String, Object> listDataMap = (Map<String, Object>) mapData.get("listData");
	    
	    String periodDivCd = (String) listDataMap.get("PERIOD_DIV_CD"); 
	    String evaYear = (String) listDataMap.get("EVA_YEAR");	    
	    String poolCd = (String) listDataMap.get("POOL_CD");
	    //String poolCdNm = (String) listDataMap.get("POOL_CD_NM");	    
		String name = (String) listDataMap.get("NAME");
		String sign = (String) listDataMap.get("SIGN_1");
		
		if (di == null || di.length() != 64) {
			String errorMessage = messageSource.getMessage("msg_102", null, Locale.getDefault());
			redirectAttributes.addFlashAttribute("message", errorMessage);
			return "redirect:/main/main.do";
		} else {
			if (mainMapper.isExistPoolDi(di)) {

				Map<String, Object> loginInfo = mainMapper.getPoolInfo(di);
				String usrId = (String) loginInfo.get("USR_ID");
				
		
				if (usrId != null) {
					session.setAttribute("USR_ID", usrId);

				} else {

					String errorMessage = messageSource.getMessage("msg_107", null, Locale.getDefault());
					redirectAttributes.addFlashAttribute("message", errorMessage);
					return "redirect:/pool/poolInfo.do";

				}

			} else {

				String errorMessage = messageSource.getMessage("msg_105", null, Locale.getDefault());
				redirectAttributes.addFlashAttribute("message", errorMessage);
				request.getSession().invalidate();
				return "redirect:/join/joinStep01.do";
			}
		}
		
		Map<String, Object> loginInfo = mainMapper.getPoolInfo(di);		
		 String position = (String) loginInfo.get("POSITION");
		 String univCdNm = (String) loginInfo.get("UNIV_CD_NM");
		 
		 if (position != null && !"".equals(position))
				position = aes256Util.aesDecode(position);
		 
		Map<String,Object> param = new HashMap<String, Object>();
		 param.put("poolCd",poolCd);
		
		Map<String, Object> map= poolResultMapper.getpoolCd(param);
		 String poolCdNm = (String) map.get("CODE_NM");
		
		model.addAttribute("periodDivCd", periodDivCd);
		model.addAttribute("evaYear", evaYear);
		model.addAttribute("poolCd", poolCd);
		model.addAttribute("poolCdNm",poolCdNm);
		model.addAttribute("position", position);
		model.addAttribute("univCdNm", univCdNm);
		model.addAttribute("name",name);
		model.addAttribute("sign",sign);
		
		//log.info(sign);
		
		return "/poolResult/poolPrintMain";
	}

	
	
	@RequestMapping(value = "/sign.do", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> sign_01(@RequestBody Map<String, Object> param,HttpServletRequest request) throws ResMsgException, Exception {
		 
		 	HttpSession session = request.getSession();
		 	 String di = (String) session.getAttribute("DI");
		 	 String usrId = (String) session.getAttribute("USR_ID");
		 	 String sname = (String) session.getAttribute("NAME");
			 String clientIp = (String) session.getAttribute("clientIp");

		 	 String periodDivCd = (String)param.get("periodDivCd");
			 String evaYear = (String)param.get("evaYear");
			 String poolCd = (String)param.get("poolCd");		
			 String sign1 = (String)param.get("sign1");
			
			 Map<String, Object> resultMap = new HashMap<String, Object>();
			 
			 
			 
			if (di == null || di.trim().equals("")) {
				throw new ResMsgException("본인인증데이터가 없습니다.");
			} 
			 
			if (sign1 == null || sign1.trim().equals("")) {
				throw new ResMsgException("사인데이터가 없습니다.");
			}
			
			if (periodDivCd == null || periodDivCd.trim().equals("")) {
				throw new ResMsgException("주기데이터가 없습니다.");
			}
			
			if (evaYear == null || evaYear.trim().equals("")) {
				throw new ResMsgException("진단년도데이터가 없습니다.");
			}
			 			
			if (poolCd == null || poolCd.trim().equals("")) {
				throw new ResMsgException("공모코드 데이터 가 없습니다.");
			}					
			 
			Map<String, Object> hisparam = new HashMap<String, Object>();
			
			 TransactionStatus txStatus = txManager.getTransaction(new DefaultTransactionDefinition());		
			 try {
				
				 HashMap<String,Object> paramMap = new HashMap<String,Object> ();
			 	 	paramMap.put("usrId", usrId);
			 	 	paramMap.put("di", di);
			 	 	paramMap.put("periodDivCd", periodDivCd);
			 	 	paramMap.put("evaYear", evaYear);
			 	 	paramMap.put("poolCd", poolCd);
			 	 	paramMap.put("sign1", sign1);			 	 
			 	 	
			 	 	
			 	 	poolResultMapper.saveSign(paramMap);				
				
					
					txManager.commit(txStatus);
						
					resultMap.put("result", "S");
					resultMap.put("sign1", param.get("sign1"));				
					hisparam.put("RESULT_TYPE", "S");
					hisparam.put("RMRK", "공모시스템 선정결과확인 사인데이터저장 성공");
				
				} catch(Exception e) {
					
					txManager.rollback(txStatus);
					resultMap.put("result", "F");
					hisparam.put("RESULT_TYPE", "E");
					String errorMessage = "공모시스템 선정결과확인 사인데이터저장 실패: " + e.getClass().getSimpleName() + " at " + e.getStackTrace()[0];
					hisparam.put("RMRK", errorMessage);
					
					throw e;
					
				} finally {
					if (clientIp != null && clientIp.contains(",")) {
					    // 쉼표(,)로 분리
					    String[] ipArray = clientIp.split(",");
					    // 첫 번째 IP만 Trim 해서 사용
					    clientIp = ipArray[0].trim();
				 }

					hisparam.put("MENU_NO", "P0005");
					hisparam.put("MENU_NM", "공모 선정결과확인");
					hisparam.put("WORK_TYPE", "저장");
					hisparam.put("FUNC_NM", "공모 선정결과확인");
					hisparam.put("PRIVACY_YN", 'N');
					hisparam.put("sessUsrNm", sname);
					hisparam.put("usrIp", clientIp);
					
					cmmnMapper.insertHis(hisparam);
				}
				
				return resultMap;
			
	 
	}
}
