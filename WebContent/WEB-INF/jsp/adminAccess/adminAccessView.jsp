<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="ko">
<head>
<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>
<meta http-equiv="X-UA-Compatible" id="X-UA-Compatible"
	content="IE=Edge" />
</head>

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
			<div class="con_box">
				<h2 class="title">개인정보 접근 기록</h2>
				<div class="sub_bg">
					<div class="sub_box">
						<div class="form_box">
							<div class="form_warp" style="margin-top: 20px;">
								<ul>
									<li style="display: inline-block;'"><span
										class="form_title">접근일시</span> <input type="date"
										id="sStartDate" /> ~ <input type="date" id="sEndDate" /></li>
										
									<li style="display: inline-block;'"><span
										class="form_title">성명/이메일</span> <input type="text"
										id="sNameEmail" /></li>
										
									<li style="display: inline-block;'"><span
										class="form_title">작업 내용</span> <input type="text"
										id="sRmrk" /></li>
										
									<div style="display: inline-block;'">
										<li><span class="form_title">심야시간 조회</span> <input
											type="checkbox" id="sLntTime" /></li>
											
										<li><span class="form_title">다건 조회</span> <input
											type="checkbox" id="sMultiSearch" /></li>
									</div>
									<!-- <li>
										<button id="btnSearch"
											class="ui-button ui-corner-all ui-widget">
											<i class="fa-solid fa-magnifying-glass"></i> 검색
										</button>
									</li> -->
								</ul>
							</div>

							<div class="form_btn_area" style="width: 100%;">
								<button id="btnSearch" class="ui-button ui-corner-all ui-widget"
									style="margin-right: 15px;">
									<i class="fa-solid fa-magnifying-glass"></i> 검색
								</button>
							</div>
							<div class="grid_container">
								<div id="grid1"
									style="background: white; width: 100%; height: 100%;"></div>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>

	</div>

	<jsp:include page="../common/footer.jsp" flush="false"></jsp:include>

	</div>

	<script type="text/javascript">
$(function () {
	searchList();
	
	var options1 = JSON.parse(JSON.stringify(common_options));
	options1.width = '100%'; // 그리드의 가로 너비를 가변적으로 설정
	options1.minWidth = 800; // 그리드의 최소 너비를 800px로 설정
	options1.height = 600; // 그리드의 높이를 600px로 설정
	options1.resizable = {
		columns: true
	};
	
	 options1.colModel = [
		{ title: "연번",dataIndx: "HIS_NO", width: 100, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "개인정보성명",dataIndx: "SUB_NAME", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "개인정보(이메일ID)",dataIndx: "EMAIL", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "작업내용",dataIndx: "RMRK", width: 250, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "접근자성명",dataIndx: "VIEWER_NAME", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "대학",dataIndx: "UNIV_NM", width: 80, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "접근일시",dataIndx: "INPT_DT", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
	];
	
	options1.menuIcon =  true;
	options1.rowDblClick =  function (evt, ui) {           
        openUpdWindow(ui.rowData);
               
    }
	
    var grid = pq.grid("#grid1", options1);
    grid.refreshDataAndView();   
   
    $("#btnSearch").click(function() {
    	searchList();
     });
    
});
	
function searchList() {
	$("#loadingImage").show();
	
    var startDate = $("#sStartDate").val();
    var endDate = $("#sEndDate").val();
    var nameEmail = $("#sNameEmail").val();
    var rmrk = $("#sRmrk").val();
    var lntTime = $("#sLntTime").prop("checked");
    var multiSearch = $("#sMultiSearch").prop("checked");
	
	$.ajax({
		url:"<%=request.getContextPath()%>/adminAccess/getAdminAccessList.do",
		type:"post",
        data: {
            startDate: startDate,
            endDate: endDate,
            nameEmail: nameEmail,
            rmrk: rmrk,
            lntTime: lntTime,
            multiSearch: multiSearch
        },
		cache: false,	
		dataType : "json",
		success:function(data){
			console.log(data);
			if(data.result=="S"){
				var grid = pq.grid("#grid1");
				grid.option("dataModel", { data: data.list });
				grid.refreshDataAndView();			
			}else{
				alert(data.message);
			}
			
		},
		error: errorHandler,
		complete: function(){		
			console.log("complete");
			$("#loadingImage").hide();
		}
	});
}
	</script>
	
	<%@ include file="/WEB-INF/jsp/common/bodyEnd.jsp" %>
</body>
</html>