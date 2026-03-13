<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div id="popupPoolList" style="display:none; position: relative;">
  <div style="display:flex; flex-direction:column; height:100%; line-height:30px; font-size:10pt;">
    <div style="margin-bottom:10px;">
      <div style="display:flex; flex-direction:column; justify-content:center; flex:1;">
      </div>
    </div>
    
    <div style="flex:1; overflow-x:auto; overflow-y:hidden; margin-top:40px;">
      <div id="grid2" style="background:white; width:1500px; height:100%; margin:auto;">
      </div>
    </div>
  </div>
  
  <div style="position:absolute; top:10px; right:10px; display:flex; gap:8px; ">
    <button id="btnInY" class="ui-button ui-corner-all ui-widget">
      <i class="fa-solid fa-file-pen"></i> 위원선정
    </button>
    <button id="btnInN" class="ui-button ui-corner-all ui-widget">
      <i class="fa-solid fa-trash"></i> 위원미선정
    </button>
    <button id="btnExcelDownload" class="ui-button ui-corner-all ui-widget">
      <i class="fa-sharp fa-solid fa-download"></i> 엑셀
    </button>
  </div>
  
</div>

<input type="hidden" id="hiddenPeriodDivCd" value="">
<input type="hidden" id="hiddenEvaYear" value="">
<input type="hidden" id="hiddenPoolCd" value="">


