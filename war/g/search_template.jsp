<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.appspot.cloudserviceapi.common.TimeUtil" %>

<%@ page import="org.compass.core.CompassQuery" %>
<%@ page import="org.compass.core.CompassQueryBuilder" %>
<%@ page import="org.compass.core.CompassQueryBuilder.CompassMultiPropertyQueryStringBuilder" %>
<%@ page import="org.compass.core.CompassSearchSession" %>
<%@ page import="org.compass.core.CompassHits" %>
<%@ page import="org.compass.core.Resource" %>
<%@ page import="com.appspot.cloudserviceapi.util.Compass" %>
<%@ page import="com.appspot.cloudserviceapi.dto.Geniu" %>
<%@ page import="com.appspot.cloudserviceapi.data.AppEngine" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.lucene.search.BooleanQuery" %>
<%@ page import="com.appspot.cloudserviceapi.common.Constants" %>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Google App Engine Search</title>
  </head>

  <body>
<p><a rel="external" href="<%= Constants.GENIUS_MAIN_URL %>/templatestart">Back</a></p>
<%
CompassSearchSession search = Compass.getCompass(com.appspot.cloudserviceapi.dto.Geniu.class).openSearchSession();
String searchParam = request.getParameter("q");
String query = searchParam;
if(query == null || query.equalsIgnoreCase(""))
query = "*";

if(query!=null){
		//=== begin - avoid "org.apache.lucene.search.BooleanQuery$TooManyClauses: maxClauseCount is set to 1024" error
		//=== source: http://stackoverflow.com/questions/1534789/help-needed-figuring-out-reason-for-maxclausecount-is-set-to-1024-error
		int defaultQueries = 1024, newQueries;
		newQueries = defaultQueries * 10;
        System.setProperty("org.apache.lucene.maxClauseCount", Integer.toString(newQueries));
        BooleanQuery.setMaxClauseCount(newQueries);
        System.out.println("BooleanQuery.setMaxClauseCount set to " + newQueries);
		//=== end - avoid "org.apache.lucene.search.BooleanQuery$TooManyClauses: maxClauseCount is set to 1024" error
		CompassQueryBuilder queryBuilder = search.queryBuilder();
		System.out.println("search_template.jsp: looking for '" + query + "' ..."); 
		CompassMultiPropertyQueryStringBuilder mpqsBuilder= queryBuilder.multiPropertyQueryString(query);
		mpqsBuilder.add("template.id");
		mpqsBuilder.add("template.platform");
		mpqsBuilder.add("template.what");
		mpqsBuilder.add("template.details");
		mpqsBuilder.add("template.similarity");
		CompassQuery qry=mpqsBuilder.toQuery();
		CompassHits hits = qry.hits();
%>
<p>Found <%= hits.length() %> hits on HQ time <%= TimeUtil.getHQDateTime(new Date()) %> for query <%= query %><p>
    <!-- 3 of 5 listview role -->
    <ul data-role="listview" data-filter="true" data-dividertheme="d" data-inset="true"><br><p> 
<%
	for(int i=0;i<hits.length();i++) {
		try {
		Geniu token = (Geniu)hits.data(i);
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
			<a href=�#� class=�ui-btn-left ui-btn-back� data-icon=�arrow-l�>Back</a>
			-->

			<a rel="external" href="<%= Constants.GENIUS_MAIN_URL %>/templatesave/<%= resource.getValue("id") %>"><%= resource.getValue("id") %></a>
			
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
			//ignore "java.lang.ClassCastException: com.appspot.cloudserviceapi.dto.Huma cannot be cast to com.appspot.cloudserviceapi.dto.Geniu"
			e.printStackTrace();
		}
	}
}
	search.close();
%>
	</ul>
  </body>
</html>
