package com.appspot.cloudserviceapi.services.manager;

import java.util.List;

import com.appspot.cloudserviceapi.security.spring.model.GaeUserDetails;

public interface UserManager {

        public abstract List<GaeUserDetails> getUsers();

        public abstract void setUsers(List<GaeUserDetails> myBeans);
        
        public void delete(Long id);
        
        public abstract void save(GaeUserDetails myBean);
        
        public GaeUserDetails getUser(Long id);

		public abstract List<GaeUserDetails> getUsersByRole(String string);       

}