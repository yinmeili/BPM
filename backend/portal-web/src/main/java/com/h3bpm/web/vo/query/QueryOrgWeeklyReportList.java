package com.h3bpm.web.vo.query;

import com.h3bpm.web.vo.ReqListOrgWeeklyReportPageVo;

@SuppressWarnings("deprecation")
public class QueryOrgWeeklyReportList extends ReqListOrgWeeklyReportPageVo {
	public QueryOrgWeeklyReportList(ReqListOrgWeeklyReportPageVo voBean) {
		this.setsEcho(voBean.getsEcho());
		this.setiDisplayStart(voBean.getiDisplayStart());
		this.setiDisplayLength(voBean.getiDisplayLength());
		this.setOrgId(voBean.getOrgId());
		this.setUserId(voBean.getUserId());
		this.setStartTime(voBean.getStartTime());
		this.setEndTime(voBean.getEndTime());
	}
}
