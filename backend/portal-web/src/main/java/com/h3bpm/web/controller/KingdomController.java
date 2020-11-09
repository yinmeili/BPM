package com.h3bpm.web.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.h3bpm.web.service.KingdomService;
import com.h3bpm.web.vo.ResponseVo;

import OThinker.H3.Controller.ControllerBase;

/**
 *
 *
 * @author lzf
 */
@Controller
@RequestMapping(value = "/Portal/kingdom")
public class KingdomController extends ControllerBase {

	private static final Logger logger = LoggerFactory.getLogger(KingdomController.class);

	@Autowired
	private KingdomService kingdomService;

	@RequestMapping(value = "/getToken", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo getToken() throws Exception {

		String token = kingdomService.getToken();
		return new ResponseVo((Object) token);
	}
	
	@RequestMapping(value = "/findNodeList")
	@ResponseBody
	public ResponseVo findNodeList(HttpServletResponse response) throws Exception {
//		response.setContentLength(1);
		return new ResponseVo( kingdomService.findNodeInfo());
	}
	
	@RequestMapping(value = "/findNodeNameList")
	@ResponseBody
	public ResponseVo findNodeNameList() throws Exception {
		return new ResponseVo( kingdomService.findNodeName());
	}

	@RequestMapping(value = "/getNodeList")
	@ResponseBody
	public ResponseVo getNodeList() throws Exception {
		return new ResponseVo(kingdomService.getNodeInfo());
	}

	@Override
	public String getFunctionCode() {
		// TODO Auto-generated method stub
		return null;
	}

}
