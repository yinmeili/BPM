package com.h3bpm.web.controller;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.h3bpm.web.vo.FileDesc;

/**
 * Created by tonghao on 2020/3/1.
 */
@Controller
@RequestMapping(value="/Portal/fileManage")
public class MyFileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(MyFileUploadController.class);
    
    @Value("${application.upload.path}")
    private String uploadPath;


    /**
     * Upload single file using Spring Controller
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST, produces = "application/json;charset=utf8")
    @ResponseBody
    public FileDesc uploadFileHandler(@RequestParam("file") MultipartFile file, @RequestParam("path") String path, HttpServletResponse response) throws IOException {

        logger.info(path);
        FileDesc desc = null;
        if (!file.isEmpty()) {

            path = path.replace("\\", File.separator);
            Path folderPath = Paths.get(uploadPath + File.separator + path);

            if(Files.notExists(folderPath))
                Files.createDirectories(folderPath);
            Path filePath = Paths.get(uploadPath + File.separator + path + File.separator + file.getOriginalFilename());
            if(!Files.notExists(filePath)) {
                desc = new FileDesc(false, "文件已存在");
                return desc;
            }
            try (InputStream in = file.getInputStream(); OutputStream out = Files.newOutputStream(filePath)) {
                byte[] b = new byte[1024];
                int len = 0;
                while ((len = in.read(b)) > 0) {
                    out.write(b, 0, len);
                }
                logger.info("Server File Location=" + filePath.toAbsolutePath().toString());
                desc = new FileDesc(true, "");
                return desc;
            } catch (IOException e) {
                e.printStackTrace();
                desc = new FileDesc(false, "系统内部错误，请稍后重试");
                return desc;
            }
        } else {
            desc = new FileDesc(false, "文件为空");
            return desc;
        }
    }

    @RequestMapping(value = "/uploadMultiFile", method = RequestMethod.POST, produces = "application/json;charset=utf8")
    @ResponseBody
    public List<FileDesc> uploadMultiFileHandler(@RequestParam("file") MultipartFile[] files, @RequestParam("path") String path, HttpServletResponse response) throws IOException {

        logger.info(path);
        FileDesc desc = null;
        List<FileDesc> fileDescList = new ArrayList<>();
        for(MultipartFile file : files) {
            if (!file.isEmpty()) {

                path = path.replace("\\", File.separator);
                Path folderPath = Paths.get(uploadPath + File.separator + path);

                if (Files.notExists(folderPath))
                    Files.createDirectories(folderPath);
                Path filePath = Paths.get(uploadPath + File.separator + path + File.separator + file.getOriginalFilename());
                if (!Files.notExists(filePath)) {
                    desc = new FileDesc(false, "文件已存在");
                    fileDescList.add(desc);
                    //return desc;
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
                    //return desc;
                } catch (IOException e) {
                    e.printStackTrace();
                    desc = new FileDesc(false, "系统内部错误，请稍后重试");
                    fileDescList.add(desc);
                    //return desc;
                }
            } else {
                desc = new FileDesc(false, "文件为空");
                fileDescList.add(desc);
                //return desc;
            }
        }

        return fileDescList;
    }
}
