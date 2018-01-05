package com.appspot.cloudserviceapi.sci.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.persistence.EntityManager;

import com.appspot.cloudserviceapi.common.model.Constants;
import com.appspot.cloudserviceapi.data.EMF;
import com.appspot.cloudserviceapi.data.Persistence;
import com.appspot.cloudserviceapi.sgc.Protect;

import tapp.model.sci.RepositoryData;

/**
 * This class makes use of both JPA and JDO.
 *
 */
public class RepositoryDataDAO {

	private boolean purgeMode = false; // the id type has to be String under purge mode
	
	public RepositoryData findRepositoryDataByLoginId(String username) {
		EntityManager em = EMF.get().createEntityManager();
		RepositoryData gud;
		try {
			javax.persistence.Query q = (javax.persistence.Query) em.createQuery("select u from tapp.model.sci.RepositoryData u where u.userId = ?1");
			q.setParameter(1, username);
			gud = (RepositoryData) q.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			gud = null;
		} finally {
			em.close();
		}
		return gud;
	}
	
	public RepositoryData get(Long primaryKey) {
		PersistenceManager pm = Persistence.getManager();
		RepositoryData wo = null;

		javax.jdo.Query query = pm.newQuery(RepositoryData.class, "id == pk");
		try {
			List<RepositoryData> results = null;
			if (purgeMode) {
				query.declareParameters("String pk");
				results = (List<RepositoryData>) query.execute(primaryKey);
			} else {
				query.declareParameters("Long pk");
				Long pk = Long.valueOf(primaryKey).longValue();
				results = (List<RepositoryData>) query.execute(pk);
			}
			if (results.iterator().hasNext()) {
				for (RepositoryData e : results) {
					System.out.println("RepositoryData " + e.getId());
					wo = e;
					break;
				}
			} else {
				System.out.println("no RepositoryData");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return wo;
	}

	public List getList(Long primaryKey) {
		PersistenceManager pm = Persistence.getManager();
		RepositoryData wo = null;
		List a = null;
		List<RepositoryData> results = null;
		javax.jdo.Query query = null;
		try {
			if (purgeMode) {
				query.declareParameters("String pk");
				results = (List<RepositoryData>) query.execute(primaryKey);
			} else {
				query.declareParameters("Long pk");
				Long pk = Long.valueOf(primaryKey).longValue();
				results = (List<RepositoryData>) query.execute(pk);
			}
			if (results.iterator().hasNext()) {
				a = new ArrayList();
				for (RepositoryData e : results) {
					System.out.println("RepositoryData " + e.getId());
					a.add(e);
				}
			} else {
				System.out.println("no RepositoryData");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return a;
	}

	public List getList() {
		PersistenceManager pm = Persistence.getManager();
		List<RepositoryData> results = null;
		javax.jdo.Query query = null;
		try {
			query = pm.newQuery(RepositoryData.class);
			results = (List<RepositoryData>) query.execute();
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

	public List getCloneList() {
		PersistenceManager pm = Persistence.getManager();
		List<RepositoryData> results = null;
		javax.jdo.Query query = null;
		try {
			query = pm.newQuery(RepositoryData.class);
			results = (List<RepositoryData>) query.execute();
		} finally {
			query.closeAll();
		}

		List<RepositoryData> woList = results; // getList();
		List<RepositoryData> clonedList = new ArrayList<RepositoryData>(woList.size());
		try {
			for (RepositoryData wo : woList) {
				clonedList.add((RepositoryData) wo.clone());
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		pm.close();
		return clonedList;
	}

	public void save(RepositoryData wo) {
		// XXX 4J JDO limitation? id can not be zero
		/*
		 * if(wo.getId() == 0) { //wo.setId(new Long(1));
		 * System.err.println("Id can not be zero!"); return; }
		 */
		PersistenceManager pm = Persistence.getManager();
		try {
			pm.makePersistent(wo);
		} finally {
			pm.close();
		}
	}

	public boolean remove(Long primaryKey) {
		PersistenceManager pm = Persistence.getManager();
		boolean retVal = false;

		javax.jdo.Query query = pm.newQuery(RepositoryData.class, "id == pk");
		if (purgeMode) {
			query.declareParameters("String pk");
		} else {
			query.declareParameters("Long pk");
		}
		try {
			List<RepositoryData> results = (List<RepositoryData>) query.execute(primaryKey);
			Long id = null;
			if (results.iterator().hasNext()) {
				for (RepositoryData e : results) {
					id = e.getId();
					pm.deletePersistent(e);
					System.out.println("RepositoryData " + id + " removed ");
				}
			} else {
				System.out.println("no RepositoryData found for removal");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return retVal;
	}

	/**
	 * Use this with caution. It will remove all RepositoryData instances from the
	 * datastore!!!
	 */
	public boolean purgeUser() {
		boolean retVal = false;

		PersistenceManager pm = Persistence.getManager();
		if (Protect.isAdmin()) { // extra caution
			// Query
			Query query = pm.newQuery(RepositoryData.class);
			List<RepositoryData> results = (List<RepositoryData>) query.executeWithArray();
			// results = (List<RepositoryData>) pm.detachCopyAll(results);
			Iterator it = results.iterator();
			RepositoryData wo = null;
			while (it.hasNext()) {
				wo = (RepositoryData) it.next();
				// Delete
				try {
					pm.deletePersistent(wo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			retVal = true;
		} else {
			throw new RuntimeException(Constants.NO_RIGHT_TO + "purge users!");
		}
		pm.close();

		return retVal;
	}
	
	public Long exist(RepositoryData user) {
		PersistenceManager pm = Persistence.getManager();

		Long retVal = -1L;

		Query q = pm.newQuery(RepositoryData.class, "userId == :userId");
		q.setUnique(true);
		
		RepositoryData attachedUser = (RepositoryData) q.execute(user.getId());
		
		if(attachedUser != null) {
			retVal = attachedUser.getId();
		}
		
		pm.close();
		
		return retVal;
	}

}