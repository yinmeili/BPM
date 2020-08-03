package com.h3bpm.web.vo;

public class RespPageVo {
	private int sEcho = 1;
	private long iTotalDisplayRecords = 0;
	private long iTotalRecords = 0;
	private long Total = 0;
	private String ExtendProperty = null;
	private String ObjectID = null;

	private Object datas = null;

	@Deprecated
	public RespPageVo() {

	}

	public RespPageVo(long total, Object datas) {
		this.sEcho = 1;
		this.iTotalDisplayRecords = total;
		this.iTotalRecords = total;
		this.Total = total;
		this.datas = datas;
	}

	public RespPageVo(int sEcho, long total, Object datas) {
		this.sEcho = sEcho;
		this.iTotalDisplayRecords = total;
		this.iTotalRecords = total;
		this.Total = total;
		this.datas = datas;
	}

	public int getsEcho() {
		return sEcho;
	}

	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}

	public String getExtendProperty() {
		return ExtendProperty;
	}

	public void setExtendProperty(String extendProperty) {
		ExtendProperty = extendProperty;
	}

	public String getObjectID() {
		return ObjectID;
	}

	public void setObjectID(String objectID) {
		ObjectID = objectID;
	}

	public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public void setiTotalRecords(long iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public void setTotal(long total) {
		Total = total;
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

	public Object getDatas() {
		return datas;
	}

	public void setDatas(Object datas) {
		this.datas = datas;
	}

}
