package com.appspot.cloudserviceapi.services.manager;

import java.util.ArrayList;
import java.util.List;

import com.appspot.cloudserviceapi.security.spring.model.GaeUserDetails;
import com.appspot.cloudserviceapi.security.spring.model.UserSecurityDAO;

public class UserManagerImpl implements UserManager {

	private List<GaeUserDetails> myBeans = (new UserSecurityDAO()).getCloneList();

	public List<GaeUserDetails> getUsers() {
		return (new UserSecurityDAO()).getCloneList();
	}

	public List<GaeUserDetails> getUsersByRole(String rolePattern) {
		List<GaeUserDetails>retVal = new ArrayList();
		List<GaeUserDetails>ou = (new UserSecurityDAO()).getCloneList();
		for(GaeUserDetails u:ou) {
			if(u.getUserRole().toString().indexOf(rolePattern) > -1) {
				retVal.add(u);
			}
		}
		
		return retVal;
	}

	public void setUsers(List<GaeUserDetails> myBeans) {
		this.myBeans = myBeans;
	}

	public void delete(Long id) {
		GaeUserDetails myBean = getUser(id);
		getUsers().remove(myBean);
		(new UserSecurityDAO()).remove(id);
	}

	public void save(GaeUserDetails myBean) {
		if (myBeans.indexOf(myBean) > -1) {
			// update
			myBeans.set(myBeans.indexOf(myBean), myBean);
		} else {
			myBeans.add(myBean);
		}
		(new UserSecurityDAO()).save(myBean);
	}

	public GaeUserDetails getUser(Long id) {
		GaeUserDetails retVal = null;
//		for (GaeUserDetails myBean : myBeans) {
//			if (myBean.getId() != null && myBean.getId().equals(id)) {
//				retVal = myBean;
//			}
//		}
		retVal = (new UserSecurityDAO()).get(String.valueOf(id));
		return retVal;
	}

	@Override
	public String toString() {
		return myBeans.toString();
	}

}