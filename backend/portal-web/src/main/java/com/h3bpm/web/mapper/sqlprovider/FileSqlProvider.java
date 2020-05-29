package com.h3bpm.web.mapper.sqlprovider;

import java.util.Map;

public class FileSqlProvider {
	public String findFileByParentIdAndKeyword(Map<String, Object> para) {
		String parentId = (String) para.get("parentId");
		String keyword = para.get("keyword") == null ? "" : (String) para.get("keyword");
		
		String parentIdSqlStr = parentId.isEmpty() ? "parent_id is null" : "parent_id='" + parentId + "'";
		
		/*
		 * 关键字不为空时，模糊搜索所有目录名称和文件名 
		 */
		String keywordSqlStr = "";
		if(!keyword.isEmpty()){
			keywordSqlStr = " AND name like '%" + keyword + "%'";
			parentIdSqlStr = "";
		}
		
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
			"				WHERE is_delete=0 AND "+
						parentIdSqlStr +
						keywordSqlStr +
			"			ORDER BY"+
			"				name";
		return sql;
	}
	
}
