package com.h3bpm.web.controller;

import OThinker.Common.Data.BoolMatchValue;
import OThinker.Common.Data.Database.Parameter;
import OThinker.Common.DateTimeUtil;
import OThinker.Common.DotNetToJavaStringHelper;
import OThinker.Common.Organization.Interface.IOrganization;
import OThinker.Common.Organization.Models.Unit;
import OThinker.Common.Organization.Models.User;
import OThinker.Common.Organization.enums.State;
import OThinker.Common.Organization.enums.UserServiceState;
import OThinker.Common.util.ListUtil;
import OThinker.H3.Controller.ControllerBase;
import OThinker.H3.Controller.Controllers.ProcessCenter.WorkItemController;
import OThinker.H3.Controller.ViewModels.WorkItemViewModel;
import OThinker.H3.Entity.WorkItem.WorkItemModels.WorkItem;
import OThinker.H3.Entity.WorkItem.WorkItemModels.WorkItemFinished;
import OThinker.H3.Entity.WorkItem.WorkItemState;
import com.h3bpm.base.engine.client.PortalQuery;
import com.h3bpm.base.user.UserValidator;
import com.h3bpm.base.user.UserValidatorFactory;
import com.h3bpm.web.enumeration.InstanceStatus;
import com.h3bpm.web.enumeration.WorkCalendarStatus;
import com.h3bpm.web.vo.ResponseVo;
import com.h3bpm.web.vo.ResponseWorkCalendarVo;
import com.h3bpm.web.vo.WorkCalendarVo;
import data.DataTable;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/Portal/workCalendar")
public class WorkCalendarController extends ControllerBase {

	private static final Logger logger = LoggerFactory.getLogger(WorkCalendarController.class);

	@Value(value = "${application.api.systemCode}")
	private String systemCode = null;

	@Value(value = "${application.api.secret}")
	private String secret = null;

	/**
	 * 获取当前Controller的权限编码
	 */
	@Override
	public String getFunctionCode() {
		return "";
	}

	public final PortalQuery getPortalQuery() throws Exception {
		return getEngine().getPortalQuery();
	}

	// 组织结构管理器
	public final IOrganization getOrganization() throws Exception {
		return getEngine().getOrganization();
	}

	protected User getUserByUserId(String userId) throws Exception {
		if (DotNetToJavaStringHelper.isNullOrEmpty(userId)) {
			return null;
		}
		Unit unit = getOrganization().GetUnit(userId);
		if (unit != null && unit instanceof User) {
			User user = (User) unit;
			return user;
		}
		return null;
	}

	// 验证当前用户是否正确
	protected UserValidator getUserValidator(String userId) throws Exception {
		logger.info("Validator User " + userId + ".");

		User user = getUserByUserId(userId);
		if (user == null) {
			return null;
		}

		// add by luwei
		if (user.getIsVirtualUser() || user.getState() == State.Inactive || user.getServiceState() == UserServiceState.Dismissed) {
			return null;
		}

		UserValidator validator = UserValidatorFactory.GetUserValidator(request, getEngine(), user.getCode());
		if (validator == null) {
			return null;
		}

		// this._Engine = (EngineClient) validator.getEngine();
		return validator;
	}

	/**
	 * @throws @Title:
	 *             findAll
	 * @Description: [GET] /workitems/findAll 查询用户的待办、已办和超时任务
	 * @Param: userId 用户id 必填
	 * @param: startTime
	 *             开始时间
	 * @param: endTime
	 *             结束时间
	 * @param: workflowCode
	 *             流程编码
	 * @param: instanceName
	 *             流程实例名称
	 * @Return: ResponseVo
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/workitems/findAll", method = RequestMethod.GET)
	@ResponseBody
	public ResponseVo findAll(@RequestParam("userId") String userId, @RequestParam("startTime") String startTimeStr, @RequestParam("endTime") String endTimeStr, String workflowCode, String instanceName) throws Exception {

		int startIndex = -1;
		int endIndex = -1;

		WorkItemController controller = new WorkItemController();
		WorkCalendarVo workCalendarVo = null;
		List<WorkCalendarVo> list = new ArrayList<>();
		ResponseWorkCalendarVo responseWorkCalendarVo = null;

		UserValidator validator = getUserValidator(userId);

		/*
		 * **************查询已办任务**************
		 */
		List<WorkItemViewModel> griddataFinish = null;

		String[] conditions1 = getPortalQuery().GetWorkItemConditions(userId, DateTimeUtil.getStringToDate(startTimeStr), DateTimeUtil.getDatePlusOne(endTimeStr), WorkItemState.Finished, instanceName, BoolMatchValue.Unspecified, workflowCode, true, WorkItemFinished.TableName);

		String orderBy1 = "ORDER BY " + WorkItemFinished.TableName + "." + WorkItemFinished.PropertyName_ReceiveTime + " DESC";

		DataTable dtWorkItem1 = getPortalQuery().QueryWorkItem(conditions1, startIndex, endIndex, orderBy1, WorkItemFinished.TableName);

		String[] columns = new String[] { WorkItemFinished.PropertyName_OrgUnit };
		griddataFinish = controller.Getgriddata(dtWorkItem1, columns, true);

