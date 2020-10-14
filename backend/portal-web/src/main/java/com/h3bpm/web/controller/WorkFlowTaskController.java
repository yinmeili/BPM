package com.h3bpm.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.h3bpm.web.enumeration.WorkflowCode;
import com.h3bpm.web.service.WorkFlowTaskService;
import com.h3bpm.web.vo.ReqListWorkflowTaskPageVo;
import com.h3bpm.web.vo.RespPageVo;
import com.h3bpm.web.vo.ResponseVo;
import com.h3bpm.web.vo.WorkFlowTaskVo;
import com.h3bpm.web.vo.query.QueryWorkFlowTaskList;

@Controller
@RequestMapping(value = "/Portal/workflowTask")
public class WorkFlowTaskController extends AbstractController {

	@Autowired
	private WorkFlowTaskService workFlowTaskService;

	@RequestMapping(value = "/importTask", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo importExcel(@RequestParam("file") MultipartFile file, @RequestParam("workflowCode") String workflowCode) {
		if (WorkflowCode.parse(workflowCode) == WorkflowCode.LIQUIDATION) {
			workFlowTaskService.importLiquidationExcel(file);
		}

		return new ResponseVo("导入成功");
	}

	@RequestMapping(value = "/listWorkFlowTaskByPage", produces = "application/json;charset=utf8")
	@ResponseBody
	public RespPageVo listWorkFlowTaskByPage(@ModelAttribute ReqListWorkflowTaskPageVo requestBean) {
		QueryWorkFlowTaskList queryWorkFlowTaskList = new QueryWorkFlowTaskList(requestBean);
		PageInfo<WorkFlowTaskVo> pageInfo = workFlowTaskService.findWorkFlowTaskByPage(queryWorkFlowTaskList);

		return new RespPageVo(requestBean.getsEcho(), pageInfo.getTotal(), pageInfo.getList());
	}

	@RequestMapping(value = "/addWeeklyReportTask", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo addWorkFlowTask(){
		workFlowTaskService.addWeeklyReportWorkFlowTask();
		return new ResponseVo("导入周报人员成功");
	}

	@Override
	public String getFunctionCode() {
		// TODO Auto-generated method stub
		return null;
	}

}
