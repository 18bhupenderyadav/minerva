package com.bhupender.minerva.service;

import com.bhupender.minerva.model.Task;
import com.bhupender.minerva.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TaskRedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private TaskExecutorService taskExecutorService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private RabbitMQProducerService rabbitMQProducerService;

    @Autowired
    private CronParserService cronParserService;

    @Value("${TASK_NOTIFY_PREFIX:notify#}")
    private String taskNotifyPrefix;

    @Value("${TASK_DATA_PREFIX:data#}")
    private String taskDataPrefix;

    /**
     * Set the TTL for a given key in Redis.
     *
     * @param key       The key to set the TTL for.
     * @param ttlSeconds The TTL in seconds.
     */
    private void setTTL(String key, long ttlSeconds) {
        redisTemplate.expire(key, ttlSeconds, TimeUnit.SECONDS);
        System.out.println("Set TTL for key: " + key + " to " + ttlSeconds + " seconds");
    }

    /**
     * Save a task to Redis and set its expiration time.
     *
     * @param task The task to save.
     */
    public void saveTaskToRedis(Task task) {
        System.out.println("Received task: " + task);

        if (task.isImmediate()) {
            // Immediate execution
            System.out.println("Task is immediate. Setting execution time to now and executeOnce to true.");
            task.setDateTime(LocalDateTime.now());
            task.setExecuteOnce(true);
            System.out.println("Task updated with current time: " + task.getDateTime());

            String expiryKey = taskNotifyPrefix + task.getTaskId() + "#" + task.getDateTime();
            String dataKey = taskDataPrefix + task.getTaskId() + "#" + task.getDateTime();
            redisTemplate.opsForValue().set(expiryKey, task.getTaskId());
            redisTemplate.opsForValue().set(dataKey, task);
            setTTL(expiryKey, 10); // Example TTL of 10 seconds
            System.out.println("Task saved to Redis with TTL of 10 seconds.");

        } else if (task.isExecuteOnce()) {
            // Execute once at specified dateTime
            LocalDateTime dateTime = LocalDateTime.parse(task.getDateTime().toString());
            System.out.println("Task is set to execute once at: " + dateTime);

            long secondsUntilExecution = Duration.between(LocalDateTime.now(), dateTime).getSeconds();
            System.out.println("Seconds until execution: " + secondsUntilExecution);

            // Save to Redis with TTL if it is greater than 0
            if (secondsUntilExecution > 0) {
                String expiryKey = taskNotifyPrefix + task.getTaskId() + "#" + task.getDateTime();
                String dataKey = taskDataPrefix + task.getTaskId() + "#" + task.getDateTime();
                redisTemplate.opsForValue().set(expiryKey, task.getTaskId());
                redisTemplate.opsForValue().set(dataKey, task);
                setTTL(expiryKey, secondsUntilExecution);
                System.out.println("Task saved to Redis with TTL of " + secondsUntilExecution + " seconds.");
            } else {
                System.out.println("Execution time is in the past or less than current time. Task not saved.");
            }
        } else {
            // Recurring execution with cronTime
            System.out.println("Task is recurring with cron expression: " + task.getCronTime());

            List<LocalDateTime> executionTimes = cronParserService.getExecutionTimesForToday(task.getCronTime());
            for (LocalDateTime executionTime : executionTimes) {
                long secondsUntilExecution = Duration.between(LocalDateTime.now(), executionTime).getSeconds();
                System.out.println("Scheduled execution time: " + executionTime + ", seconds until execution: " + secondsUntilExecution);

                // Save each execution time as a separate entry in Redis
                if (secondsUntilExecution > 0) {
                    String expiryKey = taskNotifyPrefix + task.getTaskId() + "#" + executionTime;
                    String dataKey = taskDataPrefix + task.getTaskId() + "#" + executionTime; // Corrected dataKey
                    redisTemplate.opsForValue().set(expiryKey, task.getTaskId());
                    redisTemplate.opsForValue().set(dataKey, task); // Corrected dataKey
                    setTTL(expiryKey, secondsUntilExecution);
                    System.out.println("Task saved to Redis for execution at " + executionTime + " with TTL of " + secondsUntilExecution + " seconds.");
                } else {
                    System.out.println("Execution time " + executionTime + " is in the past or less than current time. Task not saved for this execution time.");
                }
            }
        }
    }


    /**
     * Handle the event when a Redis key expires.
     *
     * @param key The expired key.
     */
    public void handleExpiredTaskEvent(String key) {
        // Extract task ID and execution time from the key
        String[] parts = key.replace(taskNotifyPrefix, "").split("#");
        if (parts.length >= 2) {
            Integer taskId = Integer.parseInt(parts[0]);
            LocalDateTime executionTime = LocalDateTime.parse(parts[1]);

            System.out.println("Handling expired task event for key: " + key + " with task ID: " + taskId + " and execution time: " + executionTime);

            Task expiredTask = constructTaskFromRedis(taskId, executionTime);

            if (expiredTask != null) {
                rabbitMQProducerService.sendMessageToExecutionQueue(expiredTask);
            }
        } else {
            System.out.println("Invalid key format: " + key);
        }
    }

    private Task constructTaskFromRedis(Integer taskId, LocalDateTime executionTime) {
        String dataKey = taskDataPrefix + taskId + "#" + executionTime;
        Task task = (Task) redisTemplate.opsForValue().get(dataKey);
        if (task != null) {
            System.out.println("Retrieved task with ID: " + taskId + " and command: " + task.getCommand());
        } else {
            System.out.println("No task found for ID: " + taskId + " and execution time: " + executionTime);
        }
        return task;
    }

}
