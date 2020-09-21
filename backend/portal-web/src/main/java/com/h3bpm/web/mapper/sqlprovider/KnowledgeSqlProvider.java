package com.h3bpm.web.mapper.sqlprovider;

import OThinker.Common.Organization.Models.User;

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

		String statusSqlStr = " AND ( status = 1 ";
		if(!userId.isEmpty()){
			statusSqlStr += "or create_user_id = '" + userId + "'";
		}
		statusSqlStr += " ) ";
		
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
			"			 a.end_time endTime,"+
			"			 a.status status," +
			"			 a.desc_list_data descListData" +
			"			FROM"+
			"				ot_knowledge a,ot_knowledge_permission b"+
			"				WHERE a.is_delete=0"+
			"				AND a.id = b.knowledge_id "+	//a.id是查询用户的id,b.knowledge_id是文件分享者的id,所以导我的文件分享给别人，别人去查询的时候,这个条件一定不满足。
						nameSqlStr +
						tagNameSqlStr +
						nameSqlStr +
						tagNameSqlStr +
						flowCodesSqlStr +
						startTimeStartSqlStr +
						startTimeEndSqlStr +
						endTimeStartSqlStr +
						endTimeEndSqlStr +
						statusSqlStr +
						permissionSqlStr +
			"			ORDER BY"+
			"				a.create_time DESC";
		return sql;
	}

	//TODO: 需要如权限过滤
	public String findDeleteKnowledgeByUserId(Map<String, Object> para) {

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
			userIdSqlStr = " AND create_user_id='" + userId + "'";
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
						"				ot_knowledge"+
						"				WHERE is_delete=1"+
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
