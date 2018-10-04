package com.appspot.cloudserviceapi.common;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.appspot.cloudserviceapi.data.AppEngine;

public class CodesionUtil {

	/*
	 * Prefix line numbers in each lines.
	 */
	public static String prefixLineNumbers(String content) {
		StringBuffer sb = new StringBuffer();
		if(content != null) {
			Scanner s = new Scanner(content);
			s.useDelimiter("\n");
			int count = 1;
			NumberFormat myFormat = NumberFormat.getInstance();
		    myFormat.setMinimumIntegerDigits(3);
		    while(s.hasNext()) {
				sb.append(myFormat.format(count++) + " " + s.next() + "\n");
			}
		}
		
		return sb.toString();
	}
	
	/*
	 * Check to make sure ticket number is supplied.
	 */
	public static boolean isTicketSupplied(String log) {
		boolean retVal = false;

		if (log == null)
			log = "";
		int start, end;
		start = log.indexOf(", t:");
		end = log.indexOf(", r:");
		String temp = null;
		if (start != -1 && end != -1) {
			temp = log.substring(start+4, end);
			System.out.println("ticket '" + temp + "'");
		}

//		int ticketNo = 0;
//		try {
//			ticketNo = Integer.valueOf(temp);
//			System.out.println("ticket number '" + ticketNo+ "'");
			if(temp != null) retVal = true;
//		} catch (Exception e) {
//			// e.printStackTrace();
//			System.out.println("ticket number '" + ticketNo+ "'");
//		} finally {
			System.out.println("isTicketSupplied '" + retVal + "'");
//		}
		return retVal;
	}

	/*
	 * Enforce the validation check based on the specified project only.
	 */
	public static boolean isIncluded(String project) {
		boolean retVal = false;

		if (project == null)
			project = "";
		int start;
		start = project.toLowerCase().indexOf(SettingsDBUtils.getSettings("name1.project"));
		if (start != -1) {
			retVal = true;
		}
		System.out.println("isIncluded '" + retVal + "'");

		return retVal;
	}

	/**
	 * Check to see if it is a supported file type for static code analysis.
	 */
	public static boolean isSupportedFileType(String file) {
		boolean retVal = false;
		
		if(file != null && (
				(file.toLowerCase().indexOf(".java") > -1) || 
				(file.toLowerCase().indexOf(".js") > -1) || 
				(file.toLowerCase().indexOf(".css") > -1) ||
				(file.toLowerCase().indexOf(".htm") > -1))) {
					retVal = true;
				}
				
		return retVal;
	}
	
	public static List<String> handleChanged(String project, String changed) {
		String serverEndPoint = "https://" + AppEngine.getHostName() + "/"+SettingsDBUtils.getSettings("1.alias.company")+"/svn?token=pgs100&uri=/" + project;
		//by default, a white space:
		StringTokenizer st = new StringTokenizer(changed);
		List l = new ArrayList();
        //when there are still more tokens, print out the
        //next one:
		String temp = null;
		while(st.hasMoreTokens()) {
			temp = st.nextToken();
			if(isSupportedFileType(temp)) {
				l.add(serverEndPoint + temp);
			} else {
				l.add(temp);
			}
		}
	
		return l;
	}
	
}
