package com.tigillo.z.zutilities.jes;

public class JobNotExistsException extends Exception{

	private static final long serialVersionUID = 3097869422294897856L;

	public JobNotExistsException(String jobId)
	{
		super("Job "+jobId+" does not exist.");
	}
}
