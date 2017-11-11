package com.appspot.cloudserviceapi.sci.fios;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.cloudserviceapi.sci.dao.FiOSTokenDAO;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import tapp.model.sci.FiOSToken;

@SuppressWarnings("serial")
public class CalendarAuthenticationServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		Long id = -1L;
		Long passcode = -1L;
		FiOSToken token = null;
		try {
			id = Long.parseLong((getValue(req, "profileid")));
			passcode = Long.parseLong((getValue(req, "passcode")));
			token = new FiOSToken();
			token.setId(id);
			token.setPasscode(passcode);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if(token != null && (new FiOSTokenDAO()).isValid(token)) {
			resp.getWriter().print("ok");
		} else {
			resp.getWriter().print("sorry");
		}
		
/*		if (user != null && !"".equals(user.getEmail().trim())) {
			try {
				req.getSession().setAttribute("email", user.getEmail());
				resp.sendRedirect("/"+SettingsDBUtils.getSettings("1.alias.company")+"/fiosregistrationstart");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
		}
*/	}

	private String getValue(HttpServletRequest req, String reqName) {
		String retVal = null;
		
		if(req.getParameter(reqName) == null) {
			retVal = "";
		} else {
			retVal = req.getParameter(reqName);
		}

		return retVal;
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doGet(req, resp);
	}
}
