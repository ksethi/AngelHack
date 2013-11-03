package com.Memories;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

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
	private static final String m_clientId = "os2uo6nui67ax39uay2h1j9eapipdgho";
	private static final String m_clientSecret = "09uu3ws9LtFEbHvSK7NCdDcIvbni5BgG";
	private static final String m_redirectUrl = "https://www.linkedin.com/pub/kshitij-sethi/19/846/447";
	@Override
	 public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.activity_fullscreen);
		  btnCamera = (Button)findViewById(R.id.btnCamera);
		  Log.d("onCreate", "Starting up the activity");
		  testBoxApi();
	 }
	
	private void testBoxApi() {
		new BoxManager().execute("https://app.box.com/api/oauth2/authorize?response_type=code&client_id=" + m_clientId + "&redirect_uri=" + m_redirectUrl);
	}
	
	public void loadCamera(View view) {
		Log.d("HELLO", "WORLD");
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	}
}
