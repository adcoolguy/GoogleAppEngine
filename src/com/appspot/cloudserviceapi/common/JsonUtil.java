package com.appspot.cloudserviceapi.common;

import java.text.DateFormat;

//import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.model.Calendar;

/**
 * https://sites.google.com/site/gson/gson-user-guide
 */
public class JsonUtil {

	public static String toString(Object obj) {
//		Gson gson = new Gson();
		Gson gson = new GsonBuilder()
//	     .registerTypeAdapter(Id.class, new IdTypeAdapter())
//		 .registerTypeAdapter(String.class, new StringConverter())		
	     .enableComplexMapKeySerialization()
	     .serializeNulls()	//output null feidls as well
	     .setDateFormat(DateFormat.LONG)
	     .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
	     .setPrettyPrinting()
	     .setVersion(1.0)
	     .disableHtmlEscaping()	//https://groups.google.com/forum/?fromgroups=#!topic/google-gson/uVkDErWkCjE
	     .create();
		
		String json = null;
		try {
			json = //new String(
					gson.toJson(obj)
					//.getBytes("utf-8")
					//, "utf-8"
					//)
					;
			//=== http://stackoverflow.com/questions/42068/how-do-i-handle-newlines-in-json/42073#42073
			json = json.replaceAll("\n"," ");	//fix "SyntaxError: JSON Parse error: Unexpected EOF" in Apple Safari browser
			//=== Good as at 12/13/2013(thanks to https://groups.google.com/forum/#!topic/google-gson/W3eXzqCnZ6U)
			json = json.replaceAll("null","\"\"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}

	public static Object toObject(String json, Class type) {
		Gson gson = new Gson();

		Object obj = gson.fromJson(json, type);

		return obj;
	}

	public static void main(String[] args) {
		Calendar cal = new Calendar();
		cal.setDescription("desc for test");
		cal.setHit(new Long(123));
		//to json
		String json = JsonUtil.toString(cal);
		System.out.println("json [" + json + "]");
		//back to pojo
		Calendar cal2 = (Calendar)JsonUtil.toObject(json, Calendar.class);
		System.out.println("object [" + cal2.toString() + "]");
	}

}
