package com.appspot.cloudserviceapi.sgc.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.appspot.cloudserviceapi.data.Persistence;
import com.appspot.cloudserviceapi.sgc.Protect;

import tapp.model.sgc.Client;

public class ClientDAO {

	// private static boolean purgeMode = true; //remember to change the id type
	// to String as well
	private boolean purgeMode = false; // the id type has to be Long

	public Client get(String primaryKey) {
		PersistenceManager pm = Persistence.getManager();
		Client wo = null;

		javax.jdo.Query query = pm.newQuery(Client.class, "id == pk");
		try {
			List<Client> results = null;
			if (purgeMode) {
				query.declareParameters("String pk");
				results = (List<Client>) query.execute(primaryKey);
			} else {
				query.declareParameters("Long pk");
				Long pk = Long.valueOf(primaryKey).longValue();
				results = (List<Client>) query.execute(pk);
			}
			if (results.iterator().hasNext()) {
				for (Client e : results) {
					System.out.println("client " + e.getFirstname() + ", "
							+ e.getLastname());
					wo = e;
					break;
				}
			} else {
				System.out.println("no client");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return wo;
	}

	public List getList(Long primaryKey) {
		PersistenceManager pm = Persistence.getManager();
		Client wo = null;
		List a = null;
		List<Client> results = null;
		javax.jdo.Query query = null;
		try {
			if (purgeMode) {
				query.declareParameters("String pk");
				results = (List<Client>) query.execute(primaryKey);
			} else {
				query.declareParameters("Long pk");
				Long pk = Long.valueOf(primaryKey).longValue();
				results = (List<Client>) query.execute(pk);
			}
			if (results.iterator().hasNext()) {
				a = new ArrayList();
				for (Client e : results) {
					System.out.println("client " + e.getFirstname() + ", "
							+ e.getLastname());
					a.add(e);
				}
			} else {
				System.out.println("no client");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return a;
	}

	public List getList() {
		PersistenceManager pm = Persistence.getManager();
		List<Client> results = null;
		javax.jdo.Query query = null;
		try {
			query = pm.newQuery(Client.class);
			results = (List<Client>) query.execute();
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
		List<Client> results = null;
		javax.jdo.Query query = null;
		try {
			query = pm.newQuery(Client.class);
			results = (List<Client>) query.execute();
		} finally {
			query.closeAll();
		}

		List<Client> woList = results; // getList();
		List<Client> clonedList = new ArrayList<Client>(woList.size());
		try {
			for (Client wo : woList) {
				clonedList.add((Client) wo.clone());
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		pm.close();
		return clonedList;
	}

	public void save(Client wo) {
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

		javax.jdo.Query query = pm.newQuery(Client.class, "id == pk");
		if (purgeMode) {
			query.declareParameters("String pk");
		} else {
			query.declareParameters("Long pk");
		}
		try {
			List<Client> results = (List<Client>) query.execute(primaryKey);
			String fName = null;
			String lName = null;
			if (results.iterator().hasNext()) {
				for (Client e : results) {
					fName = e.getFirstname();
					lName = e.getLastname();
					pm.deletePersistent(e);
					System.out.println("order from " + fName + ", " + lName
							+ " removed ");
				}
			} else {
				System.out.println("no client found for removal");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return retVal;
	}

	/**
	 * Use this with caution. It will remove all Client instances from the
	 * datastore!!!
	 */
	public boolean purgeClient() {
		boolean retVal = false;

		PersistenceManager pm = Persistence.getManager();
		if (Protect.isAdmin()) { // extra caution
			// Query
			Query query = pm.newQuery(Client.class);
			List<Client> results = (List<Client>) query.executeWithArray();
			// results = (List<Client>) pm.detachCopyAll(results);
			Iterator it = results.iterator();
			Client wo = null;
			while (it.hasNext()) {
				wo = (Client) it.next();
				// Delete
				try {
					pm.deletePersistent(wo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			retVal = true;
		}
		pm.close();

		return retVal;
	}
}
