package com.h3bpm.web.vo;

public class ReqShareMyKnowledge {
    private String id;
    private KnowledgePermissionVo permission;


    public ReqShareMyKnowledge() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public KnowledgePermissionVo getPermission() {
        return permission;
    }

    public void setPermission(KnowledgePermissionVo permission) {
        this.permission = permission;
    }
}
