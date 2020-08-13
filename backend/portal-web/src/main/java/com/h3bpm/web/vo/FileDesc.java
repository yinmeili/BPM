package com.h3bpm.web.vo;

import com.h3bpm.web.entity.File;

import java.util.Date;

/**
 * Created by tonghao on 2020/3/1.
 */
public class FileDesc {

	// list
	private String id;
	private String name;
	private String rights;
	private long size;
	private Date date;
	private String type;
	private String dir;

	private String parentId;


	// rename remove
	private boolean success;
	private String error;

	// 文件内容
	private String result;

	private FilePermissionVo filePermission = null;
	
	private String createUserId;

	public FileDesc() {
	}

	public FileDesc(File file) {
		this.id = file.getId();
		this.name = file.getName();
		this.rights = null;
		this.size = file.getFileSize();
		this.date = file.getCreateTime();
		this.type = file.getType();
		this.parentId = file.getParentId();
		this.dir = file.getDir();
		this.createUserId = file.getCreateUserId();
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param name
	 *            文件名
	 * @param rights
	 *            权限
	 * @param size
	 *            大小
	 * @param date
	 *            最后修改日期
	 * @param type
	 *            文件类型
	 *
	 * @param parentId
	 */
	public FileDesc(String name, String rights,long size, Date date, String type) {
		this.name = name;
		this.rights = rights;
		this.size = size;
		this.date = date;
		this.type = type;

	}

	public FileDesc(String name, long size, Date date, String type) {
		this.name = name;
		this.size = size;
		this.date = date;
		this.type = type;

	}

	public FileDesc(boolean success, String error) {
		this.success = success;
		this.error = error;
	}

	public FileDesc(String result) {
		this.result = result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRights() {
		return rights;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
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
}
