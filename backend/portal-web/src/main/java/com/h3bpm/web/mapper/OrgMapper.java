package com.h3bpm.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.h3bpm.web.entity.OrgInfo;

/**
 * 部门组织 Mapper
 * 
 * @author tonghao
 *
 */
public interface OrgMapper {

	/**
	 * 根据id查找部门组织
	 * 
	 * @param orgId
	 * @return
	 */
	@Select("SELECT `ObjectID` id, `Name` name, `ParentID` parentId FROM `ot_organizationunit` where ObjectID = #{orgId}")
	public OrgInfo getOrgById(@Param("orgId") String orgId);

	/**
	 * 根据id查找部门组织
	 * 
	 * @param orgId
	 * @return
	 */
	@Select("SELECT `ObjectID` id, `Name` name, `ParentID` parentId FROM `ot_organizationunit` where `Name` = #{orgName} limit 1")
	public OrgInfo getOrgByName(@Param("orgName") String orgName);

	/**
	 * 根据userId查找该用户管理的所有组织
	 * 
	 * @param orgId
	 * @return
	 */
	@Select("SELECT `ObjectID` id, `Name` name, `ParentID` parentId FROM `ot_organizationunit` where `ManagerID` = #{userId}")
	public List<OrgInfo> findOrgByManagerId(@Param("userId") String userId);
}
