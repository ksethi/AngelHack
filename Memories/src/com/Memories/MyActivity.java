package com.Memories;

import java.io.Console;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
//import android.R;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MyActivity extends Activity {
	
	private static final int CAMERA_PIC_REQUEST = 1337;
	private static final int RESULT_LOAD_IMAGE = 1;
	private Button btnSelectPhoto;
	ImageView viewImage;
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.activity_fullscreen);
		  btnSelectPhoto = (Button)findViewById(R.id.btnSelectPhoto);
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
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
 
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
		
	public void loadCamera(View view) {
		Log.i("info", "Hello");
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
	}
	
	public void loadGallery(View view){
		Intent galleryIntent = new Intent(Intent.ACTION_PICK);
		galleryIntent.setType("image/*");
		galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
		
		Log.i("info", "gallery loaded");
	}
	
	public void onActivtyResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == CAMERA_PIC_REQUEST )
		{
			Log.i("info", "photo taken");
//			private void galleryAddPic() {
//			    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//			    File f = new File(mCurrentPhotoPath);
//			    Uri contentUri = Uri.fromFile(f);
//			    mediaScanIntent.setData(contentUri);
//			    this.sendBroadcast(mediaScanIntent);
//			}
			//for saving to Gallery 
			
			Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
			
			ImageView image = (ImageView) findViewById(R.id.imageView1);  
			image.setImageBitmap(thumbnail);  
		}
	}
}
