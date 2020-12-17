package com.h3bpm.web.mapper.sqlprovider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class BusinessExceptionSqlProvider {
	public String findBusinessException(Map<String, Object> para) {
		String keyword = para.get("keyword") == null ? "" : (String) para.get("keyword");
		String keywordSqlStr = "";
		if (!keyword.isEmpty()) {
			keywordSqlStr = " AND a.`title` like '%" + keyword + "%'";
		}

		String userId = para.get("userId") == null ? "" : (String) para.get("userId");
		String userIdSqlStr = "";
		if (!userId.isEmpty()) {
			userIdSqlStr = " AND a.CreatedBy='" + userId + "'";
		}

		String businessSys = para.get("businessSystem") == null ? "" : (String) para.get("businessSystem");
		String businessSysSqlStr = "";
		if (!businessSys.isEmpty()) {
			String[] businessSyslist = businessSys.split(",");
			businessSysSqlStr = " AND a.business_sys in (";
			for (String businessSysStr : businessSyslist) {
				businessSysSqlStr += "'" + businessSysStr + "',";
			}
			businessSysSqlStr = businessSysSqlStr.substring(0, businessSysSqlStr.length() - 1);
			businessSysSqlStr += ")";
		}

		Date startTimeStart = para.get("startTimeStart") == null ? null : (Date) para.get("startTimeStart");
		String startTimeStartSqlStr = "";
		if (startTimeStart != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = format.format(startTimeStart);
			startTimeStartSqlStr = " AND a.start_time >= '" + dateString + "'";
		}

		Date startTimeEnd = para.get("startTimeEnd") == null ? null : (Date) para.get("startTimeEnd");
		String startTimeEndSqlStr = "";
		if (startTimeEnd != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = format.format(startTimeEnd);
			startTimeEndSqlStr = " AND a.start_time <= '" + dateString + "'";
		}

		Date endTimeStart = para.get("endTimeStart") == null ? null : (Date) para.get("endTimeStart");
		String endTimeStartSqlStr = "";
		if (endTimeStart != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = format.format(endTimeStart);
			endTimeStartSqlStr = " AND a.end_time >= '" + dateString + "'";
		}

		Date endTimeEnd = para.get("endTimeEnd") == null ? null : (Date) para.get("endTimeEnd");
		String endTimeEndSqlStr = "";
		if (endTimeEnd != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = format.format(endTimeEnd);
			endTimeEndSqlStr = " AND a.end_time <= '" + dateString + "'";
		}


		String sql = "SELECT"+
				"	b.ObjectID instanceId,"+
				"	a.`title` `title`,"+
				"	a.business_sys businessSystem,"+
				"	a.start_time startTime,"+
				"	a.end_time endTime,"+
				"	c.`Name` createUserName"+
				" FROM "+
				"	`i_business_exception` a,"+
				"	ot_instancecontext b,"+
				"	ot_user c "+
				" WHERE "+
				"	a.ObjectID = b.BizObjectId"+
				"	AND a.CreatedBy = c.ObjectID"+
				"   AND b.State=4"+
				keywordSqlStr+
				userIdSqlStr+
				businessSysSqlStr+
				startTimeStartSqlStr+
				startTimeEndSqlStr+
				endTimeStartSqlStr+
				endTimeEndSqlStr+
				" ORDER BY "+
				"	a.start_time DESC";

		return sql;
	}

}
