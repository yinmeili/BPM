package com.h3bpm.web.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

public class WorkFlowTask implements java.io.Serializable{
    @Excel(name = "id", orderNum = "0")
    private String id;

    @Excel(name = "instance_id", orderNum = "1")
    private String instanceId;

    @Excel(name = "workflow_code", orderNum = "2")
    private String workFlowCode;

    @Excel(name = "user_login_name", orderNum = "3")
    private String userLoginName;

    @Excel(name = "user_display_name", orderNum = "4")
    private String userDisplayName;

    @Excel(name = "create_time", orderNum = "5")
    private Date createTime;

    @Excel(name = "start_time", orderNum = "6", format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Excel(name = "end_time", orderNum = "7", format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Excel(name = "execute_type", orderNum = "8")
    private int executeType;

    @Excel(name = "param_data", orderNum = "9")
    private String paramData;

    public WorkFlowTask(String id, String instanceId, String workFlowCode, String userLoginName, String userDisplayName, Date createTime, Date startTime, Date endTime, int executeType, String paramData) {
        this.id = id;
        this.instanceId = instanceId;
        this.workFlowCode = workFlowCode;
        this.userLoginName = userLoginName;
        this.userDisplayName = userDisplayName;
        this.createTime = createTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.executeType = executeType;
        this.paramData = paramData;
    }

    public WorkFlowTask() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getWorkFlowCode() {
        return workFlowCode;
    }

    public void setWorkFlowCode(String workFlowCode) {
        this.workFlowCode = workFlowCode;
    }

    public String getUserLoginName() {
        return userLoginName;
    }

    public void setUserLoginName(String userLoginName) {
        this.userLoginName = userLoginName;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public int getExecuteType() {
        return executeType;
    }

    public void setExecuteType(int executeType) {
        this.executeType = executeType;
    }

    public String getParamData() {
        return paramData;
    }

    public void setParamData(String paramData) {
        this.paramData = paramData;
    }

    @Override
    public String toString() {
        return "WorkFlowTask{" +
                "id='" + id + '\'' +
                ", instanceId='" + instanceId + '\'' +
                ", workFlowCode='" + workFlowCode + '\'' +
                ", userLoginName='" + userLoginName + '\'' +
                ", userDisplayName='" + userDisplayName + '\'' +
                ", createTime=" + createTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", executeType=" + executeType +
                ", paramData='" + paramData + '\'' +
                '}';
    }
}
