package com.h3bpm.web.vo.query;

import java.util.Date;

import com.h3bpm.web.vo.ReqListLeaderActiveProjectInfoVo;

import OThinker.Common.DateTimeUtil;

@SuppressWarnings("deprecation")
public class QueryLeaderActiveProjectInfo extends ReqListLeaderActiveProjectInfoVo {
	public QueryLeaderActiveProjectInfo(ReqListLeaderActiveProjectInfoVo voBean) {
		this.setLeaderId(voBean.getLeaderId());
		this.setStartTime(voBean.getStartTime());

		// 页面接收的时间没有时分秒，将时分秒加大到该天的最后时刻
		if (voBean.getEndTime() != null) {
			Date endTime = DateTimeUtil.addHours(voBean.getEndTime(), 23);
			endTime = DateTimeUtil.addMinutes(endTime, 59);
			endTime = DateTimeUtil.addSeconds(endTime, 59);
			this.setEndTime(endTime);
		}
	}
}
