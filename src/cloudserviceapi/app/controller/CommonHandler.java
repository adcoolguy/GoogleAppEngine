package cloudserviceapi.app.controller;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import app.model.User;

public class CommonHandler {

	/**
	 * Delete all calendar(s) based on the Query By Example/QBE (in this case its associated movie object).
	 * 
	 * @param movie
	 * @return
	 * @throws Exception
	 */
//	public static long doDeleteItems(String uid, CalendarHandler manager, Movie movie) throws Exception {
//		long deletedCount = -1; 
//		Collection<Calendar> l;
//		User parent = getParent(uid);
//		manager.setUid(uid);	//important!!!
//		if(parent != null && parent.getCalendar() != null) {
//			l = parent.getCalendar();
//			Iterator<Calendar> it = l.iterator();
//			Calendar cal = null;
//			Long movieId = movie.getId();		//GAEJ specific
//			deletedCount = 0;
//			while(it.hasNext()) {
//				cal = it.next();
//				cal.setId(cal.getKey().getId());	//GAEJ specific
//				if(cal.getId() == movieId) {
//					manager.doDeleteItem(cal.getId());
//					deletedCount++;
//				}
//			}
//		}
//		return deletedCount;
//	}
	
	/**
	 * Return the parent object (in this case, User object of movies and calendars)
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public static User getParent(String uid) throws Exception {
		User retVal = null;
		User u = new User();
		UserHandler uh = new UserHandler();
		u.setName(uid);
		u = uh.getUserByName(u);
		if(u.getKey() != null) {
			retVal = u;
		} else {
			System.out.println("no parent with uid [" + uid + "] found, nothing will be done for object instance of " + u.getClass().getName());
		}
		return retVal;
	}
	
	/**
	 * Get the date formated for the front end (Channel).
	 * @param dateStr date string submitted from UI
	 * @return
	 * @throws Exception
	 */
	public static DateTime getChannelDateTime(String dateStr) throws Exception {
		DateTimeFormatter format = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
		DateTime time = format.parseDateTime(dateStr);

		return time;
	}

	public static String getChannelDateString(DateTime date) throws Exception {
		DateTimeFormatter format = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
		String ret = date.toString(format);
		
		return ret;
	}

}
