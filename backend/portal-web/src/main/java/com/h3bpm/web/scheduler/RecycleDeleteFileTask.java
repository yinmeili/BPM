package com.h3bpm.web.scheduler;

import com.h3bpm.web.entity.File;
import com.h3bpm.web.enumeration.DeletedFileType;
import com.h3bpm.web.enumeration.RecycleDeleteFileMode;
import com.h3bpm.web.mapper.FileMapper;
import com.h3bpm.web.mapper.MyFileMapper;
import com.h3bpm.web.service.FileService;
import com.h3bpm.web.utils.SFTPUtil;
import com.jcraft.jsch.SftpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RecycleDeleteFileTask {
    private static final Logger logger = LoggerFactory.getLogger(WorkflowTask.class);

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private MyFileMapper myFileMapper;

    @Autowired
    private FileService fileService;

    @Value("${application.recycleFile.save.max.time}")
    private int saveMaxTime;

    @Value("${application.upload.fileManage.ftp.dir}")
    private String ftpDir;

    @Value("${application.upload.fileManage.ftp.ip}")
    private String ftpIp;

    @Value("${application.upload.fileManage.ftp.username}")
    private String ftpUserName;

    @Value("${application.upload.fileManage.ftp.password}")
    private String ftpPassword;
    /**
     * 实现每半小时轮寻回收站，将删除日期大于10天的所有文件都删除
     */
    @Scheduled(cron = "* * 0 * * ?")
    private void processor(){
        logger.info("======== RecycleDeleteFileTask start ========");
        Map<String,Integer> res = findDeleteFile();
        for (Map.Entry<String, Integer> fileIntegerEntry : res.entrySet()) {

            if(fileIntegerEntry.getValue() == 0){
                File file = fileMapper.getFileById(fileIntegerEntry.getKey());
                file.setIsDelete(DeletedFileType.COMPLETE_DELETED.getValue());
                fileMapper.updateFile(file);
            }
            else if(fileIntegerEntry.getValue() == 2){
                File myFile = myFileMapper.getMyFileById(fileIntegerEntry.getKey());
                myFile.setIsDelete(DeletedFileType.COMPLETE_DELETED.getValue());
                myFileMapper.updateMyFile(myFile);
            }
            else if(fileIntegerEntry.getValue() == 1){
                SFTPUtil sftp = new SFTPUtil(ftpUserName, ftpPassword, ftpIp, 22);
                sftp.login();
                try{
                    File file = fileMapper.getFileById(fileIntegerEntry.getKey());
                    file.setIsDelete(DeletedFileType.COMPLETE_DELETED.getValue());
                    fileMapper.updateFile(file);
                    sftp.delete(ftpDir,file.getDownloadFileId());   //删除服务器上的文件
                }catch (SftpException e){
                    e.printStackTrace();
                }
            }
            else{
                SFTPUtil sftp = new SFTPUtil(ftpUserName, ftpPassword, ftpIp, 22);
                sftp.login();
                try{
                    File myFile = myFileMapper.getMyFileById(fileIntegerEntry.getKey());
                    myFile.setIsDelete(DeletedFileType.COMPLETE_DELETED.getValue());
                    myFileMapper.updateMyFile(myFile);
                    sftp.delete(ftpDir,myFile.getDownloadFileId());   //删除服务器上的文件
                }catch (SftpException e){
                    e.printStackTrace();
                }
            }
        }
        logger.info("======== RecycleDeleteFileTask end   ========");
    }

    /**
     * 处理回收站中的超时文件
     * @return
     * Srting代表file_id,
     * Integer = 0 file is_delete = 2,
     * Integer = 1 file is_delete = 2 && delete file in sftp
     * Integer = 2 my_file is_delete = 2
     * Integer = 3 my_file is_delete = 2 && delete my_file in sftp
     */

    private Map<String,Integer> findDeleteFile(){
        Map<String,Integer> res = new HashMap<>();

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.DATE,calendar.get(Calendar.DATE) - saveMaxTime);

        /*
        或取共享文件回收站中的可删除文件
         */
        List<File> deletedFile = fileMapper.findDeleteFileByDeleteTime(calendar.getTime());
        for (File file : deletedFile) {

            Date deleteTime = file.getDeleteTime();
            if(deleteTime == null || file.getDownloadFileId() == null) continue;

            res.put(file.getId(),RecycleDeleteFileMode.FILE_DELETE.getValue());

            //共享文件中 is_delete = 0 的文件数量, 过滤掉共享文件中有未删除的文件
            int count1 = fileMapper.getCountOfUnDeletedFileByDownloadFileId(file.getDownloadFileId());
            if(count1 != 0) continue;

            //共享文件中 is_delete = 1 并且 还未超期的文件数量，过滤掉共享文件回收站中有未超期的文件
            int count2 = fileMapper.getCountOfSaveFileByDownloadFileId(file.getDownloadFileId(),calendar.getTime());
            if(count2 != 0) continue;

            //我的文件中 is_delete = 0 的文件数量，过滤掉我的文件中有未删除的文件
            int count3 = myFileMapper.getCountOfUnDeletedMyFileByDownloadFileId(file.getDownloadFileId());
            if(count3 != 0) continue;

            //我的文件中 is_delete = 1 并且 还未超期的文件数量，过滤掉我的文件回收站中有未超期的文件
            int count4 = myFileMapper.getCountOfSaveMyFileByDownloadFileId(file.getDownloadFileId(),calendar.getTime());
            if(count4 != 0) continue;

            //将为过滤掉的共享文件加入到完全删除文件列表中
            res.put(file.getId(),RecycleDeleteFileMode.FILE_COMPLETE_DELET.getValue());
        }

        /*
        获取我的文件回收站中的可删除文件
         */
        List<File> deletedMyFile = myFileMapper.findDeleteMyFileByDeleteTime(calendar.getTime());
        for (File file : deletedMyFile) {

            Date deleteTime = file.getDeleteTime();
            if(deleteTime == null || file.getDownloadFileId() == null) continue;

            res.put(file.getId(),RecycleDeleteFileMode.MY_FILE_DELETE.getValue());

            //我的文件中 is_delete = 0 的文件数量，过滤掉我的文件中有未删除的文件
            int count1 = myFileMapper.getCountOfUnDeletedMyFileByDownloadFileId(file.getDownloadFileId());
            if(count1 != 0) continue;

            //我的文件中 is_delete = 1 并且 还未超期的文件数量，过滤掉我的文件回收站中有未超期的文件
            int count2 = myFileMapper.getCountOfSaveMyFileByDownloadFileId(file.getDownloadFileId(),calendar.getTime());
            if(count2 != 0) continue;

            //共享文件中 is_delete = 0 的文件数量，过滤掉共享文件中有未删除的文件
            int count3 = fileMapper.getCountOfUnDeletedFileByDownloadFileId(file.getDownloadFileId());
            if(count3 != 0) continue;

            //共享文件中 is_delete = 1 并且 还未超期的文件数量，过滤掉共享文件回收站中有未超期的文件
            int count4 = fileMapper.getCountOfSaveFileByDownloadFileId(file.getDownloadFileId(),calendar.getTime());
            if(count4 != 0) continue;

            //将为过滤掉的我的文件加入到完全删除文件列表中
            res.put(file.getId(),RecycleDeleteFileMode.MY_FILE_COMPLETE_DELET.getValue());
        }
        return res;
    }
}
