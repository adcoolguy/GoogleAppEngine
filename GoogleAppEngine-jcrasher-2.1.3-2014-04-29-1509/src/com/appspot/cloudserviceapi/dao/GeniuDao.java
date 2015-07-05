/*
 * This file was generated - do not edit it directly !!
 * Generated by AuDAO tool, a product of Spolecne s.r.o.
 * For more information please visit http://www.spoledge.com
 */
package com.appspot.cloudserviceapi.dao;

import com.appspot.cloudserviceapi.dto.Geniu;
import com.spoledge.audao.db.dao.AbstractDao;
import com.spoledge.audao.db.dao.DaoException;


/**
 * This is the DAO.
 *
 * @author generated
 */
public interface GeniuDao extends AbstractDao {

    /**
     * Finds a record identified by its primary key.
     * @return the record found or null
     */
    public Geniu findByPrimaryKey( long id );

    /**
     * Finds a record.
     */
    public Geniu findByWhat( String what );

    /**
     * Finds records ordered by what.
     */
    public Geniu[] findAll( );

    /**
     * Deletes a record identified by its primary key.
     * @return true iff the record was really deleted (existed)
     */
    public boolean deleteByPrimaryKey( long id ) throws DaoException;

    /**
     * Inserts a new record.
     * @return the generated primary key - id
     */
    public long insert( Geniu dto ) throws DaoException;

    /**
     * Updates one record found by primary key.
     * @return true iff the record was really updated (=found and any change was really saved)
     */
    public boolean update( long id, Geniu dto ) throws DaoException;

}