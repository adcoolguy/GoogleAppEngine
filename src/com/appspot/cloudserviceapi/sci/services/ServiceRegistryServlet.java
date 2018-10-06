package com.appspot.cloudserviceapi.sci.services;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
//import org.datanucleus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.appspot.cloudserviceapi.common.model.Constants;
import com.appspot.cloudserviceapi.common.model.SettingsDBUtils;
import com.appspot.cloudserviceapi.common.model.StringUtil;
import com.appspot.cloudserviceapi.data.ServiceRegistryUtil;
import com.appspot.cloudserviceapi.data.URLCategory;
import com.appspot.cloudserviceapi.sci.dao.ServiceRegistryDAO;
import com.appspot.cloudserviceapi.sci.dao.ServiceRegistryRepository;

import app.common.DalekUtils;
import app.common.ProtractorUtils;
import tapp.model.ServiceRegistry;

@SuppressWarnings("serial")
public class ServiceRegistryServlet extends HttpServlet {
	@Autowired
	ServiceRegistryRepository r;

	//=== common db based settings of SR
	private static Boolean hitCountEnabled;
	private static Boolean backupServiceEnabled;

	public static boolean isBackupServiceEnabled() {
		return backupServiceEnabled;
	}

	public static void setBackupServiceEnabled(boolean backupServiceEnabled) {
		ServiceRegistryServlet.backupServiceEnabled = backupServiceEnabled;
	}

	public static Boolean isHitCountEnabled() {
		return hitCountEnabled;
	}

	public static void setHitCountEnabled(Boolean hitCountEnabled) {
		ServiceRegistryServlet.hitCountEnabled = hitCountEnabled;
	}

	public void init(ServletConfig config) throws ServletException {
//	    GaeVFS.setRootPath(config.getServletContext().getRealPath("/"));
    	String temp = SettingsDBUtils.getSettings("sr.hit.count.enabled");
    	if(!StringUtils.isEmpty(temp) && !temp.startsWith("${")) {
//    		setHitCountEnabled(Boolean.parseBoolean(temp));
    		System.out.println("Hit count enabled in datastore detected.");
    	} else {
    		System.out.println("Hit count disabled.");
    	}
    	temp = SettingsDBUtils.getSettings("sr.backup.service.enabled");
    	if(!StringUtils.isEmpty(temp) && !temp.startsWith("${")) {
//    		setBackupServiceEnabled(Boolean.parseBoolean(temp));
    		System.out.println("Backup service enabled in datastore detected.");
    	} else {
    		System.out.println("Backup service disabled.");
    	}
		System.out.println("ServiceRegistryServlet initialized.");
	}
	
