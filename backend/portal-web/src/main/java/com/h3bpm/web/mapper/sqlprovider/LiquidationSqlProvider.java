package com.h3bpm.web.mapper.sqlprovider;

import java.util.Map;

public class LiquidationSqlProvider {
    
	public String findLiquidation(Map<String, Object> para) {
		String keyword = para.get("keyword") == null ? "" : (String) para.get("keyword");
		String keywordSqlStr = "";
		if (!keyword.isEmpty()) {
			keywordSqlStr += " AND ( ";

			keywordSqlStr += " name like '%" + keyword + "%'";
			keywordSqlStr += " OR ip_address like '%" + keyword + "%'";
			keywordSqlStr += " OR operate_step like '%" + keyword + "%'";
			keywordSqlStr += " OR comment like '%" + keyword + "%'";

			keywordSqlStr += " ) ";
		}

		Integer status = para.get("status") == null ? null : (Integer) para.get("status");
		String statusSqlStr = "";
		if (status != null) {
			statusSqlStr = " AND status = " + status;
		}

        String sql =
                "SELECT"+
                        "			 id,"+
                        "			 name,"+
                        "			 ip_address ipAddress,"+
                        "			 operate_step operateStep,"+
                        "			 do_time doTime,"+
                        "			 status,"+
                        "			 comment,"+
                        "			 `index`"+
                        "			FROM"+
                        "				ot_liquidation"+
                        "				WHERE 1=1  "+
                        keywordSqlStr + 
                        statusSqlStr + 
						"			order by `index`";
        return sql;
    }
	
	public String updateIndexAddOne(Map<String, Object> para) {
		Integer startIndex = para.get("startIndex") == null ? null : (Integer) para.get("startIndex");
		String startIndexSqlStr = "";
		if (startIndex != null) {
			startIndexSqlStr += " AND  `index` >= " + startIndex;
		}

		Integer endIndex = para.get("endIndex") == null ? null : (Integer) para.get("endIndex");
		String endIndexSqlStr = "";
		if (endIndex != null) {
			endIndexSqlStr += " AND  `index` < " + endIndex;
		}

        String sql =
                "UPDATE `ot_liquidation` SET `index`=`index` + 1 "+
                        "				WHERE 1=1  "+
                        startIndexSqlStr + 
                        endIndexSqlStr ;
        return sql;
    }
	
	public String updateIndexRemoveOne(Map<String, Object> para) {
		Integer startIndex = para.get("startIndex") == null ? null : (Integer) para.get("startIndex");
		String startIndexSqlStr = "";
		if (startIndex != null) {
			startIndexSqlStr += " AND  `index` > " + startIndex;
		}

		Integer endIndex = para.get("endIndex") == null ? null : (Integer) para.get("endIndex");
		String endIndexSqlStr = "";
		if (endIndex != null) {
			endIndexSqlStr += " AND  `index` <= " + endIndex;
		}

        String sql =
                "UPDATE `ot_liquidation` SET `index`=`index` - 1 "+
                        "				WHERE 1=1  "+
                        startIndexSqlStr + 
                        endIndexSqlStr ;
        return sql;
    }
}
