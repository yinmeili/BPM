package com.h3bpm.web.controller;

import OThinker.Common.Organization.Models.User;
import OThinker.H3.Controller.ControllerBase;
import com.h3bpm.web.enumeration.FileType;
import com.h3bpm.web.service.FilePermissionService;
import com.h3bpm.web.service.FileService;
import com.h3bpm.web.utils.Constants;
import com.h3bpm.web.utils.FtUtils;
import com.h3bpm.web.utils.SFTPUtil;
import com.h3bpm.web.utils.UserSessionUtils;
import com.h3bpm.web.vo.*;
import com.jcraft.jsch.SftpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by tonghao on 2020/3/1.
 */
@Controller
@RequestMapping(value = "/Portal/fileManage")
public class FileManagerController extends ControllerBase {

	private static final Logger logger = LoggerFactory.getLogger(FileManagerController.class);

	@Value("${application.upload.path}")
	private String uploadPath;

	@Value("${application.upload.fileManage.ftp.ip}")
	private String ftpIp;

	@Value("${application.upload.fileManage.ftp.username}")
	private String ftpUserName;

	@Value("${application.upload.fileManage.ftp.password}")
	private String ftpPassword;

	@Value("${application.upload.fileManage.ftp.dir}")
	private String ftpDir;

	@Autowired
	private FileService fileService;

	@Autowired
	private FilePermissionService filePermissionService;

	// @RequestMapping(value = "/listFile", produces = "application/json;charset=utf8")
	// @ResponseBody
	// public FileDescList listFile(@RequestBody ReqParamList paramList) {
	// ReqParam param = paramList.getParams();
	// String pathStr = param.getPath();
	// pathStr = pathStr.replace("\\", File.separator);
	// Path currentPath = Paths.get(uploadPath + pathStr);
	//
	// FileDesc desc = null;
	// List<FileDesc> descList = new ArrayList<>();
	// FileDescList result = new FileDescList(descList);
	// try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentPath)) {
	// for (Path entry : stream) {
	//
	// if (Files.isDirectory(entry)) {
	// // 文件夹取不到文件大小
	// desc = new FileDesc(entry.getFileName().toString(), Files.size(entry), new Date(Files.getLastModifiedTime(entry).toMillis()), FileType.DIR.toString());
	// } else {
	// desc = new FileDesc(entry.getFileName().toString(), Files.size(entry), new Date(Files.getLastModifiedTime(entry).toMillis()), FileType.File.toString());
	// }
	// descList.add(desc);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return result;
	// }

