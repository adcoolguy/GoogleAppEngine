package cloudserviceapi.app.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import tapp.model.ServiceRegistry;
import app.common.AppUtils;
import app.common.Constants;
import app.common.DatastoreUtils;
import app.common.SecurityUtils;
import app.model.Calendar;
import app.model.Movie;
import app.model.MovieEndpoint;
import app.model.User;

import com.appspot.cloudserviceapi.common.JsonUtil;
import com.appspot.cloudserviceapi.data.EMF;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

/**
 * Format:
 * 
 * GET LIST OF ITEMS:
 * http://localhost:8888/ws/crud?type=modelMovie
 * GET ITEM:
 * http://localhost:8888/ws/crud?type=modelMovie&id=1
 * DELETE ITEM:
 * http://localhost:8888/ws/crud?type=modelMovie&id=1&action=delete
 * DELETE LEGACY ITEM:
 * http://localhost:8888/ws/crud?type=modelMovie&id=agpjbG91ZGF3YXJlch0LEgRVc2VyGNEPDAsSBU1vdmllGICAgICA8IsKDA&action=migrate_delete&uid=j
 * CREATE ITEM:
 * http://localhost:8888/ws/crud?type=modelMovie&id=1&action=create
 * id=1
 * name=name 1
 * description=text 1
 * UPDATE ITEM:
 * http://localhost:8888/ws/crud?type=modelMovie&id=1&action=update&uid=j
 * id=1
 * name=name 2
 * description=text 2
 * BY USER ID:
 * http://localhost:8888/ws/crud?type=modelMovie&uid=j
 * BY USER ID with pagination:
 * http://localhost:8888/ws/crud?type=modelMovie&uid=song&pageNumber=1&maxPerPage=6
 * BY USER'S FILTERED:
 * http://localhost:8888/ws/crud?type=modelMovie&uid=j&filter=next5
 * BY SCHEDULED COLLECTION:
 * http://localhost:8888/ws/crud?type=modelMovie&action=scheduled&filter=scheduled
 * BY SHARED (By Anybody) COLLECTION:
 * http://localhost:8888/ws/crud?type=modelMovie&action=shared&filter=shared
 * BY SHARED (By only the user) COLLECTION:
 * http://localhost:8888/ws/crud?type=modelMovie&action=shared&filter=shared&uid=j
 * PURGE USER'S CALENDAR:
 * http://localhost:8888/ws/crud?type=modelMovie&uid=j&action=purgecalendar
 * MIGRATE USER'S LEGACY ENTITIES:
 * http://localhost:8888/ws/crud?type=modelMovie&uid=j&&action=migrate&filter=ownedbyme
 *
 */
public class MovieHandler implements CrudServiceCallback, ServletContextListener {
	private static List<Movie> clonedList;	//used by UI, acts as cache
	private static MovieEndpoint dao;
	private String oid;	//the object id
	private String uid;	//the parent id
	private String type;
	private String action;
	private String filter;
	private String legacy;
	private CalendarHandler calendarHandler;
	private int maxPerPage = 6;	//this needs to be in sy with the front end UI!
	private int pageNumber = 1;
	//KISS cache for all users
//	private HashMap allMoviesCache = new HashMap();