<%@ include file="adminSchedulePoolInfo.jsp" %>
<script type="text/javascript">
	function openUpdWindow(periodDivCd, evaYear, poolCd) {	
		console.log("openUpdWindow 함수실행");	
					
		// Hidden input에 값 설정
	    $("#hiddenPeriodDivCd").val(periodDivCd);
	    $("#hiddenEvaYear").val(evaYear);
	    $("#hiddenPoolCd").val(poolCd);	
	    
	    
		
		$( "#popupPoolList" ).dialog({
			title: '공모 지원자정보',
			resizable: false,
			height: 700,
			width: 1300,    		
			modal: true,
			beforeClose : function(){
				var grid = pq.grid( "#grid2");
				grid.destroy();
				
			},
			buttons : [
				 	{
				      text: "닫기",
	   			      click: function() {
	   			    	 searchList();
	   			    	console.log("조회 실행:");
	   			        $( this ).dialog( "close" );
	   			      }
				    }
			]
		});
		
		function tooltipRender(ui) {
		    return {
		        //here we inject html title attribute in each cell.
		        attr: {
		            title: ((ui.formatVal || "") + "").replace(/"/g, '&quot;')
		        }
		    }
		}
		
		function popupRenderer(ui) {
		    var rowData = ui.rowData;
			var lisData = rowData
		    // SEC_PLEDGE_YN_DT 필드에 데이터가 있는지 확인
		    if (rowData.SEC_PLEDGE_YN_DT && rowData.SEC_PLEDGE_YN_DT.trim() !== "") {
		        return '<button class="ui-button ui-corner-all ui-widget open-applicant-info" style="padding:.1rem .5rem;" ' +
		                'data-period-div-cd="' + rowData.PERIOD_DIV_CD + '" ' +
		                'data-eva-year="' + rowData.EVA_YEAR + '" ' +
		                'data-pool-cd="' + rowData.POOL_CD + '" onclick="openPopup(' + JSON.stringify(rowData).replace(/"/g, '&quot;') + ')">보안확약서 확인</button>';
		    } else {
		        return '<span style="color: grey;"></span>';
		    }
		}
		
		function secPledgeYnRenderer(ui) {
		    if (!ui.cellData) return ""; // 값이 없으면 미출력
		    if(ui.cellData.toLowerCase() === "na") return "N/A";
		    
		    var dateStr = ui.cellData.toString();
		    return dateStr;
		}

		
		function selectRenderer(ui) {
			if(ui != null) {
				var //grid = $(this).pqGrid('getInstance').grid,
	            grid = this,
	            rowData = ui.rowData,
	            rowIndx = ui.rowIndx,
	            dataIndx = ui.dataIndx;

				if(ui.cellData == 'Y') {
					grid.addClass({ rowIndx: rowIndx, dataIndx: dataIndx, cls: 'selYTd' });
					grid.removeClass({ rowIndx: rowIndx, dataIndx: dataIndx, cls: 'selNTd' });
				} else if(ui.cellData == 'N') {
					grid.addClass({ rowIndx: rowIndx, dataIndx: dataIndx, cls: 'selNTd' });
					grid.removeClass({ rowIndx: rowIndx, dataIndx: dataIndx, cls: 'selYTd' });
				} 
		        
			}
	    }
		
		
		
		var optionsPopup  = JSON.parse(JSON.stringify(common_options));
		optionsPopup.width = 'flex'; // 그리드가 가변 너비를 가지도록 설정
		//optionsPopup.minWidth = 1200; 
		optionsPopup .colModel = [
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
			{ title: "위원선정여부",dataIndx: "SEL_RSLT", width: 20, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,render: selectRenderer,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "공모분야",dataIndx: "JOB_TITLE", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "소속대학구분",dataIndx: "UNIV_DIV_NM", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "대학",dataIndx: "UNIV_NM", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "소속학과",dataIndx: "DEPT", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "직급",dataIndx: "POSITION", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "성명",dataIndx: "NAME", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "생년월일",dataIndx: "BIRTHDAY", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},			
			{ title: "성별",dataIndx: "GENDER", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "학력(학사)",dataIndx: "A_UNIV", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "학사세부전공",dataIndx: "A_MAJOR", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "학력(석사)",dataIndx: "B_UNIV", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "석사 세부전공",dataIndx: "B_MAJOR", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "학력(박사)",dataIndx: "C_UNIV", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "박사 세부전공",dataIndx: "C_MAJOR", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "자기소개1",dataIndx: "RESUME_1", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "자기소개2",dataIndx: "RESUME_2", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "결격사항(기피제척)여부",dataIndx: "DISQ_YN_1", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "기피제척 대학",dataIndx: "DISQ_UNIV_NM_1", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "결격사항(수행곤란대학)여부",dataIndx: "DISQ_YN_2", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "수행곤란 대학",dataIndx: "DISQ_UNIV_NM_2", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "이메일",dataIndx: "EMAIL", width: 180, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "휴대폰번호",dataIndx: "TEL_HP", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},			
			{ title: "재직증명서", dataIndx: "FILE_NM", width: 170, dataType: "string" , align:"center", halign:"center", hvalign: "center", valign:"center", render: anchorRenderer, menuIcon:false},
			{ title: "보안서약서 동의여부",dataIndx: "SEC_PLEDGE_YN", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  filter: { crules: [{ condition: 'range' }]}},
			{ title: "동일일자",dataIndx: "SEC_PLEDGE_YN_DT", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true, render: secPledgeYnRenderer},			
			{ title: "보안서약서 확인", width: 130, dataType: "string" , align:"center",valign: "center",  halign:"center", hvalign: "center", menuIcon:true,  render: popupRenderer}
			
	    ]; 
		
		var additionalInfo = {
	            periodDivCd: periodDivCd,
	            evaYear: evaYear,
	            poolCd: poolCd
	    };
		
		optionsPopup .menuIcon =  true;
		optionsPopup .rowDblClick =  function (evt, ui) {
			openUpPoolInfo(ui.rowData,additionalInfo);			
	    }
		optionsPopup.create = function(evt, ui) {
			this.widget().pqTooltip();
		}
		
		optionsPopup .menuUI = {
	        buttons: [], //use no button at bottom of filter menu
	        gridOptions: {
	            rowHt: 30 //increase height of rows in menu grid.
	        },
	        tabs: [ 'filter' ] //display only filter tab in menu.
	    };	
		
			
	    var grid = pq.grid("#grid2", optionsPopup );
	    grid.refreshDataAndView(); 
	    
	    /* grid.scrollColumn({ dataIndx: "SEC_PLEDGE_YN_DT" }, function() {
	    	console.log("Scroll to SEC_PLEDGE_YN_DT complete");
	    });
	   	 */
	    //excel 다운로드
	    $("#btnExcelDownload").click(function() {
	    	excelDownload();
	    });
	    
	    
	    searchList2(periodDivCd, evaYear, poolCd);
	 	
	   
	}
	
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
	
	function searchList2(periodDivCd, evaYear, poolCd) {
		
		
		$("#loadingImage").show();
			
		$.ajax({
			url:"<%=request.getContextPath() %>/adminSchedule/getSchedulePoolList.do",
			type:"post",
			data: {periodDivCd:periodDivCd, evaYear:evaYear, poolCd:poolCd},
			cache: false,	
			dataType : "json",
			success:function(data){					
				var grid = pq.grid( "#grid2", {dataModel: {data: data.list}});
				console.log(data);
				grid.refreshDataAndView();			
				
			},
			error: errorHandler,
			complete: function(){		
				console.log("complete");
				$("#loadingImage").hide();
			}
		});
		
	}

	function openPopup(data) {
	    var popupWindow = window.open('', 'popup', 'width=900,height=1400');

	    // 팝업 창에 폼을 추가하고 제출
	    var formHtml = '<form method="post" action="' + '<%=request.getContextPath()%>/adminSchedule/poolPrintMain.do' + '">' +
	                   '<input type="hidden" name="periodDivCd" value="' + data.PERIOD_DIV_CD + '">' +
	                   '<input type="hidden" name="evaYear" value="' + data.EVA_YEAR + '">' +
	                   '<input type="hidden" name="poolCd" value="' + data.POOL_CD + '">' +
	                   '<input type="hidden" name="di" value="' + data.DI + '">' + // Add DI parameter here
	                   '</form>';

	    $(popupWindow.document.body).html(formHtml);
	    popupWindow.document.forms[0].submit();
		
	  
		
	    // 팝업 창이 닫혔는지 확인
	    var intervalId = setInterval(function() {
	        if (popupWindow.closed) {
	            clearInterval(intervalId);
	        }
	    }, 500);
	}	
	
	function excelDownload() {
		var curTimeMills = new Date().getTime();
		var grid = pq.grid( "#grid2");			
		var realfilename = btoa(encodeURIComponent("공모 지원자 정보" + curTimeMills));
		//console.log(realfilename);
		grid.exportExcel( { url: "<%=request.getContextPath() %>/adminSchedule/exportData.do", filename: realfilename} );
	}	
	
	
$(function () {	
		
		
		$("#btnInY").click(function() { 	
	 		
	    	if(!confirm('위원으로 선정 하시겠습니까?')){
	    		return;
	    	}
	    	
	    	var grid = pq.grid("#grid2");
	    	
	    	var checked = grid.Checkbox('stuState').getCheckedNodes();
	    		
	    	
			if(checked.length == 0){
				alert('위원을 선택해주세요.');
				return;
			}
			
			// Hidden input에서 값 읽기
		    var periodDivCd = $("#hiddenPeriodDivCd").val();
		    var evaYear = $("#hiddenEvaYear").val();
		    var poolCd = $("#hiddenPoolCd").val();
	    	
			$.ajax({
	    		url:"<%=request.getContextPath() %>/adminSchedule/insYData.do",
	    		type:"post",
	    		data: {upData : JSON.stringify(checked)  },
	    		cache: false,
	    		dataType : "json",
	    		success:function(data){
	    			searchList2(periodDivCd, evaYear, poolCd); 			 
	    		},
	    		error: errorHandler,
	    		
	    	});
			
		});
		
		$("#btnInN").click(function() {
		    if(!confirm('위원으로 미선정 하시겠습니까?')) {
		        return;
		    }
		    
		    var grid = pq.grid("#grid2");
		    var checked = grid.Checkbox('stuState').getCheckedNodes();
		        
		    if(checked.length == 0) {
		        alert('위원을 선택해주세요.');
		        return;
		    }
		    
		    // Hidden input에서 값 읽기
		    var periodDivCd = $("#hiddenPeriodDivCd").val();
		    var evaYear = $("#hiddenEvaYear").val();
		    var poolCd = $("#hiddenPoolCd").val();
		    
		    $.ajax({
		        url: "<%=request.getContextPath() %>/adminSchedule/insNData.do",
		        type: "post",
		        data: { upData: JSON.stringify(checked) },
		        cache: false,
		        dataType: "json",
		        success: function(data) {
		        	searchList2(periodDivCd, evaYear, poolCd);
		        	
		        },
				error: errorHandler,
	    		
	    	});
		});
	
});

</script>