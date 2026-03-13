package tms.poolSbmt.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import tms.cmmn.maps.CmmnMapper;
import tms.main.exception.ResMsgException;
import tms.main.maps.MainMapper;
import tms.poolSbmt.maps.PoolSbmtMapper;

@Controller
@RequestMapping("/poolSbmt")
public class PoolSbmtController {
	private Log log = LogFactory.getLog(this.getClass());

	@Autowired
	MainMapper mainMapper;
	
	@Autowired
	PoolSbmtMapper poolSbmtMapper;
	
	@Autowired
	CmmnMapper cmmnMapper;
	
	@Autowired
	MessageSource messageSource;

	@Resource(name = "txManager")
	private PlatformTransactionManager txManager;
	

	@RequestMapping("/poolSbmtList.do")
	public String poolSbmtList(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
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
				String univCd = (String) loginInfo.get("UNIV_CD");
				
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
		            // 데이터가 없으면 validFulltimes를 true로 유지하거나, 필요에 따라 다른 처리를 할 수 있음
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

		return "/poolSbmt/poolSbmtList";
	}

	@RequestMapping(value = "/getpoolSbmList.do")
	public void poolSbmList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		String di = (String) session.getAttribute("DI");
		String usrId = (String) session.getAttribute("USR_ID");
		String sname = (String) session.getAttribute("NAME");
		String clientIp = (String) session.getAttribute("clientIp");
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("di", di);
		paramMap.put("usrId", usrId);
		
		String jobCd = poolSbmtMapper.selectUsrJobCd(paramMap);
		
		List<Map<String, Object>> list;
		
		if ("1".equals(jobCd)) {
			list = poolSbmtMapper.selectPoolApplList1(paramMap);
		}else{
			list = poolSbmtMapper.selectPoolApplList2(paramMap);
		}
		
		resultMap.put("result", "S");
		resultMap.put("list", list);

		//log.info(list);

		if (clientIp != null && clientIp.contains(",")) {
		    // 쉼표(,)로 분리
		    String[] ipArray = clientIp.split(",");
		    // 첫 번째 IP만 Trim 해서 사용
		    clientIp = ipArray[0].trim();
	 }
		
		Map<String, Object> hisparam = new HashMap<String, Object>();
		hisparam.put("MENU_NO", "P0004");
		hisparam.put("MENU_NM", "공모 신청서 제출");
		hisparam.put("WORK_TYPE", "조회");
		hisparam.put("FUNC_NM", "공모 신청서 제출");
		hisparam.put("PRIVACY_YN", 'N');
		hisparam.put("RMRK", "공모시스템 신청서 제출 화면 조회");
		hisparam.put("RESULT_TYPE", 'S');
		hisparam.put("sessUsrNm", sname);
		hisparam.put("usrIp", clientIp);
		cmmnMapper.insertHis(hisparam);

