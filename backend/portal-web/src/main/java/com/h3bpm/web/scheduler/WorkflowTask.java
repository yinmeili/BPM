package com.h3bpm.web.scheduler;

import com.h3bpm.web.entity.File;
import com.h3bpm.web.entity.WorkFlowTask;
import com.h3bpm.web.service.WorkFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class WorkflowTask {

	@Autowired
	private WorkFlowService workFlowService;


	/**
	 * @Author tonghao
	 * @Description 设置每5分钟执行一次
	 * @Date 14:23 2019/1/24
	 * @Param
	 * @return void
	 **/
	@Scheduled(cron = "* */5 * * * ?")
	private void process() {
		List<WorkFlowTask> workflowTasks = workFlowService.findWorkFlowTask();
		for (WorkFlowTask workFlowTask : workflowTasks) {
			workFlowService.createWorkFlow(workFlowTask.getId());
		}
	}
}
