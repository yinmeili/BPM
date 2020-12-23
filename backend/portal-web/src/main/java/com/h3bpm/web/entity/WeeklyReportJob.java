package com.h3bpm.web.entity;

public class WeeklyReportJob {
	private String id = null;
	private String parentId = null;
	private String userId = null;
	private String content = null;
	private String evolve = null;
	private String ratio = null;
	private String problem = null;
	private String type = null;
	private int jobLevel = 1;

	public WeeklyReportJob() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEvolve() {
		return evolve;
	}

	public void setEvolve(String evolve) {
		this.evolve = evolve;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getJobLevel() {
		return jobLevel;
	}

	public void setJobLevel(int jobLevel) {
		this.jobLevel = jobLevel;
	}

}
