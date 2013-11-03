package com.Memories;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.dropbox.sync.android.DbxAccountManager;
import com.Memories.ExpandableListAdapter;
import com.Memories.R;

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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.ExpandableListView.OnChildClickListener;


public class MyActivity extends Activity {
	
	private Button btnWriteFile;
	private DropboxManager dbManager;
	public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
	private static final int CAMERA_PIC_REQUEST = 1;
	private static final int RESULT_LOAD_IMAGE = 2;
	private Button btnSelectPhoto;
	private Button btnIndividualPhoto;
	ImageView viewImage;
	TextView Exif;
	List<String> groupList;
    List<String> childList;
    Map<String, List<String>> laptopCollection;
    ExpandableListView expListView;
    String newGroup;
    String defaultDesc = "It was great";
	boolean createdView = false;
	@Override
	 public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.activity_fullscreen);
		  createGroupList();
	        
	        createCollection();
	        
	        expListView = (ExpandableListView) findViewById(R.id.laptop_list);
	        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
	                this, groupList, laptopCollection);
	        expListView.setAdapter(expListAdapter);
	 
	        //setGroupIndicatorToRight();
	 
	        expListView.setOnChildClickListener(new OnChildClickListener() {
	        	 
	            public boolean onChildClick(ExpandableListView parent, View v,
	                    int groupPosition, int childPosition, long id) {
	                final String selected = (String) expListAdapter.getChild(
	                        groupPosition, childPosition);
	                String parentName =  (String)expListAdapter.getGroup(groupPosition);

	                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
	                        .show();
	                try {
	      		      Intent k = new Intent(MyActivity.this, Gallery.class);
	      		      k.putExtra("SelectedName",selected );
	      		      k.putExtra("Parent", parentName);
	      		      //k.putExtra("DBMan", value)
	      		      startActivity(k);
	      		  } catch(Exception e) {

	      		  }
	                return true;
	            }
	        });
	     
	        btnIndividualPhoto = (Button)findViewById(R.id.individual);
	        btnIndividualPhoto.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
					Intent i = new Intent(getApplicationContext(), DisplayImage.class);
					//i.putExtra("image name", "value");
					startActivity(i);
				}
			});
	        
		  /*
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
		  */
	      //setContentView(R.layout.activity_drop_box);
		  dbManager = new DropboxManager(getApplicationContext());

		  try {
		      Intent k = new Intent(MyActivity.this, DropBoxActivity.class);
		       startActivity(k);
		  } catch(Exception e) {

		  }
	 }
	
	@Override
	protected void onStart()
	{
		super.onStart();
		 Log.d("onStart", "Starting up the activity");
	}
	

		

	public void buttonWriteFile(View view) {
		dbManager.writeJsonFile("This is a test file from KJ", "FirstEvent" , "testFile.json");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("onResume", "Starting up the activity");
		
		if (!createdView && dbManager.getdbfxFileSystem() != null)
		{
			createdView = true;
			String dbOutput = dbManager.readJsonFile("", "metadata.json");
			if (dbOutput == "FAIL")
			{
				Log.i("NOMETADETA","Metadata doesn't exist.");
			}
			else
			{
				Metadata.putJsonData(dbOutput);
				Log.d("Create MetaD","MetaDeta created" );
				ArrayList<Event> e = Metadata.getEvents();
				Log.d("mDRan","MetaDeta ran" );
				
				for (Event myEvent : e)
				{
					newGroup = myEvent.getName();
			    	defaultDesc = myEvent.getDesc();
			      	addNewGroup();
				}
			}
		}
		
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE) //Camera Request
        {
        	Log.i("info", "photo taken");
        	
        	//Bitmap photo = (Bitmap) data.getExtras().get("data");
            //ImageView test = (ImageView) findViewById(R.id.viewImage);
            //test.setImageBitmap(photo);
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
	private void ShowDialog()
    {
    	Context context = getApplicationContext();
    	LinearLayout layout = new LinearLayout(context);
    	layout.setOrientation(LinearLayout.VERTICAL);

    	final EditText titleBox = new EditText(context);
    	titleBox.setHint("Title");
    	layout.addView(titleBox);

    	final EditText descriptionBox = new EditText(context);
    	descriptionBox.setHint("Description");
    	layout.addView(descriptionBox);

    	//dialog.setView(layout);
    	
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
    	alert.setTitle("Add Event");
    	alert.setView(layout);


    	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	  public void onClick(DialogInterface dialog, int whichButton) {
    	    // Canceled.
    	  }
    	});
    	
    	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    	public void onClick(DialogInterface dialog, int whichButton) {
    		
    		
    	  newGroup = titleBox.getText().toString();
    	  defaultDesc = descriptionBox.getText().toString();
    	  
    	  Metadata.addEvent(newGroup, defaultDesc);
    	  String jsoned = Metadata.Jsonify();
    	  Log.d("jsdong","jsoned" + jsoned);
    	  dbManager.writeJsonFile(jsoned, "", "metadata.json");
      	addNewGroup();
    	  // Do something with value!
    	  }
    	});

    	alert.show();
    }
    public void HeaderOnClick(View v)
    {
    	Log.d("Hello", "Hello");
    	ShowDialog();
    }

    private void createGroupList() {
        groupList = new ArrayList<String>();
 //       groupList.add("Angel Hack");
 //       groupList.add("Portland");
 //       groupList.add("White Water Rafting");
    }
    
    private void addNewGroup() {
    	groupList.add(newGroup);
    	childList = new ArrayList<String>();
    	childList.add(defaultDesc);
    	laptopCollection.put(newGroup, childList);
    	((BaseAdapter) expListView.getAdapter()).notifyDataSetChanged();
    }
 
    private void createCollection() {
        // preparing laptops collection(child)
        //String[] hpModels = { "Description: Fake description\nDate:Oct 27th" };
        //String[] hclModels = { "Description: Fake description\nDate:Oct 27th" };
        //String[] sonyModels = { "Description: Fake description\nDate:Oct 27th" };
        laptopCollection = new LinkedHashMap<String, List<String>>();
        /*
        for (String laptop : groupList) {
            if (laptop.equals("Angel Hack")) {
                loadChild(hpModels);
            } else if (laptop.equals("Portland"))
            {
                loadChild(hclModels);
            }
            else
            {
                loadChild(sonyModels);
            }
 
            laptopCollection.put(laptop, childList);
        }
        */
    }
    
    private void loadChild(String[] laptopModels) {
        childList = new ArrayList<String>();
        for (String model : laptopModels)
            childList.add(model);
    }
    
    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
 
        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }
    
 // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }
}
