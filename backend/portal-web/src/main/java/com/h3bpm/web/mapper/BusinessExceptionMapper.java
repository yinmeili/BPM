package com.h3bpm.web.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.h3bpm.web.mapper.sqlprovider.BusinessExceptionSqlProvider;
import com.h3bpm.web.vo.BusinessExceptionVo;

public interface BusinessExceptionMapper {

	@SelectProvider(type = BusinessExceptionSqlProvider.class, method = "findBusinessException")
	public List<BusinessExceptionVo> findBusinessException(@Param("keyword") String keyword, @Param("userId") String userId, @Param("businessSystem") String businessSystem, @Param("startTimeStart") Date startTimeStart, @Param("startTimeEnd") Date startTimeEnd, @Param("endTimeStart") Date endTimeStart, @Param("endTimeEnd") Date endTimeEnd);
}
