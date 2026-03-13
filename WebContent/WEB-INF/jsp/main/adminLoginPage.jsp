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
		<div id="container">
			<div id="content">
				<div class="main_con_bg ma_t70">
					<div class="mConBox">
						<div class="mbox">
							<div class="section_header">	
								<h3>관리자 로그인</h3>
							</div>
							<div class="w_50">
								ID <input type="text" style="width:100%;" id="usrId" />
							</div>
							<div class="w_50">
								PW <input type="password" style="width:100%;" id="pw" />
							</div>
							<div style="margin-top: 10px">
								<button type="button" title="로그인" class="btn_default"  id="loginBtn">로그인</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<jsp:include page="../common/footer.jsp" flush="false"></jsp:include>
	</div>
</body>
</html>

<script type="text/javascript">	
$("#loginBtn").click(function() {
	if($("#usrId").val().trim() == '') {
		alert("ID를 입력하여 주십시오.");
		return false;
	}
	
	if($("#pw").val().trim() == '') {
		alert("ID를 입력하여 주십시오.");
		return false;
	}
	
	$.ajax({
		url:"<%=request.getContextPath() %>/main/adminLogin.do",
		data: {usrId : $("#usrId").val().trim(), 
				pw : $("#pw").val().trim()}, 
		type:"post",
		cache: false,	
		dataType : "json",
		success: function(data){
			if(data.result == 'S') {							
				location.href="<%=request.getContextPath() %>/main/main.do";							
			} else {
				if(data.code == '1' || data.code == '2') {
					alert("계정 정보가 존재하지 않습니다.");
				} else {
					alert("오류가 발생 했습니다.");
				}
			}
			
		},				
		error: function(xhr, status, error){
			alert("오류가 발생 했습니다.");
		}
	});	
})

</script> 