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
<%@ page import="tapp.model.ServiceRegistry" %>
<%@ page import="com.appspot.cloudserviceapi.data.AppEngine" %>
<%@ page import="com.opensymphony.util.TextUtils" %>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Google App Engine Search</title>
  </head>

  <body>
<a href="javascript:history.go(-1)">Back</a></p>  
<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    String userID = null;
    if (user == null) {
    	userID = "guest";
    } else {
    	userID = user.getNickname();	
    }

CompassSearchSession search = Compass.getCompass(tapp.model.ServiceRegistry.class).openSearchSession();
String searchParam = request.getParameter("q");
String query = searchParam;
if(query == null || query.equalsIgnoreCase(""))
query = "*";

if(query!=null){
		CompassQueryBuilder queryBuilder = search.queryBuilder();
		CompassMultiPropertyQueryStringBuilder mpqsBuilder= queryBuilder.multiPropertyQueryString(query);
		mpqsBuilder.add("scireg.id");
		mpqsBuilder.add("scireg.service");
		mpqsBuilder.add("scireg.owner");
		mpqsBuilder.add("scireg.project");
		mpqsBuilder.add("scireg.organization");
		mpqsBuilder.add("scireg.endpoint");
		mpqsBuilder.add("scireg.summary");
		mpqsBuilder.add("scireg.description");	//first 500 characters only due to JDO limitations
		mpqsBuilder.add("scireg.lastUpdated");
		mpqsBuilder.add("scireg.shortUrl");
		CompassQuery qry=mpqsBuilder.toQuery();
		CompassHits hits = qry.hits();
%>
<p>Found <%= hits.length() %> hits on HQ time <%= TimeUtil.getHQDateTime(new Date()) %> for query <%= query %><p>
<%
	for(int i=0;i<hits.length();i++) {
		ServiceRegistry token = (ServiceRegistry)hits.data(i);
		Resource resource = hits.resource(i);
		if(resource.getProperty("id") == null) {
%>
<p>Null id with details:</p>
<%
		} else {
%>
<p><b>Id <%= resource.getValue("id") %></b> details:</p>
<%
		}
%>
<blockquote><a href="http://<%= AppEngine.getHostName() %>/sci/serviceregistrysave/<%= resource.getValue("id") %>"><%= resource.getValue("id") %></a>: <%= resource.getValue("service") %>, <%= resource.getValue("description") %>, <%= TextUtils.hyperlink(resource.getValue("endpoint")) %>, <%= resource.getValue("hit") %></blockqoute>
<%
	}
}
	search.close();
%>
  </body>
</html>
