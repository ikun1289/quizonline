package com.tlcn.quizonline.Config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class SpringAsyncConfig implements AsyncConfigurer{
	
	@Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }
	
	@Async("threadPoolTaskExecutor")
	public void asyncMethodWithConfiguredExecutor() {
	    System.out.println("Execute method with configured executor - "
	      + Thread.currentThread().getName());
	}

	@Override
	public Executor getAsyncExecutor() {
		// TODO Auto-generated method stub
		return new ThreadPoolTaskExecutor();
	}
	
	

}
