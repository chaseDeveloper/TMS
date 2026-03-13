<%@page import="com.clipsoft.clipreport.oof.OOFFile"%>
<%@page import="com.clipsoft.clipreport.oof.OOFDocument"%>
<%@page import="com.clipsoft.clipreport.oof.connection.*"%>
<%@page import="java.io.File"%>
<%@page import="com.clipsoft.clipreport.server.service.ReportUtil"%>
<%@page import="com.clipsoft.org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="com.clipsoft.org.json.simple.JSONObject"%>
<%@page import="com.clipsoft.org.json.simple.parser.JSONParser"%>
<%
	//인코딩
	request.setCharacterEncoding("utf-8");
    
	//리포트 생성에 필요한 파라미터
	String crfName   = (request.getParameter("crfName")   == "" || request.getParameter("crfName")   == null ? "CLIP" : request.getParameter("crfName") );
	String crfParams = (request.getParameter("crfParams") == "" || request.getParameter("crfParams") == null ? "" : request.getParameter("crfParams") );
	String crfData = (request.getParameter("crfData") == ""   || request.getParameter("crfData")   == null ? "" : request.getParameter("crfData") );

	//oof document 생성, '자세한 사항은 클립리포트 4.0 OOF Generator' 메뉴얼 참고 !!
	OOFDocument oof = OOFDocument.newOOF();

	////리포트 파일 명
	OOFFile crfFile1 = oof.addFile("crf.root", "%root%/crf/" + crfName + ".crf");

	//커넥션의 네임스페이스, XML/CSV/JSON 데이터 문자열.. CSV데이터일 경우에 <![CDATA[]]>로 쌓아서 사용
	OOFConnectionMemo mc = oof.addConnectionMemo("*", crfData);
	
	//데이터셋 네임스페이스, 데이터 인코딩, XML 데이터의 root 경로(Xpath)
	mc.addContentParamXML("*", "UTF-8", "{%dataset.xml.root%}");

	//리포트 파라미터
	//System.out.println("crfParams:" + crfParams);
	String[] crfParamsArr = crfParams.split("\\|");
	for(String param : crfParamsArr){
		String[] temp = param.split(":");
		String name  = ""; 
		String value = "";
		
		if( temp.length == 0 ){
			name  = "";
			value = "";
		} else if( temp.length == 1 ){
			name  = temp[0];
			value = "";
		} else if( temp.length == 2 ){
			name  = temp[0];
			value = temp[1];
		}

		crfFile1.addField(name, value);
	}
	//System.out.println("oof:" + oof.toString());

%><%@include file="../Property.jsp"%><%
String resultKey =  ReportUtil.createReport(request, oof.toString(), "false", "false", "localhost", propertyPath);
%>
<%=resultKey%>
