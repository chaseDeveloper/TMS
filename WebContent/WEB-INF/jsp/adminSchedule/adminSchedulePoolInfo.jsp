<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div id="dialog" style="display:none;">
	<div id="wrapper">
		
		<!-- container -->
		<div class="subContainer">

			<div id="content">
				<div class="con_box">
				<h2 class="title">신청서 작성</h2>

				<div class="sub_bg">	
					<div class="sub_box">	
						
						<!-- 개인신상정보 -->
						<div class="form_box">
							<h3>개인신상정보</h3>
							
							<table class="inquiry" summary="개인신상정보">
								<caption>공모분야,성명,성별,생년월일,소속대학 구분,대학명,부서(학과),직급,E-Mail,연락처(핸드폰),연락처(자택),주소 정보를 확인할 수 있습니다.</caption>
								<colgroup>
									<col style="width:14%;" />
									<col style="width:*" />
								</colgroup>
								<tbody>									
									<tr>										
										<th>공모분야</th>										
										<td><div class="w_20">
											<select id="PoolInfoJobCd">
												<option value="1" >교수</option>
												<option value="2" >교직원</option>
											</select>											
											</div>
										</td>
									</tr>
									<tr>
										<th>성명</th>										
										<td><div class="w_20"><input type="text" title="성명" id="PoolInfoName" disabled></div></td>
									</tr>
									<tr>
										<th>성별</th>
										<td><div class="w_20">
											<select id="PoolInfoGender" disabled>
												 <option value="0" >여성</option>
												 <option value="1" >남성</option>		
											</select>											
											</div>																				
										</td>
									</tr>							  
									<tr>
										<th>생년월일</th>
										<td><div class="w_20"><input type="text" title="생년월일" id="PoolInfoBirthday" disabled></div></td>
									</tr>
									<tr>
										<th>소속대학 구분</th>
										<td><div class="w_20"><input type="text" title="소속대학 구분" id="PoolInfoUnivDivNm" disabled></div></td>
									</tr>							  
									<tr>
										<th>대학명</th>
										<td><div class="w_20"><input type="text" title="대학명" id="PoolInfoJobNm" readonly></div></td>
									</tr>
									<tr>
										<th>부서(학과)</th>
										<td><div class="w_20"><input type="text" title="부서(학과)" id="PoolInfoDept"></div></td>
									</tr>							  
									<tr>
										<th>직급</th>
										<td><div class="w_20"><input type="text" title="직급" id="PoolInfoPosition"></div></td>
									</tr>
									<tr>
										<th>E-Mail</th>
										<td><div class="w_20"><input type="text" title="E-Mail" id="PoolInfoEmail"></div></td>																
									</tr>
									
									<tr>
										<th>연락처(핸드폰)</th>
										<td><div class="w_20"><input type="text" title="연락처(핸드폰)" id="PoolInfoTelHp" disabled></div></td>
									</tr>
									<tr>
										<th>연락처(자택)</th>
										<td><div class="w_20"><input type="text" title="연락처(자택)" id="PoolInfoTelHome"></div></td>
									</tr>
									<tr>
										<th>주소</th>
										<td><div class="w_30"><input type="text" id="sample4_postcode" placeholder="우편번호를 검색해 주세요." title="우편번호를 검색해 주세요." readonly></div>
										<div class="w_100"><input type="text" id="sample4_roadAddress" placeholder="도로명 주소." title="주소를 검색해 주세요." readonly></div>
										<div class="w_100"><input type="text"   id="sample4_detailAddress" placeholder="상세주소를 입력해 주세요." title="상세주소를 입력해 주세요."></div></td>
										
									</tr>
																		
								</tbody>								
							</table>
							<input type="hidden" id="PoolInfoUnivDiv"/>
							<input type="hidden" id="PoolInfoUnivCd"/>
							<input type="hidden" id="PoolInfoCtpvCd"/>							
							<input type="hidden" id="sample4_extraAddress"/>
														
						</div>
						<!-- //개인신상정보 -->
						
						<!-- 학력 -->
						<div class="form_box">
							<div class="form_title">
								<div class="fl_left">
									<h3>학력</h3>
								</div>														
							</div>
							<div class="form_table" >
							<table id="degreeTable" class="list" summary="학력">
								<caption>No.,학위,대학,학력취득년월,소재지,세부전공 정보를 확인할 수 있습니다.</caption>
								<colgroup class="dp_n_t dp_n_m">
									<col style="width:3%;" />
									<col style="width:*" />
									<col style="width:*" />									
									<col style="width:*" />
									<col style="width:*" />
								</colgroup>
								<thead class="dp_n_t dp_n_m">
									<th scope="col">No.</th>
									<th scope="col">학위</th>
									<th scope="col">대학</th>									
									<th scope="col">학력취득년월</th>									
									<th scope="col">세부전공</th>
								</thead>
								<tbody id="poolDegreeTableBody" >
								
								</tbody>
							</table>
							</div>
							
						</div>
						<!-- //학력 -->
					
					    <!-- 재직경력 -->
						<div class="form_box">
							<div class="form_title">
								<div class="fl_left">
									<h3>재직경력</h3><span>* 교수 전임교원, 경력만 기입, 교직원은 대학 재직 경력만 기입</span>
								</div>															
							</div>													
							<div class="form_table">
							<table id="fulltimeTable" class="list" summary="재직경력">
								<caption>No.,근무시작년월,근무종료년월,대학,부서(학과) 정보를 확인할 수 있습니다.</caption>
								<colgroup class="dp_n_t dp_n_m">
									<col style="width:3%;" />
									<col style="width:*" />
									<col style="width:*" />
									<col style="width:*" />
									<col style="width:*" />
								</colgroup>
								<thead class="dp_n_t dp_n_m">
									<th scope="col">No.</th>
									<th scope="col">근무시작년월</th>
									<th scope="col">근무종료년월</th>
									<th scope="col">대학</th>
									<th scope="col">부서(학과)</th>
								</thead>
								<tbody id="poolFulltimeTableBody">
														
								</tbody>
							</table>
							</div>
						</div>
						<!-- //재직경력 -->
					    
					
					    <!-- 진단(평가)위원 활동경험 -->
						<div class="form_box">
							<div class="form_title">
								<div class="fl_left">
									<h3>진단(평가)위원 활동경험</h3>
								</div>								
							</div>
							<div class="form_table">
							<table id="evaExp1Table" class="list" summary="진단(평가)위 활동경험">
								<caption>No.,참여연도,진단명,주관기관,역할 정보를 확인할 수 있습니다.</caption>
								<colgroup class="dp_n_t dp_n_m">
									<col style="width:3%;" />
									<col style="width:*" />
									<col style="width:*" />
									<col style="width:*" />
									<col style="width:*" />
								</colgroup>
								<thead class="dp_n_t dp_n_m">
									<th scope="col">No.</th>
									<th scope="col">참여연도</th>
									<th scope="col">진단명</th>
									<th scope="col">주관기관</th>
									<th scope="col">역할</th>
								</thead>
								<tbody id="poolEvaExp1TableBody">
															
								</tbody>
							</table>
							</div>
						</div>
						<!-- //진단(평가)위 활동경험 -->
					
					
					    <!-- 진단(평가) 준비위원 활동경험 -->
						<div class="form_box">
							<div class="form_title">
								<div class="fl_left">
									<h3>진단(평가) 준비위원 활동경험</h3><span>* 외부 진단에 대비한 준비위 경험  ex) 2017/교원양성기관역량진단/자체진단위원장</span>
								</div>								
							</div>
							<div class="form_table">
							<table id="evaExp2Table" class="list" summary="진단(평가) 준비위원 활동경험">
								<caption>No.,참여연도,진단명,주관기관,역할 정보를 확인할 수 있습니다.</caption>
								<colgroup class="dp_n_t dp_n_m">
									<col style="width:3%;" />
									<col style="width:*" />
									<col style="width:*" />
									<col style="width:*" />
									<col style="width:*" />
								</colgroup>
								<thead class="dp_n_t dp_n_m">
									<th scope="col">No.</th>
									<th scope="col">참여연도</th>
									<th scope="col">진단명</th>
									<th scope="col">주관기관</th>
									<th scope="col">역할</th>
								</thead>
								<tbody id="poolEvaExp2TableBody">
													
								</tbody>
							</table>
							</div>
						</div>
						<!-- //진단(평가) 준비위원 활동경험 -->						
						
						 <!-- 기피제척대상 대학 확인 -->
						<div class="form_box">
							<div class="form_title">
								<div class="fl_left">
									<h3>기피제척대상 대학 확인</h3>
									<div class="add_list">
										<span class="txt01">현직 특정 대학 총장, 학장, 이사장, 이사의 직계가족 또는 그 배우자 있는 기피제척대학정보</span>
                                    </div>									
								</div>								
							</div>
							
							<div class="form_table">
							<table class="list" summary="기피제척대상 대학 확인">
								<caption>No.,기피제척대학 정보를 확인할 수 있습니다.</caption>
								<colgroup>
									<col style="width:3%;" />
									<col style="width:*" />
								</colgroup>
								<thead>
									<th scope="col">No.</th>
									<th scope="col">기피제척대학</th>
								</thead>
								<tbody id ="poolDisq1UnivTableBody">
																	
								</tbody>
							</table>
							</div>
						</div>
						<!-- //기피제척대상 대학 확인 -->
					
					    <!-- 기피제척대상 대학 확인 -->
						<div class="form_box">
							<div class="form_title">
								<div class="fl_left">
									<div class="add_list">
										<span class="txt01">기타 개인적 사유에 의해 진단 업무를 수행하기 곤란한 기피제척대학정보</span>
                                    </div>									
								</div>							
							</div>
							
							<div class="form_table">
							<table class="list" summary="기피제척대상 대학 확인">
								<caption>No.,기피제척대학 정보를 확인할 수 있습니다.</caption>
								<colgroup>
									<col style="width:3%;" />
									<col style="width:*" />
								</colgroup>
								<thead>
									<th scope="col">No.</th>
									<th scope="col">기피제척대학</th>
								</thead>
								<tbody id ="poolDisq2UnivTableBody">
																
								</tbody>
							</table>
							</div>
						</div>
						<!-- //기피제척대상 대학 확인 -->
					
					
					    <!-- 대학관련 진단(평가)에서의 진단위원 및 준비위원 활동경험 -->
						<div class="form_box">
							<div class="form_title">
								<div class="fl_left">
									<h3>대학관련 진단(평가)에서의 진단위원 및 준비위원 활동경험</h3>
									<div class="add_list">
										<span class="txt01">* 교직원은 대학관련 진단(평가)에서의 업무 경험 위주로 기술</span>
                                    </div>
								</div>								
							</div>
							<div class="form_txt">
								<textarea  id="PoolInfoResume1" placeholder="내용을 입력해 주세요."></textarea>
							</div>
							
						</div>
						<!-- //대학관련 진단(평가)에서의 진단위원 및 준비위원 활동경험 -->
					
					
					    <!-- 전공분야 및 진단(평가) 관심영역 -->
						<div class="form_box">
							<div class="form_title">
								<div class="fl_left">
									<h3>전공분야 및 진단(평가) 관심영역</h3>
								</div>								
							</div>
							<div class="form_txt">
								<textarea id="PoolInfoResume2" placeholder="내용을 입력해 주세요."></textarea>
							</div>
							
						</div>
						<!-- //전공분야 및 진단(평가) 관심영역 -->				    				
						
					</div>
				</div>

				</div>
			</div>

		</div>
		<!-- //container -->
	</div>
