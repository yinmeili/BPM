package com.h3bpm.web.mapper;

import com.h3bpm.web.entity.Knowledge;
import com.h3bpm.web.entity.MyKnowledge;
import com.h3bpm.web.mapper.sqlprovider.KnowledgeSqlProvider;
import com.h3bpm.web.mapper.sqlprovider.MyKnowledgeSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

public interface MyKnowledgeMapper {

    @SelectProvider(type = MyKnowledgeSqlProvider.class, method = "findMyKnowledge")
    public List<MyKnowledge> findMyKnowledge(@Param("name") String name, @Param("tagName") String tagName, @Param("flowCodes") String flowCodes, @Param("startTimeStart") Date startTimeStart, @Param("startTimeEnd") Date startTimeEnd, @Param("endTimeStart") Date endTimeStart, @Param("endTimeEnd") Date endTimeEnd, @Param("userId") String userId);

    @Insert({ "INSERT INTO `h3bpm`.`ot_my_knowledge` (`id`, `type`, `name`, `desc`, `tag_name`, `create_user_id`, `create_user_name`, `create_time`, `is_delete`,`delete_time`,`flow_id`,`flow_code`, `flow_code_desc`, `start_time`,`end_time`,`desc_list_data`) VALUES (#{id}, #{type}, #{name}, #{desc}, #{tagName}, #{createUserId}, #{createUserName}, #{createTime}, #{isDelete}, #{deleteTime}, #{flowId}, #{flowCode}, #{flowCodeDesc}, #{startTime}, #{endTime}, #{descListData})" })
    public void createMyKnowledge(MyKnowledge myKnowledge);

    @Update({ "UPDATE `h3bpm`.`ot_my_knowledge` SET `type`=#{type}, `name`=#{name}, `desc`=#{desc}, `tag_name`=#{tagName}, `create_user_id`=#{createUserId}, `create_user_name`=#{createUserName},`create_time`=#{createTime}, `is_delete`=#{isDelete}, `delete_time` = #{deleteTime},`flow_id`=#{flowId}, `flow_code` = #{flowCode}, `flow_code_desc` = #{flowCodeDesc},`start_time` = #{startTime}, `end_time` = #{endTime},`desc_list_data` = #{descListData} WHERE `id`=#{id}" })
    public void updateMyKnowledge(MyKnowledge myKnowledge);

    @Select("SELECT `id`, `type`, `name`, `desc`,`tag_name` `tagName`, `create_user_id` `createUserId`, `create_user_name` `createUserName`, `create_time` `createTime`, `is_delete` `isDelete`, `flow_id` `flowId`, `flow_code` `flowCode`, `flow_code_desc` `flowCodeDesc`, `start_time` `startTime`, `end_time` `endTime`,`desc_list_data` descListData from `h3bpm`.`ot_my_knowledge` WHERE `id` = #{id}")
    public MyKnowledge getMyKnowledgeById(@Param("id") String id);

}
