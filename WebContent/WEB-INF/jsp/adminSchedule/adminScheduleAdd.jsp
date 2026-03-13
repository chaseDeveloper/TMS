<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  
<div id="popupPoolSchedule" style="display:none;">
	<div class="tableHeaderContainer" >
		<div class="tableHeaderLabel">
		공모 일정 정보 등록
		</div>
	</div>	
	<div class="divTable" >		
		<div class="divTr" >			
			<div class="divTh" style="width:16%;">주기구분</div>
			<div style="width:34%;">
				<select style="width:100%;" id="popInsPeriodDivCd">				
					 <option value="6">6주기</option>					
				</select>
			</div>	
			<div class="divTh" style="width:16%;">진단연도</div>
			<div style="width:34%;">
				<select style="width:100%;" id="popInsEvaYear">
					<option value="2024">2024</option>
					<option value="2025">2025</option>
					<option value="2026">2026</option>
				</select>				
			</div>							
		</div>
		<div class="divTr" >			
			<div class="divTh" style="width:16%;">공모분야</div>
			<div style="width:34%;">
				<select style="width:100%;" id="popInsPoolCd">
					<c:forEach items="${poolCdList }" var="item">
							<option value="${item.ORG_CD }" >${item.CODE_NM }</option>
					</c:forEach>
				</select>				
			</div>							
		</div>
		<div class="divTr" >			
			<div class="divTh" style="width:16%;">신청시 확인사항</div>
			<div style="width:84%;">
				<input type="text" style="width:100%;" id="popInsPoolNm" />
			</div>							
		</div>
		<div class="divTr">
		    <div class="divTh" style="width:16%;text-align:center;">공모 시작일시</div>
		    <div style="width:34%;">
		        <input type="text" style="width:100%;text-align:center;color:#555;" id="popInsStartDate" placeholder="YYYY-MM-DD"/>
		    </div>
		    <div style="width:25%;">
		        <select style="width:48%;text-align:center;" id="popInsStartHour"></select>
		        <select style="width:48%;text-align:center;" id="popInsStartMinute"></select>
		    </div>
		</div>
		<div class="divTr">
		    <div class="divTh" style="width:16%;text-align:center;">공모 종료일시</div>
		    <div style="width:34%;">
		        <input type="text" style="width:100%;text-align:center;color:#555;" id="popInsEndDate" placeholder="YYYY-MM-DD"/>
		    </div>
		    <div style="width:25%;">
		        <select style="width:48%;text-align:center;" id="popInsEndHour"></select>
		        <select style="width:48%;text-align:center;" id="popInsEndMinute"></select>
		    </div>
		</div>
		<div class="divTr" >
			<div class="divTh" style="width:16%;">운영상태</div>
			<div style="width:34%;">
				<select style="width:100%;" id="popInsStatusCd">
					<option value="Y">예</option>
					<option value="N">아니오</option>
				</select>				
			</div>				
		</div>		
	</div> 
</div>



<script type="text/javascript">	
	
