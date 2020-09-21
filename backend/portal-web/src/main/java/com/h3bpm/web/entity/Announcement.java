package com.h3bpm.web.entity;

import com.h3bpm.web.vo.AnnouncementVo;

import java.util.Date;

public class Announcement {
    private String id = null;
    private String title = null;
    private String description = null;
    private String createUserId = null;
    private Date createTime = null;
    private String link = null;
    private Date startTime = null;
    private Date endTime = null;
    private int type = 0;
    private Date updateTime = null;
    private String updateUserId = null;

    public Announcement() {

    }

    public Announcement(AnnouncementVo announcementVo) {
        this.id = announcementVo.getId();
        this.title = announcementVo.getTitle();
        this.description = announcementVo.getDescription();
        this.createUserId = announcementVo.getCreateUserId();
        this.createTime = new Date(announcementVo.getCreateTime());
        this.link = announcementVo.getLink();
        if (announcementVo.getStartTime() != null) {
            this.startTime = new Date(announcementVo.getStartTime());
        }
        if (announcementVo.getEndTime() != null) {
            this.endTime = new Date(announcementVo.getEndTime());
        }
        this.type = announcementVo.getType();
        if (announcementVo.getUpdateTime() != null) {
            this.updateTime = new Date(announcementVo.getUpdateTime());
        }
        this.updateUserId = announcementVo.getUpdateUserId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }
}
