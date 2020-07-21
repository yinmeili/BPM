package com.h3bpm.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.h3bpm.web.mapper.FilePermissionMapper;
import com.h3bpm.web.utils.Constants;
import com.h3bpm.web.utils.UserSessionUtils;
import com.h3bpm.web.vo.FilePermissionVo;
import com.h3bpm.web.vo.OrgInfoVo;
import com.h3bpm.web.vo.UserSessionInfo;

@Service
public class FilePermissionService {
	@Autowired
	private FilePermissionMapper filePermissionMapper;

	public FilePermissionVo getFilePermissionByFileId(String fileId) {
		return new FilePermissionVo(filePermissionMapper.getFilePermissionByFileId(fileId));
	}

	public void createFilePermission(FilePermissionVo filePermission) {
		filePermissionMapper.createFilePermission(new com.h3bpm.web.entity.FilePermission(filePermission));
	}

	public void deleteFilePermissionByFileId(String fileId) {
		filePermissionMapper.deleteFilePermissionByFileId(fileId);
	}
	
	/**
	 * 判断用户是否有文件夹或文件的访问权限
	 *
	 * @param fileId
	 * @param userId
	 * @return
	 */
	public boolean validateFilePermission(String fileId, String userId) {
		FilePermissionVo filePermissionVo = new FilePermissionVo(filePermissionMapper.getFilePermissionByFileId(fileId));

		/*
		 * 判断用户是否有权限查看
		 */
		// if (filePermissionVo.getUserList() != null && !filePermissionVo.getUserList().isEmpty()) {
		// for (UserInfoVo userInfoVo : filePermissionVo.getUserList()) {
		// if (userInfoVo.getId().equals(userId)) {
		// return true;
		// }
		// }
		// }

		/*
		 * 判断用户是否属于部门下面,由于前端数据不能分出部门和用户ID，因此 用户ID和部门ID 全部都放入OrgList中，只做orglist的判断
		 */
		if (filePermissionVo.getOrgList() != null && !filePermissionVo.getOrgList().isEmpty()) {
			UserSessionInfo userSessionInfo = UserSessionUtils.get(Constants.SESSION_USER, UserSessionInfo.class);
			List<String> parentIds = userSessionInfo.getParentIds();
			for (OrgInfoVo orgInfoVo : filePermissionVo.getOrgList()) {

				if (orgInfoVo.getId().equals(userId)) {
					return true;
				}

				for (String parentId : parentIds) {
					if (orgInfoVo.getId().equals(parentId)) {
						return true;
					}
				}
			}
		}

		return false;
	}
}
