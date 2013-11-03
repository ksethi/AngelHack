package com.Memories;

public class Event {
	private String name;
	private String description;
	
	public Event(String name, String desc) {
		this.name = name;
		this.description = desc;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDesc()
	{
		return description;
	}
	
}