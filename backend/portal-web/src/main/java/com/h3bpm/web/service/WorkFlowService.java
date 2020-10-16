package com.h3bpm.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.h3bpm.web.entity.WorkFlowTask;
import com.h3bpm.web.enumeration.ApiActionUrl;
import com.h3bpm.web.enumeration.WorkflowExecuteType;
import com.h3bpm.web.mapper.WorkFlowTaskMapper;

@Service
public class WorkFlowService extends ApiDataService{

    @Autowired
    private WorkFlowTaskMapper workFlowTaskMapper;

    /**
     * 新建一个流程
     * @param id
     */
    @SuppressWarnings("unchecked")
	public String createWorkFlow(String id){
        WorkFlowTask workFlowTask = null;

        try{
            workFlowTask = workFlowTaskMapper.getWorkFlowTaskById(id);
            Map<String,Object> map = new HashMap<>();

            map.put("userCode",workFlowTask.getUserLoginName());
            map.put("finishStart",false);
            map.put("workflowCode",workFlowTask.getWorkFlowCode());

            Map<String, Object> tmp = this.processSyncBpm(String.format(ApiActionUrl.CREATE_WORKFLOW.getUrl(),workFlowTask.getWorkFlowCode()), ApiActionUrl.CREATE_WORKFLOW.getHttpRequestType(), map);

            if(tmp != null){
                Map<String,Object> data = (Map<String,Object>)tmp.get("data");
                workFlowTask.setExecuteType(WorkflowExecuteType.SUCCESS.getValue());
                workFlowTask.setInstanceId((String)data.get("instanceId"));
            }
            workFlowTaskMapper.updateWorkFlowTask(workFlowTask);
            return workFlowTask.getInstanceId();

        }catch(Exception e){
            workFlowTask.setExecuteType(WorkflowExecuteType.FAIL.getValue());
            e.printStackTrace();
        }

        return null;
    }
}
