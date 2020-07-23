package com.h3bpm.web.service;

import java.util.ArrayList;
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
import com.h3bpm.web.mapper.MyFileMapper;
import com.h3bpm.web.vo.FileVo;

@Service
public class FileService extends ApiDataService {
	@Autowired
	private FileMapper fileMapper;

	@Autowired
	private MyFileMapper myFileMapper;

	@Autowired
	private FilePermissionMapper filePermissionMapper;

	public List<File> findFileByParentIdAndKeyword(String parentId, String keyword) {
		List<File> fileList = null;
		String searchPath = null;
		try {
			// 如果关键字不为空，则查询该目录开头的所有文件及文件夹
			if (keyword != null && !keyword.isEmpty()) {
				// 根目录则查询下面所有文件
				if (parentId == null || parentId.isEmpty()) {
					searchPath = "";
				} else {
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

	/**
	 * 根据我的文件ID，构建出共享文件List，主要用于共享到共享文件 下钻所有的文件夹和文件
	 * 
	 * @return
	 */
	public List<File> buildShareFileList(String myFileId, String shareFileParentId, String userId) {
		List<File> shareFileList = new ArrayList<>();

		com.h3bpm.web.entity.File myFile = myFileMapper.getMyFileById(myFileId);
		com.h3bpm.web.entity.File shareParentFile = this.getFileById(shareFileParentId);

		String rootMyFileDir = myFile.getDir().replace(myFile.getName() + "/", "");
		String rootShareFileDir = shareParentFile.getDir();

		this.findMyFileChild(shareFileList, rootMyFileDir, rootShareFileDir, myFileId, shareFileParentId, userId);

		return shareFileList;
	}

	private void findMyFileChild(List<File> shareFileList, String rootShareFileDir, String rootMyFileDir, String myFileId, String shareFileParentId, String userId) {
		List<File> myFileList = myFileMapper.findMyFileByParentIdAndKeyword(myFileId, null, null, userId);
		String shareFileId = UUID.randomUUID().toString();

		File shareFile = myFileMapper.getMyFileById(myFileId);

		shareFile.setId(shareFileId);
		shareFile.setParentId(shareFileParentId);
		shareFile.setCreateUserId(userId);

		String myfileDir = rootMyFileDir + shareFile.getDir().replace(rootShareFileDir, "");

		shareFile.setDir(myfileDir);
		shareFile.setCreateTime(new Date());

		if (myFileList != null && !myFileList.isEmpty()) {
			for (File myFile : myFileList) {
				findMyFileChild(shareFileList, rootShareFileDir, rootMyFileDir, myFile.getId(), shareFileId, userId);
			}
		}
		shareFileList.add(shareFile);
	}

}
