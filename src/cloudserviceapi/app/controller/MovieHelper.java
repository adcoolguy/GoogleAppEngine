package cloudserviceapi.app.controller;

import java.util.Collection;
import java.util.Iterator;

import org.joda.time.DateTime;

import com.appspot.cloudserviceapi.common.model.JsonUtil;
import com.google.api.server.spi.response.CollectionResponse;

import app.common.Constants;
import app.model.Calendar;
import app.model.Movie;
import app.model.User;

public class MovieHelper {
	
	/**
	 * Get all items related to a logged in user and update the movie based on the "channel shifting" business logic.
	 * @param calendarHandler 
	 * 
	 * #perf top 1 (10-17-2013) sorted by desc avg time = 2211 ms
	 */
	public static String handleChannelPattern(String uid, CalendarHandler calendarHandler) throws Exception {
		CollectionResponse<Movie> cr = null;
		Collection<Movie> l;
		String retVal = "";
		
		try {
			User u = UserHandler.getUser(uid);
			if(u != null) {
				l = u.getMovie();
				Iterator<Movie> it = l.iterator();
				Movie m = null;
				Calendar c = null;
				while(it.hasNext()) {
					m = it.next();
					m.setId(m.getKey().getId());	//GAEJ specific
					
					//begin business logic - must have channel pattern setup in CRUD UI as well as scheduled in calendar UI 
					if(m != null && m.getChannelPattern() != null && m.getChannelPattern().equalsIgnoreCase(Constants.CH_DAILY) && m.getCalendarId() != null) {
						//=== get its scheduled time
						c = (Calendar) calendarHandler.doGetItem(m.getCalendarId());
							if(c != null && c.getStartDate() != null) {
							DateTime originalStartDate = new DateTime(c.getStartDate());
							DateTime originalEndDate = new DateTime(c.getEndDate());
							DateTime today = new DateTime();
							DateTime newDate = new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), originalStartDate.getHourOfDay(), originalStartDate.getMinuteOfHour(), originalStartDate.getSecondOfMinute(), originalStartDate.getMillisOfSecond());
							c.setStart(CommonHandler.getChannelDateString(newDate));
							c.setStartDate(newDate.toDate());
							c.setEnd(CommonHandler.getChannelDateString(originalEndDate));
							c.setEndDate(originalEndDate.toDate());							
							c.setId(m.getCalendarId());		//set surrogate key
							c = (Calendar) calendarHandler.doUpdateItem(c, m.getId());
						}
					}
					//end business logic
					
					retVal += JsonUtil.toString(c);
					retVal = retVal.replaceAll("\\n", "");
				}
				retVal = retVal.replaceAll("\\}\\{", "},{");
				retVal = "[" + retVal + "]";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retVal;
	}
}
