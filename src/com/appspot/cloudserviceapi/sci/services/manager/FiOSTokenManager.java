package com.appspot.cloudserviceapi.sci.services.manager;

import java.util.List;

import tapp.model.sci.FiOSToken;

public interface FiOSTokenManager {

        public abstract List<FiOSToken> getFiOSTokens();

        public abstract void setFiOSTokens(List<FiOSToken> myBeans);
        
        public void delete(Long id);
        
        public abstract void save(FiOSToken myBean);
        
        public FiOSToken getFiOSToken(Long id);       

}