<%@page import="com.appspot.cloudserviceapi.common.model.Constants" %>

<body style="overflow-x: hidden;">
<jsp:include page="header_login.jsp"/>
<table id="content-box">
<tr>&nbsp;
</tr>
<tr>

	<%
	if(request.getRequestURL().indexOf("/sci/") > -1) {
	%>
	<td>&nbsp;&nbsp;&nbsp;<a href="/sci/html/pc/index.html">Back to Home</a>&nbsp;&nbsp;&nbsp;<a href="<%= Constants.MAIN_URL %>">Back to Main</a>
	</td>
	<%
	}
	%>
</tr>
</table>
</body>