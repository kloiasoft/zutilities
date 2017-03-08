package com.tigillo.z.zutilities.jes;

public class JobStateRefreshException extends Exception{

	private static final long serialVersionUID = 3877180040805364138L;

	public JobStateRefreshException(String jobId, Throwable cause)
	{
		super("Refresh job state failed for job " + jobId + ".", cause);
	}
}
