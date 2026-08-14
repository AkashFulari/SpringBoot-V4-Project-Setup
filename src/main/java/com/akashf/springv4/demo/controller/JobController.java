package com.akashf.springv4.demo.controller;

import org.springframework.web.bind.annotation.*;

import com.akashf.springv4.demo.jobs.async.JobWorker;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/jobs/test")
public class JobController {
    private final JobWorker jobWorker;

    public JobController(JobWorker jobWorker) {
        this.jobWorker = jobWorker;
    }

    @GetMapping("/{taskName}")
    public ResponseEntity<String> submitJob(@PathVariable String taskName) {
        // Drop the job into the internal queue
        jobWorker.processHeavyJob(taskName);
        // Instantly reply to the client
        return ResponseEntity.ok("Job submitted successfully! It is now running in the background queue.");
    }
}
