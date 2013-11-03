package com.Memories;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class Album extends AsyncTask<String,String,String> {

	private String name;
	private String description;
	private Date date;
	private float latitude;
	private float longitude;
	private ArrayList<String> imgNames;
	private String textLocation;
	private boolean hasLocation;
	
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
    
    //TODO WRITE TO DISK FOR AN ALBUM WHEN ?
    //TODO READ FROM DISK FOR AN ALBUM
}
