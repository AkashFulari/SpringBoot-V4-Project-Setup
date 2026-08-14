package com.akashf.springv4.demo.jobs.cron;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestJob {

    // Runs every minute
    @Scheduled(cron = "0 * * * * *")
    public void executeTask() {
        System.out.println("Cron job executed successfully!");
        // Your core business logic goes here
    }

}
