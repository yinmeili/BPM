package com.h3bpm.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.h3bpm.web.entity.User;
import com.h3bpm.web.entity.WeeklyReportJob;
import com.h3bpm.web.entity.WeeklyReportProject;
import com.h3bpm.web.mapper.WeeklyReportMapper;
import com.h3bpm.web.vo.WeeklyReportJobVo;
import com.h3bpm.web.vo.WeeklyReportProjectVo;

@Service
public class WeeklyReportService extends ApiDataService {

	@Autowired
	private WeeklyReportMapper weeklyReportMapper;

	@Autowired
	private OrgService orgService;

	/**
	 * 查找部门下所有人员常规工作周报
	 * 
	 * @param id
	 * @throws ServiceException
	 */
	public List<WeeklyReportJobVo> findWeeklyReportJobByOrgId(String orgId, Date startTime, Date endTime, int jobLevel) throws ServiceException {
		List<WeeklyReportJobVo> weeklyReportVoJobs = new ArrayList<WeeklyReportJobVo>();
		List<WeeklyReportJob> weeklyReportJobs = new ArrayList<WeeklyReportJob>();
		try {
			List<User> userList = orgService.findChildrenUserByOrgId(orgId);

			if (userList != null) {

				for (User user : userList) {
					List<WeeklyReportJob> userJobs = weeklyReportMapper.findWeeklyReportJob(user.getId(), startTime, endTime, jobLevel);

					if (userJobs != null && !userJobs.isEmpty()) {
						weeklyReportJobs.addAll(userJobs);
					}
				}

			}

		} catch (Exception e) {
			throw new ServiceException("findWeeklyReportJobByOrgId service error!", e);
		}

		if (weeklyReportJobs != null && !weeklyReportJobs.isEmpty()) {
			for (WeeklyReportJob weeklyReportJob : weeklyReportJobs) {
				weeklyReportVoJobs.add(new WeeklyReportJobVo(weeklyReportJob));
			}
		}

		return weeklyReportVoJobs;
	}
	
	/**
	 * 查找部门下所有人员项目工作周报
	 * 
	 * @param id
	 * @throws ServiceException
	 */
	public List<WeeklyReportProjectVo> findWeeklyReportProjectByOrgId(String orgId, Date startTime, Date endTime) throws ServiceException {
		List<WeeklyReportProjectVo> weeklyReportVoProjectes = new ArrayList<WeeklyReportProjectVo>();
		List<WeeklyReportProject> weeklyReportProjectes = new ArrayList<WeeklyReportProject>();
		try {
			List<User> userList = orgService.findChildrenUserByOrgId(orgId);

			if (userList != null) {

				for (User user : userList) {
					List<WeeklyReportProject> userProjectes = weeklyReportMapper.findWeeklyReportProject(user.getId(), startTime, endTime);

					if (userProjectes != null && !userProjectes.isEmpty()) {
						weeklyReportProjectes.addAll(userProjectes);
					}
				}

			}

		} catch (Exception e) {
			throw new ServiceException("findWeeklyReportProjectByOrgId service error!", e);
		}

		if (weeklyReportProjectes != null && !weeklyReportProjectes.isEmpty()) {
			for (WeeklyReportProject weeklyReportProject : weeklyReportProjectes) {
				weeklyReportVoProjectes.add(new WeeklyReportProjectVo(weeklyReportProject));
			}
		}

		return weeklyReportVoProjectes;
	}
	
	/**
	 * 查找部门下所有人员常规工作计划周报
	 * 
	 * @param id
	 * @throws ServiceException
	 */
	public List<WeeklyReportJobVo> findWeeklyReportJobPlanByOrgId(String orgId, Date startTime, Date endTime, int jobLevel) throws ServiceException {
		List<WeeklyReportJobVo> weeklyReportVoJobs = new ArrayList<WeeklyReportJobVo>();
		List<WeeklyReportJob> weeklyReportJobs = new ArrayList<WeeklyReportJob>();
		try {
			List<User> userList = orgService.findChildrenUserByOrgId(orgId);

			if (userList != null) {

				for (User user : userList) {
					List<WeeklyReportJob> userJobs = weeklyReportMapper.findWeeklyReportJobPlan(user.getId(), startTime, endTime, jobLevel);

					if (userJobs != null && !userJobs.isEmpty()) {
						weeklyReportJobs.addAll(userJobs);
					}
				}

			}

		} catch (Exception e) {
			throw new ServiceException("findWeeklyReportJobPlanByOrgId service error!", e);
		}

		if (weeklyReportJobs != null && !weeklyReportJobs.isEmpty()) {
			for (WeeklyReportJob weeklyReportJob : weeklyReportJobs) {
				weeklyReportVoJobs.add(new WeeklyReportJobVo(weeklyReportJob));
			}
		}

		return weeklyReportVoJobs;
	}
	
	/**
	 * 查找部门下所有人员项目工作计划周报
	 * 
	 * @param id
	 * @throws ServiceException
	 */
	public List<WeeklyReportProjectVo> findWeeklyReportProjectPlanByOrgId(String orgId, Date startTime, Date endTime) throws ServiceException {
		List<WeeklyReportProjectVo> weeklyReportVoProjectes = new ArrayList<WeeklyReportProjectVo>();
		List<WeeklyReportProject> weeklyReportProjectes = new ArrayList<WeeklyReportProject>();
		try {
			List<User> userList = orgService.findChildrenUserByOrgId(orgId);

			if (userList != null) {

				for (User user : userList) {
					List<WeeklyReportProject> userProjectes = weeklyReportMapper.findWeeklyReportProjectPlan(user.getId(), startTime, endTime);

					if (userProjectes != null && !userProjectes.isEmpty()) {
						weeklyReportProjectes.addAll(userProjectes);
					}
				}

			}

		} catch (Exception e) {
			throw new ServiceException("findWeeklyReportProjectPlanByOrgId service error!", e);
		}

		if (weeklyReportProjectes != null && !weeklyReportProjectes.isEmpty()) {
			for (WeeklyReportProject weeklyReportProject : weeklyReportProjectes) {
				weeklyReportVoProjectes.add(new WeeklyReportProjectVo(weeklyReportProject));
			}
		}

		return weeklyReportVoProjectes;
	}
}
