<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>       
<html>
<head>
    <title>NICE평가정보 - CheckPlus 안심본인인증 테스트</title>
    <script src="/tbp//js/jquery/jquery-1.9.1.js"></script>
    <script type="text/javascript" >
    	$("#confirm").click(function(){    		
    		window.close();
    	});
    </script>
</head>
<body>
    
    <p /><p /><p /><p />
    본인인증이 실패하였습니다.<br />     
    <br />
    
    <button id="confirm"></button>
    
    <c:out value = "sErrorCode" />    
</body>
</html>