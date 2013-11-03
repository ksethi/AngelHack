package com.Memories;

import java.io.IOException;

import android.content.Context;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

public class DropboxManager {
    private static final String appKey = "x55675yqdk6g8uu";
    private static final String appSecret = "04r3cuuk37q9ido";
	
	private static DbxAccountManager mDbxAcctMgr;
	
	public DropboxManager(Context appContext) {
		mDbxAcctMgr = DbxAccountManager.getInstance(appContext, appKey, appSecret);
	}
	
	public static DbxAccountManager GetManager() {
		return mDbxAcctMgr;
	}
	
	 public void writeJsonFile(String fileContents, String folderName, String fileName)
	 {
		 DbxPath path = new DbxPath(DbxPath.ROOT,  "/" + folderName + "/" + fileName);
		 try {
			DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
		 	if (!dbxFs.exists(path)) {
               DbxFile file = dbxFs.create(path);
               try {
                   file.writeString(fileContents);
               } finally {
                   file.close();
               }
           }
		 } catch (IOException e) {
			 
		 }
	 }
	 
	 public String readJsonFile(String folderName, String fileName)
	 {
		 String retVal = "";
		 DbxPath path = new DbxPath(DbxPath.ROOT, "/" + folderName + "/" + fileName);
		 try {
			DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
			if (dbxFs.isFile(path)) {
               DbxFile file = dbxFs.open(path);
               try {
                   retVal = file.readString();
               } finally {
                   file.close();
               }
           }
			return retVal;
		 } catch (IOException e) {
			return retVal;
		 }
	 }
}
