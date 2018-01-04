package app.model;

import java.util.HashMap;
import java.util.List;

//import javax.annotation.Nullable;
import com.google.gdata.util.common.base.Nullable;
import javax.inject.Named;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import com.appspot.cloudserviceapi.data.PMF;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;


@Api(name = "calendarendpoint")
public class CalendarEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public CollectionResponse<Calendar> listCalendar(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Calendar> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Calendar.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Calendar>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Calendar obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Calendar> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	public Calendar getCalendar(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Calendar calendar = null;
		try {
			calendar = mgr.getObjectById(Calendar.class, id);
		} finally {
			mgr.close();
		}
		return calendar;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param calendar the entity to be inserted.
	 * @return The inserted entity.
	 */
	public Calendar insertCalendar(Calendar calendar) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsCalendar(calendar)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(calendar);
		} finally {
			mgr.close();
		}
		return calendar;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param calendar the entity to be updated.
	 * @return The updated entity.
	 */
	public Calendar updateCalendar(Calendar calendar) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsCalendar(calendar)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(calendar);
		} finally {
			mgr.close();
		}
		return calendar;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	public Calendar removeCalendar(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Calendar calendar = null;
		try {
			calendar = mgr.getObjectById(Calendar.class, id);
			mgr.deletePersistent(calendar);
		} finally {
			mgr.close();
		}
		return calendar;
	}

	private boolean containsCalendar(Calendar calendar) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Calendar.class, calendar.getId());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			mgr.close();
		}
		return contains;
	}

	public static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}
