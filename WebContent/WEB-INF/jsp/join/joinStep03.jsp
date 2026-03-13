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
								<li class="regs2 step02">
									<div>
										<span class="num">STEP. 02</span>
										실명인증
									</div>
								</li>
								<li class="regs3 step03 on">
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
							<div class="joinEnd">
							<p class="txt1"><img src="<%=request.getContextPath() %>/images/icon_join.png" alt="회원가입 완료 체크 이미지"/></p>
							<p class="txt2">회원가입이 완료되었습니다.</p>
							
						</div>
							
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

</html>


<script type="text/javascript">	

//시작시  로딩
$(document).ready(function() {
	
});
</script>