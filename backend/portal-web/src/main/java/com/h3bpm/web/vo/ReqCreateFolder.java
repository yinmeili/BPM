package com.h3bpm.web.vo;

public class ReqCreateFolder {

    private String path;
    private String parentId;//父ID
    private String name;
    private FilePermissionVo filePermission = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FilePermissionVo getFilePermission() {
        return filePermission;
    }

    public void setFilePermission(FilePermissionVo filePermission) {
        this.filePermission = filePermission;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
