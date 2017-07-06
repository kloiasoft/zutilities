# ZUtilities
This is the utility to connect to Z/OS over FTP JESInterface and to submit JCL jobs and to retvieve spool files.

## Initialize FTP Connection Manager
```
   FTPConnectionManager ftpConnectionManager = new FTPConnectionManager(ftpHostName, ftpUser, ftpPassword);
```

## Submit A Job
```
  public void submitjob(FTPConnectionManager ftpConnectionManager, File jclFile) throws JobSubmitException, IOException {
    Job job = new Job(jclFile);
    job.submit(ftpConnectionManager);
    out.println("Job submitted with job id: '" +job.getJobId() + "'.");
  }
```

## Submit A Job With Dynamic Content Creation
```
  public void submitjob(FTPConnectionManager ftpConnectionManager, String jclContent) throws JobSubmitException, IOException {
    File jclFile = File.createTempFile("tmpJcl", null);
    OutputStream outputStream = new FileOutputStream(jclFile);
    outputStream.write(jclContent.getBytes());
    Job job = new Job(jclFile);
    job.submit(ftpConnectionManager);
    outputStream.flush();
    outputStream.close();
    outputStream = null;
    out.println("Job submitted with job id: '" +job.getJobId() + "'.");
  }
```

## Get Job Result
```
  public void getJobResult(FTPConnectionManager ftpConnectionManager, Job job) throws SpoolFileRetrieveException, JobStateRefreshException, JobStepRetrieveFailedException {
    job.refreshJobState(ftpConnectionManager);
    if(job.getStatus().equals(Job.Status.INPUT)) {
      System.out.println("Job is waiting in the queue...");
    }
    else if(job.getStatus().equals(Job.Status.ACTIVE)) {
      System.out.println("Job is running...");
    }
    else if(job.getStatus().equals(Job.Status.OUTPUT)) {

      System.out.println("Job '" + job.getJobName() + "." + job.getJobId() +"' completed with rc: '" + job.getReturnCode() + "'.");

      ArrayList<SpoolFile> spoolFiles = job.getSpoolFiles();
      
      for (int i = 0; i < spoolFiles.size(); i++) {
        System.out.println("Retrieving job spool file '" + spoolFiles.get(i).getDataDefinitionName() + "'...");
        job.retrieveSpoolFile(ftpConnectionManager, spoolFiles.get(i), "/tmp/", "filename");
      }
      
      System.out.println("Analysing jes message log...");
      job.analyseJesMessageLog();
      
      for(SpoolFile spoolFile:job.getSpoolFiles()) {
        int logType = CompileLog.COMPILE_LOG;
        for(JobStep jobStep:job.getSteps()) {
          if(jobStep.getProcedureStep().equals(spoolFile.getProcedureStep()) && !jobStep.getRc().equals("00") && !jobStep.getRc().equals("01") && !jobStep.getRc().equals("02") && !jobStep.getRc().equals("03") && !jobStep.getRc().equals("04")) {
            System.out.println("Step failed: '"+ jobStep.getStepName() + "'");
            System.out.println("'" + spoolFile.getDataDefinitionName() + "' spool file: '" + spoolFile.getLocalFile().getPath() + "'");
          }
        }
      }

      if(job.getReturnCode().equals("0000")){
        System.out.println("Job completed with success.");
      }
      else if(job.getReturnCode().equals("0001") || job.getReturnCode().equals("0002") || job.getReturnCode().equals("0003") || job.getReturnCode().equals("0004")){
        System.out.println("Job completed with warnings.");
      }
      else {
        System.out.println("Job failed.");
      }
    }
  }
```