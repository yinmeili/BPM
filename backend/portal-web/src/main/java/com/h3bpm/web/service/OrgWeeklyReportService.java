package com.h3bpm.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.h3bpm.web.entity.OrgWeeklyReport;
import com.h3bpm.web.mapper.OrgWeeklyReportMapper;
import com.h3bpm.web.vo.query.QueryOrgWeeklyReportList;

@Service
public class OrgWeeklyReportService {
	@Autowired
	private OrgWeeklyReportMapper orgWeeklyReportMapper;

	/**
	 * 分页查询交易异常信息
	 * 
	 * @param queryBean
	 */
	public PageInfo<OrgWeeklyReport> findOrgWeeklyReportByPage(QueryOrgWeeklyReportList queryBean) {
		Page<OrgWeeklyReport> page = PageHelper.startPage(queryBean.getPageNum(), queryBean.getiDisplayLength());
		List<OrgWeeklyReport> dataList = orgWeeklyReportMapper.findOrgWeeklyReport(queryBean.getUserId(), queryBean.getStartTime(), queryBean.getEndTime(), queryBean.getOrgId());

		PageInfo<OrgWeeklyReport> pageInfo = new PageInfo<OrgWeeklyReport>(dataList);
		pageInfo.setTotal(page.getTotal());

		return pageInfo;
	}
}
