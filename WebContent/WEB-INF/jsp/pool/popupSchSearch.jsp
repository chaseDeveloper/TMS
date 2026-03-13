<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		
		
<body>
	<div class="pop_wrap" id="popupSchSearch" style="display:none;">	
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
									<col style="width:*" />
									<col style="width:*" />
								</colgroup>
								<thead>
									<th scope="col">No.</th>
									<th scope="col">구분</th>
									<th scope="col">대학</th>
									<th scope="col">대학코드</th>
									<th scope="col">지역</th>
								</thead>
								<tbody id="schlListTableBody">
												
								</tbody>
					</table>
					
				</div>
				
				
				
		  </div>
		</div>
	</div>	
		
</body>




<script type="text/javascript">

	$(document).ready(function() {
	    SchlSearch();  // 페이지 로드 완료 시 자동으로 함수 호출
	});


	function openSchSearchPopup() {
		 var btn = $("#btnSchlSearch");  // 버튼의 jQuery 선택자
		 var btnOffset = btn.offset();  // 버튼의 위치 및 크기 정보
		
		$( "#popupSchSearch" ).dialog({				
			title: '대학검색',
			resizable: false,
			height: 520,
			width: 650,    		
			modal: true,
			position: {
				 my: "left-10 center+50",  // 팝업의 왼쪽 중앙을 기준점으로 오른쪽으로 10px, 아래로 20px 이동
				 at: "left center",  // 버튼의 오른쪽 중앙을 기준점으로
				 of: btn  // 이 위치 기준은 버튼
		    }
		});
	}	
	
	
	function SchlSearch() {
		var tbody = $("#schlListTableBody");
		
		tbody.empty();
		
		$.ajax({
			url:"<%=request.getContextPath() %>/pool/selectSchlList.do",
			type:"post",
			data: {searchText : $("#dFilterAcqsSchl").val()},
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
						        row.append($("<td>").addClass("center").css("display", "none").text(schlData.UNIV_TYPE_CD)); // 직접 숨김 처리
						        row.append($("<td>").text(schlData.UNIV_TYPE_NM));
						        row.append($("<td>").text(schlData.UNIV_NM));
						        row.append($("<td>").text(schlData.UNIV_CD));
						        row.append($("<td>").addClass("center").css("display", "none").text(schlData.CTPV_CD)); // 직접 숨김 처리
						        row.append($("<td>").text(schlData.CTPV_NM));
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
			SchlSearch();
		}
	});
	
	//팝업창 조회버튼
	$("#btnPopupSchlSearch").click(SchlSearch);
	
	
	$("#schlListTableBody").on("dblclick", "tr", function() {
	    // 선택된 행에서 데이터 가져오기
	    var selectedRow = $(this);
	    var UnivTypeCd = selectedRow.find("td:eq(1)").text(); // 대학구분코드
	    var UnivTypeNm = selectedRow.find("td:eq(2)").text(); // 대학구분명
	    var UnivNm = selectedRow.find("td:eq(3)").text(); // 대학명
	    var UnivCd = selectedRow.find("td:eq(4)").text(); // 대학코드
	    var CtpvCd = selectedRow.find("td:eq(5)").text(); // 소재지 코드
	   
	    $("#PoolInfoUnivDiv").val(UnivTypeCd);
	    $("#PoolInfoUnivDivNm").val(UnivTypeNm);
	    $("#PoolInfoJobNm").val(UnivNm);
	    $("#PoolInfoUnivCd").val(UnivCd);
	    $("#PoolInfoCtpvCd").val(CtpvCd);
	    
	    // 팝업 닫기
        $("#popupSchSearch").dialog("close");
	    
	    <%-- // 새로운 로우 생성 및 추가
	    var newRow = $("<tr>");
	    newRow.append($("<td>").text($("#poolDisq1UnivTableBody tr").length + 1)); // 새로운 로우 번호
	    newRow.append($("<td>").addClass("left").text(disqUnivNm));
	    newRow.append($("<td>").text(disqUnivCd));
	    newRow.append($('<td><button type="button" class="deleteButton"><img src="<%=request.getContextPath() %>/images/icon_del.png" alt="삭제"/></button></td>'));

	    // poolDisq1UnivTableBody에 새로운 로우 추가
	    $("#poolDisq1UnivTableBody").append(newRow); --%>
	   
	});
</script>



</html>
