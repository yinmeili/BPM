package com.h3bpm.web.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.h3bpm.web.entity.WeeklyReportJob;
import com.h3bpm.web.entity.WeeklyReportProject;
import com.h3bpm.web.mapper.sqlprovider.WeeklyReportSqlProvider;

public interface WeeklyReportMapper {

	@SelectProvider(type = WeeklyReportSqlProvider.class, method = "findWeeklyReportJob")
	public List<WeeklyReportJob> findWeeklyReportJob(@Param("userId") String userId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("jobLevel") int jobLevel);

	@SelectProvider(type = WeeklyReportSqlProvider.class, method = "findWeeklyReportProject")
	public List<WeeklyReportProject> findWeeklyReportProject(@Param("userId") String userId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
	
	@SelectProvider(type = WeeklyReportSqlProvider.class, method = "findWeeklyReportJobPlan")
	public List<WeeklyReportJob> findWeeklyReportJobPlan(@Param("userId") String userId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("jobLevel") int jobLevel);

	@SelectProvider(type = WeeklyReportSqlProvider.class, method = "findWeeklyReportProjectPlan")
	public List<WeeklyReportProject> findWeeklyReportProjectPlan(@Param("userId") String userId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
