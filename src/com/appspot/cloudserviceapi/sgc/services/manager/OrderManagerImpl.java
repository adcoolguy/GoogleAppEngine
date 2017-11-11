package com.appspot.cloudserviceapi.sgc.services.manager;

import java.util.List;

import com.appspot.cloudserviceapi.sgc.dao.WorkOrderDAO;

import tapp.model.sgc.WorkOrder;

public class OrderManagerImpl implements OrderManager {

//	private List<WorkOrder> myBeans = new ArrayList<WorkOrder>();
	private List<WorkOrder> myBeans = (new WorkOrderDAO()).getCloneList();

	public List<WorkOrder> getWorkOrders() {
//		return myBeans;
		return (new WorkOrderDAO()).getCloneList();
	}

	public void setWorkOrders(List<WorkOrder> myBeans) {
		this.myBeans = myBeans;
	}

	public void delete(Long id) {
		WorkOrder myBean = getWorkOrder(id);
		getWorkOrders().remove(myBean);
		(new WorkOrderDAO()).remove(id);
	}

	public void save(WorkOrder myBean) {
		if (myBeans.indexOf(myBean) > -1) {
			// update
			myBeans.set(myBeans.indexOf(myBean), myBean);
		} else {
			myBeans.add(myBean);
		}
		(new WorkOrderDAO()).save(myBean);
	}

	public WorkOrder getWorkOrder(Long id) {
		WorkOrder retVal = null;
//		for (WorkOrder myBean : myBeans) {
//			if (myBean.getId() != null && myBean.getId().equals(id)) {
//				retVal = myBean;
//			}
//		}
		retVal = (new WorkOrderDAO()).get(String.valueOf(id));
		return retVal;
	}

	@Override
	public String toString() {
		return myBeans.toString();
	}

}