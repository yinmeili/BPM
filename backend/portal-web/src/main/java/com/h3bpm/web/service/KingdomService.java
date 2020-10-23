package com.h3bpm.web.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.h3bpm.web.enumeration.HttpRequestType;
import com.h3bpm.web.vo.api.kingdom.KingdomRequestVo;
import com.h3bpm.web.vo.api.kingdom.KingdomResponseVo;
import com.h3bpm.web.vo.api.kingdom.KingdomNodeVo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	@Value(value = "${application.api.kingdom.flowId}")
	private String flowId = null;

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
		return findNodeNameByFlowId(flowId);
	}
	
	public List<KingdomNodeVo> findNodeInfo() {
		return findNodeInfoByFlowId(flowId);
	}

    public KingdomNodeVo getNodeInfo() {
        return getNodeInfoByFlowIdAndNodeName(flowId, "(192.168.41.189).(启动融航行情)");
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
			String token = getToken();
//			String token = "b667aa83cf514fae9a3da4e7162e76f2";

			List<KingdomRequestVo> kingdomRequestVoList = new ArrayList<KingdomRequestVo>();
			kingdomRequestVoList.add(new KingdomRequestVo("TFlowDM", modelName));
			kingdomRequestVoList.add(new KingdomRequestVo(token, "Token"));
			kingdomRequestVoList.add(new KingdomRequestVo("GetFlowObejctInfo", methodName));
			kingdomRequestVoList.add(new KingdomRequestVo(flowId, "FlowID"));
//			kingdomRequestVoList.add(new KingdomRequestVo("2023A3E9EB0740678CBDA8B4B8A771CA", "FlowID"));

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

					for (List<Map<String, Object>> nodeMaps : nodeMapList) {
						kingdomVoList.add(buildNodeVo(nodeMaps));
					}

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
//			String token = "b667aa83cf514fae9a3da4e7162e76f2";

List<KingdomRequestVo> kingdomRequestVoList = new ArrayList<KingdomRequestVo>();
			kingdomRequestVoList.add(new KingdomRequestVo("TFlowDM", modelName));
			kingdomRequestVoList.add(new KingdomRequestVo(token, "Token"));
			kingdomRequestVoList.add(new KingdomRequestVo("GetFlowObjectVCLParam", methodName));
			kingdomRequestVoList.add(new KingdomRequestVo(flowId, "FlowID"));
//			kingdomRequestVoList.add(new KingdomRequestVo("2023A3E9EB0740678CBDA8B4B8A771CA", "FlowID"));

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
	}    /**
     * 查询金证自动化流程的节点运行状态
     *
     * @param flowId
     * @param nodeName
     * @return
     */
    @SuppressWarnings("unchecked")
    public KingdomNodeVo getNodeInfoByFlowIdAndNodeName(String flowId, String nodeName) {

        KingdomNodeVo kingdomNodeVo = new KingdomNodeVo();

        try {
            String token = getToken();

            List<KingdomRequestVo> kingdomRequestVoList = new ArrayList<KingdomRequestVo>();
            kingdomRequestVoList.add(new KingdomRequestVo("TBigScreenDM", modelName));
            kingdomRequestVoList.add(new KingdomRequestVo(token, "Token"));
            kingdomRequestVoList.add(new KingdomRequestVo("GetFlowObjectState", methodName));
            kingdomRequestVoList.add(new KingdomRequestVo(flowId, "FlowID"));
            kingdomRequestVoList.add(new KingdomRequestVo(nodeName, "ObjectName"));

            List<Map<String, Object>> mapList = this.processSyncKingdom(HttpRequestType.POST, kingdomRequestVoList);

            if (mapList == null || mapList.size() != 2) {
                return null;

            } else {

                for (Map<String, Object> map : mapList) {
                    String srcStr = (String) map.get("Value");
//                    String srcStr = "数据[数据]级别[正常]描述[描述]开始时间[2020-10-23 08:40:02]结束时间[2020-10-23 08:40:27]执行状态[执行完成]执行耗时[00:00:25]";
                    if (srcStr != null && !srcStr.equals("")) {
                        kingdomNodeVo.setName(nodeName);
                        kingdomNodeVo.setStatusStr(getValue(srcStr, STATUS_STR));
                        kingdomNodeVo.setExecuteResult(getValue(srcStr, EXECUTE_RESULT));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return kingdomNodeVo;
    }

	private KingdomNodeVo buildNodeVo(List<Map<String, Object>> nodeMaps) {

		String name = (String) nodeMaps.get(0).get("Value");
		String status = (String) nodeMaps.get(1).get("Value");
		String executeResult = (String) nodeMaps.get(3).get("Value");

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
    public String getValue(String srcStr, String s) {

        String pattern = s + "\\[+[\\S*]+?\\]";//执行状态[未执行]
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
}
