package com.appspot.cloudserviceapi.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

/*
 * Reference: http://audao.spoledge.com/doc-overview.html
 */
public class Datastore {

	/**
     * Our per Thread context.
     */
    private static class ThreadContext {
        DatastoreService ds;
    }

    /**
     * All contexts are stored here.
     */
    private static final ThreadLocal<ThreadContext> contexts = new ThreadLocal<ThreadContext>() {
        @Override
        protected ThreadContext initialValue() {
            return new ThreadContext();
        }
    };
    
    /**
     * Returns the thread's local DatastoreService.
     * @return the existing DatastoreService or creates a new one
     */
    public static DatastoreService getDS() {
        ThreadContext ctx = contexts.get();
        if (ctx.ds == null) ctx.ds = DatastoreServiceFactory.getDatastoreService();

        return ctx.ds;
    }

    /**
     * Customized method to get unique long integer from GAEJ.
     * 
     * @param kind
     * @param uniqueName
     * @return
     */
    public static Long getId(Object kind, String uniqueName) {
        //Key parent = KeyFactory.createKey(kind.getClass().getSimpleName(), uniqueName);
		//KeyFactory.Builder b = new KeyFactory.Builder(parent);;
		//Key key = b.addChild(kind.getClass().getSimpleName() , UUID.randomUUID().toString()).getKey();
        //return key.getId();     	
        //return UUID.randomUUID().getLeastSignificantBits();
    	ShardedCounterV2 counter = new ShardedCounterV2(uniqueName);
    	counter.increment();
    	return counter.getCount();
    }
    
}
