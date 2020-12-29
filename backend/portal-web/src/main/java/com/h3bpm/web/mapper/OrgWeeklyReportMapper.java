package com.h3bpm.web.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.h3bpm.web.entity.OrgWeeklyReport;
import com.h3bpm.web.mapper.sqlprovider.OrgWeeklyReportSqlProvider;

public interface OrgWeeklyReportMapper {

	@SelectProvider(type = OrgWeeklyReportSqlProvider.class, method = "findOrgWeeklyReport")
	public List<OrgWeeklyReport> findOrgWeeklyReport(@Param("userId") String userId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("orgId") String orgId);
}