	// author:lhl
	@RequestMapping(value = "/recycleStation", produces = "application/json;charset=utf8")
	@ResponseBody
	public FileDescList recycle(@RequestBody RecycleParam recycleParam) {
		List<FileDesc> descList = new ArrayList<>();

		try {
			UserSessionInfo userSessionInfo = UserSessionUtils.get(Constants.SESSION_USER, UserSessionInfo.class);
			String req_create_id = userSessionInfo.getUser().getObjectID();

			List<com.h3bpm.web.entity.File> fileList = fileService.findFileByCreateUserId(req_create_id);
			for (com.h3bpm.web.entity.File file : fileList) {

				FileDesc fileDesc = new FileDesc(file);
				descList.add(fileDesc);

			}

			FileDescList result = new FileDescList(descList);

			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@RequestMapping(value = "/listFile", produces = "application/json;charset=utf8")
	@ResponseBody
	public FileDescList listFile(@RequestBody ReqParamList paramList) {

		List<FileDesc> descList = new ArrayList<>();

		try {
			ReqParam param = paramList.getParams();

			String id = param.getFileId();

			String pathStr = param.getPath();
			pathStr = pathStr.replace("\\", File.separator);
			Path currentPath = Paths.get(uploadPath + pathStr);

			String fileId = param.getFileId();

			FileDesc desc = null;

			com.h3bpm.web.entity.File fileDb = fileService.getFileById(id);
			List<com.h3bpm.web.entity.File> fileList = fileService.findFileByParentIdAndKeyword(fileId, param.getKeyword());
			Map<String, Object> userMap = this._getCurrentUser();
			OThinker.Common.Organization.Models.User user = (User) userMap.get("User");

			if (fileList != null) {
				for (com.h3bpm.web.entity.File file : fileList) {

					/*
					 * 判断当前用户是否有文件的访问权限
					 */
					// if(fileService.validateFilePermission(file.getId(), user.getObjectId())){
					FileDesc fileDesc = new FileDesc(file);
					fileDesc.setFilePermission(filePermissionService.getFilePermissionByFileId(file.getId()));

					descList.add(fileDesc);
					// }

				}

			}
			FileDescList result = new FileDescList(descList);

			if (fileDb == null || fileDb.getParentId() == null) {
				result.setParentId("");
			} else {
				result.setParentId(fileDb.getParentId());
			}

			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		// return result;
	}

	@RequestMapping(value = "/getFileIdByPath", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo getFileIdByPath(@RequestParam(value="path")String path) {	
	Map<String, String> resultMap = new HashMap<String, String>();
		
		try {
			com.h3bpm.web.entity.File file = fileService.getFileByPath(path);
			
			if (file != null) {
				resultMap.put("id", file.getId());
			}
			return new ResponseVo(resultMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/listMyFolder", produces = "application/json;charset=utf8")
	@ResponseBody
	public FileDescList listMyFolder(@RequestBody ReqParamList paramList) {
		ReqParam param = paramList.getParams();
		String pathStr = param.getPath();
		pathStr = pathStr.replace("\\", File.separator);
		Path currentPath = Paths.get(uploadPath + pathStr);

		FileDesc desc = null;
		List<FileDesc> descList = new ArrayList<>();
		FileDescList result = new FileDescList(descList);

		descList.add(new FileDesc("aaa.txt", 11111111L, new Date(), "file"));
		descList.add(new FileDesc("bbb.txt", 11111111L, new Date(), "file"));

		return result;
	}

	@RequestMapping(value = "/updateFile", produces = "application/json;charset=utf8")
	@ResponseBody
	public FileDescSingle updateFile(@RequestBody ReqUpdateFile reqUpdateFile) {
		String pathStr = reqUpdateFile.getOldPath();
		String newPathStr = reqUpdateFile.getNewPath();
		pathStr = pathStr.replace("\\", File.separator);
		newPathStr = newPathStr.replace("\\", File.separator);

		// Path sourcePath = Paths.get(uploadPath + pathStr);
		// Path desPath = Paths.get(uploadPath + newPathStr);

		com.h3bpm.web.entity.File fileEntity = fileService.getFileById(reqUpdateFile.getFileId());
		FileVo fileVo = new FileVo(fileEntity);

		fileVo.setParentId(reqUpdateFile.getParentId());
		fileVo.setDir(newPathStr != null ? newPathStr : pathStr);
		fileVo.setFilePermission(reqUpdateFile.getFilePermission());

		fileService.updateFile(fileVo);
		FileDesc desc = null;
		FileDescSingle fileDescSingle = new FileDescSingle();

		String errorMsg = "";
		fileDescSingle.setResult(desc);
		return fileDescSingle;
	}

	// @RequestMapping(value = "/removeFile", produces = "application/json;charset=utf8")
	// @ResponseBody
	// public FileDescSingle removeFile(@RequestBody ReqParamList paramList) {
	// ReqParam param = paramList.getParams();
	// String pathStr = param.getPath();
	// pathStr = pathStr.replace("\\", File.separator);
	// Path sourcePath = Paths.get(uploadPath + pathStr);
	// FileDesc desc = null;
	// FileDescSingle fileDescSingle = new FileDescSingle();
	// String errorMsg = "";
	// try {
	// if (Files.isDirectory(sourcePath)) {
	// FileUtils.deleteDirectory(sourcePath.toFile());
	// errorMsg = "删除文件夹出错";
	// } else {
	// Files.delete(sourcePath);
	// errorMsg = "删除文件出错";
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// desc = new FileDesc(false, errorMsg);
	// fileDescSingle.setResult(desc);
	// return fileDescSingle;
	// }
	//
	// desc = new FileDesc(true, "");
	// fileDescSingle.setResult(desc);
	// return fileDescSingle;
	// }

	@RequestMapping(value = "/removeFile", produces = "application/json;charset=utf8")
	@ResponseBody
	public FileDescSingle removeFile(@RequestBody ReqParamList paramList) {
		ReqParam param = paramList.getParams();
		String pathStr = param.getPath();
		pathStr = pathStr.replace("\\", File.separator);
		Path sourcePath = Paths.get(uploadPath + pathStr);
		FileDesc desc = null;
		FileDescSingle fileDescSingle = new FileDescSingle();
		String errorMsg = "";

		String fileId = param.getFileId();
		fileService.deleteFile(fileId);

		desc = new FileDesc(true, "");
		fileDescSingle.setResult(desc);
		return fileDescSingle;
	}

	@RequestMapping(value = "/getContent", produces = "application/json;charset=utf8")
	@ResponseBody
	public FileDesc getFileContent(@RequestBody ReqParamList paramList) {
		ReqParam param = paramList.getParams();
		String pathStr = param.getPath();
		pathStr = pathStr.replace("\\", File.separator);
		Path sourcePath = Paths.get(uploadPath + pathStr);
		FileDesc desc = null;
		String errorMsg = "";
		if (Files.isDirectory(sourcePath)) {
			desc = new FileDesc("打开的是文件夹");
			return desc;
		}

		try {
			if (Files.size(sourcePath) > 1024 * 1024 * 10) {
				desc = new FileDesc("文件大于10M");
				return desc;
			}
		} catch (IOException e) {
			e.printStackTrace();
			desc = new FileDesc("打开文件出错");
			return desc;
		}

		logger.info(pathStr);
		logger.info(FtUtils.getFileEncode(sourcePath.toFile()).toString());
		StringBuilder builder = new StringBuilder("");
		try (BufferedReader reader = Files.newBufferedReader(sourcePath, FtUtils.getFileEncode(sourcePath.toFile()))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
				logger.info(line);
			}
			desc = new FileDesc(builder.toString());
		} catch (IOException e) {
			desc = new FileDesc("打开文件出错");
			e.printStackTrace();
		}
		return desc;
	}

	// @RequestMapping(value = "/createFolder", produces = "application/json;charset=utf8")
	// @ResponseBody
	// public FileDescSingle createFolder(@RequestBody ReqParamList paramList) {
	// ReqParam param = paramList.getParams();
	// String pathStr = param.getPath();
	// pathStr = pathStr.replace("\\", File.separator);
	// Path sourcePath = Paths.get(uploadPath + File.separator + pathStr + File.separator + param.getName());
	// logger.info(pathStr);
	// FileDesc desc = null;
	// FileDescSingle fileDescSingle = new FileDescSingle();
	// try {
	// Files.createDirectories(sourcePath);
	// } catch (IOException e) {
	// e.printStackTrace();
	//
	// desc = new FileDesc(false, "创建文件夹出错");
	// fileDescSingle.setResult(desc);
	// return fileDescSingle;
	// }
	//
	// desc = new FileDesc(true, null);
	// fileDescSingle.setResult(desc);
	// return fileDescSingle;
	// }

	// author:lhl
	@RequestMapping(value = "/createFolder", produces = "application/json;charset=utf8")
	@ResponseBody
	public ReqCreateFolder createFolder(@RequestBody ReqCreateFolder reqParam) throws Exception {
		UserSessionInfo userSessionInfo = UserSessionUtils.get(Constants.SESSION_USER, UserSessionInfo.class);

		FileVo fileVo = new FileVo();

		// 获取地址
		String pathStr = reqParam.getPath();
		pathStr = pathStr.replace("\\", "/");
//		Path sourcePath = Paths.get(uploadPath + File.separator + pathStr + File.separator + reqParam.getName());
		logger.info(pathStr);

		fileVo.setName(reqParam.getName()); // 获取名字存入fileVo
		fileVo.setFilePermission(reqParam.getFilePermission()); // 获取权限存入fileVo
		fileVo.setParentId(reqParam.getParentId());// 获取Id存入
		fileVo.setDir(pathStr + "/" + fileVo.getName() + "/");// 地址存入

		fileVo.setType(FileType.DIR.getValue());// 存入类型
		fileVo.setCreateUserId(userSessionInfo.getUser().getObjectId());// 存入用户Id
		fileVo.setCreateTime(new Date());

		fileService.createFile(fileVo);

		return reqParam;
	}

	@RequestMapping(value = "/saveFile", produces = "application/json;charset=utf8")
	@ResponseBody
	public FileDescSingle saveFile(@RequestBody ReqParamList paramList) {
		ReqParam param = paramList.getParams();
		String pathStr = param.getPath();
		pathStr = pathStr.replace("\\", "/");
		Path sourcePath = Paths.get(uploadPath + pathStr);
		String content = param.getContent();
		FileDesc desc = null;
		FileDescSingle single = new FileDescSingle();
		String errorMsg = "";
		if (Files.isDirectory(sourcePath)) {
			desc = new FileDesc(false, "写入的是文件夹");
			single.setResult(desc);
			return single;
		}

		Charset charset = FtUtils.getFileEncode(sourcePath.toFile());
		try (BufferedWriter writer = Files.newBufferedWriter(sourcePath, charset)) {
			writer.write(content, 0, content.length());
			desc = new FileDesc(true, "");
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
			desc = new FileDesc(false, "写入文件出错");
		}
		/*
		 * byte[] data = content.getBytes();
		 * 
		 * try (OutputStream out = new BufferedOutputStream( Files.newOutputStream(sourcePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) { out.write(data , 0, data.length); desc = new FileDesc(true, ""); } catch (IOException x) { System.err.println(x); desc = new FileDesc(false, "写入文件出错"); }
		 */
		single.setResult(desc);
		return single;
	}

	@RequestMapping(value = "/previewFile")
	@ResponseBody
	public void previewFile(ReqParam param, HttpServletRequest request, HttpServletResponse response) {

		String pathStr = param.getPath();
		String fileId = param.getFileId();
		/*
		 * try { pathStr = URLDecoder.decode(pathStr, "UTF-8"); } catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		 */
		pathStr = pathStr.replace("\\", File.separator);
		Path sourcePath = Paths.get(uploadPath + pathStr);

		logger.info(param.getPreview() + "");
		if (param.getPreview().equals("true")) {
			response.setContentType("image/gif");
			try (OutputStream out = response.getOutputStream(); FileInputStream fis = new FileInputStream(sourcePath.toFile());) {
				byte[] b = new byte[fis.available()];
				fis.read(b);
				out.write(b);
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {

			com.h3bpm.web.entity.File file = fileService.getFileById(fileId);
			OutputStream os = null;

			try (OutputStream out = response.getOutputStream();) {
				response.setHeader("Content-Type", "application/octet-stream");
				response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
				response.setContentLength((int) file.getFileSize());

				SFTPUtil sftp = new SFTPUtil(ftpUserName, ftpPassword, ftpIp, 22);
				sftp.login();

				String[] strArray = file.getName().split("\\.");
				int suffixIndex = strArray.length - 1;
				String fileSuffix = strArray[suffixIndex];

				byte[] buff = sftp.download(ftpDir, fileId);
				// System.out.println(Arrays.toString(buff));

				sftp.logout();

				os = new BufferedOutputStream(response.getOutputStream());
				os.write(buff);// 输出文件
				os.flush();

				// 循环将输入流中的内容读取到缓冲区当中 while ((len = in.read(buffer)) > 0) { // 输出缓冲区的内容到浏览器，实现文件下载 out.write(buffer, 0, len); //out.flush(); } response.flushBuffer(); } catch (UnsupportedEncodingException e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }

				// try {
				// MultipartFileSender.fromPath(sourcePath).with(request).with(response).serveResource();
			} catch (Exception e) {
				e.printStackTrace();
				if (os != null) {
					try {
						os.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	// /**
	// * Upload single file using Spring Controller
	// */
	// @RequestMapping(value = "/uploadFile", method = RequestMethod.POST, produces = "application/json;charset=utf8")
	// @ResponseBody
	// public FileDesc uploadFileHandler(@RequestParam("file") MultipartFile file, @RequestParam("path") String path, HttpServletResponse response) throws IOException {
	//
	// logger.info(path);
	// FileDesc desc = null;
	// if (!file.isEmpty()) {
	//
	// path = path.replace("\\", File.separator);
	// Path folderPath = Paths.get(uploadPath + File.separator + path);
	//
	// if (Files.notExists(folderPath))
	// Files.createDirectories(folderPath);
	// Path filePath = Paths.get(uploadPath + File.separator + path + File.separator + file.getOriginalFilename());
	// if (!Files.notExists(filePath)) {
	// desc = new FileDesc(false, "文件已存在");
	// return desc;
	// }
	// try (InputStream in = file.getInputStream(); OutputStream out = Files.newOutputStream(filePath)) {
	// byte[] b = new byte[1024];
	// int len = 0;
	// while ((len = in.read(b)) > 0) {
	// out.write(b, 0, len);
	// }
	// logger.info("Server File Location=" + filePath.toAbsolutePath().toString());
	// desc = new FileDesc(true, "");
	// return desc;
	// } catch (IOException e) {
	// e.printStackTrace();
	// desc = new FileDesc(false, "系统内部错误，请稍后重试");
	// return desc;
	// }
	// } else {
	// desc = new FileDesc(false, "文件为空");
	// return desc;
	// }
	// }

	/**
	 * Upload single file using Spring Controller
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST, produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo uploadFileHandler(@RequestParam("file") MultipartFile file, @RequestParam("filePermission") String filePermission, @RequestParam("path") String path, @RequestParam("parentId") String parentId, HttpServletResponse response) throws IOException {

		if (!file.isEmpty()) {
			UserSessionInfo userSessionInfo = UserSessionUtils.get(Constants.SESSION_USER, UserSessionInfo.class);
			String fileId = UUID.randomUUID().toString();

			path = path.replace("\\", "/");
			String fileFullName = file.getOriginalFilename();

			// 获取文件后缀
			String[] strArray = fileFullName.split("\\.");
			int suffixIndex = strArray.length - 1;
			String fileSuffix = strArray[suffixIndex];

			// :TODO 判断文件是否已存在
			// Path folderPath = Paths.get(uploadPath + File.separator + path);
			//
			// if (Files.notExists(folderPath))
			// Files.createDirectories(folderPath);
			// Path filePath = Paths.get(uploadPath + File.separator + path + File.separator + file.getOriginalFilename());
			// if (!Files.notExists(filePath)) {
			// desc = new FileDesc(false, "文件已存在");
			// return desc;
			// }

			try (InputStream in = file.getInputStream()) {

				SFTPUtil sftp = new SFTPUtil(ftpUserName, ftpPassword, ftpIp, 22);
				sftp.login();

				// byte[] buff = sftp.download("/opt", "start.sh");
				// System.out.println(Arrays.toString(buff));

				sftp.upload(ftpDir, fileId, in);
				sftp.logout();

				FileVo fileVo = new FileVo();
				fileVo.setId(fileId);
				fileVo.setParentId(parentId);
				fileVo.setType(FileType.FILE.getValue());
				fileVo.setName(fileFullName);
				fileVo.setDir(path + "/" + fileFullName);
				fileVo.setFileSize(file.getSize());
				fileVo.setCreateUserId(userSessionInfo.getUser().getObjectId());
				fileVo.setCreateTime(new Date());

				// 处理filePermission字符串
				String[] idList = filePermission.split(",");
				FilePermissionVo filePermissionVo = new FilePermissionVo(idList);
				// 设置FilePermission
				fileVo.setFilePermission(filePermissionVo);

				fileService.createFile(fileVo);

				// 设置返回数据格式
				Map<String, String> resultMap = new HashMap<String, String>();
				resultMap.put("id", fileVo.getId());

				return new ResponseVo(resultMap);
			} catch (IOException | SftpException e) {
				e.printStackTrace();
			}
		} else {
			ResponseVo respUploadFileFalse = new ResponseVo();
			respUploadFileFalse.setErrorCode(404);
			respUploadFileFalse.setMsg("调用失败");
			return respUploadFileFalse;
		}

		return null;
	}

	@RequestMapping(value = "/uploadMultiFile", method = RequestMethod.POST, produces = "application/json;charset=utf8")
	@ResponseBody
	public List<FileDesc> uploadMultiFileHandler(@RequestParam("file") MultipartFile[] files, @RequestParam("path") String path, HttpServletResponse response) throws IOException {

		logger.info(path);
		FileDesc desc = null;
		List<FileDesc> fileDescList = new ArrayList<>();
		for (MultipartFile file : files) {
			if (!file.isEmpty()) {

				path = path.replace("\\", File.separator);
				Path folderPath = Paths.get(uploadPath + File.separator + path);

				if (Files.notExists(folderPath))
					Files.createDirectories(folderPath);
				Path filePath = Paths.get(uploadPath + File.separator + path + File.separator + file.getOriginalFilename());
				if (!Files.notExists(filePath)) {
					desc = new FileDesc(false, "文件已存在");
					fileDescList.add(desc);
					// return desc;
				}
				try (InputStream in = file.getInputStream(); OutputStream out = Files.newOutputStream(filePath)) {
					byte[] b = new byte[1024];
					int len = 0;
					while ((len = in.read(b)) > 0) {
						out.write(b, 0, len);
					}
					logger.info("Server File Location=" + filePath.toAbsolutePath().toString());
					desc = new FileDesc(true, "");
					fileDescList.add(desc);
					// return desc;
				} catch (IOException e) {
					e.printStackTrace();
					desc = new FileDesc(false, "系统内部错误，请稍后重试");
					fileDescList.add(desc);
					// return desc;
				}
			} else {
				desc = new FileDesc(false, "文件为空");
				fileDescList.add(desc);
				// return desc;
			}
		}

		return fileDescList;
	}

	@RequestMapping(value = "/getServerUrl")
	@ResponseBody
	public List<String> getServerUrl(HttpServletRequest request) {
		List<String> urls = new ArrayList<>();
		String appUrl = request.getContextPath();
		logger.info(request.getLocalAddr());
		logger.info(request.getLocalPort() + "");
		// String port = request.g

		List<String> ips = FtUtils.getIps();
		for (String ip : ips) {
			urls.add("http://" + ip + ":" + request.getLocalPort() + appUrl);
		}
		/*
		 * for(String url : urls) { logger.info(url); }
		 */
		return urls;

		// logger.info(request.getRequestURI());
	}

	@Override
	public String getFunctionCode() {
		// TODO Auto-generated method stub
		return null;
	}
}
