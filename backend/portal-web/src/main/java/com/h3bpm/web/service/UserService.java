package com.h3bpm.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.h3bpm.web.entity.File;
import com.h3bpm.web.entity.FilePermission;
import com.h3bpm.web.enumeration.ApiActionUrl;
import com.h3bpm.web.mapper.FileMapper;
import com.h3bpm.web.mapper.FilePermissionMapper;
import com.h3bpm.web.vo.FilePermissionVo;
import com.h3bpm.web.vo.FileVo;
import com.h3bpm.web.vo.OrgInfoVo;
import com.h3bpm.web.vo.UserInfoVo;

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
