package com.nibado.example.jwtangspr;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
//import org.datanucleus.util.StringUtils;

import com.appspot.cloudserviceapi.common.model.HttpUtil;
import com.appspot.cloudserviceapi.common.model.SettingsDBUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class JwtFilter implements Filter {

	private static String JWTSecretKeyDB = "jwtsecretkey01072016";
	private static long hit = 0;

	public void init(FilterConfig cfg) {
    	String temp = SettingsDBUtils.getSettings("secretkey.common");
    	if(!StringUtils.isEmpty(temp) && !temp.startsWith("${")) {
    		setJWTSecretKeyDB(temp);
    		System.out.println("JWT secret key in datastore detected.");
    	} else {
    		System.out.println("Default JWT secret key used."
    		//+ JWTSecretKeyDB
    		);
    	}
		System.out.println("JwtFilter initialized.");
	}

    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
//        final String referrer = "[" + hit++ + "] " + request.getHeader("referer") + " -> " + request.getRequestURL();
//        System.out.println("JwtFilter: referrer [" + referrer + "]");
        HttpUtil.dumpAllHttpRequests("JWT", request);
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ServletException("Missing or invalid Authorization header.");
        }

        final String token = authHeader.substring(7); // The part after "Bearer "

        try {
            //$$$$$$$$ THIS MUST BE COMMENTED OUT IN PRODUCTION !!!!!! $$$$$$$$$$
//            System.out.println("secretkey [" + JwtFilter.getJWTSecretKeyDB() + "]");
            final Claims claims = Jwts.parser().setSigningKey(JwtFilter.getJWTSecretKeyDB())
                .parseClaimsJws(token).getBody();
            request.setAttribute("claims", claims);
        }
        catch (final SignatureException e) {
            throw new ServletException("Invalid token: " + e);
        }

        chain.doFilter(req, res);
    }

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public static String getJWTSecretKeyDB() {
		return JWTSecretKeyDB;
	}

	public static void setJWTSecretKeyDB(String jWTSecretKeyDB) {
		JWTSecretKeyDB = jWTSecretKeyDB;
	}

}
