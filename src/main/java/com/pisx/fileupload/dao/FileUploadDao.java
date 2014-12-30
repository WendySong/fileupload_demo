package com.pisx.fileupload.dao;

import com.pisx.fileupload.bean.FileUpload;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by pisx on 2014/12/25.
 */
public interface FileUploadDao extends BaseDao<FileUpload> {
    @Override
    int create(FileUpload entity) throws DataAccessException;

    @Override
    int update(FileUpload entity);

    @Override
    int update(Map<String, Object> params, Object[] ids);

    @Override
    int delete(Serializable pk);

    @Override
    int delete(int[] pks);

    @Override
    void deleteAll();

    @Override
    FileUpload get(int pk);

    @Override
    List<FileUpload> getByIds(int[] pks);

    FileUpload getByMD5(String md5) throws IncorrectResultSizeDataAccessException;

    @Override
    int countByParameters(ParameterList params);

    @Override
    Map<String, Object> mapByParameters(int pageNo, int pageSize, ParameterList params, String orderBy);

    @Override
    RowMapper<FileUpload> getRowMapper();
}
