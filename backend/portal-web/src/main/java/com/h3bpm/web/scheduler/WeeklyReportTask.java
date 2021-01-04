package com.h3bpm.web.scheduler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.h3bpm.web.entity.OrgInfo;
import com.h3bpm.web.entity.User;
import com.h3bpm.web.entity.WeeklyReportSendData;
import com.h3bpm.web.enumeration.WorkflowCode;
import com.h3bpm.web.service.KingdomService;
import com.h3bpm.web.service.OrgService;
import com.h3bpm.web.service.UserService;
import com.h3bpm.web.service.WorkFlowService;
import com.h3bpm.web.service.WorkFlowTaskService;
import com.h3bpm.web.utils.FileUtils;
import com.h3bpm.web.vo.SmsInfoVo;

import OThinker.Common.DateTimeUtil;

@Component
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true, encoding = "UTF-8")
public class WeeklyReportTask {
	private static final Logger logger = LoggerFactory.getLogger(WeeklyReportTask.class);

	@Autowired
	private WorkFlowService workFlowService;

	@Autowired
	private WorkFlowTaskService workFlowTaskService;

	@Autowired
	private KingdomService kingdomService;

	@Autowired
	private UserService userService;

	@Autowired
	private OrgService orgService;

	// @Autowired
	// private Environment env;

	private static final String READ_CODE = "check_report";
	private static final String FINISH_CODE = "finish";

	@Value(value = "${application.conf.path}")
	private String CONF_PATH = null;

	@Value(value = "${application.weeklyReport.send.filePath}")
	private String WEEKLY_REPORT_SEND_PATH = null;

	/**
	 * @Author lzf
	 * @Description
	 * @Data
	 * @Param
	 * @return void
	 **/
	@Scheduled(cron = "0 0 8 ? * WED") // 每周三上午8点执行一次
	private void addWeeklyReportProcess() {
		logger.info("======== addWeeklyReportProcess start ========");

		workFlowTaskService.addWeeklyReportWorkFlowTask();

		logger.info("======== addWeeklyReportProcess end ========");
	}

	/**
	 * 定时生成部门待阅周报
	 * 
	 * @Author tonghao
	 * @Description
	 * @Data
	 * @Param
	 * @return void
	 **/
	// @Scheduled(cron = "0 46 9 ? * TUE") // 每周四13点执行一次
	@Scheduled(cron = "0 0 13 ? * THU") // 每周四13点执行一次
	private void addOrgWeeklyReportProcess() {
		logger.info("======== addOrgWeeklyReportProcess start ========");

		InputStream is = null;
		List<WeeklyReportSendData> list = null;
		try {
			is = new FileInputStream(new File(CONF_PATH + WEEKLY_REPORT_SEND_PATH));
			list = FileUtils.importWeeklyReportSend(is);

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

		Date mondayDate = this.getMonday(new Date());
		Date sundayDate = this.getSunday(new Date());

		if (list != null) {
			for (WeeklyReportSendData weeklyReportSendData : list) {
				try {
					List<Map<String, Object>> dataList = new ArrayList<>();
					String loginName = weeklyReportSendData.getLoginName();
					String orgName = weeklyReportSendData.getOrgName();
					int jobLevel = weeklyReportSendData.getJobLevel();

					String title = orgName + "周报[%s - %s]";
					title = String.format(title, DateTimeUtil.format(mondayDate, "yyyy.MM.dd"), DateTimeUtil.format(sundayDate, "yyyy.MM.dd"));

					Map<String, Object> titleData = new HashMap<>();
					titleData.put("itemName", "title");
					titleData.put("itemValue", title);
					dataList.add(titleData);

					Map<String, Object> startTimeData = new HashMap<>();
					startTimeData.put("itemName", "start_time");
					startTimeData.put("itemValue", DateTimeUtil.format(mondayDate, "yyyy-MM-dd HH:mm:ss"));
					dataList.add(startTimeData);

					Map<String, Object> endTimeData = new HashMap<>();
					endTimeData.put("itemName", "end_time");
					endTimeData.put("itemValue", DateTimeUtil.format(sundayDate, "yyyy-MM-dd HH:mm:ss"));
					dataList.add(endTimeData);

					Map<String, Object> jobLevelData = new HashMap<>();
					jobLevelData.put("itemName", "job_level");
					jobLevelData.put("itemValue", jobLevel);
					dataList.add(jobLevelData);

					OrgInfo orgInfo = orgService.getOrgByOrgName(orgName);

					Map<String, Object> orgData = new HashMap<>();
					orgData.put("itemName", "org_id");
					orgData.put("itemValue", orgInfo.getId());
					dataList.add(orgData);

					// 新建一条部门周报给用户
					String instanceId = workFlowService.createWorkFlow(loginName, WorkflowCode.ORG_WEEKLY_REPORT.getValue(), false, dataList);

					// 将部门周报传阅给用户
					workFlowService.activateWorkFlowNode(instanceId, READ_CODE, loginName, null);

					// 结束该条部门周报
					workFlowService.activateWorkFlowNode(instanceId, FINISH_CODE, loginName, null);

					// 发送短信提醒
					User user = userService.getUserByLoginName(loginName);
					if (user != null && user.getMobile() != null && !user.getMobile().isEmpty()) {
						kingdomService.sendSmsInfo(new SmsInfoVo(user.getName(), user.getMobile(), "【协办平台】您有一个新的 部门周报 待阅，请及时查看，谢谢！"));
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

		logger.info("======== addWeeklyReportProcess end ========");
	}

	private Date getMonday(Date date) {
		if (date == null || date.equals("")) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// set the first day of the week is Monday
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cal.getTime();
	}

	private Date getSunday(Date date) {
		if (date == null || date.equals("")) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// set the first day of the week is Monday
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return cal.getTime();
	}

}
