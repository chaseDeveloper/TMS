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
				<h2 class="title">회원가입</h2>

				<div class="sub_bg">	
					<div class="sub_box">	
						<!-- STEP -->
						<div class="joinStep">
							<ol>
								<li class="regs1 step01">
									<div>
										<span class="num">STEP. 01</span>
										약관동의
									</div>
								</li>
								<li class="regs2 step02 on">
									<div>
										<span class="num">STEP. 02</span>
										실명인증
									</div>
								</li>
								<li class="regs3 step03">
									<div>
										<span class="num">STEP. 03</span>
										가입완료
									</div>
								</li>
							</ol>
						</div>
						<!-- //STEP -->
						
						<!-- 실명인증 -->
						<div class="terms">
							<div class="terms_txt">
							<span class="title">아래 휴대폰 인증을 통해서 회원가입을 진행해 주세요.</span><br>
							<p>관련법률에 따라 주민등록번호 생성기를 이용하거나 주민등록번호를 도용하여 인터넷 서비스에 가입하는<br>경우에는 3년 이하의 징역 또는 1천만원 이하의 벌금에 처해지므로 반드시 본인 실명으로만 가입해 주십시오.</p>
							</div>
							
							<div class="joinSearch">							
								<button class="btn" type="button" title="새창열림" onclick="fnPopup()">가입여부 및 휴대전화 인증</button>						
							    <p class="txt">휴대전화 인증을 통한 가입을 원하시면<br>
								위 버튼을 눌러 회원가입을 진행해주세요.</p>
					   	   </div>
							<p><span class="col_orang">개정 "주민등록법"에 의해 타인의 주민등록번호를 부정사용하는 자는 3년 이하의 징역 또는 1천만원. 이하의 벌금이 부과될 수
								있습니다.<br>
								※ 관련법률: 주민등록법 제37조(벌칙) 제10호(시행일 : 2009.04.01)</span><br>
							만약, 타인의 주민번호를 도용하여 온라인 회원 가입을 하신 분들은 지금 즉시 명의 도용을 중단하시길 바랍니다.</p>
							
						</div>
						<!-- //실명인증 -->						
						
						
						
						
						
					</div>
				</div>
				</div>
			</div>

		</div>
		<!-- //container -->


		<jsp:include page="../common/footer.jsp" flush="false"></jsp:include>

	</div>
	
</body>




<script type="text/javascript">	

//시작시  로딩
$(document).ready(function() {
	
		
});

	
function fnPopup() {
	
	
	var winWidth = 400;
	var winHeight = 700;
	var winURL = "";
	var winName = "";
	var winPosLeft = (screen.width - winWidth) / 2;
	var winPosTop = (screen.height - winHeight) / 2 ;
	var winOpt = "width="+winWidth+",height="+winHeight+",top="+winPosTop+",left="+winPosLeft;

	var option = "menu=no,toolbar=no,scrollbars=no,status=no,resizable=yes,"+winOpt;
	var openUrl = "<%=request.getContextPath() %>/main/nice.do?type=insert";
	
	window.open(openUrl, 'nicePopup', option);
	
}



</script> 

</html>
