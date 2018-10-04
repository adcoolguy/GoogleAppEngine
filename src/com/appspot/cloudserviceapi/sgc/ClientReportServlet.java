package com.appspot.cloudserviceapi.sgc;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.cloudserviceapi.common.model.Constants;
import com.appspot.cloudserviceapi.common.model.TimeUtil;
import com.appspot.cloudserviceapi.sgc.dao.WorkOrderDAO;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import tapp.model.sgc.WorkOrder;

@SuppressWarnings("serial")
public class ClientReportServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		if (user != null) {
			resp.setContentType("text/html");
			resp.getWriter().println("<html>");
			resp.getWriter().println("  <head>");
			resp.getWriter().println("    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
			resp.getWriter().println("    <title>Client Report</title>");
			resp.getWriter().println("    <script>var parameter = '#';</script>");
			resp.getWriter().println("  </head>");
			resp.getWriter().println("  <body>");
			resp.getWriter().println("  <div data-role=\"page\" data-theme=\"a\">");
			resp.getWriter().println("  <div data-role=\"header\" data-nobackbtn=\"true\">");
			resp.getWriter().println("  <a href=\"" + Constants.MAIN_URL + "\" data-icon=\"back\">" + req.getSession().getAttribute("data-icon-back") + "</a>");
			resp.getWriter().println("  <h1>Client Report</h1>");
			resp.getWriter().println("  </div>");
			resp.getWriter().println("  <div data-role=\"content\" data-theme=\"a\">");
			String phone = (String) req.getSession().getAttribute("phone");
			if (phone != null) {
				List<WorkOrder> results = (new WorkOrderDAO()).getCloneList();
				int count = 0;
				if (results != null && results.iterator() != null
						&& results.iterator().hasNext()) {
					resp.getWriter().println(
							"Your service history with us so far based on phone number '"
									+ phone + "' is as the following -<br>");
					resp.getWriter().println("<br>");
					for (WorkOrder e : results) {
						resp
								.getWriter()
								.println(
										">>>>>> Service Request ["
												+ ++count
												+ "]: Submitted by '"
												+ e.getUpdatedBy()
												+ "' on "
												+ TimeUtil.getHQDateTime(e
														.getUpdatedDate())
												+ " for cleaning service to be performed on "
												+ TimeUtil.getHQDateTime(e
														.getDateRequested())
												+ ". Date served '"
												+ TimeUtil.getHQDateTime(e
														.getDatePerformed())
												+ "'.");
						resp.getWriter()
								.println("<br>" + e.toString() + "<br>");
					}
				} else {
					resp.getWriter().println(
							"You do not have any work order previously.");
				}
			} else {
				resp
						.getWriter()
						.println(
								"You have not requested any service before or entered a valid phone number to see the service history.");
			}
			
			resp.getWriter().println("  </div>");
			resp.getWriter().println("  </div>");
			resp.getWriter().println("  </body>");
			resp.getWriter().println("</html>");			
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
