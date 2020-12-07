package com.h3bpm.web.mapper;

import com.h3bpm.web.entity.User;
import com.h3bpm.web.mapper.sqlprovider.UserSqlProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface UserMapper {

	/**
	 * 查找指定userId下的所有下级
	 *
	 * @param userId
	 * @return
	 */
	@SelectProvider(type = UserSqlProvider.class, method = "findSubordinateByUserId")
	public List<User> findSubordinateByUserId(@Param("userId") String userId);

	/**
	 * 根据登录名查找用户信息
	 *
	 * @param userId
	 * @return
	 */
	@SelectProvider(type = UserSqlProvider.class, method = "getUserByLoginName")
	public User getUserByLoginName(@Param("loginName") String loginName);

	/**
	 * 根据用户名查找用户登录名
	 *
	 * @param userId
	 * @return
	 */
	@Select(("SELECT `Code` from `ot_user` where `Name` = #{userDisplayName} limit 1"))
	public String getUserLoginNameByUserDisplayName(@Param("userDisplayName") String userDisplayName);
}
