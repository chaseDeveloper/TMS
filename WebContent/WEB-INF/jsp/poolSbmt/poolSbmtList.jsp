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
				<h2 class="title">신청서 제출</h2>

				<div class="sub_bg">	
					<div class="sub_box">	
						
						<!-- 학력 -->
						<div class="form_box">

							<table class="list">
								<caption>참여신청,No.,주기,진단년도,공모분야,성명,활동 시작일,활동 종료일,제출여부,제출일자 정보를 확인할 수 있습니다.</caption>
								<colgroup class="dp_n_t dp_n_m">
									<col style="width:2%;" />
									<col style="width:3%;" />
									<col style="width:6%;" />
									<col style="width:*" />
									<col style="width:*" />									
									<col style="width:11%;" />
									<col style="width:11%;" />
									<col style="width:7%;" />
									<col style="width:7%;" />
									<col style="width:9%;" />
								</colgroup>
								<thead class="dp_n_t dp_n_m">
									<th scope="col">No.</th>
									<th scope="col">주기</th>
									<th scope="col">진단년도</th>
									<th scope="col">공모명</th>
									<th scope="col">진단 일정</th>									
									<th scope="col">접수 시작일</th>
									<th scope="col">접수 종료일</th>
									<th scope="col">제출여부</th>									
									<th scope="col">신청서</th>
									<th scope="col">제출일자</th>
								</thead>
								<tbody id="poolSbmListTableBody">
												
								</tbody>
							</table>
							
						</div>
						<!-- //학력 -->
					
					
					    <!-- btn -->
					 	<!-- <div class="btn_box_right">							
							<button type="button" title="제출취소" class="btn_gray" onclick="poolSubmitCancel()">제출취소</button>
							<button type="button" title="제출" class="btn_default" onclick="poolSubmit()">제출</button>
						</div> -->
						<!-- //btn -->
						
						
						
						
						
						
					</div>
				</div>

				</div>
			</div>

		</div>
		<!-- //container -->


		<jsp:include page="../common/footer.jsp" flush="false"></jsp:include>

	</div>

