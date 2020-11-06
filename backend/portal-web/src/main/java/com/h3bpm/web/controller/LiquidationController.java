package com.h3bpm.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.h3bpm.web.service.LiquidationService;
import com.h3bpm.web.vo.LiquidationVo;
import com.h3bpm.web.vo.ReqListLiquidationPageVo;
import com.h3bpm.web.vo.RespPageVo;
import com.h3bpm.web.vo.ResponseVo;
import com.h3bpm.web.vo.query.QueryLiquidationList;

@Controller
@RequestMapping(value = "/Portal/liquidation")
public class LiquidationController extends AbstractController {

	@Autowired
	private LiquidationService liquidationService;

	@RequestMapping(value = "/listLiquidationByPage", produces = "application/json;charset=utf8")
	@ResponseBody
	public RespPageVo listWorkFlowTaskByPage(@ModelAttribute ReqListLiquidationPageVo requestBean) {
		QueryLiquidationList queryLiquidationList = new QueryLiquidationList(requestBean);
		PageInfo<LiquidationVo> pageInfo = liquidationService.findLiquidationByPage(queryLiquidationList);

		return new RespPageVo(requestBean.getsEcho(), pageInfo.getTotal(), pageInfo.getList());
	}

	@RequestMapping(value = "/createLiquidation", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResponseVo createLiquidation(@RequestBody LiquidationVo liquidationVo) throws Exception {
		liquidationService.createLiquidation(liquidationVo);

		return new ResponseVo("创建成功");
	}

	@RequestMapping(value = "/updateLiquidation", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo updateAnnouncement(@RequestBody LiquidationVo liquidationVo) throws Exception {
		liquidationService.updateLiquidation(liquidationVo);

		return new ResponseVo("修改成功");
	}

	@RequestMapping(value = "/deleteLiquidation", method = RequestMethod.GET, produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo deleteLiquidation(@RequestParam("id") String id) {
		liquidationService.deleteLiquidation(id);
		return new ResponseVo("删除成功");
	}

	@Override
	public String getFunctionCode() {
		// TODO Auto-generated method stub
		return null;
	}

}
