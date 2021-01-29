package com.h3bpm.web.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.h3bpm.web.entity.Liquidation;
import com.h3bpm.web.entity.ProjectInfo;
import com.h3bpm.web.mapper.sqlprovider.ProjectInfoSqlProvider;

public interface ProjectInfoMapper {
	@Insert({ "INSERT INTO `h3bpm`.`ow_project` (`id`, `name`, `desc`, `leader_id`, `leader_name`, `start_time`,  `end_time`, city) VALUES (#{id}, #{name}, #{desc}, #{leaderId}, #{leaderName}, #{startTime}, #{endTime}, #{city})" })
	public void createProjectInfo(ProjectInfo projectInfo);

	@Update({ "UPDATE `h3bpm`.`ow_project` SET `name`= #{name}, `desc`=#{desc}, `leader_id`=#{leaderId}, `leader_name`=#{leaderName}, `start_time`=#{startTime}, `end_time`=#{endTime}, `city`=#{city} WHERE `id`=#{id}" })
	public void updateProjectInfo(ProjectInfo projectInfo);

	@Delete({ "DELETE FROM ow_project WHERE id = #{id}" })
	public void deleteProjectInfo(@Param("id") String id);

	@SelectProvider(type = ProjectInfoSqlProvider.class, method = "findProjectInfo")
	public List<ProjectInfo> findProjectInfo(@Param("keyword") String keyword, @Param("city") String city, @Param("leaderId") String leaderId, @Param("startTimeStart") Date startTimeStart, @Param("startTimeEnd") Date startTimeEnd, @Param("endTimeStart") Date endTimeStart, @Param("endTimeEnd") Date endTimeEnd);

	@SelectProvider(type = ProjectInfoSqlProvider.class, method = "findProjectInfoByLeaderIdAndStartTimeAndEndTime")
	public List<ProjectInfo> findProjectInfoByLeaderIdAndStartTimeAndEndTime(@Param("leaderId") String leaderId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
