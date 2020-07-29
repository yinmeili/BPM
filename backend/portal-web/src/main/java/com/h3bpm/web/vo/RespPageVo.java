package com.h3bpm.web.vo;

public class RespPageVo {
	private int sEcho = 1;
	private long iTotalDisplayRecords = 0;
	private long iTotalRecords = 0;
	private long Total = 0;

	private Object Rows = null;

	@Deprecated
	public RespPageVo() {

	}

	public RespPageVo(long total, Object datas) {
		this.sEcho = 1;
		this.iTotalDisplayRecords = total;
		this.iTotalRecords = total;
		this.Total = total;
		this.Rows = datas;
	}

	public RespPageVo(int sEcho, long total, Object datas) {
		this.sEcho = sEcho;
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

	public long getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public long getiTotalRecords() {
		return iTotalRecords;
	}

	public void setiTotalRecords(int iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public long getTotal() {
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
