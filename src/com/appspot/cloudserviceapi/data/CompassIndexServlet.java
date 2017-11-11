package com.appspot.cloudserviceapi.data;

/**
 * http://stackoverflow.com/questions/108822/delete-all-data-for-a-kind-in-google-app-engine
 */
//import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.url;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.cloudserviceapi.util.Compass;

@SuppressWarnings("serial")
public class CompassIndexServlet extends HttpServlet {

	private long count = 1;

	public void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {

		request.getSession().setAttribute("cis_count", count);
		response.setContentType("text/plain");
		String reqClass = request.getParameter("targetClass");
		System.out.println("targetClass = '" + reqClass + "'");
		//=== whitelist the input
		try {
			if ("com.appspot.cloudserviceapi.guestbook.Greeting"
					.equals(reqClass)) {
				Compass.index(com.appspot.cloudserviceapi.guestbook.Greeting.class);
			} else if ("com.appspot.cloudserviceapi.dto.Huma"
					.equals(reqClass)) {
				Compass.index(com.appspot.cloudserviceapi.dto.Huma.class);
			} else if ("com.appspot.cloudserviceapi.dto.Geniu"
					.equals(reqClass)) {
				Compass.index(com.appspot.cloudserviceapi.dto.Geniu.class);
			} else if ("com.appspot.cloudserviceapi.dto.Secure"
					.equals(reqClass)) {
				Compass.index(com.appspot.cloudserviceapi.dto.Secure.class);
			} else if ("tapp.model.ServiceRegistry".equals(reqClass)) {
				Compass.index(tapp.model.ServiceRegistry.class);
			} else if ("tapp.model.sci.FiOSToken".equals(reqClass)) {
				Compass.index(tapp.model.sci.FiOSToken.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}