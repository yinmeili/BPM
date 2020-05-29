package com.h3bpm.web.vo;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.h3bpm.web.entity.FilePermission;
import com.h3bpm.web.utils.ObjectUtil;

public class FilePermissionVo {
	private String fileId = null;
	private List<OrgInfoVo> orgList = null;
	private List<UserInfoVo> userList = null;

	public FilePermissionVo() {

	}

	@SuppressWarnings("unchecked")
	@Autowired
	public FilePermissionVo(FilePermission entityBean) {
		if(entityBean == null){
			return;
		}
		
		this.fileId = entityBean.getFileId();
		try {
			this.orgList = (List<OrgInfoVo>) ObjectUtil.unPersistenceObject(entityBean.getOrgs());
			this.userList = (List<UserInfoVo>) ObjectUtil.unPersistenceObject(entityBean.getUsers());

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public List<OrgInfoVo> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<OrgInfoVo> orgList) {
		this.orgList = orgList;
	}

	public List<UserInfoVo> getUserList() {
		return userList;
	}

	public void setUserList(List<UserInfoVo> userList) {
		this.userList = userList;
	}

}
