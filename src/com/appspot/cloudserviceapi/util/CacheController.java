package com.appspot.cloudserviceapi.util;

import java.util.HashMap;
import java.util.Map;

import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;
import net.sf.jsr107cache.CacheStatistics;

/*
 * Source: http://blog.knoldus.com/2010/09/30/google-app-engine-understanding-caching/
 * Issue: Encountered runtime exception "Memcache put: Item may not be more than 1048503 bytes in length; received 3031859 bytes" on production!
 */
public class CacheController {
	 
    static Cache cache;
    static {
        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            Map props = createPolicyMap();
            cache = cacheFactory.createCache(props);
        } catch (CacheException e) {
            e.printStackTrace();
        }
    }
 
    private static Map createPolicyMap() {
        Map props = new HashMap();
        props.put(GCacheFactory.EXPIRATION_DELTA, 1800);
        return props;
    }
 
    public static String fetchCacheStatistics() {
        CacheStatistics stats = cache.getCacheStatistics();
        int hits = stats.getCacheHits();
        int misses = stats.getCacheMisses();
        return "Cache Hits=" + hits + " : Cache Misses=" + misses;
    }
 
    public static void put(String key, Object value) {
        cache.put(key, value);
    }
 
    public static Object get(String key) {
        return cache.get(key);
    }
}