package com.h3bpm.web.mapper.sqlprovider;

import java.util.Map;

public class TagSqlProvider {
	public String findTagByName(Map<String, Object> para) {

		String name = para.get("name") == null ? "" : (String) para.get("name");
		String nameSqlStr = "";
		if (!name.isEmpty()) {
			nameSqlStr = " and name like '%" + name + "%'";
		}

		String sql =
				"SELECT"+
			"			 id,"+
			"			 name,"+
			"			 type"+
			"			FROM"+
			"				ot_tag"+
			"				WHERE 1=1"+
						nameSqlStr +
			"			ORDER BY"+
			"				name";
		return sql;
	}
	
}
