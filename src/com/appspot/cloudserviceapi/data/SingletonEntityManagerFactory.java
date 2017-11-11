//source: http://hoangle.info/2010/07/02/jpa-in-google-app-engine-singleton-entitymanagerfactory/

package com.appspot.cloudserviceapi.data;

import java.util.HashMap;
import java.util.Map;

//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

public class SingletonEntityManagerFactory //extends LocalContainerEntityManagerFactoryBean 
{

	private static Map entityManagerFactoryMap = new HashMap();

//	@Override
//	protected EntityManagerFactory createNativeEntityManagerFactory()
//			throws PersistenceException {
//		EntityManagerFactory emf = (EntityManagerFactory) entityManagerFactoryMap
//				.get(getPersistenceUnitName());
//
//		if (emf == null) {
//			emf = super.createNativeEntityManagerFactory();
//			entityManagerFactoryMap.put(getPersistenceUnitName(), emf);
//		}
//
//		return emf;
//	}

}