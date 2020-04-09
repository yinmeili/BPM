package com.h3bpm.web.entity;

public class Staff {
	private String id = null;
	private String name = null;
	private boolean isLeaveOffice  = false;
	private String industryId = null;
	private String industryName = null;
	private String partTimeIndustryId = null;
	private String partTimeIndustryName = null;
	private boolean isLeader = false;
	
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
	public boolean getIsLeaveOffice() {
		return isLeaveOffice;
	}
	public void setIsLeaveOffice(boolean isLeaveOffice) {
		this.isLeaveOffice = isLeaveOffice;
	}
	public String getIndustryId() {
		return industryId;
	}
	public void setIndustryId(String industryId) {
		this.industryId = industryId;
	}
	public String getIndustryName() {
		return industryName;
	}
	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}
	public String getPartTimeIndustryId() {
		return partTimeIndustryId;
	}
	public void setPartTimeIndustryId(String partTimeIndustryId) {
		this.partTimeIndustryId = partTimeIndustryId;
	}
	public String getPartTimeIndustryName() {
		return partTimeIndustryName;
	}
	public void setPartTimeIndustryName(String partTimeIndustryName) {
		this.partTimeIndustryName = partTimeIndustryName;
	}
	public boolean getIsLeader() {
		return isLeader;
	}
	public void setIsLeader(boolean isLeader) {
		this.isLeader = isLeader;
	}
}
