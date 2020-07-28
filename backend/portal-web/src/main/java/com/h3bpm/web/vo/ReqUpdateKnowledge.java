package com.h3bpm.web.vo;

public class ReqUpdateKnowledge {
    private String id;
    private String flowId;
    private String name;
    private String desc;
    private String tagName;
    private String startTime;
    private String endTime;
    private String flowCodeDesc;

    private FilePermissionVo permission = null;

    public ReqUpdateKnowledge() {
    }

    public ReqUpdateKnowledge(String id, String flowId, String name, String desc, String tagName, String startTime, String endTime, FilePermissionVo permission) {
        this.id = id;
        this.flowId = flowId;
        this.name = name;
        this.desc = desc;
        this.tagName = tagName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.permission = permission;
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

    @Override
    public String toString() {
        return "ReqUpdateKnowledge{" +
                "id='" + id + '\'' +
                ", flowId='" + flowId + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", tagName='" + tagName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", permission=" + permission +
                '}';
    }
}
