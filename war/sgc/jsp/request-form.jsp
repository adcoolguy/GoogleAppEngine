<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="tapp.model.sgc.WorkOrder" %>
<%@ page import="com.appspot.cloudserviceapi.common.StringUtil" %>
<%@ page import="com.appspot.cloudserviceapi.common.Constants" %>

<!-- workaround form bug -->
<style type="text/css">
    #section_58{
    }
</style>

<%
	String phoneAreaCode = "";
	String phoneNumber = "";
	String email = "";
	String firstName = "";
	String lastName = "";
	String address1 = "";
	String address2 = "";
	String city = "";
	String state = "";
	String postal = "";
	String country = "";

	WorkOrder wo = (WorkOrder)request.getSession().getAttribute("wo");
	//com.appspot.cloudserviceapi.sgc.MockupWorkOrder wo = new com.appspot.cloudserviceapi.sgc.MockupWorkOrder();
	if(wo != null) {
		phoneAreaCode = StringUtil.getPhoneAreaCode(wo.getPhone());
		phoneNumber = StringUtil.getPhoneNumber(wo.getPhone());
		email = StringUtil.handleNull(wo.getEmail());
		firstName = StringUtil.handleNull(wo.getFirstName());
		lastName = StringUtil.handleNull(wo.getLastName());
		address1 = StringUtil.handleNull(wo.getAddress1());
		address2 = StringUtil.handleNull(wo.getAddress2());
		city = StringUtil.handleNull(wo.getCity());
		state = StringUtil.handleNull(wo.getState());
		postal = StringUtil.handleNull(wo.getPostal());
		country = StringUtil.handleNull(wo.getCountry());
	}
%>
<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Request Form</title>
  </head>

  <body>
<div data-role="page" data-theme="a">
	<div data-role="header" data-nobackbtn="true">
		<a href="<%= Constants.MAIN_URL %>" data-icon="back"><%=session.getAttribute("data-icon-back")%></a>
		<h1>Request Form</h1>
	</div>
	<div data-role="content" data-theme="a">
	You are logged in as <%= request.getAttribute("userID") %> and update will be logged under this account.<p>
<div align="center">

<!--  begin - generated by jot form -->
<!-- 
replace action="http://www.jotform.com/submit.php" with 
action="/sgc/request"
-->

<jsp:include page="../html/pc/request-form.html"/>
<!--  end - generated by jot form -->

</div>
	</div>
</div>
  </body>
</html>