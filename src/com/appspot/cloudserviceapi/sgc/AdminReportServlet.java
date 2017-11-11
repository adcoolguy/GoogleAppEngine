package com.appspot.cloudserviceapi.sgc;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.cloudserviceapi.common.Constants;
import com.appspot.cloudserviceapi.sgc.dao.WorkOrderDAO;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import tapp.model.sgc.WorkOrder;

@SuppressWarnings("serial")
public class AdminReportServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		if (user != null) {
			resp.setContentType("text/html");
			if(Protect.isAdmin()) {
	            List<WorkOrder> results = (new WorkOrderDAO()).getCloneList();
	            int count = 0;
	            if (results.iterator().hasNext()) {
	                for (WorkOrder e : results) {
	                	resp.getWriter().print("order #" + ++count + "# ID '" + e.getId() + "' Phone '" + e.getPhone() + "' First Name '" + e.getFirstName() + "', Last '" + e.getLastName() + "' Requested " + e.getDateRequested() + " Served " + e.getDatePerformed() + " ");
	    				resp.getWriter().println(e.toString() + "<br>");
	                }
	            } else {
	            	resp.getWriter().println("No work order");
	            }
			} else {
				resp.getWriter().println(Constants.NO_RIGHT_TO + "access the page.");	//TODO, in production might not even want to respond to prevent hacking!
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
