package com.bhupender.minerva.repository;

import com.bhupender.minerva.model.Task;

import java.util.List;


public interface TaskRepository {
    List<Task> findAll();

    Task findById(Integer task_id);

    Task create(Task task);

    Task update(Task task);

    int deleteById(Integer task_id);
}
