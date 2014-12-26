package com.pisx.fileupload.dao;

import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by pisx on 2014/12/23.
 */
public interface BaseDao<T> {
    /**
     * 创建指定的对象
     *
     * @param	obj	待创建的对象
     * @return	对象创建后的ID
     */
    public int create(T entity);

    public int update(T entity);

    public int update(Map<String,Object> params, Object[] ids);

    public int delete(Serializable pk);

    public int delete(int[] pks);

    public void deleteAll();

    public T get(int pk);

    public List<T> getByIds(int[] pks);

    public int countByParameters(ParameterList params);

    public Map<String,Object> mapByParameters(int pageNo, int pageSize, ParameterList params, String orderBy);

    RowMapper<T> getRowMapper();
}
