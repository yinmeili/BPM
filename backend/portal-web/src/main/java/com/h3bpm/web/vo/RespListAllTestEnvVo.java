package com.h3bpm.web.vo;

public class RespListAllTestEnvVo {
    private String id = null;
    private String text = null;

    public RespListAllTestEnvVo(TestEnvVo voBean) {
        this.id = voBean.getId();
        this.text = voBean.getName();
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
