package cloudserviceapi.app.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.appspot.cloudserviceapi.common.TimeUtil;

import app.model.Calendar;

public class CalendarHelper {

	private boolean enforceYearCheck = true; // maximum events per request check

	public static String getValue(HttpServletRequest req, String reqName) {
		return req.getParameter(reqName);
	}

	public void createMonthlyEvent(Date startDate, Date endDate) {

	}

	public void createWeeklyEvent(Date startDate, Date endDate) {

	}

	public void createDailyEvent(Date startDate, Date endDate) {

	}

	public Calendar createItem(HttpServletRequest request, String start,
			String end) {
		Calendar item = new Calendar();
		item.setName(getValue(request, "name"));
		item.setDescription(getValue(request, "description"));
		item.setColor(getValue(request, "color"));
		item.setCreatedDate(new Date());
		item.setModifiedDate(new Date());
		// view specific attributes
		item.setTitle(getValue(request, "title"));
		item.setUrl(getValue(request, "url"));
		String temp = start; // getValue(request, "start");
		if (temp != null && !temp.equals("")) {
			try {
				DateTime time = CommonHandler.getChannelDateTime(temp);
				item.setStart(temp);
				item.setStartDate(time.toDate());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.println(this.getClass().getName() + ": " + e);
				item.setStartDate(new Date(Long.parseLong(temp)));
			}
		}
		temp = end; // getValue(request, "end");
		if (temp != null && !temp.equals("")) {
			try {
				DateTimeFormatter format = DateTimeFormat
						.forPattern("MM/dd/yyyy HH:mm");
				DateTime time = format.parseDateTime(temp);
				item.setEnd(temp);
				item.setEndDate(new Date());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.println(this.getClass().getName() + ": " + e);
				item.setEndDate(new Date(Long.parseLong(temp)));
			}
		}
		try {
			item.setAllDay(Boolean.valueOf(getValue(request, "allDay")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println(this.getClass().getName() + ": "
					+ "Exception during processing of allDay flag: " + e);
		}
		item.setUrl(getValue(request, "url"));

		return item;
	}

	private boolean isValidRequest(String requestType) {
		boolean retVal = false;

		long days = 1;
		Date today = new Date(DateTimeUtils.currentTimeMillis());
		long millis = 86400000; // a day
		DateTimeUtils.setCurrentMillisOffset(millis * days); // set offset based
																// on days count
		Date dateLater = new Date(DateTimeUtils.currentTimeMillis());
		long daysPassed = TimeUtil.calculateDays(today, dateLater);

		if (daysPassed < 367) {
			retVal = true;
		}
		return retVal;
	}

	public static String getEventPattern(Date startDate) {
		LocalDateTime localDate = new LocalDateTime(startDate);
		String temp = localDate.getYear() + "," + (localDate.getMonthOfYear()-1) + "," + localDate.getDayOfMonth() + "," + localDate.getHourOfDay() + "," + localDate.getMinuteOfHour() + "," + localDate.getSecondOfMinute();
		return temp;
	}

	public static void main(String[] args) {
		DateTimeFormatter format = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");

		DateTime dateTime = format.parseDateTime("7/04/2013 15:10");
		Instant toAdd = dateTime.toInstant();
		Instant answer = toAdd.toDateTime().plusDays(1).toInstant();

		System.out.println(answer.toString(format)); //07/05/2013 19:10
	}
}
