package com.pisx.fileupload.test;

import com.pisx.fileupload.bean.FileUpload;
import com.pisx.fileupload.dao.FileUploadDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created by pisx on 2014/12/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/resources/applicationContext.xml")
public class FileUploadDaoTest {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadDaoTest.class);

    @Autowired
    private FileUploadDao fileUploadDao;

    @Test
    public void createTest() {
        FileUpload fileUpload = new FileUpload();
        fileUpload.setFilename("123");
        fileUpload.setMd5("123123123");
        fileUpload.setStatus(FileUpload.UPLOAD_SUCCEED);
        fileUpload.setCreateTime(new Date());
        fileUpload.setPath("E:/fileupload/1420008462251/Wildlife.wmv");
        fileUploadDao.create(fileUpload);
    }
}
