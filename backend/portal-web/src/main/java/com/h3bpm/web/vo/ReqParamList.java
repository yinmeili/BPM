package com.h3bpm.web.vo;

/**
 * Created by tonghao on 2020/3/1.
 */
public class ReqParamList {

    private ReqParam params;

    public ReqParamList() {
    }

    public ReqParamList(ReqParam params) {
        this.params = params;
    }

    public ReqParam getParams() {
        return params;
    }

    public void setParams(ReqParam params) {
        this.params = params;
    }
}
