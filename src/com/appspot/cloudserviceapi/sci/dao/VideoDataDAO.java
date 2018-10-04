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

import tapp.model.sci.VideoData;

/**
 * This class makes use of both JPA and JDO.
 *
 */
public class VideoDataDAO {

	private boolean purgeMode = false; // the id type has to be String under purge mode
	
	public VideoData findVideoDataByLoginId(String username) {
		EntityManager em = EMF.get().createEntityManager();
		VideoData gud;
		try {
			javax.persistence.Query q = (javax.persistence.Query) em.createQuery("select u from tapp.model.sci.VideoData u where u.userId = ?1");
			q.setParameter(1, username);
			gud = (VideoData) q.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			gud = null;
		} finally {
			em.close();
		}
		return gud;
	}
	
	public VideoData get(String primaryKey) {
		PersistenceManager pm = Persistence.getManager();
		VideoData wo = null;

		javax.jdo.Query query = pm.newQuery(VideoData.class, "id == pk");
		try {
			List<VideoData> results = null;
			if (purgeMode) {
				query.declareParameters("String pk");
				results = (List<VideoData>) query.execute(primaryKey);
			} else {
				query.declareParameters("Long pk");
				Long pk = Long.valueOf(primaryKey).longValue();
				results = (List<VideoData>) query.execute(pk);
			}
			if (results.iterator().hasNext()) {
				for (VideoData e : results) {
					System.out.println("VideoData " + e.getId());
					wo = e;
					break;
				}
			} else {
				System.out.println("no VideoData");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return wo;
	}

	public List getList(String primaryKey) {
		PersistenceManager pm = Persistence.getManager();
		VideoData wo = null;
		List a = null;
		List<VideoData> results = null;
		javax.jdo.Query query = null;
		try {
			if (purgeMode) {
				query.declareParameters("String pk");
				results = (List<VideoData>) query.execute(primaryKey);
			} else {
				query.declareParameters("Long pk");
				Long pk = Long.valueOf(primaryKey).longValue();
				results = (List<VideoData>) query.execute(pk);
			}
			if (results.iterator().hasNext()) {
				a = new ArrayList();
				for (VideoData e : results) {
					System.out.println("VideoData " + e.getId());
					a.add(e);
				}
			} else {
				System.out.println("no VideoData");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return a;
	}

	public List getList() {
		PersistenceManager pm = Persistence.getManager();
		List<VideoData> results = null;
		javax.jdo.Query query = null;
		try {
			query = pm.newQuery(VideoData.class);
			results = (List<VideoData>) query.execute();
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
		List<VideoData> results = null;
		javax.jdo.Query query = null;
		try {
			query = pm.newQuery(VideoData.class);
			results = (List<VideoData>) query.execute();
		} finally {
			query.closeAll();
		}

		List<VideoData> woList = results; // getList();
		List<VideoData> clonedList = new ArrayList<VideoData>(woList.size());
		try {
			for (VideoData wo : woList) {
				clonedList.add((VideoData) wo.clone());
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		pm.close();
		return clonedList;
	}

	public void save(VideoData wo) {
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

		javax.jdo.Query query = pm.newQuery(VideoData.class, "id == pk");
		if (purgeMode) {
			query.declareParameters("String pk");
		} else {
			query.declareParameters("Long pk");
		}
		try {
			List<VideoData> results = (List<VideoData>) query.execute(primaryKey);
			String fName = null;
			String lName = null;
			if (results.iterator().hasNext()) {
				for (VideoData e : results) {
					fName = e.getVideoId();
					pm.deletePersistent(e);
					System.out.println("VideoData " + fName + " removed ");
				}
			} else {
				System.out.println("no VideoData found for removal");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return retVal;
	}

	/**
	 * Use this with caution. It will remove all VideoData instances from the
	 * datastore!!!
	 */
	public boolean purgeUser() {
		boolean retVal = false;

		PersistenceManager pm = Persistence.getManager();
		if (Protect.isAdmin()) { // extra caution
			// Query
			Query query = pm.newQuery(VideoData.class);
			List<VideoData> results = (List<VideoData>) query.executeWithArray();
			// results = (List<VideoData>) pm.detachCopyAll(results);
			Iterator it = results.iterator();
			VideoData wo = null;
			while (it.hasNext()) {
				wo = (VideoData) it.next();
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
	
	public Long exist(VideoData user) {
		PersistenceManager pm = Persistence.getManager();

		Long retVal = -1L;

		Query q = pm.newQuery(VideoData.class, "userId == :userId");
		q.setUnique(true);
		
		VideoData attachedUser = (VideoData) q.execute(user.getId());
		
		if(attachedUser != null) {
			retVal = attachedUser.getId();
		}
		
		pm.close();
		
		return retVal;
	}

}