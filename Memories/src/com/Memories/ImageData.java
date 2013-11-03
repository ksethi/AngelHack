package com.Memories;

public class ImageData {
	private String fileName;
	private String caption;
	
	public ImageData(String fileName, String caption)
	{
		this.fileName = fileName;
		this.caption = caption;
	}
	
	public String Jsonify()
	{
		JsonParser parse = new JsonParser();
    	return parse.createJsonImageData(fileName, caption);
	}
}
