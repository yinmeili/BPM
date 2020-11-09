package com.h3bpm.web.vo.query;

import com.h3bpm.web.vo.ReqListLiquidationPageVo;

public class QueryLiquidationList extends ReqListLiquidationPageVo {

	public QueryLiquidationList(ReqListLiquidationPageVo voBean) {
		this.setsEcho(voBean.getsEcho());
		this.setiDisplayStart(voBean.getiDisplayStart());
		this.setiDisplayLength(voBean.getiDisplayLength());
		this.setStatus(voBean.getStatus());
		this.setKeyword(voBean.getKeyword());
	}

}
