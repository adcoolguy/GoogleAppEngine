<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.appspot.cloudserviceapi.common.model.Constants" %>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Request Form</title>
  </head>
  
  <body>

<%if("1".equals(request.getParameter("rid"))) { %>
You have not requested any service before or entered a valid phone number to see the service history.
<% } else if("2".equals(request.getParameter("rid"))) { %>
You do not have any work order previously.
<% } %>

  </body>
</html>