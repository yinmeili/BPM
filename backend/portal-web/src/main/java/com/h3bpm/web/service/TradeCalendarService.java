package com.h3bpm.web.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.h3bpm.web.mapper.TradeCalendarMapper;

@Service
public class TradeCalendarService {
	@Autowired
	private TradeCalendarMapper tradeCalendarMapper;

	/**
	 * 查询下一次交易所时间
	 * 
	 * @param queryBean
	 */
	public Date getNextTradeDateByDate(Date nowDate) {
		return tradeCalendarMapper.getNextTradeDateByDate(nowDate);
	}
}
