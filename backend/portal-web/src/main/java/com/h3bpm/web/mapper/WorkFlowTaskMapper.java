package com.h3bpm.web.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.h3bpm.web.entity.WorkFlowTask;
import com.h3bpm.web.mapper.sqlprovider.WorkFlowTaskSqlProvider;

public interface WorkFlowTaskMapper {
    @Insert({ "INSERT INTO `h3bpm`.`ot_workflow_task` (`id`, `instance_id`, `workflow_code`, `user_login_name`, `user_display_name`, `create_time`,  `start_time`, `end_time`, `execute_type`, `param_data`) VALUES (#{id}, #{instanceId}, #{workflowCode}, #{userLoginName}, #{userDisplayName}, #{createTime}, #{startTime}, #{endTime}, #{executeType}, #{paramData})" })
    public void createWorkFlowTask(WorkFlowTask workFlowTask);
   
    @Update({ "UPDATE `h3bpm`.`ot_workflow_task` SET `instance_id`=#{instanceId}, `workflow_code`=#{workflowCode}, `user_login_name`=#{userLoginName}, `user_display_name`=#{userDisplayName}, `create_time`=#{createTime}, `start_time`=#{startTime}, `end_time`=#{endTime}, `execute_type`=#{executeType} ,`param_data` = #{paramData} WHERE `id`=#{id}" })
    public void updateWorkFlowTask(WorkFlowTask workFlowTask);

    @Select("SELECT `id`, `instance_id` `instanceId`, `workflow_code` `workflowCode`, `user_login_name` `userLoginName`, `user_display_name` `userDisplayName`, `create_time` `createTime`, `start_time` `startTime`, `end_time` `endTime`, `execute_type` `executeType`, `param_data` `paramData` FROM `ot_workflow_task` where `id` = #{id}")
    public WorkFlowTask getWorkFlowTaskById(@Param("id") String id);

    @SelectProvider(type = WorkFlowTaskSqlProvider.class, method = "findUnFinishWorkFlowTask")
    public List<WorkFlowTask> findUnFinishWorkFlowTask(@Param("startTime") Date startTime);

    @Select("SELECT `id`, `instance_id` `instanceId`, `workflow_code` `workflowCode`, `user_login_name` `userLoginName`, `user_display_name` `userDisplayName`, `create_time` `createTime`, `start_time` `startTime`, `end_time` `endTime`, `execute_type` `executeType`, `param_data` `paramData` FROM `ot_workflow_task` where `workflow_code` = #{workflowCode} AND `start_time` = #{startTime}")
    public WorkFlowTask getWorkFlowTaskByWorkflowCodeAndStartTime(@Param("workflowCode") String workflowCode,@Param("startTime") Date startTime);
    
    @SelectProvider(type = WorkFlowTaskSqlProvider.class, method = "findWorkFlowTask")
	public List<WorkFlowTask> findWorkFlowTask(@Param("userDisplayName") String userDisplayName,  @Param("flowCode") String flowCode, @Param("startTimeStart") Date startTimeStart, @Param("startTimeEnd") Date startTimeEnd);
}
