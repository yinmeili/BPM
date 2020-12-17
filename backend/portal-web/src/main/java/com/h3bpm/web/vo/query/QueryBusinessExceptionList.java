package com.h3bpm.web.vo.query;

import com.h3bpm.web.vo.ReqListBusinessExceptionPageVo;

@SuppressWarnings("deprecation")
public class QueryBusinessExceptionList extends ReqListBusinessExceptionPageVo {
	public QueryBusinessExceptionList(ReqListBusinessExceptionPageVo voBean) {
		this.setsEcho(voBean.getsEcho());
		this.setiDisplayStart(voBean.getiDisplayStart());
		this.setiDisplayLength(voBean.getiDisplayLength());
		this.setBusinessSystem(voBean.getBusinessSystem());
		this.setKeyword(voBean.getKeyword());
		this.setUserId(voBean.getUserId());
		this.setStartTimeStart(voBean.getStartTimeStart());
		this.setStartTimeEnd(voBean.getStartTimeEnd());
		this.setEndTimeStart(voBean.getEndTimeStart());
		this.setEndTimeEnd(voBean.getEndTimeEnd());
	}
}
