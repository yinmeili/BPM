package com.h3bpm.web.mapper.sqlprovider;

import java.util.Map;

public class UserSqlProvider {
    public String findSubordinateByUserId(Map<String, Object> para) {

        String userId = para.get("userId") == null ? "" : (String) para.get("userId");
        String userIdSqlStr = "";
        if (!userId.isEmpty()) {
            userIdSqlStr = " AND ManagerID='" + userId + "'";
        }

        String sql =
                "SELECT" +
                        "			 objectId id," +
                        "			 name" +
                        "			FROM" +
                        "				ot_user" +
                        "				WHERE 1=1" +
                        userIdSqlStr +
                        "			ORDER BY" +
                        "				name";
        return sql;
    }

}
