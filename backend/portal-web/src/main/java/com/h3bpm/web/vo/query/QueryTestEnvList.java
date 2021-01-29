package com.h3bpm.web.vo.query;

import com.h3bpm.web.vo.ReqListTestEnvPageVo;

public class QueryTestEnvList extends ReqListTestEnvPageVo {

	public QueryTestEnvList(ReqListTestEnvPageVo voBean) {
		this.setsEcho(voBean.getsEcho());
		this.setiDisplayStart(voBean.getiDisplayStart());
		this.setiDisplayLength(voBean.getiDisplayLength());
		this.setKeyword(voBean.getKeyword());
	}
}
