package com.h3bpm.web.controller;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.h3bpm.base.login.LoginRestrictUtil;
import com.h3bpm.base.user.UserValidatorFactory;
import com.h3bpm.base.user.UserValidatorHolder;
import com.h3bpm.base.util.AppUtility;
import com.h3bpm.web.service.UserService;
import com.h3bpm.web.utils.Constants;
import com.h3bpm.web.utils.UserSessionUtils;
import com.h3bpm.web.vo.UserSessionInfo;

import OThinker.Common.LanguageConstants;
import OThinker.Common.Clusterware.LogicUnits.AuthenticationType;
import OThinker.Common.Data.DbType;
import OThinker.Common.Data.Database.Parameter;
import OThinker.Common.Organization.Models.Unit;
import OThinker.Common.Organization.Models.User;
import OThinker.Common.Organization.enums.MobileSystem;
import OThinker.Common.charset.H3Charset;
import OThinker.H3.Controller.ControllerBase;
import OThinker.H3.Controller.Controllers.vo.LoginUserInfo;
import data.DataRow;
import data.DataRowCollection;
import data.DataTable;

/**
 * 组织机构服务
 * 
 */
// [Authorize]
@Controller
@RequestMapping(value = { "/Portal/login", "/portal/login" })
public class LoginController extends ControllerBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private UserService userService;

	/**
	 * 构造函数
	 * 
	 */

	public LoginController() {
	}

	/**
	 * 系统登录 add by luwei@Future 2018.8.10modify RequestMethod.Get to be RequestMethod.POST
	 * 
	 * @param userCode
	 *            用户账号
	 * @param password
	 *            用户密码
	 * @return
	 * @throws Exception
	 */
	// update by zhengyj 加上 method=RequestMethod.GET
	@ResponseBody
	@RequestMapping(value = "/LoginIn", method = RequestMethod.POST)
	public final Object LoginIn(@RequestBody LoginUserInfo loginUserInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean loginResult;
		Object result;
		// add by luwei@Future 2018.8.10
		String userCode = loginUserInfo.getUserCode();
		userCode = userCode.toLowerCase();
		String isMobile = loginUserInfo.getIsMobile();
		if (isMobile == null) {
			isMobile = "";
		}
		// add by 陈龙 根据IP设置登陆失败次数
		String clientIp = request.getRemoteAddr();
		if (!LoginRestrictUtil.isIPCanLogin(clientIp) && !isMobile.equals("1")) {
			Map<String, Object> temp = new HashMap<>();
			temp.put("Success", false);
			temp.put("Message", "登陆太频繁，请稍后再试");
			if (!LanguageConstants.getLang().equals("zh_CN")) {
				temp.put("Message", "Login too often, please try again later");
			}
			return temp;
		}
		// add end
		Object codeValidateFromSession = request.getSession().getAttribute("codeValidate");
		// add by 陈龙
		// reason:每次验证，不管对与错，都将验证码从session中删掉,做到一个验证码只用一次
		request.getSession().removeAttribute("codeValidate");
		String codeValidate = (loginUserInfo.getUserValidCode() != "" && loginUserInfo.getUserValidCode() != null) ? loginUserInfo.getUserValidCode().toLowerCase() : "";
		if (LoginRestrictUtil.checkFaileTimes(userCode) && !isMobile.equals("1")) {
			if (codeValidateFromSession == null || "".equals(codeValidate)) {
				Map<String, Object> temp = new HashMap<>();
				temp.put("Success", false);
				temp.put("Message", "请输入验证码");
				if (!LanguageConstants.getLang().equals("zh_CN")) {
					temp.put("Message", "Please enter verification code");
				}
				temp.put("failedCount", LoginRestrictUtil.checkFaileTimes(userCode));
				return temp;
			}
			if (!codeValidate.equals(((String) codeValidateFromSession).toLowerCase())) {
				Map<String, Object> temp = new HashMap<>();
				temp.put("Success", false);
				temp.put("Message", "验证码错误");
				if (!LanguageConstants.getLang().equals("zh_CN")) {
					temp.put("Message", "Verification code error");
				}
				temp.put("failedCount", LoginRestrictUtil.checkFaileTimes(userCode));
				return temp;
			}
		}
		if (!LoginRestrictUtil.canLogin(userCode)) {
			Map<String, Object> temp = new HashMap<>();
			temp.put("Success", false);
			temp.put("Message", "登陆失败次数过多，请稍后重试");
			if (!LanguageConstants.getLang().equals("zh_CN")) {
				temp.put("Message", "There are too many login failures, please try again later");
			}
			temp.put("failedCount", LoginRestrictUtil.checkFaileTimes(userCode));
			return temp;
		}
		try {
			loginResult = UserValidatorFactory.Login(request, AuthenticationType.Forms, "", userCode, loginUserInfo.getPassword());
			// WorkItemService.clearCatche1();
		} catch (Exception ex) {
			ex.printStackTrace();
			// update by luwei : 由于使用用户的输入用户名密码连接引擎，所以此处报错并不准确
			String exceptionMsg = ex.getMessage();
			if (StringUtils.isNotBlank(exceptionMsg) && (exceptionMsg.contains("PasswordInvalid") || exceptionMsg.contains("UserNotExist") || exceptionMsg.contains("UserDisabled"))) {
				Map<String, Object> temp = new HashMap<>();
				temp.put("Success", false);
				if (LanguageConstants.getLang().equals("zh_CN")) {
					temp.put("Message", "用户名密码错误");
				} else {
					temp.put("Message", "The password that you`ve entered is incorrect");
				}
				// add by luwei@Future 2018.8.10
				LoginRestrictUtil.increaseFailTimes(userCode, clientIp);
				temp.put("failedCount", LoginRestrictUtil.checkFaileTimes(userCode));
				return temp;
			}
			// updated by linwp
			Map<String, Object> temp = new HashMap<>();
			temp.put("Success", false);
			temp.put("Message", "连接失败");
			if (!LanguageConstants.getLang().equals("zh_CN")) {
				temp.put("Message", "Connection failed");
			}
			temp.put("failedCount", LoginRestrictUtil.checkFaileTimes(userCode));
			return temp;

		}

		if (loginResult) {
			result = _getCurrentUser();
			String jpushId = loginUserInfo.getjPushID();
			if (StringUtils.isNotBlank(jpushId)) {

				String sql = "select * from " + User.TableName + " where JPushID = ?";
				Parameter[] parameters = new Parameter[] { new Parameter("JPushID", DbType.String, jpushId) };
				DataTable dataTable = getEngine().getPortalQuery().QueryTable(sql, parameters);

				DataRowCollection collection = dataTable.getRows();
				for (int i = 0; i < collection.size(); i++) {
					DataRow row = collection.get(i);
					String id = row.getString(User.PropertyName_ObjectID);
					Unit unit = getEngine().getOrganization().GetUnit(id);
					if (null == unit || !(unit instanceof User)) {
						continue;
					}

					User user = (User) unit;
					user.setJPushID(null);
					getEngine().getOrganization().UpdateUnit(user.getCode(), user);
				}

				Unit unit = getEngine().getOrganization().GetUnit(this.getUserValidator().getUser().getUnitID());
				User user = (User) unit;
				user.setJPushID(jpushId);
				user.setMobileType(getDeviceType(request));
				getEngine().getOrganization().UpdateUnit(user.getCode(), user);
			}

			// Update by linjh
			String _userCode = URLEncoder.encode(this.getUserValidator().getUser().getCode(), H3Charset.DEFAULT_CHARSET);
			Cookie c = new Cookie(_userCode, "false");
			c.setMaxAge(3600);
			response.addCookie(c);
			LoginRestrictUtil.resetFailTimes(userCode, clientIp);
		} else {
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("Success", false);
			if (LanguageConstants.getLang().equals("zh_CN")) {
				temp.put("Message", "用户名密码错误");
			} else {
				temp.put("Message", "The password that you`ve entered is incorrect");
			}
			// add by luwei@Future 2018.8.10
			LoginRestrictUtil.increaseFailTimes(userCode, clientIp);
			// Update by linjh
			AppUtility.OnUserLogout(UserValidatorHolder.get(), request);
			HttpSession session = request.getSession();
			session.invalidate();
			return temp;
		}

		HttpSession session = request.getSession();

		/*
		 * 将当前用户的所有上级ID保存到session中
		 */
		Map<String, Object> userMap = this._getCurrentUser();
		User user = (User) userMap.get("User");
		List<String> parentIds = userService.findParentIdsByUserId(user.getObjectId());
		UserSessionInfo userSessionInfo = new UserSessionInfo();
		userSessionInfo.setParentIds(parentIds);
		userSessionInfo.setUser(user);

		session.setAttribute(Constants.SESSION_USER, userSessionInfo);

		return result;
	}

	private MobileSystem getDeviceType(HttpServletRequest request) {
		String agent = request.getHeader("user-agent");
		if (agent.contains("Android")) {
			return MobileSystem.Android;
		}

		if (agent.contains("iPhone")) {
			return MobileSystem.iOS;
		}

		return MobileSystem.None;
	}

	@Override
	public String getFunctionCode() {
		// TODO Auto-generated method stub
		return null;
	}

}
