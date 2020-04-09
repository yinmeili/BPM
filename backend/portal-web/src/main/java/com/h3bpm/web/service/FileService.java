package com.h3bpm.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.h3bpm.web.entity.File;
import com.h3bpm.web.mapper.FileMapper;

@Service
public class FileService {
	@Autowired
	private FileMapper fileMapper;

	public List<File> findFileByParentId(String parentId) {
		List<File> fileList = null;
		try {
			fileList = fileMapper.findFileByParentId(parentId);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileList;
	}
}
