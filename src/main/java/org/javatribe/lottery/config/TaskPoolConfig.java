package org.javatribe.lottery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author Jimzising
 * @Date 2019/10/21
 * @Desc
 */
@Configuration
public class TaskPoolConfig {

    @Bean
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 初始线程数
        executor.setCorePoolSize(2);
        // 最大线程数
        executor.setMaxPoolSize(2);
        // 等待队列容量
        executor.setQueueCapacity(32);
        // 等待时间
        executor.setKeepAliveSeconds(10);
        // 线程前缀
        executor.setThreadNamePrefix("taskExecutor-");
        // 线程拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(15);
        return executor;
    }
}
