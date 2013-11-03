package com.Memories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.Memories.Metadata.Event;

public class JsonParser {

public String createJsonAlbum (String name, Date date, String desc, float lat, float longi,  Object[] imgs){
		
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

public String createJsonImageData (String filename, String caption) {
		JSONObject myObj = new JSONObject();
		try {
			myObj.put("name", filename);
			myObj.put("caption", caption);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return myObj.toString();
}

public String createMetadataJson (ArrayList<Event> events) {
	ArrayList<String> strings = new ArrayList<String>();
	for(Event ev : events) {
		JSONObject myObj = new JSONObject();
		try {
			myObj.put("name", ev.getName());
			myObj.put("desc", ev.getDesc());
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		strings.add(myObj.toString());	
	}
	
	String retString = "[";
	for(int i = 0; i < strings.size(); i++) {
		retString += strings.get(i);
		if(i != strings.size() -1 ) 
		{
			retString += ",";
		}
	}
	retString += "]";
	return retString;
}

public ArrayList<Event> getMetadata(String jsonStr) {
	ArrayList<Event> events = new ArrayList<Event>();
	try {
		JSONArray jsonArray = new JSONArray(jsonStr);
		for (int i = 0; i < jsonArray.length(); i++)
		{
			JSONObject obj = jsonArray.getJSONObject(i);
			events.add(new Event(obj.getString("name"), obj.getString("desc")));
		}
		return events;
	} catch (JSONException e) {
		return null;
	}
}
	
public Map<String, Object> albumStringToMap (String jsonStr){
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


	public Map<String, Object> imageDataStringToMap (String imgData) {
		Map<String, Object> hm = new HashMap<String, Object>();
		
		String name = null;
		String caption = null;
		
		try {
			JSONObject jObject = new JSONObject(imgData);
			name = jObject.getString("name");
			caption = jObject.getString("caption");
		} catch(JSONException e) {
			e.printStackTrace();
		}
		
		hm.put("name", name);
		hm.put("caption", caption);
		return hm;
	}

}
