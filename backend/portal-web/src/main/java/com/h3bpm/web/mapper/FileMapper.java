package com.h3bpm.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.h3bpm.web.entity.File;
import com.h3bpm.web.mapper.sqlprovider.FileSqlProvider;

public interface FileMapper {

	@SelectProvider(type = FileSqlProvider.class, method = "findFileByParentIdAndKeyword")
	public List<File> findFileByParentIdAndKeyword(@Param("parentId") String parentId, @Param("keyword") String keyword);

	@Select("SELECT `id`, `parent_id` parentId, `type`, `name`, `dir`, `file_size` fileSize, `create_user_id` createUserId, `create_time` createTime, `is_delete` isDelete FROM `ot_file` where id = #{id}")
	public File getFileById(@Param("id") String id);

	@Insert({ "INSERT INTO `h3bpm`.`ot_file` (`id`, `parent_id`, `type`, `name`, `dir`, `file_size`, `create_user_id`, `create_time`, `is_delete`) VALUES (#{id}, #{parentId}, #{type}, #{name}, #{dir}, #{fileSize}, #{createUserId}, #{createTime}, #{isDelete})" })
	public void createFile(File file);

	@Update({ "UPDATE `h3bpm`.`ot_file` SET `parent_id`=#{parentId}, `type`=#{type}, `name`=#{name}, `dir`=#{dir}, `file_size`=#{fileSize}, `create_user_id`=#{createUserId}, `create_time`=#{createTime}, `is_delete`=#{isDelete} ,`delete_time` = #{deleteTime} WHERE `id`=#{id}" })
	public void updateFile(File file);
}
