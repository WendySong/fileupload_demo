package com.pisx.fileupload.dao.impl;

import com.pisx.fileupload.bean.File;
import com.pisx.fileupload.dao.FileDao;
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
@Repository
public class FileDaoImpl extends BaseDaoImpl<File> implements FileDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public int create(final File entity) {
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
    public int update(File entity) {
        return super.update(entity);
    }

    @Override
    public String getTableName() {
        return "file";
    }

    @Override
    public RowMapper<File> getRowMapper() {
        return new RowMapper<File>() {
            @Override
            public File mapRow(ResultSet resultSet, int i) throws SQLException {
                File file = new File();
                file.setId(resultSet.getInt("ID"));
                file.setFilename(resultSet.getString("filename"));
                file.setPath(resultSet.getString("path"));
                file.setCreateTime(resultSet.getTimestamp("createtime"));
                file.setStatus(resultSet.getInt("status"));
                file.setMd5(resultSet.getString("md5"));
                return file;
            }
        };
    }
}
