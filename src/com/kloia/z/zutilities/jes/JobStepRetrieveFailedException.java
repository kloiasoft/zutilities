package com.kloia.z.zutilities.jes;

public class JobStepRetrieveFailedException extends Exception {

	private static final long serialVersionUID = 5301311689578332808L;

	public JobStepRetrieveFailedException(String jobId, Throwable cause) {
		super("Retrieve job step failed for job '" + jobId + "'.", cause);
	}

}
