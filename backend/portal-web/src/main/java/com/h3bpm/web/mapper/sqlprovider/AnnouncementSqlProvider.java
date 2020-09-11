package com.h3bpm.web.mapper.sqlprovider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class AnnouncementSqlProvider {

    //TODO: 需要如权限过滤
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
                        "			 start_time startTime," +
                        "			 end_time endTime," +
                        "			 type" +
                        "			FROM" +
                        "				ot_announcement" +
                        "				WHERE 1=1" +
                        timeSqlStr +
                        "			ORDER BY" +
                        "				create_time DESC";
        return sql;
    }

    /*//TODO: 需要如权限过滤
    public String findAnnouncementByPage(Map<String, Object> para) {

        String timeSqlStr = " AND start_time <= '" + timeStr + "' and " + "end_time >= '" + timeStr + "'";

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
                        "			 type" +
                        "			FROM" +
                        "				ot_announcement" +
                        "				WHERE 1=1" +
                        timeSqlStr +
                        "			ORDER BY" +
                        "				create_time DESC";
        return sql;
    }*/

}
