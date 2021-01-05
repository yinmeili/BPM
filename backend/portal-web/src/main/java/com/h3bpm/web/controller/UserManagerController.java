package com.h3bpm.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.h3bpm.web.service.UserService;
import com.h3bpm.web.vo.RespListChildrenOrgByUserIdVo;
import com.h3bpm.web.vo.RespListSubordinateByUserIdVo;

import OThinker.Common.Organization.Interface.IOrganization;
import OThinker.Common.Organization.Models.Unit;
import OThinker.Common.Organization.Models.User;
import OThinker.Common.Organization.enums.State;
import OThinker.Common.Organization.enums.UnitType;
import OThinker.H3.Controller.ControllerBase;

@Controller
@RequestMapping(value = "/Portal/user")
public class UserManagerController extends ControllerBase {

	private static final Logger logger = LoggerFactory.getLogger(UserManagerController.class);

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/listSubordinate", method = RequestMethod.GET, produces = "application/json;charset=utf8")
	@ResponseBody
	public List<RespListSubordinateByUserIdVo> listSubordinate() throws IOException {

		List<RespListSubordinateByUserIdVo> list = new ArrayList<>();

		Map<String, Object> userMap = null;
		try {
			userMap = this._getCurrentUser();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		OThinker.Common.Organization.Models.User loginUser = (OThinker.Common.Organization.Models.User) userMap.get("User");

		List<com.h3bpm.web.entity.User> subordinateList = userService.findSubordinateByUserId(loginUser.getObjectId());

		list.add(new RespListSubordinateByUserIdVo(loginUser.getObjectId(), loginUser.getName()));
		for (com.h3bpm.web.entity.User user : subordinateList) {
			list.add(new RespListSubordinateByUserIdVo(user));
		}

		return list;
	}

	// public List<RespListSubordinateByUserIdVo> listSubordinate() throws InstantiationException, IllegalAccessException, Exception {
	//
	// List<RespListSubordinateByUserIdVo> list = new ArrayList<>();
	//
	// Map<String, Object> userMap = null;
	// try {
	// userMap = this._getCurrentUser();
	// } catch (Exception e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	//
	// OThinker.Common.Organization.Models.User loginUser = (OThinker.Common.Organization.Models.User) userMap.get("User");
	//
	// IOrganization organization = this.getEngine().getOrganization();
	// String unitId = loginUser.getParentID();
	// String managerId = organization.GetOUManager(unitId);
	//
	// list.add(new RespListSubordinateByUserIdVo(loginUser.getObjectId(), loginUser.getName()));
	//
	// List<User> userIdList = null;
	// if (managerId != null && managerId.equals(loginUser.getObjectId())) {
	// userIdList = organization.GetMemberUsers(new String[] { unitId }, State.Active);
	// }
	//
	// for (User user : userIdList) {
	// list.add(new RespListSubordinateByUserIdVo(user.getObjectId(),user.getName()));
	// }
	//
	// return list;
	//
	// // list.add(new RespListSubordinateByUserIdVo(loginUser.getObjectId(), loginUser.getName()));
	// // for (User user : subordinateList) {
	// // list.add(new RespListSubordinateByUserIdVo(user));
	// // }
	// //
	// // return list;
	// }

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/listChildrenOrg", method = RequestMethod.GET, produces = "application/json;charset=utf8")
	@ResponseBody
	public List<RespListChildrenOrgByUserIdVo> listChildrenOrg() throws Exception {

		List<RespListChildrenOrgByUserIdVo> list = new ArrayList<>();

		Map<String, Object> userMap = null;
		try {
			userMap = this._getCurrentUser();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		OThinker.Common.Organization.Models.User loginUser = (OThinker.Common.Organization.Models.User) userMap.get("User");

		IOrganization organization = this.getEngine().getOrganization();
		List<String> unitIdList = organization.GetChildren(loginUser.getParentID(), UnitType.OrganizationUnit, true, State.Active);

		Unit unit = organization.GetUnit(loginUser.getParentID());
		list.add(new RespListChildrenOrgByUserIdVo(unit));

		if (unitIdList != null) {
			for (String unitId : unitIdList) {
				Unit unitChildren = organization.GetUnit(unitId);
				list.add(new RespListChildrenOrgByUserIdVo(unitChildren));
			}
		}

		return list;
	}

	@Override
	public String getFunctionCode() {
		// TODO Auto-generated method stub
		return null;
	}

}
