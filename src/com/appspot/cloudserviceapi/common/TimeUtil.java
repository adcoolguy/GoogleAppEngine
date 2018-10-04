package com.appspot.cloudserviceapi.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {

	private static String handleUTCDate(Date date) throws Exception {
		// "03-11-2008 11:00:00"
		SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		return sd.format(date);
	}

	/**
	 * Reference:
	 * http://www.coderanch.com/t/416639/java/java/Convert-Local-time-UTC-vice
	 * 
	 * @param p_city
	 * @param p_UTCDateTime
	 * @return
	 * @throws Exception
	 */
	public static String convertUTCtoLocalTime(String p_city,
			String p_UTCDateTime) throws Exception {

		String lv_dateFormateInLocalTimeZone = "";// Will hold the final
		// converted date
		Date lv_localDate = null;
		String lv_localTimeZone = "";
		SimpleDateFormat lv_formatter;
		SimpleDateFormat lv_parser;

		// Temp for testing(mapping of cities and timezones will eventually be
		// in a properties file
		if (p_city.equals("LON")) {
			lv_localTimeZone = "Europe/London";
		} else if (p_city.equals("NBI")) {
			lv_localTimeZone = "EAT";
		} else if (p_city.equals("BRS")) {
			lv_localTimeZone = "Europe/Brussels";
		} else if (p_city.equals("MNT")) {
			lv_localTimeZone = "America/Montreal";
		} else if (p_city.equals("LAS")) {
			lv_localTimeZone = "PST";
		}

		// create a new Date object using the UTC timezone
		lv_parser = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		lv_parser.setTimeZone(TimeZone.getTimeZone("UTC"));
		lv_localDate = lv_parser.parse(p_UTCDateTime);

		// Set output format - // prints "2007/10/25  18:35:07 EDT(-0400)"
		lv_formatter = new SimpleDateFormat("E MMM, dd yyyy hh:mm a z' ('Z')'");
//		System.out.println("convertUTCtoLocalTime " + p_city + ": "
//				+ "The Date in the UTC time zone(UTC) "
//				+ lv_formatter.format(lv_localDate));

		// Convert the UTC date to Local timezone
		lv_formatter.setTimeZone(TimeZone.getTimeZone(lv_localTimeZone));
		lv_dateFormateInLocalTimeZone = lv_formatter.format(lv_localDate);
//		System.out.println("convertUTCtoLocalTime: " + p_city + ": "
//				+ "The Date in the LocalTime Zone time zone "
//				+ lv_formatter.format(lv_localDate));

		return lv_dateFormateInLocalTimeZone;
	}

	public static Date getUTCLocalTime(String p_city,
			String p_UTCDateTime) throws Exception {

		String lv_dateFormateInLocalTimeZone = "";// Will hold the final
		// converted date
		Date lv_localDate = null;
		String lv_localTimeZone = "";
		SimpleDateFormat lv_formatter;
		SimpleDateFormat lv_parser;

		// Temp for testing(mapping of cities and timezones will eventually be
		// in a properties file
		if (p_city.equals("LON")) {
			lv_localTimeZone = "Europe/London";
		} else if (p_city.equals("NBI")) {
			lv_localTimeZone = "EAT";
		} else if (p_city.equals("BRS")) {
			lv_localTimeZone = "Europe/Brussels";
		} else if (p_city.equals("MNT")) {
			lv_localTimeZone = "America/Montreal";
		} else if (p_city.equals("LAS")) {
			lv_localTimeZone = "PST";
		}

		// create a new Date object using the UTC timezone
		lv_parser = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		lv_parser.setTimeZone(TimeZone.getTimeZone("UTC"));
		lv_localDate = lv_parser.parse(p_UTCDateTime);

		return lv_localDate;
	}

	public static Date convertUTCtoLocalDateTime(String p_city,
			String p_UTCDateTime) throws Exception {
		String lv_dateFormateInLocalTimeZone = "";// Will hold the final
		// converted date
		Date lv_localDate = null;
		String lv_localTimeZone = "";
		SimpleDateFormat lv_formatter;
		SimpleDateFormat lv_parser;

		// Temp for testing(mapping of cities and timezones will eventually be
		// in a properties file
		if (p_city.equals("LON")) {
			lv_localTimeZone = "Europe/London";
		} else if (p_city.equals("NBI")) {
			lv_localTimeZone = "EAT";
		} else if (p_city.equals("BRS")) {
			lv_localTimeZone = "Europe/Brussels";
		} else if (p_city.equals("MNT")) {
			lv_localTimeZone = "America/Montreal";
		} else if (p_city.equals("LAS")) {
			lv_localTimeZone = "PST";
		}

		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(lv_localTimeZone));
		lv_localDate = calendar.getTime();
		
		return lv_localDate;
	}
	
	public static String convertLocalTimeToUTC(String p_city,
			String p_localDateTime) throws Exception {

		String lv_dateFormateInUTC = "";// Will hold the final converted date
		Date lv_localDate = null;
		String lv_localTimeZone = "";
		SimpleDateFormat lv_formatter;
		SimpleDateFormat lv_parser;

		// Temp for testing(mapping of cities and timezones will eventually be
		// in a properties file
		if (p_city.equals("LON")) {
			lv_localTimeZone = "Europe/London";
		} else if (p_city.equals("NBI")) {
			lv_localTimeZone = "EAT";
		} else if (p_city.equals("BRS")) {
			lv_localTimeZone = "Europe/Brussels";
		} else if (p_city.equals("MNT")) {
			lv_localTimeZone = "America/Montreal";
		} else if (p_city.equals("LAS")) {
			lv_localTimeZone = "PST";
		}

		// create a new Date object using the timezone of the specified city
		lv_parser = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		lv_parser.setTimeZone(TimeZone.getTimeZone(lv_localTimeZone));
		lv_localDate = lv_parser.parse(p_localDateTime);

		// Set output format prints "2007/10/25  18:35:07 EDT(-0400)"
		lv_formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z'('Z')'");
		lv_formatter.setTimeZone(TimeZone.getTimeZone(lv_localTimeZone));

		System.out.println("convertLocalTimeToUTC: " + p_city + ": "
				+ " The Date in the local time zone "
				+ lv_formatter.format(lv_localDate));

		// Convert the date from the local timezone to UTC timezone
		lv_formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		lv_dateFormateInUTC = lv_formatter.format(lv_localDate);
		System.out.println("convertLocalTimeToUTC: " + p_city + ": "
				+ " The Date in the UTC time zone " + lv_dateFormateInUTC);

		return lv_dateFormateInUTC;
	}

	public static final Date getHQDate(Date now) {
		Date retVal = null;
		// assuming HQ is at east coast! :)
		try {
			if (now != null) {
				retVal = convertUTCtoLocalDateTime("MNT", handleUTCDate(now));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	public static final String getHQDateTime(Date now) {
		String retVal = "N/A";
		// assuming HQ is at east coast! :)
		try {
			if (now != null) {
				retVal = convertUTCtoLocalTime("MNT", handleUTCDate(now));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	public static Date toDateWithZone(String _4digityear, String _2digitmonth, String _2digitday, String _24hour, String _2digitminute, String tz) {
        SimpleDateFormat timestampFormatWithZone = new SimpleDateFormat("yyyy-MM-dd HH:mm zzz");
		//String dateStr = timestampFormatWithZone.format(date);
        //Date date = timestampFormatWithZone.parse("2007-06-06 14:00 EDT");
        Date realDate = null;
        String temp = _4digityear+ "-" +_2digitmonth+ "-" +_2digitday+ " " +_24hour+ ":" +_2digitminute + " " + tz;
		try {			
			realDate = timestampFormatWithZone.parse(temp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        System.out.println(temp + " converted to " + realDate);
        
		return realDate;
	}
	
	/**
	 * Calculate the days passed from dateEarly till dateLater.
	 * 
	 * @param dateEarly
	 * @param dateLater
	 * @return
	 */
	public static long calculateDays(Date dateEarly, Date dateLater) {  
		   return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);  
	}
}
