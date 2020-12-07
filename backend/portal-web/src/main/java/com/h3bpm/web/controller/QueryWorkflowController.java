package com.h3bpm.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.h3bpm.web.service.BusinessExceptionService;
import com.h3bpm.web.vo.BusinessExceptionVo;
import com.h3bpm.web.vo.ReqListBusinessExceptionPageVo;
import com.h3bpm.web.vo.RespPageVo;
import com.h3bpm.web.vo.query.QueryBusinessExceptionList;

import OThinker.H3.Controller.ControllerBase;

/**
 * Created by tonghao on 2020/12/3.
 */
@RestController
@RequestMapping(value = "/Portal/queryWorkflowList")
public class QueryWorkflowController extends ControllerBase {

	private static final Logger logger = LoggerFactory.getLogger(QueryWorkflowController.class);

	@Autowired
	private BusinessExceptionService businessExceptionService;

	@RequestMapping(value = "/listBusinessExceptionByPage", method = RequestMethod.POST, produces = "application/json;charset=utf8")
	@ResponseBody
	public RespPageVo listBusinessExceptionByPage(@ModelAttribute ReqListBusinessExceptionPageVo requestBean) {
		QueryBusinessExceptionList queryList = new QueryBusinessExceptionList(requestBean);
		PageInfo<BusinessExceptionVo> pageInfo = businessExceptionService.findBusinessExceptionByPage(queryList);

		return new RespPageVo(requestBean.getsEcho(), pageInfo.getTotal(), pageInfo.getList());
	}

	@Override
	public String getFunctionCode() {
		// TODO Auto-generated method stub
		return null;
	}
}
