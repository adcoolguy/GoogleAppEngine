<%@page import="com.appspot.cloudserviceapi.common.model.Constants" %>
<%@page import="com.google.appengine.api.users.User" %>
<%@page import="com.google.appengine.api.users.UserService" %>
<%@page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@page import="com.appspot.cloudserviceapi.security.spring.model.UserSecurityDAO" %>
<%@page import="org.springframework.security.core.userdetails.UserDetails" %>
<%@page import="org.springframework.security.core.context.SecurityContextHolder" %>

<div id="pcheader">
<table width="100%">
<tr>
	<td width="68%">
<%
	String title = "";
	if(request.getRequestURL().indexOf("/sci/") > -1) {
		title = "Token Management";
	}
%>
		<h2><img src="http://scigrp.com/sites/default/files/scigrp_logo.gif" alt="SCIGRP LOGO"> <%= title %></h2>
<%
System.out.println("referer " + request.getSession().getAttribute("referer"));
	if(request.getSession().getAttribute("referer") != null && ((String)request.getSession().getAttribute("referer")).indexOf("goo.gl") == -1) {
%>
	This portal can be reached via <a href="http://goo.gl/yCS0S">http://goo.gl/yCS0S</a> as well.
<%
	}
%>

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
	<a href="/j_spring_security_logout" target="_top">Log Out <%= ssUserID %></a>&nbsp;
<%
} else {
%>
	<!--<a href="/login.jsp" target="_top">Log In Portal</a>&nbsp;-->
<%
}
%>   

<% if(user != null && user.getNickname() != null && !"".equals(user.getNickname().trim())) {
	request.getSession().setAttribute("email", user.getEmail());
%>
	<a href="<%= userService.createLogoutURL("/sci/tokenregistrationstart") %>" target="_top">Log Out Google (<%= user.getNickname() %>)</a>
<% } else { %>
	<a href="<%= userService.createLoginURL("/sci/tokenregistrationstart") %>" target="_top">Log In Google</a>
<% } %>

	</td>
</tr>
</table>
</div>