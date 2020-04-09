package com.h3bpm.web.entity;

import java.util.Date;

public class Report {
	private String id = null;
	private String title = null;
	private String subtit = null;
	private String firstLevelName = null;
	private String secondLevelName = null;
	private String type = null;
	private boolean inDepthReport;
	private String stockName = null;
	private String industryName = null;
	private String investmentRating = null;
	private String firstAuthorId = null;
	private String firstAuthor = null;
	private String secondAutorIds = null;
	private String secondAutor = null;
	private String abstractInfo = null;
	private String accessoryId = null;
	private String accessoryPath = null;
	private Date finishTime = null;
	private Date modifyTime = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtit() {
		return subtit;
	}

	public void setSubtit(String subtit) {
		this.subtit = subtit;
	}

	public String getFirstLevelName() {
		return firstLevelName;
	}

	public void setFirstLevelName(String firstLevelName) {
		this.firstLevelName = firstLevelName;
	}

	public String getSecondLevelName() {
		return secondLevelName;
	}

	public void setSecondLevelName(String secondLevelName) {
		this.secondLevelName = secondLevelName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean getInDepthReport() {
		return inDepthReport;
	}

	public void setInDepthReport(boolean inDepthReport) {
		this.inDepthReport = inDepthReport;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public String getInvestmentRating() {
		return investmentRating;
	}

	public void setInvestmentRating(String investmentRating) {
		this.investmentRating = investmentRating;
	}

	public String getFirstAuthor() {
		return firstAuthor;
	}

	public void setFirstAuthor(String firstAuthor) {
		this.firstAuthor = firstAuthor;
	}

	public String getSecondAutor() {
		return secondAutor;
	}

	public void setSecondAutor(String secondAutor) {
		this.secondAutor = secondAutor;
	}

	public String getAbstractInfo() {
		return abstractInfo;
	}

	public void setAbstractInfo(String abstractInfo) {
		this.abstractInfo = abstractInfo;
	}

	public String getAccessoryId() {
		return accessoryId;
	}

	public void setAccessoryId(String accessoryId) {
		this.accessoryId = accessoryId;
	}

	public String getAccessoryPath() {
		return accessoryPath;
	}

	public void setAccessoryPath(String accessoryPath) {
		this.accessoryPath = accessoryPath;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getFirstAuthorId() {
		return firstAuthorId;
	}

	public void setFirstAuthorId(String firstAuthorId) {
		this.firstAuthorId = firstAuthorId;
	}

	public String getSecondAutorIds() {
		return secondAutorIds;
	}

	public void setSecondAutorIds(String secondAutorIds) {
		this.secondAutorIds = secondAutorIds;
	}

}
