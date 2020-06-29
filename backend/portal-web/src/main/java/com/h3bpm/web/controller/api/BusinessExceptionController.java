package com.h3bpm.web.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.h3bpm.web.service.BusinessExceptionService;
import com.h3bpm.web.vo.BusinessExceptionVo;
import com.h3bpm.web.vo.ReqBusinessExceptionList;

import OThinker.H3.Controller.ControllerBase;

/**
 * Created by tonghao on 2020/3/1.
 */
@RestController
@RequestMapping(value = "/Portal/api/businessException")
public class BusinessExceptionController extends ControllerBase {

	private static final Logger logger = LoggerFactory.getLogger(BusinessExceptionController.class);

	@Autowired
	private BusinessExceptionService businessExceptionService;

	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=utf8")
	@ResponseBody
	public List<BusinessExceptionVo> list(@RequestBody ReqBusinessExceptionList requestBean) {

		List<BusinessExceptionVo> result = new ArrayList<>();

		try {
			result = businessExceptionService.findList(requestBean.getSystemType());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public String getFunctionCode() {
		// TODO Auto-generated method stub
		return null;
	}
}
