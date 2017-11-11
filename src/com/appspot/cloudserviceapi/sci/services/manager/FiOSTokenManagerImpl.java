package com.appspot.cloudserviceapi.sci.services.manager;

import java.util.List;

import com.appspot.cloudserviceapi.sci.dao.FiOSTokenDAO;

import tapp.model.sci.FiOSToken;

public class FiOSTokenManagerImpl implements FiOSTokenManager {

//	private List<User> myBeans = new ArrayList<User>();
	private List<FiOSToken> myBeans = (new FiOSTokenDAO()).getCloneList();

	public List<FiOSToken> getFiOSTokens() {
//		return myBeans;
		return (new FiOSTokenDAO()).getCloneList();
	}

	public void setFiOSTokens(List<FiOSToken> myBeans) {
		this.myBeans = myBeans;
	}

	public void delete(Long id) {
		FiOSToken myBean = getFiOSToken(id);
		getFiOSTokens().remove(myBean);
		(new FiOSTokenDAO()).remove(id);
	}

	public void save(FiOSToken myBean) {
		if (myBeans.indexOf(myBean) > -1) {
			// update
			myBeans.set(myBeans.indexOf(myBean), myBean);
		} else {
			myBeans.add(myBean);
		}
		(new FiOSTokenDAO()).save(myBean);
	}

	public FiOSToken getFiOSToken(Long id) {
		FiOSToken retVal = null;
//		for (FiOSToken myBean : myBeans) {
//			if (myBean.getId() != null && myBean.getId().equals(id)) {
//				retVal = myBean;
//			}
//		}
		retVal = (new FiOSTokenDAO()).get(String.valueOf(id));
		return retVal;
	}

	@Override
	public String toString() {
		return myBeans.toString();
	}

}