package com.Memories;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.DropBoxManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

public class Gallery extends Activity {

	public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
	private static final int RESULT_LOAD_IMAGE = 2;
	Album album;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		Intent i = getIntent();
		
		try{
			
			String description = i.getStringExtra("SelectedName");
			String name = i.getStringExtra("Parent");
			
			Log.d("e","name is" + name);
			String json = DropboxManager.readJsonFile(name,"album.json");
			Log.d("e","json string is" + json);
			album = new Album(json);
			
			//Log.d("e","album is" + Float.toString(album.latitude));
			
			
			
			
			TextView text = (TextView)findViewById(R.id.topTitle)  ;
			text.setText(name);
			GridView gridView = (GridView) findViewById(R.id.gridview);
	        
	       
	        ArrayList<Bitmap> listOfBitMaps = new ArrayList<Bitmap>();
	        for(String img: album.imgNames) {
	        	listOfBitMaps.add(DropboxManager.readImageFile(name, img));
	        	Log.d("Bitmap", "bmp " + img);
	        }
	        gridView.setAdapter(new ImageAdapter(this, (Bitmap[])listOfBitMaps.toArray()));
	        
	        
			
		} catch (Exception e) {
			Log.d("nblah", "exceptiona" + e.toString());
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gallery, menu);
		return true;
	}
	
	public void loadPictureMenu(View v)
	{
		final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(Gallery.this);
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
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
	}
	
	public void loadGallery(){
		Intent galleryIntent = new Intent(Intent.ACTION_PICK);
		galleryIntent.setType("image/*");
		startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
		
		Log.i("info", "gallery loaded");
	}

}
