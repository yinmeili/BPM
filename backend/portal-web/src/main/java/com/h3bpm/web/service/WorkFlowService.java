package com.h3bpm.web.service;

import java.io.IOException;
import java.util.*;

import com.h3bpm.web.entity.LiquidationImportData;
import com.h3bpm.web.enumeration.WorkflowCode;
import com.h3bpm.web.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.h3bpm.web.entity.WorkFlowTask;
import com.h3bpm.web.enumeration.ApiActionUrl;
import com.h3bpm.web.enumeration.WorkflowExecuteType;
import com.h3bpm.web.mapper.WorkFlowTaskMapper;
import org.springframework.web.multipart.MultipartFile;

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

    public void importExcel(MultipartFile inputStream){

        List<LiquidationImportData> list = null;
        try {
            list = FileUtils.importExcel(inputStream.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list == null) return;
        for (LiquidationImportData liquidationImportData : list) {
            String userLoginName = workFlowTaskMapper.getUserLoginNameByUserDisplayName(liquidationImportData.getUserDisplayName());

            if(userLoginName == null || userLoginName.equals("")) continue;

            if(liquidationImportData.getStartTime() == null || liquidationImportData.getUserDisplayName() == null) continue;

            WorkFlowTask workFlowTask = workFlowTaskMapper.getWorkFlowTaskByWorkflowCodeAndStartTime(WorkflowCode.LIQUIDATION.getValue(), liquidationImportData.getStartTime());
            if(workFlowTask == null){
                workFlowTask = new WorkFlowTask();

                workFlowTask.setCreateTime(new Date());
                workFlowTask.setWorkFlowCode(WorkflowCode.LIQUIDATION.getValue());
                workFlowTask.setUserDisplayName(liquidationImportData.getUserDisplayName());
                workFlowTask.setStartTime(liquidationImportData.getStartTime());
                workFlowTask.setId(UUID.randomUUID().toString());
                workFlowTask.setUserLoginName(workFlowTaskMapper.getUserLoginNameByUserDisplayName(liquidationImportData.getUserDisplayName()));

                workFlowTaskMapper.createWorkFlowTask(workFlowTask);
            }
            else{
                workFlowTask.setUserDisplayName(liquidationImportData.getUserDisplayName());
                workFlowTask.setUserLoginName(workFlowTaskMapper.getUserLoginNameByUserDisplayName(liquidationImportData.getUserDisplayName()));
                workFlowTaskMapper.updateWorkFlowTask(workFlowTask);
            }
        }
    }

    public List<WorkFlowTask> findWorkFlowTask(){
        return workFlowTaskMapper.findUnFinishWorkFlowTask(new Date());
    }
}
