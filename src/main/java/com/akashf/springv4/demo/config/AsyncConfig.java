package com.akashf.springv4.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Bean(name = "jobExecutor")
    public Executor jobExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Minimum active workers
        executor.setMaxPoolSize(10); // Maximum allowed workers
        executor.setQueueCapacity(100); // The max number of jobs allowed to wait in the queue
        executor.setThreadNamePrefix("JobWorker-");
        executor.initialize();
        return executor;
    }
}
