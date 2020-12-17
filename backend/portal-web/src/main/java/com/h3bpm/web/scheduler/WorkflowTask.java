package com.h3bpm.web.scheduler;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.h3bpm.web.entity.User;
import com.h3bpm.web.entity.WorkFlowTask;
import com.h3bpm.web.service.KingdomService;
import com.h3bpm.web.service.ServiceException;
import com.h3bpm.web.service.UserService;
import com.h3bpm.web.service.WorkFlowService;
import com.h3bpm.web.service.WorkFlowTaskService;
import com.h3bpm.web.vo.SmsInfoVo;

import OThinker.Common.DateTimeUtil;

@Component
public class WorkflowTask {
	private static final Logger logger = LoggerFactory.getLogger(WorkflowTask.class);

	@Autowired
	private WorkFlowService workFlowService;

	@Autowired
	private WorkFlowTaskService workFlowTaskService;

	@Autowired
	private KingdomService kingdomService;

	@Autowired
	private UserService userService;

	/**
	 * @Author tonghao
	 * @Description 设置每5分钟执行一次
	 * @Date 14:23 2019/1/24
	 * @Param
	 * @return void
	 * @throws ServiceException
	 **/
	@Scheduled(cron = "0 0/5 * * * ?")
	private void process() throws ServiceException {
		logger.info("======== autoStartWorkflowTask start ========");
		try {
			List<WorkFlowTask> workflowTasks = workFlowTaskService.findUnFinishWorkFlowTask();
			for (WorkFlowTask workFlowTask : workflowTasks) {

				Date nowDate = DateTimeUtil.parse(DateTimeUtil.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
				Date taskDate = DateTimeUtil.parse(DateTimeUtil.format(workFlowTask.getStartTime(), "yyyy-MM-dd"), "yyyy-MM-dd");

				/*
				 * 只创建当天任务
				 */
				if (nowDate.getTime() == taskDate.getTime()) {
					workFlowService.createWorkFlow(workFlowTask.getId());

					// 发送提醒短信
					User user = userService.getUserByLoginName(workFlowTask.getUserLoginName());
					if (user != null && user.getMobile() != null && !user.getMobile().isEmpty()) {
						kingdomService.sendSmsInfo(new SmsInfoVo(user.getName(), user.getMobile(), "【协办平台】您有一个新的待办任务，请及时处理，谢谢！"));
					}
				}

			}

		} catch (ServiceException e) {
			logger.error("error to autoStartWorkflowTask : " + e.getMessage());
		}
		
		logger.info("======== autoStartWorkflowTask end ========");
	}

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

}
