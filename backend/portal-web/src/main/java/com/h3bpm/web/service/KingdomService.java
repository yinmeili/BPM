package com.h3bpm.web.service;

import com.h3bpm.web.enumeration.HttpRequestType;
import com.h3bpm.web.vo.api.kingdom.KingdomRequestVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * This class is designed to get Kingdom Token.
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
}
