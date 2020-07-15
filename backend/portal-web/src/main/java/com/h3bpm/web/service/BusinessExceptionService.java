package com.h3bpm.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.h3bpm.web.mapper.BusinessExceptionMapper;
import com.h3bpm.web.vo.BusinessExceptionVo;

@Service
public class BusinessExceptionService {
	@Autowired
	private BusinessExceptionMapper businessExceptionMapper;

	public List<BusinessExceptionVo> findList(String systemType) {
		return businessExceptionMapper.findList(systemType);
	}
}
