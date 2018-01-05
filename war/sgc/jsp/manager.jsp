<%@ page import="com.appspot.cloudserviceapi.common.model.Constants" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Google App Engine Administration</title>
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
%>
    <h1><%= Constants.GREETING_PRETEXT %> <%= userID %>!</h1>
	
    <table>
      <tr>
        <td colspan="2" style="font-weight:bold;">Reports:</td>
      </tr>
      <tr>
        <td style="width:30%"></td><td><a href="/sgc/managerreport">Service Report</a></td>
      </tr>
      <tr>
        <td colspan="2" style="font-weight:bold;">Management Controls:</td>
      </tr>
      <tr>
        <td style="width:30%"></td><td><a href="/sgc/clientstart">Manage Client Account</a></td>
      </tr>
      <tr>
        <td style="width:30%"></td><td><a href="/sgc/employeestart">Manage Employee Account</a></td>
      </tr>
      <tr>
        <td style="width:30%"></td><td><a href="/sgc/orderstart">Manage Work Order</a></td>
      </tr>
      <tr>
        <td style="width:30%"></td><td><a href="/sgc/out">Export Datastore</a></td>
      </tr>
    </table>
  </body>
</html>
