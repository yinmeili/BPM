package com.h3bpm.web.vo;

import com.h3bpm.web.entity.Tag;

public class RespListKnowledgeTagByNameVo {
    private String id = null;
    private String text = null;

    public RespListKnowledgeTagByNameVo(Tag tag) {
        this.id = tag.getId();
        this.text = tag.getName();
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
