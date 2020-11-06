package com.h3bpm.web.vo;

import com.h3bpm.web.entity.Liquidation;
import com.h3bpm.web.enumeration.LiquidationStatus;

import OThinker.Common.DateTimeUtil;

public class LiquidationVo {
	private String id = null;
	private String name = null;
	private String ipAddress = null;
	private String operateStep = null;
	private String doTime = null;
	private int status = 0;
	private String comment = null;
	private int index = 0;

	public LiquidationVo() {

	}

	public LiquidationVo(Liquidation model) {
		this.id = model.getId();
		this.name = model.getName();
		this.ipAddress = model.getIpAddress();
		this.operateStep = model.getOperateStep();

		if (model.getDoTime() != null) {
			this.doTime = DateTimeUtil.format(model.getDoTime(), "HH:mm");
		}

		this.status = model.getStatus();
		this.comment = model.getComment();
		this.index = model.getIndex();
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

	public String getDoTime() {
		return doTime;
	}

	public void setDoTime(String doTime) {
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

	public String getStatusStr() {
		return LiquidationStatus.parse(this.status).getDisplayName();
	}

}
