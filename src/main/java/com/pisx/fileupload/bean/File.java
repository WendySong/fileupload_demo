package com.pisx.fileupload.bean;

import com.pisx.fileupload.util.DateUtil;

import java.util.Date;

/**
 * Created by pisx on 2014/12/23.
 */
public class File {
    private Integer id;
    private String filename;
    private String path;
    private Date createTime;
    private Integer status;
    private String md5;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStr(){
        return DateUtil.date2String(this.createTime);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
