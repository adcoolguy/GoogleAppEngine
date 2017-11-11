package com.appspot.cloudserviceapi.sci.services.manager;

import java.util.List;

import tapp.model.ServiceRegistry;

public interface ServiceRegistryManager {

        public abstract List<ServiceRegistry> getServiceRegistrys();

        public abstract void setServiceRegistrys(List<ServiceRegistry> myBeans);
        
        public void delete(Long id);
        
        public abstract void save(ServiceRegistry myBean);
        
        public ServiceRegistry getServiceRegistry(Long id);       

}