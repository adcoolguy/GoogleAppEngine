<%@ page import="com.appspot.cloudserviceapi.common.model.Constants" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Google App Engine Administration</title>
  </head>
	<script language="javascript" src="/sgc/js/screendetect.js">
	</script>
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
        <td colspan="2" style="font-weight:bold;">App Engine Controls:</td>
      </tr>
      <tr>
        <td style="width:30%"></td><td><a href="http://code.google.com/status/appengine">Service Status</a></td>
      </tr>
      <tr>
        <td colspan="2" style="font-weight:bold;">Administrator Controls:</td>        
      </tr>
      <tr>
        <td style="width:30%"></td><td><a href="/userstart">Manage Users</a></td>
      </tr>
      <tr>
        <td style="width:30%"></td><td><a href="/sgc/in">Import Datastore</a></td>
      </tr>
      <tr>
        <td style="width:30%"></td><td><a href="/sgc/admin?action=setTimeZone&value=EST">Change Date View to EST</a></td>
      </tr>
      <tr>
        <td class="admin-warning" colspan="2">
        	<div class="admin-warning-red">
*** Link marked by triple stars has no confirmation page and implies that you agree that the action is unrecoverable.
			</div>
		</td><td>&nbsp;</td>
      </tr>
      <tr>
        <td style="width:30%"></td><td>*** <a href="/sgc/admin?action=purgeGreeting">Purge Greeting</a></td>
      </tr>
      <tr>
        <td style="width:30%"></td><td>*** <a href="/sgc/admin?action=purgeUser">Purge User</a></td>
      </tr>
      <tr>
        <td style="width:30%"></td><td>*** <a href="/sgc/admin?action=purgeWorkOrder">Purge Work Order</a></td>
      </tr>
      <tr>
        <td style="width:30%"></td><td>*** <a href="/sgc/jsp/init.jsp">Initialize Database</a></td>
      </tr>
      <tr>
        <td style="width:30%"></td><td>*** <a href="/sgc/nuke">Nuke Datastore</a></td>
      </tr>
      <tr>
        <td style="width:30%"></td><td>*** <a href="/sgc/jsp/drop.jsp">Drop Database</a></td>
      </tr>
      <tr>
        <td colspan="2" style="font-weight:bold;">Experimental Controls:</td>
      </tr>
      <tr>
        <td class="admin-warning" colspan="2">
        	<div class="admin-warning-red">
PHP based and not production ready.
			</div>
		</td><td>&nbsp;</td>
      </tr>
      <tr>
        <td style="width:30%"></td><td><a href="../php/customer/list.php">Manage Client Account</a></td>
      </tr>
      <tr>
        <td style="width:30%"></td><td><a href="../php/employee/list.php">Manage Employee Account</a></td>
      </tr>
      <tr>
        <td style="width:30%"></td><td><a href="../php/workorder/list.php">Manage Work Order</a></td>
      </tr>
    </table>
  </body>
</html>
