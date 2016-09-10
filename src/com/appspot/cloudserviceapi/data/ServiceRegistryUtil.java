package com.appspot.cloudserviceapi.data;

import java.util.Date;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import tapp.model.ServiceRegistry;
import app.common.AppUtils;
import app.common.Constants;

import com.appspot.cloudserviceapi.sci.dao.ServiceRegistryDAO;
import com.appspot.cloudserviceapi.sci.dao.ServiceRegistryRepository;

public class ServiceRegistryUtil {

	/**
	 * Handle embedded token e.g. ${server1} where server1 is another entry defined in service registry.
	 * It returns the real value of the entry (service key).
	 * 
	 * Note: Limited to 10000 tokens.
	 */
	public static String handleEndPoint(String endPoint, ServiceRegistryDAO r) throws Exception {
		ServiceRegistry sr = null;
		String b = "${";
		String e = "}";
		int begin = endPoint.lastIndexOf(b);
		int end = endPoint.lastIndexOf(e); //e.g. ${private1} = private1
		int count = -1;
		String finalEndPoint = null;
		while(count <10000) { //TODO - restrict up to 10000 replacements only, should not have to restrict this!
			if(begin > -1 && end > -1) {
				String lookupService = endPoint.substring(begin + b.length(),end);
				//System.out.println("lookupService found in endPoint = " + lookupService);
				sr = (tapp.model.ServiceRegistry)r.findServiceRegistryByService(lookupService);
				if(sr != null) {
					//TBD - this could be a target for the (future's) Java closure :)
					//=== BT0001 begin - handling "legacy" bigtable column (does not exist before the new changes)
					boolean disabled = false, useDescription = false, useHTML = true;
//					try {
//						if(u.getDisabled()) {
//							disabled = true;
//						}
//					} catch (Exception e1) {
//						System.out.println("legacy entity without disabled flag: " + u);
//					}
//					try {
//						if(u.getUseDescription()) {
//							useDescription = true;
//						}
//					} catch (Exception e2) {
//						System.out.println("legacy entity without useDescription flag: " + u);
//					}
//					try {
//						if(!u.getUseHtml()) {
//							useHTML = false;
//						}
//					} catch (Exception e3) {
//						System.out.println("legacy entity without useHTML flag: " + u);
//					}
					//=== BT0001 end - handling "legacy" bigtable column (does not exist before the new changes)
					
					if(!useDescription) {
						finalEndPoint = endPoint.substring(0, begin) + sr.getEndpoint() + endPoint.substring(end + e.length(), endPoint.length());
					} else {
						finalEndPoint = endPoint.substring(0, begin) + sr.getDescription() + endPoint.substring(end + e.length(), endPoint.length());
					}
					System.out.println("Final endPoint = " + finalEndPoint);
					endPoint = finalEndPoint;
				}
			}
			count++;
			begin = endPoint.lastIndexOf(b);
			end = endPoint.lastIndexOf(e); //e.g. ${private1} = private1
		}
		
		return endPoint;
	}
	
	public static void countHit(ServiceRegistry u, ServiceRegistryDAO r, HttpServletRequest request, boolean hitCountEnabled) {
		if(hitCountEnabled) {
			if(request.getParameter(Constants.STEALTH_MODE) == null) {
//			if(request.getParameter(Constants.COUNT_HIT_MODE) != null) {
				Long temp = u.getHit();
				if(temp == null) {
					temp = new Long(0);
				}
				u.setHit(temp + 1L);
				u.setLastAccessed(new Date());
				System.out.println(u.getService() + " hit " + u.getHit());
				r.save(u);
				r.updateCache(u);
			} else {
				System.out.println("stealth mode, tracking disabled");
			}
		} else {
			System.out.println("hit count disabled, tracking disabled");
		}
	}

	
	/**
	 * Given a URL, returns its service registry object.
	 * @param endPoint
	 * @param r
	 * @return
	 * @throws Exception
	 */
	public static ServiceRegistry getServiceRegistry(String service, ServiceRegistryDAO r) throws Exception {
		ServiceRegistry sr = null;
		sr = (tapp.model.ServiceRegistry)r.findServiceRegistryByService(service);
		if(sr == null) {
			throw new Exception("No such service registry identified by '" + service + "'");
		}
		return sr;
	}

	/** Check if it a String is a SR endpoint i.e. in the format of ${sid} */
	public static boolean isRedirectedEndPoint(String endPoint) throws Exception {
		if(endPoint == null) throw new Exception("EndPoint is null or empty!");

		boolean retVal = false;
		
		String b = "${";
		String e = "}";
		int begin = endPoint.trim().lastIndexOf(b);
		int end = endPoint.trim().lastIndexOf(e); //e.g. ${private1} = private1
		if(begin == 0 && end == (endPoint.trim().length() -1)) {
			retVal = true;
		}
		
		return retVal;
	}

	/** 
	 * Retrieve the name of a SR endpoint i.e. for ${sid}, returns just sid 
	 * returns null if it is not an SR endpoint i.e. no id found!
	 * */
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
	
//	public static String getRequestMode(HttpServletRequest request) throws Exception {
//		String ret = request.getParameter("t");
//		if(ret != null) {
//			ret = Constants.TRAVERSE_MODE;
//		}
//
//		return ret;
//	}

	  public static String getSRLink(String service, ServiceRegistryDAO r, String remoteHost) {
		  	String ret = service;

		  	return AppUtils.getAppHost(remoteHost) + "/go/" + ret;	//KISS for now
	  }

	  public static String getRealLink(String service, ServiceRegistryDAO r) {
		  	String ret = service;
		  	
		  	if(r != null) {
				ServiceRegistry sr = null;
				sr = (tapp.model.ServiceRegistry)r.findServiceRegistryByService(service);
				if(sr == null) {
					//do nothing
				} else {
					ret = sr.getEndpoint();
				}
		  	}
//		  	else {
//			  	if(service.equals("related1")) {
//			  		ret = "http://www.x.com";
//			  	} else 
//			  	if(service.equals("related2")) {
//			  		ret = "http://www.y.com";
//			  	}
//		  	}
		  	
		  	return ret;
		  }
		  
	  public static String toRelatedLinks(String summaryText, ServiceRegistryDAO r, String remoteHost) throws RuntimeException {
		StringTokenizer st =new StringTokenizer(summaryText);
		//		System.out.println("tokens count: " + st.countTokens());
		StringBuffer sb = new StringBuffer();
		String temp = null;
		while (st.hasMoreElements()) {
			String token = st.nextElement().toString();
			temp = getRedirectedServiceName(token);
//			System.out.println("token = " + getRedirectedServiceName(token));
			if(temp == null) {
				sb.append(token).append(' ');
			} else {
				//look up for the real link!
				sb.append("<a target='_new' href='").append(getSRLink(temp, r, remoteHost)).append("'>${").append(temp).append("}</a>").append(' ');
			}
		}
	  	return sb.toString();
	  }

}