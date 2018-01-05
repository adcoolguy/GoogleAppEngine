<%@ page import="com.appspot.cloudserviceapi.common.model.Constants" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<html>
	<%
	String dname = (String)request.getParameter("decorator");
	if(dname == null || "".equals(dname.trim())) {
		dname = (String)session.getAttribute("decorator");
	}
	String decoratorParam = "";
	if("mobile".equals(dname)) { 
		decoratorParam = "decorator=layout&confirm=true"; %>
	<% 
		session.setAttribute("decorator", "mobile");
		session.setAttribute("list-divider", "li");
		session.setAttribute("data-icon-back", "Back");
		session.setAttribute("data-icon-grid", "Desktop");
	} else
	if("layout".equals(dname)) 
	{ 
		decoratorParam = "decorator=mobile&confirm=true"; %>
	<%
		session.setAttribute("decorator", "layout");
		session.setAttribute("list-divider", "label");
		session.setAttribute("data-icon-back", "");
		session.setAttribute("data-icon-grid", "Mobile");
	} 
	session.setAttribute("decoratorParam", decoratorParam);
	%>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
<%
	String title = "";
	if(request.getRequestURL().indexOf("/sgc/") > -1) {
		title = "Soapy Green Cleaning";
	}
%>
    <title><%= title %></title>
  </head>
<%
String desktop = null;
if("layout".equals(request.getParameter("decorator"))) {
	desktop = "";
} else {
	desktop = (String)session.getAttribute("data-icon-grid");
}
%>
  <body>
<div data-role="page" data-theme="a">
	<div data-role="header" data-nobackbtn="true">
		<% if(desktop != null) { %>
		<a href="/sgc/jsp/main.jsp?decorator=layout&confirm=true" rel="external" data-icon="grid"><%= desktop %></a>
		<% } %>
		<h1>Main Page</h1>
	</div>
	<div data-role="content">
  		<% if(request.getRequestURL().indexOf("/sgc/jsp/main.jsp") == -1) { %>
		<jsp:include page="/sgc/jsp/header_login.jsp"/>
		<a href="/sgc/html/pc/index.html">Back to Home</a>
   		<div id="header">  
    		<h2><a href="/sgc/jsp/staff.jsp">Management</a> Console</h2>
   		</div>
   		<% } %>
<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    String userID = null;
    if (user == null) {
    	userID = "guest";
    } else {
    	userID = user.getNickname();
    }
%>
    <h2><%= Constants.GREETING_PRETEXT %> <%= userID %>!</h2>
	<% 
	if(!"mobile".equals(dname)) { 
		decoratorParam = "decorator=layout&confirm=true";
	} else {
		decoratorParam = "decorator=mobile&confirm=true";
	}
	String deco = decoratorParam;
	%>
	  <ul data-role="listview" data-inset="true" data-theme="a" data-dividertheme="b">
		<<%=session.getAttribute("list-divider")%> data-role="list-divider">Public</<%=session.getAttribute("list-divider")%>>
        <li><a href=/sgc/jsp/guestbook.jsp?<%=deco%> rel="external">Sign Guestbook</a></li>
		<<%=session.getAttribute("list-divider")%> data-role="list-divider">Client</<%=session.getAttribute("list-divider")%>>
        <li><a href="/sgc/request?<%=deco%>" rel="external">Request Cleaning</a></li>
        <li><a href="/sgc/report?<%=deco%>" rel="external">View My Report</a></li>
		<<%=session.getAttribute("list-divider")%> data-role="list-divider">Employee</<%=session.getAttribute("list-divider")%>>
        <li><a href="/sgc/work?<%=deco%>" rel="external">Close Request</a></li>
      </ul>
</div>
  </body>
</html>
