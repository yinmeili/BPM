package com.h3bpm.web.mapper.sqlprovider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class KnowledgeSqlProvider {
	
	@SuppressWarnings("unchecked")
	public String findKnowledge(Map<String, Object> para) {

		String name = para.get("name") == null ? "" : (String) para.get("name");
		String nameSqlStr = "";
		if (!name.isEmpty()) {
			nameSqlStr = " AND a.name like '%" + name + "%'";
		}

		String tagName = para.get("tagName") == null ? "" : (String) para.get("tagName");
		String tagNameSqlStr = "";
		if (!tagName.isEmpty()) {
			tagNameSqlStr = " AND a.tag_name='" + tagName + "'";
		}

		String flowCodes = para.get("flowCodes") == null ? "" : (String) para.get("flowCodes");
		String flowCodesSqlStr = "";
		if (!flowCodes.isEmpty()) {
			String[] flowCodeslist = flowCodes.split(",");
			flowCodesSqlStr = " AND a.flow_code in (";
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
			startTimeStartSqlStr = " AND a.start_time >= '" + dateString + "'";
		}

		Date startTimeEnd = para.get("startTimeEnd") == null ? null : (Date) para.get("startTimeEnd");
		String startTimeEndSqlStr = "";
		if (startTimeEnd != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = format.format(startTimeEnd);
			startTimeEndSqlStr = " AND a.start_time <= '" + dateString + "'";
		}

		Date endTimeStart = para.get("endTimeStart") == null ? null : (Date) para.get("endTimeStart");
		String endTimeStartSqlStr = "";
		if (endTimeStart != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = format.format(endTimeStart);
			endTimeStartSqlStr = " AND a.end_time >= '" + dateString + "'";
		}

		Date endTimeEnd = para.get("endTimeEnd") == null ? null : (Date) para.get("endTimeEnd");
		String endTimeEndSqlStr = "";
		if (endTimeEnd != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = format.format(endTimeEnd);
			endTimeEndSqlStr = " AND a.end_time <= '" + dateString + "'";
		}
		
		String userId = para.get("userId") == null ? "" : (String) para.get("userId");
		String permissionSqlStr = " AND ( ";
		if (!userId.isEmpty()) {
			permissionSqlStr += " create_user_id = '" + userId + "'";
			permissionSqlStr += "  OR b.orgs LIKE '%" + userId + "%'";
		}
		List<String> userAllParentIds = para.get("userAllParentIds") == null ? null : (List<String>) para.get("userAllParentIds");
		if(userAllParentIds != null){
			for(String parentId:userAllParentIds){
				permissionSqlStr += " OR b.orgs LIKE '%" + parentId + "%'";
			}
		}
		permissionSqlStr += ") ";
		
		String sql =
				"SELECT"+
			"			 a.id,"+
			"			 a.name,"+
			"			 a.type,"+
			"			 a.`desc`,"+
			"			 a.tag_name tagName,"+
			"			 a.create_user_id createUserId,"+
			"			 a.create_user_name createUserName,"+
			"			 a.create_time createTime,"+
			"			 a.is_delete isDelete,"+
			"			 a.delete_time deleteTime,"+
			"			 a.flow_id flowId,"+
			"			 a.flow_code flowCode,"+
			"			 a.flow_code_desc flowCodeDesc,"+
			"			 a.start_time startTime,"+
			"			 a.end_time endTime"+
			"			FROM"+
			"				ot_knowledge a,ot_knowledge_permission b"+
			"				WHERE a.is_delete=0"+
			"				AND a.id = b.knowledge_id"+
						nameSqlStr +
						tagNameSqlStr +
						nameSqlStr +
						tagNameSqlStr +
						flowCodesSqlStr +
						startTimeStartSqlStr +
						startTimeEndSqlStr +
						endTimeStartSqlStr +
						endTimeEndSqlStr +
						permissionSqlStr +
			"			ORDER BY"+
			"				a.create_time DESC";
		return sql;
	}

}
