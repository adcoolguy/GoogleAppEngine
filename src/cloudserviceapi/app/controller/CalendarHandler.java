package cloudserviceapi.app.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.appspot.cloudserviceapi.common.JsonUtil;
import com.appspot.cloudserviceapi.data.EMF;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import app.common.Constants;
import app.model.Calendar;
import app.model.CalendarEndpoint;
import app.model.Movie;
import app.model.User;

/**
 * Format:
 * 
 * GET LIST OF ITEMS:
 * http://localhost:8888/ws/crud?type=modelCalendar&uid=j
 * GET ITEM:
 * http://localhost:8888/ws/crud?type=modelCalendar&id=1
 * DELETE ITEM:
 * http://localhost:8888/ws/crud?type=modelCalendar&id=1&action=delete
 * CREATE ITEM:
 * http://localhost:8888/ws/crud?type=modelCalendar&id=1&action=create
 * id=1
 * name=name 1
 * description=text 1
 * UPDATE ITEM:
 * http://localhost:8888/ws/crud?type=modelCalendar&id=1&action=update
 * id=1
 * name=name 2
 * description=text 2
 *
 */
public class CalendarHandler implements CrudServiceCallback, ServletContextListener {
	public static String DEFAULT_HQ_TIME = "US/Eastern";
	private static CalendarEndpoint dao;
	private String uid;	//the parent id
	private Long movieId;
	private boolean shuffleFlag;
	private String movieUrl;
	private MovieHandler movieHandler = new MovieHandler();

	static {
		dao = new CalendarEndpoint();
	}
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	private String getValue(HttpServletRequest req, String reqName) {
		return req.getParameter(reqName);
	}
	
