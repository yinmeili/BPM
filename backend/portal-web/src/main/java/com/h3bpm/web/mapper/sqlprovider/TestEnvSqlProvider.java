package com.h3bpm.web.mapper.sqlprovider;

import java.util.Map;

public class TestEnvSqlProvider {
	
	public String findTestEnv(Map<String, Object> para) {

		String keyword = para.get("keyword") == null ? "" : (String) para.get("keyword");
		String keywordSqlStr = "";
		if (!keyword.isEmpty()) {
			keywordSqlStr = " AND (";
			keywordSqlStr += " a.name like '%" + keyword + "%'";
			keywordSqlStr += " OR a.env_type like '%" + keyword + "%'";
			keywordSqlStr += " OR a.system_name like '%" + keyword + "%'";
			keywordSqlStr += ")";
		}
		
		String sql =
				"SELECT"+
			"			 a.id,"+
			"			 a.name,"+
			"			 a.`desc`,"+
			"			 a.`join_address` joinAddress,"+
			"			 a.vm_datas vmDatas,"+
			"			 a.env_datas envDatas,"+
			"			 a.env_type envType,"+
			"			 a.system_name systemName,"+
			"			 a.create_time createTime"+
			"			FROM"+
			"				ow_test_env a "+
			"				WHERE 1=1"+
						keywordSqlStr +
			"			ORDER BY"+
			"				a.create_time DESC";
		return sql;
	}
	
}
