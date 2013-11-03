package com.Memories;

import java.util.ArrayList;

public class Metadata {
	
	private static ArrayList<Event> events = new ArrayList<Event>();
	
	public Metadata()
	{
	}
	
	public static ArrayList<Event> getEvents()
	{
		return events;
	}
	public static void putJsonData(String json)
	{
		JsonParser parse = new JsonParser();
		events = parse.getMetadata(json);
	}
	
	public static void addEvent(String name, String desc) {
		Event e = new Event(name, desc);
		events.add(e);
	}
	
	public static String Jsonify()
	{
    	JsonParser parse = new JsonParser();
    	return parse.createMetadataJson(events);
	}
}
