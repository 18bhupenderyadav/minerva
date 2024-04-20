package com.bhupender.minerva.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private String name;

    @NotBlank(message = "Cron time cannot be blank")
    private String cronTime;

    @NotNull(message = "Execute once flag cannot be null")
    private Boolean executeOnce;

    private LocalDateTime dateTime;

    @NotBlank(message = "Command cannot be blank")
    private String command;

    @NotNull(message = "Immediate flag cannot be null")
    private Boolean immediate;

    private LocalDateTime createTime;

}
