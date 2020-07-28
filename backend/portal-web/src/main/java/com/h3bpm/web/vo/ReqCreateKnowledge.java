package com.h3bpm.web.vo;

public class ReqCreateKnowledge {
    private String flowId;
    private String name;
    private String desc;
    private String tagName;
    private String startTime;
    private String endTime;
    private String flowCodeDesc;

    private FilePermissionVo permission = null;

    public String getFlowCodeDesc() {
        return flowCodeDesc;
    }

    public void setFlowCodeDesc(String flowCodeDesc) {
        this.flowCodeDesc = flowCodeDesc;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public FilePermissionVo getPermission() {
        return permission;
    }

    public void setPermission(FilePermissionVo permission) {
        this.permission = permission;
    }
}
