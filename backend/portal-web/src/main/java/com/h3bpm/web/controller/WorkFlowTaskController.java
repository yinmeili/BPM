package com.h3bpm.web.controller;

import java.io.BufferedOutputStream;
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

	@RequestMapping(value = "/downloadTemplate")
	@ResponseBody
	public void previewFile(@RequestParam("workflowCode") String workflowCode, HttpServletRequest request, HttpServletResponse response) {

		InputStream is = null;
		OutputStream os = null;

		try (OutputStream out = response.getOutputStream();) {
			response.setHeader("Content-Type", "application/octet-stream");

			if (WorkflowCode.parse(workflowCode) == WorkflowCode.LIQUIDATION) {
				response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("template-" + workflowCode + ".xlsx", "UTF-8"));
				is = this.getClass().getClassLoader().getResourceAsStream("config/files/template_liquidation.xlsx");
			}

			if (is != null) {
				os = new BufferedOutputStream(response.getOutputStream());
				byte[] buff = new byte[100]; // buff用于存放循环读取的临时数据
				int rc = 0;
				while ((rc = is.read(buff, 0, 100)) > 0) {
					os.write(buff, 0, rc);
				}

				os.flush();
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (os != null) {
				try {
					os.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String getFunctionCode() {
		// TODO Auto-generated method stub
		return null;
	}

}
