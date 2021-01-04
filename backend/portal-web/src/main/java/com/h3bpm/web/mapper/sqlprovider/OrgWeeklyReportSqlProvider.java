package com.h3bpm.web.mapper.sqlprovider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class OrgWeeklyReportSqlProvider {
	public String findOrgWeeklyReport(Map<String, Object> para) {
		String orgId = para.get("orgId") == null ? "" : (String) para.get("orgId");
		String orgIdSqlStr = "";
		if (!orgId.isEmpty()) {
			orgIdSqlStr = " AND a.org_id = '" + orgId + "'";
		}

		String userId = para.get("userId") == null ? "" : (String) para.get("userId");
		String userIdSqlStr = "";
		if (!userId.isEmpty()) {
			userIdSqlStr = " AND c.ObjectID='" + userId + "'";
		}

		Date startTime = para.get("startTime") == null ? null : (Date) para.get("startTime");
		Date endTime = para.get("endTime") == null ? null : (Date) para.get("endTime");
		String timeSqlStr = "";
		if (startTime != null || endTime != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startTimeString = format.format(startTime);
			String endTimeString = format.format(endTime);
			timeSqlStr = " AND a.start_time >= '" + startTimeString + "'";
			timeSqlStr += " AND a.start_time <= '" + endTimeString + "'";
		}

		String sql = "SELECT"+
				"	b.ObjectID instanceId,"+
				"	d.ObjectID workItemId,"+
				"	a.`title` `title`,"+
				"	a.start_time startTime,"+
				"	a.end_time endTime"+
				" FROM "+
				"	`i_org_weekly_report` a,"+
				"	ot_instancecontext b,"+
				"	ot_user c,"+
				"	ot_circulateitemfinished d"+
				" WHERE"+
				"	a.ObjectID = b.BizObjectId"+
				" AND d.InstanceId=b.ObjectID"+
				" AND d.Originator = c.ObjectID"+
				" AND b.State = 4"+
				userIdSqlStr+
				orgIdSqlStr+
				timeSqlStr+
				"ORDER BY"+
				"	a.start_time DESC";

		return sql;
	}
}
