package com.bhupender.minerva.repository.impl;

import com.bhupender.minerva.model.Task;
import com.bhupender.minerva.mapper.TaskRowMapper;
import com.bhupender.minerva.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Task> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM taskdetails;",
                new TaskRowMapper()
        );
    }

    @Override
    public Task findById(Integer taskId) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM taskdetails WHERE \"taskId\" = ?;",
                new Object[]{taskId},
                new TaskRowMapper()
        );
    }

    @Override
    public Task create(Task task) {
        String insertSql = "INSERT INTO taskdetails (name, \"cronTime\", \"executeOnce\", \"dateTime\", command, immediate, \"createTime\") " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getCronTime());
            preparedStatement.setBoolean(3, task.getExecuteOnce());
            preparedStatement.setObject(4, task.getDateTime());
            preparedStatement.setString(5, task.getCommand());
            preparedStatement.setBoolean(6, task.getImmediate());
            preparedStatement.setObject(7, task.getCreateTime());
            return preparedStatement;
        }, keyHolder);

        task.setTaskId((Integer) Objects.requireNonNull(keyHolder.getKeys()).get("taskId"));

        return task;
    }

    @Override
    public Task update(Task task) {
        String updateSql = "UPDATE taskdetails SET name = ?, \"cronTime\" = ?, \"executeOnce\" = ?, \"dateTime\" = ?, " +
                "command = ?, immediate = ?, \"createTime\" = ? WHERE \"taskId\" = ?";
        jdbcTemplate.update(updateSql,
                task.getName(),
                task.getCronTime(),
                task.getExecuteOnce(),
                task.getDateTime(),
                task.getCommand(),
                task.getImmediate(),
                task.getCreateTime(),
                task.getTaskId());
        return task;
    }

    @Override
    public int deleteById(Integer taskId) {
        return jdbcTemplate.update("DELETE FROM taskdetails WHERE \"taskId\" = ?", taskId);
    }
}