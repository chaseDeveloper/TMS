<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<meta http-equiv="content-type" content="text/html;charset=UTF-8"/>

<style>
  #lastDegCertFile {
    line-height: 40px;
    padding: 0;
  }
</style>


<body>
	<!-- skip navi -->
	<ul id="skip_navi">
		<li><a href="#content">본문 바로가기</a></li>
		<li><a href="#gnb">메인메뉴 바로가기</a></li>
	</ul>
	<!-- //skip navi -->

	<div id="wrapper">
		<jsp:include page="../common/header.jsp" flush="false"></jsp:include>

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
										<td><div class="w_20"><input type="text" title="대학명" id="PoolInfoJobNm" readonly></div>
											<button type="button" title="대학검색" onclick="" class="btn_inquiry"  id="btnSchlSearch">대학검색</button></td>
									</tr>
									<tr>
										<th>부서(학과)</th>
										<td><div class="w_20"><input type="text" title="부서(학과)" id="PoolInfoDept"></div></td>
									</tr>							  
									<tr>
										<th>직급</th>
										<td><div class="w_20"><input type="text" title="직급" id="PoolInfoPosition"></div></td>
									</tr>
									<th>재직증명서<br><small>(pdf만 업로드 가능)</small></th>
										<td>
											<div class="w_20">
												<input type="hidden" id="fileState" value="K">
												<div style="flex:1; display:flex; justify-content:flex-start;align-items:center" id="lastDegCertFileDiv"></div>
											</div>
										</td>																
									</tr>
									<tr>
										<th>E-Mail</th>
										<td><div class="w_20"><input type="text" title="E-Mail" id="PoolInfoEmail"></div></td>																
									</tr>
									
									<tr>
										<th>연락처(핸드폰)</th>
										<td><div class="w_20"><input type="text" title="연락처(핸드폰)" id="PoolInfoTelHp" disabled></div></td>
									</tr>
									<!-- <tr>
										<th>연락처(자택)</th>
										<td><div class="w_20"><input type="text" title="연락처(자택)" id="PoolInfoTelHome"></div></td>
									</tr> -->
									<tr>
										<th>주소</th>
										<td><div class="w_30"><input type="text" id="sample4_postcode" placeholder="우편번호를 검색해 주세요." title="우편번호를 검색해 주세요." readonly></div><button type="button" title="주소검색" onclick="sample4_execDaumPostcode()" class="btn_inquiry">주소검색</button>
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
									<col style="width:5%;" />
									<col style="width:*" />									
									<col style="width:*" />
									<col style="width:*" />
									<col style="width:3%;" />
								</colgroup>
								<thead class="dp_n_t dp_n_m">
									<th scope="col">No.</th>
									<th scope="col">학위</th>
									<th scope="col">대학</th>									
									<th scope="col">학력취득년월(예: 200306)</th>									
									<th scope="col">세부전공</th>
									<th scope="col">대학명삭제</th>
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
								<div class="fl_right">										
									<button type="button" id="addFulltimeRow" title="추가"><img src="<%=request.getContextPath() %>/images/icon_add.png" alt="추가"/></button>																			 
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
									<col style="width:3%;" />
								</colgroup>
								<thead class="dp_n_t dp_n_m">
									<th scope="col">No.</th>
									<th scope="col">근무시작년월(예: 200103)</th>
									<th scope="col">근무종료년월(예: 200207)<br>*재직 중인 경우 999912로 작성</th>
									<th scope="col">대학</th>
									<th scope="col">부서(학과)</th>
									<th scope="col">삭제</th>
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
								<div class="fl_right">										
									<button type="button" id="addEvaExp1Row" title="추가"><img src="<%=request.getContextPath() %>/images/icon_add.png" alt="추가"/></button>																			 
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
									<col style="width:3%;" />
								</colgroup>
								<thead class="dp_n_t dp_n_m">
									<th scope="col">No.</th>
									<th scope="col">참여연도(예: 2024)</th>
									<th scope="col">진단명</th>
									<th scope="col">주관기관</th>
									<th scope="col">역할</th>
									<th scope="col">삭제</th>
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
									<h3>진단(평가) 준비위원 활동경험</h3><span>* 외부 진단에 대비한 준비위원 경험  ex) 2017/교원양성기관역량진단/자체진단위원장</span>
								</div>
								<div class="fl_right">
									<button type="button" id="addEvaExp2Row" title="추가"><img src="<%=request.getContextPath() %>/images/icon_add.png" alt="추가"/></button>
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
									<col style="width:3%;" />
								</colgroup>
								<thead class="dp_n_t dp_n_m">
									<th scope="col">No.</th>
									<th scope="col">참여연도(예: 2024)</th>
									<th scope="col">진단명</th>
									<th scope="col">주관기관</th>
									<th scope="col">역할</th>
									<th scope="col">삭제</th>
								</thead>
								<tbody id="poolEvaExp2TableBody">
													
								</tbody>
							</table>
							</div>
						</div>
						<!-- //진단(평가) 준비위원 활동경험 -->						
							
					
					
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
					   
					
					    <!-- btn -->
					 	<div class="btn_box_right">
							<button type="button" title="저장" class="btn_default" onclick="updatePoolInfo()">저장</button>
						</div>
						<!-- //btn -->					
						
					</div>
				</div>

				</div>
			</div>

		</div>
		<!-- //container -->


		<jsp:include page="../common/footer.jsp" flush="false"></jsp:include>


	</div>
