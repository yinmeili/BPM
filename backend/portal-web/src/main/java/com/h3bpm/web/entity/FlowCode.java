package com.h3bpm.web.entity;

public class FlowCode {
    private String flowCode;
    private String flowCodeDesc;

    public FlowCode() {
    }

    public FlowCode(String flowCode, String flowCodeDesc) {
        this.flowCode = flowCode;
        this.flowCodeDesc = flowCodeDesc;
    }

    public String getFlowCode() {
        return flowCode;
    }

    public void setFlowCode(String flowCode) {
        this.flowCode = flowCode;
    }

    public String getFlowCodeDesc() {
        return flowCodeDesc;
    }

    public void setFlowCodeDesc(String flowCodeDesc) {
        this.flowCodeDesc = flowCodeDesc;
    }
}