$(function() {
	 // 날짜 선택기 초기화
    $("#popInsStartDate, #popInsEndDate").datepicker({
        dateFormat: "yy-mm-dd",
        changeMonth: true,
        changeYear: true,
        yearRange: "-10:+20",
        showButtonPanel: true
    });

    // 시간과 분을 위한 드롭다운 메뉴 생성
    for (let i = 0; i < 24; i++) {
        $('#popInsStartHour, #popInsEndHour').append($('<option>', {
        	value: ('0' + i).slice(-2),
            text: ('0' + i).slice(-2) + '시'
        }));
    }
  
    for (let i = 0; i < 60; i += 5) {
        const padded = ('0' + i).slice(-2);
        $('#popInsStartMinute, #popInsEndMinute').append($('<option>', {
            value: padded,
            text: padded + '분'
        }));
    }

    // 기존의 시간 입력 필드 제거 및 버튼 이벤트 수정
    $("#btnPopInsStartYmd, #btnPopInsEndYmd").remove();
});

	function openPoolSchedule() {
	    // 폼 요소 초기화
	    $("#popInsPeriodDivCd").val('');
	    $("#popInsEvaYear").val('');
	    $("#popInsPoolCd").val('');
	    $("#popInsPoolNm").val('');
	    $("#popInsStartDate").val('');
	    $("#popInsStartHour").val('');
	    $("#popInsStartMinute").val('');
	    $("#popInsEndDate").val('');
	    $("#popInsEndHour").val('');
	    $("#popInsEndMinute").val('');
	    $("#popInsStatusCd").val('');
	   
	    // 초기화가 완료된 후 데이터 로드 및 팝업 설정
	    loadPoolData();
	    console.log("openPoolSchedule 함수 실행");
	}
	

	
	function loadPoolData(rowData) {
		console.log("loadPoolData 함수 실행");
		
		if (rowData != null) {
		    $("#popInsPeriodDivCd").val(rowData.PERIOD_DIV_CD).prop('disabled', true);
		    $("#popInsEvaYear").val(rowData.EVA_YEAR).prop('disabled', true);
		    $("#popInsPoolCd").val(rowData.POOL_CD).prop('disabled', true);
		    $("#popInsPoolNm").val(rowData.POOL_CD_NM);
		    $("#popInsStartDate").val(rowData.ST_DATE);
		    $("#popInsStartHour").val(rowData.ST_HOUR);
		    $("#popInsStartMinute").val(rowData.ST_MINUTE);
		    $("#popInsEndDate").val(rowData.ED_DATE);
		    $("#popInsEndHour").val(rowData.ED_HOUR);
		    $("#popInsEndMinute").val(rowData.ED_MINUTE);
		    $("#popInsStatusCd").val(rowData.STATUS_CD);
		}else{
			$('#popInsPeriodDivCd').prop('selectedIndex', 0);
			$('#popInsEvaYear').prop('selectedIndex', 0);
			$('#popInsPoolCd').prop('selectedIndex', 0);
			$('#popInsStatusCd').prop('selectedIndex', 0);
			
		    $("#popInsStartHour").prop('selectedIndex', 0);
		    $("#popInsStartMinute").prop('selectedIndex', 0);
		    $("#popInsEndHour").prop('selectedIndex', 0);
		    $("#popInsEndMinute").prop('selectedIndex', 0);
			
			$("#popInsPeriodDivCd").prop('disabled', false);
			$("#popInsEvaYear").prop('disabled', false);
			$("#popInsPoolCd").prop('disabled', false);
		}
		
		$( "#popupPoolSchedule" ).dialog({
    		title: '공모 일정 등록',
    		resizable: false,
    		height: 450,
    		width: 630,    		
    		modal: true,
    		buttons : [
    			{
    			      text: "저장",
	   			      click: function() {
	   			    	  if(rowData == null){
	   			    		    insertPoolSchedual();	   		   			    		  
	   			    	  } else {
	   			    		 	updatePoolSchedual(rowData);  	   			    		  
	   			    	  }
	   			      } 
   			    }, 
   			 	{
  			      text: "닫기",
	   			      click: function() {
	   			        $( this ).dialog( "close" );
	   			      }
 			    }
    		]
		});
	}
	
	function insertPoolSchedual() {	
		
		
		
		var periodDivCd = $("#popInsPeriodDivCd").val();		
		var evaYear = $("#popInsEvaYear").val();	           
	    var poolCd = $("#popInsPoolCd").val();	            
	    var poolNm = $("#popInsPoolNm").val();	
	    var startDate = $("#popInsStartDate").val();	
	    var startHour = $("#popInsStartHour").val();	
	    var startMinute = $("#popInsStartMinute").val();	
	    var endDate = $("#popInsEndDate").val();	
	    var endHour = $("#popInsEndHour").val();	
	    var endMinute = $("#popInsEndMinute").val();
	    var statusCd = $("#popInsStatusCd").val();
	    
	    				
		var formData = new FormData();
		formData.append("periodDivCd", periodDivCd);		
		formData.append("evaYear", evaYear);
		formData.append("poolCd", poolCd);
		formData.append("poolNm", poolNm);
		formData.append("startDate", startDate);
		formData.append("startHour", startHour);
		formData.append("startMinute", startMinute);
		formData.append("endDate", endDate);		
		formData.append("endHour", endHour);
		formData.append("endMinute", endMinute);
		formData.append("statusCd", statusCd);
		
		$.ajax({
			url:"<%=request.getContextPath() %>/adminSchedule/insertData.do",
			type:"post",
			cache: false,
			data:formData,
			enctype: 'form-data',
			contentType : false,
			processData: false,			
			success:function(data){					
				alert("저장 되었습니다.");
				$("#popupPoolSchedule").dialog("destroy");
				searchList();
				
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

		
	function openPoolSchedualModify(rowData) {
		console.log("openPoolSchedualModify 함수실행");
				
		loadPoolData(rowData);	
	}
	
	function updatePoolSchedual(rowData) {
		console.log('updatePoolSchedual 함수 실행');
		
		
		var periodDivCd = $("#popInsPeriodDivCd").val();		
		var evaYear = $("#popInsEvaYear").val();	           
	    var poolCd = $("#popInsPoolCd").val();	            
	    var poolNm = $("#popInsPoolNm").val();	
	    var startDate = $("#popInsStartDate").val();	
	    var startHour = $("#popInsStartHour").val();	
	    var startMinute = $("#popInsStartMinute").val();	
	    var endDate = $("#popInsEndDate").val();	
	    var endHour = $("#popInsEndHour").val();	
	    var endMinute = $("#popInsEndMinute").val();
	    var statusCd = $("#popInsStatusCd").val();
		
				
		var formData = new FormData();
		formData.append("periodDivCd", periodDivCd);		
		formData.append("evaYear", evaYear);
		formData.append("poolCd", poolCd);
		formData.append("poolNm", poolNm);
		formData.append("startDate", startDate);
		formData.append("startHour", startHour);
		formData.append("startMinute", startMinute);
		formData.append("endDate", endDate);		
		formData.append("endHour", endHour);
		formData.append("endMinute", endMinute);
		formData.append("statusCd", statusCd);
		
		
		$.ajax({
			url:"<%=request.getContextPath() %>/adminSchedule/updateData.do",
			type:"post",
			cache: false,
			data:formData,
			enctype: 'form-data',
			contentType : false,
			processData: false,			
			success:function(data){					
				alert("수정 되었습니다.");
				$("#popupPoolSchedule").dialog("destroy");
				searchList();
				
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