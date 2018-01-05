<%@page import="com.appspot.cloudserviceapi.common.model.Constants" %>

<body style="overflow-x: hidden;">
<jsp:include page="header_login.jsp"/>
<table id="content-box">
<tr>&nbsp;
</tr>
<tr>
	<td>
	<%
	String dname = (String)request.getParameter("decorator");
	if(dname == null || "".equals(dname.trim())) {
		dname = (String)session.getAttribute("decorator");
	}
	String decoratorParam = "";
	if("mobile".equals(dname)) { 
		decoratorParam = "decorator=layout&confirm=true"; %>
		<a href="<%=request.getRequestURL()%>?<%=decoratorParam%>" data-theme="b">Desktop</a>
	<% 
		session.setAttribute("decorator", "mobile");
		session.setAttribute("list-divider", "li");
		session.setAttribute("data-icon-back", "Back");
		session.setAttribute("data-icon-grid", "Desktop");
	} else {
		decoratorParam = "decorator=mobile&confirm=true"; %>
		<a href="<%=request.getRequestURL()%>?<%=decoratorParam%>">Mobile</a>
	<%
		session.setAttribute("decorator", "layout");
		session.setAttribute("list-divider", "label");
		session.setAttribute("data-icon-back", "");
		session.setAttribute("data-icon-grid", "");
	} %>
	</td>
	<%
	if(request.getRequestURL().indexOf("/sgc/") > -1) {
	%>
	<td>&nbsp;&nbsp;&nbsp;<a href="/sgc/html/pc/index.html">Back to Home</a>&nbsp;&nbsp;&nbsp;<a href="<%= Constants.MAIN_URL %>">Back to Main</a>
	</td>
	<%
	}
	%>
</tr>
</table>
</body>