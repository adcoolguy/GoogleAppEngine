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

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withOffset;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.ShortBlob;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.users.User;
import com.spoledge.audao.db.dao.DBException;
import com.spoledge.audao.db.dao.DaoException;
import com.spoledge.audao.db.dao.DtoCache;
import com.spoledge.audao.db.dao.ExpiringMemoryDtoCacheImpl;
import com.spoledge.audao.db.dao.RootDaoImpl;


/**
 * This is the parent of all DAO implementation classes.
 * It uses all common generic methods and utilities.
 * The implementation is not thread safe - we assume
 * that the client creates one DAO impl per thread.
 */
public abstract class GaeAbstractDaoImpl<T> extends RootDaoImpl {

    private static final String GQL_DYNAMIC_QUERY_IMPL = "com.spoledge.audao.parser.gql.GqlDynamic";
    private static Class<? extends GQLDynamicQuery> GQLDynamicQueryImplClass;


    /**
     * Caching of entities - a short term cache allowing to optimize
     * the calls find + update.
     */
    protected static DtoCache<Key, Entity> entityCache = new ExpiringMemoryDtoCacheImpl<Key, Entity>( 100, 10 );

    /**
     * Caching of entities - a short term cache allowing to optimize
     * the calls find + update.
     */
    protected DtoCache<Key, Entity> txEntityCache;

    /**
     * The last known transaction.
     */
    protected Transaction tx;


    ////////////////////////////////////////////////////////////////////////////
    // Attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     * The assigned datastore service.
     */
    protected DatastoreService ds;


    /**
     * Flag indicating that "!=" or "IN" operators are used in conditions.
     * THe query must be processed by GQLDynamicQuery.
     */
    protected boolean multipleQueries;


    /**
     * GQLDynamicQuery is used to preocess dynamic queries
     * and static queries containing "!=" or "IN" operators.
     */
    protected GQLDynamicQuery gqlDynamicQuery;


    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new DAO implementation.
     * @throws NullPointerException when the passed DS is null.
     */
    protected GaeAbstractDaoImpl( DatastoreService ds ) {
        if (ds == null) {
            throw new NullPointerException("The datastore service is null");
        }

        this.ds = ds;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Public
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Clears entity cache.
     * This method is useful when testing.
     */
    public static void clearEntityCache() {
        entityCache.clear();
    }


    ////////////////////////////////////////////////////////////////////////////
    // Protected
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Finds one record by filled query.
     * @param query the query ready for execution
     * @param cond the SQL/GQL condition used only for logging
     * @param offset the offset of the result set starting at 0
     * @param params the parameters used only for logging
     * @return the record found or null
     */
    protected T findOne( Query query, String cond, int offset, Object... params ) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + cond;

        debugSql( sql, params );

        PreparedQuery pq = prepare( query, false );

        try {
            List<Entity> result = pq.asList( getFetchOptions( offset, 1 ));

            return result.size() != 0 ? fetch( result.get( 0 )) : null;
        }
        catch (Exception e) {
            errorSql( e, sql, params );

            handleException( e );

            return null;
        }
    }


    protected T[] findManyArray( Query query, String cond, int offset, int count, Object... params ) {
        return toArray( findManyImpl( query, cond, offset, count, params ));
    }


    protected ArrayList<T> findManyList( Query query, String cond, int offset, int count, Object... params ) {
        return findManyImpl( query, cond, offset, count, params );
    }


    /**
     * Finds more than one record by filled query.
     * @param query the query ready for execution
     * @param cond the SQL/GQL condition used only for logging
     * @param offset the offset of the result set starting at 0
     * @param count the max number of returned records; -1 or Integer.MAX_VALUE mean no limit
     * @param params the parameters used only for logging
     * @return the result list (even empty)
     */
    protected ArrayList<T> findManyImpl( Query query, String cond, int offset, int count, Object[] params ) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + cond;

        debugSql( sql, params );

        PreparedQuery pq = prepare( query, false );

