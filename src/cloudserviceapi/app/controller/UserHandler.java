package cloudserviceapi.app.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.cloudserviceapi.common.JsonUtil;
import com.appspot.cloudserviceapi.data.EMF;

import app.common.Constants;
import app.model.Calendar;
import app.model.Movie;
import app.model.User;
import app.model.UserEndpoint;

/**
 * Format:
 * 
 * Admin:
 * http://localhost:8888/_ah/admin/datastore?kind=User
 * GET LIST OF ITEMS:
 * http://localhost:8888/ws/crud?type=modelUser&uid=h
 * GET ITEM:
 * http://localhost:8888/ws/crud?type=modelUser&id=1
 * DELETE ITEM:
 * http://localhost:8888/ws/crud?type=modelUser&id=1&action=delete
 * CREATE ITEM:
 * http://localhost:8888/ws/crud?type=modelUser&id=1&action=create
 * id=1
 * name=name 1
 * description=text 1
 * UPDATE ITEM:
 * http://localhost:8888/ws/crud?type=modelUser&id=1&action=update
 * id=1
 * name=name 2
 * description=text 2
 *
 */
public class UserHandler implements CrudServiceCallback, ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(UserHandler.class);
	private static UserEndpoint dao;
	private String uid;

	static {
		dao = new UserEndpoint();
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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
	    User item = null;
		Movie mChildItem = null;
		Calendar cChildItem = null;
		String type = getValue(request, "type");

		if(type != null && type.equals(Constants.USER_MODEL_ID)) {
			item = new User();
			String temp = getValue(request, "id");
			// parse the rest of the information off of the request
			if(temp != null && !temp.trim().equals("")) {
				item.setId(new Long(temp));
			} else {
				item.setId(new Long(0L));
			}
			uid = getValue(request, "uid");
			item.setName(uid);
			/*
			//Movie specific attributes
			if(getValue(request, "title") != null) {
				mChildItem = new Movie();
				//view specific attributes
				mChildItem.setModified(new Date());
				//view specific attributes
				mChildItem.setTitle(getValue(request, "title"));
				mChildItem.setDescription(getValue(request, "description"));
				temp = getValue(request, "search_results");
				try {
					mChildItem.setSearchResults(new Text(temp));
					mChildItem.setURL(getValue(request, "url"));
					List<Movie> m = new ArrayList<Movie>();
					m.add(mChildItem);
					item.setMovie(m);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			*/
		}
		
		return item;
	}
	
	public Object doCreateItem(Object item) throws Exception {
		if(uid == null) {
			throw new Exception("User ID is NULL or empty.");
		}

		long id = -1;
		
		User u = new User();
		try {
			u.setName(uid);
			u = getUserByName(u);
			if(u != null && u.getKey() == null) {
				u = dao.insertUser((User) item);
				id = u.getKey().getId();
				if(id > -1) {
					u.setId(id);
					System.out.println(this.getClass().getName() + " object created with id '" + id + "'");
				} else {
					u = null;
				}
			} else {
				//u = (User)doUpdateItem(u);	//why would you update if the caller ask for insert/create?!!!
				u = null;
			}
		} catch (Exception e) {
			System.out.println("UserHandler:doCreateItem exception: " + e);			
			e.printStackTrace();
		}
		
		return u;
	}

	public String doGetItems() throws Exception {
		if(uid == null) {
			throw new Exception("User ID is NULL or empty.");
		}
		String retVal = "";
		
		try {
			User u = new User();
			u.setName(uid);
			u = getUserByName(u);
			if(u.getKey() != null) {
				u.setId(u.getKey().getId());
				//get the child
				if(u.getMovie() != null && u.getMovie().size() > 0) {
					Iterator<Movie> it2 = u.getMovie().iterator();
					while(it2.hasNext()) {
						retVal += JsonUtil.toString(it2.next());
					}
				}
				retVal = JsonUtil.toString(u);
				retVal = retVal.replaceAll("\\n", "");
				retVal = retVal.replaceAll("\\}\\{", "},{");
				retVal = "[" + retVal + "]";
			} else {
				retVal = "[]";
			}
		} catch (Exception e) {
			System.out.println("UserHandler:doGetItems exception: " + e);			
			e.printStackTrace();
		}
		
		return retVal;
	}

	public User getUserByName(User user) throws Exception {
		if(user == null) {
			throw new Exception("UserHandler:getUserByName() User is null or empty!");
		}
		User cachedUser = CacheManager.getUserCache(user);
		if(cachedUser != null) {
			return cachedUser;
		}
		
		System.out.println("UserHandler#getUserByName ...");
		EntityManager em = getEntityManager();
		try {
			logger.info("querying user by name [" + user.getName() + "]");
		    String queryStr = String.format("select u from " + User.class.getName() + " u" +
		    								" where u.name = :name");
            javax.persistence.Query query = em.createQuery(queryStr);
            query.setParameter("name", user.getName());
            //begin JPA2
            if(CollectionUtils.isNotEmpty(query.getResultList())) {
//            	user = (User) query.getSingleResult();	//ASS#1 assuming the user id is unique
                if(query.getResultList().size() == 1) {
                	user = (User) query.getSingleResult();
                } else if(query.getResultList().size() > 1) {
                	user = (User) query.getResultList().get(0);
                }
            }
            //end JPA2
            if(user.getKey() != null) {
    		    user.setId(user.getKey().getId());    //GAEJ specific
            }
			CacheManager.addUserCache(user);
		} catch (Exception e) {
			System.out.println("UserHandler:getUserByName exception: user [" + user.getName() + "] " + e);
		} finally {

			em.close();
		}

		return user;
	}
	
	public boolean exists(User u) {
		boolean ret = true;
		
		if(u != null && u.getKey() == null && u.getId() == null) {
			ret = false;	//if both are null, that means it is not found!
		}
		
		return ret;
	}

	public Object doGetItem(long itemId) throws Exception {
		Object retVal = null;
		
		try {
			retVal = dao.getUser(itemId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retVal;
	}

	public User persist(User user) {
		System.out.println("1 UserHandler:persist entered");
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			System.out.println("1 UserHandler:persist user [" + user.getName() + "] persisting ...");
			em.persist(user);
			em.getTransaction().commit();
			System.out.println("1 UserHandler:persist user [" + user.getName() + "] commited");
			em.refresh(user);
			System.out.println("1 UserHandler:persist user [" + user.getName() + "] refreshed");
		} catch (Exception e) {
			System.out.println("1 UserHandler:persist exception: user [" + user.getName() + "] " + e);
		} finally {

			em.close();
		}

		return user;
	}

/*	public User persist(User user) {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		try {
			em.persist(user);
			em.flush();
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("1 UserHandler:persist exception: user [" + user.getName() + "] " + e);
		} finally {

			em.close();
		}

		return user;
	}
*/	

	public User merge(User user) {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			System.out.println("2 UserHandler:merge user '" + user.getName() + "' merging ...");
			user = em.merge(user);
			em.getTransaction().commit();
			System.out.println("2 UserHandler:merge user '" + user.getName() + "' commited and refreshed");
			//em.refresh(user);
			//System.out.println("2 UserHandler:merge user '" + user.getName() + "' refreshed");
		} catch (Exception e) {
			e.printStackTrace();
			if(user != null) {
				String name = user.getName();
				System.out.println("2 :) NPE? UserHandler:merge exception: user '" + name + "' [" + e + "]");
			} else {
				System.out.println("UserHandler:merge exception: user is null [" + e + "]");
			}
		} finally {
			//begin JPA2
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
		    }		
			//end JPA2
			em.close();
		}

		return user;
	}
	
