package com.first.machin.asyn;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

 

@Configuration
@EnableAsync
public class AsyncConfiguration  implements AsyncConfigurer
{
	@Value("${corePoolSize}")
	private int corePoolSize;
	
	@Value("${bulkMaxPoolSize}")
	private int bulkMaxPoolSize;
	
	@Value("${bulkQueueSize}")
	private int bulkQueSize;
	
	@Bean(name = "bulkObAsyncExecutor")
	public Executor bulkObAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(bulkMaxPoolSize);
		executor.setQueueCapacity(bulkQueSize);
		
		//executor.setKeepAliveSeconds(100);
		executor.setAllowCoreThreadTimeOut(true);
		executor.setThreadNamePrefix("BulkObAsyncExecutor-");
		executor.initialize();
		return executor;
	}

	@Autowired
	private AsyncExceptionHandler asyncExceptionHandler;

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return  asyncExceptionHandler;
	}
}
