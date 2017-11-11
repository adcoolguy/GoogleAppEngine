/*
 * Copyright 2010 Spolecne s.r.o. (www.spoledge.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.spoledge.audao.db.dao.gae;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.spoledge.audao.db.dao.DtoCache;


/**
 * Memcache DtoCache uses MemcacheService - distributed cache.
 * The max size is not relevant for this type of cache.
 */
public class MemcacheDtoCacheImpl<K,V> implements DtoCache<K,V> {

    /**
     * The default MemcacheService namespace prefix.
     */
    public static final String DEFAULT_NAMESPACE_PREFIX = "AuDAO.DtoCache.";


    /**
     * The anonymous suffix name used for MemcacheService's namespace..
     */
    public static final String ANONYMOUS = "__anonymous__";


    ////////////////////////////////////////////////////////////////////////////
    // Attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     * The underlying memcache instance.
     */
    protected final MemcacheService memcacheService;


    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Creates anonymous memcache instance.
     */
    public MemcacheDtoCacheImpl() {
        this( DEFAULT_NAMESPACE_PREFIX, ANONYMOUS );
    }


    /**
     * Creates named memcache instance.
     * The full namespace is set as: <code>DEFAULT_NAMESPACE_PREFIX + name</code> .
     * This is used for generated DAO implementation classes.
     */
    public MemcacheDtoCacheImpl( String name ) {
        this( DEFAULT_NAMESPACE_PREFIX, name );
    }


    /**
     * Creates named memcache instance.
     */
    public MemcacheDtoCacheImpl( String namespacePrefix, String namespaceSuffix ) {
        memcacheService = MemcacheServiceFactory.getMemcacheService( namespacePrefix + namespaceSuffix );
    }


    ////////////////////////////////////////////////////////////////////////////
    // DtoCache
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the associated object or null.
     */
    @SuppressWarnings("unchecked")
    public V get( K key ) {
        return (V) memcacheService.get( key );
    }


    /**
     * Puts the key/value pair into the cache.
     */
    public void put( K key, V value ) {
        memcacheService.put( key, value );
    }


    /**
     * Removes the associated object.
     */
    public void remove( K key ) {
        memcacheService.delete( key );
    }


    /**
     * Clears the cache.
     * NOTE: this clears ALL items no regards the namespaces.
     */
    public void clear() {
        memcacheService.clearAll();
    }

}
