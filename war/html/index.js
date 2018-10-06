	gMovieURL = '/mcrud/movie.html';
    function fb_logout() {
        //var UserObject = Parse.Object.extend("UserObject");
        //var userObject = new UserObject();
        //https://parse.com/apps/2share/collections#class/_User
        $("#fb-redirect-for-IE").hide();
        $("#fb-login-notice").hide();
    }
    /*
     var TestObject = Parse.Object.extend("TestObject");
     var testObject = new TestObject();
     testObject.save({foo: "bar"}, {
     success: function(object) {
     $(".success").show();
     },
     error: function(model, error) {
     $(".error").show();
     }
     });
     */

    $.ajaxSetup({headers: { 'Authorization': 'Bearer ' + localStorage.getItem('2shareJWTToken') }}); //JWT support

    function handleUser(type) {
        $("#status").show();

        if(type === 'signup') {
            var isNotABot = false;

            if(isNotABot) {
                Parse.User.signUp($('#username').val(), $('#password').val(), { ACL: new Parse.ACL() }, {
                    success: function(user) {
                        $("#status").hide();
                        alert($('#username').val() + " signed up successfully!");
                    },
                    error: function(user, error) {
                        $("#status").hide();
                        alert('Error = ' + error + "' [" + error.message + "]: Name entered was [" + $('#username').val() + "]");
                    }
                });
            }
            //need to refresh reCAPTCHA thus use KISS approach
//            window.location.reload();
        } else {
            //********************************* Main Login Logic !!! *********************************
            Parse.User.logIn($('#username').val(), $('#password').val(), {
                success: function(user) {
                    msg = "Logging in user " + $('#username').val() + " ...";
                    var username = Parse.User.current().getUsername();
                    var userId4JWT = user.id;
                    var pwd4JWT = Parse.User.current().getSessionToken();
                    //console.log(msg + " [" + userId4JWT + "] updatedAt [" + user.updatedAt + "] cpattern [" + user.get("cpattern") + "]");
                    //console.log(["User logged in", pwd4JWT]);
                    var fd = JSON.stringify({name: userId4JWT, password: pwd4JWT});
                    $.ajax({
                        url : location.origin + "/api/user/login",
                        async: false,
                        type: "POST",
                        contentType: "application/json;charset=utf-8",
                        dataType: "json",
                        data : fd,
                        success: function(data, textStatus, jqXHR)
                        {
                            //console.log(data.token);
                            // Save the JWT token.
                            localStorage.setItem('2shareJWTToken', data.token);
                        },
                        error: function (jqXHR, textStatus, errorThrown)
                        {
                            console.log('index.js: jwt token request failure: textStatus[' + textStatus + '] errorThrown[' + errorThrown + ']');
                        }
                    });
                    $('#notice').text(msg);
                    //for angularJS (http://stackoverflow.com/questions/1370376/prevent-loss-of-variables-when-browser-reload-button-is-pressed)
                    window.location = gMovieURL + '?username='+username+'&logintype=1';
                },
                error: function(user, error) {
                    // The login failed. Check error to see why.
                    $("#status").hide();
                    $('#password').focus();
                    alert(error.message + ": Name entered was [" + $('#username').val() + "]");
                }
            });
            //********************************* Main Login Logic !!! *********************************
        }
        //window.console && console.log("handleUser " + fbBuild);
    }

    function fb_login() {
        //alert("1fb_login: invoked!");

        //window.console && console.log("facebook gBuild " + fbBuild);
        $("#fb-redirect-for-IE").show();
        $("#fb-login-notice").show();

        //=== source: https://www.parse.com/questions/trouble-with-facebook-login-with-javascript-sdk
        if (Parse.User.current() == null) {
            Parse.FacebookUtils.logIn("email,public_profile", {
                success: function(user) {
                    var msg;
                    if (!user.existed()) {
                        msg = "User signed up and logged in through Facebook!";
                        $("#status").hide();
                    } else {
                        doFbloginNow();
                    }
                },
                error: function(user, error) {
                    //alert("User cancelled the Facebook login, did not fully authorize or your Facebook login id/password is incorrect. Please visit https://www.facebook.com to sign off the account that you try to login and try again.");
                    window.console && console.log("User cancelled the Facebook login, did not fully authorize or your Facebook login id/password is incorrect. Please visit https://www.facebook.com to sign off the account that you try to login and try again.");
                    $("#status").hide();
                    $("#fb-redirect-for-IE").hide();
                    $("#fb-login-notice").hide();
                }
            });
        } else {
            doFbloginNow();
        }
        //window.console && console.log("fb_login " + fbBuild);
    }

    function doFbloginNow() {
        FB.api('/me', function(response) {	//http://stackoverflow.com/questions/3770979/fb-api-is-returning-undefined-for-response-name
            //alert('Your name is ' + response.name);
            currentUserName = response.name;
            $('#currentUserName').html(currentUserName);
            msg = "User " + currentUserName + " logged in through Facebook!";
            //alert(msg);
            window.location.href = window.gMovieURL + '?username='+currentUserName+'&logintype=2';
            //window.location.href = "http://"+window.location.host + '/mcrud/movie.html?username='+username;
            $("#status").hide();
        });
    }

    //http://stackoverflow.com/questions/979662/how-to-detect-pressing-enter-on-keyboard-using-jquery
    $(document).keypress(function(event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == '13') {
            $('#username').attr("disabled", "disabled");
            $('#password').attr("disabled", "disabled");

            if (document.location.toString().indexOf("index1") > -1) {
                handleUser('signup');
            } else {
                $('#notice').text('Contacting ' + Parse.serverURL + ' ...');
                //alert('You pressed a "enter" key in somewhere');
                handleUser('login');
            }
        }
    });
    $("#loginButton").click(function() {
        handleUser('login');
    });
    $("#fbLoginButton").click(function() {
        fb_login();
    });
    $("#createAccountButton").click(function() {
    	var gSignupURL;

        //initialize facebook SDK via parse
        var realAppId;
        if(location.hostname.indexOf('radiant') > -1) {
            gSignupURL = '/parse/index1.html';
        }
        else
        if(location.hostname.indexOf('cbiit') > -1 || location.hostname.indexOf('cadsr') > -1) {
        	gSignupURL = '/parse/index1.html';
        }
        else
        if(location.hostname.indexOf('oo.tv') > -1 || location.hostname.indexOf('hudoone') > -1) {
        	gSignupURL = '/parse/index-c.html';
        }
        else
        if(location.hostname.indexOf('service') > -1 || location.hostname.indexOf('share') > -1) {
        	gSignupURL = '/parse/index1.html';
        }
        else
        if(location.hostname.indexOf('localhost') > -1) {
        	gSignupURL = '/parse/index1.html';
        }

        window.location.href = gSignupURL;
    });
    $("#signupButton").click(function() {
        handleUser('signup');
    });

//define('index', [], function () { //begin of AMD style
//        //does nothing for now - just to suppress "Uncaught Error: No define call for ..." error
//    return function Index(one, two) {
//        this.one = one;
//        this.two = two;
//    };
//}); //end of AMD style