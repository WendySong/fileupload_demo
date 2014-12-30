package com.pisx.fileupload.test;

import com.pisx.fileupload.controller.FileUploadController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by pisx on 2014/12/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/resources/applicationContext.xml")
public class ValueConfigTest {

    private static final Logger logger = LoggerFactory.getLogger(ValueConfigTest.class);

    @Autowired
    private FileUploadController fileUploadController;

    @Test
    public void test() {
        logger.info("fileUploadController.fileRoot:{}", fileUploadController.getFileRoot());
        Assert.assertEquals("E:/fileupload/", fileUploadController.getFileRoot());
    }
}
