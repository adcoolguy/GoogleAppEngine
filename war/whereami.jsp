<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.appspot.cloudserviceapi.common.model.Constants" %>

<html>
<head>
	<title>GAEJ</title>
</head>

<body>
<div data-role="page" data-theme="c">
	<div data-role="header" data-nobackbtn="true">
	</div>
	<div data-role="content" data-theme="c">
	<h1>Location</h1>
<%
	String value = request.getHeader("X-AppEngine-country");
    if (value == null) {
        // The request header was not present
%>
Sorry, I could not figure out your location :(
<%        
    } else {
%>
You are from <%= value %> correct?
<% 
	} 
%>
	</div>
	<div data-role="footer">
	</div>
</body>

</html>