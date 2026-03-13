<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<meta http-equiv="content-type" content="text/html;charset=UTF-8"/>
<head>
	<title>한국교육개발원 교원양성기관역량진단 - 공모시스템</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=yes">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />	
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/common.css" />
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-ui.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/menu.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/main.js"></script>
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
				<h2 class="title">개인정보처리 재동의</h2>

				<div class="sub_bg">	
					<div class="sub_box">	
						<!-- STEP -->
						<div class="joinStep">
							<ol>
								<li class="regs1 step01 on"style="width: 100%;">
									<div>
										<span class="num"></span>
										 개인정보 처리동의 일자: ${formattedPrivDttm}
									</div>
								</li>								
							</ol>
						</div>
						<!-- //STEP -->
						
						<!-- 약관동의 -->
						<div class="terms">
								<!-- <div class="terms">
									<p class="tit">회원약관</p>
									<div class="termsBox" tabindex="0">
										<strong>제1장 총 칙</strong><br><br>
										제1조(목적)<br>
										
									</div>
									<div class="agreeChk">
										<input type="radio" name="mAgree" id="mAgree1" value="1">
										<label for="mAgree1">동의합니다</label>

										<input type="radio" name="mAgree" id="mAgree2" value="2">
										<label for="mAgree2">동의하지 않습니다</label>

									</div>

								</div> -->
								<div class="terms mgt70">
									<p class="tit" style="width:100%; display:inline-block;"><span
											class="fl titTypeB">진단위원 후보자 신청서 개인정보 수집 이용 동의서</span>
									</p>
									<div class="termsBox" tabindex="0">
										한국교육개발원 교원양성기관 역량진단은 회원가입, 각종 서비스 등 기본적인 서비스 제공을 위한 필수정보와 맞춤 서비스 제공을 위한 선택정보로 구분하여 아래와 같은 개인정보를 수집하고 있습니다.<br><br>	
										1. 수집 및 이용 목적<br><br>	
										가. 교원양성기관 역량진단 진단위원 선정 관련 심사<br><br>	
										나. 공지사항 전달 및 신규 서비스 등 정보 안내<br><br>	
										2. 개인정보 수집 항목<br><br>	
										성명, 성별, 생년월일, E-mail, 연락처(핸드폰, 자택, 직장), 주소(자택, 직장), 직장정보(직장명, 직위, 부서명), 학력, 전임교원 경력<br><br>	
										3. 개인정보 보유 및 이용 기간<br><br>	
										2년마다 재동의 절차 후 보유<br><br>	
										4. 동의를 거부할 관리 및 동의 거부에 따른 안내<br><br>	
										이용자는 개인정보 수집 및 이용에 대한 동의를 거부할 수 있으며, 거부 시 회원가입이 제한됨을 알려드립니다. <br><br>										

									</div>
									<div class="agreeChk">
										<input type="radio" name="piAgree" id="piAgree1" value="Y">
										<label for="piAgree1">동의합니다</label>
										<input type="radio" name="piAgree" id="piAgree2" value="N">
										<label for="piAgree2">동의하지 않습니다</label>
									</div>
									<!-- <div class="agreeChk_all">
										<input type="checkbox" id="checkAll">전체 동의
									</div> -->
								</div>

								<!-- btn -->
								<div class="btn_box">
									<button type="button" title="취소" class="btn_gray2">취소</button>
									<button type="button" title="확인" class="btn_default">확인</button>
								</div>
								<!-- //btn -->
							
						</div>
						<!-- //약관동의 -->						
						
					</div>
				</div>

				</div>
				</div>

		</div>
		<!-- //container -->
		
		<jsp:include page="../common/footer.jsp" flush="false"></jsp:include>

	</div>
</body>

</html>

<script type="text/javascript">	

//시작시  로딩
$(document).ready(function() {
	
	/*  // 전체 동의 체크박스 클릭 시 mAgree, piAgree 라디오 버튼 그룹 모두 선택
    $("#checkAll").click(function() {
        if ($(this).is(":checked")) {
            $("input[id='mAgree1']").prop("checked", true);
            $("input[id='piAgree1']").prop("checked", true);
        } else {
            $("input[name='mAgree']").prop("checked", false);
            $("input[name='piAgree']").prop("checked", false);
        }
    }); */
   
   
    
    $(".btn_default").click(function() {
        var piAgreeChecked = $("input[name='piAgree']:checked").val();
        
        /* if (!piAgreeChecked) {
            alert("약관에 동의해야 개인정보처리 재동의가 가능합니다.");
            return;  
        } */

        // 동의 여부와 기타 필요한 데이터를 FormData에 추가
        var formData = new FormData();
        formData.append("piAgree", piAgreeChecked);  // 동의 여부 추가
        

        // AJAX 요청
        $.ajax({
            url: "<%=request.getContextPath() %>/reconsent/updateData.do",
            type: "post",
            cache: false,
            data: formData,
            contentType: false,
            processData: false,    
            success: function(data) {
                alert("개인정보처리 재동의 되었습니다.");
                window.location.reload(); 
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
    });
    
    $(".btn_gray2").click(function() {    	
    	window.location.href ="<%=request.getContextPath() %>/main/main.do";
    });
});	
	
	
		
	

</script> 