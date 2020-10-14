package com.h3bpm.web.entity;
import java.util.Date;

public class WorkFlowTask implements java.io.Serializable{
	private static final long serialVersionUID = 8088353609402628248L;
	private String id;
    private String instanceId;
    private String workflowCode;
    private String userLoginName;
    private String userDisplayName;
    private Date createTime;
    private Date startTime;
    private Date endTime;
    private int executeType;
    private String paramData;


    public WorkFlowTask(String id, String instanceId, String workFlowCode, String userLoginName, String userDisplayName, Date createTime, Date startTime, Date endTime, int executeType, String paramData) {
        this.id = id;
        this.instanceId = instanceId;
        this.workflowCode = workFlowCode;
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
        return workflowCode;
    }

    public void setWorkFlowCode(String workFlowCode) {
        this.workflowCode = workFlowCode;
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
                ", workFlowCode='" + workflowCode + '\'' +
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
