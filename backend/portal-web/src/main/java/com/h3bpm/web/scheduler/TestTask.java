package com.h3bpm.web.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.h3bpm.web.entity.File;
import com.h3bpm.web.service.FileService;

@Component
public class TestTask {
	private int count = 0;
	
	@Autowired
	private FileService fileService;


	/**
	 * @Author tonghao
	 * @Description 设置没6秒执行一次
	 * @Date 14:23 2019/1/24
	 * @Param
	 * @return void
	 **/
	@Scheduled(cron = "*/6 * * * * ?")
	private void process() {
		List<File> files = fileService.findFileByParentIdAndKeyword("111", null);
		System.out.println("this is scheduler task running " + (count++));
	}
}
