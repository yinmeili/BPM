package com.h3bpm.web.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TradeCalendarMapper {

    @Select("SELECT physical_date FROM `ow_trade_calendar` where (sh_trade_flag=1 OR sz_trade_flag=1) AND physical_date>#{nowDate} ORDER BY physical_date LIMIT 1")
    public Date getNextTradeDateByDate(@Param("nowDate") Date nowDate);
}
