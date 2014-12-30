package com.pisx.fileupload.controller;

import com.pisx.fileupload.bean.FileUpload;
import com.pisx.fileupload.service.FileUploadService;
import com.pisx.fileupload.util.ReturnMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by pisx on 2014/12/29.
 */
@Controller
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Resource
    private FileUploadService fileUploadService;
    //    @Value("#{settings['file.rootPath']}")
    @Value("${file.rootPath}")
    private String fileRoot;

    public String getFileRoot() {
        return fileRoot;
    }

    public void setFileRoot(String fileRoot) {
        this.fileRoot = fileRoot;
    }

    @RequestMapping(value = "/upload")
    @ResponseBody
    public String upload(@RequestParam("file") CommonsMultipartFile file, @RequestParam("md5") String md5) {
        long startTime = System.currentTimeMillis();
        String msg = "";
        String path = fileRoot + new Date().getTime() + "/" + file.getOriginalFilename();
        logger.info("file:{},md5:{}", file, md5);
        File newFile = new File(path);
        try {
            //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
            file.transferTo(newFile);
            long endTime = System.currentTimeMillis();
            logger.info("文件上传success! -costTime:{} ms,fileSize:{} byte", (endTime - startTime), file.getSize());
            FileUpload fileUpload = new FileUpload();
            fileUpload.setMd5(md5);
            fileUpload.setPath(path);
            fileUpload.setCreateTime(new Date());
            fileUpload.setStatus(FileUpload.UPLOAD_SUCCEED);
            fileUpload.setFilename(file.getOriginalFilename());
            int id = fileUploadService.save(fileUpload);
            msg = ReturnMessageUtil.createOKMsg("fileupload success!");
        } catch (IOException e) {
            logger.error("文件上传失败-file:{},e:{}", file.getOriginalFilename(), e);
            msg = ReturnMessageUtil.createErrorMsg("文件上传失败！");
        } catch (DataAccessException e) {
            logger.error("save fileupload failed! - fileName:{}", file.getOriginalFilename());
            msg = ReturnMessageUtil.createErrorMsg("保存文件信息失败！");
        }
        return msg;
    }

    @ResponseBody
    @RequestMapping(value = "/matchMd5")
    public String checkMD5(@RequestParam("md5") String md5) {
        FileUpload fileUpload = fileUploadService.matchingMD5(md5);
        boolean result = fileUpload == null;
        return ReturnMessageUtil.createOKMsg(result);
    }
}
