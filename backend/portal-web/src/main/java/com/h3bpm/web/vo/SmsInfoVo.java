package com.h3bpm.web.vo;

public class SmsInfoVo {
	private String userName = null;
	private String phoneNum = null;
	private String content = null;

	public SmsInfoVo(String userName, String phoneNum, String content) {
		this.userName = userName;
		this.phoneNum = phoneNum;
		this.content = content;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
