package com.h3bpm.web.vo;

import com.h3bpm.web.entity.FilePermission;
import com.h3bpm.web.utils.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilePermissionVo {
	private String fileId = null;
	private List<OrgInfoVo> orgList = null;	//原版
	private List<UserInfoVo> userList = null;	//原版

	public FilePermissionVo() {

	}

	public FilePermissionVo(String[] orgIds) {
		orgList = new ArrayList<OrgInfoVo>();
		for(String str:orgIds){
			OrgInfoVo orgInfoVo = new OrgInfoVo();

			System.out.println(str);
			orgInfoVo.setId(str);

			orgList.add(orgInfoVo);
		}
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
