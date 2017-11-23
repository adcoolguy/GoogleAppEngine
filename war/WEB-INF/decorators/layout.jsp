<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<html>
	<head>  
  		<title>  
   			:<decorator:title default="General" />:
  		</title>
  		<decorator:head />
 	</head>
  	<body style="overflow-x: hidden;">
  		<div>
   			<div>
   				<% if(request.getRequestURL().indexOf("/sgc/") > -1) { %>
    			<label><a href="/sgc/jsp/staff.jsp">Management Console</a></label>
				<% } %>
   			</div>
   				<% if(request.getRequestURL().indexOf("/sgc/") > -1) { %>
			<jsp:include page="/sgc/jsp/header.jsp"/>
				<% } %>
   			<decorator:body />  
   		</div>
  	</body>
</html>
