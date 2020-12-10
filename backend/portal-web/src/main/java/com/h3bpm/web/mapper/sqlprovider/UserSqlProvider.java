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
                        "			 name," +
                        "			 Code loginName," +
                        "			 Mobile mobile" +
                        "			FROM" +
                        "				ot_user" +
                        "				WHERE 1=1" +
                        userIdSqlStr +
                        "			ORDER BY" +
                        "				name";
        return sql;
    }
    
    public String getUserByLoginName(Map<String, Object> para) {

        String loginName = para.get("loginName") == null ? "" : (String) para.get("loginName");

        String sql =
                "SELECT" +
                        "			 objectId id," +
                        "			 name," +
                        "			 Code loginName," +
                        "			 Mobile mobile" +
                        "			FROM" +
                        "				ot_user" +
                        "				WHERE 1=1" +
                        " AND Code='" + loginName + "'" +
                        "			ORDER BY" +
                        "				name";
        return sql;
    }

}
