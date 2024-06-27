package com.bhupender.minerva.service;

import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.cronutils.model.CronType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CronParserService {

    private final CronParser cronParser;

    public CronParserService() {
        this.cronParser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));
    }

    public List<LocalDateTime> getExecutionTimesForToday(String cronExpression) {
        System.out.println("Parsing cron expression: " + cronExpression);

        Cron cron = cronParser.parse(cronExpression);
        ExecutionTime executionTime = ExecutionTime.forCron(cron);
        ZonedDateTime now = ZonedDateTime.now();

        System.out.println("Parsed cron object: " + cron);

        List<LocalDateTime> executionTimes = new ArrayList<>();

        // Iterate over the next execution times for today
        while (true) {
            ZonedDateTime nextExecution = executionTime.nextExecution(now).get();
            if (!nextExecution.toLocalDate().equals(now.toLocalDate())) {
                break; // Stop if next execution is on another day
            }
            executionTimes.add(nextExecution.toLocalDateTime());
            now = nextExecution.plusSeconds(1); // Move to the next second to avoid getting the same time again
        }

        System.out.println("Execution times for today:");
        executionTimes.forEach(System.out::println);

        return executionTimes;
    }
}
