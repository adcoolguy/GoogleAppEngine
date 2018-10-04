package com.appspot.cloudserviceapi.sci.fios;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.cloudserviceapi.common.SettingsDBUtils;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class CalendarRegistrationServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		if (user != null && !"".equals(user.getEmail().trim())) {
			try {
				req.getSession().setAttribute("referer", req.getHeader("referer"));
				System.out.println("setting referer " + req.getSession().getAttribute("referer"));
				req.getSession().setAttribute("email", user.getEmail());
				resp.sendRedirect("/"+SettingsDBUtils.getSettings("1.alias.company")+"/tokenregistrationstart");
			} catch (Exception e) {
				e.printStackTrace();
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
