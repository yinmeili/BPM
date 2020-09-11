package com.h3bpm.web.vo.query;

import com.h3bpm.web.vo.ReqListAnnouncementPageVo;
import com.h3bpm.web.vo.ReqListKnowledgePageVo;

import java.util.List;

@SuppressWarnings("deprecation")
public class QueryAnnouncementList extends ReqListAnnouncementPageVo {
	private String queryUserId = null; // 当前查询的用户ID
	private List<String> userAllParentIds = null; // 用户的所有父部门ID

	public QueryAnnouncementList(ReqListAnnouncementPageVo voBean) {
		this.setsEcho(voBean.getsEcho());
		this.setiDisplayStart(voBean.getiDisplayStart());
		this.setiDisplayLength(voBean.getiDisplayLength());
		this.setType(voBean.getType());
		this.setTitle(voBean.getTitle());
		this.setCreateTimeStart(voBean.getCreateTimeStart());
		this.setCreateTimeEnd(voBean.getCreateTimeEnd());
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
