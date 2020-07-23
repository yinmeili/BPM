package com.h3bpm.web.controller;

import OThinker.Common.Organization.Models.User;
import OThinker.H3.Controller.ControllerBase;
import com.h3bpm.web.enumeration.FileType;
import com.h3bpm.web.service.FilePermissionService;
import com.h3bpm.web.service.FileService;
import com.h3bpm.web.service.MyFileService;
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
@RequestMapping(value = "/Portal/knowledgeManage")
public class KnowledgeManagerController extends ControllerBase {

	private static final Logger logger = LoggerFactory.getLogger(KnowledgeManagerController.class);

	@Override
	public String getFunctionCode() {
		// TODO Auto-generated method stub
		return null;
	}
}
