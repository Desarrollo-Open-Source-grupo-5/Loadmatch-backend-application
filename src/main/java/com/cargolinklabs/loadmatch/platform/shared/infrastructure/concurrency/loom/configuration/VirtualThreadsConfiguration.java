package com.cargolinklabs.loadmatch.platform.shared.infrastructure.concurrency.loom.configuration;

import org.jspecify.annotations.NullMarked;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Loom Concurrency configuration
 * <p>
 *     Configures asynchronous task execution using OpenJDK Project Loom virtual threads.
 * </p>
 */
@Configuration
@EnableAsync
@NullMarked
public class VirtualThreadsConfiguration {

    /**
     * Creates a TaskExecutor bean backed by Project Loom virtual threads
     */
    @Bean(name = "applicationTaskExecutor")
    public TaskExecutor applicationTaskExecutor() {
        var executor = new SimpleAsyncTaskExecutor("loom-exec-");
        executor.setVirtualThreads(true);
        return executor;
    }
}
