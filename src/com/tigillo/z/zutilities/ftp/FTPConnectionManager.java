package com.tigillo.z.zutilities.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;


public class FTPConnectionManager {
	private static FTPClient ftpClient;
	private static String ftpHostName;
	private static String ftpUser;
	private static String ftpPassword;
	private final static int PATHNAME_ALREADY_EXISTS = 521;
	
	public static FTPClient initializeFTPConnectionManager(String ftpHostName, String ftpUser, String ftpPassword) throws FTPConnectionNotAvailableException {
		FTPConnectionManager.ftpHostName = ftpHostName;
		FTPConnectionManager.ftpUser = ftpUser;
		FTPConnectionManager.ftpPassword = ftpPassword;
		return getFTPClient();
	}
	public static String getFtpHostName() {
		return ftpHostName;
	}
	public static String getFtpUser() {
		return ftpUser;
	}
	public static FTPClient getFTPClient() throws FTPConnectionNotAvailableException {
		try
		{
			if(ftpClient==null) ftpClient = new FTPClient();
			if(!isAvailable()) {
				ftpClient.connect(ftpHostName);
				ftpClient.login(ftpUser, ftpPassword);
				if(ftpClient.getReplyCode() != 230) throw new FTPLoginFailedException(ftpUser, new IOException(ftpClient.getReplyString()));
			}
		}
		catch (Exception e) {
			throw new FTPConnectionNotAvailableException(e);
		}
		
		return ftpClient;
	}

	public static FTPClient getFTPClientForJESFile() throws FTPConnectionNotAvailableException, FTPCommandFailedException {
		getFTPClient();
		try
		{
			ftpClient.site("filetype=jes");
			ftpClient.configure(null);
		}
		catch (Exception e) {
			throw new FTPCommandFailedException(e);
		}
		
		return ftpClient;
	}

	public static FTPClient getFTPClienForSEQFile() throws FTPConnectionNotAvailableException, FTPCommandFailedException {
		getFTPClient();
		try
		{
			ftpClient.site("filetype=seq");
			ftpClient.configure(null);
		}
		catch (Exception e) {
			throw new FTPCommandFailedException(e);
		}
		
		return ftpClient;
	}
	
	public static void changeToRootDirectory() throws FTPConnectionNotAvailableException, FTPCommandFailedException {
		getFTPClienForSEQFile();
		try
		{
			ftpClient.changeToParentDirectory();
	        while(ftpClient.getReplyCode() == FTPReply.FILE_ACTION_OK) {
	        	ftpClient.changeToParentDirectory();
	        }
	        if(ftpClient.getReplyCode() != FTPReply.SYNTAX_ERROR_IN_ARGUMENTS)
        		throw new IOException(ftpClient.getReplyString());
		}
		catch (Exception e) {
			throw new FTPCommandFailedException(e);
		}
	}
	
	public static void storeSEQFile(String localFilePath, String remoteFilePath, boolean makeDirectory) throws FTPConnectionNotAvailableException, FTPCommandFailedException {
		changeToRootDirectory();
		try
		{
			if(makeDirectory) {
	        ftpClient.makeDirectory(remoteFilePath.split("\\(")[0]);
		        if(ftpClient.getReplyCode() != FTPReply.PATHNAME_CREATED && ftpClient.getReplyCode() != PATHNAME_ALREADY_EXISTS)
	        		throw new IOException(ftpClient.getReplyString());
			}
			FileInputStream inputStream = new FileInputStream(localFilePath);
			ftpClient.storeFile(remoteFilePath, inputStream);
	        if(ftpClient.getReplyCode() != FTPReply.FILE_ACTION_OK)
        		throw new IOException(ftpClient.getReplyString());
		}
		catch (Exception e) {
			throw new FTPCommandFailedException(e);
		}
	}
	
	public static void storeSEQFile(String localFilePath, String remoteFilePath) throws FTPConnectionNotAvailableException, FTPCommandFailedException {
		storeSEQFile(localFilePath, remoteFilePath, true);
	}
	
	public static void retrieveSEQFile(String remoteFilePath, String localFilePath) throws FTPConnectionNotAvailableException, FTPCommandFailedException {
		changeToRootDirectory();
		try
		{
			File localFile = new File(localFilePath);
			localFile.getParentFile().mkdirs();
			if(localFile.exists())
				localFile.delete();
			localFile.createNewFile();
			OutputStream outStream = new FileOutputStream(localFilePath, true);
			ftpClient.retrieveFile(remoteFilePath, outStream);
			outStream.close();
	        if(ftpClient.getReplyCode() != FTPReply.FILE_ACTION_OK)
        		throw new IOException(ftpClient.getReplyString());
		}
		catch (Exception e) {
			throw new FTPCommandFailedException(e);
		}
	}
		
	
	public static void deleteSEQFile(String remoteFilePath) throws FTPConnectionNotAvailableException, FTPCommandFailedException {
		changeToRootDirectory();
		try
		{
	        ftpClient.deleteFile(remoteFilePath);
	        if(ftpClient.getReplyCode() != FTPReply.FILE_ACTION_OK)
        		throw new IOException(ftpClient.getReplyString());
		}
		catch (Exception e) {
			throw new FTPCommandFailedException(e);
		}
	}
	
	public static boolean closeFTPClient() {	
		try
		{
			ftpClient.disconnect();
			ftpClient = null;
			return true;
		}
		catch (Exception e) {
			ftpClient = null;
			return false;
		}
	}
	
	private static boolean isAvailable() {	
		try
		{
			ftpClient.listNames();			
			if(ftpClient.getReplyCode() == FTPReply.FILE_ACTION_OK)
				return true;
			return false;
		}
		catch (Exception e) {
			return false;
		}
	}
}
