package com.cbbs;

public class CommunityDTO {

	private long num;
	private long picnum;
	private String userId;
	private String userName;
	private String ctitle;
	private String ccontent;
	private String creg_date;
	private String ccategory;
	
	// 좋아요
	private int likeCount;
	

	// 모임 번호
	private long mnum;
	// 조회수
	private int chitCount;
	// 모임일자
	private String mreg_date;
	// 모임장소
	private String zip;
	private String addr1;
	private String addr2;
	
	// 모임 최대 인원수
	private int cmember;
	
	private String picFileName;
	private String[] picFileNames;

	

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
	
	public long getMnum() {
		return mnum;
	}

	public void setMnum(long mnum) {
		this.mnum = mnum;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getAddr1() {
		return addr1;
	}

	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}

	public String getAddr2() {
		return addr2;
	}

	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}

	public int getCmember() {
		return cmember;
	}

	public void setCmember(int cmember) {
		this.cmember = cmember;
	}
	
	
	public String getPicFileName() {
		return picFileName;
	}

	public void setPicFileName(String picFileName) {
		this.picFileName = picFileName;
	}

	public String[] getPicFileNames() {
		return picFileNames;
	}

	public void setPicFileNames(String[] picFileNames) {
		this.picFileNames = picFileNames;
	}

	public long getPicnum() {
		return picnum;
	}

	public void setPicnum(long picnum) {
		this.picnum = picnum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public String getCcontent() {
		return ccontent;
	}

	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
	}

	public String getCreg_date() {
		return creg_date;
	}

	public void setCreg_date(String creg_date) {
		this.creg_date = creg_date;
	}

	public String getCcategory() {
		return ccategory;
	}

	public void setCcategory(String ccategory) {
		this.ccategory = ccategory;
	}

	public int getChitCount() {
		return chitCount;
	}

	public void setChitCount(int chitCount) {
		this.chitCount = chitCount;
	}

	public String getMreg_date() {
		return mreg_date;
	}

	public void setMreg_date(String mreg_date) {
		this.mreg_date = mreg_date;
	}

}
