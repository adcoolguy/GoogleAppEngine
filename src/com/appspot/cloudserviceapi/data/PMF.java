package com.appspot.cloudserviceapi.data;

import java.util.Properties;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

import com.appspot.cloudserviceapi.util.Compass;

public final class PMF {
	private static final PersistenceManagerFactory pmfInstance = 
			null;
//			JDOHelper.getPersistenceManagerFactory("transactions-optional");

	private static PersistenceManagerFactory pmfWithTransaction = null;

	static {
		Compass.initCompass(pmfInstance);
	}

	private PMF() {
	}

	public static PersistenceManagerFactory get1() {
		return pmfInstance;
	}

	public static PersistenceManagerFactory get() {
		if (pmfWithTransaction == null) {
			Properties newProperties = new Properties();
			newProperties.put("javax.jdo.PersistenceManagerFactoryClass",
//					"org.datanucleus.store.appengine.jdo.DatastoreJDOPersistenceManagerFactory"	//JDO 1
					"org.datanucleus.api.jdo.JDOPersistenceManagerFactory" //JDO 2
					);
			newProperties.put("javax.jdo.option.ConnectionURL", "appengine");
			newProperties.put("javax.jdo.option.NontransactionalRead", "true");
			newProperties.put("javax.jdo.option.NontransactionalWrite", "true");
			newProperties.put("javax.jdo.option.RetainValues", "true");
			newProperties.put("datanucleus.appengine.autoCreateDatastoreTxns",
					"true");
//			newProperties.put("appengine.orm.disable.duplicate.pmf.exception", "true");	//JDO 1
			newProperties.put("datanucleus.DetachAllOnCommit",
					"true");
			newProperties.put("datanucleus.DetachOnClose",
					"true");
			pmfWithTransaction = JDOHelper
					.getPersistenceManagerFactory(newProperties);
		}
		return pmfWithTransaction;
	}

}