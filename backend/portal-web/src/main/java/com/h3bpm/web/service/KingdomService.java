package com.h3bpm.web.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.h3bpm.web.entity.MonitorNodeHistory;
import com.h3bpm.web.enumeration.HttpRequestType;
import com.h3bpm.web.enumeration.KingdomNodeStatus;
import com.h3bpm.web.enumeration.MonitorNodeName;
import com.h3bpm.web.mapper.LiquidationMonitorMapper;
import com.h3bpm.web.vo.SmsInfoVo;
import com.h3bpm.web.vo.api.kingdom.KingdomNodeVo;
import com.h3bpm.web.vo.api.kingdom.KingdomRequestVo;
import com.h3bpm.web.vo.api.kingdom.KingdomResponseVo;

import OThinker.Common.DateTimeUtil;

/**
 *
 *
 * @author lzf
 */
@Service
public class KingdomService extends ApiDataService {

	@Value(value = "${application.api.kingdom.username}")
	private String user = null;

	@Value(value = "${application.api.kingdom.password}")
	private String pass = null;

	@Value(value = "${application.api.kingdom.modelName}")
	private String modelName = null;

	@Value(value = "${application.api.kingdom.methodName}")
	private String methodName = null;

	@Value(value = "${application.api.kingdom.flowId.stockToMarket}")
	private String stockToMarketFlowId = null;

	@Value(value = "${application.api.kingdom.flowId.deal}")
	private String dealFlowId = null;

	@Value(value = "${application.api.kingdom.flowId.marketOpenAtNight}")
	private String marketOpenAtNightFlowId = null;

	@Autowired
	private LiquidationMonitorMapper liquidationMonitorMapper;

	private static String TOKEN = null;

	private static final String TOKEN_ERROR_INFO = "TokenError";
	private static final String FLOW_NODE_KEY = "k_flow_object";
	private static final String STATUS_STR = "执行状态";
	private static final String EXECUTE_RESULT = "级别";
	private static final String FLOW_NODE_NAMES_KEY = "k_object_vclparam";

