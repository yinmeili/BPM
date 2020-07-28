package com.h3bpm.web.entity;


import com.h3bpm.web.vo.KnowledgeVo;

import java.util.Date;

public class Knowledge {
    private String id = null;
    private String type = null;
    private String name = null;
    private String desc = null;
    private String tagName = null;
    private String createUserId = null;
    private String createUserName = null;
    private Date createTime = null;
    private boolean isDelete = false;
    private Date deleteTime = null;
    private String flowId = null;
    private String flowCode = null;
    private String flowCodeDesc = null;

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getFlowCodeDesc() {
        return flowCodeDesc;
    }

    public void setFlowCodeDesc(String flowCodeDesc) {
        this.flowCodeDesc = flowCodeDesc;
    }

    private Date startTime = null;
    private Date endTime = null;



    public Knowledge(KnowledgeVo knowledgeVo) {
        this.id = knowledgeVo.getId();
        this.flowId = knowledgeVo.getFlowId();
        this.name = knowledgeVo.getName();
        this.desc = knowledgeVo.getDesc();
        this.tagName = knowledgeVo.getTagName();
        this.startTime = knowledgeVo.getStartTime();
        this.endTime = knowledgeVo.getEndTime();
        this.createTime = knowledgeVo.getCreateTime();
        this.createUserId = knowledgeVo.getCreateUserId();
        this.createUserName = knowledgeVo.getCreateUserName();
        this.flowCodeDesc = knowledgeVo.getFlowCodeDesc();
    }

    @Deprecated
    public Knowledge() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
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

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getFlowCode() {
        return flowCode;
    }

    public void setFlowCode(String flowCode) {
        this.flowCode = flowCode;
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

    @Override
    public String toString() {
        return "Knowledge{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", tagName='" + tagName + '\'' +
                ", createUserId='" + createUserId + '\'' +
                ", createTime=" + createTime +
                ", isDelete=" + isDelete +
                ", deleteTime=" + deleteTime +
                ", flowId='" + flowId + '\'' +
                ", flowCode='" + flowCode + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
