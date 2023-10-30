package com.alert;

public class AlertDTO {
	private long alertnum;
	private String title;
	private String content;
	private String reg_date;
	private String hitcount;
	
	private String[] saveFiles;
	
	
	public long getAlertnum() {
		return alertnum;
	}
	public void setAlertnum(long alertnum) {
		this.alertnum = alertnum;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getHitcount() {
		return hitcount;
	}
	public void setHitcount(String hitcount) {
		this.hitcount = hitcount;
	}
	public String[] getSaveFiles() {
		return saveFiles;
	}
	public void setSaveFiles(String[] saveFiles) {
		this.saveFiles = saveFiles;
	}
	public String[] getOriginalFiles() {
		return null;
	}
	
	
}
