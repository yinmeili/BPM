package com.h3bpm.web.mapper;

import com.h3bpm.web.entity.Tag;
import com.h3bpm.web.mapper.sqlprovider.TagSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TagMapper {

	@SelectProvider(type = TagSqlProvider.class, method = "findTagByName")
	public List<Tag> findTagByName(@Param("name") String name);

	@Insert({ "INSERT INTO `h3bpm`.`ot_tag` (`id`, `name`, `type`) VALUES (#{id}, #{name}, #{type})" })
	public void createTag(Tag tag);

}
