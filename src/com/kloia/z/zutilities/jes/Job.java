package com.kloia.z.zutilities.jes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.kloia.z.zutilities.ftp.FTPConnectionManager;

public class Job {
	private File jclFile;
	private String jobId;
	private String jobName;
	private String owner;
	private String status;
	private String jobClass;
	private String returnCode;
	private ArrayList<SpoolFile> spoolFiles;
	private ArrayList<JobStep> steps;

	public static class Status {
		public static String INPUT = "INPUT";
		public static String ACTIVE = "ACTIVE";
		public static String OUTPUT = "OUTPUT";
	}

	public Job() {
		super();
		this.jobName = "";
		this.owner = "";
		this.status = "";
		this.jobClass = "";
		this.returnCode = "";
		this.spoolFiles = new ArrayList<SpoolFile>();
	}

	public Job(File jclFile) {
		super();
		this.jclFile = jclFile;
		this.jobName = "";
		this.owner = "";
		this.status = "";
		this.jobClass = "";
		this.returnCode = "";
		this.spoolFiles = new ArrayList<SpoolFile>();
		this.steps = new ArrayList<JobStep>();
	}

	public Job(String jclFilePath) {
		super();
		this.jclFile = new File(jclFilePath);
		this.jobName = "";
		this.owner = "";
		this.status = "";
		this.jobClass = "";
		this.returnCode = "";
		this.spoolFiles = new ArrayList<SpoolFile>();
		this.steps = new ArrayList<JobStep>();
	}

	public File getJclFile() {
		return jclFile;
	}

	public void setJclFile(File jclFile) {
		this.jclFile = jclFile;
	}

