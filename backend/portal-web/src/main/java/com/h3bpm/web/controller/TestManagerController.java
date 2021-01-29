package com.h3bpm.web.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.h3bpm.web.service.TestManageService;
import com.h3bpm.web.vo.ReqListTestEnvPageVo;
import com.h3bpm.web.vo.RespListAllTestEnvVo;
import com.h3bpm.web.vo.RespPageVo;
import com.h3bpm.web.vo.ResponseVo;
import com.h3bpm.web.vo.TestEnvVo;
import com.h3bpm.web.vo.query.QueryTestEnvList;

/**
 * Created by tonghao on 2020/3/1.
 */
@Controller
@RequestMapping(value = "/Portal/testManage")
public class TestManagerController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(TestManagerController.class);

	@Autowired
	private TestManageService testManageService;

	@Override
	public String getFunctionCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value = "/createTestEnv", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo createTestEnv(@RequestBody TestEnvVo reqParam) throws Exception {
		testManageService.createTestEnv(reqParam);
		return new ResponseVo("创建成功");
	}

	@RequestMapping(value = "/updateTestEnv", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo updateTestEnv(@RequestBody TestEnvVo reqParam) throws Exception {
		testManageService.updateKnowledge(reqParam);
		return new ResponseVo("编辑成功");
	}

	@RequestMapping(value = "/listTestEnvByPage", produces = "application/json;charset=utf8")
	@ResponseBody
	public RespPageVo listTestEnvByPage(@ModelAttribute ReqListTestEnvPageVo requestBean) {
		QueryTestEnvList queryTestEnvList = new QueryTestEnvList(requestBean);

		PageInfo<TestEnvVo> pageInfo = testManageService.findTestEnvByPage(queryTestEnvList);
		return new RespPageVo(requestBean.getsEcho(), pageInfo.getTotal(), pageInfo.getList());
	}

	@RequestMapping(value = "/listAllTestEnv", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo listAllTestEnv() {
		List<RespListAllTestEnvVo> respListAllTestEnvVoList = new ArrayList<>();
		List<TestEnvVo> testEvnVoList = testManageService.findAllTestEnv();

		if (testEvnVoList != null) {
			for (TestEnvVo testEvnVo : testEvnVoList) {
				respListAllTestEnvVoList.add(new RespListAllTestEnvVo(testEvnVo));
			}
		}
		return new ResponseVo(respListAllTestEnvVoList);
	}

	@RequestMapping(value = "/deleteTestEnv", method = RequestMethod.GET, produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo deleteTestEnv(@RequestParam("id") String id) {
		testManageService.deleteTestEnv(id);
		return new ResponseVo("删除成功");
	}
	
	@RequestMapping(value = "/getTestEnvById", method = RequestMethod.GET, produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo getTestEnvById(@RequestParam("id") String id) {
		return new ResponseVo(testManageService.getTestEnvById(id));
	}
}
