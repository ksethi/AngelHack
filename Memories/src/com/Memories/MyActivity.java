package com.Memories;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import com.dropbox.sync.android.DbxAccountManager;

import android.app.Activity;
//import android.R;
import android.app.ListActivity;
import android.content.*;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;


public class MyActivity extends Activity {
	
	private Button btnCamera;
	private Button btnWriteFile;
	private DropboxManager dbManager;
	@Override
	 public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.activity_fullscreen);
		  btnCamera = (Button)findViewById(R.id.btnCamera);
		  
		  
		  btnWriteFile =  (Button)findViewById(R.id.btnWriteFile);
		  
		  
		  dbManager = new DropboxManager(getApplicationContext());
		  Log.d("onCreate", "Starting up the activity");
		  try {
		      Intent k = new Intent(MyActivity.this, DropBoxActivity.class);
		       startActivity(k);
		  } catch(Exception e) {

		  }
	 }
	
	public void buttonWriteFile(View view) {
		dbManager.writeJsonFile("This is a test file from KJ", "FirstEvent" , "testFile.json");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	public void loadCamera(View view) {
		Log.d("HELLO", "WORLD");
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	}
}
