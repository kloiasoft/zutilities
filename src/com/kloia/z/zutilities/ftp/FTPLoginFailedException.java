package com.kloia.z.zutilities.ftp;

public class FTPLoginFailedException extends Exception {

	private static final long serialVersionUID = -6721163039004224499L;

	public FTPLoginFailedException(String ftpUser, Throwable cause) {
		super("Login failed for user " + ftpUser + ".", cause);
	}

}
