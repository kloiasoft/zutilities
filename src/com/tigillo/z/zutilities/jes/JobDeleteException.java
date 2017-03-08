package com.tigillo.z.zutilities.jes;

public class JobDeleteException extends Exception {

	private static final long serialVersionUID = 5680100123839029782L;
	
	public JobDeleteException(String jobId, Throwable cause)
	{
		super("Delete failed for job " + jobId + ".", cause);
	}
}
