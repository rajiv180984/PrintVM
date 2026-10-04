package com.first.machin.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FileWriteUtility {
	public static final String basePath = "/home/rajeev/Example";
	
	public static ObjectMapper mapper ;
	public static ObjectMapper getObjectMapper() {
		if(mapper == null) {
		mapper = new ObjectMapper();
		}
		return mapper;
	}
	
	
	
}
