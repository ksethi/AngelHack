package com.Memories;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.dropbox.sync.android.DbxAccountManager;

import android.app.Activity;
import android.app.AlertDialog;
//import android.R;
import android.app.ListActivity;
import android.content.*;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.widget.*;


public class MyActivity extends Activity {
	
	private Button btnWriteFile;
	private DropboxManager dbManager;
	public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
	private static final int CAMERA_PIC_REQUEST = 1;
	private static final int RESULT_LOAD_IMAGE = 2;
	private Button btnSelectPhoto;
	ImageView viewImage;
	TextView Exif;
	
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.activity_fullscreen);
		  
		  btnSelectPhoto = (Button)findViewById(R.id.btnSelectPhoto);
		  Exif = (TextView)findViewById(R.id.exif);
		  viewImage =(ImageView)findViewById(R.id.viewImage);
		  btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	loadPictureMenu();
	            }
	        });
		  
		  
		  btnWriteFile =  (Button)findViewById(R.id.btnWriteFile);
		  
		  
		  dbManager = new DropboxManager(getApplicationContext());
		  Log.d("onCreate", "Starting up the activity");
		  try {
		      Intent k = new Intent(MyActivity.this, DropBoxActivity.class);
		       startActivity(k);
		  } catch(Exception e) {

		  }
	 }
	
	public void loadPictureMenu()
	{
		final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
        builder.setTitle("Add New Photo");
        
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    loadCamera();
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    loadGallery();
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
		
	public void loadCamera() {
		Log.i("info", "Hello");
		//Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		//startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
		
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		
		//_file = new File(_dir, String.format("myPhoto_{0}.jpg", UUID.randomUUID()));

	    //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(_file));
	    
		//File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
		//cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivityForResult(cameraIntent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
	}
	
	public void loadGallery(){
		Intent galleryIntent = new Intent(Intent.ACTION_PICK);
		galleryIntent.setType("image/*");
		//galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
		
		Log.i("info", "gallery loaded");
	}
	
	public void buttonWriteFile(View view) {
		dbManager.writeJsonFile("This is a test file from KJ", "FirstEvent" , "testFile.json");
	}
	
	@Override
	protected void onResume() {
		super.onResume();	
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE) //Camera Request
        {
        	Log.i("info", "photo taken");
        	
        	Bitmap photo = (Bitmap) data.getExtras().get("data");
            ImageView test = (ImageView) findViewById(R.id.viewImage);
            test.setImageBitmap(photo);
//            try{
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//                String currentDateandTime = sdf.format(new Date()).replace(" ","");
//                FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/memories"+currentDateandTime+".jpg");
//                photo.compress(Bitmap.CompressFormat.JPEG, 90, out);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
            
        }
        else if(requestCode == 2) //Gallery Request
        {
        	Uri selectedImage = data.getData();
            String[] filePath = { MediaColumns.DATA };
            Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
            Log.w("path of image from gallery......******************.........", picturePath+"");
            viewImage.setImageBitmap(thumbnail);
          
            Float latitude = getLatitude(picturePath);
            Float longitude = getLongitude(picturePath);
            Exif.setText("LON " + Float.toString(longitude) + " LAT " + Float.toString(latitude));
            connectGeo(latitude, longitude);
        }
    }   
	
	public void connectGeo(Float latitude, Float longitude)
	{
		HttpClient httpclient = new DefaultHttpClient();

	    String url = null;
	    url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + Float.toString(latitude) + "," + Float.toString(longitude) + "&sensor=true";
	    
	    new HttpRequestHandler().execute(url);
	}
	
//	private static String convertStreamToString(InputStream is) {
//	    /*
//	     * To convert the InputStream to String we use the BufferedReader.readLine()
//	     * method. We iterate until the BufferedReader return null which means
//	     * there's no more data to read. Each line will appended to a StringBuilder
//	     * and returned as String.
//	     */
//	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//	    StringBuilder sb = new StringBuilder();
//
//	    String line = null;
//	    try {
//	        while ((line = reader.readLine()) != null) {
//	            sb.append(line + "\n");
//	        }
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    } finally {
//	        try {
//	            is.close();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//	    }
//	    return sb.toString();
//	}
	
	Float getLongitude(String Path)
	{
		String Longitude = null;
		String Direction = null;
		
        try {
        	ExifInterface exifInterface = new ExifInterface(Path);
        	Longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        	Direction =	exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
        	Log.i("info", "Direction " + Direction);
        }
        
        catch (IOException e)
        {
        	// TODO Auto-generated catch block
        	   e.printStackTrace();
        	   Toast.makeText(this, 
        	     e.toString(), 
        	     Toast.LENGTH_LONG).show();
        }
        Log.i("info", "Longitude " + Longitude);
        Float finalCoord = convertToDegree(Longitude);
        Log.i("Longitude", Direction);
        if (Direction.equals("W")){
        	Log.d("getDelong","In the if");
        	finalCoord = -finalCoord;
        }
        return finalCoord;
	}
	
	Float getLatitude(String Path)
	{
		
		String Latitude = null;
		String latDirection = null;
				
        try {
        	Log.i("info", "In Latitude :)");
        	ExifInterface exifInterface = new ExifInterface(Path);
        	Latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        	latDirection = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
        	Log.i("info", "end of lat");
        }
        
        catch (IOException e)
        {
        	// TODO Auto-generated catch block
        	   e.printStackTrace();
        	   Toast.makeText(this, 
        	     e.toString(), 
        	     Toast.LENGTH_LONG).show();
        }
    	Log.i("info", "done with try stuff");
        Float finalCoord = convertToDegree(Latitude);
    	Log.i("info", "done gettng final coord");
        Log.i("Latitude", latDirection);
        if (latDirection.equals("S")){
        	Log.d("getLat","In the if");
        	finalCoord = -finalCoord;
        }
        Log.i("info", Latitude);
        return finalCoord;
	}
	
	private Float convertToDegree(String stringDMS){
		 Float result = null;
		 String[] DMS = stringDMS.split(",", 3);

		 	String[] stringD = DMS[0].split("/", 2);
		    Double D0 = new Double(stringD[0]);
		    Double D1 = new Double(stringD[1]);
		    Double FloatD = D0/D1;

			 String[] stringM = DMS[1].split("/", 2);
			 Double M0 = new Double(stringM[0]);
			 Double M1 = new Double(stringM[1]);
			 Double FloatM = M0/M1;
			  
			 String[] stringS = DMS[2].split("/", 2);
			 Double S0 = new Double(stringS[0]);
			 Double S1 = new Double(stringS[1]);
			 Double FloatS = S0/S1;
		  
		    result = new Float(FloatD + (FloatM/60) + (FloatS/3600));
		  
		 return result;
	};
}
