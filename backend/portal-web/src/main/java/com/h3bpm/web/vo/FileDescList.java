package com.h3bpm.web.vo;

import java.util.List;

/**
 * Created by tonghao on 2020/3/1.
 */
public class FileDescList {
    private List<FileDesc> result;

    private String parentId;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public FileDescList() {
    }

    public FileDescList(List<FileDesc> result) {
        this.result = result;
    }

    public List<FileDesc> getResult() {
        return result;
    }

    public void setResult(List<FileDesc> result) {
        this.result = result;
    }
}
