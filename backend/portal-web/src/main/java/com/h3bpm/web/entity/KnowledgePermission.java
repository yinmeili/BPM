package com.h3bpm.web.entity;

import com.h3bpm.web.vo.KnowledgePermissionVo;

public class KnowledgePermission {
    private String knowledgeId = null;
    private String orgs = null;

    public KnowledgePermission() {
    }

    public KnowledgePermission(KnowledgePermissionVo knowledgePermissionVo){
        this.knowledgeId = knowledgePermissionVo.getKnowledgeId();
        String s = "";
        for (String arg : knowledgePermissionVo.getOrgs()) {
            s += arg;
            s += ",";
        }
        s = s.substring(0,s.length()-1);
        this.orgs = s;
    }


    public KnowledgePermission(String knowledgeId, String orgs) {
        this.knowledgeId = knowledgeId;
        this.orgs = orgs;
    }

    public String getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(String knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public String getOrgs() {
        return orgs;
    }

    public void setOrgs(String orgs) {
        this.orgs = orgs;
    }
}