	public Object parseRequest(HttpServletRequest request) {
	    //=== begin TBD
		uid = getValue(request, Constants.UNIVERSAL_ID);
        //KISS store username - just for display not for real stuff to avoid security risk!!!
//		if(uid != null && !uid.equals("null") && !uid.equals("undefined")) {
//			request.getSession().setAttribute(Constants.UNIVERSAL_ID, uid);
//		}
//		if(!SecurityUtils.isAuthenticated(request)) {
//			try {
//				throw new Exception(Constants.ERR_NOT_AUTHENTICATED);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	    //=== end TBD
		Calendar item = null;
		uid = CalendarHelper.getValue(request, "uid");
		String temp1 = CalendarHelper.getValue(request, "movieid");
		if(temp1 == null || temp1.isEmpty()) {
			movieId = -1L;
		} else {
			movieId = new Long(temp1);
		}
		temp1 = CalendarHelper.getValue(request, "shuffled");
		if(temp1 != null && temp1.equals("true")) {
			shuffleFlag = true;
		} else {
			shuffleFlag = false;
		}
		
		String type = CalendarHelper.getValue(request, "type");

		if(type != null && type.equals(Constants.CALENDAR_MODEL_ID)) {
			String frequency = CalendarHelper.getValue(request, "repeat");
			if(frequency != null && !"One Time Event".equals(frequency.trim())) {
				CalendarHelper helper = new CalendarHelper();
				DateTimeFormatter format = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
				String baseStartTimeStr = CalendarHelper.getValue(request, "start");
				String baseEndTimeStr = CalendarHelper.getValue(request, "end");
				item = helper.createItem(request, baseStartTimeStr, baseEndTimeStr);
				String pattern = baseStartTimeStr + baseEndTimeStr;		//KISS
				item.setIsRecurring(true);
				item.setRecurringPattern(pattern);
				if(baseStartTimeStr != null && !baseStartTimeStr.equals("") && baseEndTimeStr != null && !baseEndTimeStr.equals("")) {
					if("Daily Event".equals(frequency.trim())) {
						//create additional items - 1
						try {
							int daysAfter = 6;
							Calendar item1 = null;
							DateTime baseStartTime = format.parseDateTime(baseStartTimeStr);
							Instant baseStartDate = baseStartTime.toInstant();
							DateTime baseEndTime = format.parseDateTime(baseEndTimeStr);
							Instant baseEndDate = baseEndTime.toInstant();
							Instant finalStartDate, finalEndDate;
							String a, b;
							for(int i=1; i<=daysAfter; i++) {
								finalStartDate = baseStartDate.toDateTime().plusDays(i).toInstant();
								finalEndDate = baseEndDate.toDateTime().plusDays(i).toInstant();
								a = finalStartDate.toDateTime().withZone(DateTimeZone.forID(DEFAULT_HQ_TIME)).toString(format);
								b = finalEndDate.toDateTime().withZone(DateTimeZone.forID(DEFAULT_HQ_TIME)).toString(format);
								System.out.println("[" + i + "] baseStartTimeStr " + baseStartTimeStr + " baseEndTimeStr " + baseEndTimeStr + " ==> " + a + " to " + b);
								item1 = (new CalendarHelper()).createItem(request, a, b);
								item1.setIsRecurring(true);
								item1.setRecurringPattern(pattern);
								String temp = CalendarHelper.getValue(request, "all_day");
								if(temp != null && temp.equalsIgnoreCase("true")) {
									item1.setAllDay(true);
								}
								doCreateItem(item1);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else 
					if("Weekly Event".equals(frequency.trim())) {
						//create additional items - 1
						try {
							int weeksAfter = 3;
							Calendar item1 = null;
							DateTime baseStartTime = format.parseDateTime(baseStartTimeStr);
							Instant baseStartDate = baseStartTime.toInstant();
							DateTime baseEndTime = format.parseDateTime(baseEndTimeStr);
							Instant baseEndDate = baseEndTime.toInstant();
							Instant finalStartDate, finalEndDate;
							String a, b;
							for(int i=1; i<=weeksAfter; i++) {
								finalStartDate = baseStartDate.toDateTime().plusWeeks(i).toInstant();
								finalEndDate = baseEndDate.toDateTime().plusWeeks(i).toInstant();
								a = finalStartDate.toDateTime().withZone(DateTimeZone.forID(DEFAULT_HQ_TIME)).toString(format);
								b = finalEndDate.toDateTime().withZone(DateTimeZone.forID(DEFAULT_HQ_TIME)).toString(format);
								System.out.println("[" + i + "] baseStartTimeStr " + baseStartTimeStr + " baseEndTimeStr " + baseEndTimeStr + " ==> " + a + " to " + b);
								item1 = (new CalendarHelper()).createItem(request, a, b);
								item1.setIsRecurring(true);
								item1.setRecurringPattern(pattern);
								doCreateItem(item1);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else 
					if("Monthly Event".equals(frequency.trim())) {
						//create additional items - 1
						try {
							int monthsAfter = 11;
							Calendar item1 = null;
							DateTime baseStartTime = format.parseDateTime(baseStartTimeStr);
							Instant baseStartDate = baseStartTime.toInstant();
							DateTime baseEndTime = format.parseDateTime(baseEndTimeStr);
							Instant baseEndDate = baseEndTime.toInstant();
							Instant finalStartDate, finalEndDate;
							String a, b;
							for(int i=1; i<=monthsAfter; i++) {
								finalStartDate = baseStartDate.toDateTime().plusMonths(i).toInstant();
								finalEndDate = baseEndDate.toDateTime().plusMonths(i).toInstant();
								a = finalStartDate.toDateTime().withZone(DateTimeZone.forID(DEFAULT_HQ_TIME)).toString(format);
								b = finalEndDate.toDateTime().withZone(DateTimeZone.forID(DEFAULT_HQ_TIME)).toString(format);
								System.out.println("[" + i + "] baseStartTimeStr " + baseStartTimeStr + " baseEndTimeStr " + baseEndTimeStr + " ==> " + a + " to " + b);
								item1 = (new CalendarHelper()).createItem(request, a, b);
								item1.setIsRecurring(true);
								item1.setRecurringPattern(pattern);
								doCreateItem(item1);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}					
					}
				}
			} else {
				item = (new CalendarHelper()).createItem(request, CalendarHelper.getValue(request, "start"), CalendarHelper.getValue(request, "end"));
				item.setIsRecurring(false);
				item.setRecurringPattern(null);
			}
			//=== It was a hot spot with average time of 5516 microseconds in a total of 5 invocations
			//item.setEventPattern(CalendarHelper.getEventPattern(item.getStartDate()));
			String temp = CalendarHelper.getValue(request, "id");
			if(temp != null) {
				item.setId(Long.parseLong(temp));	//this is only good for update/delete
				// parse the rest of the information off of the request
			}
			temp = CalendarHelper.getValue(request, "movieUrl");
			if(temp != null) {
				movieUrl = temp;	//this is for movie scheduled movie
			}
			temp = CalendarHelper.getValue(request, "all_day");
			if(temp != null && temp.equalsIgnoreCase("true")) {
				item.setAllDay(true);
			}
			
		}
		
		return item;
	}

	private User getParent() throws Exception {
//		User retVal = null;
//		User u = new User();
//		UserHandler uh = new UserHandler();
//		u.setName(uid);
//		u = uh.getUserByName(u);
//		if(u.getKey() != null) {
//			retVal = u;
//		} else {
//			System.out.println("no parent with uid [" + uid + "] found, nothing will be done for object instance of " + u.getClass().getName());
//		}
//		return retVal;
		return CommonHandler.getParent(uid);
	}

	private void saveParent(User item) throws Exception {
		UserHandler uh = new UserHandler();
		uh.doUpdateItem(item);
	}

	public void saveMovie(String uid, long movieId, boolean shuffleFlag, long itemId, Calendar item) {
		this.movieId = movieId;
		saveMovie(uid, movieId, shuffleFlag, itemId, item);
	}
	
	private void saveMovie(String uid, long itemId, boolean shuffleFlag, Calendar item) {
		try {
			if(movieId == null) return;	//save only if it is called from calendar UI and like not from automatic channel shifting
			
			movieHandler.setUid(uid);
			Movie m = (Movie) movieHandler.doGetItem(movieId);
			if(m != null) {
				m.setId(movieId);
				if(item.getStartDate() != null) {	//KISS - use the start date to kick off scheduling in channel
					//=== Target format required by jquery.countdown.js: 2013, 8-1, 23, 21, 51, 40
//					String temp = CalendarHelper.getEventPattern(item.getStartDate());	//localDate.getYear() + "," + (localDate.getMonthOfYear()-1) + "," + localDate.getDayOfMonth() + "," + localDate.getHourOfDay() + "," + localDate.getMinuteOfHour() + "," + localDate.getSecondOfMinute();
					//String.valueOf(item.getStartDate().getTime());
					m.setCalendarId(itemId);
//					m.setEventPattern(temp);
					if(movieUrl != null) {	//in case of channel shifting
						m.setURL(movieUrl);
					}
				} else {
					m.setCalendarId(null);
//					m.setEventPattern(null);
				}
				m.setShuffled(shuffleFlag);
				movieHandler.setType(Constants.CALENDAR_MODEL_ID);
				movieHandler.setAction(Constants.ACT_UPDATE);
				movieHandler.doUpdateItemForAnyone(m);
			} else {
				throw new Exception(this.getClass().getName() + " ****** Movie with id [" + movieId + "] can not be found, does it exist or valid? ******");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void unlinkMovie(String uid) {
		try {
			if(movieId == null) return;	//save only if it is called from calendar UI and like not from automatic channel shifting
			
			movieHandler.setUid(uid);
			Movie m = (Movie) movieHandler.doGetItem(movieId);
			if(m != null) {
				m.setId(movieId);
				m.setCalendarId(null);
//				m.setEventPattern(null);
				movieHandler.setType(Constants.CALENDAR_MODEL_ID);
				movieHandler.setAction(Constants.ACT_DELETE);
				movieHandler.doUpdateItemForAnyone(m);
			} else {
				throw new Exception(this.getClass().getName() + " ****** Movie with id [" + movieId + "] can not be found, does it exist or valid? ******");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the associated calendar object(s) which is part of a recurring event.
	 * @param user
	 * @return
	 */
	public long deleteItemsByPattern(String pattern) {
		long retVal = 0;
		EntityManager em = getEntityManager();
		try {
			System.out.println("deleting calendar by pattern [" + pattern + "] ...");
		    String queryStr = String.format("delete from " + Calendar.class.getName() + " c" +
		    								" where c.recurringPattern = :pattern");
            javax.persistence.Query query = em.createQuery(queryStr);
            query.setParameter("pattern", pattern);
            retVal = query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}

		return retVal;
	}

	public Object doCreateItem(Object item) throws Exception {
		long id = -1;
		
		Calendar cal = (Calendar)item;
		try {
			User u = getParent();
			if(u != null) {
				List<Calendar> cl = u.getCalendar();
				if(cl != null) {
					cl.add(cal);
				} else {
					cl = new ArrayList<Calendar>();
					cl.add(cal);
					u.setCalendar(cl);	//JPA2					
				}
				cal.setMovieId(movieId);
				saveParent(u);
				id = cal.getKey().getId();
				cal.setId(id);	//set the "surrogate key" - important as everything else is depending on this key not the real key.id!
				saveMovie(uid, id, shuffleFlag, cal);
				movieHandler.listCalendar(movieId);
				System.out.println(this.getClass().getName() + " calendar object inserted with id '" + id + "' " + cal.getTitle() + " " + cal.getStart() + " " + cal.getEnd() + " pattern [" + cal.getRecurringPattern() + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return cal;
	}

	public String doGetItems() throws Exception {
		CollectionResponse<Calendar> cr = null;
		Collection<Calendar> l;
		String retVal = "";
		
		try {
			User u = getParent();
			if(u != null) {
				l = u.getCalendar();
				if(l != null) {	//JPA2
					Iterator<Calendar> it = l.iterator();
					Calendar cal = null;
					while(it.hasNext()) {
						cal = it.next();
						cal.setId(cal.getKey().getId());	//GAEJ specific
						retVal += JsonUtil.toString(cal);
						retVal = retVal.replaceAll("\\n", "");
					}
				}
				retVal = retVal.replaceAll("\\}\\{", "},{");
				retVal = retVal.replaceAll("all_day", "allDay");	//fullCalendar 1.4 requires it to be "allDay"		
				retVal = "[" + retVal + "]";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retVal;
	}

	public Object doGetItem(long itemId) throws Exception {
		Object retVal = null;
		
		EntityManager em = getEntityManager();
		try {
			System.out.println("getting calendar by id '" + itemId + "' ...");
			User u = getParent();
			//GAEJ specific
			Key parentKey = KeyFactory.createKey(User.class.getSimpleName(), u.getKey().getId());
			Key key = KeyFactory.createKey(parentKey, Calendar.class.getSimpleName(), itemId);
			retVal = em.find(Calendar.class, key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}

		return retVal;
	}
	
	public Object doUpdateItem(Object item, long movieId) throws Exception {
		this.movieId = movieId;
		return doUpdateItem(item);
	}
	
	//http://stackoverflow.com/questions/4998819/google-appengine-gae-complete-object-key
	public Object doUpdateItem(Object item) throws Exception {
		EntityManager mgr = getEntityManager();
        EntityTransaction tx = mgr.getTransaction();
        
		User u = getParent();
		Calendar newCal = (Calendar)item;
		Calendar cal = null;
		//GAEJ specific
		Key parentKey = KeyFactory.createKey(User.class.getSimpleName(), u.getKey().getId());
		Key key = KeyFactory.createKey(parentKey, Calendar.class.getSimpleName(), newCal.getId());
//		Query q = (Query) getEntityManager().createQuery("select from " + Calendar.class.getSimpleName() + " where id = :f"); 
//		q.setParameter("f", key.getId());
//		cal = (Calendar) q.getSingleResult();
		cal = mgr.find(Calendar.class, key);
		cal.setTitle(newCal.getTitle());
		cal.setDescription(newCal.getDescription());
		cal.setStart(newCal.getStart());
		cal.setStartDate(newCal.getStartDate());
		cal.setEnd(newCal.getEnd());
		cal.setAllDay(newCal.getAllDay());
		cal.setUrl(newCal.getUrl());
		cal.setMovieId(movieId);
        tx.begin();
		mgr.persist(cal);
        tx.commit();
        
		Long id = cal.getKey().getId();
		cal.setId(id);	//set the "surrogate key" - important as everything else is depending on this key not the real key.id!
		if(cal != null) {
			if(movieHandler != null && movieId != null) {
				saveMovie(uid, id, shuffleFlag, (Calendar) item);
				movieHandler.listCalendar(movieId);			
				System.out.println(cal.getClass().getName() + " object updated with id '" + id + "' " + cal.getTitle() + " " + cal.getStart() + " " + cal.getEnd());
			}
		}
		
		return cal;
	}
	
	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		CrudService.addObjectHandlers(this);
		System.out.println(this.getClass().getName() + " added as crud service handler");
	}

	@Override
	public Object doDeleteItem(long itemId) throws Exception {
		Long id = -1L;
		EntityManager mgr = getEntityManager();
        EntityTransaction tx = mgr.getTransaction();	
        
		User u = getParent();
		Calendar cal = null, retVal = new Calendar();
		//GAEJ specific
		Key parentKey = KeyFactory.createKey(User.class.getSimpleName(), u.getKey().getId());
		Key key = KeyFactory.createKey(parentKey, Calendar.class.getSimpleName(), itemId);
		cal = mgr.find(Calendar.class, key);
        if(cal != null) {
			id = cal.getKey().getId();		//GAEJ specific
			retVal.setId(id);	//set the "surrogate key" - important as everything else is depending on this key not the real key.id!
	        tx.begin();
			mgr.remove(cal);
	        tx.commit();
			System.out.println("2: " + cal.getClass().getName() + " object deleted with id '" + id + "' " + cal.getTitle() + " " + cal.getStart() + " " + cal.getEnd());
		}
        //=== begin need to set calendar id to null (by nullify the startDate) so that it will not be visible to scheduler!
		unlinkMovie(uid);
//		movieHandler.listCalendar(movieId);		
        //=== end need to set calendar id to null (by nullify the startDate) so that it will not be visible to scheduler!
		
		return retVal.getId();	
	}
	
	@Override
	public Object doDeleteItem(Object item) throws Exception {
		Long id = -1L;
		if(item != null) {
			Calendar cal = (Calendar)item;
			Long tempId = -1L;
			tempId = cal.getId().longValue();
			Calendar cOld = (Calendar)doGetItem(tempId); 
			String pattern = cOld != null?cOld.getRecurringPattern():null;
			//=== takes care of its associated events first if it is recurring
			if(cOld != null && cOld.getIsRecurring() != null && pattern != null) {
				System.out.println("1: " + cal.getClass().getName() + " object deleted with id '" + id + "' " + cal.getTitle() + " " + cal.getStart() + " " + cal.getEnd() + " existing pattern [" + pattern + "]");
				long totalDeleted = deleteItemsByPattern(pattern);
				System.out.println(totalDeleted + " recurring events deleted.");
				if(totalDeleted > 0) {
					id = tempId;
				}
			} else {
				id = cal.getId() == null ? 0 : cal.getId();
				System.out.println("associated movieId [" + movieId + "] of cal id [" + id + "]");
				doDeleteItem((long)id);
			}
		}
		
		return id;	
	}

	@Override
	public String doGetAllItems() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object doMigrateAllItems() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object doMigrateAllItems(String uid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object doMigratePurgeItem(String uid, String keyString)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}