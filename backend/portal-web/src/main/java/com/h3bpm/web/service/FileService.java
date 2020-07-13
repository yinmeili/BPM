package com.h3bpm.web.service;

import com.h3bpm.web.entity.File;
import com.h3bpm.web.entity.FilePermission;
import com.h3bpm.web.mapper.FileMapper;
import com.h3bpm.web.mapper.FilePermissionMapper;
import com.h3bpm.web.utils.Constants;
import com.h3bpm.web.utils.UserSessionUtils;
import com.h3bpm.web.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FileService extends ApiDataService {
	@Autowired
	private FileMapper fileMapper;

	@Autowired
	private FilePermissionMapper filePermissionMapper;

	public List<File> findFileByParentIdAndKeyword(String parentId, String keyword) {
		List<File> fileList = null;
		try {
			fileList = fileMapper.findFileByParentIdAndKeyword(parentId, keyword);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileList;
	}

	/**
	 * 根据ID获取文件
	 * 
	 * @param fileId
	 * @return
	 */
	public File getFileById(String fileId) {
		return fileMapper.getFileById(fileId);
	}

	/**
	 * 根据路径获取文件
	 * 
	 * @param fileId
	 * @return
	 */
	public File getFileByPath(String path) {
		return fileMapper.getFileByPath(path);
	}

	/**
	 * 根据用户ID获取用户删除的文件
	 * 
	 * @param createId
	 * @return
	 */
	public List<File> findDeletedFileByUserId(String userId) {

		List<File> fileList = null;
		try {
			fileList = fileMapper.findDeletedFileByUserId(userId);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileList;

	}

	/**
	 * 新增文件或文件夹
	 * 
	 * @param fileVo
	 * @return 文件ID
	 */
	@Transactional
	public String createFile(FileVo fileVo) {
		String uuid = fileVo.getId();
		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
			fileVo.setId(uuid);
		}

		fileMapper.createFile(new File(fileVo));

		if (fileVo.getFilePermission() != null) {
			fileVo.getFilePermission().setFileId(uuid);

			filePermissionMapper.createFilePermission(new FilePermission(fileVo.getFilePermission()));
		}

		return uuid;
	}

	/**
	 * 删除文件或文件夹
	 *
	 * @param fileVo
	 * @return 文件ID
	 */
	@Transactional
	public void deleteFile(String fileId) {
		File file = fileMapper.getFileById(fileId);
		file.setIsDelete(true);
		file.setDeleteTime(new Date());

		fileMapper.updateFile(file);
	}

	/**
	 * 新增文件或文件夹
	 *
	 * @param fileVo
	 * @return 文件ID
	 */
	@Transactional
	public void updateFile(FileVo fileVo) {
		fileMapper.updateFile(new File(fileVo));

		if (fileVo.getFilePermission() != null) {
			filePermissionMapper.deleteFilePermissionByFileId(fileVo.getId());

			if (fileVo.getFilePermission().getFileId() == null) {
				fileVo.getFilePermission().setFileId(fileVo.getId());
			}
			filePermissionMapper.createFilePermission(new FilePermission(fileVo.getFilePermission()));
		}
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
