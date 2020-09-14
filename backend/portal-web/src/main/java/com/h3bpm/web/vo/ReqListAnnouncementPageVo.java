package com.h3bpm.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ReqListAnnouncementPageVo extends ReqPageVo {
    private Integer type = null;
    private String title = null;

    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private Date createTimeStart = null;

    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private Date createTimeEnd = null;

    @Deprecated
    public ReqListAnnouncementPageVo() {

    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(Date createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }
}
