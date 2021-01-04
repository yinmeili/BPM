package com.h3bpm.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.h3bpm.base.engine.client.PortalQuery;
import com.h3bpm.base.model.GridViewModel;
import com.h3bpm.base.user.UserValidator;
import com.h3bpm.base.user.UserValidatorFactory;
import com.h3bpm.web.entity.BizObjectInfo;
import com.h3bpm.web.enumeration.InstanceStatus;
import com.h3bpm.web.enumeration.WorkCalendarStatus;
import com.h3bpm.web.enumeration.WorkflowCode;
import com.h3bpm.web.service.WorkFlowService;
import com.h3bpm.web.vo.ResponseVo;
import com.h3bpm.web.vo.ResponseWorkCalendarVo;
import com.h3bpm.web.vo.WorkCalendarVo;

import OThinker.Common.DateTimeUtil;
import OThinker.Common.DotNetToJavaStringHelper;
import OThinker.Common.Data.BoolMatchValue;
import OThinker.Common.Data.Database.Parameter;
import OThinker.Common.Organization.Interface.IOrganization;
import OThinker.Common.Organization.Models.Unit;
import OThinker.Common.Organization.Models.User;
import OThinker.Common.Organization.enums.State;
import OThinker.Common.Organization.enums.UserServiceState;
import OThinker.Common.util.ListUtil;
import OThinker.H3.Controller.ControllerBase;
import OThinker.H3.Controller.Controllers.ProcessCenter.WorkItemController;
import OThinker.H3.Controller.ViewModels.CirculateItemViewModel;
import OThinker.H3.Controller.ViewModels.WorkItemViewModel;
import OThinker.H3.Entity.Instance.InstanceContext;
import OThinker.H3.Entity.WorkItem.WorkItemState;
import OThinker.H3.Entity.WorkItem.CirculateModels.CirculateItem;
import OThinker.H3.Entity.WorkItem.WorkItemModels.WorkItem;
import OThinker.H3.Entity.WorkItem.WorkItemModels.WorkItemFinished;
import OThinker.H3.Entity.WorkflowTemplate.WorkflowClause;
import data.DataColumn;
import data.DataColumnCollection;
import data.DataException;
import data.DataRow;
import data.DataRowCollection;
import data.DataTable;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/Portal/workCalendar")
public class WorkCalendarController extends ControllerBase {

	private static final Logger logger = LoggerFactory.getLogger(WorkCalendarController.class);

	@Value(value = "${application.api.systemCode}")
	private String systemCode = null;

	@Value(value = "${application.api.secret}")
	private String secret = null;

	@Autowired
	private WorkFlowService workFlowService;

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
	 * 
	 *          参照了 OThinker.H3.Controller.Controllers.ProcessCenter.WorkItemController 类;
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/workitems/findAll", method = RequestMethod.GET)
	@ResponseBody
	public ResponseVo findAll(@RequestParam("userId") String userId, @RequestParam("startTime") String startTimeStr, @RequestParam("endTime") String endTimeStr, String workflowCode, String instanceName) throws Exception {

		int startIndex = -1;
		int endIndex = -1;

		Date startTime = DateTimeUtil.getStringToDate(startTimeStr);
		Date endTime = DateTimeUtil.getStringToDate(endTimeStr);

		WorkItemController controller = new WorkItemController();
		WorkCalendarVo workCalendarVo = null;
		List<WorkCalendarVo> list = new ArrayList<>();
		ResponseWorkCalendarVo responseWorkCalendarVo = null;

		UserValidator validator = getUserValidator(userId);

		/*
		 * **************查询已办任务**************
		 */
		List<WorkItemViewModel> griddataFinish = null;

		String[] conditions1 = getPortalQuery().GetWorkItemConditions(userId, startTime, endTime, WorkItemState.Finished, instanceName, BoolMatchValue.Unspecified, workflowCode, true, WorkItemFinished.TableName);

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
			workCalendarVo.setTitle(initCalendarTitle(workItemViewModel));
			workCalendarVo.setStatus(WorkCalendarStatus.FINISH.getValue());
			workCalendarVo.setStart((DateTimeUtil.getStringToDate(workItemViewModel.getReceiveTime(), null)).getTime());

			list.add(workCalendarVo);

			finishTotal++;
		}
		/* *****************************************/

		/*
		 * **************查询待办任务**************
		 */
		List<WorkItemViewModel> griddataUnfinish = null;

		String[] conditions2 = getPortalQuery().GetWorkItemConditions(userId, startTime, endTime, WorkItemState.Unfinished, instanceName, BoolMatchValue.Unspecified, workflowCode, true, WorkItem.TableName);
		String orderBy2 = " ORDER BY " + WorkItem.TableName + "." + WorkItem.PropertyName_Priority + " DESC," + WorkItem.TableName + "." + WorkItem.PropertyName_Urged + " DESC," + WorkItem.TableName + "." + WorkItem.PropertyName_ReceiveTime + " DESC";

		DataTable dtWorkItem2 = getPortalQuery().QueryWorkItem(conditions2, startIndex, endIndex, orderBy2, WorkItem.TableName);

