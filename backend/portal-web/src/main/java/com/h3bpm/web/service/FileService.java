package com.h3bpm.web.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.h3bpm.web.entity.File;
import com.h3bpm.web.entity.FilePermission;
import com.h3bpm.web.mapper.FileMapper;
import com.h3bpm.web.mapper.FilePermissionMapper;
import com.h3bpm.web.utils.Constants;
import com.h3bpm.web.utils.UserSessionUtils;
import com.h3bpm.web.vo.FilePermissionVo;
import com.h3bpm.web.vo.FileVo;
import com.h3bpm.web.vo.OrgInfoVo;
import com.h3bpm.web.vo.UserSessionInfo;

@Service
public class FileService extends ApiDataService {
	@Autowired
	private FileMapper fileMapper;

	@Autowired
	private FilePermissionMapper filePermissionMapper;

	public List<File> findFileByParentIdAndKeyword(String parentId, String keyword) {
		List<File> fileList = null;
		String searchPath = null;
		try {
			//如果关键字不为空，则查询该目录开头的所有文件及文件夹
			if(keyword != null && !keyword.isEmpty()){
				//根目录则查询下面所有文件
				if(parentId == null || parentId.isEmpty()){
					searchPath = "";
				}else{
					searchPath = fileMapper.getFileById(parentId).getDir();
				}
			}
			
			fileList = fileMapper.findFileByParentIdAndKeyword(parentId, keyword, searchPath);

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
}
