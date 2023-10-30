package com.alert;

public class AlertDTO {
	private long alertnum;
	private String title;
	private String content;
	private String reg_date;
	private String hitcount;
	
	private long fileNum;
	private String saveFilename;
	private String originalFilename;
		
	private String[] saveFiles;
	private String[] originalFiles;
	private long gap;
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
	public long getFileNum() {
		return fileNum;
	}
	public void setFileNum(long fileNum) {
		this.fileNum = fileNum;
	}
	public String getSaveFilename() {
		return saveFilename;
	}
	public void setSaveFilename(String saveFilename) {
		this.saveFilename = saveFilename;
	}
	public String getOriginalFilename() {
		return originalFilename;
	}
	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}
	public String[] getSaveFiles() {
		return saveFiles;
	}
	public void setSaveFiles(String[] saveFiles) {
		this.saveFiles = saveFiles;
	}
	public String[] getOriginalFiles() {
		return originalFiles;
	}
	public void setOriginalFiles(String[] originalFiles) {
		this.originalFiles = originalFiles;
	}
	public long getGap() {
		return gap;
	}
	public void setGap(long gap) {
		this.gap = gap;
	}
	public void setUserId(String userId) {
		// TODO Auto-generated method stub
		
	}
	public void setAlert(int parseInt) {
		// TODO Auto-generated method stub
		
	}
	public void OriginalFiles(String[] originalFiles2) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}