package com.h3bpm.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.h3bpm.web.entity.MyKnowledge;
import com.h3bpm.web.service.MyKnowledgeService;
import com.h3bpm.web.utils.Constants;
import com.h3bpm.web.utils.UserSessionUtils;
import com.h3bpm.web.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.h3bpm.web.entity.Knowledge;
import com.h3bpm.web.service.KnowledgePermissionService;
import com.h3bpm.web.service.KnowledgeService;
import com.h3bpm.web.vo.query.QueryKnowledgeList;

import OThinker.Common.Organization.Models.User;

/**
 * Created by tonghao on 2020/3/1.
 */
@Controller
@RequestMapping(value = "/Portal/knowledgeManage")
public class KnowledgeManagerController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(KnowledgeManagerController.class);

	@Autowired
	private KnowledgeService knowledgeService;

	@Autowired
	private MyKnowledgeService myKnowledgeService;

	@Autowired
	private KnowledgePermissionService knowledgePermissionService;

	@Override
	public String getFunctionCode() {
		// TODO Auto-generated method stub
		return null;
	}

	// author:lbh
	@RequestMapping(value = "/createKnowledge", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo createKnowledge(@RequestBody ReqCreateKnowledge reqParam) throws Exception {
		Map<String, Object> userMap = this._getCurrentUser();
		User user = (User) userMap.get("User");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // String与Date之间进行相互转换
		KnowledgeVo knowledgeVo = new KnowledgeVo();

		knowledgeVo.setCreateUserName(user._Name);
		knowledgeVo.setCreateUserId(user.getObjectID()); // 存入用户id
		knowledgeVo.setFlowId(reqParam.getFlowId());
		knowledgeVo.setName(reqParam.getName());
		knowledgeVo.setDesc(reqParam.getDesc());
		knowledgeVo.setTagName(reqParam.getTagName());
		knowledgeVo.setFlowCodeDesc(reqParam.getFlowCodeDesc());
		try {
			knowledgeVo.setStartTime(format.parse(reqParam.getStartTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			knowledgeVo.setEndTime(format.parse(reqParam.getEndTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		knowledgeVo.setCreateTime(new Date());
		knowledgeVo.setPermission(reqParam.getPermission());
		knowledgeService.createKnowledge(knowledgeVo);

		return new ResponseVo("创建成功");
	}

	@RequestMapping(value = "/createMyKnowledge", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo createMyKnowledge(@RequestBody ReqCreateMyKnowledge reqParam) throws Exception {
		Map<String, Object> userMap = this._getCurrentUser();
		User user = (User) userMap.get("User");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); // String与Date之间进行相互转换
		MyKnowledgeVo myKnowledgeVo = new MyKnowledgeVo();

		myKnowledgeVo.setCreateUserName(user._Name);
		myKnowledgeVo.setCreateUserId(user.getObjectID()); // 存入用户id

		// myKnowledgeVo.setCreateUserName("协办平台管理员");
		// myKnowledgeVo.setCreateUserId("82237383-a18e-4055-8006-8c873e84e087"); //存入用户id

		myKnowledgeVo.setFlowId(reqParam.getFlowId());
		myKnowledgeVo.setName(reqParam.getName());
		myKnowledgeVo.setDesc(reqParam.getDesc());
		myKnowledgeVo.setTagName(reqParam.getTagName());
		myKnowledgeVo.setFlowCodeDesc(reqParam.getFlowCodeDesc());
		try {
			myKnowledgeVo.setStartTime(format.parse(reqParam.getStartTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			myKnowledgeVo.setEndTime(format.parse(reqParam.getEndTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		myKnowledgeVo.setCreateTime(new Date());
		myKnowledgeService.createMyKnowledge(myKnowledgeVo);

		return new ResponseVo("创建成功");
	}

	@RequestMapping(value = "/updateKnowledge", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo updateKnowledge(@RequestBody ReqUpdateKnowledge reqUpdateKnowledge) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // String与Date之间进行相互转换
		Knowledge knowledgeEntity = knowledgeService.getKnowledgeById(reqUpdateKnowledge.getId());
		KnowledgeVo knowledgeVo = new KnowledgeVo(knowledgeEntity);

		knowledgeVo.setFlowId(reqUpdateKnowledge.getFlowId());
		knowledgeVo.setName(reqUpdateKnowledge.getName());
		knowledgeVo.setDesc(reqUpdateKnowledge.getDesc());
		knowledgeVo.setTagName(reqUpdateKnowledge.getTagName());
		knowledgeVo.setFlowCodeDesc(reqUpdateKnowledge.getFlowCodeDesc());
		try { // 将前端传过来的'yyyy-MM-dd'样式的String转换成Date类型
			knowledgeVo.setStartTime(format.parse(reqUpdateKnowledge.getStartTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			knowledgeVo.setEndTime(format.parse(reqUpdateKnowledge.getEndTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		knowledgeVo.setPermission(reqUpdateKnowledge.getPermission());
		knowledgeService.updateKnowledge(knowledgeVo);

		return new ResponseVo("修改成功");
	}

	@RequestMapping(value = "/updateMyKnowledge", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo updateMyKnowledge(@RequestBody ReqUpdateMyKnowledge reqUpdateMyKnowledge) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); // String与Date之间进行相互转换
		MyKnowledge myKnowledgeEntity = myKnowledgeService.getMyKnowledgeById(reqUpdateMyKnowledge.getId());
		MyKnowledgeVo myKnowledgeVo = new MyKnowledgeVo(myKnowledgeEntity);

		myKnowledgeVo.setFlowId(reqUpdateMyKnowledge.getFlowId());
		myKnowledgeVo.setName(reqUpdateMyKnowledge.getName());
		myKnowledgeVo.setDesc(reqUpdateMyKnowledge.getDesc());
		myKnowledgeVo.setTagName(reqUpdateMyKnowledge.getTagName());
		myKnowledgeVo.setFlowCodeDesc(reqUpdateMyKnowledge.getFlowCodeDesc());
		try { // 将前端传过来的'yyyy-MM-dd'样式的String转换成Date类型
			myKnowledgeVo.setStartTime(format.parse(reqUpdateMyKnowledge.getStartTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			myKnowledgeVo.setEndTime(format.parse(reqUpdateMyKnowledge.getEndTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		myKnowledgeService.updateMyKnowledge(myKnowledgeVo);

		return new ResponseVo("修改成功");
	}

	@RequestMapping(value = "/listKnowledgeByPage", produces = "application/json;charset=utf8")
	@ResponseBody
	public RespPageVo listKnowledgeByPage(@ModelAttribute ReqListKnowledgePageVo requestBean) {
		Map<String, Object> userMap = null;
		try {
			userMap = this._getCurrentUser();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		User user = (User) userMap.get("User");

		QueryKnowledgeList queryKnowledgeList = new QueryKnowledgeList(requestBean);
		queryKnowledgeList.setQueryUserId(user.getObjectId());

		UserSessionInfo userSessionInfo = UserSessionUtils.get(Constants.SESSION_USER, UserSessionInfo.class);
		List<String> parentIds = userSessionInfo.getParentIds();
		if (parentIds != null) {
			queryKnowledgeList.setUserAllParentIds(parentIds);
		}

		PageInfo<KnowledgeVo> pageInfo = knowledgeService.findKnowledgeByPage(queryKnowledgeList);
		List<KnowledgeVo> knowledgeList = pageInfo.getList();
		if (knowledgeList != null) {
			for (KnowledgeVo knowledge : knowledgeList) {
				knowledge.setPermission(knowledgePermissionService.getKnowledgePermissionByKnowledgeId(knowledge.getId()));
			}
		}

		return new RespPageVo(requestBean.getsEcho(), pageInfo.getTotal(), pageInfo.getList());
	}

	@RequestMapping(value = "/deleteKnowledge", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo deleteKnowledge(@RequestBody ReqDeleteKnowledgeVo reqDeleteKnowledgeVo) {
		knowledgeService.deleteKnowledge(reqDeleteKnowledgeVo.getId());
		return new ResponseVo("删除成功");
	}
}
