<%@page import="java.io.File"%>
<%@page import="com.clipsoft.clipreport.server.service.ReportUtil"%>
<%@page import="com.clipsoft.clipreport.oof.*"%>
<%@page import="com.clipsoft.org.apache.commons.lang.StringEscapeUtils"%>
<%
    //인코딩
    
	request.setCharacterEncoding("utf-8");

    //리포트 생성에 필요한 파라미터
	String crfName   = (request.getParameter("crfName")   == "" || request.getParameter("crfName")   == null ? "CLIP" : request.getParameter("crfName") );
	String crfParams = (request.getParameter("crfParams") == "" || request.getParameter("crfParams") == null ? "" : request.getParameter("crfParams") );
	String crfDbName = (request.getParameter("crfDbName") == "" || request.getParameter("crfDbName") == null ? "oracle1" : request.getParameter("crfDbName") );
	
    //oof document 생성, '자세한 사항은 클립리포트 4.0 OOF Generator' 메뉴얼 참고 !!
	OOFDocument oof = OOFDocument.newOOF();

	//리포트 파일 명
	OOFFile crfFile1 = oof.addFile("crf.root", "%root%/crf/" + crfName + ".crf");

    //커넥션의 네임스페이스, DataConnection.properties 설정된 dbname
	crfFile1.addConnectionData("*", crfDbName);
	
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
