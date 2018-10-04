package com.appspot.cloudserviceapi.common;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.appspot.cloudserviceapi.data.PMF;

public class SettingsDBUtils {
	public static final Logger _logger = Logger.getLogger(SettingsDBUtils.class
			.getName());

	private static HashMap cache = new HashMap();

	// Currently we are hardcoding this list. But this could also be retrieved
	// from
	// database
	public static String getSettingsMasterList() throws Exception {
		return "Header,Body,Footer";
	}

	/**
	 * This method get a value of the settings specified by the pinCode from the datastore.
	 */
	public static String getSettings(String pinCode) {
		String retVal = "${" + pinCode + "}";
		if(cache.containsKey(pinCode)) {
			//only 30 ms :)
			retVal = (String) cache.get(pinCode);
		} else {
			//jprofiler = 137 ms per call
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Settings setting = null;
			//pinCode has to be unique
			Query query = pm.newQuery(Settings.class);
		    query.setFilter("pinCode == pinCodeParam");
//		    query.setOrdering("recorded desc");		//avoid multi-value property/custom index, this should reduce the cost (should be lower than 137 ms now)
		    query.declareParameters("String pinCodeParam");
	
		    try {
		        List<Settings> results = (List<Settings>) query.execute(pinCode);
		        if (!results.isEmpty()) {
		            for (Settings s : results) {
		            	setting = s;
		            	break;	//get only the first one !
		            }
		            retVal = setting.getSetting();
		            //=== add to cache
		            cache.put(pinCode, retVal);
		        } else {
		            // ... no results ...
		        }
		    } finally {
		        query.closeAll();
		    }
		}

    	return retVal;
	}
	
	/**
	 * This method persists a record to the database.
	 */
	public static void saveSettings(Settings settings)
			throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		//pinCode has to be unique
		Query query = pm.newQuery(Settings.class, "pinCode == pinCodeParam");
	    query.declareParameters("String pinCodeParam");

	    try {
	    	System.out.println("SettingsDBUtils saveSettings() pinCode [" + settings.getPinCode() + "] value [" + settings.getSetting() + "]");
	        List<Settings> results = (List<Settings>) query.execute(settings.getPinCode());
	        if (!results.isEmpty()) {
	            for (Settings s : results) {
	                throw new Exception("Duplicate pinCode!");
	            }
	        } else {
	            // ... no results ...
	        }
	    } finally {
	        query.closeAll();
	    }
		
		try {
			pm.makePersistent(settings);
			//dirty the cache
			if(cache != null) {
				cache.clear();
			}
			_logger.log(Level.INFO, "Setting has been saved");
		} catch (Exception ex) {
			_logger.log(
					Level.SEVERE,
					"Could not save the Setting. Reason : "
							+ ex.getMessage());
			throw ex;
		} finally {
			pm.close();
		}
	}

	public static void setSettings(String pinCode, String value) {
		String strRecordStatus = "ACTIVE";
		Date dt = new Date();
		Settings HR = new Settings(pinCode, value,
				strRecordStatus, dt);
		try {
			SettingsDBUtils.saveSettings(HR);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void updateSettings(String pinCode, String newValue) {
		Settings settings = new Settings(pinCode, null, null, null);
		
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    try {
	    	Query q = pm.newQuery(Settings.class, "pinCode == pPinCode");
	    	q.declareParameters("String pPinCode");

			  List<Settings> results = (List<Settings>) q.execute(pinCode);
			  if (!results.isEmpty()) {
			    for (Settings e : results) {
			      // Process result p
				  System.out.println("SettingsDBUtils updateSettings key [" + e.getPinCode() + "] old [" + e.getSetting() + "] new [" + newValue + "]");
				  e.setSetting(newValue);
				  break;
			    }
			  } else {
			    // Handle "no results" case
			  }
	    } finally {
	        pm.close();
	    }
	}
	
	/**
	 * This method gets the count all setting records in an area
	 * (Pincode/Zipcode) for the current month
	 * 
	 * @param Settings
	 * @param pinCode
	 * @return A Map containing the setting record name and the number of cases
	 *         reported for it in the current month
	 */
	public static Map<String, Integer> getSettingsCountForCurrentMonth(
			String Settings, String pinCode) {
		Map<String, Integer> _Settings = new HashMap<String, Integer>();

		PersistenceManager pm = null;

		// Get the current month and year
		Calendar c = Calendar.getInstance();
		int CurrentMonth = c.get(Calendar.MONTH);
		int CurrentYear = c.get(Calendar.YEAR);

		try {
			// Determine if we need to generate data for only one setting
			// Incident or ALL
			String[] Settingss = {};
			if (Settings.equalsIgnoreCase("ALL")) {
				String strSettingss = getSettingsMasterList();
				Settingss = strSettingss.split(",");
			} else {
				Settingss = new String[] { Settings };
			}

			pm = PMF.get().getPersistenceManager();
			Query query = null;

			// If Pincode (Zipcode) is ALL, we need to retrieve all the records
			// irrespective of Pincode
			if (pinCode.equalsIgnoreCase("ALL")) {
				// Form the query
				query = pm
						.newQuery(
								Settings.class,
								" setting == paramSettings && recorded >= paramStartDate && recorded < paramEndDate && status == paramStatus");

				// declare parameters used above
				query.declareParameters("String paramSettings, java.util.Date paramStartDate, java.util.Date paramEndDate, String paramStatus");
			} else {
				query = pm
						.newQuery(
								Settings.class,
								" setting == paramSettings && pinCode == paramPinCode && recorded >= paramStartDate && recorded <paramEndDate && status == paramStatus");

				// declare params used above
				query.declareParameters("String paramSettings, String paramPinCode, java.util.Date paramStartDate, java.util.Date paramEndDate, String paramStatus");
			}

			// For each setting record (i.e. Cold Flu Cough), retrieve the
			// records

			for (int i = 0; i < Settingss.length; i++) {
				int SettingsCount = 0;
				// Set the From and To Dates i.e. 1st of the month and 1st day
				// of next month
				Calendar _cal1 = Calendar.getInstance();
				_cal1.set(CurrentYear, CurrentMonth, 1);
				Calendar _cal2 = Calendar.getInstance();
				_cal2.set(CurrentYear, CurrentMonth + 1, 1);

				List<Settings> codes = null;
				if (pinCode.equalsIgnoreCase("ALL")) {
					// Execute the query by passing in actual data for the
					// filters
					codes = (List<Settings>) query.executeWithArray(
							Settingss[i], _cal1.getTime(),
							_cal2.getTime(), "ACTIVE");
				} else {
					codes = (List<Settings>) query.executeWithArray(
							Settingss[i], pinCode, _cal1.getTime(),
							_cal2.getTime(), "ACTIVE");
				}

				// Iterate through the results and increment the count
				for (Iterator iterator = codes.iterator(); iterator.hasNext();) {
					Settings _report = (Settings) iterator.next();
					SettingsCount++;
				}

				// Put the record in the Map data structure
				_Settings.put(Settingss[i], new Integer(
						SettingsCount));
			}
			return _Settings;
		} catch (Exception ex) {
			return null;
		} finally {
			pm.close();
		}
	}
}