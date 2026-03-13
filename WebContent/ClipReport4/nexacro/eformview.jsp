<!doctype html>
<%
	response.setHeader("Access-Control-Allow-Origin", request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort());
	String contextPath = "/".equals(request.getContextPath()) ? "" : request.getContextPath();
%>
<html>
	<head>
		<title>Insert title here</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<link rel="stylesheet" type="text/css" href="<%= contextPath %>/ClipReport4/css/clipreport.css">
		<link rel="stylesheet" type="text/css" href="<%= contextPath %>/ClipReport4/css/eform.css">
		<link rel="stylesheet" type="text/css" href="<%= contextPath %>/ClipReport4/css/UserConfig.css">
		<link rel="stylesheet" type="text/css" href="<%= contextPath %>/ClipReport4/css/font.css">
		<script type='text/javascript' src='<%= contextPath %>/ClipReport4/js/jquery-1.11.1.js'></script>
		<script type='text/javascript' src='<%= contextPath %>/ClipReport4/js/clipreport.js'></script>
		<script type='text/javascript' src='<%= contextPath %>/ClipReport4/js/UserConfig.js'></script>
		<script src="//code.jquery.com/jquery-3.2.1.min.js"></script>
		<script type='text/javascript'>
			window.onload = function(){
			}
			
			function view() {
				var report = "";
				var urlPath = document.location.protocol + "//" + document.location.host;
					
			    //리포트 생성에 필요한 데이터
				var callType   = document.getElementById("callType").value;		//COnenction Type
				var crfName    = document.getElementById("crfName").value;		//리포트 이름
				var crfParams  = document.getElementById("crfParams").value; 	//리포트 파라미터, DEPARTMENT_ID:100|FIRST_NAME:a|TEST:한글테스트
				var crfDbName  = document.getElementById("crfDbName").value; 	//DB Connection 사용, dbname - oracle1
				var crfUrlPath = document.getElementById("crfUrlPath").value; 	//url Connection 사용- http://domain/data.jsp
				var crfData    = document.getElementById("crfData").value; 		//memo Connection 사용
				//alert("crfParams >" + crfParams);
				
			    //커넥션 타입별로 리포트 실행 준비
				if( callType == "db") {
					eform = createJSPEForm(urlPath + "<%= contextPath %>/ClipReport4/Clip.jsp", urlPath + "<%= contextPath %>/ClipReport4/nexacro/reportCreateDB.jsp",   "crfName=" + crfName + "&crfParams=" + crfParams + "&crfDbName=" + crfDbName ,document.getElementById("targetDiv1"));	
				}else if( callType == "memo") {
				 	//alert("crfData:" + crfData);
				 	eform = createJSPEForm(urlPath + "<%= contextPath %>/ClipReport4/Clip.jsp", urlPath + "<%= contextPath %>/ClipReport4/nexacro/eformCreateMEMO.jsp", "crfName=" + crfName + "&crfParams=" + crfParams + "&crfData=" + crfData ,document.getElementById("targetDiv1"));	
				 }else if( callType == "http") {
					 eform = createJSPEForm(urlPath + "<%= contextPath %>/ClipReport4/Clip.jsp", urlPath + "<%= contextPath %>/ClipReport4/nexacro/reportCreateHTTP.jsp", "crfName=" + crfName + "&crfParams=" + crfParams + "&crfUrlPath" + crfUrlPath, document.getElementById("targetDiv1"));
				 }
				eform.setSlidePage(true);    //리포트 뷰어의 옵션
				eform.view();     //리포트 뷰어 실행
				
				
				eform.setEndReportEvent(function(){
					var btn = eform.findControl("Button1");
					
					btn.onClickEvent(function(eventTarget){
						//---------- key로 추출하는 방법
						//console.log(eformkey);
						
						//---------- 사인 컨트롤 값 추출
						var sign1 = eform.findControl("clipsign1");
						var sign2 = eform.findControl("clipsign2");
				
						console.log(sign1.getValue());
						//Base64
						alert(sign1.getValue());
						alert(sign2.getValue());
						
						//Path
						//alert(sign1.getPath());
						//alert(sign2.getPath());
						//console.log(eformkey);;
					});
				});
				
				
				
				//eform의 저장버튼을 눌렀을때 이벤트 발생
			 	eform.setEndSaveButtonEvent(function(){
					//---------- key로 추출하는 방법
					//console.log(eformkey);
					
					//---------- 사인 컨트롤 값 추출
					var sign1 = eform.findControl("clipsign1");
					var sign2 = eform.findControl("clipsign2");
					
					$.ajax({
						method: "POST",
		    			url: "<%= contextPath %>/SIGN_01.do",
		    			cache: false,
		    			dataType: "json",
		    			headers: { "KEDI-PID": "eformview" },
		    			data: JSON.stringify({ "sign1": sign1.getValue(), "sign2": sign2.getValue() }),
		    			contentType: "application/json;charset=UTF-8",
		    			success: function(data) {
		    				if (data.result == "S") {
		    					alert("저장이 완료되었습니다.");
		    					sign2.setValue(data.sign1);
		    					sign1.setValue(data.sign2);
		    				}
		    			}, error : function(xhr, text) {
		    				alert("error " + xhr + " " + text);
		    			}
		    		});
					//console.log(sign1.getValue());
					//alert(sign1.getValue());
					//alert(sign2.getValue());
					//console.log(eformkey);
				}); 
			}
		</script>
	</head>
<body>
<input type="text"   id="callType"    value="db" />
<input type="text"   id="crfName"     value="CLIP" />
<input type="text"   id="crfParams"   value="crfParams" />
<input type="text"   id="crfDbName"   value="oracle1" />
<input type="text"   id="crfUrlPath"   value="crfUrlPath" />
<textarea type="text" id="crfData" ></textarea>
<button id="btnClick" onclick="view();"></button>
<div id='targetDiv1' style='position:absolute;left:0px;top:0px;height:100%;width:100%' ></div>
</body>
</html>
