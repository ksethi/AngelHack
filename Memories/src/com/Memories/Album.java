package com.Memories;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.util.Log;

public class Album extends AsyncTask<String,String,String> {

	public String name;
	public String description;
	public Date date;
	public float latitude;
	public float longitude;
	public ArrayList<String> imgNames;
	public String textLocation;
	public boolean hasLocation;
	
	public Album(String Json)
	{
		JsonParser parser = new JsonParser();
		HashMap<String, Object> hash = (HashMap)parser.albumStringToMap(Json);
		for (Map.Entry entry : hash.entrySet() )
		{
			Log.d("val", "hash " + entry.getKey() + entry.getValue());
		}
		this.name = (String)hash.get("name");
		this.description = (String)hash.get("desc");
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		try {this.date = formatter.parse((String)hash.get("date")); } catch (Exception e) { Log.d("Date ", "Date prob " + e.toString()); };
		this.imgNames = new ArrayList<String>();
		imgNames.add("funny.bmp");
		for (String entry : this.imgNames )
		{
			Log.d("val", "img " + entry);
		}
				//
		this.hasLocation = false;

		///this.latitude = Float.parseFloat((String)hash.get("lat"));
		///this.longitude = Float.parseFloat((String)hash.get("long"));
		//this.SearchForLocation();
	}
	
	public Album(String name,  String desc) {
		this.name = name;
		this.description = desc;
		this.date = new Date();
		this.imgNames = new ArrayList<String>();
		this.hasLocation = false;
	}
	
	public Album(String name, String desc, Date date) {
		this.name = name;
		this.description = desc;
		this.date = date;
		this.imgNames = new ArrayList<String>();
		this.hasLocation = false;
	}
	
	public void AddImage(String imageName) {
		this.imgNames.add(imageName);
	}
	
	public ArrayList<String> GetImageNames() {
		return this.imgNames;
	}
	
	public void SetLocation(float lati, float longi) {
		this.latitude = lati;
		this.longitude = longi;
		this.SearchForLocation();
	}
	
	public String GetLocation()
	{
		if(hasLocation)
		{
			return this.textLocation;
		} else {
			return "NOLOC";
		}
	}
	
	public void SearchForLocation() {
		HttpClient httpclient = new DefaultHttpClient();

	    String url = null;
	    url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + Float.toString(latitude) + "," + Float.toString(longitude) + "&sensor=true";
	    
	    this.doInBackground(url);
	    
	}
	
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        
        Log.d("HttpRequestHandler",result);
        //Do anything with response..
        textLocation = result;
        hasLocation = true;
    }
    
    private String Jsonify()
    {
    	JsonParser parse = new JsonParser();
    	return parse.createJsonAlbum(this.name, this.date, this.description, this.latitude, this.longitude, this.imgNames.toArray());
    }
    
    public String GetPath()
    {
    	return this.name + "/" + "metadata.json";
    }

    //TODO READ FROM DISK FOR AN ALBUM
}
