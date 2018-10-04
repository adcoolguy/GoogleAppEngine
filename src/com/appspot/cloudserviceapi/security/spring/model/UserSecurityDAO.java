package com.appspot.cloudserviceapi.security.spring.model;

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

/**
 * This class makes use of both JPA (due to Spring Security) and JDO.
 *
 */
public class UserSecurityDAO {

	private boolean purgeMode = false; // the id type has to be String under purge mode
	
	public GaeUserDetails findUserDetailsByLoginId(String username) {
		EntityManager em = EMF.get().createEntityManager();
		GaeUserDetails gud;
		try {
			javax.persistence.Query q = (javax.persistence.Query) em.createQuery("select u from GaeUserDetails u where u.userId = ?1");
			q.setParameter(1, username);
			gud = (GaeUserDetails) q.getSingleResult();
		} catch (Exception e) {
//			e.printStackTrace();
			gud = null;
		} finally {
			em.close();
		}
		return gud;
	}
	
	public GaeUserDetails get(String primaryKey) {
		PersistenceManager pm = Persistence.getManager();
		GaeUserDetails wo = null;

		javax.jdo.Query query = pm.newQuery(GaeUserDetails.class, "id == pk");
		try {
			List<GaeUserDetails> results = null;
			if (purgeMode) {
				query.declareParameters("String pk");
				results = (List<GaeUserDetails>) query.execute(primaryKey);
			} else {
				query.declareParameters("Long pk");
				Long pk = Long.valueOf(primaryKey).longValue();
				results = (List<GaeUserDetails>) query.execute(pk);
			}
			if (results.iterator().hasNext()) {
				for (GaeUserDetails e : results) {
					System.out.println("GaeUserDetails " + e.getUsername());
					wo = e;
					break;
				}
			} else {
				System.out.println("no GaeUserDetails");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return wo;
	}

	public List getList(String primaryKey) {
		PersistenceManager pm = Persistence.getManager();
		GaeUserDetails wo = null;
		List a = null;
		List<GaeUserDetails> results = null;
		javax.jdo.Query query = null;
		try {
			if (purgeMode) {
				query.declareParameters("String pk");
				results = (List<GaeUserDetails>) query.execute(primaryKey);
			} else {
				query.declareParameters("Long pk");
				Long pk = Long.valueOf(primaryKey).longValue();
				results = (List<GaeUserDetails>) query.execute(pk);
			}
			if (results.iterator().hasNext()) {
				a = new ArrayList();
				for (GaeUserDetails e : results) {
					System.out.println("GaeUserDetails " + e.getUsername());
					a.add(e);
				}
			} else {
				System.out.println("no GaeUserDetails");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return a;
	}

	public List getList() {
		PersistenceManager pm = Persistence.getManager();
		List<GaeUserDetails> results = null;
		javax.jdo.Query query = null;
		try {
			query = pm.newQuery(GaeUserDetails.class);
			results = (List<GaeUserDetails>) query.execute();
		} finally {
			query.closeAll();
			pm.close();
		}

		return results;
	}

	/**
	 * This does not seem to work, could be a bug related to the GAE4J JDO or
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
		List<GaeUserDetails> results = null;
		javax.jdo.Query query = null;
		try {
			query = pm.newQuery(GaeUserDetails.class);
			results = (List<GaeUserDetails>) query.execute();
		} finally {
			query.closeAll();
		}

		List<GaeUserDetails> woList = results; // getList();
		List<GaeUserDetails> clonedList = new ArrayList<GaeUserDetails>(woList.size());
		try {
			for (GaeUserDetails wo : woList) {
				clonedList.add((GaeUserDetails) wo.clone());
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		pm.close();
		return clonedList;
	}

	public void save(GaeUserDetails wo) {
		// XXX GAE4J JDO limitation? id can not be zero
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

		javax.jdo.Query query = pm.newQuery(GaeUserDetails.class, "id == pk");
		if (purgeMode) {
			query.declareParameters("String pk");
		} else {
			query.declareParameters("Long pk");
		}
		try {
			List<GaeUserDetails> results = (List<GaeUserDetails>) query.execute(primaryKey);
			String fName = null;
			String lName = null;
			if (results.iterator().hasNext()) {
				for (GaeUserDetails e : results) {
					fName = e.getUsername();
					pm.deletePersistent(e);
					System.out.println("GaeUserDetails " + fName + " removed ");
				}
			} else {
				System.out.println("no GaeUserDetails found for removal");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return retVal;
	}

	/**
	 * Use this with caution. It will remove all GaeUserDetails instances from the
	 * datastore!!!
	 */
	public boolean purgeUser() {
		boolean retVal = false;

		PersistenceManager pm = Persistence.getManager();
		if (Protect.isAdmin()) { // extra caution
			// Query
			Query query = pm.newQuery(GaeUserDetails.class);
			List<GaeUserDetails> results = (List<GaeUserDetails>) query.executeWithArray();
			// results = (List<GaeUserDetails>) pm.detachCopyAll(results);
			Iterator it = results.iterator();
			GaeUserDetails wo = null;
			while (it.hasNext()) {
				wo = (GaeUserDetails) it.next();
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
	
	public Long exist(GaeUserDetails user) {
		PersistenceManager pm = Persistence.getManager();

		Long retVal = -1L;

		Query q = pm.newQuery(GaeUserDetails.class, "userId == :userId");
		q.setUnique(true);
		
		GaeUserDetails attachedUser = (GaeUserDetails) q.execute(user.getUserId());
		
		if(attachedUser != null) {
			retVal = attachedUser.getId();
		}
		
		pm.close();
		
		return retVal;
	}

	public GaeUserDetails getGaeUserDetails(String userId) {
		PersistenceManager pm = Persistence.getManager();

		GaeUserDetails retVal = null;

		Query q = pm.newQuery(GaeUserDetails.class, "userId == :userId");
		q.setUnique(true);
		
		GaeUserDetails attachedUser = (GaeUserDetails) q.execute(userId);
		
		if(attachedUser != null) {
			retVal = attachedUser;
		}
		
		pm.close();
		
		return retVal;
	}

	public String getRole(String userId) {
		PersistenceManager pm = Persistence.getManager();

		String retVal = null;

		Query q = pm.newQuery(GaeUserDetails.class, "userId == :userId");
		q.setUnique(true);
		
		GaeUserDetails attachedUser = (GaeUserDetails) q.execute(userId);
		
		if(attachedUser != null) {
			if(attachedUser.getUserRole() != null) {
				retVal = attachedUser.getUserRole().toString();
			}
		}
		
		pm.close();
		
		return retVal;
	}

	public List<GaeUserDetails> getUserByRole(String userRole) {
		PersistenceManager pm = Persistence.getManager();

		List<GaeUserDetails> retVal = null;

		Query q = pm.newQuery(GaeUserDetails.class, "userRole == :userRole");
		//q.setUnique(true);
		
		List<GaeUserDetails> attachedUser = (List) q.execute(userRole);
		
		if(attachedUser != null) {
			if(attachedUser != null && attachedUser.size() > 0) {
				retVal = attachedUser;
			}
		}
		
		pm.close();
		
		return retVal;
	}
}