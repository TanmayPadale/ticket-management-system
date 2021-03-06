package com.example.demo.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
@EnableAsync
public class AsyncConfig 
{
	  @Bean
	  public Executor asyncExecutor() 
	  {
	    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	    executor.setCorePoolSize(16);
	    executor.setMaxPoolSize(20);
	    executor.setQueueCapacity(1000);
	    executor.setThreadNamePrefix("AsyncThread-");
	    executor.initialize();
	    return executor;
	  }
	
}
