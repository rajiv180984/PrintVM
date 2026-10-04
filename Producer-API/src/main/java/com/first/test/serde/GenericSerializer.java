package com.first.test.serde;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.test.model.Domain;

public class GenericSerializer<T extends Domain> implements Serializer<T>{

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] serialize(String topic, T data) {
		byte[] retVal = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
		     retVal = objectMapper.writeValueAsString(data).getBytes();
		   } catch (Exception e) {
		     e.printStackTrace();
		   }
		   return retVal;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	
}
