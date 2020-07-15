package com.h3bpm.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.h3bpm.web.enumeration.ApiActionUrl;

@Service
public class UserService extends ApiDataService {

	public List<String> findParentIdsByUserId(String userId) {
		List<String> parentIdList = new ArrayList<>();
		
		return getParentId(parentIdList, userId);
	}

	private List<String> getParentId(List<String> parentIdList, String ouId) {
		try {
			Map<String, Object> result = this.processSyncOrg(String.format(ApiActionUrl.GET_PARENT_ORG.getUrl(), ouId), ApiActionUrl.GET_PARENT_ORG.getHttpRequestType(), null);

			if(result.get("data") == null){
				return parentIdList;
				
			}else{
				parentIdList.add((String) result.get("data"));
				getParentId(parentIdList, (String)result.get("data"));
			}

		} catch (IllegalArgumentException | TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parentIdList;
	}
}
