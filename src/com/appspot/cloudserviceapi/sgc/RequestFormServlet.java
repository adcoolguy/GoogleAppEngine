package com.appspot.cloudserviceapi.sgc;

import static com.appspot.cloudserviceapi.common.StringUtil.handleNumber;
import static com.appspot.cloudserviceapi.common.StringUtil.handleYesNo;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.cloudserviceapi.common.Constants;
import com.appspot.cloudserviceapi.common.TimeUtil;
import com.appspot.cloudserviceapi.sgc.dao.WorkOrderDAO;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import tapp.model.sgc.WorkOrder;

@SuppressWarnings("serial")
public class RequestFormServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		resp.setContentType("text/html");

		String email = null;
		if (user != null) {
			email = user.getEmail();
			req.setAttribute("userID", email);
			System.out.println("in request form servlet userID = "
					+ user.getEmail());
		} else {
			req.setAttribute("userID", Constants.NOT_LOGGED_IN_TITLE);
		}

		// populate the form if it is accessed directly from URL (not from a
		// form POST)
		if (getValue(req, "formID") == null
				|| "".equals(getValue(req, "formID"))) {
			if (user != null) {
				long id = req.getSession().getAttribute("phone") != null ? Long
						.valueOf((String) req.getSession()
								.getAttribute("phone")) : -1;
				if (id > -1) {
					WorkOrder wo = (new WorkOrderDAO()).get(String.valueOf(id));
					if (wo != null) {
						req.getSession().setAttribute("wo", wo);
					}
				}
			}

			try {
				req.getRequestDispatcher("/sgc/jsp/request-form.jsp").forward(
						req, resp);
			} catch (ServletException e) {
				e.printStackTrace();
			}
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

		String userID = null;
		if (user != null) {
			userID = user.getEmail();
		}

		String phone = getValue(req, "q8_phoneNumber8[area]")
				+ getValue(req, "q8_phoneNumber8[phone]");
		Long pk = Long.valueOf(phone);
		if (pk == 0) {
			resp.getWriter().println(
					"<a href=\"javascript:history.go(-1)\">Back to what I've entered</a><br>");
			resp
					.getWriter()
					.println(
							"Your phone number must be a valid number and can not be zero!");
		} else {

			req.getSession().setAttribute("phone", phone);
			String fname = getValue(req, "q3_firstName3");
			String lname = getValue(req, "q4_lastName");
			String email = getValue(req, "q6_email6");
			String address1 = getValue(req, "q7_address[addr_line1]");
			String address2 = getValue(req, "q7_address[addr_line2]");
			String city = getValue(req, "q7_address[city]");
			String state = getValue(req, "q7_address[state]");
			String postal = getValue(req, "q7_address[postal]");
			String country = getValue(req, "q7_address[country]");

			int familySize = handleNumber(getValue(req, "q26_familySize26"));
			int childSize = handleNumber(getValue(req, "q27_children"));
			String childInfo = getValue(req, "q33_childrenAges33");
			String hasPet = getValue(req, "q41_pets41");
			String pet1 = getValue(req, "q38_pet1");
			String pet2 = getValue(req, "q39_pet239");
			String pet3 = getValue(req, "q40_pet3");
			String petComments = getValue(req, "q57_comments");
			String hasAllergy = getValue(req, "q34_allergies");
			String allergyInfo = getValue(req, "q42_ifYes42");
			String residenceType = getValue(req, "q14_residenceType");
			int bedRoom = handleNumber(getValue(req, "q20_Bed"));
			int bathRoom = handleNumber(getValue(req, "q19_Bath"));
			String room1 = getValue(req, "q47_room1");
			String room2 = getValue(req, "q48_room2");
			String room3 = getValue(req, "q49_room3");
			String primeReason = getValue(req, "q51_primaryReason");
			String primeComments = getValue(req, "q53_comments53");

			String addService = getValue(req, "q55_55[]");
			String addServiceComments = getValue(req, "q56_comments56");
			String specialInstruction = getValue(req,
					"q25_specialInstruction25");

			System.out
					.println("Received cleaning request based on phone number "
							+ phone + ".  ");

			WorkOrder wo = new WorkOrder();
			wo.setLastName(fname);
			wo.setFirstName(lname);
			wo.setEmail(email);
			wo.setAddress1(address1);
			wo.setAddress2(address2);
			wo.setCity(city);
			wo.setState(state);
			wo.setPostal(postal);
			wo.setCountry(country);
			if (getValue(req, "q9_preferredDay9[day]") != null) {
				String dateRequested = getValue(req, "q9_preferredDay9[month]")
						+ " " + getValue(req, "q9_preferredDay9[day]") + " "
						+ getValue(req, "q9_preferredDay9[year]") + " "
						+ getValue(req, "q9_preferredDay9[hour]") + " "
						+ getValue(req, "q9_preferredDay9[min]") + " "
						+ getValue(req, "q9_preferredDay9[ampm]");
				Date date = null;
				String hour = getValue(req, "q9_preferredDay9[hour]");
				if ("PM".equalsIgnoreCase(getValue(req,
						"q9_preferredDay9[ampm]"))) {
					hour = String.valueOf(Integer.parseInt(hour) + 12);
				}
				date = TimeUtil.toDateWithZone(getValue(req,
						"q9_preferredDay9[year]"), getValue(req,
						"q9_preferredDay9[month]"), getValue(req,
						"q9_preferredDay9[day]"), hour, getValue(req,
						"q9_preferredDay9[min]"), "EST5EDT");

				wo.setDateRequested(date);
			}
			wo.setFamilySize(familySize);
			wo.setChildren(childSize);
			wo.setChildrenInfo(childInfo);
			wo.setHasPet(handleYesNo(hasPet));
			wo.setPetInfo(pet1 + ", " + pet2 + ", " + pet3);
			wo.setPetComments(petComments);
			wo.setAllergic(handleYesNo(hasAllergy));
			wo.setAllergicInfo(allergyInfo);
			wo.setResidenceType(residenceType);
			wo.setBedRoom(bedRoom);
			wo.setBathRoom(bathRoom);
			wo.setRoom1Info(room1);
			wo.setRoom2Info(room2);
			wo.setRoom3Info(room3);
			wo.setPrimaryCleaningReason(primeReason);
			wo.setPrimaryCleaningComments(primeComments);
			wo.setAddtionalService1(addService);
			wo.setAddtionalServiceComments(addServiceComments);
			wo.setSpecialInstruction(specialInstruction);
			wo.setUpdatedBy(userID);
			wo.setUpdatedDate(new Date());
			wo.setPhone(phone);
			// use phone as the primary key for now
			// wo.setId(phone);
			wo.setId(Long.valueOf(phone).longValue());
			try {
				(new WorkOrderDAO()).save(wo);
				System.out.println("work order saved");
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (user != null) {
				if (wo != null) {
					resp.getWriter().println(
							user.getNickname()
									+ ", thank you for your business.<br>");
					resp.getWriter().println("");
					// Date dateRequested_EST = null;
					// if(wo.getDateRequested() != null) {
					// dateRequested_EST =
					// TimeUtil.toDateWithZone(wo.getDateRequested().getYear(),
					// wo.getDateRequested().getMonth(),
					// wo.getDateRequested().getDay(),
					// wo.getDateRequested().getHours(),
					// wo.getDateRequested().getMinutes(), "EST");
					// }
					resp.getWriter().println(
							"You have requested a cleaning service to be performed on "
									+ TimeUtil.getHQDateTime(wo
											.getDateRequested()) + ".");
					resp.getWriter().println("");
					resp
							.getWriter()
							.println(
									"Local HQ time is "
											+ TimeUtil
													.getHQDateTime(new Date())
											+ ". Someone will get back to you in less than 2 business days to confirm the date and to go over the details of the service.<br>");
					resp.getWriter().println("");
					resp.getWriter().println("Work order has been created for you based on the phone number '" + phone + "'. Please use the phone number for any reference to the cleaning service.");

				} else {
					resp.getWriter().println("Hi " + user.getNickname() + "!");
				}
			} else {
				resp.sendRedirect(userService.createLoginURL(req
						.getRequestURI()));
			}
		}
	}
}
