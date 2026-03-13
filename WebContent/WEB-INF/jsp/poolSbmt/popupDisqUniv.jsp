<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

	<div class="subContainer"  id="popupDisqUniv" style="display:none;"> 		
		<div id="content">
			<div class="con_box">						
			 			<!-- 기피제척대상 대학 확인 -->
						<div class="form_box">
							<div class="form_title">
								<div class="fl_left">
									<h3>기피제척대상 대학 확인</h3>
									<div class="add_list">
										<span class="txt01">현직 특정 대학 총장, 학장, 이사장, 이사의 직계가족 또는 그 배우자입니까?</span> <span class="txt02">예<input type="radio" name="disq1Univ" value="Y">
																															                        아니오<input type="radio" name="disq1Univ"  value="N" checked></span>
                                    </div>
									<div class="add_list">
										<span class="txt01"> * 위에서 “예” 선택시 해당 대학검색</span>
                                    </div>                                 
								</div>								
							   <div class="fl_right">
									<button type="button" title="대학검색" class="btn_search" id="disq1btnSchlSearch">대학검색</button>																				 
								</div>							
							</div>						
							<div class="form_table">
							<table id="disq1UnivTable" class="list_01" summary="기피제척대상 대학 확인">
								<caption>No.,기피제척대학 정보를 확인할 수 있습니다.</caption>
								<colgroup>
									<col style="width:3%;" />
									<col style="width:*" />
									<col style="width:3%;" />
								</colgroup>
								<thead>
									<th scope="col">No.</th>
									<th scope="col">기피제척대학</th>
									<th scope="col">삭제</th>
								</thead>
								<tbody id="poolDisq1UnivTableBody">
																		
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
										<span class="txt01">기타 개인적 사유에 의해 진단 업무를 수행하기 곤란한 대학이 있습니까?</span> <span class="txt02">예<input type="radio" name="disq2Univ" value="Y">
																																	 아니오<input type="radio" name="disq2Univ" value="N" checked></span>
                                    </div>
									<div class="add_list">
										<span class="txt01"> * 위에서 “예” 선택시 해당 대학검색</span>
                                    </div>                                    
								</div>	
								<div class="fl_right">
									<button type="button" title="대학검색" class="btn_search" id="disq2btnSchlSearch">대학검색</button>																													 
								</div>															
							</div>
							
							<div class="form_table">
							<table id="disq2UnivTable" class="list_01" summary="기피제척대상 대학 확인">
								<caption>No.,기피제척대학 정보를 확인할 수 있습니다.</caption>
								<colgroup>
									<col style="width:3%;" />
									<col style="width:*" />
									<col style="width:3%;" />
								</colgroup>
								<thead>
									<th scope="col">No.</th>
									<th scope="col">기피제척대학</th>
									<th scope="col">삭제</th>
								</thead>
								<tbody id="poolDisq2UnivTableBody">
																	
								</tbody>
							</table>
							</div>
						</div>
						<!-- //기피제척대상 대학 확인 -->
						 <!-- btn -->
					 	<!-- <div class="btn_box_right">
							<button type="button" title="저장" class="btn_default" onclick="updateDisqUniv()">저장</button>
						</div> -->
						<div class="btn_box_right">							
							<!-- <button type="button" title="제출취소" class="btn_gray" onclick="poolSubmitCancel()">제출취소</button> -->
							<button type="button" title="제출" class="btn_default" onclick="poolSubmit()">제출</button>
						</div>
						<!-- //btn -->	
						<input type="hidden" id="disqPeriodDivCd" />
						<input type="hidden" id="disqEvaYear" />
						<input type="hidden" id="disqPoolCd" />
			</div>
		</div>
	</div>
	






<style>
#disq1btnSchlSearch {
  margin-bottom: 5px; /* 원하는 크기로 마진을 지정합니다. */
}

