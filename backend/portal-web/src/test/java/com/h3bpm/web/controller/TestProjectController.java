package com.h3bpm.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.h3bpm.web.entity.User;
import com.h3bpm.web.service.KingdomService;
import com.h3bpm.web.service.ServiceException;
import com.h3bpm.web.service.UserService;
import com.h3bpm.web.service.WorkFlowService;
import com.h3bpm.web.service.WorkFlowTaskService;
import com.h3bpm.web.vo.ResponseVo;
import com.h3bpm.web.vo.SmsInfoVo;

@Controller
@RequestMapping(value = "/Portal/testProject")
public class TestProjectController extends AbstractController {

	@Autowired
	private WorkFlowTaskService workFlowTaskService;

	@Autowired
	private WorkFlowService workFlowService;
	
	@Autowired
	private KingdomService kingdomService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/addWeeklyReportTask", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo addWorkFlowTask() {
		workFlowTaskService.addWeeklyReportWorkFlowTask();
		return new ResponseVo("导入周报人员成功");
	}
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/createWorkFlow", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo createWorkFlow() throws ServiceException {
		workFlowService.createWorkFlow("fe56c0e8-e19c-4512-b60f-9b7254a1409d");
		return new ResponseVo();
	}
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/sendSms", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo sendSms() {
		User user = userService.getUserByLoginName("tonghao");
		kingdomService.sendSmsInfo(new SmsInfoVo(user.getName(),user.getMobile(), "【协办平台】您有一个新的待办任务，请按时处理，谢谢！"));
		
		return new ResponseVo();
	}

	@Override
	public String getFunctionCode() {
		// TODO Auto-generated method stub
		return null;
	}

}
