package com.appspot.cloudserviceapi.sgc.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.appspot.cloudserviceapi.data.Persistence;
import com.appspot.cloudserviceapi.sgc.Protect;

import tapp.model.sgc.WorkOrder;

public class WorkOrderDAO {

	// private boolean purgeMode = true; //remember to change the id type to
	// String as well
	private boolean purgeMode = false; // the id type has to be Long

	public WorkOrder get(String primaryKey) {
		PersistenceManager pm = Persistence.getManager();
		WorkOrder wo = null;

		javax.jdo.Query query = pm.newQuery(WorkOrder.class, "id == pk");
		try {
			List<WorkOrder> results = null;
			if (purgeMode) {
				query.declareParameters("String pk");
				results = (List<WorkOrder>) query.execute(primaryKey);
			} else {
				query.declareParameters("Long pk");
				Long pk = Long.valueOf(primaryKey).longValue();
				results = (List<WorkOrder>) query.execute(pk);
			}
			if (results.iterator().hasNext()) {
				for (WorkOrder e : results) {
					System.out.println("order from " + e.getFirstName() + ", "
							+ e.getLastName() + " requested "
							+ e.getDateRequested() + " served "
							+ e.getDatePerformed());
					wo = e;
					break;
				}
			} else {
				System.out.println("no work order");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return wo;
	}

	public List getList(Long primaryKey) {
		PersistenceManager pm = Persistence.getManager();
		WorkOrder wo = null;
		List a = null;
		List<WorkOrder> results = null;
		javax.jdo.Query query = null;
		try {
			if (purgeMode) {
				query.declareParameters("String pk");
				results = (List<WorkOrder>) query.execute(primaryKey);
			} else {
				query.declareParameters("Long pk");
				Long pk = Long.valueOf(primaryKey).longValue();
				results = (List<WorkOrder>) query.execute(pk);
			}
			if (results.iterator().hasNext()) {
				a = new ArrayList();
				for (WorkOrder e : results) {
					System.out.println("order from " + e.getFirstName() + ", "
							+ e.getLastName() + " requested "
							+ e.getDateRequested() + " served "
							+ e.getDatePerformed());
					a.add(e);
				}
			} else {
				System.out.println("no work order");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return a;
	}

	public List getList() {
		PersistenceManager pm = Persistence.getManager();
		List<WorkOrder> results = null;
		javax.jdo.Query query = null;
		try {
			query = pm.newQuery(WorkOrder.class);
			results = (List<WorkOrder>) query.execute();
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
		List<WorkOrder> results = null;
		javax.jdo.Query query = null;
		try {
			query = pm.newQuery(WorkOrder.class);
			results = (List<WorkOrder>) query.execute();
		} finally {
			query.closeAll();
		}

		List<WorkOrder> woList = results; // getList();
		List<WorkOrder> clonedList = new ArrayList<WorkOrder>(woList.size());
		try {
			for (WorkOrder wo : woList) {
				clonedList.add((WorkOrder) wo.clone());
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		pm.close();
		return clonedList;
	}

	public void save(WorkOrder wo) {
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

		javax.jdo.Query query = pm.newQuery(WorkOrder.class, "id == pk");
		if (purgeMode) {
			query.declareParameters("String pk");
		} else {
			query.declareParameters("Long pk");
		}
		try {
			List<WorkOrder> results = (List<WorkOrder>) query
					.execute(primaryKey);
			String fName = null;
			String lName = null;
			if (results.iterator().hasNext()) {
				for (WorkOrder e : results) {
					fName = e.getFirstName();
					lName = e.getLastName();
					pm.deletePersistent(e);
					System.out.println("order from " + fName + ", " + lName
							+ " removed ");
				}
			} else {
				System.out.println("no work order found for removal");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return retVal;
	}

	/**
	 * Use this with caution. It will remove all Work Order instances from the
	 * datastore!!!
	 */
	public boolean purgeWorkOrder() {
		boolean retVal = false;

		PersistenceManager pm = Persistence.getManager();
		if (Protect.isAdmin()) { // extra caution
			// Query
			Query query = pm.newQuery(WorkOrder.class);
			List<WorkOrder> results = (List<WorkOrder>) query
					.executeWithArray();
			// results = (List<WorkOrder>) pm.detachCopyAll(results);
			Iterator it = results.iterator();
			WorkOrder wo = null;
			while (it.hasNext()) {
				wo = (WorkOrder) it.next();
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
