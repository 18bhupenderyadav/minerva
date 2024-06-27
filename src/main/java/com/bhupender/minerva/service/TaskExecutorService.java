package com.bhupender.minerva.service;

import com.bhupender.minerva.model.Task;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class TaskExecutorService {

    public void executeTask(Task task) {
        try {
            String[] command = {"cmd.exe", "/c", task.getCommand()}; // Adjust the command for Windows

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Capture the output
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }
            }

            int exitCode = process.waitFor();
            String result = output.toString();

            // Log the result
            System.out.println("Task executed with ID: " + task.getTaskId() + ", exit code: " + exitCode + ", output: " + result);

            // Save the result in Redis or handle it as needed
            // Example: redisTemplate.opsForValue().set("taskResult#" + task.getTaskId(), result);

            // Report back to the user
            reportTaskResult(task.getTaskId(), exitCode, result);
        } catch (Exception e) {
            System.err.println("Exception occurred while executing task with ID: " + task.getTaskId());
            e.printStackTrace();
        }
    }

    private void reportTaskResult(Integer taskId, int exitCode, String result) {
        // Implement reporting logic, such as sending an email, saving to database, or providing an API endpoint
    }
}
