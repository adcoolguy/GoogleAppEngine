package com.appspot.cloudserviceapi.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class StringUtil {

	public static String getPhoneAreaCode(String phone) {
		String retVal = "";

		if (phone != null) {
			int index = phone.indexOf("-");
			if (index > -1) {
				retVal = phone.substring(0, index);
			}
		}

		return retVal;
	}

	public static String getPhoneNumber(String phone) {
		String retVal = "";

		if (phone != null) {
			int index = phone.indexOf("-");
			if (index > -1) {
				retVal = phone.substring(index + 1, phone.length());
			}
		}

		return retVal;
	}

	public static String handleNull(String value) {
		String retVal = "";

		if (value != null) {
			retVal = value;
		}

		return retVal;
	}

	public static int handleNumber(String value) {
		int retVal = -1;

		if (value != null && !"".equals(value.trim())) {
			try {
				retVal = Integer.valueOf(value).intValue();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		return retVal;
	}

	public static Long handleLongNumber(String value) {
		Long retVal = -1L;

		if (value != null && !"".equals(value.trim())) {
			try {
				retVal = Long.valueOf(value).longValue();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		return retVal;
	}

	public static boolean handleYesNo(String value) {
		boolean retVal = false;

		if (value != null && "true".equalsIgnoreCase(value))
			retVal = true;

		return retVal;
	}

	public static boolean isNumber(String numberStr) {
		boolean retVal = true;

		try {
			Long n = Long.getLong(numberStr);
		} catch (Exception e) {
			retVal = false;
		}

		return retVal;
	}

	/*
	 * Escape special characters
	 * Source: http://www.javapractices.com/topic/TopicAction.do?Id=96
	 */
	public static String safeString(String str) throws Exception {
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(
				str);
		char character = iterator.current();
		boolean found = false;
		while (character != CharacterIterator.DONE) {
			if ((int) character < 32 || (int) character > 126) {
				result.append(" ");
				found = true;
				// System.out.println("Ctrl char detected -"+(int)character+"-, filtered with a space!");
			} else {
				result.append(character);
			}
			character = iterator.next();
		}
		if (found) {
			System.out.println("Ctrl char detected in str '" + str + "'");
		}
		return result.toString();
	}

	/** "xray" function - prints out its ASCII value */
	public static String toASCIICode(String str) throws Exception {
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(
				str);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			if ((int) character < 32 || (int) character > 126) {
				result.append("{").append((int)character).append("}");
			} else {
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}

	/**
	 * Source: http://www.kodejava.org/examples/237.html
	 */
	public static String toString(Throwable e) {
		// Create a StringWriter and a PrintWriter both of these object
		// will be used to convert the data in the stack trace to a string.
		//
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		//
		// Instead of writting the stack trace in the console we write it
		// to the PrintWriter, to get the stack trace message we then call
		// the toString() method of StringWriter.
		//
		e.printStackTrace(pw);

		return sw.toString();
	}	
}
