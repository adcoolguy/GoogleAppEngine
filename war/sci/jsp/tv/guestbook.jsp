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
<% if(session.getAttribute("data-icon-back") != null) { %>
		<a href="<%= Constants.CORP1_MAIN_URL %>" data-icon="back"><%=session.getAttribute("data-icon-back")%></a>
<% } %>
		<h1>Guestbook</h1>
	</div>
	<div data-role="content" data-theme="c">
<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
%>
<%= Constants.GREETING_PRETEXT %> <%= user.getNickname() %>! (You can
<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>" rel="external">log out</a>.)</p>

<%
    } else {
%>
<p><%= Constants.GREETING_PRETEXT %>!
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>" rel="external">Log in</a>
to include your name with greetings you post.</p>
<%
    }
%>

<%
    PersistenceManager pm = Persistence.getManager();
    String query = "select from " + Greeting.class.getName();
    List<Greeting> greetings = (List<Greeting>) pm.newQuery(query).execute();
    if (greetings.isEmpty()) {
%>
<p>The guestbook has no messages.</p>
<%
    } else {
        for (Greeting g : greetings) {
            if (g.getAuthor() == null) {
%>
<p>An anonymous person wrote:</p>
<%
            } else {
%>
<p><b><%= g.getAuthor().getNickname() %></b> wrote:</p>
<%
            }
%>
<blockquote><%= g.getContent() %></blockquote>
<%
        }
    }
    pm.close();
%>

	    <form action="/sci/sign" method="post">
		<div data-role="fieldcontain"> 
			<label for="textarea"></label>
			<textarea cols="50%" rows="3" name="content" id="content"></textarea>
		</div>      	
    	
		<button type="submit" data-theme="a">Post Gomments</button>
    	
    	</form>
	</div>
</div>

  </body>
</html>