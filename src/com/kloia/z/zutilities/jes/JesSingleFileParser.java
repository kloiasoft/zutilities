package com.kloia.z.zutilities.jes;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileEntryParser;


public class JesSingleFileParser implements FTPFileEntryParser {

	public FTPFile parseFTPEntry(String arg0) {
		String[] lines = arg0.split("\r\n");
		
		if(lines.length==0) return null;
		
		JesJob jesJob = new JesJob();
		jesJob.setJobName(lines[0].substring(0, 8).trim());
		jesJob.setJobId(lines[0].substring(9, 17).trim());
		jesJob.setOwner(lines[0].substring(18, 26).trim());
		jesJob.setStatus(lines[0].substring(27, 33).trim());
		jesJob.setJobClass(lines[0].substring(34, 42).trim());
		if(jesJob.getStatus().equals(Job.Status.OUTPUT)) jesJob.setReturnCode(lines[0].substring(43).replaceAll("RC=", "").trim());
		
		if(lines.length>1)
		{
			for (int i = 1; i < lines.length; i++) {
				SpoolFile spoolFile = new SpoolFile();
				spoolFile.setId(lines[i].substring(9, 12).trim());
				spoolFile.setStepName(lines[i].substring(13, 21).trim());
				spoolFile.setProcedureStep(lines[i].substring(22, 30).trim());
				spoolFile.setC(lines[i].substring(31, 32).trim());
				spoolFile.setDataDefinitionName(lines[i].substring(33, 41).trim());
				spoolFile.setByteCount(Integer.parseInt(lines[i].substring(42).trim()));
				jesJob.getSpoolFiles().add(spoolFile);
			}
		}
		
		return jesJob;
	}

	public List<String> preParse(List<String> arg0) {
		if(arg0.size() == 2) {
			arg0.remove(0);
		}
		else {
			if(arg0.size()>=1) arg0.remove(arg0.size()-1);
			if(arg0.size()>=4) arg0.remove(3);
			if(arg0.size()>=3) arg0.remove(2);
			if(arg0.size()>=1) arg0.remove(0);
			if(arg0.get(0).length()<=43 && arg0.size()>1) arg0.remove(1);
			
			String singleJesJob = "";

			Iterator<String> it = arg0.iterator();
			while (it.hasNext()) {
				singleJesJob += it.next() + "\r\n";
				it.remove();
			}
			if(!singleJesJob.equals("")) arg0.add(singleJesJob);
		}
		return arg0;
	}

	public String readNextEntry(BufferedReader arg0) throws IOException {
		return arg0.readLine();
	}

}
