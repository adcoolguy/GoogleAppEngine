<%@ page import="app.common.Constants" %>
<%@ page import="app.model.User" %>
<%@ page import="cloudserviceapi.app.controller.UserHandler" %>
<%@ page import="com.appspot.cloudserviceapi.common.model.JsonUtil" %>
<html>
    <title>This is just a test JSP dude!</title>
<%
String userId = (String)request.getParameter("userid");
//String password = (String)request.getParameter("pass");   //no need, remember 2Share does not keep the password locally! :)
if(userId != null /* && password != null */) {
%>
    You entered userId [<%= userId %>]
    <%--and password [<%= password %>]--%>
<%
    User item = null;
    try {
        item = new User();
        item.setName(userId);
        UserHandler h = new UserHandler();
        h.setUid(item.getName());
        item = (User)h.doCreateItem(item);

%>
    UserId [<%= userId %>] created successfully with uid [<%= item.getId() %>].
<%
    } catch (Exception e) {
%>
        <%= item.toString() %> Error: <%= e.getMessage() %>
<%
    }
} else {
%>
    Good try but you are missing something ...
<%
}
%>
</html>