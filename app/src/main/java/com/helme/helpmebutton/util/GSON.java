package com.helme.helpmebutton.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class GSON
{
	private static GSON _dgson = null;
	private Gson _gson = null;
	private GsonBuilder _gBuilder = new GsonBuilder();
	private boolean _initialized = false;

	public static synchronized GSON getInstance()
	{
		if(_dgson == null)
		{
			_dgson = new GSON();
		}
		
		return _dgson;
	}

	public void addSerializer(Type type, Object object) throws Exception
	{
		if(_dgson == null)
		{
			_dgson = new GSON(); 
		}
		
		if(_initialized)
		{
			throw new Exception("Cannot add serializer after initializing GSON class");
		}		
		else
		{
			_gBuilder.registerTypeAdapter(type, object);
		}
	}
	
	public void initialize()
	{
		_initialized = true;

		_gBuilder.registerTypeAdapter(HashMap.class, new HashMapDeserializer());
		_gBuilder.registerTypeAdapter(HashMap.class, new HashMapSerializer());
		//_gBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
		
		_gson = _gBuilder.create();
	}

	public <T> String toJson(Object obj, Class<T> objType)
	{
		return _gson.toJson(obj, objType);
	}

	public <T> T fromJson(String json, Class<T> objType) throws Exception
	{
		return _gson.fromJson(json, objType);
	}

	public String toJson(Object obj, Type objType)
	{
		return _gson.toJson(obj, objType);
	}

	public Object fromJson(String json, Type objType) throws Exception
	{
		return _gson.fromJson(json, objType);
	}

	// private class DateDeserializer implements JsonDeserializer<Date>
	// {
	// public Date getUTCDate(String date)
	// {
	// // Change this to match string date format
	// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
	// // this sets timezone
	// formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
	// try
	// {
	// return formatter.parse(date);
	// }
	// catch (ParseException e)
	// {
	// return null;
	// }
	// }
	//
	// @Override
	// public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws
	// JsonParseException
	// {
	// String date = element.getAsString();
	// Date utcDate = getUTCDate(date);
	// if (utcDate == null)
	// {
	// // if UTC Date is null then throw exception
	// throw new JsonParseException("date error");
	// }
	// return utcDate;
	// }
	// }

	//leaving this here just in case if we need to use it in the future
	/*private HashMap<String, String> getHashMap(JsonArray jsonArray)
	{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		String key = null;
		String value = null;

		for (int i = 0; i < jsonArray.size(); i++)
		{
			key = jsonArray.get(i).getAsJsonObject().get("Key").getAsString().toLowerCase();
			value = jsonArray.get(i).getAsJsonObject().get("Value").getAsString();

			if (!hashMap.containsKey(key))
			{
				hashMap.put(key, value);
			}
		}

		return hashMap;
	}

	private class HashMapDeserializer implements JsonDeserializer<HashMap<String, String>>
	{
		@Override
		public HashMap<String, String> deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException
		{
			return getHashMap(element.getAsJsonArray());
		}
	}*/

	/* some of the stuff in this class needs to be overriden at the app level
	private class ModuleDeserializer implements JsonDeserializer<Module>
	{
		@Override
		public Module deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException
		{
			Gson json = new Gson();
			Module module = json.fromJson(element, Module.class);

			// GsonBuilder gsonb = new GsonBuilder();
			// Gson gson = gsonb.create();
			//
			// Module module = gson.fromJson(element, Module.class);
			//
			// JsonObject moduleObject = element.getAsJsonObject();
			// if (moduleObject.get("ModuleData") != null && !moduleObject.get("ModuleData").isJsonNull())
			// {
			// module.ModuleData = getHashMap(moduleObject.get("ModuleData").getAsJsonArray());
			// }

			
			 * JsonObject moduleObject = element.getAsJsonObject();
			 * 
			 * Module module = new Module();
			 * 
			 * if (moduleObject.get("Description") != null && !moduleObject.get("Description").isJsonNull()) {
			 * module.setDescription(moduleObject.get("Description").getAsString()); } if
			 * (moduleObject.get("DestinationLocationCode") != null &&
			 * !moduleObject.get("DestinationLocationCode").isJsonNull()) {
			 * module.setDestinationLocationCode(moduleObject.get("DestinationLocationCode").getAsString()); } if
			 * (moduleObject.get("DisplayOrder") != null && !moduleObject.get("DisplayOrder").isJsonNull()) {
			 * module.setDisplayOrder(moduleObject.get("DisplayOrder").getAsInt()); } // if
			 * (moduleObject.get("GraphicUrl") != null && !moduleObject.get("GraphicUrl").isJsonNull()) // { //
			 * module.GraphicUrl = moduleObject.get("GraphicUrl").getAsString(); // } if (moduleObject.get("GraphicKey")
			 * != null && !moduleObject.get("GraphicKey").isJsonNull()) {
			 * module.setGraphicKey(moduleObject.get("GraphicKey").getAsString()); } if
			 * (moduleObject.get("IsVideoOnlyModule") != null && !moduleObject.get("IsVideoOnlyModule").isJsonNull()) {
			 * module.setIsVideoOnlyModule(moduleObject.get("IsVideoOnlyModule").getAsBoolean()); } if
			 * (moduleObject.get("IsWebLink") != null && !moduleObject.get("IsWebLink").isJsonNull()) {
			 * module.setIsWebLink(moduleObject.get("IsWebLink").getAsBoolean()); } if (moduleObject.get("ModuleCode")
			 * != null && !moduleObject.get("ModuleCode").isJsonNull()) {
			 * module.setModuleCode(moduleObject.get("ModuleCode").getAsString()); } if
			 * (moduleObject.get("ModuleGroupId") != null && !moduleObject.get("ModuleGroupId").isJsonNull()) {
			 * module.setModuleGroupId(moduleObject.get("ModuleGroupId").getAsInt()); } if (moduleObject.get("Name") !=
			 * null && !moduleObject.get("Name").isJsonNull()) { module.setName(moduleObject.get("Name").getAsString());
			 * } if (moduleObject.get("RegisteredDevicesOnly") != null &&
			 * !moduleObject.get("RegisteredDevicesOnly").isJsonNull()) {
			 * module.setRegisteredDevicesOnly(moduleObject.get("RegisteredDevicesOnly").getAsBoolean()); } if
			 * (moduleObject.get("UpdateRequired") != null && !moduleObject.get("UpdateRequired").isJsonNull()) {
			 * module.setUpdateRequired(moduleObject.get("UpdateRequired").getAsBoolean()); } if
			 * (moduleObject.get("VideoUrl") != null && !moduleObject.get("VideoUrl").isJsonNull()) {
			 * module.setVideoUrl(moduleObject.get("VideoUrl").getAsString()); }
			 

			// Not needed not a hashmap anymore
			// if (moduleObject.get("ModuleData") != null && !moduleObject.get("ModuleData").isJsonNull())
			// {
			// module.ModuleData = getHashMap(moduleObject.get("ModuleData").getAsJsonArray());
			// }

			try
			{
				Utils.applyModuleExtras(module);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}

			return module;
		}
	}*/

	public Object cloneObject(Object object, Class<?> classname)
	{
		try
		{
			String objString = this.toJson(object, classname);
			return this.fromJson(objString, classname);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	public static class HashMapDeserializer implements JsonDeserializer<HashMap<String, String>>
	{
		private HashMap<String, String> getHashMap(JsonArray jsonArray)
		{
			HashMap<String, String> hashMap = new HashMap<String, String>();
			String key = null;
			String value = null;

			for (int i = 0; i < jsonArray.size(); i++)
			{
				key = jsonArray.get(i).getAsJsonObject().get("Key").getAsString().toLowerCase();
                try
                {
                    value = jsonArray.get(i).getAsJsonObject().get("Value").getAsString();
                }
				catch (Exception e)
                {
//                    e.printStackTrace();
                }

				if (!hashMap.containsKey(key))
				{
					hashMap.put(key, value);
				}
			}

			return hashMap;
		}
		
		@Override
		public HashMap<String, String> deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException
		{
			return getHashMap(element.getAsJsonArray());
		}
	}
	
	private class HashMapSerializer implements JsonSerializer<HashMap<String, String>>
	{
		@Override
		public JsonElement serialize(HashMap<String, String> arg0, Type arg1, JsonSerializationContext arg2)
		{
			JsonArray element = new JsonArray();
			
			Iterator<Entry<String, String>> it = arg0.entrySet().iterator();
			
			while(it.hasNext())
			{
				Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("Key", pair.getKey());
				jsonObject.addProperty("Value", pair.getValue());
				element.add(jsonObject);
			}
			
			return element;
		}		
	}
	
//	public class DateDeserializer implements JsonDeserializer<Date>
//	{
//		public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
//		{
//			String s = json.getAsJsonPrimitive().getAsString();
//			long l = Long.parseLong(s.substring(6, s.length() - 2));
//			Date d = new Date(l);
//			return d; 
//		} 
//	}
}
