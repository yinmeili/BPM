package com.h3bpm.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.h3bpm.web.entity.File;
import com.h3bpm.web.mapper.sqlprovider.FileSqlProvider;

public interface FileMapper {

	@SelectProvider(type = FileSqlProvider.class, method = "findFileByParentId")
	public List<File> findFileByParentId(@Param("parentId") String parentId);
	
	@Insert({"INSERT INTO `h3bpm`.`ot_file` (`id`, `parent_id`, `type`, `name`, `dir`, `file_size`, `create_user_id`, `create_time`) VALUES (#{id}, #{parentId}, #{type}, #{name}, #{dir}, #{fileSize}, #{createUserId}, #{createTime});"})
	public void createFile(File file);
}
