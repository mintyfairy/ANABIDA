package com.csbbs;

public class CSBoardDTO {
	private long qnum;
	private String userId;
	private String userName;
	private String title;
	private String content;
	private String reg_date;
	private int hitCount;
	private long groupNum;
	private int orderNo;
	private int depth;
	private long parent;
	private String answerUserId;
	private String answerUserId2;
	private String filename;
	private long qanum;
	private int answeryes;
	private String[] imgfiles;
	
	public String[] getImgfiles() {
		return imgfiles;
	}
	public void setImgfiles(String[] imgfiles) {
		this.imgfiles = imgfiles;
	}
	public int getAnsweryes() {
		return answeryes;
	}
	public void setAnsweryes(int answeryes) {
		this.answeryes = answeryes;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getQanum() {
		return qanum;
	}
	public void setQanum(long qanum) {
		this.qanum = qanum;
	}
	public String getAnswerUserId2() {
		return answerUserId2;
	}
	public void setAnswerUserId2(String answerUserId2) {
		this.answerUserId2 = answerUserId2;
	}
	public String getAnswerUserId() {
		return answerUserId;
	}
	public void setAnswerUserId(String answerUserId) {
		this.answerUserId = answerUserId;
	}
	public long getGroupNum() {
		return groupNum;
	}
	public void setGroupNum(long groupNum) {
		this.groupNum = groupNum;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public long getParent() {
		return parent;
	}
	public void setParent(long parent) {
		this.parent = parent;
	}
	public long getQnum() {
		return qnum;
	}
	public void setQnum(long qnum) {
		this.qnum = qnum;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public int getHitCount() {
		return hitCount;
	}
	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;
	}
	

}
