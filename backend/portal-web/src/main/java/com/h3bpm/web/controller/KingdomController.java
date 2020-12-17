package com.h3bpm.web.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.h3bpm.web.enumeration.MonitorNodeName;
import com.h3bpm.web.service.KingdomService;
import com.h3bpm.web.service.TradeCalendarService;
import com.h3bpm.web.vo.ResponseVo;

import OThinker.Common.DateTimeUtil;
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

	@Autowired
	private TradeCalendarService tradeCalendarService;

	@RequestMapping(value = "/getToken", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo getToken() throws Exception {

		String token = kingdomService.getToken();
		return new ResponseVo((Object) token);
	}

	@RequestMapping(value = "/findStockToMarketNodeList")
	@ResponseBody
	public ResponseVo findStockToMarketNodeList(@RequestParam("queryDate") String queryDate) throws Exception {

		// 股转做市取订单生成的第二个交易日数据
		Date monitorDate = DateTimeUtil.parse(queryDate, "yyyy-MM-dd HH:mm:ss");
		monitorDate = tradeCalendarService.getNextTradeDateByDate(monitorDate);

		return new ResponseVo(kingdomService.refreshNodeHistoryList(MonitorNodeName.STMARKET, monitorDate));
	}

	@RequestMapping(value = "/findDealNodeList")
	@ResponseBody
	public ResponseVo findDealNodeInfo(@RequestParam("queryDate") String queryDate) throws Exception {
		// 投资交易 取订单生成的第二个交易日数据
		Date monitorDate = DateTimeUtil.parse(queryDate, "yyyy-MM-dd HH:mm:ss");
		monitorDate = tradeCalendarService.getNextTradeDateByDate(monitorDate);

		return new ResponseVo(kingdomService.refreshNodeHistoryList(MonitorNodeName.DEAL, monitorDate));
	}

	@RequestMapping(value = "/findMarketOpenAtNightNodeList")
	@ResponseBody
	public ResponseVo findMarketOpenAtNightNodeList(@RequestParam("queryDate") String queryDate) throws Exception {
		// 夜盘开市 取订单生成的当天数据
		Date monitorDate = DateTimeUtil.parse(queryDate, "yyyy-MM-dd HH:mm:ss");

		return new ResponseVo(kingdomService.refreshNodeHistoryList(MonitorNodeName.MOAN, monitorDate));
	}

	@RequestMapping(value = "/findNodeNameList")
	@ResponseBody
	public ResponseVo findNodeNameList() throws Exception {
		return new ResponseVo(kingdomService.findNodeName());
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
