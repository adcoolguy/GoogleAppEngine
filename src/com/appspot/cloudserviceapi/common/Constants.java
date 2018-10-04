package com.appspot.cloudserviceapi.common;

public class Constants {

	//=== Genius Service settings
	public final static String GREETING_PRETEXT = "Welcome";
	public final static String MAIN_URL = "/sgc/jsp/main.jsp";
	public final static String NO_RIGHT_TO = "Sorry you do not have the required right to ";
	public final static String NOT_LOGGED_IN_TITLE = "guest";
	public final static String CORP1_MAIN_URL = "/"+SettingsDBUtils.getSettings("1.alias.company")+"/jsp/tv/main.jsp";
	public final static String EO_MAIN_URL = "/eo/html/index.html";
	public final static String SECURE_MAIN_URL = "/g/owasp";
	public final static String GENIUS_MAIN_URL = "/g/app";
	public final static String SOCIAL_MAIN_URL = "/g/social";

	//=== Service Registry settings
	public static final String LAUNCHER_URI = "/go";
	public static final String SR_URI = "/sci";

	//=== JWT
	public static final String JWT_SECRET_KEY = "jwtsecretkey01072016";		//can not have special characters like _ !!! :(

}