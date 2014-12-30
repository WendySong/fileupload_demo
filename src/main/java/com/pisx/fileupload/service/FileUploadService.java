package com.pisx.fileupload.service;

import com.pisx.fileupload.bean.FileUpload;
import org.springframework.dao.DataAccessException;

/**
 * Created by pisx on 2014/12/30.
 */
public interface FileUploadService {
    public int save(FileUpload fileUpload) throws DataAccessException;

    public FileUpload matchingMD5(String md5);
}
