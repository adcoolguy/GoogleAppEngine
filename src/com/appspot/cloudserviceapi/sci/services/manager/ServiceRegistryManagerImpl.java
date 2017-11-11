package com.appspot.cloudserviceapi.sci.services.manager;

import java.util.List;

import com.appspot.cloudserviceapi.sci.dao.ServiceRegistryDAO;

import tapp.model.ServiceRegistry;

public class ServiceRegistryManagerImpl implements ServiceRegistryManager {

//	private List<User> myBeans = new ArrayList<User>();
	private List<ServiceRegistry> myBeans = (new ServiceRegistryDAO()).getCloneList();

	public List<ServiceRegistry> getServiceRegistrys() {	//jprofiler - 120 ms (invoked 10 times, avg 12 ms each)
//		return myBeans;
		return (new ServiceRegistryDAO()).getCloneList();
	}

	public void setServiceRegistrys(List<ServiceRegistry> myBeans) {
		this.myBeans = myBeans;
	}

	public void delete(Long id) {
		ServiceRegistry myBean = getServiceRegistry(id);
//		getServiceRegistrys().remove(myBean);	//redundant as getServiceRegistrys() already getting the latest one always!
		(new ServiceRegistryDAO()).remove(id);
	}

	public void save(ServiceRegistry myBean) {
		if (myBeans.indexOf(myBean) > -1) {
			// update
			myBeans.set(myBeans.indexOf(myBean), myBean);
		} else {
			myBeans.add(myBean);
		}
		(new ServiceRegistryDAO()).save(myBean);
	}

	public ServiceRegistry getServiceRegistry(Long id) {
		ServiceRegistry retVal = null;
//		for (ServiceRegistry myBean : myBeans) {
//			if (myBean.getId() != null && myBean.getId().equals(id)) {
//				retVal = myBean;
//			}
//		}
		retVal = (new ServiceRegistryDAO()).get(id);
		return retVal;
	}

	@Override
	public String toString() {
		return myBeans.toString();
	}

}