</div>

<script type="text/javascript">
function openUpPoolInfo(rowData,additionalInfo) {
	
	var usrId = rowData.USR_ID;
	var di = rowData.di;
	var periodDivCd = additionalInfo.periodDivCd;
	var evaYear = additionalInfo.evaYear;
	var poolCd = additionalInfo.poolCd;
	    
	$.ajax({
        url: "<%=request.getContextPath() %>/adminSchedule/getPoolInfo.do", // 서버 URL 수정 필요
        type: "post",
        data: { usrId: usrId,periodDivCd:periodDivCd,evaYear:evaYear,poolCd:poolCd }, // 서버에 넘길 데이터
        dataType: "json",
        success: function(data) {
            if (data.result == "S") {
                console.log("신청서 정보 받아오기 성공");
                fillDialogWithData(data); // 다이얼로그에 데이터 채우는 함수 호출
            } else {
                console.log("신청서 정보 받아오기 실패");
            }
        },
        error: function() {
            alert("데이터 로딩 실패");
        }
    });
}

function fillDialogWithData(data) {
   
	var poolInfo = data.poolData;
	
	
	$("#PoolInfoJobCd").val(poolInfo.JOB_CD);
	$("#PoolInfoName").val(poolInfo.NAME);						
	$("#PoolInfoGender").val(poolInfo.GENDER);
	$("#PoolInfoBirthday").val(poolInfo.BIRTHDAY);
	$("#PoolInfoUnivDivNm").val(poolInfo.UNIV_DIV_NM);
	$("#PoolInfoUnivDiv").val(poolInfo.UNIV_DIV);
	$("#PoolInfoUnivCd").val(poolInfo.UNIV_CD);
	$("#PoolInfoJobNm").val(poolInfo.JOB_NM);
	$("#PoolInfoDept").val(poolInfo.DEPT);
	$("#PoolInfoPosition").val(poolInfo.POSITION);
	$("#PoolInfoEmail").val(poolInfo.EMAIL);
	$("#PoolInfoTelHp").val(poolInfo.TEL_HP);
	$("#PoolInfoTelHome").val(poolInfo.TEL_HOME);
	$("#sample4_postcode").val(poolInfo.ADDR_HOME_ZIPCODE);
	$("#sample4_roadAddress").val(poolInfo.ADDR_HOME);
	$("#sample4_detailAddress").val(poolInfo.ADDR_HOME_DETAIL);
	$("#PoolInfoResume1").val(poolInfo.RESUME_1);
	$("#PoolInfoResume2").val(poolInfo.RESUME_2);					

		//학력
		var poolDegreeList = data.poolDegreeList
		var tbody = $("#poolDegreeTableBody");
		
		tbody.empty();
		
		if (poolDegreeList && poolDegreeList.length > 0) {
		 
		   for (var i = 0; i < poolDegreeList.length; i++) {
		        var degreeData = poolDegreeList[i];
		        var row = $("<tr>");		       
		        row.append($("<td>").append($("<span>").text(degreeData.DEG_CD).attr("title", "학위코드").attr("id", "degCd").prop("readonly", true)));
		        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "학위").attr("id", "degCdNm").val(degreeData.DEG_CD_NM).prop("readonly", true)));
		        row.append($("<td>").css("display", "none").attr("id", "degUnivCd").val(degreeData.UNIV_CD));
		        row.append($("<td>").append(
		        	    $("<div>").css("display", "flex").css("align-items", "center").append(
		        	        $("<input>").attr("type", "text").attr("title", "대학").attr("id", "degUnivNm").val(degreeData.UNIV_NM).prop("readonly", true)
		        	    )
		        	));					       
		        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "학력취득년월").attr("id", "degEduYm").val(degreeData.EDU_YM)));
		        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "세부전공").attr("id", "degMajor").val(degreeData.MAJOR)));
		        tbody.append(row);
		    }
		} else {
			
			 var defaultDegrees = ["학사", "석사", "박사"]; // 기본 학위 값
			 var defaultRowCount = defaultDegrees.length; // 기본 행 수
	
			 for (var i = 0; i < defaultRowCount; i++) {
			      var row = $("<tr>");
			      row.append($("<td>").text(i + 1));
			      row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "학위").attr("id", "degCdNm").val(defaultDegrees[i]).prop("readonly", true)));
			      row.append($("<td>").css("display", "none").attr("id", "degUnivCd"));
			      row.append($("<td>").append(
			        	    $("<div>").css("display", "flex").css("align-items", "center").append(
			        	        $("<input>").attr("type", "text").attr("title", "대학").attr("id", "degUnivNm").prop("readonly", true)
			        	    )
			        	));								     
			      row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "학력취득년월").attr("id", "degEduYm")));						   
			      row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "세부전공").attr("id", "degMajor")));	
			      row.append($('<td><button type="button" class="deleteButton"><img src="<%=request.getContextPath() %>/images/icon_del.png" alt="삭제"/></button></td>'));
			      
			      (function (index) {
				        row.find('#btnDegreeSchlSearch').click(function() {						        	
				        	openSchSearchPopup2("degree", index); 	
				            console.log("대학검색 버튼 클릭됨"+index);						            
				        });
			        })(i);
			      
			      tbody.append(row);							      
			   
			     
			 }				
		}
		
		
		//재직경력
		var poolFulltimeTeacherList = data.poolFulltimeTeacherList
		var tbody = $("#poolFulltimeTableBody");
	
		tbody.empty();
		
		if (poolFulltimeTeacherList && poolFulltimeTeacherList.length > 0) {
		 	
		   for (var i = 0; i < poolFulltimeTeacherList.length; i++) {
		        var fullTimeData = poolFulltimeTeacherList[i];
		        var row = $("<tr>");
		        row.append($("<td>").text(i + 1));
		        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "근무시작년월").attr("id", "fttJobStYm").val(fullTimeData.JOB_ST_YM)));
		        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "근무종료년월").attr("id", "fttJobEdYm").val(fullTimeData.JOB_ED_YM)));
		        row.append($("<td>").css("display", "none").attr("id", "fttUnivCd").val(fullTimeData.UNIV_CD));
		        row.append($("<td>").append(
		        	    $("<div>").css("display", "flex").css("align-items", "center").append(
		        	        $("<input>").attr("type", "text").attr("title", "대학").attr("id", "fttUnivNm").val(fullTimeData.UNIV_NM).prop("readonly", true)
		        	    )
		        	));					       
		        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "부서(학과)").attr("id", "fttDept").val(fullTimeData.DEPT)));	
		        tbody.append(row);
		    }
		} else {
			 var defaultRowCount = 1;
			    for (var i = 0; i < defaultRowCount; i++) {
			        var row = $("<tr>");
			        row.append($("<td>").text(i + 1));
			        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "근무시작년월").attr("id", "fttJobStYm")));
			        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "근무종료년월").attr("id", "fttJobEdYm")));
			        row.append($("<td>").css("display", "none").attr("id", "fttUnivCd"));
			        row.append($("<td>").append(
			        	    $("<div>").css("display", "flex").css("align-items", "center").append(
			        	        $("<input>").attr("type", "text").attr("title", "대학").attr("id", "fttUnivNm").prop("readonly", true)
			        	    )
			        	));					       
			        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "부서(학과)").attr("id", "fttDept")));				        
			        tbody.append(row);	
				}
		   
		}
		 
		
		//진단(평가)위 활동경험
		var poolEvaExp1List = data.poolEvaExp1List
		var tbody = $("#poolEvaExp1TableBody");					
	
		tbody.empty();
		
		if (poolEvaExp1List && poolEvaExp1List.length > 0) {
		 
		   for (var i = 0; i < poolEvaExp1List.length; i++) {
		        var exp1Data = poolEvaExp1List[i];
		        var row = $("<tr>");
		        row.append($("<td>").text(i + 1));
		        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "참여연도").attr("id", "partYear").val(exp1Data.PART_YEAR)));
		        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "진단명").attr("id", "evaNm").val(exp1Data.EVA_NM)));
		        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "주관기관").attr("id", "org").val(exp1Data.ORG)));
		        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "역할").attr("id", "roleCdNm").val(exp1Data.ROLE_CD_NM)));
		        tbody.append(row);
		    }
		} else {
			  var defaultRowCount = 1;
			    for (var i = 0; i < defaultRowCount; i++) {
			        var row = $("<tr>");
			        row.append($("<td>").text(i + 1));
			        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "참여연도").attr("id", "partYear")));
			        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "진단명").attr("id", "evaNm")));
			        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "주관기관").attr("id", "org")));
			        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "역할").attr("id", "roleCdNm")));
			        tbody.append(row);	
				}
		}	    
		
		//진단(평가) 준비위원 활동경험
		var poolEvaExp2List = data.poolEvaExp2List
		var tbody = $("#poolEvaExp2TableBody");
	
		tbody.empty();
		
		if (poolEvaExp2List && poolEvaExp2List.length > 0) {
		 
		   for (var i = 0; i < poolEvaExp2List.length; i++) {
		        var exp2Data = poolEvaExp2List[i];
		        var row = $("<tr>");
		        row.append($("<td>").text(i + 1));
		        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "참여연도").attr("id", "partYear").val(exp2Data.PART_YEAR)));
		        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "진단명").attr("id", "evaNm").val(exp2Data.EVA_NM)));
		        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "주관기관").attr("id", "org").val(exp2Data.ORG)));
		        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "역할").attr("id", "roleCdNm").val(exp2Data.ROLE_CD_NM)));
		        tbody.append(row);
		    }
		} else {
			 
			  var defaultRowCount = 1;
			    for (var i = 0; i < defaultRowCount; i++) {
			        var row = $("<tr>");
			        row.append($("<td>").text(i + 1));
			        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "참여연도").attr("id", "partYear")));
			        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "진단명").attr("id", "evaNm")));
			        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "주관기관").attr("id", "org")));
			        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "역할").attr("id", "roleCdNm")));
			        tbody.append(row);// 데이터가 없는 경우 빈 입력 칸을 보여줍니다.
			    }
		}	
		
		//기피제척대상 대학
		var poolDisq1UnivList = data.poolDisq1UnivList
		console.log("poolDisq1UnivList"+poolDisq1UnivList);
		var tbody = $("#poolDisq1UnivTableBody");
	
		tbody.empty();
		
		if (poolDisq1UnivList && poolDisq1UnivList.length > 0) {
		 
		   for (var i = 0; i < poolDisq1UnivList.length; i++) {
		        var disq1Data = poolDisq1UnivList[i];
		        var row = $("<tr>");
		        row.append($("<td>").text(i + 1));
		        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "기피제척대학").attr("id", "disq1UnivNm").val(disq1Data.DISQ_UNIV_NM)));		       
		        tbody.append(row);
		    }
		  
		} else {
			 
			  var defaultRowCount = 1;
			    for (var i = 0; i < defaultRowCount; i++) {
			        var row = $("<tr>");
			        row.append($("<td>").text(i + 1));
			        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "기피제척대학").attr("id", "disq1UnivNm")));			      
			        tbody.append(row);// 데이터가 없는 경우 빈 입력 칸을 보여줍니다.
			    }
		}	
		
		
		//기피제척대상 대학
		var poolDisq2UnivList = data.poolDisq2UnivList
		var tbody = $("#poolDisq2UnivTableBody");
	
		tbody.empty();
		
		if (poolDisq2UnivList && poolDisq2UnivList.length > 0) {
		 
		   for (var i = 0; i < poolDisq2UnivList.length; i++) {
		        var disq2Data = poolDisq2UnivList[i];
		        var row = $("<tr>");
		        row.append($("<td>").text(i + 1));
		        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "기피제척대학").attr("id", "disq2UnivNm").val(disq2Data.DISQ_UNIV_NM)));		       
		        tbody.append(row);
		    }
		} else {
			 
			  var defaultRowCount = 1;
			    for (var i = 0; i < defaultRowCount; i++) {
			        var row = $("<tr>");
			        row.append($("<td>").text(i + 1));
			        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "기피제척대학").attr("id", "disq2UnivNm")));			      
			        tbody.append(row);// 데이터가 없는 경우 빈 입력 칸을 보여줍니다.
			    }
		}	
		
    // 다이얼로그를 여기서 열거나 업데이트
    $("#dialog").dialog({
        width: 800,
        height: 600,
        modal: true,
        buttons: {
            "닫기": function() {
                // 저장 로직
                $(this).dialog("close");
            }
        }
    });
}
	
	
	
	
	
	
	
</script>
</body>
</html>