<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		
		
<body>
	<div class="pop_wrap" id="popupDisqSchSearch" style="display:none;">	
		<div class="pop_container">
			<div class="pop_content">	
				
				<div class="pop_box">	
					<table class="list_01" summary="대학 검색">
						<caption>대학 검색 정보를 확인할 수 있습니다.</caption>
						<colgroup>
							<col style="width:14%;" />
							<col style="width:*" />
							</colgroup>
						<tbody>
							<tr>
								<th>대학 검색</th>
								<td>									
									<div class="w_50"><input type="text" title="대학명" id="dFilterAcqsSchl"></div>
										<button type="button" title="검색" class="btn_inquiry"  id="btnPopupSchlSearch">검색</button>
								</td>
							</tr>										
						</tbody>
					</table>							
				</div>
				
				<div class="pop_box">
					<h3>대학 목록</h3>
					<table class="list_01" summary="대학 목록">
								<caption>No.,구분,대학,설립구분,지역을 확인할 수 있습니다.</caption>
								<colgroup>
									<col style="width:3%;" />									
									<col style="width:*" />
									<col style="width:*" />
								</colgroup>
								<thead>
									<th scope="col">No.</th>									
									<th scope="col">대학</th>
									<th scope="col">대학코드</th>								
								</thead>
								<tbody id="disqSchlListTableBody">
												
								</tbody>
					</table>
					
				</div>
				
				
				
		  </div>
		</div>
	</div>	
		
</body>




<script type="text/javascript">
	
	var buttonType;
	var currentEvaYear = null; // 전역 변수 선언
	
	function openDisqSchSearchPopup(type,evaYear) {
		
		buttonType = type; 	
		currentEvaYear = evaYear;
		
		console.log(buttonType +"검색");
		console.log(evaYear + " evaYear");	
		
		var tbody = $("#disqSchlListTableBody");		
		tbody.empty();
		
		// 팝업을 열 때 초기화
	    $("#dFilterAcqsSchl").val(""); // 초기화	   
		
		$( "#popupDisqSchSearch" ).dialog({				
			title: '기피제척 대학검색',
			resizable: false,
			height: 520,
			width: 650,    		
			modal: true,
			open: function(event, ui) {
				SchlSearch(currentEvaYear);  // 팝업이 열릴 때 자동으로 조회 함수 실행
		    }
		});
		
		
	}	
	
	
	function SchlSearch(evaYear) {
		var tbody = $("#disqSchlListTableBody");
		
		tbody.empty();
		
		$.ajax({
			url:"<%=request.getContextPath() %>/poolSbmt/selectSchlList.do",
			type:"post",
			data: {searchText : $("#dFilterAcqsSchl").val(),evaYear:evaYear},
			cache: false,	
			dataType : "json",
			success:function(data){				
					console.log(data);
					var schlList = data
					
					
					if (schlList && schlList.length > 0) {
						 for (var i = 0; i < schlList.length; i++) {
						        var schlData = schlList[i];
						        var row = $("<tr>").addClass('cursor-pointer'); 
						        row.append($("<td>").addClass("center").text(i + 1));						       
						        row.append($("<td>").text(schlData.UNIV_NM));
						        row.append($("<td>").text(schlData.UNIV_CD));						       
						        tbody.append(row);
						    }	
					}
					console.log(schlList);
				
			},
			error:function(key, textStatus, errorThrown){
				alert("에러 발생");
			}
		});
	}
	
	$("#dFilterAcqsSchl").keyup(function(e){
		if(e.keyCode == 13) {
			SchlSearch(currentEvaYear);
		}
	});
	
	//팝업창 조회버튼
	$("#btnPopupSchlSearch").click(function() {
		
    	SchlSearch(currentEvaYear);
    	
	});
	
	
	$("#disqSchlListTableBody").on("dblclick", "tr", function() {
	    // 선택된 행에서 데이터 가져오기
	    var selectedRow = $(this);	   
	    var UnivNm = selectedRow.find("td:eq(1)").text(); // 대학명
	    var UnivCd = selectedRow.find("td:eq(2)").text(); // 대학코드	  
	    console.log(UnivNm);
	    if(buttonType === "disq1" ) {
	   	    var newRow = $("<tr>");
	 	    newRow.append($("<td>").text($("#poolDisq1UnivTableBody tr").length + 1)); // 새로운 로우 번호
	 	    //newRow.append($("<td>").addClass("left").attr("id", "disq1UnivNm").val(UnivNm).prop("readonly", true));
	 	    newRow.append($("<td>").addClass("left").append($("<input>").attr("type", "text").attr("title", "대학").attr("id", "disq1UnivNm").val(UnivNm).prop("readonly", true)));
	 	    newRow.append($("<td>").css("display", "none").attr("id", "disq1UnivCd").val(UnivCd));
	 	    newRow.append($('<td><button type="button" class="deleteButton"><img src="<%=request.getContextPath() %>/images/icon_del.png" alt="삭제"/></button></td>'));

	 	    // poolDisq1UnivTableBody에 새로운 로우 추가
	 	    $("#poolDisq1UnivTableBody").append(newRow);
	    	
	    } else {
	    	
	    	var newRow = $("<tr>");
	    	newRow.append($("<td>").text($("#poolDisq2UnivTableBody tr").length + 1)); // 새로운 로우 번호
	 	    //newRow.append($("<td>").addClass("left").attr("id", "disq1UnivNm").val(UnivNm).prop("readonly", true));
	 	    newRow.append($("<td>").addClass("left").append($("<input>").attr("type", "text").attr("title", "대학").attr("id", "disq2UnivNm").val(UnivNm).prop("readonly", true)));
	 	    newRow.append($("<td>").css("display", "none").attr("id", "disq2UnivCd").val(UnivCd));
	 	    newRow.append($('<td><button type="button" class="deleteButton"><img src="<%=request.getContextPath() %>/images/icon_del.png" alt="삭제"/></button></td>'));

	 	    // poolDisq2UnivTableBody에 새로운 로우 추가
	 	    $("#poolDisq2UnivTableBody").append(newRow);
		   
	    }     
	    
	    // 팝업 닫기
        $("#popupDisqSchSearch").dialog("close");
	    
	  
	});
</script>



</html>
