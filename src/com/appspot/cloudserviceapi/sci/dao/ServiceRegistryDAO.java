package com.appspot.cloudserviceapi.sci.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.persistence.EntityManager;

import org.joda.time.DateTime;

import com.appspot.cloudserviceapi.common.Constants;
import com.appspot.cloudserviceapi.data.EMF;
import com.appspot.cloudserviceapi.data.Persistence;
import com.appspot.cloudserviceapi.sgc.Protect;
//import com.appspot.cloudserviceapi.util.CacheController;
//import com.appspot.cloudserviceapi.util.CacheKey;

import tapp.model.ServiceRegistry;

/**
 * This class makes use of both JPA and JDO.
 *
 */
public class ServiceRegistryDAO {

	private boolean purgeMode = false; // the id type has to be String under purge mode
	private static List<ServiceRegistry> clonedList;	//used by UI, acts as cache as CacheController is not working
	private static final String ALL_SR_LIST = "AllServiceRegistryList";
	
	public ServiceRegistry findServiceRegistryByService(String service) {
		ServiceRegistry sr = getServiceFromCache(service);
		ServiceRegistry gud;
		if(sr == null) {
			EntityManager em = EMF.get().createEntityManager();
			try {
				javax.persistence.Query q = (javax.persistence.Query) em.createQuery("select u from tapp.model.ServiceRegistry u where u.service = ?1");
				q.setParameter(1, service);
				gud = (ServiceRegistry) q.getSingleResult();
				//TODO commented out for now due to https://community.jboss.org/message/868254#868254
//			} catch (com.google.apphosting.api.ApiProxy.OverQuotaException e1) {
//				throw e1;
			} catch (Exception e) {
				//e.printStackTrace();
				gud = null;
			} finally {
				em.close();
			}
		} else {
			gud = sr;
		}
		return gud;
	}
	
