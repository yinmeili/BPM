package com.h3bpm.web.vo;

public class ReqPageVo {
	private int sEcho = 0; // 查询次数
	private int iDisplayStart = 0; // 开始页数，首页为0
	private int iDisplayLength = 0; // 每页显示条数

	public int getsEcho() {
		return sEcho;
	}

	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}

	public int getiDisplayStart() {
		return iDisplayStart;
	}

	public void setiDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	public int getiDisplayLength() {
		return iDisplayLength;
	}

	public void setiDisplayLength(int iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

}
