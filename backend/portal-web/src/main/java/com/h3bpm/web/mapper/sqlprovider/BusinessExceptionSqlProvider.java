package com.h3bpm.web.mapper.sqlprovider;

import java.util.Map;

public class BusinessExceptionSqlProvider {
	public String findList(Map<String, Object> para) {
		String systemType = (String) para.get("systemType");

		String systemTypeSqlStr = "";
		if (!systemTypeSqlStr.isEmpty()) {
			systemTypeSqlStr = " AND a.business_sys='" + systemType + "'";
		}

		String sql = "SELECT b.ObjectID id, a.`Name` name, a.CreatedBy createUser, a.business_sys businessSystem, a.start_time startTime, a.end_time endTime FROM `i_business_exception` a,ot_instancecontext b WHERE a.ObjectID = b.BizObjectId" + systemTypeSqlStr;

		return sql;
	}

}
