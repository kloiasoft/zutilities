package com.tigillo.z.zutilities.jes;

public class JobSubmitException extends Exception{

	private static final long serialVersionUID = 6144326209337204355L;

	public JobSubmitException(Throwable cause)
	{
		super("Submit failed for job.", cause);
	}
}
