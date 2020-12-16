package com.h3bpm.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.h3bpm.web.entity.MyKnowledge;
import com.h3bpm.web.service.ProjectManageService;
import com.h3bpm.web.utils.Constants;
import com.h3bpm.web.utils.UserSessionUtils;
import com.h3bpm.web.vo.KnowledgeVo;
import com.h3bpm.web.vo.MyKnowledgeVo;
import com.h3bpm.web.vo.ProjectVo;
import com.h3bpm.web.vo.ReqCollectToMyKnowledge;
import com.h3bpm.web.vo.ReqCreateKnowledge;
import com.h3bpm.web.vo.ReqCreateMyKnowledge;
import com.h3bpm.web.vo.ReqCreateProjectInfo;
import com.h3bpm.web.vo.ReqDeleteKnowledgeVo;
import com.h3bpm.web.vo.ReqListKnowledgePageVo;
import com.h3bpm.web.vo.ReqListLeaderActiveProjectInfoVo;
import com.h3bpm.web.vo.ReqListProjectInfoPageVo;
import com.h3bpm.web.vo.ReqShareMyKnowledge;
import com.h3bpm.web.vo.ReqUpdateMyKnowledge;
import com.h3bpm.web.vo.ReqUpdateProjectInfo;
import com.h3bpm.web.vo.RespPageVo;
import com.h3bpm.web.vo.ResponseVo;
import com.h3bpm.web.vo.UserSessionInfo;
import com.h3bpm.web.vo.query.QueryKnowledgeList;
import com.h3bpm.web.vo.query.QueryLeaderActiveProjectInfo;
import com.h3bpm.web.vo.query.QueryMyKnowledgeList;
import com.h3bpm.web.vo.query.QueryProjectInfoList;

import OThinker.Common.DateTimeUtil;
import OThinker.Common.Organization.Models.User;

/**
 * Created by tonghao on 2020/3/1.
 */
@Controller
@RequestMapping(value = "/Portal/project")
public class ProjectManageController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(ProjectManageController.class);

	@Autowired
	private ProjectManageService projectManageService;

	@Override
	public String getFunctionCode() {
		// TODO Auto-generated method stub
		return null;
	}

	// author:lbh
	@RequestMapping(value = "/createProject", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo createKnowledge(@RequestBody ReqCreateProjectInfo reqParam) throws Exception {
		ProjectVo projectVo = new ProjectVo();

		projectVo.setName(reqParam.getName());
		projectVo.setDesc(reqParam.getDesc());
		projectVo.setLeaderId(reqParam.getLeaderId());
		projectVo.setStartTime(reqParam.getStartTime());

		// 页面接收的时间没有时分秒，将时分秒加大到该天的最后时刻
		Date endTime = DateTimeUtil.addHours(reqParam.getEndTime(), 23);
		endTime = DateTimeUtil.addMinutes(endTime, 59);
		endTime = DateTimeUtil.addSeconds(endTime, 59);
		projectVo.setEndTime(endTime);

		projectManageService.createProjectInfo(projectVo);

		return new ResponseVo("创建成功");
	}

	@RequestMapping(value = "/updateProject", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo updateProject(@RequestBody ReqUpdateProjectInfo reqParam) {
		ProjectVo projectVo = new ProjectVo();

		projectVo.setId(reqParam.getId());
		projectVo.setName(reqParam.getName());
		projectVo.setDesc(reqParam.getDesc());
		projectVo.setLeaderId(reqParam.getLeaderId());
		projectVo.setStartTime(reqParam.getStartTime());

		// 页面接收的时间没有时分秒，将时分秒加大到该天的最后时刻
		Date endTime = DateTimeUtil.addHours(reqParam.getEndTime(), 23);
		endTime = DateTimeUtil.addMinutes(endTime, 59);
		endTime = DateTimeUtil.addSeconds(endTime, 59);
		projectVo.setEndTime(endTime);

		projectManageService.updateProjectInfo(projectVo);

		return new ResponseVo("修改成功");
	}

	@RequestMapping(value = "/listProjectByPage")
	@ResponseBody
	public RespPageVo listProjectByPage(@ModelAttribute ReqListProjectInfoPageVo requestBean) {
		QueryProjectInfoList queryProjectInfoList = new QueryProjectInfoList(requestBean);

		PageInfo<ProjectVo> pageInfo = projectManageService.findProjectInfoByPage(queryProjectInfoList);

		return new RespPageVo(requestBean.getsEcho(), pageInfo.getTotal(), pageInfo.getList());
	}

	@RequestMapping(value = "/deleteProject", method = RequestMethod.GET, produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo deleteKnowledge(@RequestParam("id") String id) {
		projectManageService.deleteProjectInfo(id);
		return new ResponseVo("删除成功");
	}

	@RequestMapping(value = "/listLeaderActiveProject")
	@ResponseBody
	public ResponseVo listLeaderActiveProject(@ModelAttribute ReqListLeaderActiveProjectInfoVo requestBean) {
		QueryLeaderActiveProjectInfo queryProjectInfoList = new QueryLeaderActiveProjectInfo(requestBean);
		return new ResponseVo(projectManageService.findLeadIdActiveProjectInfo(queryProjectInfoList));
	}
}
