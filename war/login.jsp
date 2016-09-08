<%--<%@ page session="true"%>--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.appspot.cloudserviceapi.common.Constants" %>
<%@ page import="org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter"%>
<html>
<head>
    <title>C7i106g</title>
    <meta name="viewport" content="initial-scale=2.3, user-scalable=no, width=device-width"> <!-- source: https://developer.apple.com/library/ios/DOCUMENTATION/AppleApplications/Reference/SafariWebContent/UsingtheViewport/UsingtheViewport.html -->
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<style>
        body {
            margin-left: 8px;
        }
        h2 {
            font-size: 1.2em;
            margin: 0 0 0 0;
        }
        .label, input {
/*            font-size: 1.2em; */
            margin: 5px 0 5px 0;
            max-width: 65%;
            width: 52%;
        }
        /* IE styles only */
        @-ms-viewport {
       		width: device-width;
		}
	    /* iPhone 2G-4S in portrait and landscape (source: http://stephen.io/mediaqueries/) */
	    @media only screen
	    and (min-device-width : 320px)
	    and (max-device-width : 480px) {
	        body {
	            -webkit-text-size-adjust:80%;
	            overflow-y: hidden;
	        }
	    }
		@media (min-width:801px) { /* tablet, landscape iPad, lo-res laptops ands desktops */
			h2 {
			    font-size: 3.2em;
			}
			table {
				font-size: 1.2em;
				margin: 0 auto; /* or margin: 0 auto 0 auto */
			}
		}
		@media (min-width:1025px) { /* big landscape tablets, laptops, and desktops */ 
		}
		@media (min-width:1281px) { /* hi-res laptops and desktops */ 
		}
	</style>
    <link rel="stylesheet" href="/css/mobile.css" type="text/css" media="screen, projection, tv" />
	<script type="text/javascript" src='//ajax.googleapis.com/ajax/libs/jquery/1.10.0/jquery.js'></script>
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
	<div data-role="header" data-nobackbtn="true">
<% if(session.getAttribute("data-icon-back") != null) { %>
		<a href="<%= com.appspot.cloudserviceapi.common.Constants.MAIN_URL %>" data-icon="back"><%=session.getAttribute("data-icon-back")%></a>
<% } %>
		<h2>Welcome!</h2>
	</div>
	<div data-role="content" data-theme="c">

 	<% if (session.getAttribute(AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY) != null) { %>
 		<div class="errors">
			Your login attempt was not successful, try again.<br />
			Reason: <%=session.getAttribute(AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY)%>
		</div>
 	<% } %>
 		
<form id="loginForm" name="loginForm" action="j_spring_security_check" method="post">
        <table>
<tr><td><span class="label">ID</span></td><td><input name="password" autocapitalize="off" autocorrect="off" id="usernameField" type="text" /></td></tr>
          <tr><td><span class="label">PWD</span></td><td><input autocomplete="off" id="passwordField" type="password" name="j_password" /></td></tr>
          <tr><td colspan="2"><input style="height:100%;width:100%;margin-left:-3px;" type="submit" value="Go" /></td></tr>
        </table>
</form>

	</div>
</div>
</body>

</html>