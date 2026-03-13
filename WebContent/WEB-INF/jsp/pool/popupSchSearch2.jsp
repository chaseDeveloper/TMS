<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		
		
<body>
	<div class="pop_wrap" id="popupSchSearch2" style="display:none;">	
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
									<div class="w_20">
										<select id="selectOption">
											<option value="국내" >국내</option>
											<option value="국외" >국외</option>
										</select>
									</div>									
									<div class="w_50"><input type="text" title="대학명" id="dFilterAcqsSchl2"></div>
										<button type="button" title="검색" class="btn_inquiry"  id="btnPopupSchlSearch2">검색</button>
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
								<tbody id="schlListTableBody2">
												
								</tbody>
					</table>
					
				</div>
				
				
				
		  </div>
		</div>
	</div>	
		
</body>




<script type="text/javascript">
	
	var buttonType;  
	var selectedRowIndex;
	function openSchSearchPopup2(type,index) {
		
		// 클릭된 버튼을 인자로 받아서 해당 버튼의 위치를 기준으로 팝업 설정
	    var btn;
	    var positionSettings;

	    // 버튼과 위치 설정을 조건에 따라 결정
	    if (type === 'degree') {
	        btn = $("#btnDegreeSchlSearch");
	        positionSettings = {
	        		 my: "left-50 center+50",  
					 at: "left center",  
					 of: btn  // 이 위치 기준은 버튼
	        };
	    } else { // type이 'fulltime'일 경우
	        btn = $("#btnFulltimeSchlSearch");
	        positionSettings = {
	        		 my: "center-300",  // 팝업의 왼쪽 중앙을 기준점으로 오른쪽으로 10px, 아래로 20px 이동
					 at: "left center",  // 버튼의 오른쪽 중앙을 기준점으로
					 of: btn  // 이 위치 기준은 버튼
	        };
	    }
	    
		var btnOffset = btn.offset();  // 버튼의 위치 및 크기 정보
		
		
		buttonType = type; 	
		selectedRowIndex = index; 
		
		console.log(buttonType +"검색")	
		console.log("Index in openSchSearchPopup2: " + index);
		
		var tbody = $("#schlListTableBody2");		
		tbody.empty();
		
		// 팝업을 열 때 초기화
	    $("#selectOption").val("국내"); // 선택 옵션 초기화
	    $("#dFilterAcqsSchl2").val(""); // 대학명 입력란 초기화
		
		$( "#popupSchSearch2" ).dialog({				
			title: '대학검색',
			resizable: false,
			height: 520,
			width: 650,    		
			modal: true,
			//position: positionSettings,
			position: {
		        my: "center",
		        at: "center",
		        of: window,
		        collision: "fit" // 팝업이 화면에 맞춰지도록 자동 조정
		    },
			open: function(event, ui) {
		           SchlSearch2();  // 팝업이 열릴 때 자동으로 조회 함수 실행
		    }
		});
		
		
	}	
	
	
	function SchlSearch2() {
		var tbody = $("#schlListTableBody2");
		
		tbody.empty();
		
		var schlType = $("#selectOption option:selected").val(); // 선택된 옵션의 값(국내 또는 국외)을 가져옵니다
		var searchText = $("#dFilterAcqsSchl2").val(); // 대학명 입력란의 값을 가져옵니다
		
		
		
		$.ajax({
			url:"<%=request.getContextPath() %>/pool/selectSchlList2.do",
			type:"post",
			data: {schlType: schlType, searchText: searchText},
			cache: false,	
			dataType : "json",
			success:function(data){				
					
					var schlList = data
					
					
					if (schlList && schlList.length > 0) {
						 for (var i = 0; i < schlList.length; i++) {
						        var schlData = schlList[i];
						        var row = $("<tr>").addClass('cursor-pointer'); 
						        row.append($("<td>").addClass("center").text(i + 1));						        
						        row.append($("<td>").text(schlData.ORG_CD));
						        row.append($("<td>").text(schlData.CODE_NM));
						        
						        tbody.append(row);
						    }	
					}
					
				
			},
			error:function(key, textStatus, errorThrown){
				alert("에러 발생");
			}
		});
	}
	
	$("#dFilterAcqsSchl2").keyup(function(e){
		if(e.keyCode == 13) {
			SchlSearch2();
		}
	});
	
	//팝업창 조회버튼
	$("#btnPopupSchlSearch2").click(SchlSearch2);
	
	
	$("#schlListTableBody2").on("dblclick", "tr", function() {
	    // 선택된 행에서 데이터 가져오기
	    var selectedRow = $(this);
	    var UnivCd = selectedRow.find("td:eq(1)").text(); // 대학구분코드
	    var UnivNm = selectedRow.find("td:eq(2)").text(); // 대학구분명	    
	    
	    console.log(buttonType);
	    
	    if(buttonType === "degree" ) {
	    		    	
	    	// 여기서 index 값을 사용하여 해당 로우에 대학코드값을 설정
	        var tableRows = $("#poolDegreeTableBody tr");
	        var currentRow = tableRows.eq(selectedRowIndex);
	    	
	        currentRow.find("#degUnivCd").val(UnivCd);
	        currentRow.find("#degUnivNm").val(UnivNm);
	    	
	    	
	    } else {	    	 
	    		    		  	    
	    	// 여기서 index 값을 사용하여 해당 로우에 대학코드값을 설정
	        var tableRows = $("#poolFulltimeTableBody tr");
	        var currentRow = tableRows.eq(selectedRowIndex);
	    	
	        currentRow.find("#fttUnivCd").val(UnivCd);
	        currentRow.find("#fttUnivNm").val(UnivNm);	       
		   
	    }
	   
	    
	    // 팝업 닫기
        $("#popupSchSearch2").dialog("close");
	    
	    
	});
</script>



</html>
