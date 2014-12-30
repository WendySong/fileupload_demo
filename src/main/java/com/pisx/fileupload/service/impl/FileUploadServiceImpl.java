package com.pisx.fileupload.service.impl;

import com.pisx.fileupload.bean.FileUpload;
import com.pisx.fileupload.dao.FileUploadDao;
import com.pisx.fileupload.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by pisx on 2014/12/30.
 */
@Service(value = "fileUploadService")
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    @Resource
    private FileUploadDao fileUploadDao;

    @Override
    public int save(FileUpload fileUpload) throws DataAccessException {
        return fileUploadDao.create(fileUpload);
    }

    @Override
    public FileUpload matchingMD5(String md5) {
        FileUpload fileUpload;
        try {
            fileUpload = fileUploadDao.getByMD5(md5);
        } catch (IncorrectResultSizeDataAccessException e) {
            fileUpload = null;
            logger.info("无匹配文件,md5:{}", md5);
        }
        return fileUpload;
    }

}
