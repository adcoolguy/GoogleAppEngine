package com.appspot.cloudserviceapi.common;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class HttpUtil {

	public static void dumpAllHttpRequests(String marker, HttpServletRequest request) {
		Enumeration enAttr = request.getAttributeNames(); 
		while(enAttr.hasMoreElements()){
		 String attributeName = (String)enAttr.nextElement();
		 System.out.println(marker + " Attribute Name - "+attributeName+", Value - "+(request.getAttribute(attributeName)).toString());
		}
	
		System.out.println("To out-put All the request parameters received from request - ");
	
		Enumeration enParams = request.getParameterNames(); 
		while(enParams.hasMoreElements()){
		 String paramName = (String)enParams.nextElement();
		 System.out.println(marker + " Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
		}
	}

}