package com.pisx.fileupload.dao;

import com.pisx.fileupload.bean.File;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by pisx on 2014/12/25.
 */
public interface FileDao extends BaseDao<File> {
    @Override
    int create(File entity);

    @Override
    int update(File entity);

    @Override
    int update(Map<String, Object> params, Object[] ids);

    @Override
    int delete(Serializable pk);

    @Override
    int delete(int[] pks);

    @Override
    void deleteAll();

    @Override
    File get(int pk);

    @Override
    List<File> getByIds(int[] pks);

    @Override
    int countByParameters(ParameterList params);

    @Override
    Map<String, Object> mapByParameters(int pageNo, int pageSize, ParameterList params, String orderBy);

    @Override
    RowMapper<File> getRowMapper();
}