	public ServiceRegistry get(long primaryKey) {
		PersistenceManager pm = Persistence.getManager();
		ServiceRegistry wo = null;

		javax.jdo.Query query = pm.newQuery(ServiceRegistry.class, "id == pk");
		try {
			List<ServiceRegistry> results = null;
			if (purgeMode) {
				query.declareParameters("String pk");
				results = (List<ServiceRegistry>) query.execute(primaryKey);
			} else {
				query.declareParameters("Long pk");
				Long pk = Long.valueOf(primaryKey).longValue();
				results = (List<ServiceRegistry>) query.execute(pk);
			}
			if (results.iterator().hasNext()) {
				for (ServiceRegistry e : results) {
					System.out.println("ServiceRegistry " + e.getId());
					wo = e;
					break;
				}
			} else {
				System.out.println("no ServiceRegistry");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return wo;
	}

	public List getList(long primaryKey) {
		PersistenceManager pm = Persistence.getManager();
		ServiceRegistry wo = null;
		List a = null;
		List<ServiceRegistry> results = null;
		javax.jdo.Query query = null;
		try {
			if (purgeMode) {
				query.declareParameters("String pk");
				results = (List<ServiceRegistry>) query.execute(primaryKey);
			} else {
				query.declareParameters("Long pk");
				Long pk = Long.valueOf(primaryKey).longValue();
				results = (List<ServiceRegistry>) query.execute(pk);
			}
			if (results.iterator().hasNext()) {
				a = new ArrayList();
				for (ServiceRegistry e : results) {
					System.out.println("ServiceRegistry " + e.getId());
					a.add(e);
				}
			} else {
				System.out.println("no ServiceRegistry");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return a;
	}

	public List getList() {
		PersistenceManager pm = Persistence.getManager();
		List<ServiceRegistry> results = null;
		javax.jdo.Query query = null;
		try {
			query = pm.newQuery(ServiceRegistry.class);
			results = (List<ServiceRegistry>) query.execute();
		} finally {
			query.closeAll();
			pm.close();
		}

		return results;
	}

	/**
	 * This does not seem to work, could be a bug related to the 4J JDO or
	 * the codes.
	 */
	public List getTransientList() {
		PersistenceManager pm = Persistence.getManager();

		List results = new ArrayList(getList());
		try {
			pm.makeTransientAll(results);
		} finally {
			pm.close();
		}

		return results;
	}

	//TBD - this could be the performance bottleneck hotspot! the size should be saved as a variable in a row/property instead
	public List getCloneList() {
		int limitForMemory = 50;
		int count = 0;
		//List<ServiceRegistry> clonedList = (List<ServiceRegistry>) CacheController.get(ALL_SR_LIST);
		if(clonedList == null) {	//cut time from 1 ms to 4 micro s for every SR page rendered (in grid) or saved!
			PersistenceManager pm = Persistence.getManager();
			List<ServiceRegistry> results = null;
			javax.jdo.Query query = null;
			try {
				query = pm.newQuery(ServiceRegistry.class);
				results = (List<ServiceRegistry>) query.execute();
			} finally {
				query.closeAll();
			}

			List<ServiceRegistry> woList = results; // getList();
			clonedList = new ArrayList<ServiceRegistry>(woList.size());
			try {
				for (ServiceRegistry wo : woList) {
					//=== to avoid "Out of memory" error during run time, get only those latest entries up till a year back!
					DateTime oneYearsAgo = new DateTime(); // should give you a DateTime representing 'now'
					oneYearsAgo = oneYearsAgo.minusYears(1);            // should give you 1 year ago
				    DateTime dt = new DateTime(wo.getLastUpdated());
					if (wo.getLastUpdated() != null && dt.isBefore(oneYearsAgo)) {
						if(count < limitForMemory) {
							clonedList.add((ServiceRegistry) wo.clone());
							count++;
						}
					}
				}
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			//CacheController.put(ALL_SR_LIST, clonedList);
			pm.close();
		}
		return clonedList;
	}

	private ServiceRegistry getServiceFromCache(String service) {
		ServiceRegistry ret = null;
		if(clonedList != null) {
			for (ServiceRegistry wo : clonedList) {
				if(wo.getService().trim().equals(service.trim())) {
					ret = wo;
					break;
				}
			}
		}
		
		return ret;
	}
	
	/** Warning: This will clear all entries in the cache and will cause a query for all the entries again from the datastore 
	 * and it is EXPENSIVE (could consume around 10% of the datastore read!!!!) */
	public static void clearCache() {
		//List<ServiceRegistry> clonedList = (List<ServiceRegistry>) CacheController.get(ALL_SR_LIST);
		if(clonedList != null) {
			clonedList = null;
			//CacheController.put(ALL_SR_LIST, clonedList);
		}
		System.out.println("ServiceRegistryDAO.java: clearCache() cache cleared!!!");
	}

	public void save(ServiceRegistry wo) {
		// XXX 4J JDO limitation? id can not be zero
		/*
		 * if(wo.getId() == 0) { //wo.setId(new Long(1));
		 * System.err.println("Id can not be zero!"); return; }
		 */
		PersistenceManager pm = Persistence.getManager();
		javax.jdo.Transaction tx = pm.currentTransaction();
		try {
		    tx.begin();
			pm.makePersistent(wo);	//SR2#1
			//List<ServiceRegistry> clonedList = (List<ServiceRegistry>) CacheController.get(ALL_SR_LIST);
			//update the cache too
			updateCache(wo);
			//CacheController.put(ALL_SR_LIST, clonedList);
		    tx.commit();
		} finally {
			pm.close();
		}
	}

	public void updateCache(ServiceRegistry wo) {
		List<ServiceRegistry> newList = new ArrayList<ServiceRegistry>();
		if(clonedList != null) {
			ServiceRegistry sr = null;
			for(int i = 0; i < clonedList.size(); i++) {
				sr = clonedList.get(i);
				if(sr != null && wo != null && sr.getId() != null && wo.getId() != null && !sr.getId().equals(wo.getId())) {
					newList.add(sr);
				}
			}
			newList.add(wo);
			clonedList = newList;
		}
	}

	public boolean remove(Long primaryKey) {
		PersistenceManager pm = Persistence.getManager();
		boolean retVal = false;

		javax.jdo.Query query = pm.newQuery(ServiceRegistry.class, "id == pk");
		if (purgeMode) {
			query.declareParameters("String pk");
		} else {
			query.declareParameters("Long pk");
		}
		try {
			List<ServiceRegistry> results = (List<ServiceRegistry>) query.execute(primaryKey);
			long id = -1L;
			if (results.iterator().hasNext()) {
				for (ServiceRegistry e : results) {
					id = e.getId();
					ServiceRegistry myBean = new ServiceRegistry();
					myBean.setService(e.getService());
					pm.deletePersistent(e);
					//List<ServiceRegistry> clonedList = (List<ServiceRegistry>) CacheController.get(ALL_SR_LIST);
					//=== remove the cache too
					removeCache(myBean);
					//CacheController.put(ALL_SR_LIST, clonedList);
					System.out.println("ServiceRegistry " + id + " removed ");
				}
			} else {
				System.out.println("no ServiceRegistry found for removal");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return retVal;
	}

	private void removeCache(ServiceRegistry e) {
		List<ServiceRegistry> newList = new ArrayList<ServiceRegistry>();
		ServiceRegistry sr = null;
		if(clonedList != null) {
			for(int i = 0; i < clonedList.size(); i++) {
				sr = clonedList.get(i);
				//System.out.println("sr [" + sr.getService() + "] targetService [" + targetService + "]");
				if(sr.getService() != null && !sr.getService().equals(e.getService())) {
					newList.add(sr);
				}
			}
			clonedList = newList;
		}
	}

	/**
	 * Use this with caution. It will remove all ServiceRegistry instances from the
	 * datastore!!!
	 */
	public boolean purge() {
		boolean retVal = false;

		PersistenceManager pm = Persistence.getManager();
		if (Protect.isAdmin()) { // extra caution
			// Query
			Query query = pm.newQuery(ServiceRegistry.class);
			List<ServiceRegistry> results = (List<ServiceRegistry>) query.executeWithArray();
			// results = (List<ServiceRegistry>) pm.detachCopyAll(results);
			Iterator it = results.iterator();
			ServiceRegistry wo = null;
			while (it.hasNext()) {
				wo = (ServiceRegistry) it.next();
				// Delete
				try {
					pm.deletePersistent(wo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			retVal = true;
		} else {
			throw new RuntimeException(Constants.NO_RIGHT_TO + "purge services!");
		}
		pm.close();

		return retVal;
	}
	
	public Long exist(ServiceRegistry service) {
		PersistenceManager pm = Persistence.getManager();

		Long retVal = -1L;

		Query q = pm.newQuery(ServiceRegistry.class, "service == :service");
		q.setUnique(true);
		
		ServiceRegistry attachedUser = (ServiceRegistry) q.execute(service.getService());
		
		if(attachedUser != null) {
			retVal = attachedUser.getId();
		}
		
		pm.close();
		
		return retVal;
	}

}