package com.h3bpm.web.entity;

import java.sql.Timestamp;
import java.util.Date;

import com.h3bpm.web.vo.FileVo;

public class File {
	private String id = null;
	private String parentId = null;
	private String type = null;
	private String name = null;
	private String dir = null;
	private long fileSize;
	private String createUserId = null;
	private Date createTime = null;
	private boolean isDelete = false;
	private Date deleteTime = null; //显示删除时间

	public File(){
		
	}
	
	public File(FileVo voBean){
		this.id = voBean.getId();
		this.parentId = voBean.getParentId();
		this.type = voBean.getType();
		this.name = voBean.getName();
		this.dir = voBean.getDir();
		this.fileSize = voBean.getFileSize();
		this.createUserId = voBean.getCreateUserId();
		this.createTime = voBean.getCreateTime();
		this.isDelete = voBean.getIsDelete();
	}
	
	
	public boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
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

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Timestamp deleteTime) {
		this.deleteTime = deleteTime;
	}

}
