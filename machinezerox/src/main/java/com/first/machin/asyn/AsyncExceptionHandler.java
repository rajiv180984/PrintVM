package com.first.machin.asyn;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

	private static Logger log = LoggerFactory.getLogger(AsyncExceptionHandler.class);
	
	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... params) {

		log.info(" handleUncaughtException  " + ex.getMessage() + "  " + method.getName() );

	}

}
