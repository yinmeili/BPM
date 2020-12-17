package com.h3bpm.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.h3bpm.web.mapper.BusinessExceptionMapper;
import com.h3bpm.web.vo.BusinessExceptionVo;
import com.h3bpm.web.vo.query.QueryBusinessExceptionList;

@Service
public class BusinessExceptionService {
	@Autowired
	private BusinessExceptionMapper businessExceptionMapper;

	/**
	 * 分页查询交易异常信息
	 * 
	 * @param queryBean
	 */
	public PageInfo<BusinessExceptionVo> findBusinessExceptionByPage(QueryBusinessExceptionList queryBean) {
		Page<BusinessExceptionVo> page = PageHelper.startPage(queryBean.getPageNum(), queryBean.getiDisplayLength());
		List<BusinessExceptionVo> businessExceptionList = businessExceptionMapper.findBusinessException(queryBean.getKeyword(), queryBean.getUserId(), queryBean.getBusinessSystem(), queryBean.getStartTimeStart(), queryBean.getStartTimeEnd(), queryBean.getEndTimeStart(), queryBean.getEndTimeEnd());

		PageInfo<BusinessExceptionVo> pageInfo = new PageInfo<BusinessExceptionVo>(businessExceptionList);
		pageInfo.setTotal(page.getTotal());

		return pageInfo;
	}
}
