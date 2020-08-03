package com.h3bpm.web.vo.query;

import java.util.List;

import com.h3bpm.web.vo.ReqListKnowledgePageVo;

@SuppressWarnings("deprecation")
public class QueryKnowledgeList extends ReqListKnowledgePageVo {
	private String queryUserId = null; // 当前查询的用户ID
	private List<String> userAllParentIds = null; // 用户的所有父部门ID

	public QueryKnowledgeList(ReqListKnowledgePageVo voBean) {
		this.setsEcho(voBean.getsEcho());
		this.setiDisplayStart(voBean.getiDisplayStart());
		this.setiDisplayLength(voBean.getiDisplayLength());
		this.setFlowCodes(voBean.getFlowCodes());
		this.setName(voBean.getName());
		this.setTagName(voBean.getTagName());
		this.setStartTimeStart(voBean.getStartTimeStart());
		this.setStartTimeEnd(voBean.getStartTimeEnd());
		this.setEndTimeStart(voBean.getEndTimeStart());
		this.setEndTimeEnd(voBean.getEndTimeEnd());
	}

	public String getQueryUserId() {
		return queryUserId;
	}

	public void setQueryUserId(String queryUserId) {
		this.queryUserId = queryUserId;
	}

	public List<String> getUserAllParentIds() {
		return userAllParentIds;
	}

	public void setUserAllParentIds(List<String> userAllParentIds) {
		this.userAllParentIds = userAllParentIds;
	}
}
