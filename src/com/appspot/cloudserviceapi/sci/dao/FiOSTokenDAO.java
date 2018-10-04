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

import tapp.model.sci.FiOSToken;

/**
 * This class makes use of both JPA and JDO.
 *
 */
public class FiOSTokenDAO {

	private boolean purgeMode = false; // the id type has to be String under purge mode
	
	public FiOSToken findFiOSTokenByLoginId(String username) {
		EntityManager em = EMF.get().createEntityManager();
		FiOSToken gud;
		try {
			javax.persistence.Query q = (javax.persistence.Query) em.createQuery("select u from tapp.model.sci.FiOSToken u where u.userId = ?1");
			q.setParameter(1, username);
			gud = (FiOSToken) q.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			gud = null;
		} finally {
			em.close();
		}
		return gud;
	}
	
	public FiOSToken get(String primaryKey) {
		PersistenceManager pm = Persistence.getManager();
		FiOSToken wo = null;

		javax.jdo.Query query = pm.newQuery(FiOSToken.class, "id == pk");
		try {
			List<FiOSToken> results = null;
			if (purgeMode) {
				query.declareParameters("String pk");
				results = (List<FiOSToken>) query.execute(primaryKey);
			} else {
				query.declareParameters("Long pk");
				Long pk = Long.valueOf(primaryKey).longValue();
				results = (List<FiOSToken>) query.execute(pk);
			}
			if (results.iterator().hasNext()) {
				for (FiOSToken e : results) {
					System.out.println("FiOSToken " + e.getUserId());
					wo = e;
					break;
				}
			} else {
				System.out.println("no FiOSToken");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return wo;
	}

	public List getList(String primaryKey) {
		PersistenceManager pm = Persistence.getManager();
		FiOSToken wo = null;
		List a = null;
		List<FiOSToken> results = null;
		javax.jdo.Query query = null;
		try {
			if (purgeMode) {
				query.declareParameters("String pk");
				results = (List<FiOSToken>) query.execute(primaryKey);
			} else {
				query.declareParameters("Long pk");
				Long pk = Long.valueOf(primaryKey).longValue();
				results = (List<FiOSToken>) query.execute(pk);
			}
			if (results.iterator().hasNext()) {
				a = new ArrayList();
				for (FiOSToken e : results) {
					System.out.println("FiOSToken " + e.getUserId());
					a.add(e);
				}
			} else {
				System.out.println("no FiOSToken");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return a;
	}

	public List getList() {
		PersistenceManager pm = Persistence.getManager();
		List<FiOSToken> results = null;
		javax.jdo.Query query = null;
		try {
			query = pm.newQuery(FiOSToken.class);
			results = (List<FiOSToken>) query.execute();
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
		List<FiOSToken> results = null;
		javax.jdo.Query query = null;
		try {
			query = pm.newQuery(FiOSToken.class);
			results = (List<FiOSToken>) query.execute();
		} finally {
			query.closeAll();
		}

		List<FiOSToken> woList = results; // getList();
		List<FiOSToken> clonedList = new ArrayList<FiOSToken>(woList.size());
		try {
			for (FiOSToken wo : woList) {
				clonedList.add((FiOSToken) wo.clone());
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		pm.close();
		return clonedList;
	}

	public void save(FiOSToken wo) {
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

		javax.jdo.Query query = pm.newQuery(FiOSToken.class, "id == pk");
		if (purgeMode) {
			query.declareParameters("String pk");
		} else {
			query.declareParameters("Long pk");
		}
		try {
			List<FiOSToken> results = (List<FiOSToken>) query.execute(primaryKey);
			String fName = null;
			String lName = null;
			if (results.iterator().hasNext()) {
				for (FiOSToken e : results) {
					fName = e.getUserId();
					pm.deletePersistent(e);
					System.out.println("FiOSToken " + fName + " removed ");
				}
			} else {
				System.out.println("no FiOSToken found for removal");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return retVal;
	}

	/**
	 * Use this with caution. It will remove all FiOSToken instances from the
	 * datastore!!!
	 */
	public boolean purgeUser() {
		boolean retVal = false;

		PersistenceManager pm = Persistence.getManager();
		if (Protect.isAdmin()) { // extra caution
			// Query
			Query query = pm.newQuery(FiOSToken.class);
			List<FiOSToken> results = (List<FiOSToken>) query.executeWithArray();
			// results = (List<FiOSToken>) pm.detachCopyAll(results);
			Iterator it = results.iterator();
			FiOSToken wo = null;
			while (it.hasNext()) {
				wo = (FiOSToken) it.next();
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
	
	public Long exist(FiOSToken user) {
		PersistenceManager pm = Persistence.getManager();

		Long retVal = -1L;

		Query q = pm.newQuery(FiOSToken.class, "userId == :userId");
		q.setUnique(true);
		
		FiOSToken attachedUser = (FiOSToken) q.execute(user.getUserId());
		
		if(attachedUser != null) {
			retVal = attachedUser.getId();
		}
		
		pm.close();
		
		return retVal;
	}

	/**
	 * Authenticate user based on their user id and passcode.
	 * 
	 * @param user
	 * @return
	 */
	public boolean isValid(FiOSToken user) {
		EntityManager em = EMF.get().createEntityManager();
		FiOSToken gud = null;
		boolean retVal = false;
		try {
		    String queryStr = String.format("select from " + FiOSToken.class.getName() + 
		            " where id = %d", user.getId());
		    gud = (FiOSToken) em.createQuery(queryStr).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		if(gud != null && gud.getGeneratedPasscode().equals(user.getPasscode())) retVal = true;
		
		return retVal;
	}

	/*
	public String getRole(String userId) {
		PersistenceManager pm = Persistence.getManager();

		String retVal = null;

		Query q = pm.newQuery(FiOSToken.class, "userId == :userId");
		q.setUnique(true);
		
		FiOSToken attachedUser = (FiOSToken) q.execute(userId);
		
		if(attachedUser != null) {
			if(attachedUser.getUserRole() != null) {
				retVal = attachedUser.getUserRole().toString();
			}
		}
		
		pm.close();
		
		return retVal;
	}
	*/
}