<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.appspot.cloudserviceapi.common.model.TimeUtil" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<%@ page import="org.compass.core.CompassQuery" %>
<%@ page import="org.compass.core.CompassQueryBuilder" %>
<%@ page import="org.compass.core.CompassQueryBuilder.CompassMultiPropertyQueryStringBuilder" %>
<%@ page import="org.compass.core.CompassSearchSession" %>
<%@ page import="org.compass.core.CompassHits" %>
<%@ page import="org.compass.core.Resource" %>
<%@ page import="com.appspot.cloudserviceapi.util.Compass" %>
<%@ page import="com.appspot.cloudserviceapi.guestbook.Greeting" %>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Google App Engine Search</title>
  </head>

  <body>
<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    String userID = null;
    if (user == null) {
    	userID = "guest";
    } else {
    	userID = user.getNickname();	
    }

CompassSearchSession search = Compass.getCompass(com.appspot.cloudserviceapi.guestbook.Greeting.class).openSearchSession();
String searchParam = request.getParameter("q");
String query = searchParam;
if(query == null || query.equalsIgnoreCase(""))
query = "*";

if(query!=null){
		CompassQueryBuilder queryBuilder = search.queryBuilder();
		CompassMultiPropertyQueryStringBuilder mpqsBuilder= queryBuilder.multiPropertyQueryString(query);
		mpqsBuilder.add("greeting.content");
		mpqsBuilder.add("greeting.nickname");
		CompassQuery qry=mpqsBuilder.toQuery();
		CompassHits hits = qry.hits();
%>
<p>Found <%= hits.length() %> hits on HQ time <%= TimeUtil.getHQDateTime(new Date()) %> for query <%= query %><p>
<%
	for(int i=0;i<hits.length();i++) {
		Greeting greeting = (Greeting)hits.data(i);
		Resource resource = hits.resource(i);
		if(resource.getProperty("nickname") == null) {
%>
<p>An anonymous person wrote:</p>
<%
		} else {
%>
<p><b><%= resource.getValue("nickname") %></b> wrote:</p>
<%
		}
%>
<blockquote><%= resource.getValue("content") %></blockqoute>
<%
	}
}
	search.close();
%>
  </body>
</html>
