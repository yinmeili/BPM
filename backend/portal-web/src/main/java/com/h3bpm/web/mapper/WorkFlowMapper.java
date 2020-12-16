package com.h3bpm.web.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.h3bpm.web.entity.BizObjectInfo;
import com.h3bpm.web.mapper.sqlprovider.WorkFlowSqlProvider;

public interface WorkFlowMapper {
	@Select("SELECT BizObjectId id, WorkflowCode workflowCode FROM `h3bpm`.`ot_instancecontext` where ObjectID=#{instanceId}")
	public BizObjectInfo getBizObjectIdByInstanceId(String instanceId);

	@SelectProvider(type = WorkFlowSqlProvider.class, method = "getBizObjectInfoByIdAndWorkflowCode")
	public BizObjectInfo getBizObjectInfoByBizObjectIdAndWorkflowCode(@Param("id") String id, @Param("workflowCode") String workflowCode);

}
