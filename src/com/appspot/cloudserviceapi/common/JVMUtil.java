package com.appspot.cloudserviceapi.common;

public class JVMUtil {

	/*
	 * Show where a class (package name + the name) is loaded from.
	 * 
	 * @className the full name of the class including the package
	 * @reference http://forum.springsource.org/showthread.php?52499-Runing-Junit-test-org.apache.commons.pool.impl.GenericObjectPool-method-lt-init-gt-()V-no
	 * 
	 */
	public static String getLoadedFromJar(String className) throws ClassNotFoundException {
 	   	String retVal = null;
 	   
		try {
			retVal = Thread.currentThread().getContextClassLoader().loadClass(className).getProtectionDomain().getCodeSource().getLocation().toString();
		} catch (ClassNotFoundException e) {
			throw e;
		}
 	   
		return retVal;
	}
}
