package com.appspot.cloudserviceapi;

import java.util.StringTokenizer;

public class HelloWorld {
  public static void main(String[] args) {
    System.out.println(toRelatedLinks("Hello World 1 ${related1} ${related2}"));
  }
  
  //=== this should not be used, instead, call the one inside ServiceRegistryUtil !!!
  	public static String getRedirectedServiceName(String sid) throws RuntimeException {
		if(sid == null) throw new RuntimeException("EndPoint is null or empty!");

		String retVal = null;
		
		String b = "${";
		String e = "}";
		int begin = sid.trim().lastIndexOf(b);
		int end = sid.trim().lastIndexOf(e); //e.g. ${private1} = private1
		if(begin == 0 && end == (sid.trim().length() -1)) {
			retVal = sid.substring(begin+2, end);
		}
		
		return retVal;
	}
	
  public static String getRealLink(String service) {
  	String ret = service;
  	
  	if(service.equals("related1")) {
  		ret = "http://www.x.com";
  	} else 
  	if(service.equals("related2")) {
  		ret = "http://www.y.com";
  	}
  	
  	return ret;
  }
  
  public static String toRelatedLinks(String summaryText) {
	StringTokenizer st =new StringTokenizer(summaryText);
	System.out.println("tokens count: " + st.countTokens());
	StringBuffer sb = new StringBuffer();
	String temp = null;
	while (st.hasMoreElements()) {
		String token = st.nextElement().toString();
		temp = getRedirectedServiceName(token);
		System.out.println("token = " + getRedirectedServiceName(token));
		if(temp == null) {
			sb.append(token).append(' ');
		} else {
			//look up for the real link!
			sb.append("<a href='").append(getRealLink(temp)).append("'>").append(temp).append("</a>").append(' ');
		}
	}
  	return sb.toString();
  }
}
