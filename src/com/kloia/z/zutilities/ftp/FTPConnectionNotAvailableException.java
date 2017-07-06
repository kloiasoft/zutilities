package com.kloia.z.zutilities.ftp;

public class FTPConnectionNotAvailableException extends Exception {
	private static final long serialVersionUID = 4057174989203583081L;

	public FTPConnectionNotAvailableException(Throwable cause)
	{
		super("FTP Connection is not available. ", cause);
	}
}
