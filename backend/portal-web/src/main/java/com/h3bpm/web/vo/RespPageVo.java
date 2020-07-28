package com.h3bpm.web.vo;

public class RespPageVo {
	private int sEcho = 1;
	private int iTotalDisplayRecords = 0;
	private int iTotalRecords = 0;
	private int Total = 0;

	private Object Rows = null;

	@Deprecated
	public RespPageVo() {

	}

	public RespPageVo(int total, Object datas) {
		this.sEcho = 1;
		this.iTotalDisplayRecords = total;
		this.iTotalRecords = total;
		this.Total = total;
		this.Rows = datas;
	}

	public int getsEcho() {
		return sEcho;
	}

	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}

	public int getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public int getiTotalRecords() {
		return iTotalRecords;
	}

	public void setiTotalRecords(int iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public int getTotal() {
		return Total;
	}

	public void setTotal(int total) {
		Total = total;
	}

	public Object getRows() {
		return Rows;
	}

	public void setRows(Object rows) {
		Rows = rows;
	}
}
