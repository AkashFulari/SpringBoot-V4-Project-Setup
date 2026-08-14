package com.akashf.springv4.demo.jobs.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class JobWorker {

    @Async("jobExecutor")
    public void processHeavyJob(String jobName) {
        try {
            System.out.println("Started processing queued job: " + jobName);
            // Simulate a heavy task (like sending an email or generating a PDF)
            Thread.sleep(10000);
            System.out.println("Finished processing queued job: " + jobName);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
