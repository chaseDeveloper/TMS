<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<meta http-equiv="content-type" content="text/html;charset=UTF-8"/>
<html>
<head>
    <title>NICE평가정보 - CheckPlus 안심본인인증</title>
    
    <script src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" >
    	$(document).ready(function(){
  	 		parent.opener.document.location.href = "${sParentLocation}";
    		parent.self.close();
    	});
    </script>
</head>
<body>
</body>
</html>