		Gson gson = new Gson();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(gson.toJson(resultMap));

	}
	
	
	@RequestMapping(value = "/getDisqUnivInfo.do")
	public void getDisqUnivList(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> listData) throws Exception {
		HttpSession session = request.getSession();
		String di = (String) session.getAttribute("DI");
		String usrId = (String) session.getAttribute("USR_ID");
		String periodDivCd = (String) listData.get("PERIOD_DIV_CD");
		String evaYear = (String) listData.get("EVA_YEAR");
		String poolCd = (String) listData.get("POOL_CD");
		String disqYn1 = (String) listData.get("DISQ_YN_1");
		String disqYn2 = (String) listData.get("DISQ_YN_2");

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("di", di);
		paramMap.put("usrId", usrId);
		paramMap.put("periodDivCd", periodDivCd);
		paramMap.put("evaYear", evaYear);
		paramMap.put("poolCd", poolCd);

		if (usrId != null) {

			if ("Y".equals(disqYn1)) {

				paramMap.put("disq_div", "1");
				List<Map<String, Object>> poolDisq1UnivList = poolSbmtMapper.selectPoolDisqUnivList(paramMap);
				resultMap.put("poolDisq1UnivList", poolDisq1UnivList);
				log.info(poolDisq1UnivList);
			}

			if ("Y".equals(disqYn2)) {

				paramMap.put("disq_div", "2");
				List<Map<String, Object>> poolDisq2UnivList = poolSbmtMapper.selectPoolDisqUnivList(paramMap);
				resultMap.put("poolDisq2UnivList", poolDisq2UnivList);
				log.info(poolDisq2UnivList);
			}

		}

		resultMap.put("result", "S");

		Gson gson = new Gson();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(gson.toJson(resultMap));
	}

	@RequestMapping(value = "/poolSubmitData.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> poolSubmitData(HttpServletResponse response, HttpServletRequest request) throws ResMsgException, Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		HttpSession session = request.getSession();

		String di = (String) session.getAttribute("DI");
		String usrId = (String) session.getAttribute("USR_ID");
		String name = (String) session.getAttribute("NAME");
		String sname = (String) session.getAttribute("NAME");
		String clientIp = (String) session.getAttribute("clientIp");
		
		if (di == null) {
			throw new ResMsgException("본인인증 이 필요 합니다.");
		}
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("di", di);
		
		String jobCd = poolSbmtMapper.selectUsrJobCd(paramMap);
		String fileId = poolSbmtMapper.selectUsrFileId(paramMap);

		

		
		Map<String, Object> hisparam = new HashMap<String, Object>();

		TransactionStatus txStatus = txManager.getTransaction(new DefaultTransactionDefinition());
		
		List<Map<String, Object>> disq1UnivDataList = new ObjectMapper()
				.readValue(request.getParameter("disq1UnivDataList"), new TypeReference<List<Map<String, Object>>>() {
				});
		List<Map<String, Object>> disq2UnivDataList = new ObjectMapper()
				.readValue(request.getParameter("disq2UnivDataList"), new TypeReference<List<Map<String, Object>>>() {
				});
		
		try {

			
				String periodDivCd = request.getParameter("periodDivCd");
				String evaYear = request.getParameter("evaYear");
				String poolCd = request.getParameter("poolCd");
				String disq1Univ = request.getParameter("disq1Univ");
				String disq2Univ = request.getParameter("disq2Univ");
				
				if ("2".equals(jobCd) && !(poolCd.equals("1") || poolCd.equals("3") || poolCd.equals("5"))) {
				    throw new ResMsgException("교직원은 정성진단에 제출 할 수 없습니다.");
				}

				if(fileId == null || fileId.isEmpty()){
					throw new ResMsgException("재직증명서가 누락되었습니다.\n신청서 작성에서 재직증명서를 등록해주세요.");
				}

				if (periodDivCd == null || periodDivCd.trim().equals("")) {
					throw new ResMsgException("주기 데이타가 없습니다.");
				}

				if (evaYear == null || evaYear.trim().equals("")) {
					throw new ResMsgException("진단년도 데이타가 없습니다.");
				}

				if (poolCd == null || poolCd.trim().equals("")) {
					throw new ResMsgException("공모계획 코드가 데이타가 없습니다.");
				}

				if (disq1Univ == null || disq1Univ.trim().equals("")) {
					throw new ResMsgException("기피제척대상 의 직계가족 또는 배우자 사유 선택 값 이 없습니다.");
				}

				if (disq2Univ == null || disq2Univ.trim().equals("")) {
					throw new ResMsgException("기피제척대상 기타 사유 선택 값 이 없습니다.");
				}

				
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("di", di);
				param.put("periodDivCd", periodDivCd);
				param.put("evaYear", evaYear);
				param.put("poolCd", poolCd);
				param.put("usrId", usrId);
				param.put("name", name);
				param.put("applYn", "Y");
				param.put("disq1Univ", disq1Univ);
				param.put("disq2Univ", disq2Univ);
				
				Map<String, Object> Info = poolSbmtMapper.getScheduleInfo(param);
				String poolCdNm = (String) Info.get("POOL_CD_NM");
				
				param.put("poolCdNm", poolCdNm);
				
				poolSbmtMapper.updatePoolDisqYNInfo(param);
				poolSbmtMapper.deletePoolDisqUnivList(param);
				
				if ("Y".equals(disq1Univ)) {
					if (disq1UnivDataList.size() > 0) {
						int line = 1;
						for (Map<String, Object> disq1Univ1Map : disq1UnivDataList) {

							String disqUnicCd = (String) disq1Univ1Map.get("disq1UnivCd");

							if (disqUnicCd == null || disqUnicCd.trim().equals("")) {

								throw new ResMsgException("기피제척대상 의 직계가족 또는 배우자 사유 대학 데이터" + line + "행 의 대학코드 데이터가 없습니다.");
							}

							disq1Univ1Map.put("di", di);
							disq1Univ1Map.put("usrId", usrId);
							disq1Univ1Map.put("disq_div", "1");
							disq1Univ1Map.put("periodDivCd", periodDivCd);
							disq1Univ1Map.put("evaYear", evaYear);
							disq1Univ1Map.put("poolCd", poolCd);
							disq1Univ1Map.put("disqUnicCd", disqUnicCd);

							poolSbmtMapper.insertPoolDisqUnivInfo(disq1Univ1Map);

							line++;
						}
					} else if (disq1UnivDataList.size() == 0) {

						throw new ResMsgException("기피제척대상 의 직계가족 또는 배우자 사유 대학 입력 데이터 가 없습니다. 없으시면 아니오 를 선택해주세요.");
					}

				} else {

					if (disq1UnivDataList.size() > 0) {

						throw new ResMsgException("기피제척대상 의 직계가족 또는 배우자 사유 대학 데이터 를 입력하시려면 " + "예" + " 를선택 하세요.");
					}
				}

				if ("Y".equals(disq2Univ)) {

					if (disq2UnivDataList.size() > 0) {
						int line = 1;
						for (Map<String, Object> disq2Univ1Map : disq2UnivDataList) {

							String disqUnicCd = (String) disq2Univ1Map.get("disq2UnivCd");

							if (disqUnicCd == null || disqUnicCd.trim().equals("")) {

								throw new ResMsgException("기피제척대상 기타 사유 대학 데이터" + line + "행 의 대학코드 데이터가 없습니다.");
							}

							disq2Univ1Map.put("di", di);
							disq2Univ1Map.put("usrId", usrId);
							disq2Univ1Map.put("disq_div", "2");
							disq2Univ1Map.put("periodDivCd", periodDivCd);
							disq2Univ1Map.put("evaYear", evaYear);
							disq2Univ1Map.put("poolCd", poolCd);
							disq2Univ1Map.put("disqUnicCd", disqUnicCd);

							poolSbmtMapper.insertPoolDisqUnivInfo(disq2Univ1Map);

							line++;

						}
					} else if (disq2UnivDataList.size() == 0) {

						throw new ResMsgException("기피제척대상 기타 사유 대학 입력 데이터 가 없습니다. 없으시면 아니오 를 선택해주세요.");
					}

				} else {

					if (disq2UnivDataList.size() > 0) {

						throw new ResMsgException("기피제척대상 기타 사유 대학 데이터 를 입력하시려면 " + "예" + " 를선택 하세요.");
					}
				}		
				
				
				poolSbmtMapper.savePoolApplInfo(param);

			

			txManager.commit(txStatus);

			resultMap.put("result", "S");
			hisparam.put("RESULT_TYPE", "S");
			String remark = String.format("공모 신청서제출 - 주기: %s, 진단년도: %s, 공모코드: %s", periodDivCd, evaYear, poolCd);
			hisparam.put("RMRK", remark);			
		} catch (Exception e) {

			txManager.rollback(txStatus);
			resultMap.put("result", "F");
			hisparam.put("RESULT_TYPE", "E");
			String errorMessage = "공모시스템 신청서제출 제출 실패: "+ e.getClass().getSimpleName() + " at " + e.getStackTrace()[0];
			hisparam.put("RMRK", errorMessage);
			
			throw e;
		} finally {
			if (clientIp != null && clientIp.contains(",")) {
			    // 쉼표(,)로 분리
			    String[] ipArray = clientIp.split(",");
			    // 첫 번째 IP만 Trim 해서 사용
			    clientIp = ipArray[0].trim();
		 }

			hisparam.put("MENU_NO", "P0004");
			hisparam.put("MENU_NM", "공모 신청서제출");
			hisparam.put("WORK_TYPE", "저장");
			hisparam.put("FUNC_NM", "공모 신청서제출");
			hisparam.put("PRIVACY_YN", 'N');
			hisparam.put("sessUsrNm", sname);
			hisparam.put("usrIp", clientIp);
			
			cmmnMapper.insertHis(hisparam);
		}


		return resultMap;
	}

	@RequestMapping(value = "/poolSubmitCancelData.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> poolSubmitCancelData(HttpServletResponse response, HttpServletRequest request) throws ResMsgException, Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		HttpSession session = request.getSession();

		String di = (String) session.getAttribute("DI");
		String usrId = (String) session.getAttribute("USR_ID");
		String name = (String) session.getAttribute("NAME");
		String sname = (String) session.getAttribute("NAME");
		String clientIp = (String) session.getAttribute("clientIp");
		
		if (di == null) {
			throw new ResMsgException("본인인증 이 필요 합니다.");
		}
		
		Map<String, Object> hisparam = new HashMap<String, Object>();
		
		TransactionStatus txStatus = txManager.getTransaction(new DefaultTransactionDefinition());

		try {

			String periodDivCd = request.getParameter("periodDivCd");
			String evaYear = request.getParameter("evaYear");
			String poolCd = request.getParameter("poolCd");
			String disq1Univ = request.getParameter("disq1Univ");
			String disq2Univ = request.getParameter("disq2Univ");
			

			if (periodDivCd == null || periodDivCd.trim().equals("")) {
				throw new ResMsgException("주기 데이타가 없습니다.");
			}

			if (evaYear == null || evaYear.trim().equals("")) {
				throw new ResMsgException("진단년도 데이타가 없습니다.");
			}

			if (poolCd == null || poolCd.trim().equals("")) {
				throw new ResMsgException("공모계획 코드가 데이타가 없습니다.");
			}

			/*if (disq1Univ == null || disq1Univ.trim().equals("")) {
				throw new ResMsgException("기피제척대상 의 직계가족 또는 배우자 사유 선택 값 이 없습니다.");
			}

			if (disq2Univ == null || disq2Univ.trim().equals("")) {
				throw new ResMsgException("기피제척대상 기타 사유 선택 값 이 없습니다.");
			}
			 */
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("di", di);
			param.put("periodDivCd", periodDivCd);
			param.put("evaYear", evaYear);
			param.put("poolCd", poolCd);
			param.put("usrId", usrId);
			param.put("name", name);
			param.put("applYn", "N");
			param.put("disq1Univ", "N");
			param.put("disq2Univ", "N");
			
			Map<String, Object> Info = poolSbmtMapper.getScheduleInfo(param);
			String poolCdNm = (String) Info.get("POOL_CD_NM");
			
			param.put("poolCdNm", poolCdNm);
			
			poolSbmtMapper.updatePoolDisqYNInfo(param);
			poolSbmtMapper.deletePoolDisqUnivList(param);
					
			poolSbmtMapper.savePoolApplInfo(param);

			txManager.commit(txStatus);

			resultMap.put("result", "S");
			hisparam.put("RESULT_TYPE", "S");
			hisparam.put("RMRK", "공모시스템 신청서제출 미제츨 성공");
			
		} catch (Exception e) {

			txManager.rollback(txStatus);
			resultMap.put("result", "F");
			hisparam.put("RESULT_TYPE", "F");
			String errorMessage = "공모시스템 신청서제출 미제출 실패: " + e.getClass().getSimpleName() + " at " + e.getStackTrace()[0];
			hisparam.put("RMRK", errorMessage);
			
			throw e;
		} finally {
			if (clientIp != null && clientIp.contains(",")) {
			    // 쉼표(,)로 분리
			    String[] ipArray = clientIp.split(",");
			    // 첫 번째 IP만 Trim 해서 사용
			    clientIp = ipArray[0].trim();
		 }
			
			hisparam.put("MENU_NO", "P0004");
			hisparam.put("MENU_NM", "공모 신청서제출");
			hisparam.put("WORK_TYPE", "저장");
			hisparam.put("FUNC_NM", "공모 신청서제출");
			hisparam.put("PRIVACY_YN", 'N');
			hisparam.put("sessUsrNm", sname);
			hisparam.put("usrIp", clientIp);
			
			cmmnMapper.insertHis(hisparam);
		}

		return resultMap;
	}


	@RequestMapping(value = "/selectSchlList.do", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> selectSchlList(HttpServletRequest request) throws Exception {

		String searchText = request.getParameter("searchText");
		String evaYear = request.getParameter("evaYear");
		String evaTarget = null;
		
		if ("2024".equals(evaYear)) {
		    evaTarget = "10020001"; // 2024년도의 평가 대상 코드
		} else if ("2025".equals(evaYear)) {
		    evaTarget = "10020002"; // 2025년도의 평가 대상 코드
		} else if ("2026".equals(evaYear)){
		    evaTarget = "10020003"; // 그 외의 경우, 'evaTarget'을 정의되지 않음으로 설정
		} else {
			throw new ResMsgException("evaYear 데이터가 없습니다.");
		}
		
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("searchText", searchText);
		param.put("evaTarget", evaTarget);
		
		
		List<Map<String, Object>> list = poolSbmtMapper.selectSchlList(param);

		return list;
	}

	/*@RequestMapping(value = "/disqUnivUpdateData.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> disqUnivUpdateData(MultipartHttpServletRequest request)
			throws ResMsgException, Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		HttpSession session = request.getSession();

		String di = (String) session.getAttribute("DI");
		String usrId = (String) session.getAttribute("USR_ID");

		if (di == null) {
			throw new ResMsgException("본인인증 이 필요 합니다.");
		}

		TransactionStatus txStatus = txManager.getTransaction(new DefaultTransactionDefinition());

		List<Map<String, Object>> disq1UnivDataList = new ObjectMapper()
				.readValue(request.getParameter("disq1UnivDataList"), new TypeReference<List<Map<String, Object>>>() {
				});
		List<Map<String, Object>> disq2UnivDataList = new ObjectMapper()
				.readValue(request.getParameter("disq2UnivDataList"), new TypeReference<List<Map<String, Object>>>() {
				});

		try {

			String periodDivCd = request.getParameter("periodDivCd");
			String evaYear = request.getParameter("evaYear");
			String poolCd = request.getParameter("poolCd");
			String disq1Univ = request.getParameter("disq1Univ");
			String disq2Univ = request.getParameter("disq2Univ");

			if (periodDivCd == null || periodDivCd.trim().equals("")) {
				throw new ResMsgException("주기 데이타가 없습니다.");
			}

			if (evaYear == null || evaYear.trim().equals("")) {
				throw new ResMsgException("진단년도 데이타가 없습니다.");
			}

			if (poolCd == null || poolCd.trim().equals("")) {
				throw new ResMsgException("공모계획 코드가 데이타가 없습니다.");
			}

			if (disq1Univ == null || disq1Univ.trim().equals("")) {
				throw new ResMsgException("기피제척대상 의 직계가족 또는 배우자 사유 선택 값 이 없습니다.");
			}

			if (disq2Univ == null || disq2Univ.trim().equals("")) {
				throw new ResMsgException("기피제척대상 기타 사유 선택 값 이 없습니다.");
			}

			
			 * if(disq1UnivDataList.size() == 0 && disq2UnivDataList.size() ==
			 * 0){
			 * 
			 * throw new ResMsgException("저장할 데이터 가 없습니다."); }
			 

			Map<String, Object> param = new HashMap<String, Object>();

			param.put("di", di);
			param.put("usrId", usrId);
			param.put("periodDivCd", periodDivCd);
			param.put("evaYear", evaYear);
			param.put("poolCd", poolCd);
			param.put("disq1Univ", disq1Univ);
			param.put("disq2Univ", disq2Univ);

			poolMapper.updatePoolDisqYNInfo(param);
			poolMapper.deletePoolDisqUnivList(param);

			if ("Y".equals(disq1Univ)) {
				if (disq1UnivDataList.size() > 0) {
					int line = 1;
					for (Map<String, Object> disq1Univ1Map : disq1UnivDataList) {

						String disqUnicCd = (String) disq1Univ1Map.get("disq1UnivCd");

						if (disqUnicCd == null || disqUnicCd.trim().equals("")) {

							throw new ResMsgException("기피제척대상 의 직계가족 또는 배우자 사유 대학 데이터" + line + "행 의 대학코드 데이터가 없습니다.");
						}

						disq1Univ1Map.put("di", di);
						disq1Univ1Map.put("usrId", usrId);
						disq1Univ1Map.put("disq_div", "1");
						disq1Univ1Map.put("periodDivCd", periodDivCd);
						disq1Univ1Map.put("evaYear", evaYear);
						disq1Univ1Map.put("poolCd", poolCd);
						disq1Univ1Map.put("disqUnicCd", disqUnicCd);

						poolMapper.insertPoolDisqUnivInfo(disq1Univ1Map);

						line++;
					}
				} else if (disq1UnivDataList.size() == 0) {

					throw new ResMsgException("기피제척대상 의 직계가족 또는 배우자 사유 대학 입력 데이터 가 없습니다. 없으시면 아니오 를 선택해주세요.");
				}

			} else {

				if (disq1UnivDataList.size() > 0) {

					throw new ResMsgException("기피제척대상 의 직계가족 또는 배우자 사유 대학 데이터 를 입력하시려면 " + "예" + " 를선택 하세요.");
				}
			}

			if ("Y".equals(disq2Univ)) {

				if (disq2UnivDataList.size() > 0) {
					int line = 1;
					for (Map<String, Object> disq2Univ1Map : disq2UnivDataList) {

						String disqUnicCd = (String) disq2Univ1Map.get("disq2UnivCd");

						if (disqUnicCd == null || disqUnicCd.trim().equals("")) {

							throw new ResMsgException("기피제척대상 기타 사유 대학 데이터" + line + "행 의 대학코드 데이터가 없습니다.");
						}

						disq2Univ1Map.put("di", di);
						disq2Univ1Map.put("usrId", usrId);
						disq2Univ1Map.put("disq_div", "2");
						disq2Univ1Map.put("periodDivCd", periodDivCd);
						disq2Univ1Map.put("evaYear", evaYear);
						disq2Univ1Map.put("poolCd", poolCd);
						disq2Univ1Map.put("disqUnicCd", disqUnicCd);

						poolMapper.insertPoolDisqUnivInfo(disq2Univ1Map);

						line++;

					}
				} else if (disq2UnivDataList.size() == 0) {

					throw new ResMsgException("기피제척대상 기타 사유 대학 입력 데이터 가 없습니다. 없으시면 아니오 를 선택해주세요.");
				}

			} else {

				if (disq2UnivDataList.size() > 0) {

					throw new ResMsgException("기피제척대상 기타 사유 대학 데이터 를 입력하시려면 " + "예" + " 를선택 하세요.");
				}
			}

			txManager.commit(txStatus);

			resultMap.put("result", "S");

		} catch (Exception e) {

			txManager.rollback(txStatus);
			resultMap.put("result", "F");
			throw e;
		}

		return resultMap;

	}*/

	
}
