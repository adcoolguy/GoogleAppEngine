package com.appspot.cloudserviceapi.sgc;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.cloudserviceapi.common.Constants;
import com.appspot.cloudserviceapi.data.Persistence;
import com.appspot.cloudserviceapi.security.spring.model.UserSecurityDAO;
import com.appspot.cloudserviceapi.sgc.dao.ClientDAO;
import com.appspot.cloudserviceapi.sgc.dao.EmployeeDAO;
import com.appspot.cloudserviceapi.sgc.dao.GreetingDAO;
import com.appspot.cloudserviceapi.sgc.dao.WorkOrderDAO;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class AdminActionServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		if (user != null) {
			resp.setContentType("text/html");
			if (Protect.isAdmin()) {
				if ("purgeGreeting".equals(getValue(req, "action"))) {
					(new GreetingDAO()).purgeGreeting();
					resp.getWriter().println("Greeting purged!<br>");
				} else if ("purgeUser".equals(getValue(req, "action"))) {
					(new UserSecurityDAO()).purgeUser();
					resp.getWriter().println("User purged!<br>");
				} else if ("purgeClient".equals(getValue(req, "action"))) {
					(new ClientDAO()).purgeClient();
					resp.getWriter().println("Client purged!<br>");
				} else if ("purgeEmployee".equals(getValue(req, "action"))) {
					(new EmployeeDAO()).purgeEmployee();
					resp.getWriter().println("Employee purged!<br>");
				} else if ("purgeWorkOrder".equals(getValue(req, "action"))) {
					(new WorkOrderDAO()).purgeWorkOrder();
					resp.getWriter().println("Work order purged!<br>");
				} else if ("setTimeZone".equals(getValue(req, "action"))) {
					String timeZone = getValue(req, "value");
					if (timeZone == null) {
						timeZone = "EST";
					}
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss z");
					sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
					resp
							.getWriter()
							.println(
									"Time zone changed to "
											+ timeZone
											+ ".  Please note that this affects only the date view of the application and not the internal date format (of GAE).");
				} else if ("nuke".equals(getValue(req, "action"))) {
					Persistence.nuke();
				} else {
					resp.getWriter().println("Thank you");
				}
			} else {
				resp.getWriter().println(
						Constants.NO_RIGHT_TO + "access the page."); // TODO,
				// in
				// production
				// might
				// not
				// even
				// want
				// to
				// respond
				// to
				// prevent
				// hacking!
			}
		} else {
			resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
		}
	}

	private String getValue(HttpServletRequest req, String reqName) {
		return req.getParameter(reqName);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doGet(req, resp);
	}
}
