package com.appspot.cloudserviceapi.common;

import java.text.DateFormat;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * https://sites.google.com/site/gson/gson-user-guide
 */
public class GsonUtil {

	//To resolve "circular reference error", apply setExclusionStrategies c.f. http://stackoverflow.com/questions/3340485/how-to-solve-circular-reference-in-json-serializer-caused-by-hibernate-bidirecti
	public static String toString(Object obj) {
		class Exclude implements ExclusionStrategy {

		    @Override
		    public boolean shouldSkipClass(Class<?> arg0) {
		        // TODO Auto-generated method stub
		        return false;
		    }

		    @Override
		    public boolean shouldSkipField(FieldAttributes field) {
		        SerializedName ns = field.getAnnotation(SerializedName.class);
		        if(ns != null)
		            return false;
		        return true;
		    }
		}
		Exclude ex = new Exclude();		
//		Gson gson = new Gson();
		Gson gson = new GsonBuilder()
//	     .registerTypeAdapter(Id.class, new IdTypeAdapter())
	     .enableComplexMapKeySerialization()
	     .serializeNulls()
	     .setDateFormat(DateFormat.LONG)
	     .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
	     .setPrettyPrinting()
	     .setVersion(1.0)
	     .disableHtmlEscaping()	//https://groups.google.com/forum/?fromgroups=#!topic/google-gson/uVkDErWkCjE
	     .addDeserializationExclusionStrategy(ex)
	     //.addSerializationExclusionStrategy(ex)	     
	     .create();
		
		String json = gson.toJson(obj);
		//=== Good as at 12/13/2013(thanks to https://groups.google.com/forum/#!topic/google-gson/W3eXzqCnZ6U)
		json = json.replaceAll("null","\"\"");

		return json;
	}

	public static Object toObject(String json, Class type) {
		Gson gson = new Gson();

		Object obj = gson.fromJson(json, type);

		return obj;
	}

//	public static void main(String[] args) {
//		Calendar cal = new Calendar();
//		cal.setDescription("desc for test");
//		cal.setHit(new Long(123));
//		//to gson
//		String gson = GsonUtil.toString(cal);
//		System.out.println("gson [" + gson + "]");
//		//back to pojo
//		Calendar cal2 = (Calendar)GsonUtil.toObject(gson, Calendar.class);
//		System.out.println("object [" + cal2.toString() + "]");
//	}

}
