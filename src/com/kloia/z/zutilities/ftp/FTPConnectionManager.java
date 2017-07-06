package com.kloia.z.zutilities.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;


public class FTPConnectionManager {
	private FTPClient ftpClient;
	private String ftpHostName;
	private String ftpUser;
	private String ftpPassword;
	private final static int PATHNAME_ALREADY_EXISTS = 521;

	public FTPConnectionManager(String ftpHostName, String ftpUser, String ftpPassword) {
		this.ftpHostName = ftpHostName;
		this.ftpUser = ftpUser;
		this.ftpPassword = ftpPassword;
	}

	public String getFtpHostName() {
		return ftpHostName;
	}
	public String getFtpUser() {
		return ftpUser;
	}
	public FTPClient getFTPClient() throws FTPConnectionNotAvailableException {
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

	public FTPClient getFTPClientForJESFile() throws FTPConnectionNotAvailableException, FTPCommandFailedException {
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

	public FTPClient getFTPClienForSEQFile() throws FTPConnectionNotAvailableException, FTPCommandFailedException {
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
	
	public void changeToRootDirectory() throws FTPConnectionNotAvailableException, FTPCommandFailedException {
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
	
	public void storeSEQFile(String localFilePath, String remoteFilePath, boolean makeDirectory) throws FTPConnectionNotAvailableException, FTPCommandFailedException {
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
	
	public void storeSEQFile(String localFilePath, String remoteFilePath) throws FTPConnectionNotAvailableException, FTPCommandFailedException {
		storeSEQFile(localFilePath, remoteFilePath, true);
	}
	
	public void retrieveSEQFile(String remoteFilePath, String localFilePath) throws FTPConnectionNotAvailableException, FTPCommandFailedException {
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
		
	
	public void deleteSEQFile(String remoteFilePath) throws FTPConnectionNotAvailableException, FTPCommandFailedException {
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
	
	public boolean closeFTPClient() {	
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
	
	private boolean isAvailable() {	
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
