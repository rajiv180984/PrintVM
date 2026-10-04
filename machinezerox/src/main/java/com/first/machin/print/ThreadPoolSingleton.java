package com.first.machin.print;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

@Component
public class ThreadPoolSingleton {
	
	private ThreadPoolExecutor executor;
	
//	;

	public ThreadPoolExecutor getExecutorInstance() {
		if(executor == null) {
			executor =(ThreadPoolExecutor) Executors.newFixedThreadPool(10);
			executor.setKeepAliveTime(10, TimeUnit.SECONDS);
		}
		return executor;
	}
}
