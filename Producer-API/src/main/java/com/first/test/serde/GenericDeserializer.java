package com.first.test.serde;

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.test.model.Domain;

public class GenericDeserializer<T extends Domain> implements Deserializer<T> {

	private Class<T> type;
	private ObjectMapper objectMapper = new ObjectMapper();

	public GenericDeserializer() {}
	
	public GenericDeserializer(Class<T> type) {
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		if(type != null) {
			return;
		}
		
		String typeProp = isKey ? "key.deserializer.type" : "value.deserializer.type";
		String typeName = (String)configs.get(typeProp);
		try {
			type = (Class<T>)Class.forName(typeName);
		} catch (Exception ex) {
			throw new SerializationException("Failed to initialize GenericDeserializer for " + typeName, ex);
		}
	}

	@Override
	public T deserialize(String topic, byte[] data) {
		if (data == null) {
			return null;
		}
		try {
			return this.objectMapper.readValue(data, type);
		} catch (IOException ex) {
			throw new SerializationException(ex);
		}
	}

	@Override
	public void close() {
	}

}