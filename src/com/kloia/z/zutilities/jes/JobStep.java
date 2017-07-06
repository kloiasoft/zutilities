package com.kloia.z.zutilities.jes;

public class JobStep {
	private String stepName;
	private String procedureStep;
	private String rc;
	private String excp;
	private String conn;
	private String tcb;
	private String srb;
	private String clock;
	private String serv;
	private String workLoad;
	private String page;
	private String swap;
	private String vio;
	private String swaps;
	public JobStep() {
		super();
	}
	public JobStep(String stepName,
			String procedureStep,
			String rc,
			String excp,
			String conn,
			String tcb,
			String srb,
			String clock,
			String serv,
			String workLoad,
			String page,
			String swap,
			String vio,
			String swaps) {
		super();
		this.stepName = stepName;
		this.procedureStep = procedureStep;
		this.rc = rc;
		this.excp = excp;
		this.conn = conn;
		this.tcb = tcb;
		this.srb = srb;
		this.clock = clock;
		this.serv = serv;
		this.workLoad = workLoad;
		this.page = page;
		this.swap = swap;
		this.vio = vio;
		this.swaps = swaps;
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
	public String getRc() {
		return rc;
	}
	public void setRc(String rc) {
		this.rc = rc;
	}
	public String getExcp() {
		return excp;
	}
	public void setExcp(String excp) {
		this.excp = excp;
	}
	public String getConn() {
		return conn;
	}
	public void setConn(String conn) {
		this.conn = conn;
	}
	public String getTcb() {
		return tcb;
	}
	public void setTcb(String tcb) {
		this.tcb = tcb;
	}
	public String getSrb() {
		return srb;
	}
	public void setSrb(String srb) {
		this.srb = srb;
	}
	public String getClock() {
		return clock;
	}
	public void setClock(String clock) {
		this.clock = clock;
	}
	public String getServ() {
		return serv;
	}
	public void setServ(String serv) {
		this.serv = serv;
	}
	public String getWorkLoad() {
		return workLoad;
	}
	public void setWorkLoad(String workLoad) {
		this.workLoad = workLoad;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getSwap() {
		return swap;
	}
	public void setSwap(String swap) {
		this.swap = swap;
	}
	public String getVio() {
		return vio;
	}
	public void setVio(String vio) {
		this.vio = vio;
	}
	public String getSwaps() {
		return swaps;
	}
	public void setSwaps(String swaps) {
		this.swaps = swaps;
	}
}
