<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="app.common.Constants" %>
<%@ page import="app.model.User" %>
<%@ page import="cloudserviceapi.app.controller.UserHandler" %>
<%@ page import="com.appspot.cloudserviceapi.common.JsonUtil" %>
<%@ page import="com.appspot.cloudserviceapi.security.spring.model.*" %>
<%@ page import="org.springframework.security.crypto.bcrypt.BCrypt" %>
<%@ page import="java.security.Principal" %>
<%@ page import="com.appspot.cloudserviceapi.security.spring.model.*" %>
<%@ page import="com.appspot.cloudserviceapi.security.spring.*" %>
<%@ page import="org.springframework.security.core.context.*" %>
<%@ page import="org.springframework.security.core.*" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<html>
    <title>Current User</title>
<%
SecurityContextImpl sci = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
//out.println("sci = [" + sci + "]");
GaeUserDetails principal = null;

if (sci != null) {
        principal = (GaeUserDetails) sci.getAuthentication().getPrincipal();
        // do whatever you need here with the UserDetails
}
if(principal != null) {
    out.println("{userid: \"" + principal.getUserId() + "\", role: \"" + principal.getUserRole() + "\"}");
} else {
    out.println("");
}
%>
<!--     <security:authorize access="isAuthenticated()">
        authenticated as <security:authentication property="principal.username" /> 
    </security:authorize> -->
</html>