<%@ include file="popupSchSearch.jsp" %>
<%@ include file="popupSchSearch2.jsp" %>
	


<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		
		$("#lastDegCertFileDiv").html('');
		fn_getPoolInfo();	
		
		//재직경력 추가 버튼 클릭 시
	    $("#addFulltimeRow").on("click", function() {
	        // 현재 행 번호를 계산
	        var index = $("#fulltimeTable tbody tr").length +1;

	        // 새로운 행을 생성
	        var newRow = $("<tr>");
	        newRow.html('<td class="dp_n_t dp_n_m">' + index + '</td>' +
	                    '<td><span class="dp_n_p">근무시작년월</span><input type="text" title="근무시작년월" id="fttJobStYm"></td>' +
	                    '<td><span class="dp_n_p">근무종료년월</span><input type="text" title="근무종료년월" id="fttjobEdYm"></td>' +
	        			'<td style="display: none;"><input type="hidden" title="대학코드" id="fttUnivCd"></td>'+
	                    '<td>' +
	                    '<span class="dp_n_p">대학</span>' +
	                    '<div style="display: flex; align-items: center;">' +
	                        '<input type="text" title="대학" id="fttUnivNm" readonly>' +
	                        '<button type="button" class="btn_search" title="대학검색" id="btnFulltimeSchlSearch" style="margin-left: 5px;">검색</button>' +
	                    '</div>' +	                   
	                    '<td><span class="dp_n_p">학과</span><input type="text" title="학과" id="fttdept"></td>'+
	                    '<td><button type="button" class="deleteButton"><img src="<%=request.getContextPath() %>/images/icon_del.png" alt="삭제"/></button></td>');

	        // 새로운 행을 테이블에 추가
	        $("#fulltimeTable tbody").append(newRow);
	        
	     // 대학검색 버튼 클릭 이벤트 핸들러 추가
	        newRow.find('#btnFulltimeSchlSearch').click(function() {
	            // 대학검색 팝업 또는 다른 동작 수행
	            openSchSearchPopup2("fulltime", index-1);
	            console.log("대학검색 버튼 클릭됨" + index);
	        });
	     
	    });
		//재직경력  즌비위원 활동경험 삭제 버튼
	    $("#fulltimeTable").on("click", ".deleteButton", function() {
	    	  $(this).closest("tr").remove();
	    });
		
	    //진단(평가)위 활동경험 추가 버튼 클릭 시
	    $("#addEvaExp1Row").on("click", function() {
	        // 현재 행 번호를 계산
	        var rowCount = $("#evaExp1Table tbody tr").length + 1;

	        // 새로운 행을 생성
	        var newRow = $("<tr>");
	        newRow.html('<td class="dp_n_t dp_n_m">' + rowCount + '</td>' +
	                    '<td><span class="dp_n_p">참여연도</span><input type="text" title="참여연도" id="partYear"></td>' +
	                    '<td><span class="dp_n_p">진단명</span><input type="text" title="진단명" id="evaNm"></td>' +
	                    '<td><span class="dp_n_p">주관기관</span><input type="text" title="주관기관" id="org"></td>' +
	                    '<td><span class="dp_n_p">역할</span><input type="text" title="역할" id="roleCdNm"></td>'+
	                    '<td><button type="button" class="deleteButton"><img src="<%=request.getContextPath() %>/images/icon_del.png" alt="삭제"/></button></td>');

	        // 새로운 행을 테이블에 추가
	        $("#evaExp1Table tbody").append(newRow);
	    });
	    
		//진단(평가)위 활동경험  즌비위원 활동경험 삭제 버튼
	    $("#evaExp1Table").on("click", ".deleteButton", function() {
	    	  $(this).closest("tr").remove();
	    });
		
		//진단 즌비위원 활동경험 추가 버튼 클릭 시
	    $("#addEvaExp2Row").on("click", function() {
	        // 현재 행 번호를 계산
	        var rowCount = $("#evaExp2Table tbody tr").length + 1;

	        // 새로운 행을 생성
	        var newRow = $("<tr>");
	        newRow.html('<td class="dp_n_t dp_n_m">' + rowCount + '</td>' +
	                    '<td><span class="dp_n_p">참여연도</span><input type="text" title="참여연도" id="partYear"></td>' +
	                    '<td><span class="dp_n_p">진단명</span><input type="text" title="진단명" id="evaNm" ></td>' +
	                    '<td><span class="dp_n_p">주관기관</span><input type="text" title="주관기관" id="org"></td>' +
	                    '<td><span class="dp_n_p">역할</span><input type="text" title="역할"id="roleCdNm"></td>'+
	                    '<td><button type="button" class="deleteButton"><img src="<%=request.getContextPath() %>/images/icon_del.png" alt="삭제"/></button></td>');

	        // 새로운 행을 테이블에 추가
	        $("#evaExp2Table tbody").append(newRow);
	    });
		//진단 즌비위원 활동경험 삭제 버튼
	    $("#evaExp2Table").on("click", ".deleteButton", function() {
	    	  $(this).closest("tr").remove();
	    });	
		
	  	//학력 대학 데이터 삭제 버튼
	    $("#poolDegreeTableBody").on("click", ".deleteButton", function() {
	    	var row = $(this).closest("tr");
	        row.find("#degUnivCd, #degUnivNm").val(""); 
	    });	
	  	
	    //신상정보대학검색팝업
		$('#btnSchlSearch').click(function() {			 
			
			  openSchSearchPopup();		    		
		});	  
	
	    $("#fttJobEdYm").on("focus", function () {
	        if (isFirstInput) {
	            alert("재직 중인 경우 99912로 작성해주세요.");
	            isFirstInput = false; // 한 번만 알림 표시
	        }
	    });
	}); 

	
	function fn_getPoolInfo() {
		
		$.ajax({
			url:"<%=request.getContextPath() %>/pool/getPoolInfo.do",
			type:"post",
			cache: false,
			data:{},			
			dataType : "json",
			success:function(data){
				if(data.result == "S") {
					console.log("신청서 정보");					
				
					var poolInfo = data.poolData
					$("#PoolInfoJobCd").val(poolInfo.JOB_CD);
					$("#PoolInfoName").val(poolInfo.NAME);						
					$("#PoolInfoGender").val(poolInfo.GENDER);
					$("#PoolInfoBirthday").val(poolInfo.BIRTHDAY);
					$("#PoolInfoUnivDivNm").val(poolInfo.UNIV_DIV_NM);
					$("#PoolInfoUnivDiv").val(poolInfo.UNIV_DIV);
					$("#PoolInfoUnivCd").val(poolInfo.UNIV_CD);
					$("#PoolInfoJobNm").val(poolInfo.UNIV_CD_NM);
					$("#PoolInfoDept").val(poolInfo.DEPT);
					$("#PoolInfoPosition").val(poolInfo.POSITION);
					$("#PoolInfoEmail").val(poolInfo.EMAIL);
					$("#PoolInfoTelHp").val(poolInfo.TEL_HP);
					/* $("#PoolInfoTelHome").val(poolInfo.TEL_HOME); */
					$("#sample4_postcode").val(poolInfo.ADDR_HOME_ZIPCODE);
					$("#sample4_roadAddress").val(poolInfo.ADDR_HOME);
					$("#sample4_detailAddress").val(poolInfo.ADDR_HOME_DETAIL);
					$("#PoolInfoResume1").val(poolInfo.RESUME_1);
					$("#PoolInfoResume2").val(poolInfo.RESUME_2);		
					
					if(poolInfo.FILE_NM != null){
						const url = "<%=request.getContextPath() %>/cmmn/fileDownload.do?fileId=" + poolInfo.FILE_ID;
						
						let html = '';
						html += '<a style="text-decoration: underline;" href="'+ url + '" target="emptyFrm" OLD_FILE_ID="' + poolInfo.FILE_ID + '">'+ poolInfo.FILE_NM + '</a>';
						html += '<button type="button" style="padding:.2rem .5rem; margin-left:5px;" class="ui-button ui-corner-all ui-widget" onclick="delFile();">삭제</button>';

						$("#lastDegCertFileDiv").append(html);
					} else {
						
						let html = '';
						html += '<input type="file" style="width:100%; display:block; margin:0 auto;" id="lastDegCertFile" onchange="handleFileChange(this)" />';
						$("#lastDegCertFileDiv").append(html);
					}
					
					//학력
					var poolDegreeList = data.poolDegreeList
					var tbody = $("#poolDegreeTableBody");
					
					tbody.empty();
					
					if (poolDegreeList && poolDegreeList.length > 0) {
					 
					   for (var i = 0; i < poolDegreeList.length; i++) {
					        var degreeData = poolDegreeList[i];
					        var row = $("<tr>");
					        //row.append($("<td>").text(i + 1));
					        row.append($("<td>").append($("<span>").text(degreeData.DEG_CD).attr("title", "학위코드").attr("id", "degCd").prop("readonly", true)));
					        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "학위").attr("id", "degCdNm").val(degreeData.DEG_CD_NM).prop("readonly", true)));
					        row.append($("<td>").css("display", "none").attr("id", "degUnivCd").val(degreeData.UNIV_CD));
					        row.append($("<td>").append(
					        	    $("<div>").css("display", "flex").css("align-items", "center").append(
					        	        $("<input>").attr("type", "text").attr("title", "대학").attr("id", "degUnivNm").val(degreeData.UNIV_NM).prop("readonly", true),
					        	        $('<button type="button" class="btn_search" title="대학검색" id="btnDegreeSchlSearch">검색</button>').css("margin-left", "5px")
					        	    )
					        	));					       
					        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "학력취득년월").attr("id", "degEduYm").val(degreeData.EDU_YM)));
					        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "세부전공").attr("id", "degMajor").val(degreeData.MAJOR)));
					        row.append($('<td><button type="button" class="deleteButton"><img src="<%=request.getContextPath() %>/images/icon_del.png" alt="삭제"/></button></td>'));
					        
					        (function (index) {
						        row.find('#btnDegreeSchlSearch').click(function() {						        	
						        	openSchSearchPopup2("degree", index); 	
						            console.log("대학검색 버튼 클릭됨"+index);						            
						        });
					        })(i);
					      
					        tbody.append(row);
					    }
					} else {
						
						 var defaultDegrees = ["학사", "석사", "박사"]; // 기본 학위 값
						 var defaultRowCount = defaultDegrees.length; // 기본 행 수

						 for (var i = 0; i < defaultRowCount; i++) {
						      var row = $("<tr>");
						      row.append($("<td>").text(i + 1).attr("id", "degCd"));
						      row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "학위").attr("id", "degCdNm").val(defaultDegrees[i]).prop("readonly", true)));
						      row.append($("<td>").css("display", "none").attr("id", "degUnivCd"));
						      row.append($("<td>").append(
						        	    $("<div>").css("display", "flex").css("align-items", "center").append(
						        	        $("<input>").attr("type", "text").attr("title", "대학").attr("id", "degUnivNm").prop("readonly", true),
						        	        $('<button type="button" class="btn_search" title="대학검색" id="btnDegreeSchlSearch">검색</button>').css("margin-left", "5px")
						        	    )
						        	));								     
						      row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "학력취득년월").attr("id", "degEduYm").attr("placeholder","예:200306")));						   
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
					        	        $("<input>").attr("type", "text").attr("title", "대학").attr("id", "fttUnivNm").val(fullTimeData.UNIV_NM).prop("readonly", true),
					        	        $('<button type="button" class="btn_search" title="대학검색" id="btnFulltimeSchlSearch">검색</button>').css("margin-left", "5px")
					        	    )
					        	));					       
					        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "부서(학과)").attr("id", "fttDept").val(fullTimeData.DEPT)));					       
					        row.append($('<td><button type="button" class="deleteButton"><img src="<%=request.getContextPath() %>/images/icon_del.png" alt="삭제"/></button></td>'));
					        
					        (function (index) {
						        row.find('#btnFulltimeSchlSearch').click(function() {						        	
						        	openSchSearchPopup2("fullTeacher", index); 	
						            console.log("대학검색 버튼 클릭됨"+index);						            
						        });
					        })(i);
					        
					        
					        tbody.append(row);
					    }
					} else {
						 var defaultRowCount = 1;
						    for (var i = 0; i < defaultRowCount; i++) {
						        var row = $("<tr>");
						        row.append($("<td>").text(i + 1));
						        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "근무시작년월").attr("id", "fttJobStYm").attr("placeholder","예:200103")));
						        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "근무종료년월").attr("id", "fttJobEdYm").attr("placeholder","예:200207")));
						        row.append($("<td>").css("display", "none").attr("id", "fttUnivCd"));
						        row.append($("<td>").append(
						        	    $("<div>").css("display", "flex").css("align-items", "center").append(
						        	        $("<input>").attr("type", "text").attr("title", "대학").attr("id", "fttUnivNm").prop("readonly", true),
						        	        $('<button type="button" class="btn_search" title="대학검색" id="btnFulltimeSchlSearch">검색</button>').css("margin-left", "5px")
						        	    )
						        	));					       
						        row.append($("<td>").append($("<input>").attr("type", "text").attr("title", "부서(학과)").attr("id", "fttDept")));	
						        row.append($('<td><button type="button" class="deleteButton"><img src="<%=request.getContextPath() %>/images/icon_del.png" alt="삭제"/></button></td>'));
						        
						        (function (index) {
							        row.find('#btnFulltimeSchlSearch').click(function() {						        	
							        	openSchSearchPopup2("fullTeacher", index); 	
							            console.log("대학검색 버튼 클릭됨"+index);						            
							        });
						        })(i);
						        
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
					        row.append($('<td><button type="button" class="deleteButton"><img src="<%=request.getContextPath() %>/images/icon_del.png" alt="삭제"/></button></td>'));
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
						        row.append($('<td><button type="button" class="deleteButton"><img src="<%=request.getContextPath() %>/images/icon_del.png" alt="삭제"/></button></td>'));
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
					        row.append($('<td><button type="button" class="deleteButton"><img src="<%=request.getContextPath() %>/images/icon_del.png" alt="삭제"/></button></td>'));
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
						        row.append($('<td><button type="button" class="deleteButton"><img src="<%=request.getContextPath() %>/images/icon_del.png" alt="삭제"/></button></td>'));
						        tbody.append(row);// 데이터가 없는 경우 빈 입력 칸을 보여줍니다.
						    }
					}		
					
					
					
				} else {
					console.log("실패");
				}	
			},
			error:function(key, textStatus, errorThrown){
				alert("에러 발생");
			}
		});	
		
	}
	
	
	function sample4_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var roadAddr = data.roadAddress; // 도로명 주소 변수
                var extraRoadAddr = ''; // 참고 항목 변수

                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraRoadAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                   extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraRoadAddr !== ''){
                    extraRoadAddr = ' (' + extraRoadAddr + ')';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('sample4_postcode').value = data.zonecode;
                document.getElementById("sample4_roadAddress").value = roadAddr;
               // document.getElementById("sample4_jibunAddress").value = data.jibunAddress;
                
                // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
                if(roadAddr !== ''){
                    document.getElementById("sample4_extraAddress").value = extraRoadAddr;
                } else {
                    document.getElementById("sample4_extraAddress").value = '';
                }

                var guideTextBox = document.getElementById("guide");
                // 사용자가 '선택 안함'을 클릭한 경우, 예상 주소라는 표시를 해준다.
               /*  if(data.autoRoadAddress) {
                    var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
                    guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
                    guideTextBox.style.display = 'block';

                } else if(data.autoJibunAddress) {
                    var expJibunAddr = data.autoJibunAddress;
                    guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
                    guideTextBox.style.display = 'block';
                } else {
                    guideTextBox.innerHTML = '';
                    guideTextBox.style.display = 'none';
                } */
            }
        }).open();
    }
	
	function updatePoolInfo() {
		console.log('updatePoolInfo 함수 실행');	
		
		
		var jobCd = $("#PoolInfoJobCd").val();
		var univDiv = $("#PoolInfoUnivDiv").val();				
		var jobNm = $("#PoolInfoJobNm").val();
		var univCd = $("#PoolInfoUnivCd").val();
		var dept = $("#PoolInfoDept").val();
		var position = $("#PoolInfoPosition").val();
		var email = $("#PoolInfoEmail").val();
		var telhp = $("#PoolInfoTelHp").val();
		/* var telhome = $("#PoolInfoTelHome").val(); */
		var AddrHomeZipcode = $("#sample4_postcode").val();		
		var AddrHome = $("#sample4_roadAddress").val();
		var AddrHomeDetail = $("#sample4_detailAddress").val();
		var resume1 = $("#PoolInfoResume1").val();
		var resume2 = $("#PoolInfoResume2").val();	
		var ctpvCd = $("#PoolInfoCtpvCd").val();
		
		//학력
		var degreeDataList = [];
	    $('#degreeTable tbody tr').each(function() {
	        var rowData = {
	        		 degCd: $(this).find("#degCd").text(),
	        		 degCdNm: $(this).find("#degCdNm").val(),
	        		 degUnivCd: $(this).find("#degUnivCd").val(),
	        		 degUnivNm: $(this).find("#degUnivNm").val(),
	        		 degEduYm: $(this).find("#degEduYm").val(),	        		
	        		 degMajor: $(this).find("#degMajor").val()
	        		
	        };  		 	
	        degreeDataList.push(rowData);
	    });
		console.log("degreeDataList"+degreeDataList);
		
	    //재직경력
		var fullTimeDataList = [];
	    $('#fulltimeTable tbody tr').each(function() {
	    	   var jobStYm = $(this).find("#fttJobStYm").val();
	           var jobEdYm = $(this).find("#fttjobEdYm").val();
	           var univCd = $(this).find("#fttUnivCd").val();
	           var univNm = $(this).find("#fttUnivNm").val();
	           var dept = $(this).find("#fttDept").val();
	       
	        // "근무시작년월" 필드가 비어있지 않은 경우에만 데이터를 추가
	        if (jobStYm || jobEdYm || univCd || univNm || dept) {
	            var rowData = {
	            		  fttJobStYm: jobStYm,
	                      fttjobEdYm: jobEdYm,
	                      fttUnivCd:  univCd,
	                      fttUnivNm: univNm,
	                      fttDept: dept
	            };
	            fullTimeDataList.push(rowData);
	        }
	    });
		
	    //진단(평가)위 활동경험
		var evaExp1DataList = [];
	    $('#evaExp1Table tbody tr').each(function() {
	    	 var exp1PartYear = $(this).find("#partYear").val();	  
	    	 var exp1evaNm = $(this).find("#evaNm").val();
	    	 var exp1org = $(this).find("#org").val();
	    	 var exp1roleCdNm = $(this).find("#roleCdNm").val();

	        // "진단명" 필드가 비어있지 않은 경우에만 데이터를 추가
	        if (exp1PartYear || exp1evaNm || exp1org || exp1roleCdNm) {
	            var rowData = {
	                partYear: exp1PartYear,
	                evaNm: exp1evaNm,
	                org: exp1org,
	                roleCdNm: exp1roleCdNm
	            };
	            evaExp1DataList.push(rowData);
	        }        
	    });
	  
	    //진단(평가) 준비위원 활동경험
		var evaExp2DataList = [];
	    $('#evaExp2Table tbody tr').each(function() {
	    	 var exp2PartYear = $(this).find("#partYear").val();	  
	    	 var exp2evaNm = $(this).find("#evaNm").val();
	    	 var exp2org = $(this).find("#org").val();
	    	 var exp2roleCdNm = $(this).find("#roleCdNm").val();     

	        // "진단명" 필드가 비어있지 않은 경우에만 데이터를 추가
	         if (exp2PartYear || exp2evaNm || exp2org || exp2roleCdNm) {
	            var rowData = {
	                partYear: exp2PartYear,
	                evaNm: exp2evaNm,
	                org: exp2org,
	                roleCdNm: exp2roleCdNm
	            };
	            evaExp2DataList.push(rowData);
	        }        
	    });    
	   
	    		
		var formData = new FormData();
		formData.append("jobCd", jobCd);		
		formData.append("univDiv", univDiv);
		formData.append("jobNm", jobNm);
		formData.append("univCd", univCd);
		formData.append("dept", dept);
		formData.append("position", position);
		formData.append("email", email);
		formData.append("telhp", telhp);
		/* formData.append("telhome", telhome); */
		formData.append("AddrHomeZipcode", AddrHomeZipcode);
		formData.append("AddrHome", AddrHome);
		formData.append("AddrHomeDetail", AddrHomeDetail);
		formData.append("resume1", resume1);
		formData.append("resume2", resume2);	
		formData.append("ctpvCd", ctpvCd);	
		formData.append('degreeDataList', JSON.stringify(degreeDataList));
		formData.append('fullTimeDataList', JSON.stringify(fullTimeDataList));
		formData.append('evaExp1DataList', JSON.stringify(evaExp1DataList));
		formData.append('evaExp2DataList', JSON.stringify(evaExp2DataList));		
		
		var fileState = $("#fileState").val();
		
		if(fileState === 'K'){
		} else {
			if(fileState === 'U' || ($("#lastDegCertFile").val() !== '' && $("#lastDegCertFile").val() !== undefined)){
				fileState = 'U';
				var lastDegCertFile = $("#lastDegCertFile")[0].files[0];
				formData.append("lastDegCertFile", lastDegCertFile);
			}
		}

		//테스트
		var oldFileId = $('a[target="emptyFrm"]').attr('OLD_FILE_ID');
		
 		if(oldFileId=="undefined" || oldFileId==""){
			oldFileId = null;
		}
		
		formData.append("fileState", fileState);
		formData.append("OLD_FILE_ID", oldFileId);	
		
		$.ajax({
			url:"<%=request.getContextPath() %>/pool/updateData.do",
			type:"post",
			cache: false,
			data:formData,
			enctype: 'form-data',
			contentType : false,
			processData: false,
			success:function(data){						
				 alert("저장 되었습니다.");						
				 fn_getPoolInfo();
				 location.reload(); 
			},
			error: function (xhr, textStatus, errorThrown) {
			      console.log(xhr.status); // 디버깅을 위해 상태 코드를 로깅
			       try {
			           const responseJSON = JSON.parse(xhr.responseText); // JSON 파싱
			           if (responseJSON.result === 'F') {
			               alert(responseJSON.msg); // 에러 메시지를 얼럿으로 표시
			           } else {
			               console.log("에러가 발생했습니다: " + textStatus + " - " + errorThrown);
			               alert("서버와의 통신 중에 오류가 발생했습니다.");
			           }
			       } catch (e) {
			           console.log("에러가 발생했습니다: " + textStatus + " - " + errorThrown);
			           alert("서버와의 통신 중에 오류가 발생했습니다.");
			       }
			}				
		});			
		
	}
	
	function delFile() {
	   	$("#fileState").val("D");
	   	
	   	var fileInput = $(document.createElement("input"))
        .attr("type", "file")
        .css("width", "100%")
        .attr("id", "lastDegCertFile")
        .change(function() {
            if(this.value !== ''){
            	$('#fileState').val('U');
            } else {
            	$('#fileState').val('D');
            }
        });
	   	
		$("#lastDegCertFileDiv").html(fileInput); // 파일 입력 필드 추가
	}
	
	function handleFileChange(inputElement) {
	    if(inputElement.value !== '') {
	    	$('#fileState').val('U');
	    }
	}

</script>
</body>
</html>