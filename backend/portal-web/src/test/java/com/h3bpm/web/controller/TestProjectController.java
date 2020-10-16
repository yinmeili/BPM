package com.h3bpm.web.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.h3bpm.web.utils.SFTPUtil;
import com.h3bpm.web.vo.ReqListWorkflowTaskPageVo;
import com.h3bpm.web.vo.ReqParam;
import com.h3bpm.web.vo.RespPageVo;
import com.h3bpm.web.vo.ResponseVo;
import com.h3bpm.web.vo.WorkFlowTaskVo;
import com.h3bpm.web.vo.query.QueryWorkFlowTaskList;

@Controller
@RequestMapping(value = "/Portal/testProject")
public class TestProjectController extends AbstractController {

	@Autowired
	private WorkFlowTaskService workFlowTaskService;

	@RequestMapping(value = "/addWeeklyReportTask", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo addWorkFlowTask() {
		workFlowTaskService.addWeeklyReportWorkFlowTask();
		return new ResponseVo("导入周报人员成功");
	}

	@Override
	public String getFunctionCode() {
		// TODO Auto-generated method stub
		return null;
	}

}
