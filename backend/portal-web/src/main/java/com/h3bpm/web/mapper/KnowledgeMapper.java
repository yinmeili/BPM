package com.h3bpm.web.mapper;

import java.util.Date;
import java.util.List;

import com.h3bpm.web.entity.FlowCode;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.h3bpm.web.entity.Knowledge;
import com.h3bpm.web.mapper.sqlprovider.KnowledgeSqlProvider;

public interface KnowledgeMapper {

	@SelectProvider(type = KnowledgeSqlProvider.class, method = "findKnowledge")
	public List<Knowledge> findKnowledge(@Param("name") String name, @Param("tagName") String tagName, @Param("flowCodes") String flowCodes, @Param("startTimeStart") Date startTimeStart, @Param("startTimeEnd") Date startTimeEnd, @Param("endTimeStart") Date endTimeStart, @Param("endTimeEnd") Date endTimeEnd, @Param("userId") String userId, @Param("userAllParentIds") List<String> userAllParentIds);

	/**
	 * 查询当前用户所有已删除的知识列表
	 * @param
	 */
	@SelectProvider(type = KnowledgeSqlProvider.class, method = "findDeleteKnowledgeByUserId")
	public List<Knowledge> findDeleteKnowledgeByUserId(@Param("name") String name, @Param("tagName") String tagName, @Param("flowCodes") String flowCodes, @Param("startTimeStart") Date startTimeStart, @Param("startTimeEnd") Date startTimeEnd, @Param("endTimeStart") Date endTimeStart, @Param("endTimeEnd") Date endTimeEnd, @Param("userId") String userId);

	@Insert({ "INSERT INTO `h3bpm`.`ot_knowledge` (`id`, `type`, `name`, `desc`, `tag_name`, `create_user_id`, `create_user_name`, `create_time`, `is_delete`,`delete_time`,`flow_id`,`flow_code`, `flow_code_desc`, `start_time`,`end_time`,`status`,`desc_list_data`) VALUES (#{id}, #{type}, #{name}, #{desc}, #{tagName}, #{createUserId}, #{createUserName}, #{createTime}, #{isDelete}, #{deleteTime}, #{flowId}, #{flowCode}, #{flowCodeDesc}, #{startTime}, #{endTime}, #{status}, #{descListData})" })
	public void createKnowledge(Knowledge knowledge);
	
    @Update({ "UPDATE `h3bpm`.`ot_knowledge` SET `type`=#{type}, `name`=#{name}, `desc`=#{desc}, `tag_name`=#{tagName}, `create_user_id`=#{createUserId}, `create_user_name`=#{createUserName},`create_time`=#{createTime}, `is_delete`=#{isDelete}, `delete_time` = #{deleteTime},`flow_id`=#{flowId}, `flow_code` = #{flowCode}, `flow_code_desc` = #{flowCodeDesc},`start_time` = #{startTime}, `end_time` = #{endTime}, `status` = #{status}, `desc_list_data` = #{descListData} WHERE `id`=#{id}" })
    public void updateKnowledge(Knowledge knowledge);

	@Select("SELECT `id`, `type`, `name`, `desc`,`tag_name` `tagName`, `create_user_id` `createUserId`, `create_user_name` `createUserName`, `create_time` `createTime`, `is_delete` `isDelete`, `flow_id` `flowId`, `flow_code` `flowCode`, `flow_code_desc` `flowCodeDesc`, `start_time` `startTime`, `end_time` `endTime`, `status` `status` , `desc_list_data` `descListData` from `h3bpm`.`ot_knowledge` WHERE `id` = #{id}")
	public Knowledge getKnowledgeById(@Param("id") String id);

	@Select("select distinct b.`WorkflowCode` `flowCode`,c.`WorkflowName` `flowCodeDesc` from `ot_workitemfinished` b \n" +
			"inner join `ot_workflowclause` c on b.`WorkflowCode` = c.`WorkflowCode` where b.ObjectID = #{flowId}")
	public FlowCode getFlowCodeByFlowId(@Param("flowId") String flowId);

}
