package com.appspot.cloudserviceapi.common;

import com.appspot.cloudserviceapi.dto.Template;

public class BackupService {

	public static String safeStringForBackupService(String retVal) {
		retVal = retVal.replaceAll(" ", "_"); // mysql primary key can't not
												// have a space
		retVal = retVal.replaceAll("#", "_HASH_"); // url can't contain a #
		retVal = retVal.replaceAll("\\(", "_LPAREN_"); // url can't contain a
														// left parenthesis
		retVal = retVal.replaceAll("\\)", "_RPAREN_"); // url can't contain a
														// right parenthesis
		retVal = retVal.replaceAll("\\.", "_DOT_"); // dot cause confusion with
													// the last dot for
													// extension
		// retVal = retVal.replaceAll("-", "_DASH_");
		return retVal;
	}

	/** What ID for Backup Service - the first portion separated by : */
	public static String getUniqueWhat(Template myBean) {
		String retVal = myBean.getWhat(); // can not be empty
		if (myBean.getWhat() != null) {
			int foundIndex = myBean.getWhat().indexOf(":");
			if (foundIndex > -1) {
				retVal = myBean.getWhat().substring(0, foundIndex);
			}
			retVal = BackupService.safeStringForBackupService(retVal);
		}
		System.out.println("getWhat: '" + retVal + "'");
		return retVal;
	}

	/** Category ID for Backup Service - the second portion separated by : */
	public static String getUniqueCategory(Template myBean) {
		String retVal = ""; // can be empty/it is optional
		if (myBean.getWhat() != null) {
			int foundIndex = myBean.getWhat().indexOf(":");
			if (foundIndex > -1) {
				retVal = myBean.getWhat().substring(foundIndex + 1,
						myBean.getWhat().length());
				retVal = BackupService.safeStringForBackupService(retVal);
			}
		}
		return retVal;
	}
}
