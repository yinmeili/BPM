package com.h3bpm.web.vo;

public class ReqListLiquidationPageVo extends ReqPageVo {
	private Integer status = null;// 状态
	private String keyword = null;// 关键字

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
