package com.pisx.fileupload.dao.impl;

import com.pisx.fileupload.bean.FileUpload;
import com.pisx.fileupload.dao.FileUploadDao;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.*;

/**
 * Created by pisx on 2014/12/25.
 */
@Repository(value = "fileUploadDao")
public class FileUploadDaoImpl extends BaseDaoImpl<FileUpload> implements FileUploadDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public int create(final FileUpload entity) throws DataAccessException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                String sql = "INSERT INTO file(filename, path, createtime, status, md5)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"ID"});
                int i = 1;
                preparedStatement.setString(i++, entity.getFilename());
                preparedStatement.setString(i++,entity.getPath());
                preparedStatement.setTimestamp(i++, new Timestamp(entity.getCreateTime().getTime()));
                preparedStatement.setInt(i++,entity.getStatus());
                preparedStatement.setString(i++,entity.getMd5());
                return preparedStatement;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public int update(FileUpload entity) {
        return super.update(entity);
    }

    @Override
    public FileUpload getByMD5(String md5) throws IncorrectResultSizeDataAccessException {
        String sql = "select * from " + getTableName() + " where md5=?";
        FileUpload obj = jdbcTemplate.queryForObject(sql, getRowMapper(), md5);
        return obj;
    }

    @Override
    public String getTableName() {
        return "file";
    }

    @Override
    public RowMapper<FileUpload> getRowMapper() {
        return new RowMapper<FileUpload>() {
            @Override
            public FileUpload mapRow(ResultSet resultSet, int i) throws SQLException {
                FileUpload fileUpload = new FileUpload();
                fileUpload.setId(resultSet.getInt("ID"));
                fileUpload.setFilename(resultSet.getString("filename"));
                fileUpload.setPath(resultSet.getString("path"));
                fileUpload.setCreateTime(resultSet.getTimestamp("createtime"));
                fileUpload.setStatus(resultSet.getInt("status"));
                fileUpload.setMd5(resultSet.getString("md5"));
                return fileUpload;
            }
        };
    }
}
