package com.Memories;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;

public class Gallery extends Activity {

	public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
	private static final int RESULT_LOAD_IMAGE = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		
		
		GridView gridView = (GridView) findViewById(R.id.gridview);
		 
        // Instance of ImageAdapter Class
        gridView.setAdapter(new ImageAdapter(this));
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
