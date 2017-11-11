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

import com.google.appengine.api.memcache.Expiration;


/**
 * Expiring variant of the Memcache DtoCache.
 * Since teh MemcacheService distinguishes between seconds or milliseconds expiration
 * periods, we truncate any period longer than 60000 millis (= 1 minute) to seconds.
 */
public class ExpiringMemcacheDtoCacheImpl<K,V> extends MemcacheDtoCacheImpl<K,V> {

    protected int expireMillis;
    protected int expireSecs;
    protected boolean isSecs;


    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Creates anonymous memcache instance.
     */
    public ExpiringMemcacheDtoCacheImpl( long expireMillis ) {
        initExpiration( expireMillis );
    }


    /**
     * Creates named memcache instance.
     * The full namespace is set as: <code>DEFAULT_NAMESPACE_PREFIX + name</code> .
     * This is used for generated DAO implementation classes.
     */
    public ExpiringMemcacheDtoCacheImpl( long expireMillis, String name ) {
        super( name );
        initExpiration( expireMillis );
    }


    /**
     * Creates named memcache instance.
     */
    public ExpiringMemcacheDtoCacheImpl( long expireMillis, String namespacePrefix, String namespaceSuffix ) {
        super( namespacePrefix, namespaceSuffix );
        initExpiration( expireMillis );
    }


    ////////////////////////////////////////////////////////////////////////////
    // DtoCache
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Puts the key/value pair into the cache.
     */
    @Override
    public void put( K key, V value ) {
        memcacheService.put( key, value,
            isSecs ? Expiration.byDeltaSeconds( expireSecs ) : Expiration.byDeltaMillis( expireMillis ));
    }


    ////////////////////////////////////////////////////////////////////////////
    // Private
    ////////////////////////////////////////////////////////////////////////////

    private void initExpiration( long millis ) {
        if (millis > 60000) {
            isSecs = true;
            expireSecs = (int)( millis / 1000 );
        }
        else expireMillis = (int) millis;
    }
}

