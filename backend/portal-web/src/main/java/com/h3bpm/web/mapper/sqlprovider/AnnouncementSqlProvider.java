package com.h3bpm.web.mapper.sqlprovider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class AnnouncementSqlProvider {

    public String findAnnouncementByTime(Map<String, Object> para) {

        Date date = (Date) para.get("date");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = simpleDateFormat.format(date);
        String timeSqlStr = " AND start_time <= '" + timeStr + "' AND " + "end_time >= '" + timeStr + "'";

        String sql =
                "SELECT" +
                        "			 id," +
                        "			 title," +
                        "			 description," +
                        "			 create_user_id createUserId," +
                        "			 create_time createTime," +
                        "			 link," +
                        "			 type," +
                        "			 org_id orgId," +
                        "			 org_name orgName" +
                        "			FROM" +
                        "				ot_announcement" +
                        "				WHERE 1=1" +
                        timeSqlStr +
                        "			ORDER BY" +
                        "				create_time DESC";
        return sql;
    }

    public String findAnnouncementByPage(Map<String, Object> para) {

        String typeSqlStr = "";
        if (para.get("type") != null) {
            typeSqlStr = " AND type=" + (Integer) para.get("type");
        }

        String title = para.get("title") == null ? "" : (String) para.get("title");
        String titleSqlStr = "";
        if (!title.isEmpty()) {
            titleSqlStr = " AND title like '%" + title + "%'";
        }

        String timeSqlStr = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (para.get("createTimeStart") != null) {
            Date dateStart = (Date) para.get("createTimeStart");
            String timeStartStr = simpleDateFormat.format(dateStart);
            timeSqlStr += " AND create_time >= '" + timeStartStr + "'";
        }
        if (para.get("createTimeEnd") != null) {
            Date dateEnd = (Date) para.get("createTimeEnd");
            String timeEndStr = simpleDateFormat.format(dateEnd);
            timeSqlStr += " AND create_time <= '" + timeEndStr + "'";
        }
        
        
        String orgId = para.get("orgId") == null ? "" : (String) para.get("orgId");
        String orgSqlStr = "";
        if (!orgId.isEmpty()) {
        	orgSqlStr = " AND org_id = '" + orgId + "'";
        }
        
        String sql =
                "SELECT" +
                        "			 id," +
                        "			 title," +
                        "			 description," +
                        "			 create_user_id createUserId," +
                        "			 create_time createTime," +
                        "			 link," +
                        "			 start_time startTime," +
                        "			 end_time endTime," +
                        "			 type," +
                        "			 update_time updateTime," +
                        "			 update_user_id updateUserId," +
                        "			 org_id orgId," +
                        "			 org_name orgName" +
                        "			FROM" +
                        "				ot_announcement" +
                        "				WHERE 1=1" +
                        typeSqlStr +
                        titleSqlStr +
                        timeSqlStr +
                        orgSqlStr + 
                        "			ORDER BY" +
                        "				create_time DESC";
        return sql;
    }

}
