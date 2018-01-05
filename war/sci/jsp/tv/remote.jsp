<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.appspot.cloudserviceapi.guestbook.Greeting" %>
<%@ page import="com.appspot.cloudserviceapi.data.Persistence" %>
<%@ page import="com.appspot.cloudserviceapi.common.model.Constants" %>

<html>
  <body>
<%  
System.out.println("request.getParameter(\"decorator\") = '" + request.getParameter("decorator") + "'");		
if("layout".equals(request.getParameter("decorator")) || request.getParameter("decorator") == null) { %>
		<a href="<%= Constants.CORP1_MAIN_URL %>">Back</a>
<% } %>
<div data-role="page" data-theme="c">
	<div data-role="header" data-nobackbtn="true" data-position="inline">
		<a href="<%= Constants.CORP1_MAIN_URL %>" data-icon="back"><%=session.getAttribute("data-icon-back")%></a>
		<h1>Guestbook</h1>
	</div>
	<div data-role="content" data-theme="c">
<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
//    if (user != null) {
%>

	<div data-role="controlgroup" data-type="horizontal">
<div data-role="button">
<a href="index.html" data-role="button">&nbsp;Vol +</a>
<a href="index.html" data-role="button">&nbsp;&nbsp;</a>
<a href="index.html" data-role="button">Chl +</a>
</div>
	</div>

	<div data-role="controlgroup" data-type="horizontal">
<div data-role="button">
<a href="index.html" data-role="button">&nbsp;&nbsp;Vol -</a>
<a href="index.html" data-role="button">&nbsp;^</a>
<a href="index.html" data-role="button">Chl -</a>
</div>
	</div>

	<div data-role="controlgroup" data-type="horizontal">
<div data-role="button">
<a href="index.html" data-role="button">&nbsp;&nbsp;&nbsp;&nbsp;<</a>
<a href="index.html" data-role="button">&nbsp;&nbsp;OK&nbsp;&nbsp;</a>
<a href="index.html" data-role="button">&nbsp;&nbsp;&nbsp;>&nbsp;</a>
</div>
	</div>

	<div data-role="controlgroup" data-type="horizontal">
<div data-role="button">
<a href="index.html" data-role="button">Guide</a>
<a href="index.html" data-role="button">v&nbsp;</a>
<a href="index.html" data-role="button">Last</a>
</div>
	</div>

	<div data-role="controlgroup" data-type="horizontal">
<div data-role="button">
<a href="index.html" data-role="button">M</a>
<a href="index.html" data-role="button">I</a>
<a href="index.html" data-role="button">O</a>
<a href="index.html" data-role="button">E</a>
</div>
	</div>

<%
//    }
%>
	</div>
</div>

  </body>
</html>