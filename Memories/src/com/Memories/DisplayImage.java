package com.Memories;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayImage extends Activity{
	
	ImageView viewImage;
	TextView txtView;
	EditText caption;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_image);
        
        viewImage = (ImageView)findViewById(R.id.viewImage);
        txtView = (TextView)findViewById(R.id.imageDesc);
        caption = (EditText)findViewById(R.id.descInput);
    }

}
