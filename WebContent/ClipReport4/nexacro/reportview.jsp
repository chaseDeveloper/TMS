<!doctype html><%
	response.setHeader("Access-Control-Allow-Origin", request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort());
	String contextPath = "/".equals(request.getContextPath()) ? "" : request.getContextPath();
%><html>
	<head>
		<title>Insert title here</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<link rel="stylesheet" type="text/css" href="<%= contextPath %>/ClipReport4/css/clipreport.css">
		<link rel="stylesheet" type="text/css" href="<%= contextPath %>/ClipReport4/css/UserConfig.css">
		<link rel="stylesheet" type="text/css" href="<%= contextPath %>/ClipReport4/css/font.css">
		<script type="text/javascript" src="<%= contextPath %>/ClipReport4/js/jquery-1.11.1.js"></script>
		<script type="text/javascript" src="<%= contextPath %>/ClipReport4/js/clipreport.js"></script>
		<script type="text/javascript" src="<%= contextPath %>/ClipReport4/js/UserConfig.js"></script>
		<script type="text/javascript">
			window.onload = function() {}
			
			function view() {
				//추가---------------------------
				//------------------------------
				var report  = "";
				var urlPath = document.location.protocol + "//" + document.location.host;
					
			    //리포트 생성에 필요한 데이터
				var callType   = document.getElementById("callType").value;		//COnenction Type
				var crfName    = document.getElementById("crfName").value;		//리포트 이름
				var crfParams  = document.getElementById("crfParams").value; 	//리포트 파라미터, DEPARTMENT_ID:100|FIRST_NAME:a|TEST:한글테스트
				var crfDbName  = document.getElementById("crfDbName").value; 	//DB Connection 사용, dbname - oracle1
				var crfUrlPath = document.getElementById("crfUrlPath").value; 	//url Connection 사용- http://domain/data.jsp
				var crfData    = document.getElementById("crfData").value; 		//memo Connection 사용
				var crfFile    = document.getElementById("crfFile").value; 		//memo Connection 사용
				
				   //커넥션 타입별로 리포트 실행 준비
				if (callType == "db") {
				 	report = createJSPReport(urlPath + "<%= contextPath %>/ClipReport4/Clip.jsp", urlPath + "<%= contextPath %>/ClipReport4/nexacro/reportCreateDB.jsp", "crfName=" + crfName + "&crfParams=" + crfParams + "&crfDbName=" + crfDbName, document.getElementById("targetDiv1"));	
				} else if (callType == "memo") {
				 	//alert("crfData:" + crfData);
				 	report = createJSPReport(urlPath + "<%= contextPath %>/ClipReport4/Clip.jsp", urlPath + "<%= contextPath %>/ClipReport4/nexacro/reportCreateMEMO.jsp", "crfName=" + crfName + "&crfParams=" + crfParams + "&crfData=" + encodeURIComponent(crfData), document.getElementById("targetDiv1"));	
				} else if (callType == "http") {
					report = createJSPReport(urlPath + "<%= contextPath %>/ClipReport4/Clip.jsp", urlPath + "<%= contextPath %>/ClipReport4/nexacro/reportCreateHTTP.jsp", "crfName=" + crfName + "&crfParams=" + crfParams + "&crfUrlPath=" + crfUrlPath, document.getElementById("targetDiv1"));
				} else if (callType == "file") {
					report = createJSPReport(urlPath + "<%= contextPath %>/ClipReport4/Clip.jsp", urlPath + "<%= contextPath %>/ClipReport4/nexacro/reportCreateFILE.jsp", "crfName=" + crfName + "&crfParams=" + crfParams + "&crfFile=" + encodeURIComponent(crfFile), document.getElementById("targetDiv1"));
				}
				
				report.setSlidePage(true);    //리포트 뷰어의 옵션
			    report.setDebug(true);
				report.view();     //리포트 뷰어 실행
			}
		</script>
	</head>
	<body>
		<input type="text" id="callType"   value="db" />
		<input type="text" id="crfName"    value="CLIP" />
		<input type="text" id="crfParams"  value="crfParams" />
		<input type="text" id="crfDbName"  value="oracle1" />
		<input type="text" id="crfUrlPath" value="crfUrlPath" />
		<textarea id="crfData"></textarea>
		<input type="text" id="crfFile"    value="crfFile" />
		<button id="btnClick" onclick="view();"></button>
		<div id="targetDiv1" style="position:absolute;left:0px;top:0px;height:100%;width:100%"></div>
	</body>
</html>
