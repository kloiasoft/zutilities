package com.kloia.z.zutilities.jes;

import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPFile;

public class JesJob extends FTPFile {

	private static final long serialVersionUID = 7738133627066098277L;
	private String jobId;
	private String jobName;
	private String owner;
	private String status;
	private String jobClass;
	private String returnCode;
	private ArrayList<SpoolFile> spoolFiles;

	public JesJob() {
		super();
		this.jobId = "";
		this.jobName = "";
		this.owner = "";
		this.status = "";
		this.jobClass = "";
		this.returnCode = "";
		this.spoolFiles = new ArrayList<SpoolFile>();
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getJobClass() {
		return jobClass;
	}

	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public ArrayList<SpoolFile> getSpoolFiles() {
		return spoolFiles;
	}

	public void setSpoolFiles(ArrayList<SpoolFile> spoolFiles) {
		this.spoolFiles = spoolFiles;
	}
}
