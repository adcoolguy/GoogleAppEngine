<%@page import="com.appspot.cloudserviceapi.common.model.Constants" %>
<%@page import="com.google.appengine.api.users.User" %>
<%@page import="com.google.appengine.api.users.UserService" %>
<%@page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@page import="com.appspot.cloudserviceapi.security.spring.UserSecurityDAO" %>
<%@page import="org.springframework.security.core.userdetails.UserDetails" %>
<%@page import="org.springframework.security.core.context.SecurityContextHolder" %>

<!--
<link rel="stylesheet" href="/sgc/html/pc/css/style.css" type="text/css" media="screen, projection, tv" />
-->

<div id="pcheader">
<table width="100%">
<tr>
	<td width="68%">
<%
	String title = "";
	if(request.getRequestURL().indexOf("/sgc/") > -1) {
		title = "Soapy Green Cleaning";
	}
%>
		<h2><%= title %></h2>
	</td>
	<td>
<%
String finalUserID = "";
String ssUserID = "";
//=== Spring security
if(SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
	ssUserID = ((UserDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername();
	ssUserID = ssUserID + " (" + (new UserSecurityDAO()).getRole(ssUserID) + ")";
	System.out.println("Spring security user id = '" + ssUserID + "'");
}
//=== Google
UserService userService = UserServiceFactory.getUserService();
User user = userService.getCurrentUser();
if(user != null) {
	if(user.getNickname() != null && !"".equals(user.getNickname().trim())) {
		if(!"".equals(ssUserID)) {
			finalUserID = ssUserID + " and " + user.getNickname();
		} else {
			finalUserID	= user.getNickname();
		}
	} else {
		finalUserID = Constants.NOT_LOGGED_IN_TITLE;
	}
} else {
	if(!"".equals(ssUserID)) {
		finalUserID = ssUserID;
	}
}

if(!"".equals(ssUserID) || user != null) {
%>
Logged in as <%= finalUserID %><p>
<%
}
if(!"".equals(ssUserID)) {
%>
	<a href="/j_spring_security_logout">Log Out <%= ssUserID %></a>&nbsp;
<%
} else {
%>
	<a href="/login.jsp">Log In Portal</a>&nbsp;
<%
}
%>   

<% if(user != null && user.getNickname() != null && !"".equals(user.getNickname().trim())) { %>
	<a href="<%= userService.createLogoutURL("/sgc/jsp/main.jsp") %>">Log Out Google (<%= user.getNickname() %>)</a>
<% } else { %>
	<a href="<%= userService.createLoginURL("/sgc/jsp/main.jsp") %>">Log In Google</a>
<% } %>


	<form action="/sgc/jsp/search_user.jsp">
		<!--<img src="http://cdn3.infoq.com/styles/i/btn-search.gif" alt="Search"/>--> Search <input type="text" name="q" id="q" value="" size="30">
	</form>
	</td>
</tr>
</table>
</div>