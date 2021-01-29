package com.h3bpm.web.vo;

import com.alibaba.fastjson.JSONArray;
import com.h3bpm.web.vo.KnowledgeVo;

import java.util.Date;

public class TestEnvVmVo {
	private String name = null;
	private String ip = null;
	private String os = null;
	private String cpu = null;
	private String ram = null;
	private String diskSize = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getRam() {
		return ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

	public String getDiskSize() {
		return diskSize;
	}

	public void setDiskSize(String diskSize) {
		this.diskSize = diskSize;
	}

}