	public void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		doPost(request, response);
	}

	public void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {

//		if(!StringUtils.isEmpty(request.getParameter("Access-Control-Allow-Headers")) /* just a way for SR client to request CORS separately from normal GET request */) {
//			response.setContentType("text/plain");
			//===begin support CORS (c.f. https://spring.io/guides/gs/rest-service-cors/)
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
			response.addHeader("Access-Control-Max-Age", "3600");
			response.addHeader("Access-Control-Allow-Headers", "x-requested-with");
			//===end support CORS
//		} else {
			response.setCharacterEncoding("UTF-8");
//			response.setContentType("text/json; charset=UTF-8");	//text/json type caused: "Unexpected end of input" during jQuery's json.parse() on Chrome/Webkit browsers unfortunately
			response.setContentType("text/plain; charset=UTF-8");	//Note: avoid SyntaxError: "Unexpected end of input" during jQuery's json.parse() on Chrome/Webkit browsers
			response.setContentType("text/html");
//		}
		
		String serviceName = request.getParameter("s");
		ServiceRegistryDAO r = new ServiceRegistryDAO();
		ServiceRegistry sr = null;

		//=== support Rails-style edit via URL
		String editFlag = request.getParameter(app.common.Constants.EDIT_MODE1);		//edit the service only
		if(editFlag == null) {
			editFlag = request.getParameter(app.common.Constants.EDIT_MODE2);		//try secondary edit flag
		}
		String traverseFlag = request.getParameter(app.common.Constants.TRAVERSE_INDIRECT_MODE);	//traverse indirect service, if any
		if(editFlag != null) {
			if(serviceName != null) {
				if(traverseFlag == null) {
					sr = (ServiceRegistry)r.findServiceRegistryByService(serviceName);
				} else {
					try {
						sr = handleRedirectedService(serviceName, r);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(sr != null) {
					long id = -1;
					id = sr.getId();
					//=== forward it to edit page
					String url = Constants.SR_URI + "/serviceregistrysave/" + id;
					//TODO need to get the final url here based on another level of sid e.g. ${next_sid} if any
					//???
					response.sendRedirect(url);
					return;	//I am done :)
				}
			}
		}
		
		//=== if move pass beyond this, it is business as usual pal!
		
		String resp = null, respF = "\n</body>\n</html>", respH = "<html><head><meta name=\"viewport\" content=\"initial-scale=1.07, user-scalable=no\"/><head>\n<body>\n";
		if(serviceName != null && !serviceName.trim().equals("")) {
			try {
				if(sr == null) sr = (ServiceRegistry)r.findServiceRegistryByService(serviceName);
				if(sr == null) {
					resp = respH + "*** There is nothing here :) ***" + respF;	//"<html>Sorry, no such service [" + serviceName + "] found in the registry.</html>";
					response.getWriter().println(resp);
				}
				//TODO - this could be a target for the (future's) Java closure :)
				//=== BT0001 begin - handling "legacy" bigtable column (does not exist before the new changes)
				boolean disabled = false, useDescription = false, useHTML = true;
//				try {
////					if(sr.getDisabled()) {
//					if(Boolean.valueOf(sr.getDisabled()).booleanValue()) {
//						disabled = true;
//					}
//				} catch (Exception e) {
//					System.out.println("legacy entity without disabled flag: " + sr);
//				}
//				try {
//					if(Boolean.valueOf(sr.getUseDescription()).booleanValue()) {
//						useDescription = true;
//					}
//				} catch (Exception e) {
//					System.out.println("legacy entity without useDescription flag: " + sr);
//				}
//				try {
//					if(Boolean.valueOf(sr.getUseHtml()).booleanValue()) {
//						useHTML = false;
//					}
//				} catch (Exception e) {
//					System.out.println("legacy entity without useHTML flag: " + sr);
//				}
				//=== BT0001 end - handling "legacy" bigtable column (does not exist before the new changes)
				if(sr != null 
						&& !disabled
						) {
					System.out.println("registry request id '" + serviceName + "' found '" + sr.getEndpoint() + "'");

					//=== handle special token
					ServiceRegistry firstRedirectedSR = null;
					String endPoint = sr.getEndpoint();
					if(!useDescription && !ServiceRegistryUtil.isRedirectedEndPoint(endPoint) && isUrl(endPoint)) {
						ServiceRegistryUtil.countHit(sr, r, request, hitCountEnabled);
						response.sendRedirect(endPoint);
					} else 
					if(!useDescription && ServiceRegistryUtil.isRedirectedEndPoint(endPoint)) {
						String originalEndPoint = endPoint;
						//String finalEndPoint = ServiceRegistryUtil.handleEndPoint(endPoint, r);		//costly if multiple levels are specified!!!
						firstRedirectedSR = ServiceRegistryUtil.getServiceRegistry(endPoint.trim().substring(2, endPoint.trim().length()-1), r);		//get only the first level redirection
						String finalEndPoint = firstRedirectedSR.getEndpoint();
						boolean disabled1 = false, useDescription1 = false, useHTML1 = true;
						try {
//							if(firstRedirectedSR.getUseDescription()) {
//							if(Boolean.valueOf(firstRedirectedSR.getUseDescription()).booleanValue()) {
//								useDescription1 = true;
//							}
						} catch (Exception e) {
							System.out.println("legacy entity level 1 without useDescription flag: " + sr);
						}
						//=== begin - support getting the content of the redirected service!!!
						if(!isUrl(finalEndPoint) && !useDescription1) {
							ServiceRegistryUtil.countHit(sr, r, request, hitCountEnabled);
							resp = respH + firstRedirectedSR.getEndpoint() + respF;	//returns the content of the endpoint (which is not supposed to be an URL)
							response.getWriter().print(resp.trim());
						} else
						//it is a redirection
						if(isUrl(finalEndPoint) && !useDescription1) {
							ServiceRegistryUtil.countHit(sr, r, request, hitCountEnabled);
							response.sendRedirect(finalEndPoint);
						} else {
							ServiceRegistryUtil.countHit(sr, r, request, hitCountEnabled);
							resp = respH + firstRedirectedSR.getDescription() + respF;	//returns the description content
							
							if(resp != null) {
								resp = respH + handleTestScripts(sr.getCategory(), firstRedirectedSR) + respF;
							}
							
							response.getWriter().print(resp.trim());
						}
						//=== end - support getting the content of the redirected service!!!
					} else
					if(useDescription) {
						String description = sr.getDescription();
						description = sr.getDescription();

//						if(!isUrl(endPoint)) {
//							endPoint = "";	//not a hyperlink, excluded and also good for security reason (endpoint could be a password)
//						}
						if(useHTML) {
							response.setContentType("text/html");
						} else {
//							resp = endPoint + System.getProperty("line.separator") + description;
						}
						if(sr.getCategory() != null) {
							resp = respH + handleTestScripts(sr.getCategory(), sr) + respF;
						} else
						if(request.getParameter("xray") != null) {
							resp = respH + StringUtil.toASCIICode(description) + respF;
						} else
						if(request.getParameter("xml") != null) {
							resp = respH + description.substring(1, description.length()) + respF;	//TBD - temporary solution, until the char is filtered properly
						} else {
							resp = respH + description.trim() + respF;
						}
						if(resp != null) {
							ServiceRegistryUtil.countHit(sr, r, request, hitCountEnabled);
							//=== support "Related Services" based on the summary content!
							String related = "";
							if(sr.getSummary() != null) {
								related = "~~~ " + ServiceRegistryUtil.toRelatedLinks(sr.getSummary(), r, request.getRemoteHost()) + " ~~~<p>";
							}
							//=== support "Related Services" based on the description (the content)!
							//=== KISS: support scenario like ${1}<p> or ${1}<br>
							resp = StringUtils.replace(resp, ">${", "> ${");
							resp = StringUtils.replace(resp, "}<", "} <");
							resp = resp = respH + ServiceRegistryUtil.toRelatedLinks(resp, r, request.getRemoteHost()) + respF;
							response.getWriter().print(related + resp.trim());
						}
					} else {
						if(isUrl(endPoint)) {
							ServiceRegistryUtil.countHit(sr, r, request, hitCountEnabled);
							response.sendRedirect(endPoint);
						} else {
							ServiceRegistryUtil.countHit(sr, r, request, hitCountEnabled);
							resp = respH + endPoint + respF;	//just returns the content
							response.getWriter().print(resp.trim());
						}
					}
				}
				else if(sr != null && disabled) {
					ServiceRegistryUtil.countHit(sr, r, request, hitCountEnabled);	//for tracking purpose only including DOS attack
					resp = respH + "*** Sorry, the service [" + serviceName + "] is currently disabled. ***" + respF;
					response.getWriter().println(resp);
				}
				//TODO commented out for now due to https://community.jboss.org/message/868254#868254
//			} catch (com.google.apphosting.api.ApiProxy.OverQuotaException e1) {
//				//http://www.developerit.com/2010/03/29/how-to-use-java-on-google-app-engine-without-exceeding-minute-quotas
//				//The two lines below will get the CPU before requesting User-Agent Information 
//				QuotaService qs = QuotaServiceFactory.getQuotaService();
//				long start = qs.getCpuTimeInMegaCycles();
//				//Request the user Agent info String userAgent = req.getHeader("User-Agent");
//				//The three lines below will get the CPU after requesting User-Agent Information
//				// and informed it to the application log. 
//				long end = qs.getCpuTimeInMegaCycles(); 
//				double cpuSeconds = qs.convertMegacyclesToCpuSeconds(end - start); 
//				System.out.println("CPU Seconds on geting User Agent: " + cpuSeconds);
//				resp = "<html>Sorry I am not available right now :( Please check again later?" +
//						" (" + cpuSeconds + ")" +
//						"</html>";
//				response.getWriter().println(resp);
			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().println(StringUtil.toString(e));
			}
		} else {
			resp = "";	//"<html>Service must be specified with request parameter \"s\". [" + serviceName + "]</html>";
			response.getWriter().println(resp);
		}
	}

	private String handleTestScripts(URLCategory type, ServiceRegistry sr) {
		String resp = sr.getDescription();
		if(type == URLCategory.DALEKJS) {
			DalekUtils d = new DalekUtils();
			String temp = d.parse(resp);
			String f = d.getFirstLine().replaceAll("\n", "");
			resp = DalekUtils.header.replaceAll("\\{\\{\\}\\}", f) + temp + DalekUtils.footer;
		} else
		if(type == URLCategory.PROTRACTOR) {
			ProtractorUtils p = new ProtractorUtils();
			String temp = p.parse(resp);
			String f = p.getFirstLine().replaceAll("\n", "");
			resp = ProtractorUtils.header.replaceAll("\\{\\{\\}\\}", f) + temp + ProtractorUtils.footer;
		} else {
			//TODO do I need to serve here -- could be buggy here!!!
		}
	
		return resp;
	}

	private ServiceRegistry handleRedirectedService(String serviceName, ServiceRegistryDAO r) throws Exception {
		ServiceRegistry ret = (ServiceRegistry)r.findServiceRegistryByService(serviceName);

		if(ret != null) {
			ret = traverseIndirectService(ret, r);
		}

		return ret;
	}
	
	/** Recursively traverse until the end of ${} i.e. with no ${} anymore as part of star and end string 
	 * @throws Exception */
	private ServiceRegistry traverseIndirectService(ServiceRegistry service, ServiceRegistryDAO r) throws Exception {
		ServiceRegistry ret = null;
		String temp = null;
		if(service != null) {
			String serviceName = service.getEndpoint();
			if(serviceName != null && (temp = ServiceRegistryUtil.getRedirectedServiceName(serviceName)) != null) {
				ServiceRegistry sr = (ServiceRegistry)r.findServiceRegistryByService(temp);
				if(sr != null) {
					//try harder
					return traverseIndirectService(sr, r);
				} else {
					ret = service;
				}
			} else {
				ret = service;	//that's it, this is our final one
			}
		}
		
		return ret;
	}

	/**
	 * Does not check if the URL is accesible or not, just check if it starts with http.
	 */
	private boolean isUrl(String url) {
		boolean retVal = false;
		if(url != null && url.trim().startsWith("http")) {
			retVal = true;
		}
		return retVal;
	}

}