package com.h3bpm.web.controller;

import OThinker.H3.Controller.ControllerBase;
import com.h3bpm.web.service.KingdomService;
import com.h3bpm.web.vo.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This class is designed to get Kingdom Token.
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
	public ResponseVo findNodeList() throws Exception {
		return new ResponseVo( kingdomService.findNodeInfo());
	}

	@Override
	public String getFunctionCode() {
		// TODO Auto-generated method stub
		return null;
	}

}
