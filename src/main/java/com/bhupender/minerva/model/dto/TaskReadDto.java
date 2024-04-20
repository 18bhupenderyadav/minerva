package com.bhupender.minerva.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskReadDto {
    private Long taskId;
    private String name;
    private String cronTime;
    private Boolean executeOnce;
    private LocalDateTime dateTime;
    private String command;
    private Boolean immediate;
    private LocalDateTime createTime;
}
