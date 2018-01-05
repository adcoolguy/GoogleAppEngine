package com.appspot.cloudserviceapi.sgc;

import static com.appspot.cloudserviceapi.common.model.StringUtil.handleNumber;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.cloudserviceapi.common.model.Constants;
import com.appspot.cloudserviceapi.common.model.StringUtil;
import com.appspot.cloudserviceapi.sgc.dao.EmployeeDAO;
import com.appspot.cloudserviceapi.sgc.dao.WorkOrderDAO;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import tapp.model.sgc.WorkOrder;

@SuppressWarnings("serial")
public class WorkFormServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		resp.setContentType("text/html");

		String email = null;
		String nickName = null;
		if (user != null) {
			email = user.getEmail();
			nickName = user.getNickname();
			req.setAttribute("userID", email);
			System.out.println("in work form servlet userID = "
					+ user.getEmail());
		} else {
			req.setAttribute("userID", Constants.NOT_LOGGED_IN_TITLE);
		}

		try {
			req.getRequestDispatcher("/sgc/jsp/work-form.jsp").forward(req,
					resp);
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}

	private String getValue(HttpServletRequest req, String reqName) {
		return req.getParameter(reqName);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		resp.setContentType("text/html");
		resp.getWriter().println("<html>");
		resp.getWriter().println("  <head>");
		resp.getWriter().println("    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
		resp.getWriter().println("    <title>Work Completion Form</title>");
		resp.getWriter().println("  </head>");
		resp.getWriter().println("  <body>");
		resp.getWriter().println("  <div data-role=\"page\" data-theme=\"a\">");
		resp.getWriter().println("  <div data-role=\"header\">");
		resp.getWriter().println("  <h1>Work Completion Form</h1>");
		resp.getWriter().println("  </div>");
		resp.getWriter().println("  <div data-role=\"content\" data-theme=\"a\">");

		EmployeeDAO emp = new EmployeeDAO();
		// has to check if the user is employee and allow or disallow

		if (user == null) {
			resp.getWriter().println(
					"<a href=\"javascript:history.go(-1)\">Back to what I've entered</a><br>");
			resp
					.getWriter()
					.println(
							"You can not submit work order without signing in. Please login as an employee and submit again.");
		} else if (!StringUtil.isNumber(getValue(req, "q10_employeeId10"))) {
			resp.getWriter().println(
					"<a href=\"javascript:history.go(-1)\">Back to what I've entered</a><br>");
			resp
					.getWriter()
					.println(
							"Employee id must be a number. Please try to enter a valid employee id and submit again.");
		} else if (getValue(req, "q10_employeeId10") != null
				&& !emp.isEmployee(getValue(req, "q10_employeeId10"))) {
			resp.getWriter().println(
					"<a href=\"javascript:history.go(-1)\">Back to what I've entered</a><br>");
			resp
					.getWriter()
					.println(
							"Employee id entered '"
									+ getValue(req, "q10_employeeId10")
									+ "' is not valid. Please try to enter a valid employee id and submit again.");

		} else if (!emp.isEmployeeIdUserIdMatched(StringUtil
				.handleLongNumber(getValue(req, "q10_employeeId10")), user
				.getEmail())) {
			resp.getWriter().println(
					"<a href=\"javascript:history.go(-1)\">Back to what I've entered</a><br>");
			resp
					.getWriter()
					.println(
							"Employee id entered '"
									+ getValue(req, "q10_employeeId10")
									+ "' does not match the user id '"
									+ user.getEmail()
									+ "'. Please try to enter a matching employee id/user id and submit again.");

		} else if (getValue(req, "q10_employeeId10") != null
				&& !emp.isUserIdEmployee(user.getEmail())) {

			resp.getWriter().println(
					"<a href=\"javascript:history.go(-1)\">Back to what I've entered</a><br>");
			resp
					.getWriter()
					.println(
							"User id '"
									+ user.getEmail()
									+ "' is not an employee id. Please try to login as employee and submit again.");
		} else {

			String phone = getValue(req, "q2_clientPhone")
					+ getValue(req, "q11_clientPhone11");
			String task = getValue(req, "q9_tasks[]");
			int hours = handleNumber(getValue(req, "q4_hoursSpent4"));

			System.out.println("updating cleaning request based on phone "
					+ phone);

			WorkOrder wo = (new WorkOrderDAO()).get(phone);
			// MockupWorkOrder wo = new MockupWorkOrder();

			String email = null;
			String nickName = null;
			if (user != null) {
				email = user.getEmail();
				nickName = user.getNickname();
			}
			if (wo == null) {
				resp.getWriter().println(
						"<a href=\"javascript:history.go(-1)\">Back to what I've entered</a><br>");
				resp.getWriter().println(
						"No work order found based on the phone number '"
								+ phone + "', please try again.");
			} else {
				try {
					WorkOrder newWo = (WorkOrder) wo.clone();
					newWo.setDatePerformed(new Date());
					newWo.setUpdatedDate(new Date());
					newWo.setUpdatedBy(email);
					newWo.setCompletedNote("Performed - " + task);
					newWo.setHoursSpent(hours);
					(new WorkOrderDAO()).save(newWo);
					resp
							.getWriter()
							.println(
									"Dear "
											+ nickName
											+ ", thank you for your work completion checklist submission.<br>");
					resp.getWriter().println(
							"Your work order based on the phone number '" + phone
									+ "' has been updated successfully.");
				} catch (Exception e) {
					e.printStackTrace();
					resp.getWriter().println(e);
				}

//				resp.getWriter().println("<p>");
				resp.getWriter().println("  </div>");
				resp.getWriter().println("  </div>");
				resp.getWriter().println("  </body>");
				resp.getWriter().println("</html>");			
			}
		}
	}
}
