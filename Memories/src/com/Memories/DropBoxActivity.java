package com.Memories;

import java.io.IOException;
import java.util.List;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class DropBoxActivity extends Activity {
    
    private static final int REQUEST_LINK_TO_DBX = 0;
	
	private TextView mTestOutput;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_drop_box);
		DbxAccountManager mDbxAcctMgr = DropboxManager.GetManager();
        
        mTestOutput = (TextView) findViewById(R.id.test_output);
		if (mDbxAcctMgr.hasLinkedAccount()) {
			Log.d("DropBoxActivity","Has linked Dropbox :)");
			DropboxManager.setDbxFileSystem();
			finish();
		} else {
			Log.d("DropBoxActivity","Does not have a linked dropbox ");
			mDbxAcctMgr.startLink((Activity)this, REQUEST_LINK_TO_DBX);		
		}
	}
	
	 @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (requestCode == REQUEST_LINK_TO_DBX) {
	            if (resultCode == Activity.RESULT_OK) {
	            	Log.d("DropBoxActivity","Result was good");         	
	            	DropboxManager.setDbxFileSystem();
	            	finish();
	            } else {
	            	Log.d("DropBoxActivity","Link to Dropbox failed or was cancelled.");
	            }
	        } else {
	            super.onActivityResult(requestCode, resultCode, data);
	        }
	    }
	 @Override
		protected void onResume() {
			super.onResume();
		}
}
