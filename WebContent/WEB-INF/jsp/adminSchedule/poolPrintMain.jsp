<%@page import="com.clipsoft.clipreport.oof.OOFFile"%>
<%@page import="com.clipsoft.clipreport.oof.OOFDocument"%>
<%@page import="com.clipsoft.clipreport.oof.connection.*"%>
<%@page import="java.io.File"%>
<%@page import="com.clipsoft.clipreport.server.service.ReportUtil"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%

String host = request.getServerName() + ":" + request.getServerPort();
String path = request.getContextPath();

//String di = (String) request.getParameter("di");
String di = (String)request.getAttribute("di");
String periodDivCd = (String)request.getAttribute("periodDivCd");
String evaYear = (String)request.getAttribute("evaYear");
String poolCd = (String)request.getAttribute("poolCd");
String poolCdNm = (String)request.getAttribute("poolCdNm");
String position = (String)request.getAttribute("position");
String univCdNm = (String)request.getAttribute("univCdNm");
String name = (String)request.getAttribute("name");
String sign = (String)request.getAttribute("sign");
String ynDt = (String)request.getAttribute("ynDt");


//인코딩
request.setCharacterEncoding("utf-8");
 
//리포트 생성에 필요한 파라미터
//	String crfName   = (request.getParameter("crfName")   == "" || request.getParameter("crfName")   == null ? "CLIP" : request.getParameter("crfName") );
//	String crfParams = (request.getParameter("crfParams") == "" || request.getParameter("crfParams") == null ? "" : request.getParameter("crfParams") );
//	String crfData = (request.getParameter("crfData") == ""   || request.getParameter("crfData")   == null ? "" : request.getParameter("crfData") );



//oof document 생성
OOFDocument oof = OOFDocument.newOOF();

////리포트 파일 명
OOFFile file = oof.addFile("crfe.root", "%root%/crf/poolSign01Admin.crfe");

//커넥션의 네임스페이스, XML/CSV/JSON 데이터 문자열..
//OOFConnectionFile mc = oof.addConnectionFile("*", "C:\\Users\\CornerStoneD\\Downloads\\test.xml");


file.addField("P_PERIO_DIV_CD", periodDivCd);
file.addField("P_EVA_YEAR", evaYear);
file.addField("P_POOL_CD", poolCd);
file.addField("P_POOL_CD_NM", poolCdNm);
file.addField("P_POSITION", position);
file.addField("P_UNIV_CD_NM", univCdNm);
file.addField("P_NAME", name);
file.addField("SIGN1", sign);
file.addField("P_YNDT", ynDt);

//데이터셋 네임스페이스, 데이터 인코딩, XML 데이터의 root 경로(Xpath)
//mc.addContentParamXML("*", "UTF-8", "{%dataset.xml.root%}");

	//리포트 파라미터
	//System.out.println("crfParams:" + crfParams);
// 	String[] crfParamsArr = crfParams.split("\\|");
// 	for(String param : crfParamsArr){
// 		String[] temp = param.split(":");
// 		String name  = ""; 
// 		String value = "";
		
// 		if( temp.length == 0 ){
// 			name  = "";
// 			value = "";
// 		} else if( temp.length == 1 ){
// 			name  = temp[0];
// 			value = "";
// 		} else if( temp.length == 2 ){
// 			name  = temp[0];
// 			value = temp[1];
// 		}

// 		crfFile1.addField(name, value);
// 	}


/* oof.addConnectionData("*","oracle1"); */

System.out.println("로그"+oof.toString());


%><%@include file="/ClipReport4/Property.jsp"%><%
//세션을 활용하여 리포트키들을 관리하지 않는 옵션
//request.getSession().setAttribute("ClipReport-SessionList-Allow", false);
String resultKey =  ReportUtil.createEForm(request, oof, "false", "false", request.getRemoteAddr(), propertyPath);

//리포트의 특정 사용자 ID를 부여합니다.
//clipreport4.properties 의 useuserid 옵션이 true 일 때만 적용됩니다. 
//clipreport4.properties 의 useuserid 옵션이 true 이고 기본 예제[String resultKey =  ReportUtil.createEForm(request, oof, "false", "false", request.getRemoteAddr(), propertyPath);] 사용 했을 때 세션ID가 userID로 사용 됩니다.
//String resultKey =  ReportUtil.createEForm(request, oof, "false", "false", request.getRemoteAddr(), propertyPath, "userID");

//리포트를 저장 스토리지를 지정하여 생성합니다.
//String resultKey =  ReportUtil.createEFormByStorage(request, oof, "false", "false", request.getRemoteAddr(), propertyPath, "rpt2");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>서식출력</title>
<meta name="viewport" content="width=800, user-scalable=no">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/ClipReport4/css/clipreport.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/ClipReport4/css/eform.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/ClipReport4/css/UserConfig.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/ClipReport4/css/font.css">
<script type='text/javascript' src='<%=request.getContextPath() %>/ClipReport4/js/jquery-1.11.1.js'></script>
<script type='text/javascript' src='<%=request.getContextPath() %>/ClipReport4/js/clipreport.js?ver=1.0'></script>
<script type='text/javascript' src='<%=request.getContextPath() %>/ClipReport4/js/UserConfig.js'></script>






<script type='text/javascript'>
var urlPath = document.location.protocol + "//" + document.location.host;
	
function html2xml(divPath){	
    var eformkey = "<%=resultKey%>";
	var eform = createImportJSPEForm(urlPath + "/apply/ClipReport4/Clip.jsp", eformkey, document.getElementById(divPath));
		
	eform.setNecessaryEnabled(true);
		
	//eform.setSlidePage(true);    //리포트 뷰어의 옵션
	eform.view();
 	
}



</script>
</head>
<body onload="html2xml('targetDiv1')">
<div id='targetDiv1' style='position:absolute;top:5px;left:5px;right:5px;bottom:5px;'></div>
</body>
</html>
