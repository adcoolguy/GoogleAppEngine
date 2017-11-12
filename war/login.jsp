<%--<%@ page session="true"%>--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.appspot.cloudserviceapi.common.Constants" %>
<%@ page import="org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter"%>
<html>
<head>
    <title>V1 A100</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="initial-scale=1, user-scalable=no, width=device-width"> <!-- source: https://developer.apple.com/library/ios/DOCUMENTATION/AppleApplications/Reference/SafariWebContent/UsingtheViewport/UsingtheViewport.html -->

<link rel="stylesheet" href="//code.jquery.com/mobile/1.4.2/jquery.mobile-1.4.2.min.css">
<script src="//code.jquery.com/jquery-1.10.2.min.js"></script>
<script src="//code.jquery.com/mobile/1.4.2/jquery.mobile-1.4.2.min.js"></script>

<style>
@media (min-width:500px){/*Class name*/{/*properties*/}}
@media (max-width:499px){/*Class name*/{/*properties*/}}
</style>

	<script>
		jQuery.noConflict();	//http://wiki.apache.org/tapestry/Tapestry5HowToIntegrateJQuery

		jQuery(function() {
		        jQuery('#usernameField').focus();
		});

        /** this has to be BEFOFE requireJS to avoid reloading it twice! */
        if ("https:" === document.location.protocol) {
            /* secure */
        } else {
            /* unsecure */
            if(location.hostname.indexOf('localhost') === -1) {
                window.location= "https://" + location.hostname + location.pathname;
            }
        }
	</script>
</head>

<body onload='document.loginForm.j_username.focus();'>
<div data-role="page" data-theme="c">
	<div tyle="text-align: center" data-role="header" data-nobackbtn="true">
<% if(session.getAttribute("data-icon-back") != null) { %>
		<a href="<%= com.appspot.cloudserviceapi.common.Constants.MAIN_URL %>" data-icon="back"><%=session.getAttribute("data-icon-back")%></a>
<% } %>
	</div>
	<div style="padding: 20px" data-role="content" data-theme="c">

 	<% if (session.getAttribute(AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY) != null) { %>
 		<div class="errors">
			Your login attempt was not successful, try again.<br />
			Reason: <%=session.getAttribute(AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY)%>
		</div>
 	<% } %>
	</div>


<!-------------- First page main content ----------->
<div data-role="main" class="ui-content">
<div style="text-align: center; font-size: 24px; padding: 30px;">Welcome!</div>
<form method="post" action="j_spring_security_check" data-ajax="false">
	<label for="password">User ID: <span></span></label>
	<input type="text" name="password" id="password" placeholder="Enter your user ID">
	<label for="j_password">Password: <span></span></label>
	<input type="password" id="j_password" name="j_password" placeholder="Enter your password"/>
	<br>
    <button class="btn btn-lg btn-primary btn-block" type="submit">
        Sign in</button>
	
</form>
</div>


</div>
</body>

</html>