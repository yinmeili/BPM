package com.h3bpm.web.controller;

import OThinker.Common.Organization.Models.User;
import OThinker.H3.Controller.ControllerBase;
import com.h3bpm.web.entity.Knowledge;
import com.h3bpm.web.enumeration.FileType;
import com.h3bpm.web.mapper.KnowledgeMapper;
import com.h3bpm.web.service.FilePermissionService;
import com.h3bpm.web.service.FileService;
import com.h3bpm.web.service.KnowledgeService;
import com.h3bpm.web.service.MyFileService;
import com.h3bpm.web.utils.Constants;
import com.h3bpm.web.utils.FtUtils;
import com.h3bpm.web.utils.SFTPUtil;
import com.h3bpm.web.utils.UserSessionUtils;
import com.h3bpm.web.vo.*;
import com.jcraft.jsch.SftpException;
import org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tonghao on 2020/3/1.
 */
@Controller
@RequestMapping(value = "/Portal/knowledgeManage")
public class KnowledgeManagerController extends ControllerBase {

	private static final Logger logger = LoggerFactory.getLogger(KnowledgeManagerController.class);

	@Autowired
	private KnowledgeService knowledgeService;
	@Autowired
	private KnowledgeMapper knowledgeMapper;

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
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");	//String与Date之间进行相互转换
		KnowledgeVo knowledgeVo = new KnowledgeVo();

		knowledgeVo.setCreateUserName(user._Name);
		knowledgeVo.setCreateUserId(user.getObjectID());	//存入用户id
		knowledgeVo.setFlowId(reqParam.getFlowId());
		knowledgeVo.setName(reqParam.getName());
		knowledgeVo.setDesc(reqParam.getDesc());
		knowledgeVo.setTagName(reqParam.getTagName());
		knowledgeVo.setFlowCodeDesc(reqParam.getFlowCodeDesc());
		try{
			knowledgeVo.setStartTime(format.parse(reqParam.getStartTime()));
		}catch (Exception e){
			e.printStackTrace();
		}
		try{
			knowledgeVo.setEndTime(format.parse(reqParam.getEndTime()));
		}catch (Exception e){
			e.printStackTrace();
		}
		knowledgeVo.setCreateTime(new Date());
		knowledgeVo.setPermission(reqParam.getPermission());
		knowledgeService.createKnowledge(knowledgeVo);

		return new ResponseVo("创建成功");
	}

	@RequestMapping(value = "/updateKnowledge", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo updateKnowledge(@RequestBody ReqUpdateKnowledge reqUpdateKnowledge) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");	//String与Date之间进行相互转换
		Knowledge knowledgeEntity = knowledgeService.getKnowledgeById(reqUpdateKnowledge.getId());
		KnowledgeVo knowledgeVo = new KnowledgeVo(knowledgeEntity);

		knowledgeVo.setFlowId(reqUpdateKnowledge.getFlowId());
		knowledgeVo.setName(reqUpdateKnowledge.getName());
		knowledgeVo.setDesc(reqUpdateKnowledge.getDesc());
		knowledgeVo.setTagName(reqUpdateKnowledge.getTagName());
		knowledgeVo.setFlowCodeDesc(reqUpdateKnowledge.getFlowCodeDesc());
		try{	 //将前端传过来的'yyyy-MM-dd'样式的String转换成Date类型
			knowledgeVo.setStartTime(format.parse(reqUpdateKnowledge.getStartTime()));
		}catch (Exception e){
			e.printStackTrace();
		}
		try{
			knowledgeVo.setEndTime(format.parse(reqUpdateKnowledge.getEndTime()));
		}catch (Exception e){
			e.printStackTrace();
		}
		knowledgeVo.setPermission(reqUpdateKnowledge.getPermission());
		knowledgeService.updateKnowledge(knowledgeVo);

		return new ResponseVo("修改成功");
	}
}
