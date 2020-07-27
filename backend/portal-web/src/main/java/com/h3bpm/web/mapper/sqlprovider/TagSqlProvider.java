package com.h3bpm.web.mapper.sqlprovider;

import java.util.Map;

public class TagSqlProvider {
	public String findTagByTypeAndName(Map<String, Object> para) {

		String name = para.get("name") == null ? "" : (String) para.get("name");
		String nameSqlStr = "";
		if (!name.isEmpty()) {
			nameSqlStr = " and name like '%" + name + "%'";
		}

		String type = para.get("type") == null ? "" : (String) para.get("type");
		String typeSqlStr = "";
		if (!type.isEmpty()) {
			typeSqlStr = " AND type='" + type + "'";
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
						typeSqlStr +
			"			ORDER BY"+
			"				name";
		return sql;
	}

}