	public void setJclFile(String jclFilePath) {
		this.jclFile = new File(jclFilePath);
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

	public ArrayList<JobStep> getSteps() {
		return steps;
	}

	public void setSteps(ArrayList<JobStep> steps) {
		this.steps = steps;
	}

	public void submit(FTPConnectionManager ftpConnectionManager) throws JobSubmitException {
		
		try {
			FTPClient ftpClient = ftpConnectionManager.getFTPClientForJESFile();
	        FileInputStream inputStream = new FileInputStream(jclFile.getAbsolutePath());
	        ftpClient.storeFile(ftpClient.getRemoteAddress().getHostName(),inputStream);	        
	        if(ftpClient.getReplyCode() == FTPReply.FILE_ACTION_OK) {
	        	jobId=ftpClient.getReplyString().substring(26, 34);
	        }
	        else throw new IOException(ftpClient.getReplyString());
		}
        catch (Exception e) {
			throw new JobSubmitException(e);
        }
	}

	public void refreshJobState(FTPConnectionManager ftpConnectionManager) throws JobStateRefreshException
	{
		try {
			FTPClient ftpClient = ftpConnectionManager.getFTPClientForJESFile();
			ftpClient.site("jesowner=*");
			ftpClient.site("jesjobname=*");
			FTPClientConfig conf = new FTPClientConfig("JesSingleFileParser");
			ftpClient.configure(conf);
			FTPFile[] files = ftpClient.listFiles(jobId);
	        if(files.length==0) throw new JobNotExistsException(jobId);
	        for(FTPFile file:files)
	        {
	        	JesJob jesJob = (JesJob) file;
	    		jobName = jesJob.getJobName();
	    		owner = jesJob.getOwner();
	    		status = jesJob.getStatus();
	    		jobClass = jesJob.getJobClass();
	    		returnCode = jesJob.getReturnCode();
	    		spoolFiles = jesJob.getSpoolFiles();
	        }
		}
        catch (Exception e) {
        	throw new JobStateRefreshException(jobId, e);
        }
	}

	public void retrieveSpoolFile(FTPConnectionManager ftpConnectionManager, SpoolFile spoolFile, String outputPath, String prefix) throws SpoolFileRetrieveException {
		try {
			FTPClient ftpClient = ftpConnectionManager.getFTPClientForJESFile();
			ftpClient.site("jesowner=*");
			File outputFile = new File(outputPath);;
			if(prefix==null) prefix = jobName;
			if(!outputFile.exists()) outputFile.mkdirs();
			if(!outputFile.exists()) throw new IOException("Make directory for output path '" + outputPath + "' failed.");
			if(!outputFile.isDirectory()) outputFile = outputFile.getParentFile();
			String procStep = spoolFile.getProcedureStep() + ".";
			if(procStep.equals("."))
				procStep = "";
		    outputFile = new File(outputFile.getAbsolutePath() + File.separator + prefix + "." + procStep + spoolFile.getDataDefinitionName());
		    outputFile.createNewFile();
		    OutputStream outStream = new FileOutputStream(outputFile);
			ftpClient.retrieveFile(jobId+"."+spoolFile.getId(), outStream);
			if(ftpClient.getReplyCode()!=FTPReply.FILE_ACTION_OK)
				throw new IOException(ftpClient.getReplyString());
	        spoolFile.setLocalFile(outputFile);
		}
        catch (Exception e) {
        	throw new SpoolFileRetrieveException(jobId, spoolFile, e.toString());
        }
	}
	
	public void analyseJesMessageLog() throws JobStepRetrieveFailedException {
		try {
			steps = new ArrayList<JobStep>();
			SpoolFile jesMessageLog = null;
			for(SpoolFile spoolFile:spoolFiles) {
				if(spoolFile.getDataDefinitionName().equalsIgnoreCase("JESMSGLG"))
				{
					jesMessageLog = spoolFile;
				}
			}
			if(jesMessageLog==null)
				throw new JesMessageLogNotFoundException();
			
			if(jesMessageLog.getLocalFile()==null)
				retrieveSpoolFile(jesMessageLog,System.getProperty("java.io.tmpdir"),null);
			
			BufferedReader localBufferedReader = new BufferedReader(new FileReader(jesMessageLog.getLocalFile()));
			
		    String line;
		    boolean started = false;
		    boolean completed = false;
		    while ((line = localBufferedReader.readLine()) != null && !completed) {
		    	if(!started && line.contains("-STEPNAME PROCSTEP    RC   EXCP   CONN       TCB       SRB  CLOCK          SERV  WORKLOAD  PAGE  SWAP   VIO SWAPS"))
		    		started = true;
		    	else if(line.contains("ENDED.") && line.contains("TOTAL ELAPSED TIME="))
		    		completed = true;
		    	else if(started && line.substring(20, 21).equals("-")) {
		    		JobStep step = new JobStep();
		    		step.setStepName(line.substring(21, 29).trim());
		    		step.setProcedureStep(line.substring(30, 38).trim());
		    		step.setRc(line.substring(39, 44).trim());
		    		step.setExcp(line.substring(45, 51).trim());
		    		step.setConn(line.substring(52, 58).trim());
		    		step.setTcb(line.substring(59, 68).trim());
		    		step.setSrb(line.substring(69, 78).trim());
		    		step.setClock(line.substring(79, 85).trim());
		    		step.setServ(line.substring(86, 99).trim());
		    		step.setWorkLoad(line.substring(101, 109).trim());
		    		step.setPage(line.substring(110, 115).trim());
		    		step.setSwap(line.substring(116, 121).trim());
		    		step.setVio(line.substring(122, 127).trim());
		    		step.setWorkLoad(line.substring(127).trim());
		    		steps.add(step);
				}
		    }
		    
		    localBufferedReader.close();
		    
		} catch (Exception e) {
			throw new JobStepRetrieveFailedException(jobId, e);
		}
	}
	public void delete(FTPConnectionManager ftpConnectionManager) throws JobDeleteException
	{
		try {
			FTPClient ftpClient = ftpConnectionManager.getFTPClientForJESFile();
	        ftpClient.deleteFile(jobId);
	        if(ftpClient.getReplyCode() != FTPReply.FILE_ACTION_OK)
		        throw new IOException(ftpClient.getReplyString());
		}
        catch (Exception e) {
        	throw new JobDeleteException(jobId, e);
        }
	}
}
