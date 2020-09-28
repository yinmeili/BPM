package com.h3bpm.web.mapper;

import com.h3bpm.web.entity.Announcement;
import com.h3bpm.web.mapper.sqlprovider.AnnouncementSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

public interface AnnouncementMapper {

	@Select("SELECT `id`, `title`, `description`, `create_user_id` `createUserId`, `type`, `link`, `update_user_id` `updateUserId`, `update_time` `updateTime`, `create_time` `createTime`, `start_time` `startTime`, `end_time` `endTime`,`org_id` `orgId`,`org_name` `orgName` from `h3bpm`.`ot_announcement` WHERE `id` = #{id}")
	public Announcement getAnnouncementById(@Param("id") String id);

	@Insert({ "INSERT INTO `h3bpm`.`ot_announcement` (`id`, `title`, `description`, `type`, `link`, `start_time`, `end_time`, `create_time`, `create_user_id`,`org_id`,`org_name`) VALUES (#{id}, #{title}, #{description}, #{type}, #{link}, #{startTime}, #{endTime}, #{createTime}, #{createUserId}, #{orgId}, #{orgName})" })
	public void createAnnouncement(Announcement announcement);

	@Update({ "UPDATE `h3bpm`.`ot_announcement` SET `title`= #{title}, `description`=#{description}, `type`=#{type}, `link`=#{link}, `start_time`=#{startTime}, `end_time`=#{endTime}, `update_time`=#{updateTime}, `update_user_id`=#{updateUserId}, `org_id`=#{orgId}, `org_name`=#{orgName} WHERE `id`=#{id}" })
	public void updateAnnouncement(Announcement announcement);

	@Delete({ "DELETE FROM ot_announcement WHERE id = #{id}" })
	public void deleteAnnouncement(@Param("id") String id);

	@SelectProvider(type = AnnouncementSqlProvider.class, method = "findAnnouncementByTime")
	public List<Announcement> findAnnouncementByTime(@Param("date") Date date);

	@SelectProvider(type = AnnouncementSqlProvider.class, method = "findAnnouncementByPage")
	public List<Announcement> findAnnouncementByPage(@Param("type") Integer type, @Param("title") String title, @Param("createTimeStart") Date createTimeStart, @Param("createTimeEnd") Date createTimeEnd, @Param("orgId") String orgId);

}