        try {
            return fetchList( pq.asIterator( getFetchOptions( offset, count )));
        }
        catch (Exception e) {
            errorSql( e, sql, params );

            handleException( e );

            return null;
        }
    }


    /**
     * Counts records by filled query.
     * @param query the query ready for execution
     * @param cond the SQL/GQL condition used only for logging
     * @param params the parameters used only for logging
     * @return the number of records found
     */
    protected int count( Query query, String cond, Object... params ) {
        String sql = "SELECT count(*) FROM " + getTableName() + " WHERE " + cond;

        debugSql( sql, params );

        PreparedQuery pq = prepare( query, true );

        try {
            return pq.countEntities( withLimit( Integer.MAX_VALUE ));
        }
        catch (Exception e) {
            errorSql( e, sql, params );

            handleException( e );

            return -1;
        }
    }


    /**
     * Deletes a record by filled query.
     * @param query the query ready for execution
     * @param cond the SQL/GQL condition used only for logging
     * @param offset the offset of the result set starting at 0
     * @param count the max number of deleted records; -1 or Integer.MAX_VALUE mean no limit
     * @param params the parameters used only for logging
     * @return true if the record was really deleted
     * @throws DaoException when more than one record was deleted
     */
    protected boolean deleteOne( Query query, String cond, int offset, int count, Object... params )
            throws DaoException {

        int ret = deleteMany( query, cond, offset, count, params );

        if (ret > 1) {
            String err = "More than one record deleted";
            log.error( err + " for " + sqlLog( cond, params ));

            throw new DaoException( err );
        }

        return ret == 1;
    }


    /**
     * Deletes records by filled query.
     * @param query the query ready for execution
     * @param cond the SQL/GQL condition used only for logging
     * @param offset the offset of the result set starting at 0
     * @param count the max number of deleted records; -1 or Integer.MAX_VALUE mean no limit
     * @param params the parameters used only for logging
     * @return the number of deleted records
     */
    protected int deleteMany( Query query, String cond, int offset, int count, Object... params)
            throws DaoException {

        String sql = "DELETE FROM " + getTableName() + " WHERE " + cond;

        debugSql( sql, params );

        PreparedQuery pq = prepare( query, true );

        entityDelete( keyIterable( pq.asIterable( getFetchOptions( offset, count ))));

        return iteratorCount;
    }


    ////////////////////////////////////////////////////////////////////////////
    // Fetch and convert methods
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Fetches data from generic entity into typesafe DTO.
     * @param ent the source generic entity
     * @return the typesafe DTO
     */
    protected T fetch( Entity ent ) {
        return fetch( null, ent );
    }


    /**
     * Fetches data from generic entity into typesafe DTO.
     * @param dto the typesafe DTO which is filled and returned; can be null
     * @param ent the source generic entity
     * @return the typesafe DTO
     */
    protected abstract T fetch( T dto, Entity ent );


    /**
     * Fetches data from generic entities into typesafe DTOs.
     */
    protected T[] fetchArray( Iterator<Entity> iterator ) {
        return toArray( fetchList( iterator ));
    }


    /**
     * Fetches data from generic entities into typesafe DTOs.
     */
    protected ArrayList<T> fetchList( Iterator<Entity> iterator ) {
        ArrayList<T> list = new ArrayList<T>();

        while (iterator.hasNext()) {
            list.add( fetch( null, iterator.next()));
        }

        return list;
    }


    /**
     * Converts list to array.
     */
    protected abstract T[] toArray( ArrayList<T> list );


    ////////////////////////////////////////////////////////////////////////////
    // Datastore R/W
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Reads entity from datastore by its key.
     */
    protected Entity entityGet( Key key ) {
        debugSql( "get", key );

        DtoCache<Key, Entity> cache = getEntityCache();
        Entity ret = cache.get( key );

        if (ret != null) {
            if (log.isDebugEnabled()) {
                log.debug("entityGet(): entity found in cache by key: " + key );
            }

            return ret;
        }

        try {
            ret = ds.get( key );
        }
        catch (EntityNotFoundException e) {
            if (log.isDebugEnabled()) {
                log.debug("entityGet(): entity not found by key: " + key);
            }

            return null;
        }
        catch (Exception e) {
            log.error("entityGet(): key=" + key, e);

            handleException( e );

            return null;
        }

        if (cache != entityCache) entityCache.remove( key );
        cache.put( key, ret );

        return ret;
    }


    /**
     * Writes entity to datastore.
     * @param ent the entity to be written
     * @param dto the dto used only for logging; also can be a simple parameter
     * @param operation the operation (insert/update) used only for logging
     */
    protected Key entityPut( Entity ent, Object dto, String operation ) throws DaoException {
        debugSql( operation, dto );

        Key ret = null;

        try {
            ret = ds.put( ent );
        }
        catch (ConcurrentModificationException e) {
            errorSql( e, operation, dto );

            throw new DaoException("Concurrent transaction detected on entity key=" + ent.getKey());
        }
        catch (Exception e) {
            errorSql( e, operation, dto );

            handleException( e );

            return null;
        }

        DtoCache<Key, Entity> cache = getEntityCache();
        if (cache != entityCache) entityCache.remove( ret );
        cache.put( ret, ent );

        return ret;
    }


    /**
     * Writes several entities to datastore.
     * @param ents the entities to be written
     * @param dtos the dtos used only for logging
     * @param operation the operation (insert/update) used only for logging
     */
    protected List<Key> entityPut( Iterable<Entity> ents, Iterable<?> dtos, String operation )
            throws DaoException {

        debugSql( operation, dtos );

        List<Key> ret = null;

        try {
            ret = ds.put( ents );
        }
        catch (ConcurrentModificationException e) {
            errorSql( e, operation, dtos );

            throw new DaoException("Concurrent transaction detected on entities");
        }
        catch (Exception e) {
            errorSql( e, operation, dtos );

            handleException( e );

            return null;
        }

        // no entity cache handling - should manually clear it if necessary.

        return ret;
    }


    /**
     * Removes entity from datastore.
     * @return true if the entity was really deleted
     */
    protected boolean entityDelete( Key key ) throws DaoException {
        try {
            ds.delete( key );
        }
        catch (ConcurrentModificationException e) {
            log.error("entityDelete():" + e + ", key=" + key );

            throw new DaoException("Concurrent transaction detected on entity key=" + key);
        }
        catch (IllegalArgumentException e) {
            log.warn("entityDelete():" + e + ", key=" + key );

            return false;
        }
        catch (Exception e) {
            log.error("entityDelete(): key=" + key, e);

            handleException( e );

            return false;
        }

        entityCache.remove( key );
        if (txEntityCache != null) txEntityCache.remove( key );

        return true;
    }


    /**
     * Removes entities from datastore.
     */
    protected void entityDelete( Iterable<Key> keys ) throws DaoException {
        try {
            ds.delete( keys );
        }
        catch (ConcurrentModificationException e) {
            log.error("entityDelete():" + e);

            throw new DaoException("Concurrent transaction detected on entities");
        }
        catch (Exception e) {
            log.error("entityDelete():", e);

            handleException( e );

            return;
        }

        for (Key key : keys) entityCache.remove( key );
        if (txEntityCache != null) {
            for (Key key : keys) txEntityCache.remove( key );
        }
    }


    /**
     * Prepares query.
     */
    protected PreparedQuery prepare( Query query ) {
        return prepare( query, false );
    }


    /**
     * Prepares query.
     */
    protected PreparedQuery prepare( Query query, boolean keysOnly ) {
        try {
            if (multipleQueries) {
                return getGQLDynamicQuery().prepareMultipleQueries( query, keysOnly );
            }
            else {
                if (keysOnly) query.setKeysOnly();

                return ds.prepare( query.getAncestor() != null ? ds.getCurrentTransaction( null ) : null, query );
            }
        }
        catch (Exception e) {
            log.error("prepare(): query=" + query, e);

            handleException( e );

            return null;
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    // Conversion methods Entity to DTO
    ////////////////////////////////////////////////////////////////////////////

    protected Boolean getBoolean( Entity ent, String prop ) {
        Object o = ent.getProperty( prop );
        if (o == null) return null;

        if (o instanceof Boolean) return ((Boolean)o);

        String s = o.toString().trim();

        if ("true".equals( s )) return true;
        if ("false".equals( s )) return false;

        invalidType( ent, prop, Boolean.class );
        return null;
    }


    protected Short getShort( Entity ent, String prop ) {
        Object o = ent.getProperty( prop );
        if (o == null) return null;

        if (o instanceof Number) return ((Number)o).shortValue();

        try {
            return new Short( o.toString().trim());
        }
        catch (NumberFormatException e) {}

        invalidType( ent, prop, Short.class );
        return null;
    }


    protected Integer getInteger( Entity ent, String prop ) {
        Object o = ent.getProperty( prop );
        if (o == null) return null;

        if (o instanceof Number) return ((Number)o).intValue();

        try {
            return new Integer( o.toString().trim());
        }
        catch (NumberFormatException e) {}

        invalidType( ent, prop, Integer.class );
        return null;
    }


    protected Long getLong( Entity ent, String prop ) {
        Object o = ent.getProperty( prop );
        if (o == null) return null;

        if (o instanceof Number) return ((Number)o).longValue();

        try {
            return new Long( o.toString().trim());
        }
        catch (NumberFormatException e) {}

        invalidType( ent, prop, Long.class );
        return null;
    }


    protected Double getDouble( Entity ent, String prop ) {
        Object o = ent.getProperty( prop );
        if (o == null) return null;

        if (o instanceof Number) return ((Number)o).doubleValue();

        try {
            return new Double( o.toString().trim());
        }
        catch (NumberFormatException e) {}

        invalidType( ent, prop, Double.class );
        return null;
    }


    protected String getString( Entity ent, String prop ) {
        Object o = ent.getProperty( prop );
        if (o == null) return null;

        if (o instanceof String) return ((String)o);
        if (o instanceof Text) return ((Text)o).getValue();
        if (o instanceof Category) return ((Category)o).getCategory();
        if (o instanceof Email) return ((Email)o).getEmail();

        return o.toString();
    }


    protected java.sql.Date getDate( Entity ent, String prop ) {
        Object o = ent.getProperty( prop );
        if (o == null) return null;

        if (o instanceof java.sql.Date) return ((java.sql.Date)o);
        if (o instanceof java.util.Date) return new java.sql.Date(((java.util.Date)o).getTime());
        if (o instanceof Number) return new java.sql.Date(((Number)o).longValue());

        invalidType( ent, prop, java.sql.Date.class );
        return null;
    }


    protected java.sql.Timestamp getTimestamp( Entity ent, String prop ) {
        Object o = ent.getProperty( prop );
        if (o == null) return null;

        if (o instanceof java.sql.Timestamp) return ((java.sql.Timestamp)o);
        if (o instanceof java.util.Date) return new java.sql.Timestamp(((java.util.Date)o).getTime());
        if (o instanceof Number) return new java.sql.Timestamp(((Number)o).longValue());

        invalidType( ent, prop, java.sql.Timestamp.class );
        return null;
    }


    protected byte[] getByteArray( Entity ent, String prop ) {
        Object o = ent.getProperty( prop );
        if (o == null) return null;

        if (o instanceof Blob) return ((Blob)o).getBytes();
        if (o instanceof ShortBlob) return ((ShortBlob)o).getBytes();

        invalidType( ent, prop, Blob.class );
        return null;
    }


    protected <S> S getObject( Entity ent, String prop, Class<S> clazz ) {
        Object o = ent.getProperty( prop );
        if (o == null) return null;

        if (o instanceof Blob) return deserialize(((Blob)o).getBytes(), clazz);
        if (o instanceof ShortBlob) return deserialize(((ShortBlob)o).getBytes(), clazz);

        invalidType( ent, prop, Blob.class );
        return null;
    }


    protected <S> S getCoreObject( Entity ent, String prop, Class<S> clazz ) {
        Object o = ent.getProperty( prop );
        if (o == null) return null;

        try {
            return clazz.cast( o );
        }
        catch (Exception e) {}

        if (o instanceof Blob) return deserialize(((Blob)o).getBytes(), clazz);
        if (o instanceof ShortBlob) return deserialize(((ShortBlob)o).getBytes(), clazz);
        if (clazz == String.class && (o instanceof Text)) return clazz.cast(((Text)o).getValue());

        invalidType( ent, prop, clazz );
        return null;
    }


    /**
     * Returns native list - as fetched by GAE API.
     */
    @SuppressWarnings("unchecked")
    protected <S> List<S> getList( Entity ent, String prop ) {
        Object o = ent.getProperty( prop );
        if (o == null) return null;

        if (o instanceof List) {
            return (List<S>) o;
        }

        invalidType( ent, prop, List.class );
        return null;
    }


    /**
     * Returns native list - as fetched by GAE API.
     */
    protected <S> List<S> getList( Entity ent, String prop, Class<S> clazz ) {
        return getList( ent, prop );
    }


    /**
     * Returns list of deserialized object.
     */
    @SuppressWarnings("unchecked")
    protected <S> List<S> getListOfObjects( Entity ent, String prop ) {
        List<S> list = getList( ent, prop );

        if ( list == null ) return null;

        ArrayList ret = new ArrayList( list.size());

        for ( Object o : list ) {
            if (o == null) ret.add( null );
            else if (o instanceof Blob)  ret.add( deserialize(((Blob)o).getBytes(), Object.class ));
            else if (o instanceof ShortBlob) ret.add( deserialize(((ShortBlob)o).getBytes(), Object.class ));
            else ret.add( o );
        }

        return ret;
    }


    /**
     * Returns list of deserialized object.
     */
    protected <S> List<S> getListOfObjects( Entity ent, String prop, Class<S> clazz ) {
        List<S> list = getList( ent, prop, clazz );

        if ( list == null ) return null;

        ArrayList<S> ret = new ArrayList<S>( list.size());

        for ( Object o : list ) {
            if (o == null) ret.add( null );
            else if (o instanceof Blob)  ret.add( deserialize(((Blob)o).getBytes(), clazz ));
            else if (o instanceof ShortBlob) ret.add( deserialize(((ShortBlob)o).getBytes(), clazz ));
            else {
                try {
                    ret.add( clazz.cast( o ));
                }
                catch (Exception e) {
                    invalidType( ent, prop, clazz );
                }
            }
        }

        return ret;
    }


    protected void invalidType( Entity ent, String prop, Class<?> expected ) {
        Object o = ent.getProperty( prop );
        Class<?> actual = o.getClass();

        log.error("invalidType(): kind=" + ent.getKind() + ", prop=" + prop + ", key=" + ent.getKey()
                    + ", expected=" + expected + ", found=" + actual + ", value=" + o );

        throw new RuntimeException("Invalid property type of property " + ent.getKind() + '.' + prop );
    }


    ////////////////////////////////////////////////////////////////////////////
    // Conversion methods DTO to Entity
    ////////////////////////////////////////////////////////////////////////////

    protected java.util.Date date( int year, int month, int day ) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set( Calendar.YEAR, year );
        cal.set( Calendar.MONTH, month - 1 );
        cal.set( Calendar.DAY_OF_MONTH, day );

        return cal.getTime();
    }


    protected java.util.Date datetime( int year, int month, int day, int hour, int min, int sec ) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set( Calendar.YEAR, year );
        cal.set( Calendar.MONTH, month - 1 );
        cal.set( Calendar.DAY_OF_MONTH, day );
        cal.set( Calendar.HOUR_OF_DAY, hour );
        cal.set( Calendar.MINUTE, min );
        cal.set( Calendar.SECOND, sec );

        return cal.getTime();
    }


    protected java.util.Date time( int hour, int min, int sec ) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set( Calendar.HOUR_OF_DAY, hour );
        cal.set( Calendar.MINUTE, min );
        cal.set( Calendar.SECOND, sec );

        return cal.getTime();
    }


    /**
     * This method crops time to emulate same behavior as JDBC drivers.
     */
    protected java.util.Date date( java.sql.Date val ) {
        if (val == null) return null;

        Calendar cal = Calendar.getInstance();
        cal.setTime( val );
        cal.set( Calendar.HOUR_OF_DAY, 0 );
        cal.set( Calendar.MINUTE, 0 );
        cal.set( Calendar.SECOND, 0 );
        cal.set( Calendar.MILLISECOND, 0 );

        return cal.getTime();
    }


    protected java.util.Date date( java.sql.Timestamp val ) {
        return val != null ? new java.util.Date( val.getTime()) : null;
    }


    protected Blob blob( byte[] val ) {
        return val != null ? new Blob( val ) : null;
    }


    protected Blob blob( Object val ) {
        return val != null ? new Blob( serialize( val )) : null;
    }


    protected ShortBlob shortBlob( byte[] val ) {
        return val != null ? new ShortBlob( val ) : null;
    }


    protected ShortBlob shortBlob( Object val ) {
        return val != null ? new ShortBlob( serialize( val )) : null;
    }


    protected GeoPt geopt( Number latitude, Number longitude ) {
        return latitude != null && longitude != null ?
            new GeoPt( latitude.floatValue(), longitude.floatValue()) : null;
    }


    protected User user( String email ) {
        return email != null ? new User( email, "gmail.com" ) : null;
    }


    protected List<Blob> blobs( List list ) {
        if ( list == null ) return null;

        ArrayList<Blob> ret = new ArrayList<Blob>( list.size());

        for ( Object o : list ) {
            ret.add( blob( o ));
        }

        return ret;
    }


    protected List<ShortBlob> shortBlobs( List list ) {
        if ( list == null ) return null;

        ArrayList<ShortBlob> ret = new ArrayList<ShortBlob>( list.size());

        for ( Object o : list ) {
            ret.add( shortBlob( o ));
        }

        return ret;
    }


    protected List<ShortBlob> shortBlobsOfByteArray( List<byte[]> list ) {
        if ( list == null ) return null;

        ArrayList<ShortBlob> ret = new ArrayList<ShortBlob>( list.size());

        for ( byte[] o : list ) {
            ret.add( shortBlob( o ));
        }

        return ret;
    }


    protected List<java.util.Date> datesOfDate( List<? extends java.sql.Date> list ) {
        if ( list == null ) return null;

        ArrayList<java.util.Date> ret = new ArrayList<java.util.Date>( list.size());

        for ( java.sql.Date o : list ) {
            ret.add( date( o ));
        }

        return ret;
    }


    protected List<java.util.Date> datesOfTimestamp( List<? extends java.sql.Timestamp> list ) {
        if ( list == null ) return null;

        ArrayList<java.util.Date> ret = new ArrayList<java.util.Date>( list.size());

        for ( java.sql.Timestamp o : list ) {
            ret.add( date( o ));
        }

        return ret;
    }


    protected List<Integer> ordinals( List<? extends Enum> list ) {
        if ( list == null ) return null;

        ArrayList<Integer> ret = new ArrayList<Integer>( list.size());

        for ( Enum o : list ) {
            ret.add( o.ordinal() + 1 );
        }

        return ret;
    }


    protected List<String> names( List<? extends Enum> list ) {
        if ( list == null ) return null;

        ArrayList<String> ret = new ArrayList<String>( list.size());

        for ( Enum o : list ) {
            ret.add( o.name());
        }

        return ret;
    }


    ////////////////////////////////////////////////////////////////////////////
    // Utilities
    ////////////////////////////////////////////////////////////////////////////

    protected void handleException( Exception e ) {
        throw new DBException( e );
    }


    protected Query getQueryCond( String cond, Object...params ) {
        return getQueryCond( (Query) null, cond, params );
    }


    protected Query getQueryCond( Query q, String cond, Object...params ) {
        if (q == null) q = getQuery();

        if ( cond != null && cond.length() > 0 ) {
            GQLDynamicQuery gql = getGQLDynamicQuery();
            q = gql.parseQueryCond( q, cond, params );

            multipleQueries = gql.wasMultipleQueries();
        }

        return q;
    }


    protected Query getQuery() {
        return new Query( getTableName());
    }


    protected Key key( long id ) {
        return key( getTableName(), id );
    }


    protected Key key( String name ) {
        return key( getTableName(), name );
    }


    protected Key key( String kind, long id ) {
        return KeyFactory.createKey( kind, id );
    }


    protected Key key( String kind, String name ) {
        return KeyFactory.createKey( kind, name );
    }


    protected Key key( Key parent, String kind, long id ) {
        return KeyFactory.createKey( parent, kind, id );
    }


    protected Key key( Key parent, String kind, String name ) {
        return KeyFactory.createKey( parent, kind, name );
    }


    protected Key parentKey( Key key ) {
        return parentKey( key, 1 );
    }


    protected Key parentKey( Key key, int depth ) {
        while ( depth-- > 0 ) {
            if (key == null) return null;
            key = key.getParent();
        }

        return key;
    }


    protected Long parentKeyAsLong( Key key, int depth ) {
        Key ret = parentKey( key, depth );

        return ret != null ? ret.getId() : null;
    }


    protected String parentKeyAsString( Key key, int depth ) {
        Key ret = parentKey( key, depth );

        return ret != null ? ret.getName() : null;
    }


    protected Iterable<Key> keyIterable( final Iterable<Entity> ie ) {
        return new Iterable<Key>() {
            public Iterator<Key> iterator() {
                return keyIterator( ie.iterator());
            }
        };
    }


    protected int iteratorCount;

    protected Iterator<Key> keyIterator( final Iterator<Entity> ie ) {
        iteratorCount = 0;

        return new Iterator<Key>() {
            public boolean hasNext() {
                return ie.hasNext();
            }

            public Key next() {
                iteratorCount++;

                return ie.next().getKey();
            }

            public void remove() {
                ie.remove();
            }
        };
    }


    protected FetchOptions getFetchOptions( int offset, int count ) {
        FetchOptions fo = withOffset( offset );

        if (isLimit( count )) {
            fo = fo.limit( count );
        }

        return fo;
    }


    protected boolean isLimit( int count ) {
        return count >= 0 && count != Integer.MAX_VALUE;
    }


    protected GQLDynamicQuery getGQLDynamicQuery() {
        if (gqlDynamicQuery == null) {
            try {
                if (GQLDynamicQueryImplClass == null) {
                    GQLDynamicQueryImplClass = Class.forName( GQL_DYNAMIC_QUERY_IMPL )
                                                    .asSubclass( GQLDynamicQuery.class );
                }

                gqlDynamicQuery = GQLDynamicQueryImplClass.newInstance();
                gqlDynamicQuery.setDatastoreService( ds );
            }
            catch (Exception e) {
                log.error("getGQLDynamicQuery(): cannot instantiate dynamic GQL parser: ", e );

                throw new RuntimeException( e );
            }
        }

        return gqlDynamicQuery;
    }


    protected DtoCache<Key, Entity> getEntityCache() {
        Transaction currentTx = ds.getCurrentTransaction( null );

        if (currentTx == null) {
            if (tx != null) {
                tx = null;
                txEntityCache.clear();
            }

            return entityCache;
        }
        else {
            if (txEntityCache == null) {
                txEntityCache = new ExpiringMemoryDtoCacheImpl<Key, Entity>( 100, 10 );
                tx = currentTx;
            }
            else if (tx != currentTx) {
                txEntityCache.clear();
                tx = currentTx;
            }

            return txEntityCache;
        }
    }
}

