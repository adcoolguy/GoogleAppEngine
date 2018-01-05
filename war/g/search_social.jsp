<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.appspot.cloudserviceapi.common.model.TimeUtil" %>

<%@ page import="org.compass.core.CompassQuery" %>
<%@ page import="org.compass.core.CompassQueryBuilder" %>
<%@ page import="org.compass.core.CompassQueryBuilder.CompassMultiPropertyQueryStringBuilder" %>
<%@ page import="org.compass.core.CompassSearchSession" %>
<%@ page import="org.compass.core.CompassHits" %>
<%@ page import="org.compass.core.Resource" %>
<%@ page import="com.appspot.cloudserviceapi.util.Compass" %>
<%@ page import="com.appspot.cloudserviceapi.dto.Huma" %>
<%@ page import="com.appspot.cloudserviceapi.data.AppEngine" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="com.appspot.cloudserviceapi.common.model.Constants" %>

<html>
  <head>
	<meta name="viewport" content="initial-scale=1.07, user-scalable=no"/> <!-- source: https://developer.apple.com/library/ios/DOCUMENTATION/AppleApplications/Reference/SafariWebContent/UsingtheViewport/UsingtheViewport.html -->
    <title>Google App Engine Search</title>
  </head>

  <body>
<p><a rel="external" href="<%= Constants.SOCIAL_MAIN_URL %>/socialstart">Back</a></p>
<%
CompassSearchSession search = Compass.getCompass(com.appspot.cloudserviceapi.dto.Huma.class).openSearchSession();
String searchParam = request.getParameter("q");
String query = searchParam;
if(query == null || query.equalsIgnoreCase(""))
query = "*";

if(query!=null){
		CompassQueryBuilder queryBuilder = search.queryBuilder();
		System.out.println("search_social.jsp: looking for '" + query + "' ..."); 
		CompassMultiPropertyQueryStringBuilder mpqsBuilder= queryBuilder.multiPropertyQueryString(query);
		mpqsBuilder.add("social.id");
		mpqsBuilder.add("social.platform");
		mpqsBuilder.add("social.what");
		mpqsBuilder.add("social.details");
		mpqsBuilder.add("social.similarity");
		CompassQuery qry=mpqsBuilder.toQuery();
		CompassHits hits = qry.hits();
%>
<p>Found <%= hits.length() %> hits on HQ time <%= TimeUtil.getHQDateTime(new Date()) %> for query <%= query %><p>
    <!-- 3 of 5 listview role -->
    <ul data-role="listview" data-filter="true" data-dividertheme="d" data-inset="true"><br><p> 
<%
	for(int i=0;i<hits.length();i++) {
		try {
		Huma token = (Huma)hits.data(i);
		Resource resource = hits.resource(i);
		if(resource.getProperty("id") == null) {
%>
<p>Null id with details:</p>
<%
		} else {
%>
<!-- 4 of 5 the list item -->
<li>
	<h3>[<%= resource.getValue("what") %>] *<%= resource.getValue("id") %>*</h3>
	Id <%= resource.getValue("id") %></b> details:</p>
	<!-- 5 of 5 add the label, nested ul and the external href etc -->
<!--	<label><%= StringUtils.abbreviate(resource.getValue("details"), 80) %>, <%= resource.getValue("similarity") %></label>  -->
	<label style="white-space:normal"><%= resource.getValue("details") %>, <%= resource.getValue("similarity") %></label>
	<ul>
		<li>
			<!--
			<a href=Õ#Õ class=Õui-btn-left ui-btn-backÕ data-icon=Õarrow-lÕ>Back</a>
			-->
			
			<a rel="external" href="<%= Constants.SOCIAL_MAIN_URL %>/socialsave/<%= resource.getValue("id") %>"><%= resource.getValue("id") %></a>
			
			<div data-role="fieldcontain">
				&nbsp;&nbsp;&nbsp;<label for="textarea">Details:</label>
				<textarea style="width:60%;height:15%;" READONLY cols="50" rows="15" name="textarea" id="textarea"><%= resource.getValue("details") %></textarea>
			</div>
		</li>
	</ul>
</li>
<%
		}
		} catch(ClassCastException e) {
			//ignore "java.lang.ClassCastException: com.appspot.cloudserviceapi.dto.Geniu cannot be cast to com.appspot.cloudserviceapi.dto.Huma"
			e.printStackTrace();
		}
	}
}
	search.close();
%>
	</ul>
  </body>
</html>
