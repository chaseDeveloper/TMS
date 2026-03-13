<%@page import="com.clipsoft.clipreport.oof.OOFFile"%>
<%@page import="com.clipsoft.clipreport.oof.OOFDocument"%>
<%@page import="java.io.File"%>
<%@page import="com.clipsoft.clipreport.server.service.ReportUtil"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
OOFDocument oof = OOFDocument.newOOF();

String reportPath = "%root%" + request.getParameter("P_REPORT_ID"); 
String evaYear = request.getParameter("P_EVA_YEAR");
String clegId = request.getParameter("P_CLEG_ID");
String div = request.getParameter("P_DIV");
String usrId = request.getParameter("P_USR_ID");
String idxId = request.getParameter("P_IDX_ID");

OOFFile file = oof.addFile("crf.root", reportPath);

file.addField("P_EVA_YEAR", evaYear);
file.addField("P_CLEG_ID", clegId);
file.addField("P_DIV", div);
file.addField("P_USR_ID", usrId);
file.addField("P_IDX_ID", idxId);

oof.addConnectionData("*","oracle1");

System.out.println(oof.toString());
%><%@include file="Property.jsp"%><%
//세션을 활용하여 리포트키들을 관리하지 않는 옵션
//request.getSession().setAttribute("ClipReport-SessionList-Allow", false);
String resultKey =  ReportUtil.createReport(request, oof, "false", "false", request.getRemoteAddr(), propertyPath);
//oof 문서가 xml 일 경우 (oof.toString())
//String resultKey =  ReportUtil.createReport(request, oof.toString(), "false", "false", "localhost", propertyPath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Report</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" type="text/css" href="./css/clipreport.css">
<link rel="stylesheet" type="text/css" href="./css/UserConfig.css">
<link rel="stylesheet" type="text/css" href="./css/font.css">
<script type='text/javascript' src='./js/clipreport.js'></script>
<script type='text/javascript' src='./js/jquery-1.11.1.js'></script>
<script type='text/javascript' src='./js/UserConfig.js'></script>
<script type='text/javascript'>
var urlPath = document.location.protocol + "//" + document.location.host;

function html2xml(divPath){	
    var reportkey = "<%=resultKey%>";
	var report = createImportJSPReport(urlPath + "/ClipReport4/Clip.jsp", reportkey, document.getElementById(divPath));
    //실행
    //report.setSlidePage(true);
    report.view();
}
</script>
</head>
<body onload="html2xml('targetDiv1')">
<div id='targetDiv1' style='position:absolute;top:5px;left:5px;right:5px;bottom:5px;'></div>
</body>
</html>
