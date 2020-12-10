package com.h3bpm.web.service;

import com.h3bpm.web.entity.User;
import com.h3bpm.web.enumeration.ApiActionUrl;
import com.h3bpm.web.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends ApiDataService {

	@Autowired
	private UserMapper userMapper;

	/**
	 * 根据指定userId获取所有下级
	 * @param userId
	 * @return
	 */
	public List<User> findSubordinateByUserId(String userId) {

		List<User> subordinateList = null;
		try {
			subordinateList = userMapper.findSubordinateByUserId(userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return subordinateList;
	}

	public List<String> findParentIdsByUserId(String userId) {
		List<String> parentIdList = new ArrayList<>();

		return getParentId(parentIdList, userId);
	}
	
	public User getUserByLoginName(String loginName) {
		return userMapper.getUserByLoginName(loginName);
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
