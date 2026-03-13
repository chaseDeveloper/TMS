<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<meta http-equiv="content-type" content="text/html;charset=UTF-8"/>
<head>
	<title>한국교육개발원 교원양성기관역량진단 - 공모시스템</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=yes">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />		

	<link rel="stylesheet" href="<%=request.getContextPath() %>/jquery-ui-1.13.2.custom/jquery-ui.css"/>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/paramquery-pro/pqgrid.min.css" />
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/paramquery-pro/pqgrid.ui.min.css" />
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/paramquery-pro/themes/gray/pqgrid.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/common.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/fontawesome-free-6.3.0-web/css/all.min.css" />
	<%-- <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/common20241220.css" /> --%>

	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-3.6.2.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/jquery-ui-1.13.2.custom/jquery-ui.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/paramquery-pro/pqgrid.min.js"></script>	
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/menu.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/main.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/common.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/paramquery-pro/localize/pq-localize-kr.js"></script>
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/paramquery-pro/jsZip-2.5.0/jszip.min.js" ></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-ui-timepicker-addon/1.6.3/jquery-ui-timepicker-addon.min.js"></script>
	
		
</head>

	<!-- header -->
		<header id="header" style="z-index: 100;">
			<div class="head_box">
				<h1 class="logo"><a href="<%=request.getContextPath() %>/main/main.do" title="메인페이지로 이동">한국교육개발원 교원양성기관역량진단  진단위원 공모시스템</a></h1>
				<button type="button" class="btnMenu" title="메뉴"><span>메뉴</span></button>
				<!-- gnb -->
				 <nav id="gnb">
		            <c:if test="${DI != null}">
		            <ul class="gnb">
		                <li><a href="<%=request.getContextPath() %>/pool/poolInfo.do">신청서 작성 및 수정</a></li>
		                <li><a href="<%=request.getContextPath() %>/poolSbmt/poolSbmtList.do" title="신청서 제출">신청서 제출</a></li>
		                <li><a href="<%=request.getContextPath() %>/poolResult/poolResult.do" title="선정결과 확인">선정결과 확인</a></li>
		              	<c:if test="${ROLE == 'A'}">
		              	<li>
							<a href="#">관리자 메뉴</a>
							<ul style="height: 500px;">
								<li><a href="<%=request.getContextPath() %>/adminSchedule/adminScheduleList.do">진단일정 조회</a></li>
								<li><a href="<%=request.getContextPath() %>/adminPool/adminPoolList.do">진단위원 후보자 조회</a></li>
								<li><a href="<%=request.getContextPath() %>/adminAccess/adminAccessList.do">개인정보 접근 기록</a></li>
								<li><a href="<%=request.getContextPath() %>/adminDelete/adminDeleteList.do">개인정보 파기 현황</a></li>
							</ul>
						</li>
						</c:if>		                
		            </ul>
		            </c:if>
		        </nav>
				<!-- //gnb -->
				<!-- side_menu -->
				<div class="topGlob">
					<c:if test="${DI == null}">
					<ul>
						<li><a href="<%=request.getContextPath() %>/join/joinStep01.do">회원가입</a></li>
						<li><a href="#" title="로그인" onclick="fnPopup()">로그인</a></li>
						<li><a href="#" title="관리자로그인" onclick="location.href='<%=request.getContextPath()%>/main/adminLoginPage.do'">관리자로그인</a></li>
					</ul>
					</c:if>
					<c:if test="${DI != null }">
					<ul>
						<li class="id"><a href="<%=request.getContextPath() %>/reconsent/reconsentView.do"><c:out value="${NAME}" /></a></li>
						<li class="logout"><a href="<%=request.getContextPath() %>/main/logout.do">로그아웃</a></li>						
					</ul>
					</c:if>
				</div>
				<!-- //side_menu -->

				<!-- mobile menu -->
				<nav id="mGnb">
					<button type="button" class="btnClose" title="닫기"><span>닫기</span></button>
					<div class="mbTop">
						<div class="mlogo"><a href="#" title="한국교육개발원 교원양성기관역량진단">한국교육개발원 교원양성기관역량진단</a></div>
						<c:if test="${DI == null }">
						<ul>
							<li><a href="<%=request.getContextPath() %>/join/joinStep01.do" title="회원가입">회원가입</a></li>
							<li><a href="#" title="로그인" onclick="fnPopup()">로그인</a></li>
						</ul>
						</c:if>
						<c:if test="${DI != null }">
						<ul>
							<li class="id"><a href="<%=request.getContextPath() %>/reconsent/reconsentView.do"><c:out value="${NAME}" /></a></li>
							<li class="logout"><a href="<%=request.getContextPath() %>/main/logout.do">로그아웃</a></li>
						</ul>
						</c:if>
					</div>
					<ul>
						<li>
							<a href="<%=request.getContextPath() %>/pool/poolInfo.do" title="신청서 작성 및 수정">신청서 작성 및 수정</a>
						</li>
						<li>
							<a href="<%=request.getContextPath() %>/poolSbmt/poolSbmtList.do" title="신청서 제출">신청서 제출</a>
						</li>
						<li>
							<a href="<%=request.getContextPath() %>/poolResult/poolResult.do" title="선정결과 확인">선정결과 확인</a>
						</li>						
					</ul>
				</nav>
				<!-- mobile menu -->
			</div>
		</header>
		<!-- //header -->

<% String errorMessage = (String) request.getAttribute("message"); %>
<script type="text/javascript">
//시작시  로딩
$(document).ready(function() {
	
	var errorMessage = '<%= errorMessage %>';
    if (errorMessage && errorMessage.length > 0 && errorMessage !== 'null') {
        alert(errorMessage);
    }
  
   /*  $('.dropdown').hover(function() {
        $(this).find('.dropdown-content').stop(true, true).slideDown();
    }, function() {
        $(this).find('.dropdown-content').stop(true, true).slideUp();
    }); */
  
    
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
		var openUrl = "<%=request.getContextPath() %>/main/nice.do?type=select";
		
		window.open(openUrl, 'nicePopup', option);
	}

</script>

</html>