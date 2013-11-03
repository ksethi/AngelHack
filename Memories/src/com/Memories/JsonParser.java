package com.Memories;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

public String createJsonObject (String name, Date date, String desc, float lat, float longi,  Object[] imgs){
		
		String eventName = name;
		String eventDate = date.toString();
		String eventDesc = desc;
		String eventLat = Float.toString(lat);
		String eventLong = Float.toString(longi);
		String[] imgList = (String[])imgs;
		
		JSONObject myObj = new JSONObject();
		try {
			myObj.put("name", eventName);
			myObj.put("date", eventDate);
			myObj.put("desc", eventDesc);
			myObj.put("lat", eventLat);
			myObj.put("long", eventLong);
			myObj.put("imageName", Arrays.toString(imgList));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return myObj.toString();
			
	}
	
public Map<String, Object> jsonStringToMap (String jsonStr){
	Map <String, Object> hm = new HashMap<String, Object>();
	
	String eventName = null;
	String eventDate = null;
	String eventDesc = null;
	float eventLat = (float) 0.0;
	float eventLong = (float) 0.0;
	String[] imgArray = null;
	
	try {
		JSONObject jObject = new JSONObject(jsonStr);
		eventName = jObject.getString("name");
		eventDate = jObject.getString("date");
		eventDesc = jObject.getString("desc");
		eventLat = (float)jObject.getDouble("lat");
		eventLong = (float)jObject.getDouble("long");
		
		
		String aJsonString = jObject.getString("imageName");
		imgArray = aJsonString.split(",");
		imgArray[0] = imgArray[0].replace("[","");
		imgArray[imgArray.length-1] = imgArray[imgArray.length-1].replace("]","");
		
		
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	hm.put("name", eventName);
	hm.put("date", eventDate);
	hm.put("desc", eventDesc);
	hm.put("lat", eventLat);
	hm.put("long", eventLong);
	hm.put("image", imgArray);
	return hm;
		
	}
}
