package com.h3bpm.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.h3bpm.web.service.ServiceException;
import com.h3bpm.web.service.WeeklyReportService;
import com.h3bpm.web.vo.ReqListWeeklyReportJobVo;
import com.h3bpm.web.vo.ReqListWeeklyReportProjectVo;
import com.h3bpm.web.vo.ResponseVo;

@Controller
@RequestMapping(value = "/Portal/weeklyReport")
public class WeeklyReportController extends AbstractController {

	@Autowired
	private WeeklyReportService weeklyReportService;

	@RequestMapping(value = "/listOrgJob", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo listOrgJob(@RequestBody ReqListWeeklyReportJobVo requestBean) throws ServiceException {
		validateWeeklyReportJobVo(requestBean);
		return new ResponseVo(weeklyReportService.findWeeklyReportJobByOrgId(requestBean.getOrgId(), requestBean.getStartTime(), requestBean.getEndTime(), requestBean.getJobLevel()));
	}

	@RequestMapping(value = "/listOrgProject", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo listOrgProject(@RequestBody ReqListWeeklyReportProjectVo requestBean) throws ServiceException {
		validateWeeklyReportProjectVo(requestBean);
		return new ResponseVo(weeklyReportService.findWeeklyReportProjectByOrgId(requestBean.getOrgId(), requestBean.getStartTime(), requestBean.getEndTime()));
	}

	@RequestMapping(value = "/listOrgJobPlan", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo listOrgJobPlan(@RequestBody ReqListWeeklyReportJobVo requestBean) throws ServiceException {
		validateWeeklyReportJobVo(requestBean);
		return new ResponseVo(weeklyReportService.findWeeklyReportJobPlanByOrgId(requestBean.getOrgId(), requestBean.getStartTime(), requestBean.getEndTime(), requestBean.getJobLevel()));
	}

	@RequestMapping(value = "/listOrgProjectPlan", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo listOrgProjectPlan(@RequestBody ReqListWeeklyReportProjectVo requestBean) throws ServiceException {
		validateWeeklyReportProjectVo(requestBean);
		return new ResponseVo(weeklyReportService.findWeeklyReportProjectPlanByOrgId(requestBean.getOrgId(), requestBean.getStartTime(), requestBean.getEndTime()));
	}

	@Override
	public String getFunctionCode() {
		return null;
	}

	private void validateWeeklyReportJobVo(ReqListWeeklyReportJobVo requestBean) throws ServiceException {
		if (requestBean.getStartTime() == null) {
			throw new ServiceException("请输入开始时间");
		}
		if (requestBean.getEndTime() == null) {
			throw new ServiceException("请输入截止时间");
		}
		if (requestBean.getOrgId() == null || requestBean.getOrgId().isEmpty()) {
			throw new ServiceException("部门ID不能为空");
		}
		if (requestBean.getJobLevel() == 0) {
			throw new ServiceException("工作级别不能为空");
		}
	}

	private void validateWeeklyReportProjectVo(ReqListWeeklyReportProjectVo requestBean) throws ServiceException {
		if (requestBean.getStartTime() == null) {
			throw new ServiceException("请输入开始时间");
		}
		if (requestBean.getEndTime() == null) {
			throw new ServiceException("请输入截止时间");
		}
		if (requestBean.getOrgId() == null || requestBean.getOrgId().isEmpty()) {
			throw new ServiceException("部门ID不能为空");
		}
	}

}
