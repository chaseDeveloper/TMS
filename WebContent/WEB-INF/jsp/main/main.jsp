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
								<h3>회원가입 및 로그인 방법</h3>
							</div>
								<div class="about_content">
									<ul>
										<li>상단의 우측의 회원가입 버튼 클릭</li>
										<li>개인정보수집 이용 동의서 동의 후 실명인증(핸드폰 본인인증)시 회원가입 완료</li>
										<li>로그인 시 상단우측의 로그인 버튼 클릭</li>
										<li>핸드폰 본인 인증으로 로그인</li>										
									</ul>
								</div>
						</div>
						<div class="mbox">
							<img src="<%=request.getContextPath() %>/images/main_img01.png" alt="회원가입 화면 이미지"/>
						</div>
					</div>
				</div>
				
				
				<div class="main_con_bgw">
					<div class="mConBox">
						<div class="mbox">
							<img src="<%=request.getContextPath() %>/images/main_img02.png" alt="신청서작성 및 수정 화면 이미지"/>
						</div>
						<div class="mbox">
							<div class="section_header">	
								<h3>신청서작성 및 수정 방법</h3>
							</div>
								<div class="about_content">
									<ul>
										<li>로그인 후 상단의 신청서 작성 및 수정 메뉴 버튼 클릭</li>
										<li>개인신상정보 입력 대학명 주소 입력은 오른쪽 검색버튼 클릭 검색결과를  더블클릭으로 대상지정</li>
										<li>파일 선택 클릭 후 재직증명서 필수제출</li>
										<li>재직경력,진단위원,준비위원 활동경험 추가입력시 오른쪽 +버튼 클릭 추가된 로우에 입력</li>
										<li>정보 입력 후 저장 버튼 클릭</li>
									</ul>
								</div>
						</div>
					</div>
				</div>
				
				
				<div class="main_con_bg">
					<div class="mConBox">
						<div class="mbox">
							<div class="section_header">	
								<h3>신청서 제출 방법</h3>
							</div>
								<div class="about_content">
									<ul>
										<li>로그인 후 상단의 신청서 제출 메뉴 버튼 클릭</li>
										<li>조회된 목록의 오른쪽 신청서 제출하기 버튼 클릭</li>
										<li>신청서 제출 팝업에서 기피제척대상 대학 질의서에 “예” 라고 응당합 경우 대학검색 버튼을 클릭하여 대학을 검색하고 더블클릭하여 기피제척대학 목록에 추가</li>
										<li>신청서 제출 팝업의 하단의 제출버튼 클릭시 제출처리, 제출취소의 경우 동일한 팝업에서 제출취소버튼 클릭</li>
									</ul>
								</div>
						</div>
						<div class="mbox">
							<img src="<%=request.getContextPath() %>/images/main_img03.png" alt="신청서 제출 화면 이미지"/>
						</div>
					</div>
				</div>
				
				<div class="main_con_bgw">
					<div class="mConBox">
						<div class="mbox">
							<img src="<%=request.getContextPath() %>/images/main_img04.png" alt="선정결과 화면 이미지"/>
						</div>
						<div class="mbox">
							<div class="section_header">	
								<h3>선정결과 확인 방법</h3>
							</div>
								<div class="about_content">
									<ul>
										<li>로그인 후 상단의 선정결과 확인 메뉴 버튼 클릭</li>
										<li>해당 페이지에서 내가 지원한 공모지원 목록 확인 가능</li>										
										<li>위원으로 선정될경우 보안서약서 동의여부 '동의하기'버튼 활성화</li>
										<li>보안서약서 작성시 '동의하기'버튼 클릭 보안서약서 팝업  내용확인후 서명 오른쪽 클릭 서명후 팝업의 오른쪽 상단 저장버튼 클릭</li>
									</ul>
								</div>
						</div>
					</div>
				</div>
				
				
				<div class="main_con_bg">
					<div class="mConBox">
						<div class="result_box">
							<div class="img"><img alt="진단위원 선정 결과 알림 이미지" src="<%=request.getContextPath() %>/images/main_img_noti01.png"></div>
							<div class="result">
								<ul>
									<li>진단위원 후보자신청 일정<span>진단위원 선정 결과는 개별 통보합니다.</span></li>
								</ul>
							</div>
							</div>							
					</div>
				</div>
				
				
				
				<div class="main_con_bgn">
					<div class="mConBox">						
							<!-- 바로가기 메뉴 -->
							<div class="schedule">
								<div class="schedule_box">
									<ul id="scheduleList">			
									</ul>
								</div>
					
							</div>
							<!-- //바로가기 메뉴 -->						
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


//기능1 공모 일정 STATUS_CD가 최근 3개 만 출력
function fn_getPoolScheList() {
	
	$.ajax({
		url:"<%=request.getContextPath() %>/main/selectPoolScheList.do",
		type:"post",
		cache: false,
		data:{},			
		dataType : "json",
		success:function(data){
			if(data.result == "S") {
				console.log(data);
				console.log("공모 일정");
				var list = data.list;
				if (!list || list.length === 0) {
					 $("#scheduleList").html("진행중인 공모일정이 없습니다.").css("background-color", "white");
				} else {
					
					var scheduleList = '';
					for(var i=0;i<list.length;i++) {
						
						scheduleList += '<li>'+ list[i].PERIOD_DIV_CD + '주기 ' +  list[i].EVA_YEAR + '진단년도 <br>' + list[i].POOL_CODE_NM + '<span>' +list[i].ST_YMD+ '~' +list[i].ED_YMD+ '</span></li>';
					}
					scheduleList += '';
					$("#scheduleList").html("");
					$("#scheduleList").append(scheduleList);
					
				}
			} else {
				console.log("실패");
			}	
		},
		error:function(key, textStatus, errorThrown){
			alert("에러 발생");
		}
	});
}

//시작시 공모일정 로딩
$(document).ready(function() {
	fn_getPoolScheList();
	
});
</script> 