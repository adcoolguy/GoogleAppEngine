package com.appspot.cloudserviceapi.sgc.services.manager;

import java.util.List;

import tapp.model.sgc.Client;

public interface ClientManager {

        public abstract List<Client> getClients();

        public abstract void setClients(List<Client> myBeans);
        
        public void delete(Long id);
        
        public abstract void save(Client myBean);
        
        public Client getClient(Long id);       

}