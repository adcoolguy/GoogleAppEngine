package com.nibado.example.jwtangspr;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
//import org.datanucleus.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.appspot.cloudserviceapi.common.model.SettingsDBUtils;
import com.appspot.cloudserviceapi.security.spring.model.GaeUserDetails;
import com.appspot.cloudserviceapi.security.spring.model.UserSecurityDAO;

import app.common.ParseHelper;
import app.common.SecurityUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Map<String, List<String>> userDb = new HashMap<>();

	private static String JWTSecretKeyDB = "jwtsecretkey01072016";

	public void getKey() {
    	String temp = SettingsDBUtils.getSettings("secretkey.common");
    	if(!StringUtils.isEmpty(temp) && !temp.startsWith("${")) {
    		JWTSecretKeyDB = temp;
    		System.out.println("JWT secret key in datastore detected.");
    	} else {
    		System.out.println("Default JWT secret key used.");
    	}
	}

    public UserController() {
//        userDb.put("tom", Arrays.asList("user"));
//        userDb.put("sally", Arrays.asList("user", "admin"));
//        userDb.put("foo", Arrays.asList("user", "admin", "foo"));
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody final UserLogin login)
        throws ServletException {
        //$$$$$$$$ THIS MUST BE COMMENTED OUT IN PRODUCTION !!!!!! $$$$$$$$$$
    	//System.out.println("name [" + login.name + "] password [" + login.password + "]");
		boolean authenticated = false;
		if(SecurityUtils.isParseLogin(login.password) && ParseHelper.isSessionValid(login.name, login.password)) {
			authenticated = true;
		}
		else {
			GaeUserDetails usr = (new UserSecurityDAO()).getGaeUserDetails(login.name);
//			System.out.println(usr.getPassword());
//			System.out.println(login.password);
			//=== c.f. https://docs.spring.io/spring-security/site/docs/4.0.4.CI-SNAPSHOT/apidocs/org/springframework/security/crypto/bcrypt/BCrypt.html
			if(usr != null && BCrypt.checkpw(login.password, usr.getPassword())) {
				authenticated = true;
			}
		}

        if (//login.name == null || !userDb.containsKey(login.name)
        	!authenticated
        	) {
            throw new ServletException("Invalid login");
        } else {
            userDb.put(login.name, Arrays.asList("user"));
        }
        //$$$$$$$$ THIS MUST BE COMMENTED OUT IN PRODUCTION !!!!!! $$$$$$$$$$
        //System.out.println("secretkey [" + JwtFilter.getJWTSecretKeyDB() + "]");
        return new LoginResponse(Jwts.builder().setSubject(login.name)
            .claim("roles", userDb.get(login.name)).setIssuedAt(new Date())
            .signWith(SignatureAlgorithm.HS256, JwtFilter.getJWTSecretKeyDB()).compact());
    }

    @SuppressWarnings("unused")
    private static class UserLogin {
        public String name;
        public String password;
    }

    @SuppressWarnings("unused")
    private static class LoginResponse {
        public String token;

        public LoginResponse(final String token) {
            this.token = token;
        }
    }
}