		griddataUnfinish = controller.Getgriddata(dtWorkItem2, columns, true);

		for (WorkItemViewModel workItemViewModel : griddataUnfinish) {

			workCalendarVo = new WorkCalendarVo();

			workCalendarVo.setId(workItemViewModel.getBaseObjectID());
			workCalendarVo.setTitle(initCalendarTitle(workItemViewModel));
			workCalendarVo.setStatus(WorkCalendarStatus.UNFINISH.getValue());
			workCalendarVo.setStart((DateTimeUtil.getStringToDate(workItemViewModel.getReceiveTime(), null)).getTime());

			list.add(workCalendarVo);
		}
		int unfinishTotal = griddataUnfinish == null ? 0 : griddataUnfinish.size();

		/* *****************************************/

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
			workCalendarVo.setTitle(initCalendarTitle(workItemViewModel));
			workCalendarVo.setStatus(WorkCalendarStatus.EXCEED_TIME_LIMIT.getValue());
			workCalendarVo.setStart((DateTimeUtil.getStringToDate(workItemViewModel.getReceiveTime(), null)).getTime());

			list.add(workCalendarVo);
		}
		int exceedTimeLimitTotal = griddataElapsed == null ? 0 : griddataElapsed.size();

		/* *****************************************/

		/*
		 ********************** 查询待阅任务*********************
		 */
		List<Parameter> paramsUnRead = new ArrayList<>();
		String[] conditionsUnRead = getEngine().getPortalQuery().GetWorkItemConditions(paramsUnRead, this.getUserValidator().getUserID(), startTime, endTime, WorkItemState.Unfinished, "", BoolMatchValue.Unspecified, "", false, CirculateItem.TableName);
		DataTable dtWorkitemUnRead = getEngine().getPortalQuery().QueryWorkItem(conditionsUnRead, ListUtil.toArray(paramsUnRead), startIndex, endIndex, "", CirculateItem.TableName);
		getEngine().getPortalQuery().CountWorkItem(conditionsUnRead, ListUtil.toArray(paramsUnRead), CirculateItem.TableName);
		String[] columnsUnRead = new String[] { CirculateItem.PropertyName_OrgUnit };
		List<CirculateItemViewModel> griddataUnReadList = this.Getgriddata(dtWorkitemUnRead, columnsUnRead, false);

		for (CirculateItemViewModel griddataUnRead : griddataUnReadList) {

			workCalendarVo = new WorkCalendarVo();

			workCalendarVo.setId(griddataUnRead.getBaseObjectID());
			workCalendarVo.setTitle(initCalendarTitle(griddataUnRead));
			workCalendarVo.setStatus(WorkCalendarStatus.UNREAD.getValue());
			workCalendarVo.setStart((DateTimeUtil.getStringToDate(griddataUnRead.getReceiveTime(), null)).getTime());

			list.add(workCalendarVo);
		}
		int unReadTotal = griddataUnReadList == null ? 0 : griddataUnReadList.size();

		/* ************************************************************/
		
		responseWorkCalendarVo = new ResponseWorkCalendarVo(list);
		responseWorkCalendarVo.setFinishTotal(finishTotal);
		responseWorkCalendarVo.setUnfinishTotal(unfinishTotal);
		responseWorkCalendarVo.setExceedTimeLimitTotal(exceedTimeLimitTotal);
		responseWorkCalendarVo.setUnReadTotal(unReadTotal);

		return new ResponseVo(responseWorkCalendarVo);
	}

	private String initCalendarTitle(WorkItemViewModel workItemViewModel) {
		StringBuffer title = null;
		int dot = workItemViewModel.getInstanceName().lastIndexOf('.');

		// 去掉任务名称点后后面的字符串，例如 "运维记录.32" 转为 "运维记录"
		if ((dot > -1) && (dot < (workItemViewModel.getInstanceName().length()))) {
			title = new StringBuffer(workItemViewModel.getInstanceName().substring(0, dot));

		} else {
			title = new StringBuffer(workItemViewModel.getInstanceName());
		}

		if (workItemViewModel.getWorkflowCode().equals(WorkflowCode.BUSINESS_EXCEPTION.getValue())) {

			BizObjectInfo bizObjectInfo = workFlowService.getBizObjectInfoByInstanceId(workItemViewModel.getInstanceId());

			return (title.append("-").append(bizObjectInfo.getBusinessSys())).toString();
		}

		return title.toString();
	}

	private String initCalendarTitle(CirculateItemViewModel circulateItemViewModel) {
		StringBuffer title = null;
		int dot = circulateItemViewModel.getInstanceName().lastIndexOf('.');

		// 去掉任务名称点后后面的字符串，例如 "运维记录.32" 转为 "运维记录"
		if ((dot > -1) && (dot < (circulateItemViewModel.getInstanceName().length()))) {
			title = new StringBuffer(circulateItemViewModel.getInstanceName().substring(0, dot));

		} else {
			title = new StringBuffer(circulateItemViewModel.getInstanceName());
		}
		
		if (circulateItemViewModel.getWorkflowCode().equals(WorkflowCode.ORG_WEEKLY_REPORT.getValue())) {

			BizObjectInfo bizObjectInfo = workFlowService.getBizObjectInfoByInstanceIdWithOutSysType(circulateItemViewModel.getInstanceId());

			return bizObjectInfo.getTitle();
		}

		return title.toString();
	}

	private List<CirculateItemViewModel> Getgriddata(DataTable dtWorkItem, String[] columns, boolean unfinishedWorkitem) throws DataException, Exception {
		Map<String, String> unitNames = this.GetUnitNamesFromTable(dtWorkItem, columns);
		Map<String, String> orgOUNames = this.GetParentUnitNamesFromTable(dtWorkItem, new String[] { WorkItem.PropertyName_Originator });
		List<CirculateItemViewModel> griddata = new ArrayList<CirculateItemViewModel>();
		DataRowCollection rows = dtWorkItem.getRows();
		for (int i = 0; i < rows.size(); i++) {
			DataRow row = rows.get(i);

			CirculateItemViewModel tempVar = new CirculateItemViewModel();
			// 浼犻槄鏉ユ簮鍙兘涓虹┖ add by hxc 浼犻槄鏉ユ簮涓虹┖鐨勮瘽鍙栧彂璧蜂汉
			String cCreator = this.GetColumnsValue(row, CirculateItem.PropertyName_Creator);
			if (!DotNetToJavaStringHelper.isNullOrEmpty(cCreator)) {
				tempVar.setCirculateCreator(this.GetColumnsValue(row, CirculateItem.PropertyName_Creator));
				tempVar.setCirculateCreatorName(getColumnNames(row).contains(CirculateItem.PropertyName_CreatorName) ? row.getString(CirculateItem.PropertyName_CreatorName) : null);
			} else {
				String originatorId = row.getString(WorkItem.PropertyName_Originator);
				String originatorName = row.getString(InstanceContext.PropertyName_OriginatorName);
				tempVar.setCirculateCreator(originatorId);
				tempVar.setCirculateCreatorName(originatorName);
			}

			tempVar.setBaseObjectID(this.GetColumnsValue(row, WorkItem.PropertyName_ObjectID));
			tempVar.setInstanceId(row.getString(WorkItem.PropertyName_InstanceId));
			tempVar.setInstanceName(row.getString(InstanceContext.PropertyName_InstanceName));
			tempVar.setWorkflowCode(row.getString(WorkItem.PropertyName_WorkflowCode));
			tempVar.setWorkflowName(getColumnNames(row).contains(WorkflowClause.PropertyName_WorkflowName) ? row.getString(WorkflowClause.PropertyName_WorkflowName) : null);
			tempVar.setDisplayName(row.getString(WorkItem.PropertyName_DisplayName));
			tempVar.setReceiveTime(this.GetValueFromDate(row.getObject(WorkItem.PropertyName_ReceiveTime), WorkItemTimeFormat));
			tempVar.setFinishTime(this.GetValueFromDate(row.getObject(WorkItem.PropertyName_FinishTime), WorkItemTimeFormat));
			// add by linwp@Future 2018.8.3
			tempVar.setParticipant(row.getString(CirculateItem.PropertyName_Participant));
			tempVar.setParticipantName(row.getString(CirculateItem.PropertyName_ParticipantName));
			tempVar.setInstanceCreatedTime(GetValueFromDate(row.getString(PortalQuery.ColumnName_InstanceCreatedTime), WorkItemTimeFormat));
			tempVar.setInstanceState(row.getString(PortalQuery.ColumnName_InstanceState) + "");
			tempVar.setInstanceSequenceNo(row.getString(InstanceContext.PropertyName_SequenceNo) + "");
			tempVar.setOriginator(row.getString(WorkItem.PropertyName_Originator));
			tempVar.setOriginatorName(row.getString(InstanceContext.PropertyName_OriginatorName));
			tempVar.setOriginatorOUName(this.GetValueFromDictionary(orgOUNames, row.getString(WorkItem.PropertyName_Originator)));
			griddata.add(tempVar);
		}
		return griddata;
	}

	private String GetColumnsValue(DataRow row, String columns) throws DataException {
		List<String> columnNames = new ArrayList<String>() {
			@Override
			public boolean contains(Object o) {
				return super.contains(String.valueOf(o).toLowerCase());
			}
		};
		DataColumnCollection dataColumns = row.getTable().getColumns();
		for (int i = 0; i < dataColumns.size(); i++) {
			DataColumn column = dataColumns.get(i);
			columnNames.add(column.getColumnName().toLowerCase());
		}
		return columnNames.contains(columns) ? row.getString(columns) : "";
	}

	private List<String> getColumnNames(DataRow row) {
		List<String> columnNames = new ArrayList<String>() {
			@Override
			public boolean contains(Object o) {
				return super.contains(String.valueOf(o).toLowerCase());
			}
		};
		DataColumnCollection dataColumns = row.getTable().getColumns();
		for (int i = 0; i < dataColumns.size(); i++) {
			DataColumn column = dataColumns.get(i);
			columnNames.add(column.getColumnName().toLowerCase());
		}
		return columnNames;
	}
}
