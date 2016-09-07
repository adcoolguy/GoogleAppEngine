package com.appspot.cloudserviceapi;

public class HelloWorld {
	  public static void main(String[] args) {
	    System.out.println(toRelatedLinks("Hello World 1 ${related1} ${related2}"));
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

	  	return summaryText;
	  }
	}
