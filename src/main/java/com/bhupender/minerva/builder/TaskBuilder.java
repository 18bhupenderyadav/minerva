package com.bhupender.minerva.builder;

import com.bhupender.minerva.model.Task;
import com.bhupender.minerva.model.dto.TaskDto;
import com.bhupender.minerva.model.dto.TaskReadDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskBuilder {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static TaskDto convertToDto(Task task) {
        return modelMapper.map(task, TaskDto.class);
    }

    public static TaskReadDto convertToReadDto(Task task) {
        return modelMapper.map(task, TaskReadDto.class);
    }

    public static Task convertToEntity(TaskDto taskDto) {
        return modelMapper.map(taskDto, Task.class);
    }
}
