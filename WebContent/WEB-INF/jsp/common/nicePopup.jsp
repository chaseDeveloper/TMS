<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<meta http-equiv="content-type" content="text/html;charset=UTF-8"/>
<title>휴대폰 본인인증</title>

<script language="javascript">

/* $(document).ready(function() {
	fnPopup();
});
 */

function fnPopup(){	
		
	var myForm = document.getElementById("form_chk");
	myForm.submit();
}
</script>

</head>

<body >	
	<!-- <div id="agreeDiv" style="color:black;width:100%;" class="formtable">
		<table  cellpadding="0" cellspacing="0" style="width:380px;margin-left:auto;margin-right:auto;border-color:grey;">
		  	<tbody style='border:0px;'>
		  		<tr>
	        		<td colspan=2  style="height:20px;">
	        	</td>
	        	<tr>
	          		<td colspan=2 style="text-align:left;padding:20px 5px 5px 5px;border:1px solid #CCC;">
						 <b>개인정보 제3자 제공 동의에 대한 안내</b><br><br>
							 한국교육개발원 은 본인인증을 위해 아래와 같이 제3자에게 정보를 제공합니다. <br><br>
							 개인정보보호법 등에 관한 법률에 따라 아래와 같이 개인정보 제공에 대한 사항을 안내드리오니 자세히 읽은 후 동의하여 주시기 바랍니다. <br><br>
							정보제공에 동의하지 않을 수 있으며, 동의하지 않을 경우 교원양성기관 역량진단 위촉 등에 제한이 있을 수 있습니다.<br><br>
							
							- 제공받는 자: NICE평가정보<br>
							- 제공목적: 본인인증을 위한 핸드폰 인증<br>
							- 제공정보: 성명, 핸드폰 번호, 생년월일<br>
							- 보유 및 이용기간: 본인인증 확인 후 즉시 삭제<br>
							<br>
							<br>
        			</td>
	        	</tr>
	        	<tr>
	        		<td colspan=2  style="height:20px;">
	        	</td>
	        	<tr>
	        		<td style="border:0px;text-align:center;">
		        		<input type="radio" id="agree1" name="q1" value="1"  onclick="fnPopup();return false;"  />
						<label for="agree1">동의함</label>
	        		</td>
					<td style="border:0px;text-align:center;">
		        		<input type="radio" id="agree2" name="q2" value="2"  onclick="self.close();"  />
						<label for="agree2">동의하지 않음</label>
	        		</td>
	        	</tr>	        	
			</tbody>
		</table>      
	</div> -->
	
	
	<form id="form_chk" name="form_chk" method="post" action="https://nice.checkplus.co.kr/CheckPlusSafeModel/checkplus.cb">
		<input type="hidden" name="m" value="checkplusSerivce">						<!-- 필수 데이타로, 누락하시면 안됩니다. -->
		<input type="hidden" name="EncodeData" value="${sEncData}">		<!-- 위에서 업체정보를 암호화 한 데이타입니다. -->
	    
	    <!-- 업체에서 응답받기 원하는 데이타를 설정하기 위해 사용할 수 있으며, 인증결과 응답시 해당 값을 그대로 송신합니다.
	    	 해당 파라미터는 추가하실 수 없습니다. -->
		<input type="hidden" name="param_r1" value="">
		<input type="hidden" name="param_r2" value="">
		<input type="hidden" name="param_r3" value="">	    		
	</form>

 <script>
	var myForm = document.getElementById("form_chk");
	myForm.submit();	
</script>
</body>
</html>