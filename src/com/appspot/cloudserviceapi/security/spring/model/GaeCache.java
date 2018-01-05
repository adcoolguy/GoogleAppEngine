package com.appspot.cloudserviceapi.security.spring.model;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class GaeCache {

    private MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();

    GaeCache() {
    	
    }

    public int get(String userId) {
    	int ret = -1;
		try {
			ret = (int) memcacheService.get(userId);
		} catch (Exception e) {
			ret = 0;
		}
    	return ret;
    }

    public void put(String userId, int value) {
    	int expirationInSeconds = 300;	//in 5 minutes!
    	memcacheService.put(userId, value, Expiration.byDeltaSeconds(expirationInSeconds));
    }

	public void invalidate(String userId) {
		memcacheService.delete(userId);
	}

}