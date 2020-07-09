package com.h3bpm.web.mapper.sqlprovider;

import java.util.Map;

public class FileSqlProvider {
	public String findFileByParentIdAndKeyword(Map<String, Object> para) {
		String parentId = (String) para.get("parentId");
		String keyword = para.get("keyword") == null ? "" : (String) para.get("keyword");
		
		
		String parentIdSqlStr = (parentId == null || parentId.isEmpty()) ? " AND parent_id is null" : " AND parent_id='" + parentId + "'";
		
		/*
		 * 关键字不为空时，模糊搜索所有目录名称和文件名 
		 */
		String keywordSqlStr = "";
		if(!keyword.isEmpty()){
			keywordSqlStr = " AND name like '%" + keyword + "%'";
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
			"			 create_time createTime,"+
			"			 download_file_id downloadFileId"+
			"			FROM"+
			"				ot_file"+
			"				WHERE is_delete=0 "+
						parentIdSqlStr +
						keywordSqlStr +
			"			ORDER BY"+
			"				name";
		return sql;
	}

	public String findDeletedFileByUserId(Map<String, Object> para) {
		String userId = (String)para.get("userId");

		String sql =
				"SELECT"+
						"			 id,"+
						"			 name,"+
						"			 type,"+
						"			 parent_id parentId,"+
						"			 dir,"+
						"			 file_size fileSize,"+
						"			 create_user_id createUserId,"+
						"			 create_time createTime,"+
						"			 is_delete isDelete,"+
						"			 delete_time deleteTime"+
						"			FROM"+
						"				ot_file"+
						"				WHERE is_delete=1 AND "+
						"           create_user_id = '" + userId + "'"+
						"				ORDER BY delete_time DESC";
		return sql;
	}
	
}
