package com.h3bpm.web.entity;

import java.util.Date;

import com.h3bpm.web.vo.LiquidationVo;

import OThinker.Common.DateTimeUtil;

public class Liquidation implements java.io.Serializable {
	private static final long serialVersionUID = -709451120040162079L;

	private String id = null;
	private String name = null;
	private String ipAddress = null;
	private String operateStep = null;
	private Date doTime = null;
	private int status = 0;
	private String comment = null;
	private int index = 0;

	@Deprecated
	public Liquidation() {

	}

	public Liquidation(LiquidationVo voBean) {
		this.id = voBean.getId();
		this.name = voBean.getName();
		this.ipAddress = voBean.getIpAddress();
		this.operateStep = voBean.getOperateStep();

		if (voBean.getDoTime() != null) {
			this.doTime = DateTimeUtil.parse(voBean.getDoTime(), "HH:mm");
		}

		this.status = voBean.getStatus();
		this.comment = voBean.getComment();
		this.index = voBean.getIndex();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getOperateStep() {
		return operateStep;
	}

	public void setOperateStep(String operateStep) {
		this.operateStep = operateStep;
	}

	public Date getDoTime() {
		return doTime;
	}

	public void setDoTime(Date doTime) {
		this.doTime = doTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
