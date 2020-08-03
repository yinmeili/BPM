package com.h3bpm.web.mapper.sqlprovider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MyKnowledgeSqlProvider {
	
	//TODO: 需要如权限过滤
	public String findMyKnowledge(Map<String, Object> para) {

		String name = para.get("name") == null ? "" : (String) para.get("name");
		String nameSqlStr = "";
		if (!name.isEmpty()) {
			nameSqlStr = " AND name like '%" + name + "%'";
		}

		String tagName = para.get("tagName") == null ? "" : (String) para.get("tagName");
		String tagNameSqlStr = "";
		if (!tagName.isEmpty()) {
			tagNameSqlStr = " AND tag_name='" + tagName + "'";
		}

		String flowCodes = para.get("flowCodes") == null ? "" : (String) para.get("flowCodes");
		String flowCodesSqlStr = "";
		if (!flowCodes.isEmpty()) {
			String[] flowCodeslist = flowCodes.split(",");
			flowCodesSqlStr = " AND flow_code in (";
			for (String flowCode : flowCodeslist) {
				flowCodesSqlStr += "'" + flowCode + "',";
			}
			flowCodesSqlStr = flowCodesSqlStr.substring(0, flowCodesSqlStr.length() - 1);
			flowCodesSqlStr += ")";
		}

		Date startTimeStart = para.get("startTimeStart") == null ? null : (Date) para.get("startTimeStart");
		String startTimeStartSqlStr = "";
		if (startTimeStart != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = format.format(startTimeStart);
			startTimeStartSqlStr = " AND start_time >= '" + dateString + "'";
		}

		Date startTimeEnd = para.get("startTimeEnd") == null ? null : (Date) para.get("startTimeEnd");
		String startTimeEndSqlStr = "";
		if (startTimeEnd != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = format.format(startTimeEnd);
			startTimeEndSqlStr = " AND start_time <= '" + dateString + "'";
		}

		Date endTimeStart = para.get("endTimeStart") == null ? null : (Date) para.get("endTimeStart");
		String endTimeStartSqlStr = "";
		if (endTimeStart != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = format.format(endTimeStart);
			endTimeStartSqlStr = " AND end_time >= '" + dateString + "'";
		}

		Date endTimeEnd = para.get("endTimeEnd") == null ? null : (Date) para.get("endTimeEnd");
		String endTimeEndSqlStr = "";
		if (endTimeEnd != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = format.format(endTimeEnd);
			endTimeEndSqlStr = " AND end_time <= '" + dateString + "'";
		}


		String userId = para.get("userId") == null ? "" : (String) para.get("userId");
		String userIdSqlStr = "";
		if (!userId.isEmpty()) {
			userIdSqlStr += " and create_user_id = '" + userId + "'";
		}
		
		String sql =
				"SELECT"+
			"			 id,"+
			"			 name,"+
			"			 type,"+
			"			 `desc`,"+
			"			 tag_name tagName,"+
			"			 create_user_id createUserId,"+
			"			 create_user_name createUserName,"+
			"			 create_time createTime,"+
			"			 is_delete isDelete,"+
			"			 delete_time deleteTime,"+
			"			 flow_id flowId,"+
			"			 flow_code flowCode,"+
			"			 flow_code_desc flowCodeDesc,"+
			"			 start_time startTime,"+
			"			 end_time endTime"+
			"			FROM"+
			"				ot_my_knowledge"+
			"				WHERE is_delete=0"+
						nameSqlStr +
						tagNameSqlStr +
						nameSqlStr +
						tagNameSqlStr +
						flowCodesSqlStr +
						startTimeStartSqlStr +
						startTimeEndSqlStr +
						endTimeStartSqlStr +
						endTimeEndSqlStr +
						userIdSqlStr +
			"			ORDER BY"+
			"				create_time DESC";
		return sql;
	}

}
