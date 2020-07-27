package com.h3bpm.web.mapper;

import com.h3bpm.web.entity.Tag;
import com.h3bpm.web.mapper.sqlprovider.TagSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TagMapper {

	/**
	 * 查找指定type下的like name
	 * @param name
	 * @param type
	 * @return
	 */
	@SelectProvider(type = TagSqlProvider.class, method = "findTagByTypeAndName")
	public List<Tag> findTagByTypeAndName(@Param("name") String name, @Param("type") String type);

	/**
	 * 查找指定type下的指定name
	 * @param name
	 * @param type
	 * @return
	 */
	@Select("SELECT `id`, `name`, `type` FROM `ot_tag` where name = #{name} AND type = #{type}")
	public Tag getTagByTypeAndName(@Param("name") String name, @Param("type") String type);

	@Insert({ "INSERT INTO `h3bpm`.`ot_tag` (`id`, `name`, `type`) VALUES (#{id}, #{name}, #{type})" })
	public void createTag(Tag tag);

}