	static {
		dao = new MovieEndpoint();
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	private String getValue(HttpServletRequest req, String reqName) {
		return req.getParameter(reqName);
	}
	
	/**
	 * This is the main JSON parsing method into a Java object. It does not involve any datastore calls.
	 */
	public Object parseRequest(HttpServletRequest request) {
	    //=== begin TBD
		uid = getValue(request, Constants.UNIVERSAL_ID);
		legacy = getValue(request, "legacy");	//to create new item based on a legacy item 
		parsePagedRequest(request);
		
		System.out.println("MovieHandler:parseRequest() uid = [" + uid + "] received");
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
		Movie item = null;
		oid = getValue(request, "oid");
        //end KISS store username - just for display not for real stuff to avoid security risk!
		filter = getValue(request, "filter");
		type = getValue(request, "type");
		action = getValue(request, "action");
		
		if(type != null && type.equals(Constants.MOVIE_MODEL_ID)) {
			item = new Movie();
			String temp = getValue(request, "id");
			// parse the rest of the information off of the request
			if(action != null && action.equals(Constants.MIGRATE_DELETE)) { //as migrate the Id is not a Long but a String (serialized key)
				item.setKey(KeyFactory.stringToKey(temp));
			}
			else
			if(temp != null && !temp.equals("")) {
				item.setId(Long.parseLong(temp));	//does not really matter if it is create action
			} 
			item.setModified(new Date());
			//view specific attributes
			item.setTitle(getValue(request, "title"));
			item.setDescription(new Text(getValue(request, "description")==null?"":getValue(request, "description")));
			item.setSearchResults(new Text(getValue(request, "search_results")==null?"":getValue(request, "search_results")));
			item.setURL(getValue(request, "url"));
			String temp2 = null;
			if((temp2 = getValue(request, "shared")) != null) {
				try {
					item.setShared(new Boolean(temp2));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if((temp2 = (String)getValue(request, "channelPattern")) != null) {
				try {
					item.setChannelPattern(temp2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			item.setOwner(uid);
			item.setOid(oid);
		}
		
		return item;
	}
	
	private void parsePagedRequest(HttpServletRequest request) {
		String t = getValue(request, "pageNumber");
		if(t != null && StringUtils.isNumeric(t)) {
			pageNumber = Integer.valueOf(t);
		}
		t = getValue(request, "maxPerPage");
		if(t != null && StringUtils.isNumeric(t)) {
			maxPerPage = Integer.valueOf(t);
		}
	}

	private long generateTotalPageForUI(long maxItemPerPage, long totalItem) {
		long totalPage = (int) Math.ceil((double) totalItem / maxItemPerPage);

		return totalPage;
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
//			//TODO - could have been done better to avoid security risk!!!
//			//createParent(u);	//automatic user creation to avoid future JPA upgrade for instance
//			//System.out.println("no parent with uid [" + uid + "] found, automatically created one: user [" + u + "]");
//		}
//		return retVal;
		return CommonHandler.getParent(uid);
	}
	
	private void createParent(User item) throws Exception {
		UserHandler uh = new UserHandler();
		uh.doPersistItem(item);
	}
	
	public void saveParent(User item) throws Exception {
		UserHandler uh = new UserHandler();
		uh.doUpdateItem(item);
	}

//	private void saveParent1(User item, Movie movie) throws Exception {
//		UserHandler uh = new UserHandler();
//		uh.doUpdateItem(item, movie);
//	}

	public Object doCreateItem(Object item) throws Exception {
		long id = -1;
		
		Movie cal = (Movie)item;
		try {
			User u = getParent();
			if(u != null) {
				//=== handle legacy/migrated entities, if any
				cal.setOwner(AppUtils.handleLegacyOwnerString(cal.getOwner(), legacy));
				
				List<Movie> cl = u.getMovie();
				if(cl != null) {
					cl.add(cal);
				} else {
					cl = new ArrayList<Movie>();
					cl.add(cal);
					u.setMovie(cl);		//JPA2
				}
				saveParent(u);
				System.out.println(this.getClass().getName() + ":doCreateItem: parent (user) saved for movie '" + cal.getTitle() + "' url [" + cal.getURL() + "]");
				id = cal.getKey().getId();
				cal.setId(id);	//set the "surrogate key" - important as everything else is depending on this key not the real key.id!
				System.out.println(this.getClass().getName() + ":doCreateItem: object inserted with id '" + id + "' " + cal.getTitle() + " " + cal.getURL());
			}
			
			//=== update the cache
			CacheManager.saveUserCache(u, u.getMovie(), cal);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cal;
	}

//	public Object doCreateItem1(Object item) throws Exception {
//		long id = -1;
//		
//		Movie cal = (Movie)item;
//		try {
//			User u = getParent();
//			if(u != null) {
//				saveParent1(u, cal);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return cal;
//	}

	/**
	 * Get all items related to a logged in user.
	 * 
	 * $$$$$$$$$$$$$$$$$$ THIS IS VERY EXPENSIVE OPERATION, thus it is cached by default $$$$$$$$$$$$$$$$$$
	 * #perf top 2 (10-17-2013) sorted by desc avg time = 1110 ms
	 */
	public String doGetItems() throws Exception {
//		if(allMoviesCache != null && allMoviesCache.get(CacheManager.getUserCacheKey(uid, filter, pageNumber)) != null) {
//			System.out.println("MovieHandler: doGetItems() cached hit: with key [" + uid + filter + pageNumber + "]");
//			return (String)allMoviesCache.get(CacheManager.getUserCacheKey(uid, filter, pageNumber));
//		} else {
//			System.out.println("MovieHandler: doGetItems() cached missed: with key [" + uid + filter + pageNumber + "] <==================== !!!");
//		}
		
		Collection<Movie> l;
		String retVal = "";
		long totalItem = 0;
		
		try {
			User u = getParent();
			//=== begin automatic user creation (e.g. for Facebook)
	        UserHandler h = new UserHandler();
			if(u == null) {
		        u = new User();
		        u.setName(uid);
				u = h.getUserByName(u);
				if(u == null || !h.exists(u)) {
			        h.setUid(u.getName());
			        u = (User)h.doCreateItem(u);
				}
			}
			//=== end automatic user creation (e.g. for Facebook)
			if(u == null || !h.exists(u)) {
				retVal = Constants.NO_PARENT_ERR;
			} else
			if(u != null) {
				//begin TBD JPA 2
				//to lookup based on the owner field!!!
				//end TBD JPA 2
				//=== begin supporting pagination
				//List tl = u.getMovie();	//.fetch(1,25);

//c.f. JPA https://groups.google.com/forum/#!topic/google-appengine-java/UiMhX_JMOz4
//c.f. JDO http://stackoverflow.com/questions/6867720/app-engine-java-syntax-for-setting-query-limit-and-start-offset
				
//				List ml = getMovies(maxPerPage, pageNumber);	//TODO causing Protractor subtitle play to fail for unknown reason!!!
//				if(ml != null) {
				if(u.getMovie() != null) {	//TODO need to avoid this as it is querying the datastore!!!
//					totalItem = ml.size();
				//=== begin supporting pagination
				//List tl = u.getMovie();	//.fetch(1,25);
				User cachedUser = CacheManager.getUserCache(u);
				if(cachedUser != null) {
					u = cachedUser;
				} else {
					CacheManager.addUserCache(u);
				}
				if(u.getMovie() != null) {
					totalItem = u.getMovie().size();
				} else {
					totalItem = 0;
				}
//				List tl = (List) AppUtils.getPagedResults(ml, maxPerPage, pageNumber);
				List tl = (List) AppUtils.getPagedResults(u.getMovie(), maxPerPage, pageNumber);
				//=== end supporting pagination
				
				if(action != null && !action.equals("")) {
					l = filterScheduledMovies(tl);
				} else {
					l = tl;	//no need to filter for CRUD
				}
				
				Iterator<Movie> it = l != null?l.iterator():null;
				Movie m = null;
				if(it != null) {
					CalendarHandler calendarHandler = new CalendarHandler();
					calendarHandler.setUid(uid);

					while(it.hasNext()) {
						m = it.next();
						m.setId(m.getKey().getId());	//GAEJ specific
						if(m.getCalendarId() != null) {
							Calendar cal = (Calendar) calendarHandler.doGetItem(m.getCalendarId());	//lazy load it
							if(cal != null) {
								Boolean allDay = (Boolean) cal.getAllDay();
								m.setCalendarAllDay(allDay);
								m.setEventPattern(cal.getEventPattern());
							}
							retVal += JsonUtil.toString(m);
							retVal = retVal.replaceAll("\\n", "");
						}
						else
						//=== returns only unscheduled movies
						//TBD quick hack just to show all movies for CRUD and channels
						if(action == null || action.equals("") || m.getCalendarId() == null || m.getCalendarId() < 1) {
							retVal += JsonUtil.toString(m);
							retVal = retVal.replaceAll("\\n", "");
						}
					}
				}
				retVal = retVal.replaceAll("\\}\\{", "},{");
				retVal = addMetaPage(retVal, totalItem);
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		//=== update the cache for subsequent calls
//		allMoviesCache.put(CacheManager.getUserCacheKey(uid, filter, pageNumber), retVal);

		return retVal;
	}

	private String addMetaPage(String retVal, long totalItem) {
		//=== includes metadata
		String meta = "{" +
            "\"pageNumber\":" + pageNumber + "," +
            "\"maxPerPage\":" + maxPerPage + "," +	/* decided by the client */
            "\"totalItem\":" + totalItem + "," +
            "\"totalPage\":" + generateTotalPageForUI(maxPerPage, totalItem) +
         "}";
		String finalContent = "";
//		if(totalItem > 0) {
			finalContent = meta + "," + retVal;
//		} else {
//			finalContent = meta;
//		}
		retVal = "[" + finalContent + "]";
		
		return retVal;
	}

	public List<Movie> migrateOwnedMovies(User u, List<Movie> totalListFromAllUsers) throws Exception {
		List<Movie> nList = null;
		
		if(u != null && totalListFromAllUsers != null) {
			Iterator<Movie> it = totalListFromAllUsers.iterator();
			Movie m = null;
			long count = 0;
			String uid = u.getName();
			if(u.getMovie() != null) {
				nList = u.getMovie(); 
			} else {
				nList = new ArrayList();
			}
			Movie clonedMovie = null;
			while(it.hasNext()) {
				m = it.next();
				if(uid != null && m.getOwner() != null && m.getOwner().equals(uid)) {
					//=== make a copy
					clonedMovie = SerializationUtils.clone(m);
					nList.add(clonedMovie);
					System.out.println("migrateOwnedMovies: movieId [" + m.getId() + "=>" + clonedMovie.getId() +"] title [" + m.getTitle() + "=>" + clonedMovie.getTitle() + "] owner = [" + m.getOwner() + "=>" + clonedMovie.getOwner() + "] migrated");
					count++;
				}
			}
			System.out.println("migrateOwnedMovies: total movie migrated in the collection of user [" + uid + "] = " + count );
		}
		
		return nList;
	}

	/**
	 * Get all items.
	 */
	public String doGetAllItems() throws Exception {
		Collection<Movie> l;
		String retVal = "";
		
		try {
			//=== end automatic user creation (e.g. for Facebook)
			l = filterMovies(getMovies());
//			l = filterMovies(getMovies(maxPerPage, pageNumber));	//TODO use this and not the above once it is done
			l = AppUtils.getPagedResults(l, maxPerPage, pageNumber);
			Iterator<Movie> it = l.iterator();
			Movie cal = null;
			while(it.hasNext()) {
				cal = it.next();
				cal.setId(cal.getKey().getId());	//GAEJ specific
				retVal += JsonUtil.toString(cal);
				retVal = retVal.replaceAll("\\n", "");
			}
			retVal = retVal.replaceAll("\\}\\{", "},{");
			retVal = "[" + retVal + "]";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retVal;
	}
	
	/**
	 * This is expensive operation, use it with caution!
	 *
	 * @return
	 */
	public List<Movie> getMovies() {
		EntityManager mgr = getEntityManager();
		mgr.clear();	//clear the cache, if any

		List<Movie> results = new ArrayList(), child = null;
		List<Movie> parent = mgr.createQuery("select u from " + User.class.getName() + " u").getResultList();
		Iterator it1 = parent.iterator();
		Iterator it2 = null;
		while(it1.hasNext()) {
			child = (List<Movie>) ((User)it1.next()).getMovie();
			if(child != null) {	//JPA2
				it2 = child.iterator();
				while(it2.hasNext()) {
					results.add((Movie) it2.next());
				}
			}
		}

		return results;
	}

	public List<Movie> getMovies(int maxPerPage, int pageNumber) {
		EntityManager mgr = getEntityManager();

		Query query = mgr.createQuery("select m from Movie m where owner = '" + uid + "'");
		if(pageNumber > 0) {
			query.setFirstResult(pageNumber-1);	//starts from 0 thus minus 1
			query.setMaxResults(maxPerPage);
		}
		List<Movie> resultList = query.getResultList();
		return resultList;
	}

	private Collection<Movie> filterMovies(List<Movie> l) throws Exception {
		List<Movie> nList = null;
		
		if(filter == null || filter.isEmpty() || filter.equals("undefined")) {
			nList = l;
		} else
		if(l != null) {
			Iterator<Movie> it = l.iterator();
			Movie m = null;
			nList = new ArrayList<Movie>();
			if(Constants.SCHEDULED.equalsIgnoreCase(filter)) {
				while(it.hasNext()) {
					m = it.next();
					if(m != null && m.getCalendarId() != null && m.getCalendarId() > 0 && uid != null && uid.equals(m.getOwner())) {
						nList.add(m);
						System.out.println(this.getClass().getName() + ": SCHEDULED movieId [" + m.getId() + "] title [" + m.getTitle() + "] uid [" + uid + "] cal id [" + m.getCalendarId() + "]");
					}
				}
			} else if(Constants.SHARED_BY_ALL.equalsIgnoreCase(filter)) {
				while(it.hasNext()) {
					m = it.next();
					if(m != null && m.getShared() != null && m.getShared().booleanValue()) {
						nList.add(m);
						System.out.println(this.getClass().getName() + ": SHARED by me/others movieId [" + m.getId() + "] title [" + m.getTitle() + "]");
					}
				}
			} else if(Constants.SHARED.equalsIgnoreCase(filter)) {
				while(it.hasNext()) {
					m = it.next();
					//=== only by me and have to be non-scheduled movies!
					if(m != null && m.getShared() != null && m.getShared().booleanValue() && (m.getCalendarId() == null || m.getCalendarId() < 1) && uid != null && uid.equals(m.getOwner())) {
						nList.add(m);
						System.out.println(this.getClass().getName() + ": SHARED by me movieId [" + m.getId() + "] title [" + m.getTitle() + "]");
					}
				}
			} else {
				while(it.hasNext()) {
					m = it.next();
					System.out.println(this.getClass().getName() + ": NORMAL movieId [" + m.getId() + "] title [" + m.getTitle() + "]");
					nList.add(m);
				}
			}
			//=== sort it based on the filter type
			//https://code.google.com/p/lambdaj/
			//=== get movies based on the filter
			if(Constants.NEXT5.equalsIgnoreCase(filter)) {
				Collections.sort(nList, new Comparator<Movie>() {
			        public int compare(Movie p1, Movie p2) {
			        	int retVal = 0;
//			        	if(p1.getEventPattern() != null && p2.getEventPattern() != null) {
//		        			retVal = p1.getEventPattern().compareTo(p2.getEventPattern());
//			        	}
			        	//=== compare events start date instead
			        	if(p1.getCalendarId() != null && p2.getCalendarId() != null) {
			        		try {
								Calendar c1 = (Calendar) calendarHandler.doGetItem(p1.getCalendarId());
								Calendar c2 = (Calendar) calendarHandler.doGetItem(p2.getCalendarId());
								retVal = c1.getStartDate().compareTo(c2.getStartDate());
							} catch (Exception e) {
								e.printStackTrace();
							}
			        	} else {
			        		retVal = 0;
			        	}
			        	
			           return retVal;
			        }
				});
			}

		} 
		
		return nList;
	}
	
	/*
	 * Retrieve movies with calendar events.
	 * 
	 * #perf top 3 (10-17-2013) sorted by desc avg time = 872 ms
	 */
	private Collection<Movie> filterScheduledMovies(List<Movie> l) throws Exception {
		List<Movie> nList = null;
		
		if(filter == null || filter.isEmpty()) {
			nList = l;
		} else
		if(l != null) {
			Iterator<Movie> it = l.iterator();
			Movie m = null;
			Calendar c = null;
			CalendarHandler calendarHandler = new CalendarHandler();
			nList = new ArrayList<Movie>();
			while(it.hasNext()) {
				m = it.next();
				if(m.getCalendarId() != null) {
					calendarHandler.setUid(uid);
					c = (Calendar) calendarHandler.doGetItem(m.getCalendarId());
					System.out.println(this.getClass().getName() + ": movieId [" + m.getId() + "] title [" + m.getTitle() + "] cal = [" + c + "]");
					nList.add(m);
				}
			}
			//=== sort it based on the filter type
			//https://code.google.com/p/lambdaj/
			//=== get movies based on the filter
			if(Constants.NEXT5.equalsIgnoreCase(filter)) {
				Collections.sort(nList, new Comparator<Movie>() {
			        public int compare(Movie p1, Movie p2) {
			        	int retVal = 0;
//			        	if(p1.getEventPattern() != null && p2.getEventPattern() != null) {
//		        			retVal = p1.getEventPattern().compareTo(p2.getEventPattern());
//			        	}
			        	
			           return retVal;
			        }
				});
				
				//=== automatic channel pattern updates on movie's events 
				//commented out as it is the most expensive operation as at 10-23-2013!!!
				//MovieHelper.handleChannelPattern(uid, calendarHandler);
			}

		} 
		
		return nList;
	}
	
	/*
	 * Purge calendar events of the movies (only the calendar events, if any not the movies).
	 * 
	 * Logic:
	 * 1. Remove the calendar based on the calendar id retrieved, if any from a movie
	 * 2. Make all the calendar ids if applicable of all the movies null
	 * 3. Clean up "orphanaged" movies as well (movies without events but still with invalid event ids in them), if any
	 * 
	 */
	public static Collection<Movie> purgeScheduledMovies(String uid, List<Movie>l) throws Exception {
		List<Movie> nList = null;
		
		if(uid != null) {
			User u = UserHandler.getUser(uid);
			Iterator<Movie> it = l.iterator();
			Movie m = null;
			Calendar c = null;
			CalendarHandler calendarHandler = new CalendarHandler();
			calendarHandler.setUid(uid);
			nList = new ArrayList<Movie>();
			Long ret = null;
			while(it.hasNext()) {
				m = it.next();
				if(m.getCalendarId() != null && m.getCalendarId().longValue() > 0) {
					m.setId(m.getKey().getId());	//GAEJ specific
					calendarHandler.setMovieId(m.getId());
					ret = (Long) calendarHandler.doDeleteItem(m.getCalendarId().longValue());
					//=== only "unlink" from movie if the previous operation is successful
					m.setCalendarId(null);	//unlink it!
					nList.add(m);
					System.out.println("MovieHandler purgeScheduledMovies(): calendar with movieId [" + m.getId() + "] title [" + m.getTitle() + "] cal id = [" + ret + "] purged!");
				} else {
					System.out.println("MovieHandler purgeScheduledMovies(): calendar with movieId [" + m.getId() + "] cal id [" + m.getCalendarId() + "] skipped from been purged");
				}
			}
			//purge the rest of the calendars, if any
			if(u.getCalendar() != null) {
				Iterator<Calendar> it2 = u.getCalendar().iterator();			
				while(it2.hasNext()) {
					c = it2.next();
					ret = (Long) calendarHandler.doDeleteItem(c.getKey().getId());
					System.out.println("MovieHandler purgeScheduledMovies(): calendar with movieId [" + c.getMovieId() + "] cal id = [" + ret + "] purged!");
				}
			}
			
		} 
		
		return nList;
	}
	
	/*
	 * Same as purgePastScheduledMovies() except that it check the dates of the events.
	 */
	public static Collection<Movie> purgePastScheduledMovies(String uid) throws Exception {
		List<Movie> nList = null;
		
		if(uid != null) {
			User u = UserHandler.getUser(uid);
			if(u != null && u.getMovie() != null && u.getMovie().size() > 0) {
				List<Movie>l = u.getMovie();
				Iterator<Movie> it = l.iterator();
				Calendar c = null;
				CalendarHandler calendarHandler = new CalendarHandler();
				calendarHandler.setUid(uid);
				nList = new ArrayList<Movie>();
				Long ret = null;
				//purge the calendar events, if any
				if(u.getCalendar() != null) {
					//=== only if it is a at least a day passed event or with no/invalid date
					boolean pastEvent = false;
					Iterator<Calendar> it2 = u.getCalendar().iterator();
					while(it2.hasNext()) {
						c = it2.next();
						try {
							DateTime calDate = new DateTime(c.getStartDate());
							if(calDate.minusDays(1).isBeforeNow()) {
								pastEvent = true;
							}
						} catch(Exception e) {
							e.printStackTrace();
						} finally {
							if(pastEvent) {
								ret = (Long) calendarHandler.doDeleteItem(c.getKey().getId());
								System.out.println("MovieHandler purgePastScheduledMovies(): calendar with movieId [" + c.getMovieId() + "] cal id = [" + ret + "] purged!");
							}
						}
					}
				}
				
			} 
		}
		
		return nList;
	}

	public Object doGetItem(long itemId) throws Exception {
		EntityManager mgr = getEntityManager();
        EntityTransaction tx = mgr.getTransaction();

		User u = getParent();
		Movie newMov = null;
		//GAEJ specific
		Key parentKey = KeyFactory.createKey(User.class.getSimpleName(), u.getKey().getId());
		Key key = KeyFactory.createKey(parentKey, Movie.class.getSimpleName(), itemId);
		newMov = mgr.find(Movie.class, key);
        
		//Long id = newMov.getKey().getId();
		//newMov.setId(id);	//set the "surrogate key" - important as everything else is depending on this key not the real key.id!
		
		return newMov;
	}
	
	private boolean isOwner(long itemId) throws Exception {
		Movie item = (Movie) doGetItem(itemId);

		return SecurityUtils.isOwner(item, oid);
	}
	
	public Object doUpdateItem(Object item) throws Exception {
		if(item == null) {
			throw new Exception(Constants.ERR_NO_ITEM_FOR_OWNERSHIP);
		}
		if(!SecurityUtils.isOwner((Movie)item, oid)) {
			throw new Exception(Constants.ERR_NO_OWNERSHIP);
		}
		
		EntityManager mgr = getEntityManager();
        EntityTransaction tx = mgr.getTransaction();

		User u = getParent();
		Movie newCal = (Movie)item;
		Movie m = null;
		//GAEJ specific
		Key parentKey = KeyFactory.createKey(User.class.getSimpleName(), u.getKey().getId());
		Key key = KeyFactory.createKey(parentKey, Movie.class.getSimpleName(), newCal.getId());
		m = mgr.find(Movie.class, key);
		if(m == null) throw new Exception("Item by id [" + newCal.getId() + "] can not be found! Key [" + key + "]");

		if(type != null && type.equals(Constants.MOVIE_MODEL_ID)) {
			//not touching the calendar id and event pattern!
		} else
		if(type != null && type.equals(Constants.CALENDAR_MODEL_ID)) {	//update event pattern only if it is a calendar event update request
			if(action != null && action.equals("update")) {
				//=== not a delete request
				m.setCalendarId(newCal.getCalendarId() != null? newCal.getCalendarId():m.getCalendarId());
//				m.setEventPattern(newCal.getEventPattern());
			} else {
				//=== must be a delete request
				m.setCalendarId(null);
//				m.setEventPattern(null);
			}
		}
		m.setTitle(newCal.getTitle());
		m.setDescription(newCal.getDescription());
		m.setSearchResults(newCal.getSearchResults());
		m.setURL(newCal.getURL());
		m.setShared(newCal.getShared());
		m.setChannelPattern(newCal.getChannelPattern());
		m.setOwner(newCal.getOwner());
		m.setOid(newCal.getOid());
        tx.begin();
		mgr.persist(m);
		//=== update the cache
		CacheManager.saveUserCache(u, u.getMovie(), m);
        tx.commit();
        
		Long id = m.getKey().getId();
		m.setId(id);	//set the "surrogate key" - important as everything else is depending on this key not the real key.id!
		if(m != null) {
			System.out.println(m.getClass().getName() + " object updated with id '" + id 
//					+ "' p[" + m.getEventPattern() + "] " 
					+ " t[" + m.getTitle() + "] s[" + m.getSearchResults() + "] u[" + m.getURL() + "]");
		}
		
		//=== clear the cache!
//		CacheManager.clearUserCacheById(uid, allMoviesCache);

		return m;
	}

	public Object doUpdateItemForAnyone(Object item) throws Exception {
		EntityManager mgr = getEntityManager();
        EntityTransaction tx = mgr.getTransaction();

		User u = getParent();
		Movie newCal = (Movie)item;
		Movie m = null;
		//GAEJ specific
		Key parentKey = KeyFactory.createKey(User.class.getSimpleName(), u.getKey().getId());
		Key key = KeyFactory.createKey(parentKey, Movie.class.getSimpleName(), newCal.getId());
		m = mgr.find(Movie.class, key);
		if(type != null && type.equals(Constants.MOVIE_MODEL_ID)) {
			//not touching the calendar id and event pattern!
		} else
		if(type != null && type.equals(Constants.CALENDAR_MODEL_ID)) {	//update event pattern only if it is a calendar event update request
			if(action != null && action.equals("update")) {
				//=== not a delete request
				m.setCalendarId(newCal.getCalendarId() != null? newCal.getCalendarId():m.getCalendarId());
//				m.setEventPattern(newCal.getEventPattern());
			} else {
				//=== must be a delete request
				m.setCalendarId(null);
//				m.setEventPattern(null);
			}
		}
		m.setTitle(newCal.getTitle());
		m.setDescription(newCal.getDescription());
		m.setSearchResults(newCal.getSearchResults());
		m.setURL(newCal.getURL());
		m.setShared(newCal.getShared());
		m.setChannelPattern(newCal.getChannelPattern());	//TBD SECURITY: to review if this pose any risk!
		//m.setOwner(newCal.getOwner());	//SECURITY measure: DO NOT change the ownership based on the input as it could be a non-owner/anyone!
		m.setOid(newCal.getOid());
		m.setShuffled(newCal.isShuffled());
        tx.begin();
		mgr.persist(m);
        tx.commit();
        
		Long id = m.getKey().getId();
		m.setId(id);	//set the "surrogate key" - important as everything else is depending on this key not the real key.id!
		if(m != null) {
			System.out.println(m.getClass().getName() + " object updated with id '" + id 
//					+ "' p[" + m.getEventPattern() + "] "
					+ " t[" + m.getTitle() + "] s[" + m.getSearchResults() + "] u[" + m.getURL() + "]");
		}
		
		//=== clear the cache!
//		CacheManager.clearUserCacheById(uid, allMoviesCache);
		//=== update the cache
		CacheManager.saveUserCache(u, u.getMovie(), m);

		return m;
	}
	
	public Object doDeleteItem(long itemId) throws Exception {
		if(!isOwner(itemId)) {
			throw new Exception(Constants.ERR_NO_OWNERSHIP);
		}
		deleteCalendarForMovie(itemId);
		
		EntityManager mgr = getEntityManager();
        EntityTransaction tx = mgr.getTransaction();	
        
		User u = getParent();
		Movie mov = null, retVal = new Movie();
		//GAEJ specific
		Key parentKey = KeyFactory.createKey(User.class.getSimpleName(), u.getKey().getId());
		Key key = KeyFactory.createKey(parentKey, Movie.class.getSimpleName(), itemId);
		mov = mgr.find(Movie.class, key);
		mov.setId(mov.getKey().getId());		//GAEJ specific
		Long id = mov.getId();
		retVal.setId(id);	//set the "surrogate key" - important as everything else is depending on this key not the real key.id!
        tx.begin();
		mgr.remove(mov);
		//=== delete the cache
		CacheManager.deleteUserCache(u, mov);
        tx.commit();
//        CommonHandler.doDeleteItems(uid, new CalendarHandler(), mov);
        
        if(mov != null) {
			System.out.println(mov.getClass().getName() + " object deleted with id '" + id + "' " + mov.getTitle() + " " + mov.getSearchResults() + " " + mov.getURL());
		}
		//=== clear the cache!
//		CacheManager.clearUserCacheById(uid, allMoviesCache);

		return retVal;			
	}
	
	/**
	 * Delete all associated calendar(s), if any related to movieId passed.
	 * 
	 * @param movieId
	 * @throws Exception
	 */
	private void deleteCalendarForMovie(long movieId) throws Exception {
		User user = getParent();
		if(user != null) {
			List l = null;
			if((l = user.getCalendar()) != null) {
				calendarHandler = new CalendarHandler();
				calendarHandler.setUid(uid);
				Iterator<Calendar> it = l.iterator();
				Calendar cal = null;
				while(it.hasNext()) {
					cal = it.next();
					System.out.println(cal.getClass().getName() + ":MovieHandler:deleteCalendar movieId " + movieId + " cal '" + cal + "'");
					cal.setId(cal.getKey().getId());	//GAEJ specific
					if(cal != null && cal.getMovieId() != null && cal.getMovieId().longValue() == movieId) {
						System.out.println("found movieId " + movieId + " cal '" + cal + "'");
						calendarHandler.doDeleteItem(cal);
						System.out.println("deleted calendarId '" + cal.getId() + "' based on movieId '" + movieId + "'");
					}
				}
			}
		}
	}

	public String handleCalendar() throws Exception {
		CollectionResponse<Movie> cr = null;
		String retVal = "";
		
		try {
			User u = getParent();
			if(u != null) {
				Collection<Movie> l = u.getMovie();
				Iterator<Movie> it = l.iterator();
				Movie cal = null;
				while(it.hasNext()) {
					cal = it.next();
					cal.setId(cal.getKey().getId());	//GAEJ specific
					retVal += JsonUtil.toString(cal);
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
	
	public void listCalendar(long movieId) throws Exception {
		if(movieId <= 0) return;

		User user = getParent();
		if(user != null) {
			List l = null;
			if((l = user.getCalendar()) != null) {
				calendarHandler = new CalendarHandler();
				calendarHandler.setUid(uid);
				Iterator<Calendar> it = l.iterator();
				Calendar cal = null;
				while(it.hasNext()) {
					cal = it.next();
					cal.setId(cal.getKey().getId());	//GAEJ specific
					System.out.println("listCalendar for movieId '" + movieId + "' cal " + cal);
				}
			}
		}
	}
	
	/**
	 * Purge all events related to a logged in user.
	 */
	public String purgeEvents() throws Exception {
		CollectionResponse<Movie> cr = null;
		Collection<Movie> l;
		String retVal = "";
		
		try {
			User u = getParent();
			if(u != null && uid != null) {
				l = purgeScheduledMovies(uid, u.getMovie());
				Iterator<Movie> it = l.iterator();
				Movie cal = null;
				while(it.hasNext()) {
					cal = it.next();
					cal.setId(cal.getKey().getId());	//GAEJ specific
					retVal += JsonUtil.toString(cal);
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

	/**
	 * Purge all past events related to a logged in user.
	 */
	public String purgePastEvents() throws Exception {
		CollectionResponse<Movie> cr = null;
		Collection<Movie> l;
		String retVal = "";
		
		try {
			User u = getParent();
			if(u != null && uid != null) {
				l = purgePastScheduledMovies(uid);
				Iterator<Movie> it = l.iterator();
				Movie cal = null;
				while(it.hasNext()) {
					cal = it.next();
					cal.setId(cal.getKey().getId());	//GAEJ specific
					retVal += JsonUtil.toString(cal);
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
	public Object doDeleteItem(Object item) throws Exception {
		if(item == null) {
			throw new Exception(Constants.ERR_NO_ITEM_FOR_OWNERSHIP);
		}
		if(!SecurityUtils.isOwner((Movie)item, oid)) {
			throw new Exception(Constants.ERR_NO_OWNERSHIP);
		}

		Movie m = (Movie)item;
		long itemId = m.getId() == null ? 0 : m.getId();

		return doDeleteItem(itemId);
	}

	@Override
	public Object doMigrateAllItems(String uid) throws Exception {
		List<Movie> nList = new ArrayList<Movie>();
		
		if(action.equals(Constants.MIGRATE) && Constants.OWNED_BY_ME.equalsIgnoreCase(filter)) {
			User parent = getParent();
			List<Movie> l = DatastoreUtils.getAllLegacyMovies(parent, uid);
			Iterator<Movie> it = l.iterator();
			Movie m = null;

			while(it.hasNext()) {
				m = it.next();
				//=== only by (old) me!
				if(uid == null || uid.trim().equals("")) {
					System.out.println(this.getClass().getName() + ":" +Constants.OWNED_BY_ME + " CREATED by old me movieId, uid is null or empty! Please login again?");
				}
				else
				if(m != null && uid != null && uid.equals(AppUtils.getOwnerPart(m.getOwner()))) {
					nList.add(m);
					System.out.println(this.getClass().getName() + ":" +Constants.OWNED_BY_ME + " CREATED by old me movie [" + m + "] done");
				}
			}
		} else
		if(action.equals(Constants.MIGRATE) && Constants.MIGRATE_ALL.equalsIgnoreCase(filter)) {
			//TODO ********* migrate all logic - commented out in the front end as it does not work!!! :( ********* 
			User parent = getParent();
			List<Movie> l2 = DatastoreUtils.getAllLegacyMovies(parent, uid);
			Iterator<Movie> it2 = l2.iterator();
			Movie m = null;
			int i = 0, limit = 2;	//TODO until proven that it works, so that it is not destructive!!!
			
			List ml = parent.getMovie(); 
			if(ml == null) {
				ml = new ArrayList();
			}
			while(it2.hasNext()) {
				m = it2.next();
				//=== only by (old) me!
				if(uid == null || uid.trim().equals("")) {
					System.out.println(this.getClass().getName() + ":" +Constants.MIGRATE_ALL + " CREATED by old me movieId, uid is null or empty! Please login again?");
				}
				else
				if(m != null && uid != null && uid.equals(AppUtils.getOwnerPart(m.getOwner()))) {
					//=== dummy ids
					m.setId(-999L);
					m.setModified(new Date());
					try {
						//=== save the legacy item keyString!
						String legacyKeyString = m.getKeyString();	//from front end, might be hacked, thus hopefully it is over https!
						//=== create a new item
						DatastoreUtils.createLegacyMovie(parent, m);	//TODO java.lang.NullPointerException at com.google.appengine.datanucleus.EntityUtils.getKeyForObject(EntityUtils.java:236) during merge User entity!!!
						//=== delete the legacy item	//TODO uncomment only after the above works!
//						DatastoreUtils.removeLegacyMovie(uid, legacyKeyString);
					} catch (Exception e) {
						e.printStackTrace();
					}
					i++;
					if(i > limit) break;	//insurance for mistake!
					
					System.out.println(this.getClass().getName() + ":" +Constants.MIGRATE_ALL + " CREATED by old me movie [" + m + "] done");
				}
			}
		}
		
		return nList;
	}

	@Override
	public Object doMigratePurgeItem(String uid, String id) throws Exception {
		Movie m = null;
		
		if(action.equals(Constants.MIGRATE_DELETE)) {
			m = DatastoreUtils.removeLegacyMovie(uid, id);

			System.out.println(this.getClass().getName() + ": Purged old legacy movie with keystring [" + m.getKeyString() + "] movieId [" + m.getId() + "] title [" + m.getTitle() + "]");
		}
		
		return m;
	}
	
	@Override
	public Object doMigrateAllItems() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}