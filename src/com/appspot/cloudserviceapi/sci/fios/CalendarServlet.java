package com.appspot.cloudserviceapi.sci.fios;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.cloudserviceapi.sci.dao.FiOSTokenDAO;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.ServiceException;

import tapp.model.sci.FiOSToken;

/**
 * More complete implementation can be based on
 * http://syncnotes2google.googlecode
 * .com/svn-history/r34/trunk/syncnotes2google/
 * src/com/googlecode/syncnotes2google/dao/GoogleCalendarDAO.java.
 * 
 */
public class CalendarServlet extends HttpServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CalendarServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	private String handleString(String str) {
		String retVal = str;

		if (str != null) {
			str = str.trim();
		}

		return str;
	}

	private String handleDateStr(DateTime dt1, DateTime dt2) {
		String str = dt1.toUiString();
		if (dt2 != null) {
			str = str + ".. ";
		}
		return str;
	}

	private String handleTitleStr(String title) {
		String str = title;
		try {
			if (title != null && title.length() > 24) {
				str = title.substring(0, 23);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String temp = null;
		PrintWriter out = response.getWriter();
		StringBuffer sb = new StringBuffer("");

		try {
			System.out.println(request.getParameter("time"));
			DateFormat dfGoogle = new SimpleDateFormat("yyyy-MM-dd'T HH:mm:ss'");
			CalendarService myService = new CalendarService("corp1-calEvent-1");

			System.out.println("Content type------------"
					+ myService.getContentType());

			// URL("http://www.google.com/calendar/feeds/user.corp2.fios@gmail.com/private/full");

			String profileid = handleString(request.getParameter("profileid"));
			String passcode = handleString(request.getParameter("passcode"));

			FiOSToken token = (new FiOSTokenDAO()).get(profileid);

			if (token != null && profileid != null && passcode != null
					&& !"".equals(profileid) && !"".equals(passcode)) {
				String email = token.getUserId();
				String magicCookie = token.getMagicKey();

				if (token.getGeneratedPasscode().equals(new Long(passcode))) {

					String pmcUrl = "http://www.google.com/calendar/feeds/"
							+ URLEncoder.encode(email) + "/private-"
							+ magicCookie + "/full";
					System.out.println(pmcUrl);

					URL feedUrl = new URL(pmcUrl);

					// Send the request and receive the response:
					CalendarEventFeed myFeed = myService.getFeed(feedUrl,
							CalendarEventFeed.class);

					System.out.println("after get feed----"
							+ myFeed.getEntries().size());
					if (myFeed != null && myFeed.getEntries() != null
							&& myFeed.getEntries().size() > 0) {
						for (int i = 0; i < myFeed.getEntries().size(); i++) {
							CalendarEventEntry entry = (CalendarEventEntry) myFeed
									.getEntries().get(i);

							String myEntryTitle = entry.getTitle()
									.getPlainText();
							DateTime dt1 = null;
							DateTime dt2 = null;
							List<When> x = entry.getTimes();
							Iterator<When> itr = x.iterator();
							When d = null;
							Date date = null;
							Date eDate = null;
							String dateStr = null;
							if (itr.hasNext()) {
								d = itr.next();
								dt1 = d.getStartTime();
								dt2 = d.getEndTime();
							}
							dateStr = dt1 == null ? "**Recurring Event**"
									: handleDateStr(dt1, dt2);

							System.out.println(entry.getReminder());

							// System.out.println("myEntryTitle----"+myEntryTitle);
							System.out.println("\t" + myEntryTitle);
							// String dateStr = (dt ==
							// null?"**Recurring Event**":DateFormat.getDateTimeInstance(
							// DateFormat.SHORT, DateFormat.SHORT).format(
							// new Date(dt.getValue())));
							temp = dateStr + " " + handleTitleStr(myEntryTitle)
									+ "\n";
							sb.append(temp);
						}
					}
				}
			}

		} catch (ServiceException e) {
			e.printStackTrace();
		} finally {
			if("".equals(sb.toString().trim())) {
				sb.append("empty");
			}

			response.setContentLength(sb.length());
			out.println(sb.toString());			
		}
	}
}