	/**
	 * 获取令牌
	 * 
	 * @return
	 */
	public String getToken() {

		try {
			List<KingdomRequestVo> kingdomRequestVoList = new ArrayList<KingdomRequestVo>();
			kingdomRequestVoList.add(new KingdomRequestVo("TBaseDM", modelName));
			kingdomRequestVoList.add(new KingdomRequestVo(user, "User"));
			kingdomRequestVoList.add(new KingdomRequestVo(pass, "Pass"));
			kingdomRequestVoList.add(new KingdomRequestVo("Test1", methodName));

			List<Map<String, Object>> mapList = this.processSyncKingdom(HttpRequestType.POST, kingdomRequestVoList);

			for (Map<String, Object> map : mapList) {
				if ("Token".equals(map.get("Name"))) {
					return (String) map.get("Value");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<String> findNodeName() {
		return findNodeNameByFlowId(stockToMarketFlowId);
	}

	public List<KingdomNodeVo> findStockToMarketNodeInfo() {
		// List<KingdomNodeVo> kingdomVoList = new ArrayList<>();
		// try {
		//
		// String testStr = "[" + " {" + " \"Value\": [" + " [" + " {" + " \"Type\": 4," + " \"Name\": \"Name\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"State\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"Data\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"Level\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"Desc\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"PID\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"Left\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"Top\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"AgentID\""
		// + " }," + " {" + " \"Type\": 4," + " \"Name\": \"AgentName\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"ObjectID\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"ObjectName\"" + " }" + " ]," + " " + " [" + " {" + " \"Value\": \"1-上交所行情666\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"开始\"" + " }," + " {" + " \"Value\": \"7\"" + " }," + " {" + " \"Value\": \"161\"" + " },"
		// + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{86472566-74B0-4809-80D5-C776092B1ECA}\"" + " }," + " {" + " \"Value\": \"1-上交所行情\\n相关程序开启\"" + " }" + " ]," + " " + " " + " [" + " {" + " \"Value\": \"2-深交所行情666\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"1-上交所行情\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"8\"" + " }," + " {" + " \"Value\": \"351\""
		// + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{56FA0FCD-4651-4A2F-A885-677FD46F8698}\"" + " }," + " {" + " \"Value\": \"2-深交所行情\\n相关程序开启\"" + " }" + " ]," + " " + " [" + " {" + " \"Value\": \"7-深交所交易\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"2-深交所行情\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"8\"" + " }," + " {" + " \"Value\": \"554\""
		// + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{CE0A438C-85CA-4243-B637-AB40BB55C399}\"" + " }," + " {" + " \"Value\": \"7-深交所交易\\n相关程序开启\"" + " }" + " ]," + " " + " " + " [" + " {" + " \"Value\": \"3-联合风控网关\\n开市前检查\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"开始\"" + " }," + " {" + " \"Value\": \"139\"" + " }," + " {" + " \"Value\": \"162\""
		// + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{9CCD91D2-DFA2-4AB8-9A7D-E79057765B85}\"" + " }," + " {" + " \"Value\": \"3-联合风控网关\\n开市前检查\"" + " }" + " ]," + " " + " " + " [" + " {" + " \"Value\": \"10-联合风控网关\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"(10.40.80.68).(等待导入风控网关数据)\"" + " }," + " {" + " \"Value\": \"163\"" + " }," + " {"
		// + " \"Value\": \"468\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{E187D12D-1E76-4CC4-9FCE-B257E7EBE8B5}\"" + " }," + " {" + " \"Value\": \"10-联合风控网关\\n相关程序开启\"" + " }" + " ]," + " " + " " + " " + " [" + " {" + " \"Value\": \"11-O32交易类\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"10-联合风控网关\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"163\""
		// + " }," + " {" + " \"Value\": \"630\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{14677474-13D7-4083-8622-B5ACF210045F}\"" + " }," + " {" + " \"Value\": \"11-O32交易类\\n相关程序开启\"" + " }" + " ]," + " " + " " + " [" + " {" + " \"Value\": \"12-融航系统\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"10-联合风控网关\\n相关程序开启\"" + " }," + " {"
		// + " \"Value\": \"309\"" + " }," + " {" + " \"Value\": \"624\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{C702676D-EF39-43FB-BEEC-39D3589AD818}\"" + " }," + " {" + " \"Value\": \"12-融航系统\\n相关程序开启\"" + " }" + " ]," + " " + " [" + " {" + " \"Value\": \"13-飞马证券\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"(10.5.87.12).(等待导入\\n飞马证券数据)\"" + " }," + " {"
		// + " \"Value\": \"433\"" + " }," + " {" + " \"Value\": \"626\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{1CB09A7A-2A4E-4056-87CC-E65972E79F33}\"" + " }," + " {" + " \"Value\": \"13-飞马证券\\n相关程序开启\"" + " }" + " ]," + " " + " [" + " {" + " \"Value\": \"8-上交所报盘开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"提示(检查汇总)\"" + " }," + " {"
		// + " \"Value\": \"504\"" + " }," + " {" + " \"Value\": \"325\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{25D9CFD1-98E0-4E1B-89E9-CB44A5042E5F}\"" + " }," + " {" + " \"Value\": \"8-上交所报盘开启\"" + " }" + " ]," + " " + " [" + " {" + " \"Value\": \"4-上交所\\n开市前检查\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"开始\"" + " }," + " {"
		// + " \"Value\": \"242\"" + " }," + " {" + " \"Value\": \"163\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{AB6A2754-3891-4116-A77C-0B1D24DA1FFA}\"" + " }," + " {" + " \"Value\": \"4-上交所\\n开市前检查\"" + " }" + " ]," + " " + " [" + " {" + " \"Value\": \"5-飞马证券\\n开市前检查\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"开始\"" + " }," + " {"
		// + " \"Value\": \"371\"" + " }," + " {" + " \"Value\": \"162\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{5868F968-0498-4716-AE9A-9B901C5F40FC}\"" + " }," + " {" + " \"Value\": \"5-飞马证券\\n开市前检查\"" + " }" + " ]," + " " + " [" + " {" + " \"Value\": \"6-融航系统\\n开市前检查\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"开始\"" + " }," + " {"
		// + " \"Value\": \"504\"" + " }," + " {" + " \"Value\": \"162\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{49770F8F-3141-47CC-9823-D53886E21ED1}\"" + " }," + " {" + " \"Value\": \"6-融航系统\\n开市前检查\"" + " }" + " ]," + " " + " [" + " {" + " \"Value\": \"9-O32非交易类\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"开始\"" + " }," + " {"
		// + " \"Value\": \"632\"" + " }," + " {" + " \"Value\": \"162\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{9E85B3CB-EC3F-4731-AB45-9C03C36D174A}\"" + " }," + " {" + " \"Value\": \"9-O32非交易类\\n相关程序开启\"" + " }" + " ]," + " " + " " + " " + " [" + " {" + " \"Value\": \"14-上交所固收\\n报盘开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"(10.40.80.40).(等待到指定时间)\"" + " },"
		// + " {" + " \"Value\": \"632\"" + " }," + " {" + " \"Value\": \"497\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{E0C15797-D02B-4D55-8B26-8EE875673347}\"" + " }," + " {" + " \"Value\": \"14-上交所固收\\n报盘开启\"" + " }" + " ]" + " " + " " + " ]," + " \"Type\": 19," + " \"Name\": \"k_flow_object\"" + " }," + " {" + " \"Value\": \"\"," + " \"Type\": 4," + " \"Name\": \"{50043442-8A69-4A6B-A8B5-61F882EDE4F3}\"" + " }" + "]";
		//
		// List<Map<String, Object>> mapList = com.alibaba.fastjson.JSONObject.parseObject(testStr, new TypeReference<List<Map<String, Object>>>() {
		// });
		//
		// if (mapList == null || mapList.size() != 2) {
		// return null;
		//
		// } else {
		// Map<String, Object> dataMap = null;
		//
		// for (Map<String, Object> map : mapList) {
		// if (FLOW_NODE_KEY.equals(map.get("Name"))) {
		// dataMap = map;
		// break;
		// }
		// }
		//
		// if (dataMap != null) {
		// List<List<Map<String, Object>>> nodeMapList = (List<List<Map<String, Object>>>) dataMap.get("Value");
		// nodeMapList.remove(0);
		//
		// for (List<Map<String, Object>> nodeMaps : nodeMapList) {
		// KingdomNodeVo nodeVo = buildNodeVo(nodeMaps);
		//
		// if (nodeVo != null) {
		// String name = nodeVo.getName();
		// String[] names = name.split("-");
		//
		// if (names.length == 2) {
		// kingdomVoList.add(nodeVo);
		// }
		// }
		// }
		//
		// Collections.sort(kingdomVoList);
		// }
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// return kingdomVoList;
		return findNodeInfoByFlowId(stockToMarketFlowId);
	}

	public List<KingdomNodeVo> findDealNodeInfo() {
		// List<KingdomNodeVo> kingdomVoList = new ArrayList<>();
		// try {
		//
		// String testStr = "[" + " {" + " \"Value\": [" + " [" + " {" + " \"Type\": 4," + " \"Name\": \"Name\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"State\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"Data\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"Level\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"Desc\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"PID\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"Left\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"Top\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"AgentID\""
		// + " }," + " {" + " \"Type\": 4," + " \"Name\": \"AgentName\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"ObjectID\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"ObjectName\"" + " }" + " ]," + " " + " [" + " {" + " \"Value\": \"1-上交所行情\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"开始\"" + " }," + " {" + " \"Value\": \"7\"" + " }," + " {" + " \"Value\": \"161\"" + " },"
		// + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{86472566-74B0-4809-80D5-C776092B1ECA}\"" + " }," + " {" + " \"Value\": \"1-上交所行情\\n相关程序开启\"" + " }" + " ]," + " " + " " + " [" + " {" + " \"Value\": \"2-深交所行情\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"1-上交所行情\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"8\"" + " }," + " {" + " \"Value\": \"351\""
		// + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{56FA0FCD-4651-4A2F-A885-677FD46F8698}\"" + " }," + " {" + " \"Value\": \"2-深交所行情\\n相关程序开启\"" + " }" + " ]," + " " + " [" + " {" + " \"Value\": \"7-深交所交易\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"2-深交所行情\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"8\"" + " }," + " {" + " \"Value\": \"554\""
		// + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{CE0A438C-85CA-4243-B637-AB40BB55C399}\"" + " }," + " {" + " \"Value\": \"7-深交所交易\\n相关程序开启\"" + " }" + " ]," + " " + " " + " [" + " {" + " \"Value\": \"3-联合风控网关\\n开市前检查\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"开始\"" + " }," + " {" + " \"Value\": \"139\"" + " }," + " {" + " \"Value\": \"162\""
		// + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{9CCD91D2-DFA2-4AB8-9A7D-E79057765B85}\"" + " }," + " {" + " \"Value\": \"3-联合风控网关\\n开市前检查\"" + " }" + " ]," + " " + " " + " [" + " {" + " \"Value\": \"10-联合风控网关\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"(10.40.80.68).(等待导入风控网关数据)\"" + " }," + " {" + " \"Value\": \"163\"" + " }," + " {"
		// + " \"Value\": \"468\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{E187D12D-1E76-4CC4-9FCE-B257E7EBE8B5}\"" + " }," + " {" + " \"Value\": \"10-联合风控网关\\n相关程序开启\"" + " }" + " ]," + " " + " " + " " + " [" + " {" + " \"Value\": \"11-O32交易类\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"10-联合风控网关\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"163\""
		// + " }," + " {" + " \"Value\": \"630\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{14677474-13D7-4083-8622-B5ACF210045F}\"" + " }," + " {" + " \"Value\": \"11-O32交易类\\n相关程序开启\"" + " }" + " ]," + " " + " " + " [" + " {" + " \"Value\": \"12-融航系统\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"10-联合风控网关\\n相关程序开启\"" + " }," + " {"
		// + " \"Value\": \"309\"" + " }," + " {" + " \"Value\": \"624\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{C702676D-EF39-43FB-BEEC-39D3589AD818}\"" + " }," + " {" + " \"Value\": \"12-融航系统\\n相关程序开启\"" + " }" + " ]," + " " + " [" + " {" + " \"Value\": \"13-飞马证券\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"(10.5.87.12).(等待导入\\n飞马证券数据)\"" + " }," + " {"
		// + " \"Value\": \"433\"" + " }," + " {" + " \"Value\": \"626\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{1CB09A7A-2A4E-4056-87CC-E65972E79F33}\"" + " }," + " {" + " \"Value\": \"13-飞马证券\\n相关程序开启\"" + " }" + " ]," + " " + " [" + " {" + " \"Value\": \"8-上交所报盘开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"提示(检查汇总)\"" + " }," + " {"
		// + " \"Value\": \"504\"" + " }," + " {" + " \"Value\": \"325\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{25D9CFD1-98E0-4E1B-89E9-CB44A5042E5F}\"" + " }," + " {" + " \"Value\": \"8-上交所报盘开启\"" + " }" + " ]," + " " + " [" + " {" + " \"Value\": \"4-上交所\\n开市前检查\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"开始\"" + " }," + " {"
		// + " \"Value\": \"242\"" + " }," + " {" + " \"Value\": \"163\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{AB6A2754-3891-4116-A77C-0B1D24DA1FFA}\"" + " }," + " {" + " \"Value\": \"4-上交所\\n开市前检查\"" + " }" + " ]," + " " + " [" + " {" + " \"Value\": \"5-飞马证券\\n开市前检查\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"开始\"" + " }," + " {"
		// + " \"Value\": \"371\"" + " }," + " {" + " \"Value\": \"162\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{5868F968-0498-4716-AE9A-9B901C5F40FC}\"" + " }," + " {" + " \"Value\": \"5-飞马证券\\n开市前检查\"" + " }" + " ]," + " " + " [" + " {" + " \"Value\": \"6-融航系统\\n开市前检查\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"开始\"" + " }," + " {"
		// + " \"Value\": \"504\"" + " }," + " {" + " \"Value\": \"162\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{49770F8F-3141-47CC-9823-D53886E21ED1}\"" + " }," + " {" + " \"Value\": \"6-融航系统\\n开市前检查\"" + " }" + " ]," + " " + " [" + " {" + " \"Value\": \"9-O32非交易类\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"开始\"" + " }," + " {"
		// + " \"Value\": \"632\"" + " }," + " {" + " \"Value\": \"162\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{9E85B3CB-EC3F-4731-AB45-9C03C36D174A}\"" + " }," + " {" + " \"Value\": \"9-O32非交易类\\n相关程序开启\"" + " }" + " ]," + " " + " " + " " + " [" + " {" + " \"Value\": \"14-上交所固收\\n报盘开启\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"正常\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"(10.40.80.40).(等待到指定时间)\"" + " },"
		// + " {" + " \"Value\": \"632\"" + " }," + " {" + " \"Value\": \"497\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{E0C15797-D02B-4D55-8B26-8EE875673347}\"" + " }," + " {" + " \"Value\": \"14-上交所固收\\n报盘开启\"" + " }" + " ]" + " " + " " + " ]," + " \"Type\": 19," + " \"Name\": \"k_flow_object\"" + " }," + " {" + " \"Value\": \"\"," + " \"Type\": 4," + " \"Name\": \"{50043442-8A69-4A6B-A8B5-61F882EDE4F3}\"" + " }" + "]";
		//
		// List<Map<String, Object>> mapList = com.alibaba.fastjson.JSONObject.parseObject(testStr, new TypeReference<List<Map<String, Object>>>() {
		// });
		//
		// if (mapList == null || mapList.size() != 2) {
		// return null;
		//
		// } else {
		// Map<String, Object> dataMap = null;
		//
		// for (Map<String, Object> map : mapList) {
		// if (FLOW_NODE_KEY.equals(map.get("Name"))) {
		// dataMap = map;
		// break;
		// }
		// }
		//
		// if (dataMap != null) {
		// List<List<Map<String, Object>>> nodeMapList = (List<List<Map<String, Object>>>) dataMap.get("Value");
		// nodeMapList.remove(0);
		//
		// for (List<Map<String, Object>> nodeMaps : nodeMapList) {
		// KingdomNodeVo nodeVo = buildNodeVo(nodeMaps);
		//
		// if (nodeVo != null) {
		// String name = nodeVo.getName();
		// String[] names = name.split("-");
		//
		// if (names.length == 2) {
		// kingdomVoList.add(nodeVo);
		// }
		// }
		// }
		//
		// Collections.sort(kingdomVoList);
		// }
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// return kingdomVoList;

		return findNodeInfoByFlowId(dealFlowId);
	}

	public List<KingdomNodeVo> findMarketOpenAtNightNodeInfo() {
		// List<KingdomNodeVo> kingdomVoList = new ArrayList<>();
		// try {
		//
		// String testStr = "[" + " {" + " \"Value\": [" + " [" + " {" + " \"Type\": 4," + " \"Name\": \"Name\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"State\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"Data\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"Level\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"Desc\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"PID\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"Left\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"Top\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"AgentID\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"AgentName\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"ObjectID\"" + " }," + " {" + " \"Type\": 4," + " \"Name\": \"ObjectName\"" + " }" + " ]," + " [" + " {"
		// + " \"Value\": \"1-联合风控网关\\n夜盘开市前检查\"" + " }," + " {" + " \"Value\": \"2\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"开始\"" + " }," + " {" + " \"Value\": \"182\"" + " }," + " {" + " \"Value\": \"19\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{B3C46ACA-ECC4-407B-9894-1665C1A28CE4}\"" + " }," + " {" + " \"Value\": \"1-联合风控网关\\n夜盘开市前检查\"" + " }" + " ]," + " [" + " {" + " \"Value\": \"2-融航系统\\n相关程序关闭\"" + " }," + " {" + " \"Value\": \"0\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"().(等待导入风控网关数据)\""
		// + " }," + " {" + " \"Value\": \"22\"" + " }," + " {" + " \"Value\": \"186\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{05A8C98C-3AD3-44E2-A183-B3013EF24F1B}\"" + " }," + " {" + " \"Value\": \"2-融航系统\\n相关程序关闭\"" + " }" + " ]," + " [" + " {" + " \"Value\": \"3-联合风控网关\\n夜盘开启\\n\"" + " }," + " {" + " \"Value\": \"0\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"2-融航系统\\n相关程序关闭\"" + " }," + " {" + " \"Value\": \"354\"" + " }," + " {" + " \"Value\": \"189\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{CBFA4812-6870-49EA-AAA3-6712CA0EFA07}\"" + " },"
		// + " {" + " \"Value\": \"3-联合风控网关\\n夜盘开启\\n\"" + " }" + " ]," + " [" + " {" + " \"Value\": \"4-融航系统\\n相关程序开启\"" + " }," + " {" + " \"Value\": \"0\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"3-联合风控网关\\n夜盘开启\\n\"" + " }," + " {" + " \"Value\": \"23\"" + " }," + " {" + " \"Value\": \"360\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"\"" + " }," + " {" + " \"Value\": \"{EA39FF79-25D3-410D-838F-168EFE9FA792}\"" + " }," + " {" + " \"Value\": \"4-融航系统\\n相关程序开启\"" + " }" + " ]" + " ]," + " \"Type\": 19," + " \"Name\": \"k_flow_object\"" + " }," + " {" + " \"Value\": \"\"," + " \"Type\": 4," + " \"Name\": \"{50043442-8A69-4A6B-A8B5-61F882EDE4F3}\"" + " }" + "]";
		//
		// List<Map<String, Object>> mapList = com.alibaba.fastjson.JSONObject.parseObject(testStr, new TypeReference<List<Map<String, Object>>>() {
		// });
		//
		// if (mapList == null || mapList.size() != 2) {
		// return null;
		//
		// } else {
		// Map<String, Object> dataMap = null;
		//
		// for (Map<String, Object> map : mapList) {
		// if (FLOW_NODE_KEY.equals(map.get("Name"))) {
		// dataMap = map;
		// break;
		// }
		// }
		//
		// if (dataMap != null) {
		// List<List<Map<String, Object>>> nodeMapList = (List<List<Map<String, Object>>>) dataMap.get("Value");
		// nodeMapList.remove(0);
		//
		// for (List<Map<String, Object>> nodeMaps : nodeMapList) {
		// KingdomNodeVo nodeVo = buildNodeVo(nodeMaps);
		//
		// if (nodeVo != null) {
		// String name = nodeVo.getName();
		// String[] names = name.split("-");
		//
		// if (names.length == 2) {
		// kingdomVoList.add(nodeVo);
		// }
		// }
		// }
		//
		// Collections.sort(kingdomVoList);
		// }
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// return kingdomVoList;
		return findNodeInfoByFlowId(marketOpenAtNightFlowId);
	}

	public KingdomNodeVo getNodeInfo() {
		return getNodeInfoByFlowIdAndNodeName(stockToMarketFlowId, "(192.168.41.189).(启动融航行情)");
	}

	/**
	 * 查询流程下所有的节点信息
	 * 
	 * @param flowId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<KingdomNodeVo> findNodeInfoByFlowId(String flowId) {
		List<KingdomNodeVo> kingdomVoList = new ArrayList<>();

		try {

			List<KingdomRequestVo> kingdomRequestVoList = new ArrayList<KingdomRequestVo>();
			kingdomRequestVoList.add(new KingdomRequestVo("TFlowDM", modelName));
			kingdomRequestVoList.add(new KingdomRequestVo(user, "User"));
			kingdomRequestVoList.add(new KingdomRequestVo(pass, "Pass"));
			kingdomRequestVoList.add(new KingdomRequestVo("GetFlowObejctInfo", methodName));
			kingdomRequestVoList.add(new KingdomRequestVo(flowId, "FlowID"));
			// kingdomRequestVoList.add(new KingdomRequestVo("2023A3E9EB0740678CBDA8B4B8A771CA", "FlowID"));

			List<Map<String, Object>> mapList = this.processSyncKingdom(HttpRequestType.POST, kingdomRequestVoList);

			if (mapList == null || mapList.size() != 2) {
				return null;

			} else {
				Map<String, Object> dataMap = null;

				for (Map<String, Object> map : mapList) {
					if (FLOW_NODE_KEY.equals(map.get("Name"))) {
						dataMap = map;
						break;
					}
				}

				if (dataMap != null) {
					List<List<Map<String, Object>>> nodeMapList = (List<List<Map<String, Object>>>) dataMap.get("Value");
					nodeMapList.remove(0);

					List<String> errorNodeNameList = new ArrayList<>();

					for (List<Map<String, Object>> nodeMaps : nodeMapList) {
						KingdomNodeVo nodeVo = buildNodeVo(nodeMaps, errorNodeNameList);

						if (nodeVo != null) {
							String name = nodeVo.getName();
							String[] names = name.split("-");

							if (names.length == 2) {
								kingdomVoList.add(nodeVo);
							}
						}
					}

					// 判断主节点下的子节点（下钻）是否有异常，如有异常则修改主节点的执行结果
					for (String errorNodeName : errorNodeNameList) {
						for (KingdomNodeVo kingdomVo : kingdomVoList) {
							if (errorNodeName.indexOf(kingdomVo.getName()) != -1) {
								kingdomVo.setExecuteResult("异常");
							}
						}
					}

					Collections.sort(kingdomVoList);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return kingdomVoList;
	}

	@SuppressWarnings("unchecked")
	public List<String> findNodeNameByFlowId(String flowId) {
		List<String> nodeNameList = new ArrayList<>();

		try {
			String token = getToken();

			List<KingdomRequestVo> kingdomRequestVoList = new ArrayList<KingdomRequestVo>();
			kingdomRequestVoList.add(new KingdomRequestVo("TFlowDM", modelName));
			kingdomRequestVoList.add(new KingdomRequestVo(token, "Token"));
			kingdomRequestVoList.add(new KingdomRequestVo("GetFlowObjectVCLParam", methodName));
			kingdomRequestVoList.add(new KingdomRequestVo(flowId, "FlowID"));
			// kingdomRequestVoList.add(new KingdomRequestVo("2023A3E9EB0740678CBDA8B4B8A771CA", "FlowID"));

			List<Map<String, Object>> mapList = this.processSyncKingdom(HttpRequestType.POST, kingdomRequestVoList);

			if (mapList == null || mapList.size() != 2) {
				return null;

			} else {
				Map<String, Object> dataMap = null;

				for (Map<String, Object> map : mapList) {
					if (FLOW_NODE_NAMES_KEY.equals(map.get("Name"))) {
						dataMap = map;
						break;
					}
				}

				if (dataMap != null) {
					List<List<Map<String, Object>>> nodeMapList = (List<List<Map<String, Object>>>) dataMap.get("Value");
					nodeMapList.remove(0);

					for (List<Map<String, Object>> nodeMaps : nodeMapList) {
						nodeNameList.add(buildNodeName(nodeMaps));
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nodeNameList;
	}

	/**
	 * 查询金证自动化流程的节点运行状态
	 *
	 * @param flowId
	 * @param nodeName
	 * @return
	 */
	public KingdomNodeVo getNodeInfoByFlowIdAndNodeName(String flowId, String nodeName) {

		KingdomNodeVo kingdomNodeVo = new KingdomNodeVo();

		try {
			String token = getToken();

			List<KingdomRequestVo> kingdomRequestVoList = new ArrayList<KingdomRequestVo>();
			kingdomRequestVoList.add(new KingdomRequestVo("TBigScreenDM", modelName));
			kingdomRequestVoList.add(new KingdomRequestVo(token, "Token"));
			kingdomRequestVoList.add(new KingdomRequestVo("GetFlowObjectState", methodName));
			// kingdomRequestVoList.add(new KingdomRequestVo(flowId, "FlowID"));
			kingdomRequestVoList.add(new KingdomRequestVo("2023A3E9EB0740678CBDA8B4B8A771CA", "FlowID"));
			kingdomRequestVoList.add(new KingdomRequestVo(nodeName, "ObjectName"));

			List<Map<String, Object>> mapList = this.processSyncKingdom(HttpRequestType.POST, kingdomRequestVoList);

			if (mapList == null || mapList.size() != 2) {
				return null;

			} else {

				for (Map<String, Object> map : mapList) {
					String srcStr = (String) map.get("Value");
					// String srcStr = "数据[数据]级别[正常]描述[描述]开始时间[2020-10-23 08:40:02]结束时间[2020-10-23 08:40:27]执行状态[执行完成]执行耗时[00:00:25]";
					if (srcStr != null && !srcStr.equals("")) {
						kingdomNodeVo.setName(nodeName);
						kingdomNodeVo.setStatus(getValue(srcStr, STATUS_STR));
						kingdomNodeVo.setExecuteResult(getValue(srcStr, EXECUTE_RESULT));
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return kingdomNodeVo;
	}

	public void createMonitorNodeHistory(KingdomNodeVo voBean, String moniterName) {

		MonitorNodeHistory monitorNodeHistory = new MonitorNodeHistory(voBean, moniterName);
		monitorNodeHistory.setId(UUID.randomUUID().toString());

		liquidationMonitorMapper.createMonitorNodeHistory(monitorNodeHistory);
	}

	private KingdomNodeVo buildNodeVo(List<Map<String, Object>> nodeMaps, List<String> errorNodeNameList) {

		String name = (String) nodeMaps.get(0).get("Value");
		String executeResult = (String) nodeMaps.get(3).get("Value");

		// 只取名称为"数字-"开头的节点，如 "1-数据库开启",且过滤掉子节点，如 “1-数据库开启\服务器开启”
		if (!name.matches("^\\d+?-[\\S\\s]*?.[\\S\\s]*?$") || name.indexOf("\\") != -1) {

			// 过滤掉回车和换行
			name = name.replaceAll("\r|\n", "");

			// 记录下所有非正常的节点名称
			if (executeResult != null && !executeResult.isEmpty() && !executeResult.trim().equals("正常")) {
				errorNodeNameList.add(name);
			}

			return null;
		}

		// 过滤掉回车和换行
		name = name.replaceAll("\r|\n", "");

		String status = (String) nodeMaps.get(1).get("Value");
		if (status != null) {
			status = KingdomNodeStatus.parse(status).getDisplayName();
		}

		return new KingdomNodeVo(name, status, executeResult);
	}

	private String buildNodeName(List<Map<String, Object>> nodeMaps) {

		String name = (String) nodeMaps.get(0).get("Value");

		return name;
	}

	/**
	 * 判断token是否过期
	 * 
	 * @param mapList
	 * @return
	 */
	private boolean validateToken(List<Map<String, Object>> mapList) {
		try {
			List<KingdomResponseVo> voList = JSONArray.parseArray(JSONObject.toJSONString(mapList), KingdomResponseVo.class);

			if (voList.get(2) != null) {
				KingdomResponseVo kingdomVo = voList.get(2);

				if (TOKEN_ERROR_INFO.equals(kingdomVo.getName()) && "true".equals(kingdomVo.getValue())) {
					return false;
				}
			}

			return true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 使用正则表达式获取节点运行状态值
	 *
	 * @param srcStr
	 * @param s
	 * @return
	 */
	private String getValue(String srcStr, String s) {

		String pattern = s + "\\[+[\\S*]+?\\]";// 执行状态[未执行]
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(srcStr);
		if (m.find()) {
			int i = m.group().indexOf("[");
			int j = m.group().indexOf("]");
			return m.group().substring(i + 1, j);
		} else {
			return null;
		}
	}

	public List<KingdomNodeVo> refreshNodeHistoryList(MonitorNodeName monitorNodeName, Date monitorDate) {
		List<KingdomNodeVo> nodeVoList = null;
		List<MonitorNodeHistory> nodeModelList = null;

		Date nowDate = DateTimeUtil.parse(DateTimeUtil.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
		monitorDate = DateTimeUtil.parse(DateTimeUtil.format(monitorDate, "yyyy-MM-dd"), "yyyy-MM-dd");

		// 只有取当天数据才会从接口里获取数据
		if (monitorDate.getTime() == nowDate.getTime()) {
			if (monitorNodeName == MonitorNodeName.MOAN) {
				nodeVoList = this.findMarketOpenAtNightNodeInfo();

			} else if (monitorNodeName == MonitorNodeName.STMARKET) {
				nodeVoList = this.findStockToMarketNodeInfo();

			} else if (monitorNodeName == MonitorNodeName.DEAL) {
				nodeVoList = this.findDealNodeInfo();
			}
		}

		if (nodeVoList != null) {
			for (KingdomNodeVo nodeVo : nodeVoList) {
				MonitorNodeHistory monitorNodeHistory = liquidationMonitorMapper.getMonitorNodeHistoryByNodeNameAndTypeAndDate(nodeVo.getName(), monitorNodeName.getValue(), DateTimeUtil.parse(DateTimeUtil.format(monitorDate, "yyyy-MM-dd"), "yyyy-MM-dd"));
				if (monitorNodeHistory == null) {
					this.createMonitorNodeHistory(nodeVo, monitorNodeName.getValue());

				} else if (monitorNodeHistory.getExecuteResult().isEmpty()) { // 仅在执行中后状态变更第一次更新

					monitorNodeHistory.setExecuteResult(nodeVo.getExecuteResult());
					monitorNodeHistory.setStatus(nodeVo.getStatus());

					liquidationMonitorMapper.updateMonitorNodeHistory(monitorNodeHistory);
				}
			}
		}

		nodeModelList = liquidationMonitorMapper.findMonitorNodeHistoryByTypeAndDate(monitorNodeName.getValue(), monitorDate);

		List<KingdomNodeVo> nodeVoTempList = new ArrayList<>();

		if (nodeModelList != null)
			for (MonitorNodeHistory nodeMode : nodeModelList) {
				nodeVoTempList.add(new KingdomNodeVo(nodeMode));
			}
		Collections.sort(nodeVoTempList);

		return nodeVoTempList;
	}

	public void sendSmsInfo(SmsInfoVo smsInfoVo) {
		try {
			List<KingdomRequestVo> kingdomRequestVoList = new ArrayList<KingdomRequestVo>();
			kingdomRequestVoList.add(new KingdomRequestVo("TSystemDM", modelName));
			kingdomRequestVoList.add(new KingdomRequestVo(user, "User"));
			kingdomRequestVoList.add(new KingdomRequestVo(pass, "Pass"));
			kingdomRequestVoList.add(new KingdomRequestVo("SendMessageInfo", methodName));
			kingdomRequestVoList.add(new KingdomRequestVo(smsInfoVo.getPhoneNum(), "PhoneNo"));
			kingdomRequestVoList.add(new KingdomRequestVo(smsInfoVo.getContent(), "Msg"));

			List<Map<String, Object>> mapList = this.processSyncKingdom(HttpRequestType.POST, kingdomRequestVoList);

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
