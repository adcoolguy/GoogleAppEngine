package cloudserviceapi.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.model.Movie;
import app.model.User;

public class CacheManager {
    private static final Logger logger = LoggerFactory.getLogger(CacheManager.class);
	private static List<User> userCache = new ArrayList<User>();
//	private static String separator = ",";

	public static void addUserCache(User user) {
		if(userCache != null) {
			if(!userCache.contains(user))
				userCache.add(user);
		}
	}

	public static void updateUserCache(User user) {
		User u = null;
		List<User> newUserCache = new ArrayList<User>();
		if(userCache != null) {
			User tmp = null;
			for(int i=0; i<userCache.size(); i++) {
				tmp = userCache.get(i);
				logger.debug("CacheManager#updateUserCache: tmp id [" + tmp.getId() + "] user id [" + user.getId() + "]");
				if(tmp != null && user != null && tmp.getId() != null && user.getId() != null && tmp.getId().longValue() != user.getId().longValue()) {
					newUserCache.add(tmp);
				} else {
					newUserCache.add(user);
				}
			}
		}
		userCache = newUserCache;
	}

	public static User getUserCache(User user) {
		User u = null;
		if(userCache != null) {
			User tmp = null;
			for(int i=0; i<userCache.size(); i++) {
				tmp = userCache.get(i);
				if(tmp != null && user != null && tmp.getId() != null && user.getId() != null && tmp.getId().longValue() == user.getId().longValue()) {
					logger.debug("CacheManager#getUserCache: tmp id [" + tmp.getId() + "] user id [" + user.getId() + "]");
					u = tmp;
					break;
				} 
			}
		}
		return u;
	}

	public static void saveUserCache(User user, List<Movie> movieList, Movie targetMovie) {
		if(userCache != null) {
			List<Movie> newMovieList = new ArrayList<Movie>();
			User tmp = null;
			if(targetMovie != null) {
				Movie tmp1 = null;
				for(int j=0; j<movieList.size(); j++) {
					tmp1 = movieList.get(j);
					if(tmp1 != null && targetMovie != null && tmp1.getId() != null && targetMovie.getId() != null &&  tmp1.getId().longValue() != targetMovie.getId().longValue()) {
						newMovieList.add(tmp1);
					}
				}
				newMovieList.add(targetMovie);
				user.setMovie(newMovieList);
			} else {
				user.setMovie(movieList);
			}
			updateUserCache(user);
		}
	}

	public static void deleteUserCache(User user, Movie targetMovie) throws Exception {
		if(targetMovie == null) {
			throw new Exception("targetMovie is NULL or empty.");
		}

		User cachedUser = getUserCache(user);
		List<Movie> movieList = cachedUser.getMovie();
		if(cachedUser != null) {
			List<Movie> newMovieList = new ArrayList<Movie>();
			Movie tmp1 = null;
			for(int j=0; j<movieList.size(); j++) {
				tmp1 = movieList.get(j);
				if(tmp1 != null && targetMovie != null && tmp1.getId().longValue() != targetMovie.getId().longValue()) {
					newMovieList.add(tmp1);
				}
			}
			user.setMovie(newMovieList);
			updateUserCache(user);
		}
	}

//	public static User getUserCacheByName(User user) throws Exception {
//		if(user == null) {
//			throw new Exception("CacheManager:getUserCacheByName() User is null or empty!");
//		}
//		User ret = null;
//		if(userCache != null) {
//			User tmp = null;
//			for(int i=0; i<userCache.size(); i++) {
//				tmp = userCache.get(i);
//				if(tmp.getName().equals(user.getName())) {
//					ret = tmp;
//					break;
//				}
//			}
//		}
//		
//		return ret;
//	}

//	public static User getUserCacheByUID(String uid) throws Exception {
//		if(uid == null) {
//			throw new Exception("CacheManager:getUserCacheByUID() uid is null or empty!");
//		}
//		User user = new User();
//		user.setName(uid);
//		User ret = getUserCacheByName(user);
////		if(userCache != null) {
////			User tmp = null;
////			for(int i=0; i<userCache.size(); i++) {
////				tmp = userCache.get(i);
////				if(tmp.getId().longValye() == Long.valueOf(uid)) {
////					ret = tmp;
////					break;
////				}
////			}
////		}
//		
//		return ret;
//	}

//	public static String getUserCacheKey(String uid, String filter, long pageNumber) {
//		return uid + separator + filter + pageNumber;
//	}
//
//	public static void clearUserCacheById(String uid, HashMap cache) {
//		Iterator it = cache.entrySet().iterator();
//	    while (it.hasNext()) {
//	        Map.Entry pairs = (Map.Entry)it.next();
//	        //System.out.println(pairs.getKey() + " = " + pairs.getValue());
//	        String[] tk = pairs.getKey().toString().split(separator);
//	        if(tk[0] != null && tk[0].equals(uid)) {
//	        	it.remove(); // avoids a ConcurrentModificationException
//	        }
//	    }
//	}
	
}
