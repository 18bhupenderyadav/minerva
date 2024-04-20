package com.bhupender.minerva.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
//import java.util.List;

/**
 * This class represents the Task entity which includes all its properties
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    private Integer taskId; // Changed to Long for better compatibility with database IDs
    private String name;
    private String cronTime;
    private Boolean executeOnce; // Consider renaming for clarity
    private LocalDateTime dateTime; // Consider using ZonedDateTime if timezone support is needed
    private String command;
    private Boolean immediate; // Consider reviewing necessity
    private LocalDateTime createTime;
//    private List<LocalDateTime> updateAtTime; // Storing update times in the database

    // Consider adding getters and setters for updateAtTime if needed
}
