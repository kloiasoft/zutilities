package com.kloia.z.zutilities.jes;

import java.io.File;

public class SpoolFile {
	private String id;
	private String stepName;
	private String procedureStep;
	private String c;
	private String dataDefinitionName;
	private int byteCount;
	private String dataSetName;
	private File localFile;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public String getProcedureStep() {
		return procedureStep;
	}
	public void setProcedureStep(String procedureStep) {
		this.procedureStep = procedureStep;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	public String getDataDefinitionName() {
		return dataDefinitionName;
	}
	public void setDataDefinitionName(String dataDefinitionName) {
		this.dataDefinitionName = dataDefinitionName;
	}
	public int getByteCount() {
		return byteCount;
	}
	public void setByteCount(int byteCount) {
		this.byteCount = byteCount;
	}
	public String getDataSetName() {
		return dataSetName;
	}
	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}
	public File getLocalFile() {
		return localFile;
	}
	public void setLocalFile(File localFile) {
		this.localFile = localFile;
	}
}
