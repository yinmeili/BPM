package com.h3bpm.web.vo;

import com.h3bpm.web.entity.Announcement;

public class RespFindShowAllVo {
    private String id = null;
    private String title = null;
    private String description = null;
    private String createUserId = null;
    private long createTime = 0;
    private String link = null;
    private int type = 0;

    public RespFindShowAllVo(Announcement announcement) {
        this.id = announcement.getId();
        this.title = announcement.getTitle();
        this.description = announcement.getDescription();
        this.createUserId = announcement.getCreateUserId();
        this.createTime = announcement.getCreateTime().getTime();
        this.link = announcement.getLink();
        this.type = announcement.getType();
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
