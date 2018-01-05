<%@ page import="com.appspot.cloudserviceapi.common.model.Constants" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
<%
	String title = "";
	if(request.getRequestURL().indexOf("/sci/") > -1) {
		title = "SCI";
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
<div data-role="page" data-theme="c">
	<div data-role="header" data-nobackbtn="true">
		<% 
		System.out.println("desktop = '" + desktop + "'");		
		if(desktop != null && desktop.trim().equals("Desktop")) { %>
		<a href="/sci/jsp/tv/main.jsp?decorator=layout&confirm=true" rel="external" data-icon="grid">Desktop</a>
		<% } else { %>
		<div align="left"><a href="/sci/jsp/tv/main.jsp?decorator=mobile&confirm=true" rel="external" data-icon="grid">Mobile</a></div>
		<% }
		//=== setting default UI
		if(desktop != null && desktop.trim().equals("")) {
			session.setAttribute("decorator", "layout");
			session.setAttribute("list-divider", "label");
			session.setAttribute("data-icon-back", "");
			session.setAttribute("data-icon-grid", "Mobile");
		}
		%>
		<h1>Main Page</h1>
	</div>
	<div data-role="content" data-theme="c">
  		<% if(request.getRequestURL().indexOf("/sci/jsp/tv/main.jsp") == -1) { %>
		<jsp:include page="/sci/jsp/tv/header_login.jsp"/>
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

	  <ul data-role="listview" data-inset="true" data-theme="c" data-dividertheme="b">
		<<%=session.getAttribute("list-divider")%> data-role="list-divider">Public</<%=session.getAttribute("list-divider")%>>
        <li><a href="/sci/jsp/tv/guestbook.jsp" rel="external">Sign Guestbook</a></li>
		<<%=session.getAttribute("list-divider")%> data-role="list-divider">Client</<%=session.getAttribute("list-divider")%>>
        <li><a href="/sci/jsp/tv/remote.jsp" rel="external">Remote</a></li>
      </ul>
</div>
  </body>
</html>
