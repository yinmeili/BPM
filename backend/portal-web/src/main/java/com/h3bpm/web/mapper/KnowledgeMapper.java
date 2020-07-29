package com.h3bpm.web.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.h3bpm.web.entity.Knowledge;
import com.h3bpm.web.mapper.sqlprovider.KnowledgeSqlProvider;

public interface KnowledgeMapper {

	@SelectProvider(type = KnowledgeSqlProvider.class, method = "findKnowledge")
	public List<Knowledge> findKnowledge(@Param("name") String name, @Param("tagName") String tagName, @Param("flowCodes") String flowCodes, @Param("startTimeStart") Date startTimeStart, @Param("startTimeEnd") Date startTimeEnd, @Param("endTimeStart") Date endTimeStart, @Param("endTimeEnd") Date endTimeEnd);

    @Update({ "UPDATE `h3bpm`.`ot_knowledge` SET `type`=#{type}, `name`=#{name}, `desc`=#{desc}, `tag_name`=#{tagName}, `create_user_id`=#{createUserId}, `create_user_name`=#{createUserName},`create_time`=#{createTime}, `is_delete`=#{isDelete}, `delete_time` = #{deleteTime},`flow_id`=#{flowId}, `flow_code` = #{flowCode}, `flow_code_desc` = #{flowCodeDesc},`start_time` = #{startTime}, `end_time` = #{endTime} WHERE `id`=#{id}" })
    public void updateKnowledge(Knowledge knowledge);

	@Update({ "UPDATE `h3bpm`.`ot_knowledge` SET `type`=#{type}, `name`=#{name}, `desc`=#{desc}, `tag_name`=#{tagName}, `create_user_id`=#{createUserId}, `create_user_name`=#{createUserName},`create_time`=#{createTime}, `is_delete`=#{isDelete}, `delete_time` = #{deleteTime},`flow_id`=#{flowId}, `flow_code` = #{flowCode}, `flow_code_desc = #{flowCodeDesc}`,`start_time` = #{startTime}, `end_time` = #{endTime} WHERE `id`=#{id}" })
	public void updateKnowledge(Knowledge knowledge);

	@Select("SELECT `id`, `type`, `name`, `desc`,`tag_name` `tagName`, `create_user_id` `createUserId`, `create_user_name` `createUserName`, `create_time` `createTime`, `is_delete` `isDelete`, `flow_id` `flowId`, `flow_code` `flowCode`, `flow_code_desc` `flowCodeDesc`, `start_time` `startTime`, `end_time` `endTime` from `h3bpm`.`ot_knowledge` WHERE `id` = #{id}")
	public Knowledge getKnowledgeById(@Param("id") String id);

}
