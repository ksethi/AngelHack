package com.Memories;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
//import android.R;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MyActivity extends Activity {
	
	private Button btnCamera;
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.activity_fullscreen);
		  btnCamera = (Button)findViewById(R.id.btnCamera);
	 }
	
	public void loadCamera(View view) {
		System.out.println("HELLO");
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	}
}
