package com.h3bpm.web.vo;

import java.util.Date;

import com.h3bpm.web.entity.File;

public class FileVo {
	private String id = null;
	private String parentId = null;
	private String type = null;
	private String name = null;
	private String dir = null;
	private long fileSize;
	private String createUserId = null;
	private Date createTime = null;
	private int isDelete = 0;
	private Date deleteTime = null;
	private String downloadFileId = null;

	private FilePermissionVo filePermission = null;

	public FileVo() {

	}

	public FileVo(File entity) {
		this.id = entity.getId();
		this.parentId = entity.getParentId();
		this.type = entity.getType();
		this.name = entity.getName();
		this.dir = entity.getDir();
		this.fileSize = entity.getFileSize();
		this.createUserId = entity.getCreateUserId();
		this.createTime = entity.getCreateTime();
		this.isDelete = entity.getIsDelete();
		this.deleteTime = entity.getDeleteTime();
		this.downloadFileId = entity.getDownloadFileId();
	}

	public String getDownloadFileId() {
		return downloadFileId;
	}

	public void setDownloadFileId(String downloadFileId) {
		this.downloadFileId = downloadFileId;
	}

	public FilePermissionVo getFilePermission() {
		return filePermission;
	}

	public void setFilePermission(FilePermissionVo filePermission) {
		this.filePermission = filePermission;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
