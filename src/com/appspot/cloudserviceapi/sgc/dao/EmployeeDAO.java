package com.appspot.cloudserviceapi.sgc.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.appspot.cloudserviceapi.data.Persistence;
import com.appspot.cloudserviceapi.sgc.Protect;

import tapp.model.sgc.Employee;

public class EmployeeDAO {

	// private boolean purgeMode = true; //remember to change the id type to
	// String as well
	private boolean purgeMode = false; // the id type has to be Long

	public Employee get(String primaryKey) {
		PersistenceManager pm = Persistence.getManager();
		Employee wo = null;

		javax.jdo.Query query = pm.newQuery(Employee.class, "id == pk");
		try {
			List<Employee> results = null;
			if (purgeMode) {
				query.declareParameters("String pk");
				results = (List<Employee>) query.execute(primaryKey);
			} else {
				query.declareParameters("Long pk");
				Long pk = Long.valueOf(primaryKey).longValue();
				results = (List<Employee>) query.execute(pk);
			}
			if (results.iterator().hasNext()) {
				for (Employee e : results) {
					System.out.println("Employee " + e.getName());
					wo = e;
					break;
				}
			} else {
				System.out.println("no Employee");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return wo;
	}

	public List getList(Long primaryKey) {
		PersistenceManager pm = Persistence.getManager();
		Employee wo = null;
		List a = null;
		List<Employee> results = null;
		javax.jdo.Query query = null;
		try {
			if (purgeMode) {
				query.declareParameters("String pk");
				results = (List<Employee>) query.execute(primaryKey);
			} else {
				query.declareParameters("Long pk");
				Long pk = Long.valueOf(primaryKey).longValue();
				results = (List<Employee>) query.execute(pk);
			}
			if (results.iterator().hasNext()) {
				a = new ArrayList();
				for (Employee e : results) {
					System.out.println("Employee " + e.getName());
					a.add(e);
				}
			} else {
				System.out.println("no Employee");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return a;
	}

	public List getList() {
		PersistenceManager pm = Persistence.getManager();
		List<Employee> results = null;
		javax.jdo.Query query = null;
		try {
			query = pm.newQuery(Employee.class);
			results = (List<Employee>) query.execute();
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
		List<Employee> results = null;
		javax.jdo.Query query = null;
		try {
			query = pm.newQuery(Employee.class);
			results = (List<Employee>) query.execute();
		} finally {
			query.closeAll();
		}

		List<Employee> woList = results; // getList();
		List<Employee> clonedList = new ArrayList<Employee>(woList.size());
		try {
			for (Employee wo : woList) {
				clonedList.add((Employee) wo.clone());
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		pm.close();
		return clonedList;
	}

	public void save(Employee wo) {
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

		javax.jdo.Query query = pm.newQuery(Employee.class, "id == pk");
		if (purgeMode) {
			query.declareParameters("String pk");
		} else {
			query.declareParameters("Long pk");
		}
		try {
			List<Employee> results = (List<Employee>) query.execute(primaryKey);
			String fName = null;
			String lName = null;
			if (results.iterator().hasNext()) {
				for (Employee e : results) {
					fName = e.getName();
					pm.deletePersistent(e);
					System.out.println("Employee " + fName + " removed ");
				}
			} else {
				System.out.println("no Employee found for removal");
			}
		} finally {
			query.closeAll();
			pm.close();
		}

		return retVal;
	}

	/**
	 * Use this with caution. It will remove all Employee instances from the
	 * datastore!!!
	 */
	public boolean purgeEmployee() {
		boolean retVal = false;

		PersistenceManager pm = Persistence.getManager();
		if (Protect.isAdmin()) { // extra caution
			// Query
			Query query = pm.newQuery(Employee.class);
			List<Employee> results = (List<Employee>) query.executeWithArray();
			// results = (List<Employee>) pm.detachCopyAll(results);
			Iterator it = results.iterator();
			Employee wo = null;
			while (it.hasNext()) {
				wo = (Employee) it.next();
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

	/**
	 * Check to see if the passed id is an employee id.
	 * 
	 * @param employeeId
	 * @return
	 */
	public boolean isEmployee(String employeeId) {
		boolean retVal = false;
		
		Employee e = get(employeeId);
		if(e != null) {
			retVal = true;
			System.out.println("Employee '" + employeeId + "' verified");
		}

		return retVal;
	}

	/**
	 * Check to see if the passed id is an employee id.
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isUserIdEmployee(String userId) {
		PersistenceManager pm = Persistence.getManager();
		boolean retVal = false;
		Long id = -1L;
		
		Query q = pm.newQuery(Employee.class, "userId == :userId");
		q.setUnique(true);
		Employee attachedUser = (Employee) q.execute(userId);
		if(attachedUser != null) {
			id = attachedUser.getId();
		}
		if(id > 0) {
			retVal = true;
			System.out.println("User id '" + userId + "' verified as employee");
		}

		return retVal;
	}

	public boolean isEmployeeIdUserIdMatched(Long employeeId, String userId) {
		PersistenceManager pm = Persistence.getManager();
		boolean retVal = false;
		Long id1 = -1L;
		String id2 = null;
		
		Query q = pm.newQuery(Employee.class, "userId == :userId");
		q.setUnique(true);
		Employee attachedUser = (Employee) q.execute(userId);
		if(attachedUser != null) {
			id1 = attachedUser.getId();
			id2 = attachedUser.getUserId();
		}		
		
		if(id1.equals(employeeId) && id2.endsWith(userId)) {
			retVal = true;
			System.out.println("User id '" + userId + "' " + id2 + " matched as employee's id " + id1);
		}

		return retVal;
	}
	
	public Long exist(Employee user) {
		PersistenceManager pm = Persistence.getManager();

		Long retVal = -1L;

		Query q = pm.newQuery(Employee.class, "userId == :userId");
		q.setUnique(true);
		
		Employee attachedUser = (Employee) q.execute(user.getUserId());
		
		if(attachedUser != null) {
			retVal = attachedUser.getId();
		}
		
		pm.close();
		
		return retVal;
	}
	
}
