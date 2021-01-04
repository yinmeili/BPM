package com.h3bpm.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.h3bpm.web.entity.BizObjectInfo;
import com.h3bpm.web.entity.User;
import com.h3bpm.web.entity.WorkFlowTask;
import com.h3bpm.web.enumeration.ApiActionUrl;
import com.h3bpm.web.enumeration.WorkflowExecuteType;
import com.h3bpm.web.mapper.UserMapper;
import com.h3bpm.web.mapper.WorkFlowMapper;
import com.h3bpm.web.mapper.WorkFlowTaskMapper;

@Service
public class WorkFlowService extends ApiDataService {

	@Autowired
	private WorkFlowTaskMapper workFlowTaskMapper;

	@Autowired
	private WorkFlowMapper workFlowMapper;

	@Autowired
	private UserMapper userMapper;

	/**
	 * 新建一个流程
	 * 
	 * @param workflowTaskId
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public String createWorkFlow(String workflowTaskId) throws ServiceException {
		WorkFlowTask workFlowTask = null;

		try {
			workFlowTask = workFlowTaskMapper.getWorkFlowTaskById(workflowTaskId);
			Map<String, Object> map = new HashMap<>();

			map.put("userCode", workFlowTask.getUserLoginName());
			map.put("finishStart", false);
			map.put("workflowCode", workFlowTask.getWorkFlowCode());
			if (workFlowTask.getParamData() != null && !workFlowTask.getParamData().isEmpty()) {
				map.put("paramValues", com.alibaba.fastjson.JSONObject.parseArray(workFlowTask.getParamData()));
			}

			Map<String, Object> tmp = this.processSyncBpm(String.format(ApiActionUrl.CREATE_WORKFLOW.getUrl(), workFlowTask.getWorkFlowCode()), ApiActionUrl.CREATE_WORKFLOW.getHttpRequestType(), map);

			if (tmp != null) {
				Map<String, Object> data = (Map<String, Object>) tmp.get("data");
				workFlowTask.setExecuteType(WorkflowExecuteType.SUCCESS.getValue());

				if (data == null || data.get("instanceId") == null) {
					return null;
				}

				workFlowTask.setInstanceId((String) data.get("instanceId"));
			}

			workFlowTask.setExecuteType(WorkflowExecuteType.SUCCESS.getValue());

		} catch (Exception e) {
			workFlowTask.setExecuteType(WorkflowExecuteType.FAIL.getValue());
			throw new ServiceException("create workflow task error!");
		}

		workFlowTaskMapper.updateWorkFlowTask(workFlowTask);

		return workFlowTask.getInstanceId();
	}

	/**
	 * 新建一个流程
	 * 
	 * @param workflowTaskId
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public String createWorkFlow(String loginName, String workflowCode, boolean finishStart, List<Map<String, Object>> paramDatas) throws ServiceException {
		String instanceId = null;
		try {
			Map<String, Object> map = new HashMap<>();

			map.put("userCode", loginName);
			map.put("finishStart", finishStart);
			map.put("workflowCode", workflowCode);
			if (paramDatas != null && !paramDatas.isEmpty()) {
				map.put("paramValues", paramDatas);
			}

			Map<String, Object> tmp = this.processSyncBpm(String.format(ApiActionUrl.CREATE_WORKFLOW.getUrl(), workflowCode), ApiActionUrl.CREATE_WORKFLOW.getHttpRequestType(), map);
			if (tmp != null) {
				Map<String, Object> data = (Map<String, Object>) tmp.get("data");
				if (data == null || data.get("instanceId") == null) {
					return null;
				}

				instanceId = (String) data.get("instanceId");
			}

		} catch (Exception e) {
			throw new ServiceException("create workflow task error!");
		}

		return instanceId;
	}

	public String activateWorkFlowNode(String workflowTaskId, String nodeCode) throws ServiceException {
		WorkFlowTask workFlowTask = null;

		try {
			workFlowTask = workFlowTaskMapper.getWorkFlowTaskById(workflowTaskId);
			Map<String, Object> map = new HashMap<>();

			map.put("instanceId", workFlowTask.getInstanceId());
			map.put("activityCode", nodeCode);

			User user = userMapper.getUserByLoginName(workFlowTask.getUserLoginName());

			if (user != null) {
				map.put("participants", new String[] { user.getId() });
			}

			if (workFlowTask.getParamData() != null && !workFlowTask.getParamData().isEmpty()) {
				map.put("paramValues", com.alibaba.fastjson.JSONObject.parseArray(workFlowTask.getParamData()));
			}

			Map<String, Object> tmp = this.processSyncBpm(String.format(ApiActionUrl.ACTIVATE_WORKFOW_NODE.getUrl(), workFlowTask.getInstanceId(), nodeCode), ApiActionUrl.ACTIVATE_WORKFOW_NODE.getHttpRequestType(), map);

			workFlowTask.setExecuteType(WorkflowExecuteType.SUCCESS.getValue());

		} catch (Exception e) {
			workFlowTask.setExecuteType(WorkflowExecuteType.FAIL.getValue());
			throw new ServiceException("activateWorkFlowNode error!");
		}

		workFlowTaskMapper.updateWorkFlowTask(workFlowTask);

		return workFlowTask.getInstanceId();
	}

	public String activateWorkFlowNode(String instanceId, String nodeCode, String loginName, List<Map<String, Object>> paramDatas) throws ServiceException {
		try {
			Map<String, Object> map = new HashMap<>();

			map.put("instanceId", instanceId);
			map.put("activityCode", nodeCode);

			User user = userMapper.getUserByLoginName(loginName);

			if (user != null) {
				map.put("participants", new String[] { user.getId() });
			}

			if (paramDatas != null && !paramDatas.isEmpty()) {
				map.put("paramValues", paramDatas);
			}

			Map<String, Object> tmp = this.processSyncBpm(String.format(ApiActionUrl.ACTIVATE_WORKFOW_NODE.getUrl(), instanceId, nodeCode), ApiActionUrl.ACTIVATE_WORKFOW_NODE.getHttpRequestType(), map);

		} catch (Exception e) {
			throw new ServiceException("activateWorkFlowNode error!");
		}

		return instanceId;
	}

	public BizObjectInfo getBizObjectInfoByInstanceId(String instanceId) {
		BizObjectInfo bizObjectInfo = workFlowMapper.getBizObjectIdByInstanceId(instanceId);
		bizObjectInfo = workFlowMapper.getBizObjectInfoByBizObjectIdAndWorkflowCode(bizObjectInfo.getId(), bizObjectInfo.getWorkflowCode());

		return bizObjectInfo;
	}
	
	public BizObjectInfo getBizObjectInfoByInstanceIdWithOutSysType(String instanceId) {
		BizObjectInfo bizObjectInfo = workFlowMapper.getBizObjectIdByInstanceId(instanceId);
		bizObjectInfo = workFlowMapper.getBizObjectInfoByBizObjectIdAndWorkflowCodeWithOutSysType(bizObjectInfo.getId(), bizObjectInfo.getWorkflowCode());

		return bizObjectInfo;
	}
}
