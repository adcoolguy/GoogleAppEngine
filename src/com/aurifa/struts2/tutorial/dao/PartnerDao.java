package com.aurifa.struts2.tutorial.dao;

import com.aurifa.struts2.tutorial.model.Partner;
import com.spoledge.audao.db.dao.DaoException;

public interface PartnerDao {
//    public Partner getPartner(Integer id);
//    public List getAllPartners();
//    public void delete(Integer id);
//    public void insert(Partner emp);
//    public void update(Partner emp);

    public Partner findByPrimaryKey( long id );
    public Partner findById( long id );
    public Partner[] findAll( );
    public boolean deleteByPrimaryKey( long id ) throws DaoException;
    public long insert( Partner dto ) throws DaoException;
    public boolean update( long id, Partner dto ) throws DaoException;
}