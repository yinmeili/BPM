package com.h3bpm.web.vo.query;

import java.util.Date;

import com.h3bpm.web.vo.ReqListProjectInfoPageVo;

import OThinker.Common.DateTimeUtil;

@SuppressWarnings("deprecation")
public class QueryProjectInfoList extends ReqListProjectInfoPageVo {
	public QueryProjectInfoList(ReqListProjectInfoPageVo voBean) {
		this.setsEcho(voBean.getsEcho());
		this.setiDisplayStart(voBean.getiDisplayStart());
		this.setiDisplayLength(voBean.getiDisplayLength());
		this.setKeyword(voBean.getKeyword());
		this.setLeaderId(voBean.getLeaderId());
		this.setStartTimeStart(voBean.getStartTimeStart());

		// 页面接收的时间没有时分秒，将时分秒加大到该天的最后时刻
		if (voBean.getStartTimeEnd() != null) {
			Date startEndTime = DateTimeUtil.addHours(voBean.getStartTimeEnd(), 23);
			startEndTime = DateTimeUtil.addMinutes(startEndTime, 59);
			startEndTime = DateTimeUtil.addSeconds(startEndTime, 59);
			this.setStartTimeEnd(startEndTime);
		}

		this.setEndTimeStart(voBean.getEndTimeStart());

		// 页面接收的时间没有时分秒，将时分秒加大到该天的最后时刻
		if (voBean.getEndTimeEnd() != null) {
			Date endEndTime = DateTimeUtil.addHours(voBean.getEndTimeEnd(), 23);
			endEndTime = DateTimeUtil.addMinutes(endEndTime, 59);
			endEndTime = DateTimeUtil.addSeconds(endEndTime, 59);
			this.setEndTimeEnd(endEndTime);
		}
	}
}
