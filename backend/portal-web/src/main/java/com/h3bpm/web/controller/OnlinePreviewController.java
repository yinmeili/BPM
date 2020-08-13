package com.h3bpm.web.controller;

import OThinker.H3.Controller.ControllerBase;
import com.h3bpm.web.service.FileService;
import com.h3bpm.web.utils.SFTPUtil;
import org.jodconverter.DocumentConverter;
import org.jodconverter.document.DefaultDocumentFormatRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import java.io.*;

@Controller
@RequestMapping(value = "/Portal/onlinePreview")
public class OnlinePreviewController extends ControllerBase {

    private static final Logger logger = LoggerFactory.getLogger(OnlinePreviewController.class);

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
    private DocumentConverter converter;

    /**
     * @Descriptin online preview file
     */
    @RequestMapping(value = "/previewFile", method = RequestMethod.GET, produces = "application/json;charset=utf8")
    @ResponseBody
    public void previewFile(@RequestParam(required = false, name = "fileId") String fileId) throws IOException {

        if (fileId == null || fileId.isEmpty()) {
            return;
        }

        com.h3bpm.web.entity.File file = fileService.getFileById(fileId);
        String fileName = file.getName();

        int ty = fileName.lastIndexOf(".");
        String fileType = null;
        if (ty != -1) {
            fileType = fileName.substring(ty + 1);
        }

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        InputStreamReader isr = null;
        OutputStreamWriter osw = null;

        try {
            SFTPUtil sftp = new SFTPUtil(ftpUserName, ftpPassword, ftpIp, 22);
            sftp.login();
            byte[] buff = sftp.download(ftpDir, file.getDownloadFileId());
            bis = new BufferedInputStream(new ByteArrayInputStream(buff));
            sftp.logout();
            bos = new BufferedOutputStream(response.getOutputStream());

            if (!isOfficeFile(fileType)) {

                ServletContext servletContext = request.getServletContext();
                String mimeType = servletContext.getMimeType(fileName);
                response.setHeader("content-type", mimeType + ";charset=utf-8");

                if (fileType.toLowerCase().equals("txt") || fileType.toLowerCase().equals("md")) {
                    response.setContentType(mimeType + ";charset=GBK");
                    isr = new InputStreamReader(bis);
                    osw = new OutputStreamWriter(bos, "GBK");
                    char[] chars = new char[1024 * 8];
                    int len;
                    if ((len = isr.read(chars)) != -1) {
                        osw.write(chars, 0, len);
                    }
                    return;
                }

                byte[] buf = new byte[1024 * 8];
                int len = 0;
                while ((len = bis.read(buf)) != -1) {
                    bos.write(buf, 0, len);
                }

            } else {
                response.setHeader("content-type", "application/pdf;charset=utf-8");
                generatePdf(bis, bos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getFunctionCode() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param is
     * @param os
     * @return
     * @Description generate officeFile to pdf
     */
    private void generatePdf(InputStream is, OutputStream os) {

        try {
            converter.convert(is).as(DefaultDocumentFormatRegistry.PDF).to(os).as(DefaultDocumentFormatRegistry.PDF).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param fileType
     * @return
     * @Description determine the officeFile
     */
    private boolean isOfficeFile(String fileType) {

        switch (fileType.toLowerCase()) {
            case "doc":
            case "docx":
            case "xls":
            case "xlsx":
            case "ppt":
            case "pptx":
                return true;
        }
        return false;
    }
}
