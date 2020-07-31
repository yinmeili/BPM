package com.h3bpm.web.vo;

import com.h3bpm.web.entity.Knowledge;
import com.h3bpm.web.entity.MyKnowledge;

import java.util.Date;

public class MyKnowledgeVo {

    private String id;
    private String flowId;
    private String name;
    private String desc;
    private String tagName;
    private Date startTime;
    private Date endTime;
    private String createUserId;
    private Date createTime;
    private String createUserName;
    private String flowCodeDesc;

    public MyKnowledgeVo() {
    }

    public MyKnowledgeVo(MyKnowledge myKnowledge){
        this.id = myKnowledge.getId();
        this.flowId = myKnowledge.getFlowId();
        this.name = myKnowledge.getName();
        this.desc = myKnowledge.getDesc();
        this.tagName = myKnowledge.getTagName();
        this.startTime = myKnowledge.getStartTime();
        this.endTime = myKnowledge.getEndTime();
        this.createUserId = myKnowledge.getCreateUserId();
        this.createTime = myKnowledge.getCreateTime();
        this.createUserName = myKnowledge.getCreateUserName();
        this.flowCodeDesc = myKnowledge.getFlowCodeDesc();
    }

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



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
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

}
