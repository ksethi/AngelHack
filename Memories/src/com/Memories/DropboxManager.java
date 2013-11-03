package com.Memories;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

public class DropboxManager {
    private static final String appKey = "x55675yqdk6g8uu";
    private static final String appSecret = "04r3cuuk37q9ido";
	
	private static DbxAccountManager mDbxAcctMgr;
	
	private static DbxFileSystem dbxFs ; 
	
	public DropboxManager(Context appContext) {
		mDbxAcctMgr = DbxAccountManager.getInstance(appContext, appKey, appSecret);

	}
	
	public static DbxAccountManager GetManager() {
		return mDbxAcctMgr;
	}
	
	public static DbxFileSystem getdbfxFileSystem() {
		return dbxFs;
	}
	
	public static void setDbxFileSystem() {
		try {
			dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
		} catch (Unauthorized e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 public void writeJsonFile(String fileContents, String folderName, String fileName)
	 {
		 DbxPath path = new DbxPath(DbxPath.ROOT, folderName + "/" + fileName);
		 try {
		 	if (!dbxFs.exists(path)) {
               DbxFile file = dbxFs.create(path);

               Log.d("Creating a new file with path",path.toString());
               try {
                   file.writeString(fileContents);
               } finally {
                   file.close();
               }    
           }
		 } catch (IOException e) {
			 
		 }
	 } 
	 
	 public ArrayList<Bitmap> getImagesForFolder(String folderName)
	 {
		 DbxPath path = new DbxPath(DbxPath.ROOT, folderName);
		 ArrayList<Bitmap> retImages = new ArrayList<Bitmap>();
		 try {
			 List<DbxFileInfo> filePaths = dbxFs.listFolder(path);
			 
			 for(DbxFileInfo info : filePaths)
			 {
				 retImages.add(readImageFile(info.path));
			 }
			 return retImages;
			 
		 } catch (IOException e) {
			 return retImages;
		 }
		 
	 }
	 
	 public Bitmap convertStringToBitmap(String image) {
		 try {
			 byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);
			 Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
			 return bitmap;
		 } catch (Exception e) {
			 Log.d("ConvertStringToBitmap","Error while converting from string to bitmap");
			 e.getMessage();
			 return null;
		 }
	 }
	 
	 public void writeImageFile(Bitmap bitmap, String folderName, String fileName)
	 {
		 String magic = convertBitmapToString(bitmap);
		 writeJsonFile(magic, folderName, fileName);
	 }
	 
	 public String convertBitmapToString(Bitmap src) 
	 { 
	    ByteArrayOutputStream os=new ByteArrayOutputStream(); 
	    src.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, (OutputStream) os); 
	    byte[] arr = os.toByteArray();
	    return Base64.encodeToString(arr,Base64.DEFAULT); 
	 }
	 
	 public Bitmap readImageFile(DbxPath path)
	 {
		 String retVal = "";
		 try {
               DbxFile file = dbxFs.open(path);
               try {
                   retVal = file.readString();
               } finally {
                   file.close();
               }
			return convertStringToBitmap(retVal);
		 } catch (IOException e) {
			return null;
		 }
	 }
	 public Bitmap readImageFile(String folderName,String fileName)
	 {
		 String retVal = readJsonFile(folderName, fileName);
		 return convertStringToBitmap(retVal);
	 }
	 
	 public String readJsonFile(String folderName, String fileName)
	 {
		 String retVal = "";
		 
		 Log.d("in dropbox manager", "in the read json file function");
		 DbxPath path = new DbxPath(DbxPath.ROOT, folderName + "/" + fileName);
	
		 Log.d("Read path", path.toString());
				DbxFile file = null;
			try {
				file = dbxFs.open(path);
		       retVal = file.readString();
		       Log.i("in the readjson file", retVal);
		   } catch( Exception e) {
			   Log.i("went into the catch","the readString failed");
		   }finally {
			   Log.i("in the readjson file", retVal);
			   file.close();
		       
		   }
	
		return retVal;
	 }
	 
}
