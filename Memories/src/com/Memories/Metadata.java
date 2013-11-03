package com.Memories;

import java.util.ArrayList;

public class Metadata {
	
	private ArrayList<Event> events;
	
	public Metadata()
	{
		this.events = new ArrayList<Event>();
	}
	
	public void addEvent(String name, String desc) {
		Event e = new Event(name, desc);
		this.events.add(e);
	}
	
	public String Jsonify()
	{
    	JsonParser parse = new JsonParser();
    	return parse.createMetadataJson(this.events);
	}
}
