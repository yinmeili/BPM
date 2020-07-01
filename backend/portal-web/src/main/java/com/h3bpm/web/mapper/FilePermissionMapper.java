package com.h3bpm.web.mapper;

import com.h3bpm.web.entity.FilePermission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface FilePermissionMapper {

	@Select("SELECT file_id fileId,orgs,users FROM `ot_file_permission` where file_id = #{fileId}")
	public FilePermission getFilePermissionByFileId(@Param("fileId") String fileId);
	
	@Insert({"INSERT INTO `ot_file_permission` (`file_id`, `orgs`, `users`) VALUES (#{fileId}, #{orgs}, #{users})"})
	public void createFilePermission(FilePermission filePermission);

//	@Insert({"INSERT INTO `h3bpm`.`ot_file_permission` (`file_id`) VALUES (#{fileId})"})
//	public void createFilePermissionTest(@Param("fileId") String fileId);//测试
	
	@Delete({"DELETE FROM `ot_file_permission` WHERE file_id = '#{fileId}'"})
	public void deleteFilePermissionByFileId(@Param("fileId") String fileId);

}
