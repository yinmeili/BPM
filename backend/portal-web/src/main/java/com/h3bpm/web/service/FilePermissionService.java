package com.h3bpm.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.h3bpm.web.mapper.FilePermissionMapper;
import com.h3bpm.web.vo.FilePermissionVo;

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
}
