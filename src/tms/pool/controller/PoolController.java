package tms.pool.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.ClientAbortException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

//import kedi.necte.eva.cmmn.service.CmmnService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import tms.cmmn.maps.CmmnMapper;
import tms.cmmn.service.CmmnService;
import tms.main.exception.ResMsgException;
import tms.main.maps.MainMapper;
import tms.main.util.KediAES256Util;
import tms.pool.maps.PoolMapper;

@Controller
@RequestMapping("/pool")
public class PoolController {
	private Log log = LogFactory.getLog(this.getClass());

	@Autowired
	MainMapper mainMapper;

	@Autowired
	PoolMapper poolMapper;

	@Autowired
	CmmnMapper cmmnMapper;

    @Autowired
    CmmnService cmmnService;
	
	@Autowired
	MessageSource messageSource;

	@Resource(name = "txManager")
	private PlatformTransactionManager txManager;

	@RequestMapping("/poolInfo.do")
	public String poolInfo(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
		HttpSession session = request.getSession();
		String di = (String) session.getAttribute("DI");

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
				}

			} else {

				String errorMessage = messageSource.getMessage("msg_105", null, Locale.getDefault());
				redirectAttributes.addFlashAttribute("message", errorMessage);
				request.getSession().invalidate();
				return "redirect:/join/joinStep01.do";
			}
		}

		return "/pool/poolInfo";
	}

	@RequestMapping(value = "/getPoolInfo.do")
	public void getList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		String di = (String) session.getAttribute("DI");
		String usrId = (String) session.getAttribute("USR_ID");
		String sname = (String) session.getAttribute("NAME");
		String clientIp = (String) session.getAttribute("clientIp");

		KediAES256Util aes256Util = new KediAES256Util();

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("di", di);
		paramMap.put("usrId", usrId);

		Map<String, Object> poolData = poolMapper.getPoolInfo(paramMap);

		if (poolData != null) {
			String name = (String) poolData.get("NAME");
			String gender = (String) poolData.get("GENDER");
			String telHp = (String) poolData.get("TEL_HP");
			/*String telHome = (String) poolData.get("TEL_HOME");*/
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
			/*if (telHome != null && !"".equals(telHome))
				telHome = aes256Util.aesDecode(telHome);*/
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

			poolData.put("NAME", name);
			poolData.put("GENDER", gender);
			poolData.put("TEL_HP", telHp);
			/*poolData.put("TEL_HOME", telHome);*/
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

			List<Map<String, Object>> poolDegreeList = poolMapper.selectPoolDegreeList(paramMap);
			List<Map<String, Object>> poolFulltimeTeacherList = poolMapper.selectPoolFulltimeTeacherList(paramMap);
			paramMap.put("typeDiv", "1");
			List<Map<String, Object>> poolEvaExp1List = poolMapper.selectPoolEvaExpList(paramMap);
			paramMap.put("typeDiv", "2");
			List<Map<String, Object>> poolEvaExp2List = poolMapper.selectPoolEvaExpList(paramMap);

			resultMap.put("poolDegreeList", poolDegreeList);
			resultMap.put("poolFulltimeTeacherList", poolFulltimeTeacherList);
			resultMap.put("poolEvaExp1List", poolEvaExp1List);
			resultMap.put("poolEvaExp2List", poolEvaExp2List);

		}

		resultMap.put("poolData", poolData);

		resultMap.put("result", "S");

		if (clientIp != null && clientIp.contains(",")) {
		    // 쉼표(,)로 분리
		    String[] ipArray = clientIp.split(",");
		    // 첫 번째 IP만 Trim 해서 사용
		    clientIp = ipArray[0].trim();
	 }
		
		Map<String, Object> hisparam = new HashMap<String, Object>();
		hisparam.put("MENU_NO", "P0003");
		hisparam.put("MENU_NM", "공모 신청서작성및수정");
		hisparam.put("WORK_TYPE", "조회");
		hisparam.put("FUNC_NM", "공모 신청서작성및수정");
		hisparam.put("PRIVACY_YN", 'N');
		hisparam.put("RMRK", "공모시스템 신청서작성 및 수정화면 조회");
		hisparam.put("RESULT_TYPE", 'S');
		hisparam.put("sessUsrNm", sname);
		hisparam.put("usrIp", clientIp);
		cmmnMapper.insertHis(hisparam);

		Gson gson = new Gson();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(gson.toJson(resultMap));
	}

	@RequestMapping(value = "/updateData.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateData(MultipartHttpServletRequest request) throws ResMsgException, Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		KediAES256Util aes256Util = new KediAES256Util();

		HttpSession session = request.getSession();

		String di = (String) session.getAttribute("DI");
		String sUsrId = (String) session.getAttribute("USR_ID");
		String sname = (String) session.getAttribute("NAME");
		String clientIp = (String) session.getAttribute("clientIp");

		/*
		 * Map<String, Object> poolScheMap = poolMapper.getPoolScheInfo();
		 * 
		 * if (poolScheMap == null || poolScheMap.isEmpty()) { throw new
		 * ResMsgException("신청서 작성 일자가 등록되어 있지 않습니다."); }
		 */

		if (di == null) {
			throw new ResMsgException("본인인증 이 필요 합니다.");
		}

		Map<String, Object> hisparam = new HashMap<String, Object>();

		TransactionStatus txStatus = txManager.getTransaction(new DefaultTransactionDefinition());

		List<Map<String, Object>> degreeDataList = new ObjectMapper().readValue(request.getParameter("degreeDataList"),
				new TypeReference<List<Map<String, Object>>>() {
				});
		List<Map<String, Object>> fullTimeDataList = new ObjectMapper()
				.readValue(request.getParameter("fullTimeDataList"), new TypeReference<List<Map<String, Object>>>() {
				});
		List<Map<String, Object>> evaExp1DataList = new ObjectMapper()
				.readValue(request.getParameter("evaExp1DataList"), new TypeReference<List<Map<String, Object>>>() {
				});
		List<Map<String, Object>> evaExp2DataList = new ObjectMapper()
				.readValue(request.getParameter("evaExp2DataList"), new TypeReference<List<Map<String, Object>>>() {
				});

		try {

			String usrId = request.getParameter("email");
			String jobCd = request.getParameter("jobCd");
			String univDiv = request.getParameter("univDiv");
			String jobNm = request.getParameter("jobNm");
			String univCd = request.getParameter("univCd");
			String dept = request.getParameter("dept");
			String position = request.getParameter("position");
			String email = request.getParameter("email");
			String telhp = request.getParameter("telhp");
			/*String telhome = request.getParameter("telhome");*/
			String AddrHomeZipcode = request.getParameter("AddrHomeZipcode");
			String AddrHome = request.getParameter("AddrHome");
			String AddrHomeDetail = request.getParameter("AddrHomeDetail");
			String resume1 = request.getParameter("resume1");
			String resume2 = request.getParameter("resume2");
			String ctpvCd = request.getParameter("ctpvCd");
			String orgEmail = sUsrId;
			//String testHp = "01027152793";
			
			//testHp = aes256Util.aesEncode(testHp);
			
			if (jobCd == null || jobCd.trim().equals("")) {
				throw new ResMsgException("공모분야 데이타가 없습니다.");
			}
			if (univDiv == null || univDiv.trim().equals("")) {
				throw new ResMsgException("소속구분 데이타가 없습니다.");
			}
			if (univCd == null || univCd.trim().equals("")) {
				throw new ResMsgException("소속구분 대학코드 가 없습니다.");
			}
			if (jobNm == null || jobNm.trim().equals("")) {
				throw new ResMsgException("대학명 데이타가 없습니다.");
			}
			if (dept == null || dept.trim().equals("")) {
				throw new ResMsgException("부서(학과) 데이타가 없습니다.");
			}
			if (position == null || position.trim().equals("")) {
				throw new ResMsgException("직급 데이타가 없습니다.");
			}
			if (email == null || email.trim().equals("")) {
				throw new ResMsgException("이메일 데이타가 없습니다.");

			}
			if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
				throw new ResMsgException("유효한 이메일 형식이 아닙니다.");
			}			
			if (telhp == null || telhp.trim().equals("")) {
				throw new ResMsgException("핸드폰 데이터가 없습니다.");

			}

			Map<String, Object> checkparam = new HashMap<String, Object>();
			checkparam.put("email", email);
			checkparam.put("orgEmail", orgEmail);

			if (orgEmail != null) {
				if (poolMapper.isChangeEmail(checkparam)) {
					throw new ResMsgException("중복된 이메일이 있습니다.");
				}
			} else if (orgEmail == null) {
				if (poolMapper.isExistEmail(checkparam)) {
					throw new ResMsgException("중복된 이메일이 있습니다.");
				}
			}

			/*if (telhome == null || telhome.trim().equals("")) {
				throw new ResMsgException("연락처(자택) 데이타가 없습니다.");
			}*/

			/*if (!telhome.matches("\\d+")) {
				throw new ResMsgException("연락처(자택)는 숫자만 입력 가능합니다.");
			}*/

			int minLength = 9; // 최소 길이
			int maxLength = 12; // 최대 길이

			/*if (telhome.length() < minLength || telhome.length() > maxLength) {
				throw new ResMsgException("전화번호(자택)는 " + minLength + "에서 " + maxLength + "자 사이어야 합니다.");
			}*/

			if (AddrHomeZipcode == null || AddrHomeZipcode.trim().equals("")) {
				throw new ResMsgException("우편번호 데이타가 없습니다.");
			}
			if (AddrHome == null || AddrHome.trim().equals("")) {
				throw new ResMsgException("주소 데이타가 없습니다.");
			}
			if (AddrHomeDetail == null || AddrHomeDetail.trim().equals("")) {
				throw new ResMsgException("상세주소 데이타가 없습니다.");
			}

			/*if (resume1 == null || resume1.trim().equals("")) {
				throw new ResMsgException("대학관련 진단(평가)에서의 진단위원 및 준비위원 활동경험 데이타가 없습니다.");
			}*/

			if (resume2 == null || resume2.trim().equals("")) {
				throw new ResMsgException("전공분야 및 진단(평가) 관심영역 데이타가 없습니다.");
			}

			Map<String, Object> param = new HashMap<String, Object>();

			param.put("di", di);
			param.put("usrId", usrId);
			param.put("orgEmail", orgEmail);

			if (jobNm != null && !"".equals(jobNm))
				jobNm = aes256Util.aesEncode(jobNm);
			if (dept != null && !"".equals(dept))
				dept = aes256Util.aesEncode(dept);
			if (position != null && !"".equals(position))
				position = aes256Util.aesEncode(position);
			if (email != null && !"".equals(email))
				email = aes256Util.aesEncode(email);
			if (telhp != null && !"".equals(telhp))
				telhp = aes256Util.aesEncode(telhp);
			/*if (telhome != null && !"".equals(telhome))
				telhome = aes256Util.aesEncode(telhome);*/
			if (AddrHomeZipcode != null && !"".equals(AddrHomeZipcode))
				AddrHomeZipcode = aes256Util.aesEncode(AddrHomeZipcode);
			if (AddrHome != null && !"".equals(AddrHome))
				AddrHome = aes256Util.aesEncode(AddrHome);
			if (AddrHomeDetail != null && !"".equals(AddrHomeDetail))
				AddrHomeDetail = aes256Util.aesEncode(AddrHomeDetail);
			
			param.put("jobCd", jobCd);
			param.put("univDiv", univDiv);
			param.put("jobNm", jobNm);
			param.put("univCd", univCd);
			param.put("dept", dept);
			param.put("position", position);
			param.put("email", email);
			param.put("telhp", telhp);
/*			param.put("telhome", telhome);*/
			param.put("AddrHomeZipcode", AddrHomeZipcode);
			param.put("AddrHome", AddrHome);
			param.put("AddrHomeDetail", AddrHomeDetail);
			param.put("resume1", resume1);
			param.put("resume2", resume2);
			param.put("ctpvCd", ctpvCd);
			
			String fileState = request.getParameter("fileState");
			String oldFile = request.getParameter("OLD_FILE_ID");
			MultipartFile lastDegCertFile = request.getFile("lastDegCertFile");
			
			//K - 유지 D - 삭제  U - 삭제 후 파일업로드
			if(fileState != null && fileState.equals("K")){
				//유지
				if(oldFile==null || oldFile.isEmpty() || oldFile.equals("undefined")){
					throw new ResMsgException("재직증명서가 누락되었습니다.");
				}else{
					param.put("lastDegCertFileId", oldFile);
				} 
			} else {
				//cmmnMapper.updateDelFlag(oldFile);

				if(lastDegCertFile != null) {
					String lastDegCertFileId = cmmnService.fileUpload(request, lastDegCertFile,"pdf");
					param.put("lastDegCertFileId", lastDegCertFileId);
				}else{
					throw new ResMsgException("재직증명서가 누락되었습니다.");
				}
			}
			


			poolMapper.updatePoolInfo(param);

			if (!usrId.equals(orgEmail)) {

				poolMapper.updatePoolApplInfo(param);
				poolMapper.updatePoolDisqUnivInfo(param);
			}

			poolMapper.deletePoolDegreeList(param);
			if (degreeDataList.size() > 0) {
				int line = 1;
				for (Map<String, Object> degreeMap : degreeDataList) {
					String degCd = (String) degreeMap.get("degCd");
					String degCdNm = (String) degreeMap.get("degCdNm");
					String degUnivCd = (String) degreeMap.get("degUnivCd");
					String degUnivNm = (String) degreeMap.get("degUnivNm");
					String degEduYm = (String) degreeMap.get("degEduYm");
					// String locCdNm = (String) degreeMap.get("locCdNm");
					String degMajor = (String) degreeMap.get("degMajor");
					
			        String degreeType = "";
			        if ("1".equals(degCd)) {
			            degreeType = "학사";
			        } else if ("2".equals(degCd)) {
			            degreeType = "석사";
			        } else if ("3".equals(degCd)) {
			            degreeType = "박사";
			        }
					
					if ("2".equals(jobCd)) {
						
						  if ("1".equals(degCd)) {
						        if (degCdNm == null || degCdNm.trim().equals("")) {
						            throw new ResMsgException("학력 - " + degreeType + "학위 정보가 누락되었습니다.");
						        }

						        if (degUnivCd == null || degUnivCd.trim().equals("")) {
						            throw new ResMsgException("학력 - " + degreeType + "학위 대학코드 정보가 누락되었습니다.");
						        }

						        if (degUnivNm == null || degUnivNm.trim().equals("")) {
						            throw new ResMsgException("학력 - " + degreeType + "학위 대학명 정보가 누락되었습니다.");
						        }

						        if (degEduYm == null || degEduYm.trim().equals("")) {
						            throw new ResMsgException("학력 - " + degreeType + "학위 학력학위취득년월 정보가 누락되었습니다.");
						        }

						        if (!degEduYm.matches("\\d{6}")) {
						            throw new ResMsgException("학력 - " + degreeType + "학위 학력학위취득년월 정보 형식이 올바르지 않습니다. 6자리 숫자만 허용됩니다.");
						        }

						        int minYearMonth = 190001; // 최소 연월
						        int maxYearMonth = 210012; // 최대 연월

						        int yearMonthValue = Integer.parseInt(degEduYm);
						        if (yearMonthValue < minYearMonth || yearMonthValue > maxYearMonth) {
						            throw new ResMsgException("학력 - " + degreeType + "학위 학력학위취득년월 정보가 유효한 범위 내에 있어야 합니다.");
						        }

						        if (degMajor == null || degMajor.trim().equals("")) {
						            throw new ResMsgException("학력 - " + degreeType + "학위 세부전공 정보가 누락되었습니다.");
						        }						
						  } else {
							  	// 석사, 박사 등 다른 학위의 경우 데이터가 있으면 체크
						        if (degUnivCd != null && !degUnivCd.trim().equals("")) {
						        	
						            if (degUnivNm == null || degUnivNm.trim().equals("")) {
						                throw new ResMsgException("학력 - " + degreeType + "학위 대학명 정보가 누락되었습니다.");
						            }
						            
						            if (degEduYm == null || degEduYm.trim().equals("")) {
							            throw new ResMsgException("학력 - " + degreeType + "학위 학력학위취득년월 정보가 누락되었습니다.");
							        }

						            if (degEduYm != null && !degEduYm.trim().equals("")) {
						                if (!degEduYm.matches("\\d{6}")) {
						                    throw new ResMsgException("학력 - " + degreeType + "학위 학력학위취득년월 정보 형식이 올바르지 않습니다. 6자리 숫자만 허용됩니다.");
						                }

						                int minYearMonth = 190001;
						                int maxYearMonth = 210012;

						                int yearMonthValue = Integer.parseInt(degEduYm);
						                if (yearMonthValue < minYearMonth || yearMonthValue > maxYearMonth) {
						                    throw new ResMsgException("학력 - " + degreeType + "학위 학력학위취득년월 정보가 유효한 범위 내에 있어야 합니다.");
						                }
						            }

						            if (degMajor != null && degMajor.trim().equals("")) {
						                throw new ResMsgException("학력 - " + degreeType + "학위 세부전공 정보가 누락되었습니다.");
						            }
						        }
						  }
						
						
					} else {
						
						if (degCd == null || degCd.trim().equals("")) {

							throw new ResMsgException("학력 - 학위코드가 누락되었습니다.");
						}

						if (degCdNm == null || degCdNm.trim().equals("")) {

							throw new ResMsgException("학력 - " + degreeType + "학위 학위 정보가 누락되었습니다.");
						}

						if (degUnivCd == null || degUnivCd.trim().equals("")) {

							throw new ResMsgException("학력 - " + degreeType + "학위 대학코드 정보가 누락되었습니다.");
						}

						if (degUnivNm == null || degUnivNm.trim().equals("")) {

							throw new ResMsgException("학력 - " + degreeType + "학위 대학명 정보가 누락되었습니다.");
						}

						if (degEduYm == null || degEduYm.trim().equals("")) {

							throw new ResMsgException("학력 - " + degreeType + "학위 학력학위취득년월 정보가 누락되었습니다.");
						}

						if (!degEduYm.matches("\\d{6}")) {
							throw new ResMsgException("학력 - " + degreeType + "학위 학력학위취득년월 정보 형식이 올바르지 않습니다. 6자리 숫자만 허용됩니다.");
						}

						int minYearMonth = 190001; // 최소 연월 (예: 1900년 1월)
						int maxYearMonth = 210012; // 최대 연월 (예: 2100년 12월)

						int yearMonthValue = Integer.parseInt(degEduYm);
						if (yearMonthValue < minYearMonth || yearMonthValue > maxYearMonth) {
							throw new ResMsgException("학력 - " + degreeType + "학위 학력학위취득년월 정보가 유효한 범위 내에 있어야 합니다.");
						}

						if (degMajor == null || degMajor.trim().equals("")) {

							throw new ResMsgException("학력 - " + degreeType + "학위 세부전공 정보가 누락되었습니다.");
						}

					}
					
					degreeMap.put("di", di);
					degreeMap.put("usrId", usrId);
					poolMapper.insertPoolDegreeInfo(degreeMap);
					line++;
				}
			}

			poolMapper.deletePoolFulltimeTeacherList(param);
			if (fullTimeDataList.size() > 0) {
				int line = 1;
				for (Map<String, Object> fullTimeMap : fullTimeDataList) {
					String fttJobStYm = (String) fullTimeMap.get("fttJobStYm");
					String fttjobEdYm = (String) fullTimeMap.get("fttjobEdYm");
					String fttUnivCd = (String) fullTimeMap.get("fttUnivCd");
					String fttUnivNm = (String) fullTimeMap.get("fttUnivNm");
					String fttDept = (String) fullTimeMap.get("fttDept");

					if (fttJobStYm == null || fttJobStYm.trim().equals("")) {

						throw new ResMsgException("재직경력 데이터" + line + "행 의 근무 시작년월 데이터가 없습니다.");
					}

					if (fttjobEdYm == null || fttjobEdYm.trim().equals("")) {

						throw new ResMsgException("재직경력 데이터" + line + "행 의 근무 종료년월  데이터가 없습니다.");
					}

					if (!fttJobStYm.matches("\\d{6}")) {
						throw new ResMsgException("재직경력 데이터" + line + "행의 근무 시작년월 데이터 형식이 올바르지 않습니다. 6자리 숫자만 허용됩니다.");
					}

					if (!fttjobEdYm.matches("\\d{6}")) {
						throw new ResMsgException("재직경력 데이터" + line + "행의 근무  종료년월 데이터 형식이 올바르지 않습니다. 6자리 숫자만 허용됩니다.");
					}

					// 시작년월과 종료년월을 정수로 변환하여 비교
					int startYearMonth = Integer.parseInt(fttJobStYm);
					int endYearMonth = Integer.parseInt(fttjobEdYm);

					// 시작년월이 종료년월을 넘지 않는지 확인
					if (startYearMonth > endYearMonth) {
						throw new ResMsgException("재직경력 데이터" + line + "행의 근무 시작년월이 종료년월을 넘을 수 없습니다.");
					}

					if (fttUnivCd == null || fttUnivCd.trim().equals("")) {

						throw new ResMsgException("재직경력 데이터" + line + "행 의 대학코드 데이터가 없습니다.");
					}

					if (fttUnivNm == null || fttUnivNm.trim().equals("")) {

						throw new ResMsgException("재직경력 데이터" + line + "행 의 대학명 데이터가 없습니다.");
					}

					if (fttDept == null || fttDept.trim().equals("")) {

						throw new ResMsgException("재직경력 데이터" + line + "행 의 부서(학과) 데이터가 없습니다.");
					}

					fullTimeMap.put("di", di);
					fullTimeMap.put("usrId", usrId);
					poolMapper.insertPoolFulltimeTeacherInfo(fullTimeMap);
					line++;
				}
			}

			poolMapper.deletePoolEvaExpList(param);
			if (evaExp1DataList.size() > 0) {
				int line = 1;
				for (Map<String, Object> evaExp1Map : evaExp1DataList) {
					String partYear = (String) evaExp1Map.get("partYear");
					String evaNm = (String) evaExp1Map.get("evaNm");
					String org = (String) evaExp1Map.get("org");
					String roleCdNm = (String) evaExp1Map.get("roleCdNm");

					if (partYear == null || partYear.trim().equals("")) {

						throw new ResMsgException("진단(평가)위원 활동경험 데이터" + line + "행 의 참여연도 데이터가 없습니다.");
					}

					if (!partYear.matches("\\d{4}")) {
						throw new ResMsgException(
								"진단(평가)위원 활동경험 데이터" + line + "행의 참여연도 데이터 형식이 올바르지 않습니다. 4자리 숫자만 허용됩니다.");
					}

					if (evaNm == null || evaNm.trim().equals("")) {

						throw new ResMsgException("진단(평가)위원 활동경험 데이터" + line + "행 의 진단명 데이터가 없습니다.");
					}

					if (org == null || org.trim().equals("")) {

						throw new ResMsgException("진단(평가)위원 활동경험 데이터" + line + "행 의 주관기념 데이터가 없습니다.");
					}

					if (roleCdNm == null || roleCdNm.trim().equals("")) {

						throw new ResMsgException("진단(평가)위원 활동경험 데이터" + line + "행 의 역할 데이터가 없습니다.");
					}

					evaExp1Map.put("di", di);
					evaExp1Map.put("usrId", usrId);
					evaExp1Map.put("typeDiv", "1");
					poolMapper.insertPoolEvaExpInfo(evaExp1Map);

					line++;

				}
			}

			if (evaExp2DataList.size() > 0) {
				int line = 1;
				for (Map<String, Object> evaExp2Map : evaExp2DataList) {
					String partYear = (String) evaExp2Map.get("partYear");
					String evaNm = (String) evaExp2Map.get("evaNm");
					String org = (String) evaExp2Map.get("org");
					String roleCdNm = (String) evaExp2Map.get("roleCdNm");

					if (partYear == null || partYear.trim().equals("")) {

						throw new ResMsgException("진단(평가) 준비위원 활동경험 데이터" + line + "행 의 참여연도 데이터가 없습니다.");
					}

					if (!partYear.matches("\\d{4}")) {
						throw new ResMsgException(
								"진단(평가) 준비위원 활동경험 데이터" + line + "행의 참여연도 데이터 형식이 올바르지 않습니다. 4자리 숫자만 허용됩니다.");
					}

					if (evaNm == null || evaNm.trim().equals("")) {

						throw new ResMsgException("진단(평가) 준비위원 활동경험험 데이터" + line + "행 의 진단명 데이터가 없습니다.");
					}

					if (org == null || org.trim().equals("")) {

						throw new ResMsgException("진단(평가) 준비위원 활동경험 데이터" + line + "행 의 주관기념 데이터가 없습니다.");
					}

					if (roleCdNm == null || roleCdNm.trim().equals("")) {

						throw new ResMsgException("진단(평가) 준비위원 활동경험 데이터" + line + "행 의 역할 데이터가 없습니다.");
					}

					evaExp2Map.put("di", di);
					evaExp2Map.put("usrId", usrId);
					evaExp2Map.put("typeDiv", "2");
					poolMapper.insertPoolEvaExpInfo(evaExp2Map);

					line++;

				}
			}
			
			//log.info("testHp=================================================================="+testHp);

			txManager.commit(txStatus);

			resultMap.put("result", "S");
			hisparam.put("RESULT_TYPE", "S");
			hisparam.put("RMRK", "공모시스템 신청서작성 및 수정화면 저장 성공");
		} catch (Exception e) {

			txManager.rollback(txStatus);
			resultMap.put("result", "F");
			hisparam.put("RESULT_TYPE", "E");
			
			 // 에러 메시지 상세화 및 간소화
		    String errorMessage = "신청서작성 및 수정화면 저장 실패: " + e.getClass().getSimpleName() + " at " + e.getStackTrace()[0];
		    hisparam.put("RMRK", errorMessage);

			throw e;
			
		} finally {

			if (clientIp != null && clientIp.contains(",")) {
			    // 쉼표(,)로 분리
			    String[] ipArray = clientIp.split(",");
			    // 첫 번째 IP만 Trim 해서 사용
			    clientIp = ipArray[0].trim();
		 }
			
			hisparam.put("MENU_NO", "P0003");
			hisparam.put("MENU_NM", "공모 신청서작성및수정");
			hisparam.put("WORK_TYPE", "저장");
			hisparam.put("FUNC_NM", "공모 신청서작성및수정");
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

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("searchText", searchText);

		List<Map<String, Object>> list = poolMapper.selectSchlList(param);

		return list;
	}

	@RequestMapping(value = "/selectSchlList2.do", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> selectSchlList2(HttpServletRequest request) throws Exception {

		String schlType = request.getParameter("schlType");
		String searchText = request.getParameter("searchText");

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("accCmt", schlType);
		param.put("searchText", searchText);

		List<Map<String, Object>> list = poolMapper.selectSchlList2(param);

		return list;
	}

    @RequestMapping("/fileDownload.do")
    public void fileDownload(@RequestParam String fileId, String fileSeq , HttpServletRequest request,HttpServletResponse response) throws Exception {    	
    	String univCd = (String) request.getSession().getAttribute("UNIV_CD");
    	
    	Map<String,Object> param = new HashMap<String,Object>();
    	try {
    		param.put("fileId", fileId);

    		if(fileSeq != null) {
    			param.put("fileSeq", fileSeq);
    		} else {
    			param.put("fileSeq", "0001");
    		}
    		
    	} catch(NumberFormatException e) {
    		throw new ResMsgException("fileId는 숫자형식만 가능합니다.", "blank");
    	}
    	
    	Map<String,Object> fileInfo = cmmnMapper.getFileInfo(param);
    	if(fileInfo == null) {
    		throw new ResMsgException("다운로드 할 파일이 없습니다.", "blank");
    	}
    	
    	
    	if(univCd != null) {
    		String inptUsrPerm = (String) fileInfo.get("INPT_USR_PERM");
    		if(inptUsrPerm != null && inptUsrPerm.equals("U")) {
    			String inptId = (String) fileInfo.get("INPT_ID");
    			if(!univCd.equals(inptId)) {
        			throw new ResMsgException("타 대학의 파일을 다운로드 할 수 없습니다.", "blank");
        		}
    		}
    		
    	}
    	
    	cmmnService.fileDownload(request, response, fileInfo);
    }
}
