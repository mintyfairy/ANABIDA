package com.join;

public class JoinDTO {
	private long buynum;
	private String userId;
	private String title;
	private String content;
	private String reg_date;
	private int min_peo;
	private int joinCount;
	private int hitCount;
	
	
	
	
	public int getMin_peo() {
		return min_peo;
	}
	public void setMin_peo(int min_peo) {
		this.min_peo = min_peo;
	}
	public long getBuynum() {
		return buynum;
	}
	public void setBuynum(long buynum) {
		this.buynum = buynum;
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
	public int getJoinCount() {
		return joinCount;
	}
	public void setJoinCount(int joinCount) {
		this.joinCount = joinCount;
	}
	public int getHitCount() {
		return hitCount;
	}
	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;
	}
	
	
}
