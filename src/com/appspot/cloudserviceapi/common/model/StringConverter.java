package com.appspot.cloudserviceapi.common.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/*
 * http://stackoverflow.com/questions/9483348/gson-treat-null-as-empty-string
 */
public class StringConverter implements JsonSerializer<String>,
		JsonDeserializer<String> {

	@Override
	public JsonElement serialize(String src, java.lang.reflect.Type type,
			JsonSerializationContext arg2) {
		JsonElement retVal = new JsonPrimitive("TEST");

//		if(type.getClass() == String.class) {
//			if (src == null) {
//				retVal = new JsonPrimitive("");
//			} else {
//				retVal = new JsonPrimitive(src.toString());
//			}
//		}
		System.out.println("src [" + src + "] type [" + type + "]");
		
		return retVal;
	}

	@Override
	public String deserialize(JsonElement json, java.lang.reflect.Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		// todo auto-generated method stub
		return json.getAsJsonPrimitive().getAsString();
	}

}