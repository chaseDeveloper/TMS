<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<meta http-equiv="content-type" content="text/html;charset=UTF-8"/>

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
				<h2 class="title">선정결과 확인</h2>

				<div class="sub_bg">	
					<div class="sub_box">							
						
						<div class="form_box">

							<table class="list">
								<caption>No.,주기,진단년도,공모분야,성명,활동 시작일,활동 종료일,선정결과,보안서약서 동의여부,동의일자 정보를 확인할 수 있습니다.</caption>
								<colgroup class="dp_n_t dp_n_m">
									<col style="width:3%;" />
									<col style="width:6%;" />
									<col style="width:8%;" />
									<col style="width:18%" />
									<col style="width:*" />
									<col style="width:12%;" />
									<col style="width:7%;" />
									<col style="width:7%;" />
									<col style="width:12%;" />
								</colgroup>
								<thead class="dp_n_t dp_n_m">
									<th scope="col">No.</th>
									<th scope="col">주기</th>
									<th scope="col">진단년도</th>
									<th scope="col">공모명</th>
									<th scope="col">진단 일정</th>	
									<th scope="col">성명</th>																	
									<th scope="col">선정결과</th>
									<th scope="col">보안서약서 동의여부</th>
									<th scope="col">동의일자</th>
								</thead>
								<tbody id="poolResultListTableBody">
													
								</tbody>
							</table>
							
						</div>

						
					</div>
				</div>

				</div>
			</div>

		</div>
		<!-- //container -->


		<jsp:include page="../common/footer.jsp" flush="false"></jsp:include>

	</div>


<script type="text/javascript">
	$(document).ready(function() {
		var table = $("#poolResultListTableBody");
		table.empty();
		fn_getPoolResultList();	
		
		 
	});
	
	function fn_getPoolResultList() {
		
		$.ajax({
			url:"<%=request.getContextPath() %>/poolResult/getPoolResultList.do",
			type:"post",
			cache: false,
			data:{},			
			dataType : "json",
			success:function(data){
				if(data.result == "S") {					
					console.log("신청결과 정보");
					
					$("#poolResultListTableBody").empty();
					
					var list = data.list
					var tbody = $("#poolResultListTableBody");
					
				
					
					if (list && list.length > 0) {
					 
						for (var i = 0; i < list.length; i++) {
						    var listData = list[i];
						    var stDt = listData.ST_DT;
						    var syear = stDt.substr(0, 4);
						    var smonth = stDt.substr(4, 2);
						    var sday = stDt.substr(6, 2);
						    var shours = stDt.substr(8, 2);
						    var sminutes = stDt.substr(10, 2);
						    var sformattedDate = syear + '-' + smonth + '-' + sday + ' ' + shours + ':' + sminutes;

						    var edDt = listData.ED_DT;
						    var eyear = edDt.substr(0, 4);
						    var emonth = edDt.substr(4, 2);
						    var eday = edDt.substr(6, 2);
						    var ehours = edDt.substr(8, 2);
						    var eminutes = edDt.substr(10, 2);
						    var eformattedDate = eyear + '-' + emonth + '-' + eday + ' ' + ehours + ':' + eminutes;

						    var selRslt = listData.SEL_RSLT;
						    var statusCell = getStatusCell(selRslt);
						    
						    var secPledgeYn = listData.SEC_PLEDGE_YN;

						    var secPledgeYnDtCell;					    
						    console.log("listData:", list[i]);
						    console.log("listData:", listData);
						    
						    if (selRslt === 'Y') { 
						    	(function(currentListData) {						    
							    	
							        secPledgeYnCell = $("<td>").append(	
							        		  $("<button>")						                    
							                  .text("동의하기")
							                  .addClass("btn_inquiry")
							                  .attr("title", "보안서약서 동의")
							                  .click(function() {
							                      // 팝업 열기
							                      var popupWindow = window.open('', 'popup', 'width=900,height=1400');
	
							                      // 팝업 내부에 양식 추가
							                      $(popupWindow.document.body).html(
							                          $("<form>")
							                              .attr("method", "post")
							                              .attr("action", "<%=request.getContextPath() %>/poolResult/poolPrintMain.do")
							                              .append(
							                                  $("<input>")
							                                      .attr("type", "hidden")
							                                      .attr("name", "listData")
							                                      .val(JSON.stringify({ listData: currentListData }))
							                              )						                          
							                      );
	
							                      // 양식 제출
							                      popupWindow.document.forms[0].submit();
							                     
							                      // 팝업이 닫힐 때 목록을 새로고침하기 위한 콜백 함수
							                      var intervalId = setInterval(function() {
							                          if (popupWindow.closed) {
							                              clearInterval(intervalId);
							                              // 팝업이 닫힌 후 목록을 새로고침합니다.
							                              fn_getPoolResultList();
							                          }
							                      }, 500); // 필요에 따라 간격을 조절하세요.
							                  })
							            );
						    	})(listData); 
						    } else {
						    	secPledgeYnCell = $("<td>").append($("<span>").addClass("dp_n_p ma_r04"));
						    }
						    
						    if (selRslt === 'Y' && secPledgeYn === 'Y') {
						      
						    	secPledgeYnDtCell = $("<td>").append($("<span>").addClass("dp_n_p ma_r04"),listData.SEC_PLEDGE_YN_DT);
						    } else {
						    	secPledgeYnDtCell = $("<td>").append($("<span>").addClass("dp_n_p ma_r04"));
						    }
						    
						    
						    var row = $("<tr>");						    
						    row.append($("<td>").addClass("dp_n_t dp_n_m").text(i + 1));
						    row.append($("<td>").data("periodDivCd", listData.PERIOD_DIV_CD).text(listData.PERIOD_DIV_CD + "주기"));
						    row.append($("<td>").data("evaYear", listData.EVA_YEAR).text(listData.EVA_YEAR + "년도"));
						    row.append($("<td>").data("poolCd", listData.POOL_CD).css("display", "none").text(listData.POOL_CD));
						    row.append($("<td>").data("poolCodeNm", listData.CODE_NM).text(listData.CODE_NM));
				            var poolCdNmFormatted = listData.POOL_CD_NM.replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/,/g, ',<br/>');
				            row.append(
				                $("<td>")
				                    .data("poolCdNm", listData.POOL_CD_NM)
				                    .html(poolCdNmFormatted)
				            )
						    /* row.append($("<td>").data("poolCdNm", listData.POOL_CD_NM).text(listData.POOL_CD_NM)); */
						    row.append($("<td>").append($("<span>").addClass("dp_n_p ma_r04").attr("id", "name"),listData.NAME));						   
						    row.append(statusCell);
						    row.append(secPledgeYnCell);	
						    row.append(secPledgeYnDtCell);							
						    tbody.append(row);

						   
						}

						function getStatusCell(selRslt) {
							var statusText;
							var statusClass;

							if (selRslt === 'Y') {
							    statusText = '선정';
							    statusClass = 'blue';
							} else if (selRslt === 'N') {
							    statusText = '미선정';
							    statusClass = 'gray';
							} else if (selRslt === 'D') {
							    statusText = '진행중';
							    statusClass = 'gray';  // Assuming '진행중' should be represented by yellow
							}

						    return $("<td>").append($("<span>").addClass("list_status_a status_" + statusClass).text(statusText));
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
	


</script>
</body>
</html>