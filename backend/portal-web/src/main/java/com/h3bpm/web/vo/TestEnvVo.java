package com.h3bpm.web.vo;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.h3bpm.web.entity.TestEnvInfo;

public class TestEnvVo {
	private String id = null;
	private String name = null;
	private String systemName = null;
	private String envType = null;
	private List<TestEnvVmVo> vmDatas = null;
	private List<TestEnvLinkVo> envs = null;
	private Date createTime = null;
	private String desc = null;
	private String joinAddress = null;

	@Deprecated
	public TestEnvVo() {

	}

	public TestEnvVo(TestEnvInfo model) {
		this.id = model.getId();
		this.name = model.getName();
		this.desc = model.getDesc();
		this.joinAddress = model.getJoinAddress();
		this.systemName = model.getSystemName();
		this.envType = model.getEnvType();
		this.createTime = model.getCreateTime();

		if (model.getVmDatas() != null) {
			vmDatas = JSONArray.parseArray(model.getVmDatas(), TestEnvVmVo.class);
		}

		if (model.getEnvDatas() != null) {
			envs = JSONArray.parseArray(model.getEnvDatas(), TestEnvLinkVo.class);
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

	public List<TestEnvVmVo> getVmDatas() {
		return vmDatas;
	}

	public void setVmDatas(List<TestEnvVmVo> vmDatas) {
		this.vmDatas = vmDatas;
	}

	public List<TestEnvLinkVo> getEnvs() {
		return envs;
	}

	public void setEnvs(List<TestEnvLinkVo> envs) {
		this.envs = envs;
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
