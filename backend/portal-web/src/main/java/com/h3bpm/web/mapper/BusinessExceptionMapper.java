package com.h3bpm.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.h3bpm.web.mapper.sqlprovider.BusinessExceptionSqlProvider;
import com.h3bpm.web.vo.BusinessExceptionVo;

public interface BusinessExceptionMapper {

	@SelectProvider(type = BusinessExceptionSqlProvider.class, method = "findList")
	public List<BusinessExceptionVo> findList(@Param("systemType") String systemType);
}
