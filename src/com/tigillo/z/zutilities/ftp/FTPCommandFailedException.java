package com.tigillo.z.zutilities.ftp;

public class FTPCommandFailedException extends Exception {
	private static final long serialVersionUID = 4057174989203583081L;

	public FTPCommandFailedException(Throwable cause)
	{
		super("FTP command failed.", cause);
	}
}
