package com.h3bpm.web.mapper.sqlprovider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class WeeklyReportSqlProvider {
    public String findWeeklyReportJob(Map<String, Object> para){
    	Date startTime = para.get("startTime") == null ? null : (Date) para.get("startTime");
		String startTimeSqlStr = "";
		if (startTime != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = format.format(startTime);
			startTimeSqlStr = " AND a.CreatedTime >= '" + dateString + "'";
		}

		Date endTime = para.get("endTime") == null ? null : (Date) para.get("endTime");
		String endTimeSqlStr = "";
		if (endTime != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = format.format(endTime);
			endTimeSqlStr = " AND a.CreatedTime <= '" + dateString + "'";
		}
		
		String userId = para.get("userId") == null ? "" : (String) para.get("userId");
		String userIdSqlStr = "";
		if (!userId.isEmpty()) {
			userIdSqlStr += " AND a.OwnerId = '" + userId + "'";
		}
		
		int jobLevel = para.get("jobLevel") == null ? 100 : (Integer) para.get("jobLevel");
		String jobLevelSqlStr = "";
		if (!userId.isEmpty()) {
			jobLevelSqlStr += "AND b.job_level >= " + jobLevel;
		}
        
        String sql =
        		"SELECT"+
        				"	b.ObjectID id,"+
        				"	b.ParentObjectID parentId,"+
        				"	a.OwnerId userId,"+
        				"	b.job_content content,"+
        				"	b.job_evolve evolve,"+
        				"	b.job_level jobLevel,"+
        				"	b.job_problem problem,"+
        				"	b.job_ratio ratio,"+
        				"	b.job_type type"+
        				" FROM "+
        				"	`i_weekly_report` a RIGHT JOIN"+
        				"	i_weekly_report_job b ON a.ObjectID = b.ParentObjectID"+
        				"	LEFT JOIN ot_instancecontext c ON a.ObjectID=c.BizObjectId"+
        				" WHERE c.State=4 "+
        				userIdSqlStr+
        				startTimeSqlStr+
        				endTimeSqlStr+
        				jobLevelSqlStr;
        return sql;
    }
    
    public String findWeeklyReportProject(Map<String, Object> para){
    	Date startTime = para.get("startTime") == null ? null : (Date) para.get("startTime");
		String startTimeSqlStr = "";
		if (startTime != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = format.format(startTime);
			startTimeSqlStr = " AND a.CreatedTime >= '" + dateString + "'";
		}

		Date endTime = para.get("endTime") == null ? null : (Date) para.get("endTime");
		String endTimeSqlStr = "";
		if (endTime != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = format.format(endTime);
			endTimeSqlStr = " AND a.CreatedTime <= '" + dateString + "'";
		}
		
		String userId = para.get("userId") == null ? "" : (String) para.get("userId");
		String userIdSqlStr = "";
		if (!userId.isEmpty()) {
			userIdSqlStr += " AND a.OwnerId = '" + userId + "'";
		}
		
        String sql =
        		"SELECT"+
        				"	b.ObjectID id,"+
        				"	b.ParentObjectID parentId,"+
        				"	a.OwnerId userId,"+
        				"	b.pj_name `name`,"+
        				"	b.pj_info info,"+
        				"	b.pj_evolve evolve,"+
        				"	b.pj_ratio ratio,"+
        				"	b.pj_org org,"+
        				"	b.pj_remark remark"+
        				" FROM "+
        				"	`i_weekly_report` a RIGHT JOIN"+
        				"	i_weekly_report_pj b ON a.ObjectID = b.ParentObjectID"+
        				"	LEFT JOIN ot_instancecontext c ON a.ObjectID=c.BizObjectId"+
        				" WHERE c.State=4 "+
        				userIdSqlStr+
        				startTimeSqlStr+
        				endTimeSqlStr;
        return sql;
    }
    
    public String findWeeklyReportJobPlan(Map<String, Object> para){
    	Date startTime = para.get("startTime") == null ? null : (Date) para.get("startTime");
		String startTimeSqlStr = "";
		if (startTime != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = format.format(startTime);
			startTimeSqlStr = " AND a.CreatedTime >= '" + dateString + "'";
		}

		Date endTime = para.get("endTime") == null ? null : (Date) para.get("endTime");
		String endTimeSqlStr = "";
		if (endTime != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = format.format(endTime);
			endTimeSqlStr = " AND a.CreatedTime <= '" + dateString + "'";
		}
		
		String userId = para.get("userId") == null ? "" : (String) para.get("userId");
		String userIdSqlStr = "";
		if (!userId.isEmpty()) {
			userIdSqlStr += " AND a.OwnerId = '" + userId + "'";
		}
		
		int jobLevel = para.get("jobLevel") == null ? 100 : (Integer) para.get("jobLevel");
		String jobLevelSqlStr = "";
		if (!userId.isEmpty()) {
			jobLevelSqlStr += "AND b.job_level >= " + jobLevel;
		}
        
        String sql =
        		"SELECT"+
        				"	b.ObjectID id,"+
        				"	b.ParentObjectID parentId,"+
        				"	a.OwnerId userId,"+
        				"	b.job_content content,"+
        				"	b.job_evolve evolve,"+
        				"	b.job_level jobLevel,"+
        				"	b.job_problem problem,"+
        				"	b.job_ratio ratio,"+
        				"	b.job_type type"+
        				" FROM "+
        				"	`i_weekly_report` a RIGHT JOIN"+
        				"	i_weekly_job_plan b ON a.ObjectID = b.ParentObjectID"+
        				"	LEFT JOIN ot_instancecontext c ON a.ObjectID=c.BizObjectId"+
        				" WHERE c.State=4 "+
        				userIdSqlStr+
        				startTimeSqlStr+
        				endTimeSqlStr+
        				jobLevelSqlStr;
        return sql;
    }
    
    public String findWeeklyReportProjectPlan(Map<String, Object> para){
    	Date startTime = para.get("startTime") == null ? null : (Date) para.get("startTime");
		String startTimeSqlStr = "";
		if (startTime != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = format.format(startTime);
			startTimeSqlStr = " AND a.CreatedTime >= '" + dateString + "'";
		}

		Date endTime = para.get("endTime") == null ? null : (Date) para.get("endTime");
		String endTimeSqlStr = "";
		if (endTime != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = format.format(endTime);
			endTimeSqlStr = " AND a.CreatedTime <= '" + dateString + "'";
		}
		
		String userId = para.get("userId") == null ? "" : (String) para.get("userId");
		String userIdSqlStr = "";
		if (!userId.isEmpty()) {
			userIdSqlStr += " AND a.OwnerId = '" + userId + "'";
		}
		
        String sql =
        		"SELECT"+
        				"	b.ObjectID id,"+
        				"	b.ParentObjectID parentId,"+
        				"	a.OwnerId userId,"+
        				"	b.pj_name `name`,"+
        				"	b.pj_info info,"+
        				"	b.pj_evolve evolve,"+
        				"	b.pj_ratio ratio,"+
        				"	b.pj_org org,"+
        				"	b.pj_remark remark"+
        				" FROM "+
        				"	`i_weekly_report` a RIGHT JOIN"+
        				"	i_weekly_pj_plan b ON a.ObjectID = b.ParentObjectID"+
        				"	LEFT JOIN ot_instancecontext c ON a.ObjectID=c.BizObjectId"+
        				" WHERE c.State=4 "+
        				userIdSqlStr+
        				startTimeSqlStr+
        				endTimeSqlStr;
        return sql;
    }
}