		int finishTotal = 0;
		for (WorkItemViewModel workItemViewModel : griddataFinish) {
			// 过滤掉已取消的任务
			if (workItemViewModel.getInstanceState().equals(InstanceStatus.CANCEL.getValue())) {
				continue;
			}

			workCalendarVo = new WorkCalendarVo();

			workCalendarVo.setId(workItemViewModel.getBaseObjectID());
			workCalendarVo.setTitle(workItemViewModel.getInstanceName());
			workCalendarVo.setStatus(WorkCalendarStatus.FINISH.getValue());
			workCalendarVo.setStart((DateTimeUtil.getStringToDate(workItemViewModel.getReceiveTime(), null)).getTime());

			list.add(workCalendarVo);

			finishTotal++;
		}
		/******************************************/

		/*
		 * **************查询待办任务**************
		 */
		List<WorkItemViewModel> griddataUnfinish = null;

		String[] conditions2 = getPortalQuery().GetWorkItemConditions(userId, DateTimeUtil.getStringToDate(startTimeStr), DateTimeUtil.getDatePlusOne(endTimeStr), WorkItemState.Unfinished, instanceName, BoolMatchValue.Unspecified, workflowCode, true, WorkItem.TableName);
		String orderBy2 = " ORDER BY " + WorkItem.TableName + "." + WorkItem.PropertyName_Priority + " DESC," + WorkItem.TableName + "." + WorkItem.PropertyName_Urged + " DESC," + WorkItem.TableName + "." + WorkItem.PropertyName_ReceiveTime + " DESC";

		DataTable dtWorkItem2 = getPortalQuery().QueryWorkItem(conditions2, startIndex, endIndex, orderBy2, WorkItem.TableName);

		griddataUnfinish = controller.Getgriddata(dtWorkItem2, columns, true);

		for (WorkItemViewModel workItemViewModel : griddataUnfinish) {

			workCalendarVo = new WorkCalendarVo();

			workCalendarVo.setId(workItemViewModel.getBaseObjectID());
			workCalendarVo.setTitle(workItemViewModel.getInstanceName());
			workCalendarVo.setStatus(WorkCalendarStatus.UNFINISH.getValue());
			workCalendarVo.setStart((DateTimeUtil.getStringToDate(workItemViewModel.getReceiveTime(), null)).getTime());

			list.add(workCalendarVo);
		}
		int unfinishTotal = griddataUnfinish == null ? 0 : griddataUnfinish.size();

		/******************************************/

		/*
		 * **************查询超时任务**************
		 */
		JSONObject json = ExecuteFunctionRun(null);
		if (json != null) {
			return null;
		}

		userId = StringEscapeUtils.escapeSql(userId);

		List<WorkItemViewModel> griddataElapsed = null;

		String[] orgs = DotNetToJavaStringHelper.isNullOrEmpty(userId) ? this.getUserValidator().getViewableOrgs() : userId.split(";");

		instanceName = StringEscapeUtils.escapeSql(instanceName);
		workflowCode = StringEscapeUtils.escapeSql(workflowCode);

		instanceName = DotNetToJavaStringHelper.isNullOrEmpty(instanceName) ? null : instanceName.trim().replace("'", "").replace("--", "");

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Parameter> params = new ArrayList<>();
		String[] conditions = getEngine().getPortalQuery().GetQueryWorkItemConditionsForMonitor(params, orgs, workflowCode, DotNetToJavaStringHelper.isNullOrEmpty(startTimeStr) ? DateTimeUtil.minValue() : df.parse(startTimeStr), DotNetToJavaStringHelper.isNullOrEmpty(endTimeStr) ? DateTimeUtil.maxValue() : DateTimeUtil.addDates(df.parse(endTimeStr), 1), instanceName, WorkItemState.Unfinished, BoolMatchValue.Unspecified, BoolMatchValue.True);

		DataTable dtWorkitem = getEngine().getPortalQuery().QueryWorkItem(conditions, ListUtil.toArray(params), -1, -1, "", WorkItem.TableName);

		String[] columnsElapsed = new String[] { WorkItem.PropertyName_OrgUnit };
		griddataElapsed = controller.Getgriddata(dtWorkitem, columnsElapsed, true);

		for (WorkItemViewModel workItemViewModel : griddataElapsed) {

			workCalendarVo = new WorkCalendarVo();

			workCalendarVo.setId(workItemViewModel.getBaseObjectID());
			workCalendarVo.setTitle(workItemViewModel.getInstanceName());
			workCalendarVo.setStatus(WorkCalendarStatus.EXCEED_TIME_LIMIT.getValue());
			workCalendarVo.setStart((DateTimeUtil.getStringToDate(workItemViewModel.getReceiveTime(), null)).getTime());

			list.add(workCalendarVo);
		}
		int exceedTimeLimitTotal = griddataElapsed == null ? 0 : griddataElapsed.size();

		/******************************************/

		responseWorkCalendarVo = new ResponseWorkCalendarVo(list);
		responseWorkCalendarVo.setFinishTotal(finishTotal);
		responseWorkCalendarVo.setUnfinishTotal(unfinishTotal);
		responseWorkCalendarVo.setExceedTimeLimitTotal(exceedTimeLimitTotal);
		return new ResponseVo(responseWorkCalendarVo);
	}
}
