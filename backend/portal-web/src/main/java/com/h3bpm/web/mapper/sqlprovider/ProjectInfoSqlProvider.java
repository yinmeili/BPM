package com.h3bpm.web.mapper.sqlprovider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ProjectInfoSqlProvider {
	public String findProjectInfo(Map<String, Object> para) {
		String keyword = para.get("keyword") == null ? "" : (String) para.get("keyword");
		String keywordSqlStr = "";
		if (!keyword.isEmpty()) {
			keywordSqlStr = " AND a.`name` like '%" + keyword + "%'";
		}

		String leaderId = para.get("leaderId") == null ? "" : (String) para.get("leaderId");
		String leaderIdSqlStr = "";
		if (!leaderId.isEmpty()) {
			leaderIdSqlStr = " AND a.leader_id='" + leaderId + "'";
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

		String sql = "SELECT" + 
					"	a.id id," + 
					"	a.`name` `name`," + 
					"	a.`desc` `desc`," + 
					"	a.leader_id leaderId," + 
					"	a.leader_name leaderName," + 
					"	a.start_time startTime," + 
					"	a.end_time endTime," + 
					"	a.create_time createTime" + 
					" FROM " + 
					"	`ow_project` a" + 
					" WHERE " + 
					"	1 = 1" + 
					keywordSqlStr + 
					leaderIdSqlStr + 
					startTimeStartSqlStr + 
					startTimeEndSqlStr + 
					endTimeStartSqlStr + 
					endTimeEndSqlStr + 
					" ORDER BY " + 
					"	a.start_time DESC";

		return sql;
	}
	
	public String findProjectInfoByLeaderIdAndStartTimeAndEndTime(Map<String, Object> para) {
		String leaderId = para.get("leaderId") == null ? "" : (String) para.get("leaderId");
		String leaderIdSqlStr = "";
		if (!leaderId.isEmpty()) {
			leaderIdSqlStr = " AND a.leader_id='" + leaderId + "'";
		}
		
		String timeSqlStr = "";
		Date startTime = para.get("startTime") == null ? null : (Date) para.get("startTime");
		Date endTime = para.get("endTime") == null ? null : (Date) para.get("endTime");
		
		if (startTime != null && endTime != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startTimeString = format.format(startTime);
			String endTimeString = format.format(endTime);
			timeSqlStr = " AND (";
			
			timeSqlStr += " (a.start_time >= '" + startTimeString + "' AND a.start_time <= '" + endTimeString + "')";
			timeSqlStr += " OR (a.end_time >= '" + startTimeString + "' AND a.end_time <= '" + endTimeString + "')";
			timeSqlStr += " OR (a.start_time <= '" + startTimeString + "' AND a.end_time >= '" + endTimeString + "')";
			
			timeSqlStr += ")";
		}


		String sql = "SELECT" + 
					"	a.id id," + 
					"	a.`name` `name`," + 
					"	a.`desc` `desc`," + 
					"	a.leader_id leaderId," + 
					"	a.leader_name leaderName," + 
					"	a.start_time startTime," + 
					"	a.end_time endTime," + 
					"	a.create_time createTime" + 
					" FROM " + 
					"	`ow_project` a" + 
					" WHERE " + 
					"	1 = 1" + 
					leaderIdSqlStr + 
					timeSqlStr + 
					" ORDER BY " + 
					"	a.start_time DESC";

		return sql;
	}

}
