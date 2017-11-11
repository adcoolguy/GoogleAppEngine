package com.appspot.cloudserviceapi.common;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.protocol.HttpContext;

/**
 * Source:
 * http://stackoverflow.com/questions/3420767/httpclient-redirecting-to-url
 * -with-spaces-throwing-exception
 * 
 */
public class BigTimeRedirectHandler extends DefaultRedirectStrategy {

	private static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";
	private boolean redirected;

	@Override
	public boolean isRedirected(HttpRequest hr, HttpResponse hr1, HttpContext hc) {
		if (hr1.getStatusLine().getStatusCode() == 302) {
			redirected = true;
		} else {
			redirected = false;
		}
		System.out.println("BigTimeTest:fetch:isRedirected: " + redirected);
		return redirected;
	}

//	@Override
	public HttpUriRequest getRedirect1(HttpRequest request,
			HttpResponse response, HttpContext context) {
		HttpGet get = null;
		try {

			String newLocation = "" + response.getFirstHeader("Location");
			System.out.println("BigTimeTest:fetch:getRedirect: newLocation 0 "
					+ newLocation);
			newLocation = newLocation.substring(newLocation
					.indexOf("Location:") + 10);
			System.out.println("BigTimeTest:fetch:getRedirect: newLocation 1 "
					+ newLocation);
			newLocation = "http://"+ SettingsDBUtils.getSettings("main.bigtime.host") + "/BigTime/" + newLocation;
//			if (redirected) {
				get = new HttpGet(newLocation);
				System.out.println("BigTimeTest:fetch:getRedirect: newLocation 2 "
						+ newLocation);
//			} else {
//				get = new HttpGet(newLocation);
//			}
		} catch (Exception ex) {
			System.out.println(ex.getLocalizedMessage());
		}
		return get;
	}

}