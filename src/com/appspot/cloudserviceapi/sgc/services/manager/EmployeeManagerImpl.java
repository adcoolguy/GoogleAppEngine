package com.appspot.cloudserviceapi.sgc.services.manager;

import java.util.List;

import com.appspot.cloudserviceapi.sgc.dao.EmployeeDAO;

import tapp.model.sgc.Employee;

public class EmployeeManagerImpl implements EmployeeManager {

//	private List<Employee> myBeans = new ArrayList<Employee>();
	private List<Employee> myBeans = (new EmployeeDAO()).getCloneList();

	public List<Employee> getEmployees() {
//		return myBeans;
		return (new EmployeeDAO()).getCloneList();
	}

	public void setEmployees(List<Employee> myBeans) {
		this.myBeans = myBeans;
	}

	public void delete(Long id) {
		Employee myBean = getEmployee(id);
		getEmployees().remove(myBean);
		(new EmployeeDAO()).remove(id);
	}

	public void save(Employee myBean) {
		if (myBeans.indexOf(myBean) > -1) {
			// update
			myBeans.set(myBeans.indexOf(myBean), myBean);
		} else {
			myBeans.add(myBean);
		}
		(new EmployeeDAO()).save(myBean);
	}

	public Employee getEmployee(Long id) {
		Employee retVal = null;
//		for (Employee myBean : myBeans) {
//			if (myBean.getId() != null && myBean.getId().equals(id)) {
//				retVal = myBean;
//			}
//		}
		retVal = (new EmployeeDAO()).get(String.valueOf(id));
		return retVal;
	}

	@Override
	public String toString() {
		return myBeans.toString();
	}

}