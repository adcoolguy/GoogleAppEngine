/*
 * This file was generated - do not edit it directly !!
 * Generated by AuDAO tool, a product of Spolecne s.r.o.
 * For more information please visit http://www.spoledge.com
 */
package com.appspot.cloudserviceapi.dao.gae;


import java.sql.Date;
import java.util.ArrayList;

import com.appspot.cloudserviceapi.dao.GeniuDao;
import com.appspot.cloudserviceapi.dto.Geniu;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;
import com.spoledge.audao.db.dao.DaoException;
import com.spoledge.audao.db.dao.gae.GaeAbstractDaoImpl;


/**
 * This is the DAO imlementation class.
 *
 * @author generated
 */
public class GeniuDaoImpl extends GaeAbstractDaoImpl<Geniu> implements GeniuDao {

    private static final String TABLE_NAME = "Geniu";

    public GeniuDaoImpl( DatastoreService ds ) {
        super( ds );
    }

    /**
     * Finds a record identified by its primary key.
     * @return the record found or null
     */
    public Geniu findByPrimaryKey( long id ) {
        Entity _ent = entityGet( new KeyFactory.Builder( "Geniu", id ).getKey());

        return _ent != null ? fetch( null, _ent ) : null;
    }

    /**
     * Finds a record.
     */
    public Geniu findByWhat( String what ) {
        Query _query = getQuery();
        _query.addFilter( "what", Query.FilterOperator.EQUAL, what );

        return findOne( _query, "what = :1", 0, what);
    }

    /**
     * Finds records ordered by what.
     */
    public Geniu[] findAll( ) { //jprofiler - 105 ms of 7 invocations (about 15 ms each)
        Query _query = getQuery();

        multipleQueries = false;
        String _cond = "1 = 1";

        return findManyArray( _query, _cond, 0, -1 );
    }

    /**
     * Deletes a record identified by its primary key.
     * @return true iff the record was really deleted (existed)
     */
    public boolean deleteByPrimaryKey( long id ) throws DaoException {
        return entityDelete( new KeyFactory.Builder( "Geniu", id ).getKey() );
    }

    /**
     * Inserts a new record.
     * @return the generated primary key - id
     */
    public long insert( Geniu dto ) throws DaoException {
        Entity _ent = new Entity( "Geniu");

        {
            if ( dto.getPlatform() == null ) {
                throw new DaoException("Value of column 'platform' cannot be null");
            }
            checkMaxLength( "platform", dto.getPlatform(), 500 );
            _ent.setProperty( "platform", dto.getPlatform());

            if ( dto.getWhat() == null ) {
                throw new DaoException("Value of column 'what' cannot be null");
            }
            checkMaxLength( "what", dto.getWhat(), 500 );
            _ent.setProperty( "what", dto.getWhat());

            if ( dto.getDetails() == null ) {
                throw new DaoException("Value of column 'details' cannot be null");
            }
            checkMaxLength( "details", dto.getDetails(), 10000 );
            _ent.setProperty( "details", new Text( dto.getDetails() ));

            //TODO too much cpu usage - to be implemented by external search engine
//            if ( dto.getSimilarity() == null ) {
//                throw new DaoException("Value of column 'similarity' cannot be null");
//            }
//            checkMaxLength( "similarity", dto.getSimilarity(), 500 );
//            _ent.setProperty( "similarity", dto.getSimilarity());

            if ( dto.getCreatedDate() == null ) {
                dto.setCreatedDate( new Date( System.currentTimeMillis()));
            }
//            _ent.setProperty( "createdDate", date( dto.getCreatedDate() ));

            if ( dto.getLastUpdatedDate() == null ) {
                dto.setLastUpdatedDate( new Date( System.currentTimeMillis()));
            }
//            _ent.setProperty( "lastUpdatedDate", date( dto.getLastUpdatedDate() ));

            if ( dto.getLastAccessedDate() == null ) {
                dto.setLastAccessedDate( new Date( System.currentTimeMillis()));
            }
//            _ent.setProperty( "lastAccessedDate", date( dto.getLastAccessedDate() ));
        }

        entityPut( _ent, dto, "insert" );

        dto.setId( _ent.getKey().getId());

        return dto.getId();
    }

    /**
     * Updates one record found by primary key.
     * @return true iff the record was really updated (=found and any change was really saved)
     */
    public boolean update( long id, Geniu dto ) throws DaoException {
        Entity _ent = entityGet( new KeyFactory.Builder( "Geniu", id ).getKey());
        if (_ent == null) return false;

        boolean isUpdated = false;

        if ( dto.getId() != null ) {
            isUpdated = true;
        }

        if ( dto.getPlatform() != null ) {
            checkMaxLength( "platform", dto.getPlatform(), 500 );
            _ent.setProperty( "platform", dto.getPlatform());
            isUpdated = true;
        }

        if ( dto.getWhat() != null ) {
            checkMaxLength( "what", dto.getWhat(), 500 );
            _ent.setProperty( "what", dto.getWhat());
            isUpdated = true;
        }

        if ( dto.getDetails() != null ) {
            checkMaxLength( "details", dto.getDetails(), 10000 );
            _ent.setProperty( "details", new Text( dto.getDetails() ));
            isUpdated = true;
        }

        //TODO too much cpu usage - to be implemented by external search engine
//        if ( dto.getSimilarity() != null ) {
//            checkMaxLength( "similarity", dto.getSimilarity(), 500 );
//            _ent.setProperty( "similarity", dto.getSimilarity());
//            isUpdated = true;
//        }

        if ( dto.getCreatedDate() != null ) {
//            _ent.setProperty( "createdDate", date( dto.getCreatedDate() ));
            isUpdated = true;
        }

        if ( dto.getLastUpdatedDate() != null ) {
//            _ent.setProperty( "lastUpdatedDate", date( dto.getLastUpdatedDate() ));
            isUpdated = true;
        }

        if ( dto.getLastAccessedDate() != null ) {
//            _ent.setProperty( "lastAccessedDate", date( dto.getLastAccessedDate() ));
            isUpdated = true;
        }

        if (!isUpdated) {
            return false;
        }

        entityPut( _ent, dto, "update" );

        return true;
    }

    /**
     * Returns the table name.
     */
    public String getTableName() {
        return TABLE_NAME;
    }

    protected Geniu fetch( Geniu dto, Entity ent ) {
        if ( dto == null ) dto = new Geniu();

        dto.setId( ent.getKey().getId());
        dto.setPlatform( getString( ent, "platform" ));
        dto.setWhat( getString( ent, "what" ));
        dto.setDetails( getString( ent, "details" ));
        dto.setSimilarity( getString( ent, "similarity" ));
        dto.setCreatedDate( getDate( ent, "createdDate" ));
        dto.setLastUpdatedDate( getDate( ent, "lastUpdatedDate" ));
        dto.setLastAccessedDate( getDate( ent, "lastAccessedDate" ));

        return dto;
    }

    protected Geniu[] toArray(ArrayList<Geniu> list ) {
        Geniu[] ret = new Geniu[ list.size() ];
        return list.toArray( ret );
    }

}
