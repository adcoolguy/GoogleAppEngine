package com.aurifa.struts2.tutorial.dao;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aurifa.struts2.tutorial.model.Business;
import com.aurifa.struts2.tutorial.model.Partner;
import com.spoledge.audao.db.dao.DaoException;

public class PartnerNoDBdao implements PartnerDao {
    private static Map businessMap;
//    private static ArrayList partners;
    private static Partner[] partners;

    static {
//        partners = new ArrayList();
      partners = new Partner[2];
//        partners.add(new Partner(new Integer(1), "John Doe", "john.doe@gmail.com", new Business(new Integer(100), "Accounting")));
//        partners.add(new Partner(new Integer(2), "Bob Smith", "bob.smith@gmail.com", new Business(new Integer(300), "Sales")));
      partners[0] = new Partner(new Integer(1), "John Doe", "john.doe@gmail.com", new Business(new Integer(100), "Accounting"));
      partners[1] = new Partner(new Integer(2), "Bob Smith", "bob.smith@gmail.com", new Business(new Integer(300), "Sales"));
        BusinessDao deptDao = new BusinessNoDBdao();
        businessMap = deptDao.getBusinessMap();
    }

    Log logger = LogFactory.getLog(this.getClass());

    public Partner[] findAll() {
        return partners;
    }

	public Partner findByPrimaryKey(long id) {
		Partner partner = null;
//        Iterator iter = partners.iterator();
//        while (iter.hasNext()) {
//        	partner = (Partner)iter.next();
//            if (partner.getPartnerId().equals(id)) {
//                break;
//            }
//        }
        partner = partners[(int) (id -1)];
        
        return partner;
    }

    public boolean update(long id, Partner partner) {
		return false;
//        Integer id = partner.getPartnerId();
//        for (int i = 0; i < partners.size(); i++) {
//            Partner tempPartner = (Partner)partners.get(i);
//            if (tempPartner.getPartnerId().equals(id)) {
//                partner.setBusiness((Business)businessMap.get(partner.getBusiness().getBusinessId()));
//                partners.set(i, partner);
//                break;
//            }
//        }
    }

    public long insert( Partner dto ) throws DaoException {
		return 0;
//        int lastId = 0;
//        Iterator iter = partners.iterator();
//        while (iter.hasNext()) {
//            Partner temp = (Partner)iter.next();
//            if (temp.getPartnerId().intValue() > lastId) {
//                lastId = temp.getPartnerId().intValue();
//            }
//        }
//        partner.setBusiness((Business)businessMap.get(partner.getBusiness().getBusinessId()));
//        partner.setPartnerId(new Integer(lastId + 1));
//        partners.add(partner);
    }

    public boolean deleteByPrimaryKey( long id ) throws DaoException {
		return false;
//        for (int i = 0; i < partners.size(); i++) {
//            Partner tempPartner = (Partner)partners.get(i);
//            if (tempPartner.getPartnerId().equals(id)) {
//                partners.remove(i);
//                break;
//            }
//        }
    }

	public Partner findById(long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
