package com.bhupender.minerva.mapper;

import com.bhupender.minerva.model.Task;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class TaskRowMapper implements RowMapper<Task> {

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Retrieve the updateAtTime column as an Array of LocalDateTime
//        Array updateAtTimeArray = rs.getArray("updateAtTime");
//        List<LocalDateTime> updateAtTimeList;
//        if (updateAtTimeArray != null) {
//            // Convert the Array to a List of LocalDateTime
//            updateAtTimeList = Arrays.asList((LocalDateTime[]) updateAtTimeArray.getArray());
//        } else {
//            updateAtTimeList = Collections.emptyList();
//        }

        // Create and return a Task object with all attributes
        return new Task(
                rs.getInt("taskId"),
                rs.getString("name"),
                rs.getString("cronTime"),
                rs.getBoolean("executeOnce"),
                rs.getTimestamp("dateTime").toLocalDateTime(),
                rs.getString("command"),
                rs.getBoolean("immediate"),
                rs.getTimestamp("createTime").toLocalDateTime()
//                updateAtTimeList
        );
    }
}
