package com.h3bpm.web.mapper.sqlprovider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class WorkFlowTaskSqlProvider {
    public String findUnFinishWorkFlowTask(Map<String, Object> para){
        Date startTime = (Date)para.get("startTime");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = simpleDateFormat.format(startTime);
        String startTimeStrSql = " start_time >= " + "'" + dateString + "'";
        String sql =
                "SELECT"+
                        "			 id,"+
                        "			 instance_id instanceId,"+
                        "			 workflow_code workflowCode,"+
                        "			 user_login_name userLoginName,"+
                        "			 user_display_name userDisplayName,"+
                        "			 create_time createTime,"+
                        "			 start_time startTime,"+
                        "			 end_time endTime,"+
                        "			 execute_type executeType,"+
                        "			 param_data paramData"+
                        "			FROM"+
                        "				ot_workflow_task"+
                        "				WHERE execute_type != " + 1 + " AND "+
                        startTimeStrSql;
        return sql;
    }
    
    public String findWorkFlowTask(Map<String, Object> para){
    	String name = para.get("userDisplayName") == null ? "" : (String) para.get("userDisplayName");
		String nameSqlStr = "";
		if (!name.isEmpty()) {
			nameSqlStr = " AND user_display_name like '%" + name + "%'";
		}
		
		String flowCode = para.get("flowCode") == null ? "" : (String) para.get("flowCode");
		String flowCodeSqlStr = "";
		if (!flowCode.isEmpty()) {
			flowCodeSqlStr = " AND workflow_code = '" + flowCode + "'";
		}
    	
    	Date startTimeStart = para.get("startTimeStart") == null ? null : (Date) para.get("startTimeStart");
		String startTimeStartSqlStr = "";
		if (startTimeStart != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = format.format(startTimeStart);
			startTimeStartSqlStr = " AND start_time >= '" + dateString + "'";
		}

		Date startTimeEnd = para.get("startTimeEnd") == null ? null : (Date) para.get("startTimeEnd");
		String startTimeEndSqlStr = "";
		if (startTimeEnd != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = format.format(startTimeEnd);
			startTimeEndSqlStr = " AND start_time <= '" + dateString + "'";
		}
        
        String sql =
                "SELECT"+
                        "			 id,"+
                        "			 instance_id instanceId,"+
                        "			 workflow_code workflowCode,"+
                        "			 user_login_name userLoginName,"+
                        "			 user_display_name userDisplayName,"+
                        "			 create_time createTime,"+
                        "			 start_time startTime,"+
                        "			 end_time endTime,"+
                        "			 execute_type executeType,"+
                        "			 param_data paramData"+
                        "			FROM"+
                        "				ot_workflow_task"+
                        "				WHERE 1=1  "+
                        nameSqlStr + 
                        flowCodeSqlStr + 
                        startTimeStartSqlStr +
						startTimeEndSqlStr +
						"			order by start_time DESC";
        return sql;
    }
}
