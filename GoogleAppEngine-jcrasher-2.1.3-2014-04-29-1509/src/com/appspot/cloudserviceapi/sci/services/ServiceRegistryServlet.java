package com.appspot.cloudserviceapi.sci.services;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tapp.model.ServiceRegistry;

import com.appspot.cloudserviceapi.common.Constants;
//import com.appspot.cloudserviceapi.common.JSLintUtil;
//import com.appspot.cloudserviceapi.common.PMDUtil;
import com.appspot.cloudserviceapi.common.StringUtil;
import com.appspot.cloudserviceapi.data.ServiceRegistryUtil;
import com.appspot.cloudserviceapi.sci.dao.ServiceRegistryDAO;
//import com.appspot.cloudserviceapi.test.CodesionSVNTest;
//import com.newatlanta.commons.vfs.provider.gae.GaeVFS;

@SuppressWarnings("serial")
public class ServiceRegistryServlet extends HttpServlet {

	public void init(ServletConfig config) throws ServletException {
//	    GaeVFS.setRootPath(config.getServletContext().getRealPath("/"));
	}
	
	public void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		doPost(request, response);
	}

	public void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {

		response.setContentType("text/html");

		String serviceName = request.getParameter("s");
		ServiceRegistryDAO r = new ServiceRegistryDAO();
		ServiceRegistry sr = null;

		//=== support Rails-style edit via URL
		String editFlag = request.getParameter("edit");		//edit the service only
		if(editFlag != null) {
			if(serviceName != null) {
				long id = -1;
				sr = (ServiceRegistry)r.findServiceRegistryByService(serviceName);
				if(sr != null) {
					id = sr.getId();
					//=== forward it to edit page
					String url = Constants.SR_URI + "/serviceregistrysave/" + id;
					response.sendRedirect(url);
					return;	//I am done :)
				}
			}
		}
		
		//=== if move pass beyond this, it is business as usual pal!
		
		String resp = null;
		if(serviceName != null) {
			try {
				if(sr == null) sr = (ServiceRegistry)r.findServiceRegistryByService(serviceName);
				if(sr == null) {
					resp = "<html>Sorry, no such service [" + serviceName + "] found in the registry.</html>";
					response.getWriter().println(resp);
				}
				//TODO - this could be a target for the (future's) Java closure :)
				//=== BT0001 begin - handling "legacy" bigtable column (does not exist before the new changes)
				boolean disabled = false, useDescription = false, useHTML = true;
				try {
					if(sr.getDisabled()) {
						disabled = true;
					}
				} catch (Exception e) {
					System.out.println("legacy entity without disabled flag: " + sr);
				}
				try {
					if(sr.getUseDescription()) {
						useDescription = true;
					}
				} catch (Exception e) {
					System.out.println("legacy entity without useDescription flag: " + sr);
				}
				try {
					if(!sr.getUseHtml()) {
						useHTML = false;
					}
				} catch (Exception e) {
					System.out.println("legacy entity without useHTML flag: " + sr);
				}
				//=== BT0001 end - handling "legacy" bigtable column (does not exist before the new changes)
				if(sr != null 
						&& !disabled
						) {
					System.out.println("registry request id '" + serviceName + "' found '" + sr.getEndpoint() + "'");

					//=== handle special token
					ServiceRegistry firstRedirectedSR = null;
					String endPoint = sr.getEndpoint();
					if(!useDescription && !ServiceRegistryUtil.isRedirectedEndPoint(endPoint) && isUrl(endPoint)) {
						response.sendRedirect(endPoint);
					} else 
					if(!useDescription && ServiceRegistryUtil.isRedirectedEndPoint(endPoint)) {
						String originalEndPoint = endPoint;
						//String finalEndPoint = ServiceRegistryUtil.handleEndPoint(endPoint, r);		//costly if multiple levels are specified!!!
						firstRedirectedSR = ServiceRegistryUtil.getServiceRegistry(endPoint.trim().substring(2, endPoint.trim().length()-1), r);		//get only the first level redirection
						String finalEndPoint = firstRedirectedSR.getEndpoint();
						boolean disabled1 = false, useDescription1 = false, useHTML1 = true;
						try {
							if(firstRedirectedSR.getUseDescription()) {
								useDescription1 = true;
							}
						} catch (Exception e) {
							System.out.println("legacy entity level 1 without useDescription flag: " + sr);
						}
						//=== begin - support getting the content of the redirected service!!!
						if(!isUrl(finalEndPoint) && !useDescription1) {
							resp = firstRedirectedSR.getEndpoint();	//returns the content of the endpoint (which is not supposed to be an URL)
							response.getWriter().print(resp.trim());
						} else
						//it is a redirection
						if(isUrl(finalEndPoint) && !useDescription1) {
							response.sendRedirect(finalEndPoint);
						} else {
							resp = firstRedirectedSR.getDescription();	//returns the description content
							response.getWriter().print(resp.trim());
						}
						//=== end - support getting the content of the redirected service!!!
					} else
					if(useDescription) {
//						String description = HTMLUtil.handleText(u.getDescription());
						String description = sr.getDescription();
//						description = ServiceRegistryUtil.handleEndPoint(description, r);
						description = sr.getDescription();
						
//						if(!isUrl(endPoint)) {
//							endPoint = "";	//not a hyperlink, excluded and also good for security reason (endpoint could be a password)
//						}
						if(useHTML) {
							response.setContentType("text/html");
						} else {
//							resp = endPoint + System.getProperty("line.separator") + description;
						}
//						resp = description.substring(1, description.length());	//TBD - temporary solution, until the char is known
						//resp = StringUtil.safeString(description);
						if(request.getParameter("xray") != null) {
							resp = StringUtil.toASCIICode(description);
						} if(request.getParameter("xml") != null) {
							resp = description.substring(1, description.length());	//TBD - temporary solution, until the char is filtered properly
//							resp = description.replaceAll("\u00A0", "");	//remove control character 160 10 in XML for ckeditor
//							resp = description.trim();
						} else {
							resp = description.trim();
						}
						if(resp != null) {
							response.getWriter().print(resp.trim());
						}
					} else {
						if(isUrl(endPoint)) {
							response.sendRedirect(endPoint);
						} else {
							resp = endPoint;	//just returns the content
							response.getWriter().print(resp.trim());
						}
					}
					ServiceRegistryUtil.countHit(sr, r, request);
				}
				else if(sr != null && disabled) {
					resp = "<html>Sorry, the service [" + serviceName + "] is currently disabled. Please contact the administrator for details.</html>";
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
			resp = "<html>Service must be specified with request parameter \"s\". [" + serviceName + "]</html>";
			response.getWriter().println(resp);
		}
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