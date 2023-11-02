package com.pbbs;

public class PbbsDTO {
	private long num,cost,plike,catNum,hitCount,pstate;
	private String userId;
	private String userPwd,userName;
	private String subject;
	private String content;
	private String regdate;
	private String catString;
	private String chkReply;
	
	public String getChkReply() {
		return chkReply;
	}
	public void setChkReply(String chkReply) {
		this.chkReply = chkReply;
	}
	private long fileNum;
	private String[] imageFiles;
	private String imageFilename;


	public int getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}
	private int replyCount;
	
	public long getFileNum() {
		return fileNum;
	}
	public void setFileNum(long fileNum) {
		this.fileNum = fileNum;
	}
	public String[] getImageFiles() {
		return imageFiles;
	}
	public void setImageFiles(String[] imageFiles) {
		this.imageFiles = imageFiles;
	}
	public void setCatString(String catString) {
		this.catString = catString;
	}
	public String getCatString() {
		return catString;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	public long getCost() {
		return cost;
	}
	public void setCost(long cost) {
		this.cost = cost;
	}
	public long getPlike() {
		return plike;
	}
	public void setPlike(long plike) {
		this.plike = plike;
	}
	public long getCatNum() {
		return catNum;
	}
	public void setCatNum(long catNum) {
		this.catNum = catNum;
	}
	public long getHitCount() {
		return hitCount;
	}
	public void setHitCount(long hitCount) {
		this.hitCount = hitCount;
	}
	public long getPstate() {
		return pstate;
	}
	public void setPstate(long pstate) {
		this.pstate = pstate;
	}
	public long getNum() {
		return num;
	}
	public void setNum(long num) {
		this.num = num;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userPwd) {
		this.userName = userPwd;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImageFilename() {
		return imageFilename;
	}
	public void setImageFilename(String imageFilename) {
		this.imageFilename = imageFilename;
	}

}
