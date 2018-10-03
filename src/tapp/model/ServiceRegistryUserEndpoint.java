package tapp.model;

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
import org.datanucleus.store.appengine.query.JDOCursorHelper;

@Api(name = "serviceregistryuserendpoint")
public class ServiceRegistryUserEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public CollectionResponse<ServiceRegistryUser> listServiceRegistryUser(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<ServiceRegistryUser> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(ServiceRegistryUser.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<ServiceRegistryUser>) query.execute();
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (ServiceRegistryUser obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<ServiceRegistryUser> builder()
				.setItems(execute).setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	public ServiceRegistryUser getServiceRegistryUser(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		ServiceRegistryUser serviceregistryuser = null;
		try {
			serviceregistryuser = mgr.getObjectById(ServiceRegistryUser.class,
					id);
		} finally {
			mgr.close();
		}
		return serviceregistryuser;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param serviceregistryuser the entity to be inserted.
	 * @return The inserted entity.
	 */
	public ServiceRegistryUser insertServiceRegistryUser(
			ServiceRegistryUser serviceregistryuser) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsServiceRegistryUser(serviceregistryuser)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(serviceregistryuser);
		} finally {
			mgr.close();
		}
		return serviceregistryuser;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param serviceregistryuser the entity to be updated.
	 * @return The updated entity.
	 */
	public ServiceRegistryUser updateServiceRegistryUser(
			ServiceRegistryUser serviceregistryuser) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsServiceRegistryUser(serviceregistryuser)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(serviceregistryuser);
		} finally {
			mgr.close();
		}
		return serviceregistryuser;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	public ServiceRegistryUser removeServiceRegistryUser(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		ServiceRegistryUser serviceregistryuser = null;
		try {
			serviceregistryuser = mgr.getObjectById(ServiceRegistryUser.class,
					id);
			mgr.deletePersistent(serviceregistryuser);
		} finally {
			mgr.close();
		}
		return serviceregistryuser;
	}

	private boolean containsServiceRegistryUser(
			ServiceRegistryUser serviceregistryuser) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(ServiceRegistryUser.class,
					serviceregistryuser.getId());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}
