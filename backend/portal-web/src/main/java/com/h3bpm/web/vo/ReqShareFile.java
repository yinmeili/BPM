package com.h3bpm.web.vo;

public class ReqShareFile {

	private String fileId = null;
	private String shareFileParentId = null;
	private FilePermissionVo filePermission = null;

	public FilePermissionVo getFilePermission() {
		return filePermission;
	}

	public void setFilePermission(FilePermissionVo filePermission) {
		this.filePermission = filePermission;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getShareFileParentId() {
		return shareFileParentId;
	}

	public void setShareFileParentId(String shareFileParentId) {
		this.shareFileParentId = shareFileParentId;
	}

}
