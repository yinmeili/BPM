package com.h3bpm.web.vo.query;

import com.h3bpm.web.vo.ReqListWorkflowTaskPageVo;

@SuppressWarnings("deprecation")
public class QueryWorkFlowTaskList extends ReqListWorkflowTaskPageVo {

	public QueryWorkFlowTaskList(ReqListWorkflowTaskPageVo voBean) {
		this.setsEcho(voBean.getsEcho());
		this.setiDisplayStart(voBean.getiDisplayStart());
		this.setiDisplayLength(voBean.getiDisplayLength());
		this.setFlowCode(voBean.getFlowCode());
		this.setUserDisplayName(voBean.getUserDisplayName());
		this.setStartTimeStart(voBean.getStartTimeStart());
		this.setStartTimeEnd(voBean.getStartTimeEnd());
	}

}
