package com.pisx.fileupload.dao.impl;

import com.pisx.fileupload.dao.BaseDao;
import com.pisx.fileupload.dao.ParameterList;
import com.pisx.fileupload.util.GenericsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pisx on 2014/12/23.
 */
public abstract class BaseDaoImpl<T> implements BaseDao<T> {

    private static final Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public BaseDaoImpl() {
        entityClass = GenericsUtils.getSuperClassGenricType(getClass(), 0);
    }

    @Override
    public int create(T entity) {
        return 0;
    }

    @Override
    public int update(T entity) {
        return 0;
    }

    @Override
    public int update(Map<String, Object> params, Object[] ids) {
        return 0;
    }

    @Override
    public int delete(Serializable pk) {
        String sql = " DELETE FROM " + getTableName()
                + " WHERE id=?";
        return jdbcTemplate.update(sql, pk);
    }

    @Override
    public int delete(final int[] pks) {
        if (pks == null || pks.length <= 0) {
            return 0;
        }
        String sql = " DELETE FROM " + getTableName()
                + " WHERE id=?";
        int[] rowNums = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, pks[i]);
            }

            @Override
            public int getBatchSize() {
                return pks.length;
            }
        });
        return rowNums.length;
    }

    @Override
    public void deleteAll() {
        String sql = " TRUNCATE TABLE " + getTableName();
        jdbcTemplate.execute(sql);
    }

    @Override
    public T get(int pk) {
        T obj = null;
        String sql = "select * from " + getTableName() + " where ID=?";
        obj = jdbcTemplate.queryForObject(sql, getRowMapper(), pk);
        return obj;
    }

    @Override
    public List<T> getByIds(int[] pks) {
        if (pks == null || pks.length <= 0) {
            return null;
        }
        List<T> resultList = new ArrayList<T>();
        /**
         * 循环多次查询
         */
//		for (int i = 0; i < pks.length; i++) {
//			list.add(this.get(pks[i]));
//		}

        /**
         * 一次 in查询
         */
        StringBuilder inClause = new StringBuilder();
        boolean firstValue = true;
        Integer[] integerPks = new Integer[pks.length];
        for (int i = 0; i < pks.length; i++) {
            if (firstValue) {
                firstValue = false;
            } else {
                inClause.append(',');
            }
            inClause.append('?');
            integerPks[i] = pks[i];
        }
        String sql = "select * from " + getTableName() + " where ID in (" + inClause.toString() + ")";
        resultList = jdbcTemplate.query(sql, integerPks, getRowMapper());

        /**
         * 下面代码是分层 分批查询  数据量大的情况下可用
         */
//		int totalNumberOfValuesLeftToBatch = pks.length;
//		while ( totalNumberOfValuesLeftToBatch > 0 ) {
//			int batchSize = SINGLE_BATCH;
//			if ( totalNumberOfValuesLeftToBatch >= LARGE_BATCH ) {
//			  batchSize = LARGE_BATCH;
//			} else if ( totalNumberOfValuesLeftToBatch >= MEDIUM_BATCH ) {
//			  batchSize = MEDIUM_BATCH;
//			} else if ( totalNumberOfValuesLeftToBatch >= SMALL_BATCH ) {
//			  batchSize = SMALL_BATCH;
//			}
//			totalNumberOfValuesLeftToBatch -= batchSize;
//
//			StringBuilder inClause = new StringBuilder();
//			boolean firstValue = true;
//			for (int i=0; i < batchSize; i++) {
//			  inClause.append('?');
//			  if ( firstValue ) {
//			    firstValue = false;
//			  } else {
//			    inClause.append(',');
//			  }
//			}
//			String sql = "";
//			jdbcTemplateRead.query(sql, this.getRowMapper());
//		}
        return resultList;
    }

    private int getCount(String sql,final ParameterList params) {
        int count = 0;
        count = jdbcTemplate.query(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int index = 1;
                for (Object obj : params) {
                    ps.setObject(index++, obj);
                }
            }
        }, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                int c = 0;
                if (rs.next()) {
                    c = rs.getInt(1);
                }
                return c;
            }
        });
        return count;
    }

    @Override
    public int countByParameters(ParameterList params) {
        String sWhereSQL = makeSqlWhere(params);
        int total = 0;
        String sql = "select count(*) from " + getTableName() + sWhereSQL;
        total = getCount(sql, params);
        return total;
    }

    @Override
    public Map<String, Object> mapByParameters(int pageNo, int pageSize,final ParameterList params, String orderby) {
        String sWhereSQL = makeSqlWhere(params);

        String sOrderBy = "";
        if (orderby != null && orderby.length() > 0) {
            sOrderBy = " order by " + orderby;
        }

        int total = 0;
        int offset = 0;
        //如果pageSize<0可视为提取全部数据 10000条
        if (pageSize < 0 ) {
            pageSize = Integer.MAX_VALUE;
        }
        if (pageSize > 0) {
            offset = (pageNo - 1) * pageSize;
        }

        //返回的Map信息中包含分页信息和list数据
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("pageSize", pageSize);
        map.put("pageNo", pageNo);

        final String sSQLCount = "select count(*) from " + getTableName() + sWhereSQL;
        total = getCount(sSQLCount, params);
        map.put("totalCount", total);
        if (total == 0) {
            map.put("itemList", new ArrayList<T>(0));
            return map;
        }

        List<T> list = new ArrayList<T>(pageSize > 0 && pageSize < total ? pageSize : total);
        final String SQL_RESULT = "select * from " + getTableName() + sWhereSQL + sOrderBy + " limit " + offset + "," + pageSize;
        logger.info("SQL:{}",SQL_RESULT);
        list = jdbcTemplate.query(SQL_RESULT, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int index = 1;
                for (Object obj : params) {
                    ps.setObject(index++, obj);
                }
            }
        }, getRowMapper());
        map.put("itemList", list);
        return map;
    }

    @Override
    public RowMapper<T> getRowMapper() {
        return BeanPropertyRowMapper.newInstance(entityClass);
    }

    /**
     * 返回当前DAO使用的数据表名称
     *
     * @return 当前DAO使用的数据表
     */
    public abstract String getTableName();

    protected String makeSqlWhere(ParameterList params) {
        StringBuffer sbWhere = new StringBuffer(128);
        if (params != null && params.size() > 0) {
            params.toQuery(sbWhere.append(" where "));
        }
        String sWhereSQL = sbWhere.toString();
        return sWhereSQL;
    }
}
