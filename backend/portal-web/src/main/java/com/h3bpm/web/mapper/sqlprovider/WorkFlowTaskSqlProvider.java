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
}
