package app.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;

import app.model.Movie;
import app.model.User;

/**
 * This works with deprecated datastore API only e.g. addFilter. It will not work if you change the datastore API
 * to the latest and greatest and would not serve its purpose anyway.
 * 
 * @author macbook
 *
 */
public class DatastoreUtils {

	static DatastoreService datastore = DatastoreServiceFactory
			.getDatastoreService();

	/**
	 * Returns all legacy movie entities not visible by higher level API like JPA 2.0.
	 * 
	 * @param targetUserName
	 * @return
	 * @ref https://developers.google.com/appengine/docs/java/datastore/queries
	 */
	public static List<Movie> getAllLegacyMovies(User parent, String targetUserName) {
		//TODO for privacy issue, need a check to make sure only the owner has access to the legacy data!!!
		
		List<Movie> retVal = new ArrayList<Movie>();
		List<Entity> l = null;

		Query q = new Query("Movie").addFilter("owner", Query.FilterOperator.EQUAL, targetUserName);	//deprecated for JPA 2+, but just what we needs!
		//Query q = new Query("Movie").setFilter(new FilterPredicate("owner", FilterOperator.EQUAL, Boolean.TRUE));		//new style - keep it, might be the way to go in the future!

		PreparedQuery pq = datastore.prepare(q);
		l = pq.asList(
				FetchOptions.Builder.withDefaults()	//FetchOptions.Builder.withLimit(100)
				);
		Iterator<Entity> it = l.iterator();
		Entity en = null;
		Movie mo = null;
		long dummyCount = 1;
		while(it.hasNext()) {
			en = (Entity) it.next();
			mo = new Movie();
			mo.setId(dummyCount++);
			//mo.setKey(en.getKey());
			mo.setParent(parent);
			mo.setKeyString(KeyFactory.keyToString(en.getKey()));
			if(mo.getOwner() == null || mo.getOwner() != null && mo.getOwner().indexOf(Constants.MIGRATED_ENTITY) == -1) {
				mo.setOwner(AppUtils.createOwnerString(Constants.LEGACY_ENTITY, (String)en.getProperty("owner")));	//mo.setOwner((String) en.getProperty("owner")); //denoted to filter out
			}
			mo.setTitle((String) en.getProperty("Title"));
			mo.setURL((String) en.getProperty("URL"));
			mo.setOid((String) en.getProperty("oid"));
			mo.setShared((Boolean) en.getProperty("shared"));
			try {
				mo.setDescription(new Text((String) en.getProperty("description")));
			} catch (Exception e1) {
				//does not matter if it error out or not, just ignore it
			}
			try {
				//e.g. Fri Jul 05 20:52:31 CDT 2013
//				DateTimeFormatter formatter = DateTimeFormat.forPattern("E M d  HH:mm:ss yyyy");
//				DateTime dt = formatter.parseDateTime((String) en.getProperty("Modified") != null?(String) en.getProperty("Modified"):null);
//				mo.setModified(dt.toDate());
				mo.setModified((java.util.Date)en.getProperty("Modified"));
			} catch (Exception e2) {
				e2.printStackTrace();
				//does not matter if it error out or not, just ignore it
			}
			try {
				mo.setId((Long) en.getProperty("Id"));
			} catch (Exception e3) {
				//does not matter if it error out or not, just ignore it
			}
			retVal.add(mo);
		}

		return retVal;
	}
	
	/**
	 * The key to delete is the serialized key kept at the front end (identified by Id).
	 *
	 * @param targetUserName
	 * @param keyString the gaej key serialized as String
	 * @return
	 * @ref https://developers.google.com/appengine/docs/java/datastore/entities#Java_Deleting_an_entity
	 */
	public static Movie removeLegacyMovie(String targetUserName, String keyString) {
		//TODO for privacy issue, need a check to make sure only the owner has access to the legacy data!!!

		Key key = KeyFactory.stringToKey(keyString);
		datastore.delete(key);

		Movie retVal = new Movie();
		retVal.setKeyString(keyString);

		System.out.println("DatastoreUtils:removeLegacyMovie CREATED by old me movie keyString [" + keyString + "] removed!");

		return retVal;		
	}
	
	public static Movie createLegacyMovie(User parent, Movie movie) {
		String kindName = "Movie";

		KeyFactory.Builder b = new KeyFactory.Builder(parent.getKey());;
        Key id = b.addChild(Movie.class.getSimpleName(), kindName).getKey();

        Entity mo = new Entity(kindName, id);
		datastore.put(mo);

		System.out.println("DatastoreUtils:createLegacyMovie movie [" + kindName + "] with id [" + mo.getKey() + "] created");

		return movie;		
	}
	

}