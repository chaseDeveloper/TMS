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
				<h2 class="title">공모일정 정보</h2>

				<div class="sub_bg">	
					<div class="sub_box">					
						<div class="form_box">
							 <div class="form_btn_area" style="width:100%;">
								<button id="btnSearch" class="ui-button ui-corner-all ui-widget">
									<i class="fa-solid fa-magnifying-glass"></i> 조회
								</button>
								<button id="btnInsertPop" class="ui-button ui-corner-all ui-widget">
									<i class="fa-solid fa-file-pen"></i>공모일정 등록
								</button>	
								<button id="btnDel" class="ui-button ui-corner-all ui-widget">
									<i class="fa-solid fa-trash"></i>공모일정 삭제
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

<%@ include file="adminScheduleAdd.jsp" %>
<%@ include file="adminSchedulePoolView.jsp" %>

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
	
	function popupRenderer(ui) {
		var rowData = ui.rowData;      	
		
		    return '<button class="ui-button ui-corner-all ui-widget open-applicant-info" style="padding:.1rem .5rem;" data-period-div-cd="' + rowData.PERIOD_DIV_CD + '" data-eva-year="' + rowData.EVA_YEAR + '" data-pool-cd="' + rowData.POOL_CD + '">지원자 정보</button>';

    }
	

	
	
	var options1 = JSON.parse(JSON.stringify(common_options));
	options1.height = 600; 
	options1.colModel = [
		{ dataIndx: "stuState", width: 50, align: "center", resizable: false, align:"center", valign: "center",hvalign: "center",
            title: "",
            menuIcon: false,
            type: 'checkBoxSelection', sortable: false, editable: true,
            dataType: 'bool',
            cb: {
                all: true, //checkbox selection in the header affect current page only.
                header: true, //show checkbox in header.
            }
		
        },		
		{ title: "주기",dataIndx: "PERIOD_DIV_CD", width: 20, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "진단년도",dataIndx: "EVA_YEAR", width: 100, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "공모명",dataIndx: "POOL_CODE_NM", width: 200, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "신청시 확인사항",dataIndx: "POOL_CD_NM", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "공모시작일자",dataIndx: "ST_DATE", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "공모시작일시",dataIndx: "ST_TIME", width: 100, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "공모종료일자",dataIndx: "ED_DATE", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "공모종료일시",dataIndx: "ED_TIME", width: 100, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "공모상태",dataIndx: "STATUS_CD", width: 100, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "지원현황",dataIndx: "APPLICATION_COUNT", width: 100, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
		{ title: "지원자정보", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true, render: popupRenderer}	
    ]; 
	options1.menuIcon =  true;
	options1.rowDblClick =  function (evt, ui) {           
		openPoolSchedualModify(ui.rowData);
               
    }
	options1.create = function(evt, ui) {
		//this.widget().pqTooltip();
	}
	//options1.columnTemplate = { render: tooltipRender };
	
    var grid = pq.grid("#grid1", options1);
    grid.refreshDataAndView();   
   
    
    $("#btnSearch").click(function() {
    	searchList();
     });     
    
    $("#btnInsertPop").click(function(){
    	openPoolSchedule();
    });   
   
    $(document).on('click', '.open-applicant-info', function() {
        var periodDivCd = $(this).data('period-div-cd');
        var evaYear = $(this).data('eva-year');
        var poolCd = $(this).data('pool-cd');
        openUpdWindow(periodDivCd, evaYear, poolCd);
    });
    
  
});
	
function searchList() {
	$("#loadingImage").show();
	
	
	$.ajax({
		url:"<%=request.getContextPath() %>/adminSchedule/getScheduleList.do",
		type:"post",
		data: {},
		cache: false,	
		dataType : "json",
		success:function(data){
			console.log(data.list);
			var grid = pq.grid( "#grid1", {dataModel: {data: data.list}});				
			grid.refreshDataAndView();			
		},
		error: errorHandler,
		complete: function(){		
			console.log("complete");
			$("#loadingImage").hide();
		}
	});
}

$(function () {
	
	$("#btnDel").click(function() { 	
 		
    	if(!confirm('삭제하시겠습니까?')){
    		return;
    	}
    	
    	var grid = pq.grid("#grid1");
    	
    	var checked = grid.Checkbox('stuState').getCheckedNodes();
    		
    	
		if(checked.length == 0){
			alert('삭제할 일정 정보를 선택해주세요.');
			return;
		}
		
    	
    	$.ajax({
    		url:"<%=request.getContextPath() %>/adminSchedule/deleteData.do",
    		type:"post",
    		data: {deleteData : JSON.stringify(checked)  },
    		cache: false,
    		dataType : "json",
    		success:function(data){
    			searchList();   			 
    		},
    		error: errorHandler,
    		
    	});
		
	});  
	
 	
});

</script>
</body>
</html>