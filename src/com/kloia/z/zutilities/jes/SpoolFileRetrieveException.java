package com.kloia.z.zutilities.jes;

public class SpoolFileRetrieveException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2968591153223311284L;

	public SpoolFileRetrieveException(String jobId, SpoolFile spoolFile, String message) {
		super("Retrieve failed for spool file " + spoolFile.getDataDefinitionName() + ": " + jobId + "." + spoolFile.getId() + ". " + message);
	}

}
