package com.h3bpm.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.h3bpm.web.entity.OrgInfo;
import com.h3bpm.web.entity.WeeklyReportSendData;
import com.h3bpm.web.enumeration.WorkflowCode;
import com.h3bpm.web.service.OrgService;
import com.h3bpm.web.service.UserService;
import com.h3bpm.web.utils.FileUtils;
import com.h3bpm.web.vo.RespListChildrenOrgByUserIdVo;
import com.h3bpm.web.vo.RespListSubordinateByUserIdVo;
import com.h3bpm.web.vo.SmsInfoVo;

import OThinker.Common.DateTimeUtil;
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

	@Autowired
	private OrgService orgService;

	@Value(value = "${application.conf.path}")
	private String CONF_PATH = null;

	@Value(value = "${application.weeklyReport.send.filePath}")
	private String WEEKLY_REPORT_SEND_PATH = null;

	@RequestMapping(value = "/listSubordinate", method = RequestMethod.GET, produces = "application/json;charset=utf8")
	@ResponseBody
	public List<RespListSubordinateByUserIdVo> listSubordinate(@RequestParam(required = false, name = "key") String key) throws InstantiationException, IllegalAccessException, Exception {

		List<RespListSubordinateByUserIdVo> list = new ArrayList<>();

		Map<String, Object> userMap = null;
		try {
			userMap = this._getCurrentUser();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		OThinker.Common.Organization.Models.User loginUser = (OThinker.Common.Organization.Models.User) userMap.get("User");

		IOrganization organization = UserManagerController.getEngine().getOrganization();
		List<OrgInfo> orgList = orgService.findOrgByManagerId(loginUser.getObjectId());
		List<User> userList = null;

		List<String> orgIdList = new ArrayList<>();
		if (orgList != null) {
			for (OrgInfo org : orgList) {
				orgIdList.add(org.getId());
			}
		}

		String[] orgIds = orgIdList.toArray(new String[orgIdList.size()]);

		userList = organization.GetMemberUsers(orgIds, State.Active);

		if (userList != null) {
			for (User user : userList) {
				if (key != null && !key.isEmpty()) {
					if (user.getName().indexOf(key) != -1) {
						list.add(new RespListSubordinateByUserIdVo(user.getObjectId(), user.getName()));
					}

				} else {
					list.add(new RespListSubordinateByUserIdVo(user.getObjectId(), user.getName()));
				}

			}
		}

		return list;
	}

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
		List<Unit> unitList = orgService.findAllOrgByManagerId(loginUser.getObjectId());
		
		List<String> unitIdListTemp = new ArrayList<>();
		if(unitList != null)
		for(Unit unit:unitList){
			unitIdListTemp.add(unit.getObjectID());
		}

		//
		// if (orgList != null) {
		// for (OrgInfo org : orgList) {
		// orgListTemp.add(org.getId());
		//
		// List<String> unitIdList = organization.GetChildren(org.getId(), UnitType.OrganizationUnit, true, State.Active);
		//
		// if (unitIdList != null) {
		// for (String unitId : unitIdList) {
		// if (!orgListTemp.contains(unitId)) {
		// orgListTemp.add(unitId);
		// }
		// }
		// }
		// }
		// }
		//
		// if (orgListTemp != null) {
		// for (String unitId : orgListTemp) {
		// Unit unitChildren = organization.GetUnit(unitId);
		// list.add(new RespListChildrenOrgByUserIdVo(unitChildren));
		// }
		// }

		InputStream is = null;
		List<WeeklyReportSendData> listWeeklyReportSendData = null;
		try {
			is = new FileInputStream(new File(CONF_PATH + WEEKLY_REPORT_SEND_PATH));
			listWeeklyReportSendData = FileUtils.importWeeklyReportSend(is);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		/*
		 * 读取周报配置文件的用户对应部门，让用户也可以查看配置文件中的下属部门
		 */
		IOrganization organization = this.getEngine().getOrganization();
		if (listWeeklyReportSendData != null) {
			for (WeeklyReportSendData weeklyReportSendData : listWeeklyReportSendData) {
				String loginName = weeklyReportSendData.getLoginName().trim();
				if (loginName.equals(loginUser.getCode())) {
					try {
						List<Map<String, Object>> dataList = new ArrayList<>();
						String orgName = weeklyReportSendData.getOrgName().trim();
						OrgInfo orgInfo = orgService.getOrgByOrgName(orgName);
						Unit unitOrg = organization.GetUnit(orgInfo.getId());
						if (!unitIdListTemp.contains(unitOrg.getObjectId())) {
							unitList.add(unitOrg);
							unitIdListTemp.add(unitOrg.getObjectId());
						}

						List<String> unitIdList = organization.GetChildren(orgInfo.getId(), UnitType.OrganizationUnit, true, State.Active);

						if (unitIdList != null) {
							for (String unitId : unitIdList) {
								Unit unitChildren = organization.GetUnit(unitId);
								if (!unitIdListTemp.contains(unitChildren.getObjectId())) {
//									if(unitChildren.getName().equals("测试组")){
//										System.out.println("测试组");
//									}
									unitList.add(unitChildren);
									unitIdListTemp.add(unitChildren.getObjectId());
								}
							}
						}

					} catch (InstantiationException e) {
						logger.error(e.getMessage());
					} catch (IllegalAccessException e) {
						logger.error(e.getMessage());
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			}
		}
		/* *************************************************************/

		if (unitList != null) {
			for (Unit unit : unitList) {
				list.add(new RespListChildrenOrgByUserIdVo(unit));
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
