package com.h3bpm.web.mapper;

import com.h3bpm.web.entity.File;
import com.h3bpm.web.mapper.sqlprovider.FileSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

public interface FileMapper {

	@SelectProvider(type = FileSqlProvider.class, method = "findFileByParentIdAndKeyword")
	public List<File> findFileByParentIdAndKeyword(@Param("parentId") String parentId, @Param("keyword") String keyword, @Param("searchPath") String searchPath);

	/*
	 * 根据用户id和已删除状态获取文件，author:lhl
	 */
	@SelectProvider(type = FileSqlProvider.class, method = "findDeletedFileByUserId")
	public List<File> findDeletedFileByUserId(@Param("userId") String userId);

	@SelectProvider(type = FileSqlProvider.class, method = "findDeletedMyFileByUserId")
	public List<File> findDeletedMyFileByUserId(@Param("userId") String userId);

	@Select("SELECT `id`, `parent_id` parentId, `type`, `name`, `dir`, `file_size` fileSize, `create_user_id` createUserId, `create_time` createTime, `is_delete` isDelete, `download_file_id` downloadFileId FROM `ot_file` where id = #{id}")
	public File getFileById(@Param("id") String id);
	
	@Select("SELECT `id`, `parent_id` parentId, `type`, `name`, `dir`, `file_size` fileSize, `create_user_id` createUserId, `create_time` createTime, `is_delete` isDelete, `download_file_id` downloadFileId FROM `ot_file` where dir = #{path} AND is_delete=0")
	public File getFileByPath(@Param("path") String path);

	@Insert({ "INSERT INTO `h3bpm`.`ot_file` (`id`, `parent_id`, `type`, `name`, `dir`, `file_size`, `create_user_id`, `create_time`, `is_delete`,`download_file_id`) VALUES (#{id}, #{parentId}, #{type}, #{name}, #{dir}, #{fileSize}, #{createUserId}, #{createTime}, #{isDelete}, #{downloadFileId})" })
	public void createFile(File file);

	@Update({ "UPDATE `h3bpm`.`ot_file` SET `parent_id`=#{parentId}, `type`=#{type}, `name`=#{name}, `dir`=#{dir}, `file_size`=#{fileSize}, `create_user_id`=#{createUserId}, `create_time`=#{createTime}, `is_delete`=#{isDelete} ,`delete_time` = #{deleteTime},`download_file_id`=#{downloadFileId} WHERE `id`=#{id}" })
	public void updateFile(File file);

	@SelectProvider(type = FileSqlProvider.class, method = "findFileNameByParentId")
	public List<String> findFileNameByParentId(@Param("parentId") String parentId);

	@SelectProvider(type = FileSqlProvider.class, method = "findFolderNameByParentId")
	public List<String> findFolderNameByParentId(@Param("parentId") String parentId);

	//根据download_file_id获取未被删除的共享文件
	@Select("SELECT COUNT(*) FROM `ot_file` where is_delete = 0 AND download_file_id = #{downloadFileId}")
	public int getCountOfUnDeletedFileByDownloadFileId(@Param("downloadFileId") String downloadFileId);

	//查找回收站中delete_time 超期的文件
	@Select("SELECT `id`, `parent_id` parentId, `type`, `name`, `dir`, `file_size` fileSize, `create_user_id` createUserId, `create_time` createTime, `is_delete` isDelete, `delete_time` `deleteTime`, `download_file_id` downloadFileId FROM `ot_file` where is_delete = 1 AND delete_time < #{deleteTime}")
	public List<File> findDeleteFileByDeleteTime(@Param("deleteTime") Date deleteTime);

	//根据download_file_id查找回收站中 还可以保留的文件的数量
	@Select("SELECT COUNT(*) FROM `ot_file` where is_delete = 1 AND download_file_id = #{downloadFileId} AND delete_time > #{deleteTime}")
	public int getCountOfSaveFileByDownloadFileId(@Param("downloadFileId") String downloadFileId, @Param("deleteTime") Date deleteTime);

}
