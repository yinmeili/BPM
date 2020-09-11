package com.h3bpm.web.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.h3bpm.web.entity.File;
import com.h3bpm.web.mapper.sqlprovider.FileSqlProvider;
import com.h3bpm.web.mapper.sqlprovider.MyFileSqlProvider;

public interface MyFileMapper {

	@SelectProvider(type = MyFileSqlProvider.class, method = "findMyFileByParentIdAndKeyword")
	public List<File> findMyFileByParentIdAndKeyword(@Param("parentId") String parentId, @Param("keyword") String keyword, @Param("searchPath") String searchPath, @Param("userId") String userId);

	//根据用户id和已删除状态获取文件，author:lhl
	@SelectProvider(type = MyFileSqlProvider.class, method = "findDeletedMyFileByUserId")
	public List<File> findDeletedMyFileByUserId(@Param("userId") String userId);

	@Select("SELECT `id`, `parent_id` parentId, `type`, `name`, `dir`, `file_size` fileSize, `create_user_id` createUserId, `create_time` createTime, `is_delete` isDelete, `download_file_id` downloadFileId FROM `ot_my_file` where id = #{id}")
	public File getMyFileById(@Param("id") String id);
	
	@Select("SELECT `id`, `parent_id` parentId, `type`, `name`, `dir`, `file_size` fileSize, `create_user_id` createUserId, `create_time` createTime, `is_delete` isDelete, `download_file_id` downloadFileId FROM `ot_my_file` where dir = #{path} AND is_delete=0")
	public File getMyFileByPath(@Param("path") String path);

	@Insert({ "INSERT INTO `h3bpm`.`ot_my_file` (`id`, `parent_id`, `type`, `name`, `dir`, `file_size`, `create_user_id`, `create_time`, `is_delete`,`download_file_id`) VALUES (#{id}, #{parentId}, #{type}, #{name}, #{dir}, #{fileSize}, #{createUserId}, #{createTime}, #{isDelete}, #{downloadFileId})" })
	public void createMyFile(File file);

	@Update({ "UPDATE `h3bpm`.`ot_my_file` SET `parent_id`=#{parentId}, `type`=#{type}, `name`=#{name}, `dir`=#{dir}, `file_size`=#{fileSize}, `create_user_id`=#{createUserId}, `create_time`=#{createTime}, `is_delete`=#{isDelete} ,`delete_time` = #{deleteTime},`download_file_id`=#{downloadFileId} WHERE `id`=#{id}" })
	public void updateMyFile(File file);

	@Select("SELECT `id`, `parent_id` parentId, `type`, `name`, `dir`, `file_size` fileSize, `create_user_id` createUserId, `create_time` createTime, `is_delete` isDelete, `delete_time` `deleteTime`, `download_file_id` downloadFileId FROM `ot_my_file` where `is_delete` = 1 order by delete_time asc")
	public List<File> findDeleteMyFile();

	@Select("SELECT `id`, `parent_id` parentId, `type`, `name`, `dir`, `file_size` fileSize, `create_user_id` createUserId, `create_time` createTime, `is_delete` isDelete, `delete_time` `deleteTime`, `download_file_id` downloadFileId FROM `ot_my_file` WHERE is_delete != 2 order by delete_time asc")
	public List<File> findAllNotCompleteDeletedMyFile();

	@Select("SELECT `id`, `parent_id` parentId, `type`, `name`, `dir`, `file_size` fileSize, `create_user_id` createUserId, `create_time` createTime, `is_delete` isDelete, `delete_time` `deleteTime`, `download_file_id` downloadFileId FROM `ot_my_file` where download_file_id = #{downloadFileId}")
	public List<File> findMyFileByDownloadFileId(@Param("downloadFileId") String downloadFileId);

	//根据download_file_id获取未被删除的我的文件
	@Select("SELECT COUNT(*) FROM `ot_my_file` where is_delete = 0 AND download_file_id = #{downloadFileId}")
	public int getCountOfUnDeletedMyFileByDownloadFileId(@Param("downloadFileId") String downloadFileId);

	//查找回收站中delete_time 超期的文件
	@Select("SELECT `id`, `parent_id` parentId, `type`, `name`, `dir`, `file_size` fileSize, `create_user_id` createUserId, `create_time` createTime, `is_delete` isDelete, `delete_time` `deleteTime`, `download_file_id` downloadFileId FROM `ot_my_file` where is_delete = 1 AND delete_time < #{deleteTime}")
	public List<File> findDeleteMyFileByDeleteTime(@Param("deleteTime") Date deleteTime);

	//根据download_file_id查找回收站中 还可以保留的文件的数量
	@Select("SELECT COUNT(*) FROM `ot_my_file` where is_delete = 1 AND download_file_id = #{downloadFileId} AND delete_time > #{deleteTime}")
	public int getCountOfSaveMyFileByDownloadFileId(@Param("downloadFileId") String downloadFileId, @Param("deleteTime") Date deleteTime);

}
