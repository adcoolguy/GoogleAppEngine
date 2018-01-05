<%@page import="com.appspot.cloudserviceapi.common.model.Constants" %>

<jsp:include page="header_login.jsp"/>
<table id="content-box">
<tr>
	<%
	if(request.getRequestURL().indexOf("/sci/") > -1) {
	%>
	<td><a href="http://scigrp.com/" target="_top">Back to SCIGRP</a>&nbsp;&nbsp;&nbsp;<a href="/sci/tokenregistrationstart" target="_top">Back to Home</a>&nbsp;&nbsp;&nbsp;<a href="/sci/admintokenstart" target="_top">Management Console</a>
	</td>
	<%
	}
	%>
</tr>
</table>