#disq2btnSchlSearch {
  margin-bottom: 5px; /* 원하는 크기로 마진을 지정합니다. */
}
</style>
<script type="text/javascript">
$(document).ready(function() {	
	
	 
	 //기피제척대상 대학1 삭제 버튼
    $("#disq1UnivTable").on("click", ".deleteButton", function() {
    	  $(this).closest("tr").remove();
    });
    
    //기피제척대상 대학2  삭제 버튼
    $("#disq2UnivTable").on("click", ".deleteButton", function() {
    	  $(this).closest("tr").remove();
    });
    
  	//기피제척대학1 검색 팝업
	$('#disq1btnSchlSearch').click(function() {
		 var evaYear = $("#disqEvaYear").val(); 
		if ($("input[name='disq1Univ']:checked").val() !== "Y") {
		        alert("알림: 기피제척대학  확인 선택값이 '예'가 아닙니다.");
		} else {
		        // disq1Univ가 "Y"인 경우, 대학 검색 팝업을 열기
		        openDisqSchSearchPopup("disq1",evaYear);
	    }		
	});
    
	//기피제척대학2 검색 팝업
	$('#disq2btnSchlSearch').click(function() {
		 var evaYear = $("#disqEvaYear").val(); 
		if ($("input[name='disq2Univ']:checked").val() !== "Y") {
	        alert("알림: 기피제척대학  확인 선택값이 '예'가 아닙니다.");
		} else {
	        // disq2Univ가 "Y"인 경우, 대학 검색 팝업을 열기
	        openDisqSchSearchPopup("disq2",evaYear);
    	}			
	});    
  
    
});

	function openDisqUnivPopup(listData) {		
	 	
		$( "#popupDisqUniv" ).dialog({				
			title: '신청서 제출',
			resizable: false,
			height: 680,
			width: 700,    		
			modal: true
			
		});			
		

		if (listData.DISQ_YN_1 === "Y") {
		       $("input[name='disq1Univ'][value='Y']").prop('checked', true);
		} else {
		      
		       $("input[name='disq1Univ'][value='N']").prop('checked', true);
		}
		
		
		if (listData.DISQ_YN_2 === "Y") {
		       $("input[name='disq2Univ'][value='Y']").prop('checked', true);
		} else {
		      
		       $("input[name='disq2Univ'][value='N']").prop('checked', true);
		}
		
		
		$("input[name='disq1Univ']").on("change", function() {			
			var tbody = $("#poolDisq1UnivTableBody");
			
			 if ($(this).val() === "N") { // "아니요" 선택 시
			 
				 tbody.empty(); // 테이블의 자식 요소들을 모두 제거
			    
			 }
		});
		
		
		$("input[name='disq2Univ']").on("change", function() {			
			var tbody = $("#poolDisq2UnivTableBody");
			
			 if ($(this).val() === "N") { // "아니요" 선택 시
			 
				 tbody.empty(); // 테이블의 자식 요소들을 모두 제거
			    
			 }
		});
		fn_getDisqUnivInfo(listData);
	}
	
	function fn_getDisqUnivInfo(listData) {		
		
		
		$("#disqPeriodDivCd").val(listData.PERIOD_DIV_CD);
		$("#disqEvaYear").val(listData.EVA_YEAR);
		$("#disqPoolCd").val(listData.POOL_CD);
		
		$("#poolDisq1UnivTableBody").empty();
		$("#poolDisq2UnivTableBody").empty();
		
		$.ajax({
				url:"<%=request.getContextPath() %>/poolSbmt/getDisqUnivInfo.do",
				type:"post",
				cache: false,
				contentType: "application/json", 
				data:JSON.stringify(listData),			
				dataType : "json",
				success:function(data){
					if(data.result == "S") {
						
						console.log("기피제척대학 정보");				
						if (listData.DISQ_YN_1 === "Y") {
							
							//기피제척대상1 대학 
							var poolDisq1UnivList = data.poolDisq1UnivList
							var tbody = $("#poolDisq1UnivTableBody");						
							
							if (poolDisq1UnivList && poolDisq1UnivList.length > 0) {
							 
							   for (var i = 0; i < poolDisq1UnivList.length; i++) {
							        var disq1UnivData = poolDisq1UnivList[i];
							        var row = $("<tr>");
							        row.append($("<td>").text(i + 1));
							        row.append($("<input>").attr("type", "hidden").attr("id", "disq1UnivCd").val(disq1UnivData.DISQ_UNIV_CD));
							        row.append($("<td>").addClass("left").attr("id", "disq1UnivNm").text(disq1UnivData.DISQ_UNIV_NM));				       
							        row.append($('<td><button type="button" class="deleteButton"><img src="<%=request.getContextPath() %>/images/icon_del.png" alt="삭제"/></button></td>'));
							        tbody.append(row);
							    }
							} else {
								// 데이터가 없는 경우 빈 입력 칸을 보여줍니다.
							  
							}
						}
						
						if (listData.DISQ_YN_2 === "Y") {
							
							//기피제척대상2 대학 
							var poolDisq2UnivList = data.poolDisq2UnivList
							var tbody = $("#poolDisq2UnivTableBody");
							
							if (poolDisq2UnivList && poolDisq2UnivList.length > 0) {
							 
							   for (var i = 0; i < poolDisq2UnivList.length; i++) {
							        var disq2UnivData = poolDisq2UnivList[i];
							        var row = $("<tr>");
							        row.append($("<td>").text(i + 1));
							        row.append($("<input>").attr("type", "hidden").attr("id", "disq2UnivCd").val(disq2UnivData.DISQ_UNIV_CD));
							        row.append($("<td>").addClass("left").attr("id", "disq2UnivNm").text(disq2UnivData.DISQ_UNIV_NM));
							        row.append($('<td><button type="button" class="deleteButton"><img src="<%=request.getContextPath() %>/images/icon_del.png" alt="삭제"/></button></td>'));
							        tbody.append(row);
							    }
							} else {
								// 데이터가 없는 경우 빈 입력 칸을 보여줍니다.
							   
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
	
	<%-- function updateDisqUniv() {
		console.log('updateDisqUniv 함수 실행');		
	
		var periodDivCd = $("#disqPeriodDivCd").val();
		var evaYear = $("#disqEvaYear").val();
		var poolCd = $("#disqPoolCd").val();
		var disq1Univ =  $("input[name='disq1Univ']:checked").val();
		var disq2Univ =  $("input[name='disq2Univ']:checked").val();
		
	
	    //재직경력
		var disq1UnivDataList = [];
	    $('#disq1UnivTable tbody tr').each(function() {	    	
	    	var disq1UnivCd = $(this).find("#disq1UnivCd").val();
	    	
	    	if (disq1UnivCd){
	    		  
	    		var rowData = {
	  	        
	    				disq1UnivCd:  $(this).find("#disq1UnivCd").val()         
	    		};
	  	        
	  	        disq1UnivDataList.push(rowData);
	    	}
	      
	    });
		console.log(disq1UnivDataList);
		
		var disq2UnivDataList = [];
	    $('#disq2UnivTable tbody tr').each(function() {	    	
	    	var disq2UnivCd = $(this).find("#disq2UnivCd").val();
	    	
	    	if (disq2UnivCd){
	    		  
	    		var rowData = {
	  	        
	    				disq2UnivCd:  $(this).find("#disq2UnivCd").val()         
	    		};
	  	        
	    		disq2UnivDataList.push(rowData);
	    	}
	    });
		console.log(disq2UnivDataList);
	 	
		var formData = new FormData();	
		
		formData.append("periodDivCd", periodDivCd);
		formData.append("evaYear", evaYear);
		formData.append("poolCd", poolCd);
		formData.append("disq1Univ", disq1Univ);
		formData.append("disq2Univ", disq2Univ);
		formData.append('disq1UnivDataList', JSON.stringify(disq1UnivDataList));
		formData.append('disq2UnivDataList', JSON.stringify(disq2UnivDataList));	
		
		
		
		$.ajax({
			url:"<%=request.getContextPath() %>/pool/disqUnivUpdateData.do",
			type:"post",
			cache: false,
			data:formData,
			enctype: 'form-data',
			contentType : false,
			processData: false,
			success:function(data){						
				
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
		
	} --%>
	
	function poolSubmit() {
	    
	    
		var periodDivCd = $("#disqPeriodDivCd").val();
		var evaYear = $("#disqEvaYear").val();
		var poolCd = $("#disqPoolCd").val();
		var disq1Univ =  $("input[name='disq1Univ']:checked").val();
		var disq2Univ =  $("input[name='disq2Univ']:checked").val();
		
		//재직경력
		var disq1UnivDataList = [];
	    $('#disq1UnivTable tbody tr').each(function() {	    	
	    	var disq1UnivCd = $(this).find("#disq1UnivCd").val();
	    	
	    	if (disq1UnivCd){
	    		  
	    		var rowData = {
	  	        
	    				disq1UnivCd:  $(this).find("#disq1UnivCd").val()         
	    		};
	  	        
	  	        disq1UnivDataList.push(rowData);
	    	}
	      
	    });
		console.log(disq1UnivDataList);
		
		var disq2UnivDataList = [];
	    $('#disq2UnivTable tbody tr').each(function() {	    	
	    	var disq2UnivCd = $(this).find("#disq2UnivCd").val();
	    	
	    	if (disq2UnivCd){
	    		  
	    		var rowData = {
	  	        
	    				disq2UnivCd:  $(this).find("#disq2UnivCd").val()         
	    		};
	  	        
	    		disq2UnivDataList.push(rowData);
	    	}
	    });
		console.log(disq2UnivDataList);
		
		
		var formData = new FormData();
		formData.append("periodDivCd", periodDivCd);
		formData.append("evaYear", evaYear);
		formData.append("poolCd", poolCd);
		formData.append("disq1Univ", disq1Univ);
		formData.append("disq2Univ", disq2Univ);
		formData.append('disq1UnivDataList', JSON.stringify(disq1UnivDataList));
		formData.append('disq2UnivDataList', JSON.stringify(disq2UnivDataList));
	    
		
		$.ajax({
			url:"<%=request.getContextPath() %>/poolSbmt/poolSubmitData.do",			
			type:"post",
			data: formData, 
			processData: false, 
		    contentType: false, 
		    cache: false,  
			success: function(data){
				alert("제출되었습니다.");
				$("#popupDisqUniv").dialog("close");
				var table = $("#poolSbmListTableBody");
			    table.empty();
				fn_getPoolSbmList();	
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

	function poolSubmitCancel(listData) {
			   
		var canclePeriodDivCd = listData.PERIOD_DIV_CD;
		var cancleEvaYear = listData.EVA_YEAR;
		var canclePoolCd = listData.POOL_CD;
		//var disq1Univ =  $("input[name='disq1Univ']:checked").val();
		//var disq2Univ =  $("input[name='disq2Univ']:checked").val();
		
		console.log(listData);
		var formData = new FormData();
		formData.append("periodDivCd", canclePeriodDivCd);		
		formData.append("evaYear", cancleEvaYear);
		formData.append("poolCd", canclePoolCd);
		//formData.append("disq1Univ", disq1Univ);
		//formData.append("disq2Univ", disq2Univ);
		
		console.log(canclePeriodDivCd); 
		$.ajax({
			url:"<%=request.getContextPath() %>/poolSbmt/poolSubmitCancelData.do",			
			type:"post",
			data: formData, 
			processData: false, 
		    contentType: false, 
		    cache: false,  
			success: function(data){
				//$("#popupDisqUniv").dialog("close");
				alert("제출취소되었습니다.");
				var table = $("#poolSbmListTableBody");
			    table.empty();
				fn_getPoolSbmList();	
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
	
	
	
</script>


