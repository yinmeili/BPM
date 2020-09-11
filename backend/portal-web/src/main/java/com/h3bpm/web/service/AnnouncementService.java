package com.h3bpm.web.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.h3bpm.web.entity.Announcement;
import com.h3bpm.web.entity.Knowledge;
import com.h3bpm.web.mapper.AnnouncementMapper;
import com.h3bpm.web.vo.KnowledgeVo;
import com.h3bpm.web.vo.RespFindShowAllVo;
import com.h3bpm.web.vo.query.QueryAnnouncementList;
import com.h3bpm.web.vo.query.QueryKnowledgeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementMapper announcementMapper;

    /**
     * 查询公告展示列表
     *
     * @param date
     * @return
     */
    public List<Announcement> findAnnouncementByTime(Date date) {
        List<Announcement> announcementList = null;
        try {
            announcementList = announcementMapper.findAnnouncementByTime(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return announcementList;
    }

    /**
     * 分页查询公告列表
     *
     * @param queryBean
     * @return
     */
    /*public PageInfo<RespFindShowAllVo> findKnowledgeByPage(QueryAnnouncementList queryBean) {
        Page<Announcement> page = PageHelper.startPage(queryBean.getPageNum(), queryBean.getiDisplayLength());
        List<Announcement> announcementList = announcementMapper.findAnnouncementByPage(queryBean.getType(), queryBean.getTitle(), queryBean.getCreateTimeStart(), queryBean.getCreateTimeEnd(), queryBean.getQueryUserId(), queryBean.getUserAllParentIds());

        List<RespFindShowAllVo> respFindShowAllVoList = new ArrayList<RespFindShowAllVo>();
        if (announcementList != null) {
            for (Announcement announcement : announcementList) {
                respFindShowAllVoList.add(new RespFindShowAllVo(announcement));
            }
        }
        PageInfo<RespFindShowAllVo> pageInfo = new PageInfo<RespFindShowAllVo>(respFindShowAllVoList);
        pageInfo.setTotal(page.getTotal());

        return pageInfo;
    }*/

    public List<Announcement> findAnnouncement() {
        List<Announcement> AnnouncementList = null;
        try {
            AnnouncementList = announcementMapper.findAnnouncement();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return AnnouncementList;
    }

    /**
     * 新增Announcement
     *
     * @param announcement
     */
    public void createAnnouncement(Announcement announcement) {
        if (announcement.getId() != null && !announcement.getId().isEmpty())
            announcementMapper.createAnnouncement(announcement);
    }

    /**
     * 根据流程ID更新Announcement
     *
     * @param announcement
     */
    public void updateAnnouncementById(String id, Announcement announcement) {
        if (id != null && !id.isEmpty())
            announcementMapper.updateAnnouncementById(id, announcement);
    }

    /**
     * 根据流程ID删除Announcement
     *
     * @param id
     */
    public void deleteAnnouncementById(String id) {
        if (id != null && !id.isEmpty())
            announcementMapper.deleteAnnouncementById(id);
    }

}