<%@ include file="popupDisqUniv.jsp" %>
<%@ include file="popupDisqSchSearch.jsp" %>
<script type="text/javascript">
	$(document).ready(function() {
		fn_getPoolSbmList();		
		
	
	});
	
	function fn_getPoolSbmList() {
		
		$.ajax({
			url:"<%=request.getContextPath() %>/poolSbmt/getpoolSbmList.do",
			type:"post",
			cache: false,
			data:{},			
			dataType : "json",
			success:function(data){
				if(data.result == "S") {					
					console.log("신청서 제출정보");					

					//기피제척대상2 대학 
					var list = data.list
					var tbody = $("#poolSbmListTableBody");
					
					if (list && list.length > 0) {
						list.forEach(function(listData, i) {
				            var stDt = listData.ST_DT;
				            var syear = stDt.substr(0, 4);
				            var smonth = stDt.substr(4, 2);
				            var sday = stDt.substr(6, 2);
				            var shours = stDt.substr(8, 2);
				            var sminutes = stDt.substr(10, 2);
				            var sformattedDate = syear + '-' + smonth + '-' + sday + ', ' + shours + ':' + sminutes;

				            var edDt = listData.ED_DT;
				            var eyear = edDt.substr(0, 4);
				            var emonth = edDt.substr(4, 2);
				            var eday = edDt.substr(6, 2);
				            var ehours = edDt.substr(8, 2);
				            var eminutes = edDt.substr(10, 2);
				            var eformattedDate = eyear + '-' + emonth + '-' + eday + ', ' + ehours + ':' + eminutes;

				            var applYn = listData.APPL_YN;
				            var statusCell = getStatusCell(applYn); // 상태 셀을 동적으로 생성

				            var applYnDtCell = $("<td>");
				            if (applYn === 'Y') {
				                applYnDtCell.append($("<span>").addClass("dp_n_p ma_r04").attr("id", "applYnDt"), listData.APPL_YN_DT);
				                var disqUnivCell = $("<td>").append(
				                        $("<button>")
				                            .text("제출취소")
				                            .addClass("btn_gray")
				                            .attr("title", "제출취소")
				                            .attr("type", "button")
				                             .click(function() {
									            poolSubmitCancel(listData);  // 버튼 클릭 시 poolSubmitCancel 함수에 listData를 인자로 전달
									        })
				                    );
				            } else {
				                applYnDtCell.append($("<span>").addClass("dp_n_p ma_r04").attr("id", "applYnDt"));
				                
				                // '기피제척대학 등록' 버튼 생성
					            var disqUnivCell = $("<td>").append(
					                $("<button>")
					                    .text("제출하기")
					                    .addClass("btn_search")
					                    .attr("title", "제출하기")
					                    .attr("type", "button")
					                    .click(function() {				                      
					                            openDisqUnivPopup(listData);
					                        
					                    })
					            );

				            }

				            
				            var row = $("<tr>");				           
				            row.append($("<td>").addClass("dp_n_t dp_n_m").text(i + 1));
				            row.append($("<td>").data("periodDivCd", listData.PERIOD_DIV_CD).text(listData.PERIOD_DIV_CD + "주기"));
				            row.append($("<td>").data("evaYear", listData.EVA_YEAR).text(listData.EVA_YEAR + "년도"));
				            row.append($("<td>").data("poolCd", listData.POOL_CD).css("display", "none").text(listData.POOL_CD));
				            row.append($("<td>").data("poolCodeNm", listData.CODE_NM).text(listData.CODE_NM));
				            var poolCdNmFormatted = listData.POOL_CD_NM.replace(/,/g, ',<br/>');
				            row.append(
				                $("<td>")
				                    .data("poolCdNm", listData.POOL_CD_NM)
				                    .html(poolCdNmFormatted)
				            )
				           /*  row.append($("<td>").data("poolCdNm", listData.POOL_CD_NM).text(listData.POOL_CD_NM)); */
				            row.append($("<td>").append($("<span>").addClass("dp_n_p ma_r04").attr("id", "stDt"), sformattedDate));
				            row.append($("<td>").append($("<span>").addClass("dp_n_p ma_r04").attr("id", "edDt"), eformattedDate));
				            row.append(statusCell);
				            row.append(disqUnivCell);  
				            row.append(applYnDtCell);				              
				            tbody.append(row);
				        });

						function getStatusCell(applYn) {
						    var statusText = applYn === 'Y' ? '제출완료' : '미제출';
						    var statusClass = applYn === 'Y' ? 'green' : 'gray';

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
	
	
	
	<%-- function poolSubmit() {
	
		 
			 var tdArr = [];
			 var checkbox = $("input[name=tblChk]:checked");
			 
			 if (checkbox.length === 0) {
				    alert("체크항목이 없습니다.");
			 }

			 checkbox.each(function (i) {
			     var tr = checkbox.parent().parent().eq(i);
			     var td = tr.children();

			     var periodDivCd = td.eq(2).text().replace('주기', '');
			     var evaYear = td.eq(3).text().replace('년도', '');
			     var poolCd = td.eq(4).text();
			     var poolCdNm = td.eq(5).text();

			     tdArr.push({
			         periodDivCd: periodDivCd,
			         evaYear: evaYear,
			         poolCd: poolCd,
			         poolCdNm: poolCdNm
			     });
			 });

		    console.log("tdArr: ", tdArr);
		    
		$.ajax({
			url:"<%=request.getContextPath() %>/pool/poolSubmitData.do",			
			type:"post",
			data: JSON.stringify(tdArr), 
			contentType: "application/json",
			cache: false,	
			dataType :"json",
			success: function(data){
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
	
	function poolSubmitCancel() {
			
		 var tdArr = [];
		 var checkbox = $("input[name=tblChk]:checked");
			
		 if (checkbox.length === 0) {
			    alert("체크항목이 없습니다.");
		 }
		 
		 checkbox.each(function (i) {
		     var tr = checkbox.parent().parent().eq(i);
		     var td = tr.children();

		     var periodDivCd = td.eq(2).text().replace('주기', '');
		     var evaYear = td.eq(3).text().replace('년도', '');
		     var poolCd = td.eq(4).text();
		     var poolCdNm = td.eq(5).text();

		     tdArr.push({
		         periodDivCd: periodDivCd,
		         evaYear: evaYear,
		         poolCd: poolCd,
		         poolCdNm: poolCdNm
		     });
		 });

	    console.log("tdArr: ", tdArr);    
	    
		$.ajax({
			url:"<%=request.getContextPath() %>/pool/poolSubmitCancelData.do",			
			type:"post",
			data: JSON.stringify(tdArr), 
			contentType: "application/json",
			cache: false,	
			dataType :"json",
			success: function(data){
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
	} --%>

</script>
</body>
</html>