package com.h3bpm.web.mapper;

import com.h3bpm.web.entity.User;
import com.h3bpm.web.mapper.sqlprovider.UserSqlProvider;
import org.apache.ibatis.annotations.Param;
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

}
