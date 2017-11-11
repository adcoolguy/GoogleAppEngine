package com.appspot.cloudserviceapi.sgc.services.manager;

import java.util.List;

import com.appspot.cloudserviceapi.sgc.dao.ClientDAO;

import tapp.model.sgc.Client;

public class ClientManagerImpl implements ClientManager {

//	private List<Client> myBeans = new ArrayList<Client>();
	private List<Client> myBeans = (new ClientDAO()).getCloneList();

	public List<Client> getClients() {
//		return myBeans;
		return (new ClientDAO()).getCloneList();
	}

	public void setClients(List<Client> myBeans) {
		this.myBeans = myBeans;
	}

	public void delete(Long id) {
		Client myBean = getClient(id);
		getClients().remove(myBean);
		(new ClientDAO()).remove(id);
	}

	public void save(Client myBean) {
		if (myBeans.indexOf(myBean) > -1) {
			// update
			myBeans.set(myBeans.indexOf(myBean), myBean);
		} else {
			myBeans.add(myBean);
		}
		(new ClientDAO()).save(myBean);
	}

	public Client getClient(Long id) {
		Client retVal = null;
//		for (Client myBean : myBeans) {
//			if (myBean.getId() != null && myBean.getId().equals(id)) {
//				retVal = myBean;
//			}
//		}
		retVal = (new ClientDAO()).get(String.valueOf(id));
		return retVal;
	}

	@Override
	public String toString() {
		return myBeans.toString();
	}

}