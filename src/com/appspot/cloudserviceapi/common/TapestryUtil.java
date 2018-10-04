package com.appspot.cloudserviceapi.common;

import org.apache.tapestry5.alerts.AlertManager;

public class TapestryUtil {

    private static AlertManager alertManager;

	public static AlertManager getAlertManager() {
		return alertManager;
	}

	public static void setAlertManager(AlertManager alertManager) {
		TapestryUtil.alertManager = alertManager;
	} 
    
}
