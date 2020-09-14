package com.h3bpm.web.mapper;

import com.h3bpm.web.entity.Announcement;
import com.h3bpm.web.mapper.sqlprovider.AnnouncementSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

public interface AnnouncementMapper {

    @Select("SELECT * FROM `h3bpm`.`ot_announcement`")
    public List<Announcement> findAnnouncement();

    @Insert({"INSERT INTO `h3bpm`.`ot_announcement` (`id`, `title`, `description`, `create_user_id`, `create_time`, `start_time`, `end_time`, `type`) VALUES (#{id}, #{title}, #{description}, #{createUserId}, #{createTime}, #{startTime}, #{endTime}, #{type})"})
    public void createAnnouncement(Announcement announcement);

    @Update({"UPDATE `h3bpm`.`ot_announcement` SET `title`= #{title}, `description`=#{description}, `create_user_id`=#{createUserId}, `create_time`=#{createTime}, `start_time`=#{startTime}, `end_time`=#{endTime}, `type` = #{type} WHERE `id`=#{id}"})
    public void updateAnnouncementById(@Param("id") String id, Announcement announcement);

    @Delete({"DELETE FROM `h3bpm`.`ot_announcement` WHERE id = #{id}"})
    public void deleteAnnouncementById(@Param("id") String id);

    @SelectProvider(type = AnnouncementSqlProvider.class, method = "findAnnouncementByTime")
    public List<Announcement> findAnnouncementByTime(@Param("date") Date date);

    @SelectProvider(type = AnnouncementSqlProvider.class, method = "findAnnouncementByPage")
    public List<Announcement> findAnnouncementByPage(@Param("type") Integer type, @Param("title") String title, @Param("createTimeStart") Date createTimeStart, @Param("createTimeEnd") Date createTimeEnd);

}
