package com.h3bpm.web.vo;

import com.h3bpm.web.entity.User;

public class RespListSubordinateByUserIdVo {
    private String id = null;
    private String text = null;

    public RespListSubordinateByUserIdVo(String id, String name) {
        this.id = id;
        this.text = name;
    }

    public RespListSubordinateByUserIdVo(User user) {
        this.id = user.getId();
        this.text = user.getName();
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
