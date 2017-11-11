package com.appspot.cloudserviceapi.util;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.compass.core.CompassIndexSession;
import org.compass.core.config.CompassConfiguration;
import org.compass.core.config.CompassEnvironment;
import org.compass.gps.CompassGps;
import org.compass.gps.device.jdo.Jdo2GpsDevice;
import org.compass.gps.impl.SingleCompassGps;

import com.appspot.cloudserviceapi.common.TapestryUtil;
import com.appspot.cloudserviceapi.data.PMF;
import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

/*
 * Reference: http://unknownerror.net/2011-06/in-your-application-integration-search-function-compass-31127
 */
public final class Compass {

	private static long count = 1;
	private static org.compass.core.Compass compass = null;

	private static CompassGps compassGps = null;
	private static Jdo2GpsDevice gpsDevice = null;
	private static String indexName = "appengine2";
	private static PersistenceManagerFactory pmfInstance;
	
	@Deprecated
	public static org.compass.core.Compass getCompass() throws Exception {
		if(compass == null) {
			//throw new Exception("Compass is not initialized!");
			init();
		}

		return compass;
	}

	public static org.compass.core.Compass getCompass(Class targetClass) throws Exception { //jprofiler - 14 s !
		if(compass == null) {
			//throw new Exception("Compass is not initialized!");
//			init(targetClass);		//TBD - front end logic need to be improved
			init();			
		}

		return compass;
	}
	
	public static String getIndexName() {
		return indexName;
	}

	public static void initCompass(PersistenceManagerFactory pmf) {
		pmfInstance = pmf;
	}

	/** This is a very expensive operation, and should be invoked as infrequent as possible */
	private static void init() { //jprofiler - 14 s !
		if(compass == null) {
			// === Compass setup: add any domain object intended for search as below
			compass = new CompassConfiguration()
					.setConnection("gae://index")
					.setSetting(
							CompassEnvironment.ExecutorManager.EXECUTOR_MANAGER_TYPE,
							"disabled")
					.setSetting("compass.engine.store.gae.memcacheRegexPatterns", "\\*")	//thanks to http://forum.compass-project.org/thread.jspa?messageID=298750&#298750
					.
					// === package at any levels (does not need to be the last
					// level/leaf)
					//addScan("guestbook")
					addClass(com.appspot.cloudserviceapi.guestbook.Greeting.class)
					.addClass(com.appspot.cloudserviceapi.dto.Huma.class)
					.addClass(com.appspot.cloudserviceapi.dto.Geniu.class)
					.addClass(com.appspot.cloudserviceapi.dto.Secure.class)
					.addClass(tapp.model.ServiceRegistry.class)
//					.addScan("tapp.model")
//					.addScan("com.appspot.cloudserviceapi.dto")
					//.addResource("compass.cpm.xml")
					.buildCompass();
			// === http://jira.grails.org/browse/GPSEARCHABLE-152
			compass.getSearchEngineIndexManager().releaseLocks();
			System.out.println("Compass initialized [count = " + count++ + "]");
		} else {
			System.out.println("Compass already initialized, request ignored!");
		}
	}

	/** Less expensive version of the above init() */
	private static void init(Class targetClass) {
//		if(compass == null) {
			// === Compass setup: add any domain object intended for search as below
			compass = new CompassConfiguration()
					.setConnection("gae://index")
					.setSetting(
							CompassEnvironment.ExecutorManager.EXECUTOR_MANAGER_TYPE,
							"disabled")
					.
					// === package at any levels (does not need to be the last
					// level/leaf)
					addClass(targetClass)
					.buildCompass();
			// === http://jira.grails.org/browse/GPSEARCHABLE-152
			compass.getSearchEngineIndexManager().releaseLocks();
			System.out.println("Compass initialized for target class " + targetClass + " [count = " + count++ + "]");
//		} else {
//			System.out.println("Compass already initialized, request ignored!");
//		}
	}
	
	public static void index() throws Exception {
		if(pmfInstance == null) {
			if((pmfInstance = PMF.get()) != null) {
				Compass.initCompass(pmfInstance);
			} else {
				throw new Exception("Please initialize pmf instance first!");
			}
		}

		init();

		compassGps = new SingleCompassGps(compass);
		gpsDevice = new Jdo2GpsDevice(indexName, pmfInstance);
		gpsDevice.setIgnoreMirrorExceptions(true);
		gpsDevice.setMirrorDataChanges(false);
		compassGps.addGpsDevice(gpsDevice);
		compassGps.start();
		// System.out.println("###### lucene index mirrored ######");
		if (!compassGps.isPerformingIndexOperation()) {
			//cause timeout, need to find a better solution; currently indexed manually from the JSP!!!
			compassGps.index();
//			index(Geniu.class);
			System.out.println("###### lucene index done ######");
			AlertManager alertManager = TapestryUtil.getAlertManager();
			if(alertManager != null) {
				alertManager.alert(Duration.SINGLE, Severity.INFO, "Indexing done.");
			}
		} else {
			System.out
					.println("###### lucene index skipped (currently running) ######");
		}
		// compass.close();
	}

	/**
	 * http://forum.compass-project.org/thread.jspa?messageID=299652
	 */
	public static void removeIndex(Object object, Class indexClass,
			PersistenceManager pm) {
		compass.getSearchEngineIndexManager().releaseLocks();
		// String indexName = Constants.Util.getIndexName(indexClass);
		CompassIndexSession indexSession = compass.openIndexSession();
		indexSession.delete(indexName, object);
		indexSession.close();
	}

	public static void index(Class indexedClass) throws Exception {
		init();
//		init(indexedClass);		//TBD - front end logic need to be improved
		index();
		if(compassGps != null) {
			compassGps.index(indexedClass);
			System.out.println(indexedClass.getName() + " indexed");
		} else {
			System.out.println("compassGps is null, index request skipped");
		}
	}

	public static void runIndex(Class targetClass) {
		System.out.println("runIndex: entered");
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		System.out.println("runIndex: ds " + ds);
		Queue queue = QueueFactory.getDefaultQueue();
		System.out.println("runIndex: queue " + queue);
		try {
		    Transaction txn = ds.beginTransaction();
			System.out.println("runIndex: txn " + txn);
		    queue.add(TaskOptions.Builder.withUrl("/task/compassindex").param("targetClass", targetClass.getName()).method(Method.GET));
			System.out.println("runIndex: index task added");
		    txn.commit();
			System.out.println("runIndex: commited");
		} catch (DatastoreFailureException e) {
			e.printStackTrace();
		}
		System.out.println("runIndex: done");
	}
}
