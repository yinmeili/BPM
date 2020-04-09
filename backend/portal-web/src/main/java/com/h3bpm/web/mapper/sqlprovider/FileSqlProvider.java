package com.h3bpm.web.mapper.sqlprovider;

import java.util.Map;

public class FileSqlProvider {
	public String findFileByParentId(Map<String,Object> para) {
		String parentId = (String) para.get("parentId");
		String parentIdSqlStr = parentId.isEmpty() ? "parent_id is null" : "parent_id='" + parentId + "'";
		
		String sql =
				"SELECT"+
			"			 id,"+
			"			 name,"+
			"			 type,"+
			"			 parent_id parentId,"+
			"			 dir,"+
			"			 file_size fileSize,"+
			"			 create_user_id createUserId,"+
			"			 create_time createTime"+
			"			FROM"+
			"				ot_file"+
			"				WHERE 1=1 AND "+
						parentIdSqlStr +
			"			ORDER BY"+
			"				name";
		return sql;
	}
	
}
