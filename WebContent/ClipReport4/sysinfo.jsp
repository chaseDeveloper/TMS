<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*,java.text.*,java.net.InetAddress,java.text.SimpleDateFormat" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>CLIP report Server - System Information</title>

<style>
	table {
	    border-collapse: collapse;
	    width: 60%;
	}

	th, td {
    	border-bottom: 1px solid #ddd;
    	text-align: left;
	    padding: 8px;
	}
	
	th {
		background-color: #005ECC;
	    color: white;
    }
    
    .scoll-body {
    	width: 60%; height:300px; overflow-y:auto; overflow-x: auto;
    }
</style>
</head>
<body>
	<div>
		<div>
			<h4>1. Server Information</h4>
			<div>
				<p>- Date : <%= new SimpleDateFormat("yyyy-MM-dd").format(new Date()) %>, <%= new Date().toString() %> </p>  
			</div>
			<div>
				<p>- CPU Available Processors : <%= Runtime.getRuntime().availableProcessors() %> </p>
			</div>
			<div>
				<% try { %>
						<p>- IP Address : <%= InetAddress.getLocalHost().getHostAddress() %> </p>								
				<% } catch (Exception e) {  %>
						<% 
							response.getWriter().append("<p>Failed to get host address<p>");
							e.printStackTrace(response.getWriter());
						%>							
				<% } %>
				
				<% try { %>
						<% 
							InetAddress localhost = InetAddress.getLocalHost();
						%>
						<p>CanonicalHostName : <%=localhost.getCanonicalHostName()%></p>
						<% InetAddress.getAllByName(localhost.getCanonicalHostName()); %>
				<% } catch (Exception e) { %>
						<% 
							response.getWriter().append("<p>Failed host address check<p>");
							e.printStackTrace(response.getWriter());
						%>	
				<% } %>
			</div>
			<div>
				<p>- Remote Address : <%= request.getRemoteAddr() %> </p>
			</div>
			<div>
				<p>- Server : <%= request.getSession().getServletContext().getServerInfo() %></p>
			</div>
			<div>
				<p>- Server RealPath : <%= request.getRealPath("/") %> </p>
			</div>
			<div>
				<p>- Servlet Version : <%= application.getMajorVersion()%>.<%= application.getMinorVersion() %></p>
			</div>
			<div>
				<p>- Session MaxInactiveInterval : <%= request.getSession().getMaxInactiveInterval() %> </p>
			</div>
		</div>
		<div>
			<h4>2. System Environment</h4>
		</div>
		<table>
			<thead>
				<tr>
					<th width="5%" style="text-align:center">#</th>
					<th width="20%" style="text-align:center">Key</th>
					<th width="75%" style="text-align:center">Value</th>
				</tr>
			</thead>
		</table>
	</div> 
	<div class="scoll-body">
		<table>
			<tbody>
				<% Map<String, String> env = System.getenv(); Iterator<String> it = env.keySet().iterator(); int environmentIndex = 1; while (it.hasNext()) { String key = it.next();%>
				<tr>
					<td width="5%" style="text-align:center"><%= environmentIndex++ %></td>
					<td width="20%" ><%= key %></td>
					<td width="75%" ><%= env.get(key) %></td>
				</tr>
				<% } %>
			</tbody>
		</table>
	</div>
	
	<div>
		<div>
			<h4>3. System Properties</h4>
		</div>
		<table>
			<thead>
				<tr>
					<th width="5%" style="text-align:center">#</th>
					<th width="20%" style="text-align:center">Key</th>
					<th width="75%" style="text-align:center">Value</th>
				</tr>
			</thead>
		</table>
	</div> 
	<div class="scoll-body">
		<table>
			<tbody>
				<% Enumeration<Object> properties = System.getProperties().keys(); 	int propertyIndex = 1; while (properties.hasMoreElements()) { %>
				<tr>
					<% String key = (String) properties.nextElement(); %>
					<td width="5%" style="text-align:center"><%= propertyIndex++ %></td>
					<td width="20%" ><%= key %></td>
					<td width="75%" ><%= System.getProperty(key) %></td>
				</tr>
				<% } %>
			</tbody>
		</table>
	</div>
</body>
</html>