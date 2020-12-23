package com.h3bpm.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.h3bpm.base.engine.client.EngineClient;
import com.h3bpm.base.util.AppUtility;
import com.h3bpm.web.entity.User;

import OThinker.Common.Organization.Interface.IOrganization;
import OThinker.Common.Organization.enums.State;

@Service
public class OrgService extends ApiDataService {
	/**
	 * 根据指定部门ID获取所有下属用户
	 * 
	 * @param orgId
	 * @return
	 * @throws Exception
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public List<User> findChildrenUserByOrgId(String orgId) throws InstantiationException, IllegalAccessException, Exception {
		List<User> users = new ArrayList<>();

		EngineClient engine = (EngineClient) AppUtility.getEngine();

		IOrganization organization = engine.getOrganization();
		List<OThinker.Common.Organization.Models.User> userModelList = organization.GetMemberUsers(new String[] { orgId }, State.Active);

		if (userModelList != null) {
			for (OThinker.Common.Organization.Models.User userModel : userModelList) {
				users.add(new User(userModel));
			}
		}

		return users;
	}
}