/*	public User merge(User user) {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		try {
			em.merge(user);
			em.flush();
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if(user != null) {
				String name = user.getName();
				System.out.println("2 UserHandler:merge exception: user '" + name + "' [" + e + "]");
			} else {
				System.out.println("UserHandler:merge exception: user is null [" + e + "]");
			}
		} finally {
			//begin JPA2
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
		    }		
			//end JPA2
			em.close();
		}

		return user;
	}
*/
	
	public User merge(User user, Movie movie) {
		System.out.println("3 UserHandler:merge entered");
		EntityManager em = getEntityManager();
		try {
			if(user != null) {
				em.getTransaction().begin();
				System.out.println("3 UserHandler:merge user '" + user.getName() + "' refreshed");
				List<Movie> cl = user.getMovie();
				if(cl != null) {
					cl.add(movie);
				} else {
					cl = new ArrayList<Movie>();
					cl.add(movie);
				}
				//id = cal.getKey().getId();
				//cal.setId(id);	//set the "surrogate key" - important as everything else is depending on this key not the real key.id!
				//System.out.println(this.getClass().getName() + " object inserted with id '" + id + "' " + cal.getTitle() + " " + cal.getURL());

				System.out.println("3 UserHandler:merge user '" + user.getName() + "' merging ...");
				em.merge(user);
				em.getTransaction().commit();
				System.out.println("3 UserHandler:merge user '" + user.getName() + "' commited");
				em.refresh(user);
				System.out.println("3 UserHandler:merge user '" + user.getName() + "' refreshed");
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {

			em.close();
		}

		return user;
	}

/*	public User merge(User user, Movie movie) {
		EntityManager em = getEntityManager();
		try {
			if(user != null) {
				em.getTransaction().begin();
				List<Movie> cl = user.getMovie();
				if(cl != null) {
					cl.add(movie);
				} else {
					cl = new ArrayList<Movie>();
					cl.add(movie);
				}
				//id = cal.getKey().getId();
				//cal.setId(id);	//set the "surrogate key" - important as everything else is depending on this key not the real key.id!
				//System.out.println(this.getClass().getName() + " object inserted with id '" + id + "' " + cal.getTitle() + " " + cal.getURL());

				em.merge(user);
				em.flush();
				em.getTransaction().commit();
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {

			em.close();
		}

		return user;
	}
*/
	public static User getUser(String uid) throws Exception {
		User retVal = null;
		User u = new User();
		UserHandler uh = new UserHandler();
		u.setName(uid);
		u = uh.getUserByName(u);
		if(u.getKey() != null) {
			retVal = u;
		} else {
			System.out.println("no user with uid [" + uid + "] found!");
		}
		return retVal;
	}
	
	public Object doPersistItem(Object item) throws Exception {
//		item = dao.updateUser((User) item);
		item = persist((User)item);
		
		return item;
	}
	
	public Object doUpdateItem(Object item) throws Exception {
		System.out.println("10.2 UserHandler:doUpdateItem item '" + item + "' ...");
		if(item != null) {
			User user = (User) item;
			System.out.println("10.3 UserHandler:doUpdateItem user name = '" + user.getName() + "'");			
		}
//		item = dao.updateUser((User) item);
		item = merge((User)item);
//		item = persist((User)item);
		System.out.println("20.2 UserHandler:doUpdateItem returned");

		return item;
	}

	public Object doUpdateItem(Object item, Movie movie) throws Exception {
		item = merge((User)item, movie);
		
		return item;
	}

	public Object doDeleteItem(long itemId) throws Exception {
		return dao.removeUser(itemId);
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
	
	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

	@Override
	public Object doDeleteItem(Object item) throws Exception {
		User u = (User)item;
		long itemId = u.getId() == null ? 0 : u.getId();

		return doDeleteItem(itemId);
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