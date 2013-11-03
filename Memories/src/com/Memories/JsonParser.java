package com.Memories;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

public String createJsonObject (String name, String date, String desc, String[] imgs){
		
		String eventName = name;
		String eventDate = date;
		String eventDesc = desc;
		String[] imgList = imgs;
		
		JSONObject myObj = new JSONObject();
		try {
			myObj.put("name", eventName);
			myObj.put("date", eventDate);
			myObj.put("desc", eventDesc);
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
	String[] imgArray = null;
	
	try {
		JSONObject jObject = new JSONObject(jsonStr);
		eventName = jObject.getString("name");
		eventDate = jObject.getString("date");
		eventDesc = jObject.getString("desc");
		
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
	hm.put("image", imgArray);
	return hm;
		
	}
}
