package com.h3bpm.web.vo.query;

import com.h3bpm.web.vo.ReqListAnnouncementPageVo;

@SuppressWarnings("deprecation")
public class QueryAnnouncementList extends ReqListAnnouncementPageVo {

	public QueryAnnouncementList(ReqListAnnouncementPageVo voBean) {
		this.setsEcho(voBean.getsEcho());
		this.setiDisplayStart(voBean.getiDisplayStart());
		this.setiDisplayLength(voBean.getiDisplayLength());
		this.setType(voBean.getType());
		this.setTitle(voBean.getTitle());
		this.setCreateTimeStart(voBean.getCreateTimeStart());
		this.setCreateTimeEnd(voBean.getCreateTimeEnd());
		this.setOrgId(voBean.getOrgId());
	}

}
