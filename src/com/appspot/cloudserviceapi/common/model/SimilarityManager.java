package com.appspot.cloudserviceapi.common.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SimilarityManager {

	private static Map freqMap;
	static {
		freqMap = new HashMap();
	}
	
	//KISS
	public static String toSimilarPattern(String input) {
		String retVal = null;

		retVal = getAsciiValue(input);
		//retVal = String.valueOf(countWords(input));

		return retVal;
	}
	
	public static int countWords(String input) {
		int i =0;
		if(input != null) {
			Scanner sc = new Scanner(input);
			while (sc.hasNext()) {
			    String token = sc.next();
				i++;
			}
		}
		return i;
	}
	
	private static String getAsciiValue(String input) {
		StringBuffer sb = new StringBuffer("");
		if(input != null) {
			int v = -1;
			for (int i=0; i<input.length();i++) {
				v = (int)input.charAt(i);
				//System.out.println("ASCII value of: "+input.charAt(i) + " is:"+ (int)input.charAt(i) );
				if(v == 32) {
					sb.append(" ");
				} else {
					sb.append(v);
				}
			}
		}
		return sb.toString();
	}
	
	public static String getCommonToken(String input) {
		String retVal = input;
		if(input != null) {
			int begin = input.indexOf(":");
			if(begin > -1) {
				retVal = retVal.substring(0, begin);
			}
		}
		return retVal;
	}

	public static void setAccumulatedFrequency(String pattern) {
		Long count = 0L;
		long temp = 0L;
		try {
			if(pattern != null) {
				count = (Long)freqMap.get(pattern);
				temp = count.longValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
			count = 0L;
		}
		temp++;
		freqMap.put(pattern, new Long(temp));
	}
	
	public static long getAccumulatedFrequency(String pattern) {
		long retVal = -1L;
		if(pattern != null) {
			try {
				retVal = ((Long)freqMap.get(pattern)).longValue();
			} catch (Exception e) {
				e.printStackTrace();
				retVal = 0L;
			}
		}
		return retVal;
	}
}
