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
		if (dbxFs == null)
		{
		try {
			dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
		} catch (Unauthorized e) {
			Log.d("dbxFS","UNAUTH");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}
	
	private static DbxPath getPath(String folderName, String fileName)
	{
		String separator = "";
		 if (folderName.length() != 0)
		 {
			 separator = "/";
		 }
		 DbxPath path;
		 path = new DbxPath(DbxPath.ROOT, folderName + separator + fileName);
		 return path;
	}
	 public static void writeJsonFile(String fileContents, String folderName, String fileName)
	 {
		 
		 try {
			 DbxPath path = getPath(folderName, fileName);
			 if (!dbxFs.exists(path))
			 {
               DbxFile file = dbxFs.create(path);
               Log.d("Creating a new file with path",path.toString());
               try {
                   file.writeString(fileContents);
               } finally {
                   file.close();
              }
			 }
               else
               {
            	   try
            	   {
            	   DbxFile file = dbxFs.open(path);
            	   file.writeString(fileContents);
            	   } catch (Exception e)
            	   {
            		   Log.d("dbx exists","dbx exists but can't write");
            	   }
            	}
            	   
		 } catch (IOException e) {
			 Log.d("write","writingjsonfailed" + e.toString());
		 }
	 } 
	 
	 public static ArrayList<Bitmap> getImagesForFolder(String folderName)
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
	 
	 public static Bitmap convertStringToBitmap(String image) {
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
	 
	 public static void writeImageFile(Bitmap bitmap, String folderName, String fileName)
	 {
		 String magic = convertBitmapToString(bitmap);
		 writeJsonFile(magic, folderName, fileName);
	 }
	 
	 public static String convertBitmapToString(Bitmap src) 
	 { 
	    ByteArrayOutputStream os=new ByteArrayOutputStream(); 
	    src.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, (OutputStream) os); 
	    byte[] arr = os.toByteArray();
	    return Base64.encodeToString(arr,Base64.DEFAULT); 
	 }
	 
	 public  static Bitmap readImageFile(DbxPath path)
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
	 public static Bitmap readImageFile(String folderName,String fileName)
	 {
		 String retVal = readJsonFile(folderName, fileName);
		 return convertStringToBitmap(retVal);
	 }
	 
	 public static String readJsonFile(String folderName, String fileName)
	 {
		 String retVal = "";
		 
		 Log.d("in dropbox manager", "in the read json file function");

	
		 //Log.d("Read path", path.toString());
			DbxFile file = null;
			try {
				DbxPath path = getPath(folderName, fileName);
				Log.d("MDPath", "Calc path");
				Log.d("MDPath", "PATH IS" + path.toString());
				if (dbxFs == null)
				{
					Log.i("Dbfx", "DBXFS is NULL");
				}
				file = dbxFs.open(path);
		       retVal = file.readString();
		       Log.i("in the readjson file","in the file" +  retVal);
		       file.close();
		   } catch( Exception e) {
			   Log.i("went into the catch","the readString failed" + e.toString());
			   return "FAIL";
		   }
	
		return retVal;
	 }
	 
}
