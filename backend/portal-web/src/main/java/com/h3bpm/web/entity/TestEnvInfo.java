package com.h3bpm.web.entity;

import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.h3bpm.web.vo.TestEnvVo;

public class TestEnvInfo {
	private String id = null;
	private String name = null;
	private String systemName = null;
	private String envType = null;
	private String vmDatas = null;
	private String envDatas = null;
	private Date createTime = null;
	private String desc = null;
	private String joinAddress = null;

	@Deprecated
	public TestEnvInfo() {

	}

	public TestEnvInfo(TestEnvVo voBean) {
		this.id = voBean.getId();
		this.name = voBean.getName();
		this.desc = voBean.getDesc();
		this.joinAddress = voBean.getJoinAddress();
		this.envType = voBean.getEnvType();
		this.systemName = voBean.getSystemName();
		this.createTime = voBean.getCreateTime();

		if (voBean.getVmDatas() != null) {
			this.vmDatas = (JSONArray.toJSON(voBean.getVmDatas())).toString();
		}

		if (voBean.getEnvs() != null) {
			this.envDatas = (JSONArray.toJSON(voBean.getEnvs())).toString();
		}
	}

	public String getJoinAddress() {
		return joinAddress;
	}

	public void setJoinAddress(String joinAddress) {
		this.joinAddress = joinAddress;
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

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getEnvType() {
		return envType;
	}

	public void setEnvType(String envType) {
		this.envType = envType;
	}

	public String getVmDatas() {
		return vmDatas;
	}

	public void setVmDatas(String vmDatas) {
		this.vmDatas = vmDatas;
	}

	public String getEnvDatas() {
		return envDatas;
	}

	public void setEnvDatas(String envDatas) {
		this.envDatas = envDatas;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
