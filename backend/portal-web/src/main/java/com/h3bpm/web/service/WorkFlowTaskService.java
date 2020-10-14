package com.h3bpm.web.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.h3bpm.web.entity.WeeklyReportData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.h3bpm.web.entity.LiquidationImportData;
import com.h3bpm.web.entity.WorkFlowTask;
import com.h3bpm.web.enumeration.ApiActionUrl;
import com.h3bpm.web.enumeration.WorkflowCode;
import com.h3bpm.web.enumeration.WorkflowExecuteType;
import com.h3bpm.web.mapper.WorkFlowTaskMapper;
import com.h3bpm.web.utils.FileUtils;
import com.h3bpm.web.vo.KnowledgeVo;
import com.h3bpm.web.vo.WorkFlowTaskVo;
import com.h3bpm.web.vo.query.QueryWorkFlowTaskList;

@Service
public class WorkFlowTaskService extends ApiDataService {

	@Autowired
	private WorkFlowTaskMapper workFlowTaskMapper;

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
			String userLoginName = workFlowTaskMapper.getUserLoginNameByUserDisplayName(liquidationImportData.getUserDisplayName());

			if (userLoginName == null || userLoginName.equals(""))
				continue;

			if (liquidationImportData.getStartTime() == null || liquidationImportData.getUserDisplayName() == null)
				continue;

			WorkFlowTask workFlowTask = workFlowTaskMapper.getWorkFlowTaskByWorkflowCodeAndStartTime(WorkflowCode.LIQUIDATION.getValue(), liquidationImportData.getStartTime());
			if (workFlowTask == null) {
				workFlowTask = new WorkFlowTask();

				workFlowTask.setCreateTime(new Date());
				workFlowTask.setWorkFlowCode(WorkflowCode.LIQUIDATION.getValue());
				workFlowTask.setUserDisplayName(liquidationImportData.getUserDisplayName());
				workFlowTask.setStartTime(liquidationImportData.getStartTime());
				workFlowTask.setId(UUID.randomUUID().toString());
				workFlowTask.setUserLoginName(workFlowTaskMapper.getUserLoginNameByUserDisplayName(liquidationImportData.getUserDisplayName()));

				workFlowTaskMapper.createWorkFlowTask(workFlowTask);
			} else {
				workFlowTask.setUserDisplayName(liquidationImportData.getUserDisplayName());
				workFlowTask.setUserLoginName(workFlowTaskMapper.getUserLoginNameByUserDisplayName(liquidationImportData.getUserDisplayName()));
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

//        File file = new File("src/main/resources/config/files/weeklyReport.xlsx");
		InputStream is = null;
		List<WeeklyReportData> list = null;
		try {
//            list = FileUtils.addWorkFlowTask(new FileInputStream(file));
//            is = WorkFlowService.class.getClassLoader().getResource("config/files/weeklyReport.xlsx").openStream();
			is = this.getClass().getClassLoader().getResourceAsStream("config/files/weeklyReport.xlsx");
//            is = this.getClass().getResourceAsStream("config/files/weeklyReport.xlsx");
			list = FileUtils.importWeeklyReportWorkFlowTask(is);
//        } catch (IOException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (list == null) return;
		for (WeeklyReportData weeklyReportData : list) {
			String userLoginName = workFlowTaskMapper.getUserLoginNameByUserDisplayName(weeklyReportData.getUserDisplayName());

			if (userLoginName == null || userLoginName.equals("")) continue;

			if (weeklyReportData.getUserDisplayName() == null) continue;

			WorkFlowTask workFlowTask = new WorkFlowTask();

			workFlowTask.setId(UUID.randomUUID().toString());
			workFlowTask.setCreateTime(new Date());
			workFlowTask.setWorkFlowCode(WorkflowCode.WEEKLY_REPORT.getValue());
			workFlowTask.setUserDisplayName(weeklyReportData.getUserDisplayName());
			workFlowTask.setStartTime(new Date());
			workFlowTask.setUserLoginName(userLoginName);

			workFlowTaskMapper.createWorkFlowTask(workFlowTask);

		}
	}
}
