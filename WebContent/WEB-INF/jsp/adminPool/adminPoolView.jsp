<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

			<div id="content">
				<div class="con_box">
				<h2 class="title">진단위원후보자 정보</h2>

				<div class="sub_bg">	
					<div class="sub_box">					
						<div class="form_box">
							 <div class="form_btn_area" style="width:100%;">
							 	<button id="btnSendMail" class="ui-button ui-corner-all ui-widget">
									<i class="fa-solid fa-envelope"></i> 회원가입 요청
								</button>								
								<button id="btnSearch" class="ui-button ui-corner-all ui-widget">
									<i class="fa-solid fa-magnifying-glass"></i> 조회
								</button>								
								<button id="btnExcelDownload" class="ui-button ui-corner-all ui-widget">
									<i class="fa-sharp fa-solid fa-download"></i> 엑셀
								</button>
					
							</div>
							  <div class="grid_container">
                       			 <div id="grid1" style="background:white;width:100%;height:100%;"></div>
                    		</div>							
						</div>						
					</div>
				</div>

				</div>
			</div>

		</div>
		<!-- //container -->

		<jsp:include page="../common/footer.jsp" flush="false"></jsp:include>

	</div>
	
<%@ include file="adminPoolInfo.jsp" %>

<script type="text/javascript">
$(function () {
	
	/* function tooltipRender(ui) {
	    return {
	        //here we inject html title attribute in each cell.
	        attr: {
	            title: ((ui.formatVal || "") + "").replace(/"/g, '&quot;')
	        }
	    }
	} */
	
	searchList();
	
	function formatPrivacyDttm(cellData) {
		  if (!cellData) return "-";
		  
		  // "YYYY-MM-DD HH24:MI" 형식에서 앞 10자리는 날짜입니다.
		  const dateStr = cellData.substring(0, 10);
		  const [year, month, day] = dateStr.split("-").map(Number);
		  const cellDate = new Date(year, month - 1, day);
		  if (isNaN(cellDate.getTime())) return cellData;
		  
		  const twoYearsAgo = new Date();
		  twoYearsAgo.setFullYear(twoYearsAgo.getFullYear() - 2);
		  
		  return cellDate < twoYearsAgo
		    ?  '<span style="color: red;">' + cellData + '</span>'
		    : cellData;
		}

	var options1 = JSON.parse(JSON.stringify(common_options));
	options1.width = 'flex'; // 그리드가 가변 너비를 가지도록 설정
	//options1.width = 'auto'; // 그리드의 가로 너비를 가변적으로 설정
	options1.minWidth = 800; // 그리드의 최소 너비를 800px로 설정
	options1.height = 600; // 그리드의 높이를 600px로 설정
	options1.resizable = {
		columns: true
	};
	 options1.colModel = [
		{ dataIndx: "stuState", width: 50, align:"center", valign: "center", hvalign: "center",
            title: "",
            menuIcon: false,
            type: 'checkBoxSelection', sortable: false, editable: true,
            editable: function (ui) {
                return ui.rowData.DI_STATUS === "N";
            },
            dataType: 'bool',
            cb: {
                all: true, //checkbox selection in the header affect current page only.
                header: true, //show checkbox in header.
            }
		
        },		
		{ title: "본인인증여부",dataIndx: "DI_STATUS", width: 20, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "이메일",dataIndx: "USR_ID", width: 180, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "성명",dataIndx: "NAME", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "공모분야",dataIndx: "JOB_TITLE", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "소속대학구분",dataIndx: "UNIV_DIV_NM", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "성별",dataIndx: "GENDER", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "대학",dataIndx: "UNIV_NM", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "소속학과",dataIndx: "DEPT", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "직급",dataIndx: "POSITION", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "휴대폰번호",dataIndx: "TEL_HP", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "자택번호",dataIndx: "TEL_HOME", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "생년월일",dataIndx: "BIRTHDAY", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "자택우편번호",dataIndx: "ADDR_HOME_ZIPCODE", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "자택주소",dataIndx: "ADDR_HOME", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "자택상세주소",dataIndx: "ADDR_HOME_DETAIL", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "학력(학사)",dataIndx: "A_UNIV", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "학사세부전공",dataIndx: "A_MAJOR", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "학력(석사)",dataIndx: "B_UNIV", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "석사 세부전공",dataIndx: "B_MAJOR", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "학력(박사)",dataIndx: "C_UNIV", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "박사 세부전공",dataIndx: "C_MAJOR", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "자기소개1",dataIndx: "RESUME_1", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "자기소개2",dataIndx: "RESUME_2", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "재직증명서", dataIndx: "FILE_NM", width: 170, dataType: "string" , align:"center", halign:"center", hvalign: "center", valign:"center", render: anchorRenderer, menuIcon:false},
		{ title: "개인정보동의일자",dataIndx: "PRIV_DTTM", width: 150, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center",render: ui => formatPrivacyDttm(ui.cellData), menuIcon:true, filter: { crules: [{ condition: 'range' }]}}
	];
	
	options1.menuIcon =  true;
	options1.rowDblClick =  function (evt, ui) {           
        openUpdWindow(ui.rowData);
               
    }
	options1.create = function(evt, ui) {
		//this.widget().pqTooltip();
	}
	//options1.columnTemplate = { render: tooltipRender };
	
    var grid = pq.grid("#grid1", options1);
    grid.refreshDataAndView();   
   
  /*   grid.scrollColumn({ dataIndx: "PRIV_DTTM" }, function() {
        console.log("Scroll to PRIV_DTTM complete");
    }); */

  	//회원가입 요청
    $("#btnSendMail").click(function() {
    	sendMail();
    });    
    
    $("#btnSearch").click(function() {
    	searchList();
     });
    
    //excel 다운로드
    $("#btnExcelDownload").click(function() {
    	excelDownload();
    });    
    
});

function anchorRenderer(ui) {
	if(ui.rowData["FILE_ID"] != null) {
		
		return '<a href="#"  title="' + ui.rowData["FILE_NM"] + '" onclick=\'pdfPreView( "<%=request.getContextPath() %>/adminPool/pdfPreView.do?fileId=' + ui.rowData["FILE_ID"] + '", "' +  ui.rowData["FILE_NM"] + '");\'">' + ui.rowData["FILE_NM"] + '</a>'; 
	}
	
	return ''; 
}

function pdfPreView(url, fileNm) {
	$( "#pdfView" ).dialog({
		title: fileNm,
		resizable: false,
		height: 750,
		width: 1000,    		
		modal: true,
		buttons: {
	        "닫기": function() {
	            $(this).dialog("close");
	        }
	    }
	
	});
	
	$( "#pdfViewFrm" ).attr("src", url);
}
	
function sendMail(){
	$("#loadingImage").show();
	
	var checkedData = [];
	$("#grid1").pqGrid("option", "dataModel").data.forEach(function(rowData) {
	    if (rowData.stuState) { // 'stuState'가 체크박스 상태를 나타내는 필드
	        checkedData.push(rowData);
	    }
	});
	
	$.ajax({
		url:"<%=request.getContextPath() %>/adminPool/sendJoinMail.do",
		type:"post",
		data: {checkedData : JSON.stringify(checkedData)  }, 
		cache: false,	
		dataType : "json",
		success:function(data){
		    if(data.result === "S") {
				alert("메일이 전송 되었습니다");
		      } else {
		        alert("실패: " + data.message);
		      }
		},
		error: errorHandler,
		complete: function(){		
			$("#loadingImage").hide();
		}
	});
}
	
function searchList() {
	$("#loadingImage").show();
	
	
	$.ajax({
		url:"<%=request.getContextPath() %>/adminPool/getAdminPoolList.do",
		type:"post",
		data: {},
		cache: false,	
		dataType : "json",
		success:function(data){
			var grid = pq.grid("#grid1");
			grid.option("dataModel", { data: data.list });
			grid.refreshDataAndView();			
		},
		error: errorHandler,
		complete: function(){		
			console.log("complete");
			$("#loadingImage").hide();
		}
	});
}

function excelDownload() {
	var curTimeMills = new Date().getTime();
	var grid = pq.grid( "#grid1");			
	var realfilename = btoa(encodeURIComponent("공모 후보자 정보" + curTimeMills));
	//console.log(realfilename);
	grid.exportExcel( { url: "<%=request.getContextPath() %>/adminPool/exportData.do", filename: realfilename} );
}

$(function() {
    var gridWidth = $('#grid1').outerWidth();
    var containerWidth = $('.grid_container').width();
    if (gridWidth > containerWidth) {
        $('.grid_container').css('overflow-x', 'auto');
    }
    $('.grid_container').css('overflow-y', 'hidden');
});


</script>
</body>
</html>