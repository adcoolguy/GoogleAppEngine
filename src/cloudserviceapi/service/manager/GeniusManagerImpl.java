package cloudserviceapi.service.manager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tapp.model.ServiceRegistry;
import app.model.Movie;
import app.model.User;
import tapp.model.ServiceRegistry;
import cloudserviceapi.app.controller.UserHandler;

import com.appspot.cloudserviceapi.dao.GeniuDao;
import com.appspot.cloudserviceapi.dao.gae.DaoFactoryImpl;
import com.appspot.cloudserviceapi.data.Datastore;
import com.appspot.cloudserviceapi.dto.Geniu;
import com.google.appengine.api.datastore.Transaction;
//import com.spoledge.audao.db.dao.DaoException;

@Api(value = "genius", tags = "Genius Manager")
public class GeniusManagerImpl implements GeniusManager {

    private static final Logger logger = LoggerFactory.getLogger(GeniusManagerImpl.class);
	private static List<Geniu> clonedList;	//used by UI to save datastore read free quota
	public List<Geniu> getClonedList() {
		return clonedList;
	}

	private static GeniuDao dao = (new DaoFactoryImpl()).createGeniuDao(Datastore.getDS());
	private List<Geniu> myBeans = getGenius();
		
	@ApiOperation(httpMethod = "GET", 
			   value = "Resource to get an genius items" 
//			   , response = SampleData.class
			   , nickname="getGenius"
			)
	public List<Geniu> getGenius() {	//jprofiler - 60 ms invoked 6 times per rendered page (about 10 ms each)
        Transaction tx = null;
        tx = Datastore.getDS().beginTransaction();
        Geniu[] g = null;
		if(clonedList == null) {
			g = (Geniu[]) dao.findAll();
			Geniu[] woList = g;
			clonedList = new ArrayList<Geniu>(woList.length);
			try {
				for (Geniu wo : woList) {
					clonedList.add((Geniu) wo.clone());
				}
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
        tx.commit();

		return clonedList;
	}

	public void setGeniu(List<Geniu> myBeans) {
		this.myBeans = myBeans;
	}

	public GeniuDao getDao() {
		return dao;
	}

	public void setDao(GeniuDao dao) {
		GeniusManagerImpl.dao = dao;
	}

	public void delete(Long id) {
        Transaction tx = null;
		try {
            tx = Datastore.getDS().beginTransaction();
			dao.deleteByPrimaryKey(id);
			myBeans = getGenius();
			//=== remove the cache too
			removeCache(id);
            tx.commit();
		} catch (Exception e) {
	        tx.rollback();
			e.printStackTrace();
		}
	}

	private void removeCache(Long id) {
		Geniu sr = null;
		for(int i = 0; i < clonedList.size(); i++) {
			sr = (Geniu)clonedList.get(i);
			if(sr.getId() != null && sr.getId().longValue() == id) {
				clonedList.remove(i);
				break;
			}
		}
	}

//	private void removeCache1(Long id) {
//		if(clonedList != null) {
//			List<Geniu> newClonedList = new ArrayList<Geniu>();
//			Geniu tmp1 = null;
//			for(int j=0; j<clonedList.size(); j++) {
//				tmp1 = clonedList.get(j);
//				if(tmp1 != null && tmp1.getId().longValue() != id.longValue()) {
//					newClonedList.add(tmp1);
//				}
//			}
//			clonedList = newClonedList;
//		}
//	}
	
	public void save(Geniu myBean) throws Exception {
		Transaction tx = null;
        tx = Datastore.getDS().beginTransaction();
		if (myBeans.indexOf(myBean) > -1) {
			// update
			myBeans.set(myBeans.indexOf(myBean), myBean);
		} else {
			myBeans.add(myBean);
		}
		try {
    		if (myBean != null) {
    			Geniu g = getGeniu(myBean.getId());
    			if(g != null) {
	    			dao.update(g.getId(), myBean);
	    			updateCache(g.getId(), myBean);
	                tx.commit();    			
	    		} else {
	                dao.insert(myBean);
	                insertCache(myBean);
	                tx.commit();    			
	    		}
    		}
		} catch (Exception e) {
	        tx.rollback();
			e.printStackTrace();
	        throw e;
		}
	}

	public void insertCache(Geniu wo) {
		if(clonedList != null) clonedList.add(wo);
	}
	
	public void updateCache(Long long1, Geniu wo) {
		boolean found = false;
		if(clonedList != null) {
			Iterator<Geniu> itr = clonedList.iterator();
			Geniu sr = null;
			for(int i = 0; i < clonedList.size(); i++) {
				sr = (Geniu)clonedList.get(i);
				if(sr.getId() == long1) {
					clonedList.set(i, wo);
					break;
				}
			}
		}
	}

	public void updateCache1(Long long1, Geniu wo) {
		List<Geniu> gl = getGenius();
		List<Geniu> newclonedList = new ArrayList();
		if(gl != null) {
			Geniu tmp = null;
			for(int i=0; i<gl.size(); i++) {
				tmp = gl.get(i);
				logger.debug("GeniusManagerImpl.java#updateCache: tmp id [" + tmp.getId() + "] Geniu id [" + wo.getId() + "]");
				if(tmp != null && wo != null && tmp.getId() != null && tmp.getId().longValue() != long1) {
					newclonedList.add(tmp);
				} else {
					newclonedList.add(wo);
				}
			}
		}
		clonedList = newclonedList;
	}
	
	public Geniu getGeniu(Long id) {
		Geniu retVal = null;
        Transaction tx = null;
        tx = Datastore.getDS().beginTransaction();
		retVal = (Geniu) dao.findByPrimaryKey(id);
        tx.commit();    			
		return retVal;
	}

	@Override
	public String toString() {
		return myBeans.toString();
	}

//	public void updateCache1(Geniu wo) {
//		boolean found = false;
//		List<Geniu> clonedList = getGenius();
//		if(clonedList  != null) {
//			Iterator<Geniu> itr = clonedList.iterator();
//			Geniu sr = null;
//			for(int i = 0; i < clonedList.size(); i++) {
//				sr = (Geniu)clonedList.get(i);
//				if(sr.getId() == wo.getId()) {
//					clonedList.set(i, wo);
//					found = true;
//					break;
//				}
//			}
//			if(!found) clonedList.add(wo);
//		}
//	}

	public void updateCache(Geniu wo) {
		List<Geniu> gl = getGenius();
		List<Geniu> newclonedList = new ArrayList();
		boolean existing = false;
		if(gl != null) {
			Geniu tmp = null;
			for(int i=0; i<gl.size(); i++) {
				tmp = gl.get(i);
				logger.debug("GeniusManagerImpl.java#updateCache: tmp id [" + tmp.getId() + "] Geniu id [" + wo.getId() + "]");
				if(tmp != null && wo != null && tmp.getId() != null && wo.getId() != null && tmp.getId().longValue() != wo.getId().longValue()) {
					newclonedList.add(tmp);
				} else {
					existing = true;
					newclonedList.add(wo);
				}
			}
			if(!existing) {
				newclonedList.add(wo);	//new!
			}
		}
		clonedList = newclonedList;
	}
	

	/** Warning: This will clear all entries in the cache and will cause a query for all the entries again from the datastore 
	 * and it is EXPENSIVE (could consume around 10% of the datastore read!!!!) */
	public static void clearCache() {
		if(clonedList != null) {
			clonedList = null;
		}
		System.out.println("GeniusManagerImpl.java: clearCache() cache cleared!!!");
	}

}