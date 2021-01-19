package com.h3bpm.web.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.h3bpm.web.entity.LiquidationImportData;
import com.h3bpm.web.entity.User;
import com.h3bpm.web.entity.WeeklyReportData;
import com.h3bpm.web.entity.WorkFlowTask;
import com.h3bpm.web.enumeration.WorkflowCode;
import com.h3bpm.web.mapper.UserMapper;
import com.h3bpm.web.mapper.WorkFlowTaskMapper;
import com.h3bpm.web.utils.FileUtils;
import com.h3bpm.web.vo.ReqUpdateWorkFlowTaskVo;
import com.h3bpm.web.vo.WorkFlowTaskVo;
import com.h3bpm.web.vo.query.QueryWorkFlowTaskList;

import OThinker.Common.DateTimeUtil;

@Service
public class WorkFlowTaskService extends ApiDataService {

	@Autowired
	private WorkFlowTaskMapper workFlowTaskMapper;

	@Autowired
	private UserMapper userMapper;

	@Value(value = "${application.conf.path}")
	private String CONF_PATH = null;

	@Value(value = "${application.weeklyReport.filePath}")
	private String WEEKLY_REPORT_PATH = null;

	/**
	 * 导入清算报表
	 * 
	 * @param inputStream
	 */
	public void importLiquidationExcel(MultipartFile inputStream) {

		List<LiquidationImportData> list = null;
		try {
			list = FileUtils.importExcel(inputStream.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (list == null)
			return;
		for (LiquidationImportData liquidationImportData : list) {
			String userLoginName = userMapper.getUserLoginNameByUserDisplayName(liquidationImportData.getUserDisplayName());

			if (userLoginName == null || userLoginName.equals(""))
				continue;

			if (liquidationImportData.getStartTime() == null || liquidationImportData.getUserDisplayName() == null)
				continue;

			// 每天10点开始发起清算任务
			Date workflowTaskStartTime = liquidationImportData.getStartTime();
			workflowTaskStartTime = DateTimeUtil.addHours(workflowTaskStartTime, 10);
			// workflowTaskStartTime = DateTimeUtil.addMinutes(workflowTaskStartTime, 50);

			WorkFlowTask workFlowTask = workFlowTaskMapper.getWorkFlowTaskByWorkflowCodeAndStartTime(WorkflowCode.LIQUIDATION.getValue(), workflowTaskStartTime);

			if (workFlowTask == null) {
				workFlowTask = new WorkFlowTask();

				workFlowTask.setCreateTime(new Date());
				workFlowTask.setWorkFlowCode(WorkflowCode.LIQUIDATION.getValue());
				workFlowTask.setUserDisplayName(liquidationImportData.getUserDisplayName());
				workFlowTask.setStartTime(workflowTaskStartTime);
				workFlowTask.setId(UUID.randomUUID().toString());
				workFlowTask.setUserLoginName(userMapper.getUserLoginNameByUserDisplayName(liquidationImportData.getUserDisplayName()));

				workFlowTaskMapper.createWorkFlowTask(workFlowTask);

			} else {

				workFlowTask.setUserDisplayName(liquidationImportData.getUserDisplayName());
				workFlowTask.setUserLoginName(userMapper.getUserLoginNameByUserDisplayName(liquidationImportData.getUserDisplayName()));
				workFlowTaskMapper.updateWorkFlowTask(workFlowTask);
			}
		}
	}

	/**
	 * 分页查询流程任务信息
	 * 
	 * @param queryBean
	 */
	public PageInfo<WorkFlowTaskVo> findWorkFlowTaskByPage(QueryWorkFlowTaskList queryBean) {
		Page<WorkFlowTask> page = PageHelper.startPage(queryBean.getPageNum(), queryBean.getiDisplayLength());
		List<WorkFlowTask> workFlowTaskList = workFlowTaskMapper.findWorkFlowTask(queryBean.getUserDisplayName(), queryBean.getFlowCode(), queryBean.getStartTimeStart(), queryBean.getStartTimeEnd());

		List<WorkFlowTaskVo> workFlowTaskVoList = new ArrayList<WorkFlowTaskVo>();
		if (workFlowTaskList != null) {
			for (WorkFlowTask workFlowTask : workFlowTaskList) {
				workFlowTaskVoList.add(new WorkFlowTaskVo(workFlowTask));
			}
		}
		PageInfo<WorkFlowTaskVo> pageInfo = new PageInfo<WorkFlowTaskVo>(workFlowTaskVoList);
		pageInfo.setTotal(page.getTotal());

		return pageInfo;
	}

	public List<WorkFlowTask> findUnFinishWorkFlowTask() {
		return workFlowTaskMapper.findUnFinishWorkFlowTask(new Date());
	}

	public void addWeeklyReportWorkFlowTask() {
		InputStream is = null;
		List<WeeklyReportData> list = null;
		try {
			// is = this.getClass().getClassLoader().getResourceAsStream("config/files/weeklyReport.xlsx");
			is = new FileInputStream(new File(CONF_PATH + WEEKLY_REPORT_PATH));
			list = FileUtils.importWeeklyReportWorkFlowTask(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (list == null)
			return;
		for (WeeklyReportData weeklyReportData : list) {
			String userLoginName = userMapper.getUserLoginNameByUserDisplayName(weeklyReportData.getUserDisplayName().trim());

			if (userLoginName == null || userLoginName.equals(""))
				continue;

			if (weeklyReportData.getUserDisplayName() == null)
				continue;

			WorkFlowTask workFlowTask = new WorkFlowTask();

			workFlowTask.setId(UUID.randomUUID().toString());
			workFlowTask.setCreateTime(new Date());
			workFlowTask.setWorkFlowCode(WorkflowCode.WEEKLY_REPORT.getValue());
			workFlowTask.setUserDisplayName(weeklyReportData.getUserDisplayName().trim());
			workFlowTask.setStartTime(new Date());
			workFlowTask.setUserLoginName(userLoginName);

			workFlowTaskMapper.createWorkFlowTask(workFlowTask);

		}
	}

	public void updateWorkFlowTaskUser(ReqUpdateWorkFlowTaskVo voBean) throws ServiceException {
		WorkFlowTask workFlowTask = workFlowTaskMapper.getWorkFlowTaskById(voBean.getId());
		String loginName = userMapper.getUserLoginNameByUserDisplayName(voBean.getUserDisplayName());

		if (loginName == null) {
			throw new ServiceException("该用户不存在");
		}

		User user = userMapper.getUserByLoginName(loginName);

		workFlowTask.setUserLoginName(user.getLoginName());
		workFlowTask.setUserDisplayName(user.getName());

		workFlowTaskMapper.updateWorkFlowTask(workFlowTask);
	}
}
