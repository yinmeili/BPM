package com.h3bpm.web.mapper;

import com.h3bpm.web.entity.File;
import com.h3bpm.web.mapper.sqlprovider.FileSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MyFileMapper {

	@SelectProvider(type = FileSqlProvider.class, method = "findMyFileByParentIdAndKeyword")
	public List<File> findMyFileByParentIdAndKeyword(@Param("parentId") String parentId, @Param("keyword") String keyword, @Param("userId") String userId);

	//根据用户id和已删除状态获取文件，author:lhl
	@SelectProvider(type = FileSqlProvider.class, method = "findDeletedMyFileByUserId")
	public List<File> findDeletedMyFileByUserId(@Param("userId") String userId);

	@Select("SELECT `id`, `parent_id` parentId, `type`, `name`, `dir`, `file_size` fileSize, `create_user_id` createUserId, `create_time` createTime, `is_delete` isDelete, `download_file_id` downloadFileId FROM `ot_my_file` where id = #{id}")
	public File getMyFileById(@Param("id") String id);
	
	@Select("SELECT `id`, `parent_id` parentId, `type`, `name`, `dir`, `file_size` fileSize, `create_user_id` createUserId, `create_time` createTime, `is_delete` isDelete, `download_file_id` downloadFileId FROM `ot_my_file` where dir = #{path}")
	public File getMyFileByPath(@Param("path") String path);

	@Insert({ "INSERT INTO `h3bpm`.`ot_my_file` (`id`, `parent_id`, `type`, `name`, `dir`, `file_size`, `create_user_id`, `create_time`, `is_delete`,`download_file_id`) VALUES (#{id}, #{parentId}, #{type}, #{name}, #{dir}, #{fileSize}, #{createUserId}, #{createTime}, #{isDelete}, #{downloadFileId})" })
	public void createMyFile(File file);

	@Update({ "UPDATE `h3bpm`.`ot_my_file` SET `parent_id`=#{parentId}, `type`=#{type}, `name`=#{name}, `dir`=#{dir}, `file_size`=#{fileSize}, `create_user_id`=#{createUserId}, `create_time`=#{createTime}, `is_delete`=#{isDelete} ,`delete_time` = #{deleteTime},`download_file_id`=#{downloadFileId} WHERE `id`=#{id}" })
	public void updateMyFile(File file);

}
