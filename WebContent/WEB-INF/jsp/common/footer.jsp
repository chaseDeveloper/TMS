<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- footer-->
		<footer>
			<div class="footer_box">
				<div class="footer_con">
					<h2><a href="<%=request.getContextPath() %>/main/main.do" title="메인페이지로 이동"><img src="<%=request.getContextPath() %>/images/kedi_footer_logo.png" alt="한국교육개발원"></a></h2>
					<div class="foot_link">
						<ul>
							<li ><a href="https://necte.kedi.re.kr/privacy.do" style = "color: #3396FF!important" target="_blank">개인정보처리방침</a></li>
						</ul>
					</div>
					<div class="foot_link">
						<p>충청북도 진천군 덕산읍 교학로 7</p>
						<p><span>TEL : 043-530-9388</span></p>
						<p><span>Copyright © 2023 KEDI. All Rights Reserved.</span></p>
					</div>
				</div>
			</div>
		</footer>
		
<div id="pdfView" class="popupSearch" style="display:none;">
	<iframe id="pdfViewFrm" style="width:100%;height:100%;border:none;margin:0;padding:0;"></iframe>
</div>
<!-- //footer-->