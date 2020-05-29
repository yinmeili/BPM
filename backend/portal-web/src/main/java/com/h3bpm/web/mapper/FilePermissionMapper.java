package com.h3bpm.web.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.h3bpm.web.entity.FilePermission;

public interface FilePermissionMapper {

	@Select("SELECT file_id fileId,orgs,users FROM `ot_file_permission` where file_id = #{fileId}")
	public FilePermission getFilePermissionByFileId(@Param("fileId") String fileId);
	
	@Insert({"INSERT INTO `ot_file_permission` (`file_id`, `orgs`, `users`) VALUES (#{fileId}, #{orgs}, #{users})"})
	public void createFilePermission(FilePermission filePermission);
	
	@Delete({"DELETE FROM `ot_file_permission` WHERE file_id = ${fileId}"})
	public void deleteFilePermissionByFileId(@Param("fileId") String fileId);
}
