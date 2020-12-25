package com.h3bpm.web.vo;

import com.h3bpm.web.entity.User;

import OThinker.Common.Organization.Models.Unit;

public class RespListChildrenOrgByUserIdVo {
    private String id = null;
    private String text = null;

    public RespListChildrenOrgByUserIdVo(String id, String name) {
        this.id = id;
        this.text = name;
    }

    public RespListChildrenOrgByUserIdVo(Unit model) {
        this.id = model.getObjectID();
        this.text = model.getName();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
