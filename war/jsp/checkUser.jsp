<%@ page import="app.common.Constants" %>
<%@ page import="app.model.User" %>
<%@ page import="cloudserviceapi.app.controller.UserHandler" %>
<%@ page import="com.appspot.cloudserviceapi.common.model.JsonUtil" %>
<%@ page import="com.appspot.cloudserviceapi.security.spring.model.*" %>
<%@ page import="org.springframework.security.crypto.bcrypt.BCrypt" %>
<html>
    <title>User Verification</title>
<%
String userId = (String)request.getParameter("user");
String password = (String)request.getParameter("pwd");
if(userId != null && password != null) {
	GaeUserDetailsService item = null;
    GaeUserDetails user = null;
    String storedPassword = null;
    String hashedPassword = null;
    UserSecurityDAO dao = null;
    try {
    	
        item = new GaeUserDetailsService();
        dao = new UserSecurityDAO();
        item.setUserSecurityDAO(dao);
		//System.out.println("loading user by id [" + userId + "]...");
		try {
        	user = item.loadUserByUsername(userId);
		} catch(Exception e) {
			e.printStackTrace();
		}
		//System.out.println("user [" + user + "]...");
        if(user != null) {
			storedPassword = user.getPassword();
			if(BCrypt.checkpw(password, storedPassword)) {
%>
	    		OK
<%
			} else {
%>
				NOT OK
<%
			}
        }
    } catch (Exception e) {
%>
    Error: <%= e.getMessage() %>
<%
    }
} else {
%>
    Good try but you are missing something ...
<%
}
%>
</html>