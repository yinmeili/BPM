package com.h3bpm.web.scheduler;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.h3bpm.web.enumeration.MonitorNodeName;
import com.h3bpm.web.mapper.LiquidationMonitorMapper;
import com.h3bpm.web.service.KingdomService;

import OThinker.Common.DateTimeUtil;

@Component
public class KingdomMonitorTask {
	private static final Logger logger = LoggerFactory.getLogger(KingdomMonitorTask.class);

	@Value(value = "${application.task.kingdom.monitor.startTime.marketOpenAtNight}")
	private String marketOpenAtNightStartTime = null;
	@Value(value = "${application.task.kingdom.monitor.endTime.marketOpenAtNight}")
	private String marketOpenAtNightEndTime = null;
	
	@Value(value = "${application.task.kingdom.monitor.startTime.stockToMarket}")
	private String stockToMarketStartTime = null;
	@Value(value = "${application.task.kingdom.monitor.endTime.stockToMarket}")
	private String stockToMarketEndTime = null;
	
	@Value(value = "${application.task.kingdom.monitor.startTime.deal}")
	private String dealStartTime = null;
	@Value(value = "${application.task.kingdom.monitor.endTime.deal}")
	private String dealEndTime = null;

	@Autowired
	private LiquidationMonitorMapper liquidationMonitorMapper;

	@Autowired
	private KingdomService kingdomService;

	/**
	 * @Author tonghao
	 * @Date 14:23 2019/1/24
	 * @Param
	 * @return void
	 **/
	@Scheduled(cron = "${application.task.kingdom.monitor.frequency.marketOpenAtNight}")
	private void monitorMarketOpenAtNight() {
		logger.info("======== monitor marketOpenAtNight start ========");

		String nowTimeStr = DateTimeUtil.format(new Date(), "HH:mm:ss");
		Date nowTime = DateTimeUtil.parse(nowTimeStr, "HH:mm:ss");

		Date startTime = DateTimeUtil.parse(marketOpenAtNightStartTime, "HH:mm:ss");
		Date endTime = DateTimeUtil.parse(marketOpenAtNightEndTime, "HH:mm:ss");

		if (nowTime.getTime() >= startTime.getTime() && nowTime.getTime() <= endTime.getTime()) {
			kingdomService.refreshNodeHistoryList(MonitorNodeName.MOAN, new Date());
		}

		logger.info("======== monitor marketOpenAtNight end ========");
	}
	
	
	@Scheduled(cron = "${application.task.kingdom.monitor.frequency.stockToMarket}")
	private void monitorStockToMarket() {
		logger.info("======== monitor stockToMarket start ========");

		String nowTimeStr = DateTimeUtil.format(new Date(), "HH:mm:ss");
		Date nowTime = DateTimeUtil.parse(nowTimeStr, "HH:mm:ss");

		Date startTime = DateTimeUtil.parse(stockToMarketStartTime, "HH:mm:ss");
		Date endTime = DateTimeUtil.parse(stockToMarketEndTime, "HH:mm:ss");

		if (nowTime.getTime() >= startTime.getTime() && nowTime.getTime() <= endTime.getTime()) {
			kingdomService.refreshNodeHistoryList(MonitorNodeName.STMARKET, new Date());
		}

		logger.info("======== monitor stockToMarket end ========");
	}
	
	
	@Scheduled(cron = "${application.task.kingdom.monitor.frequency.deal}")
	private void monitorDeal() {
		logger.info("======== monitor deal start ========");

		String nowTimeStr = DateTimeUtil.format(new Date(), "HH:mm:ss");
		Date nowTime = DateTimeUtil.parse(nowTimeStr, "HH:mm:ss");

		Date startTime = DateTimeUtil.parse(dealStartTime, "HH:mm:ss");
		Date endTime = DateTimeUtil.parse(dealEndTime, "HH:mm:ss");

		if (nowTime.getTime() >= startTime.getTime() && nowTime.getTime() <= endTime.getTime()) {
			kingdomService.refreshNodeHistoryList(MonitorNodeName.DEAL, new Date());
		}

		logger.info("======== monitor deal end ========");
	}
}
