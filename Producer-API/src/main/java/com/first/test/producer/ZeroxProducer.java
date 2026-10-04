package com.first.test.producer;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.first.test.config.ProducerKafkaConfig;
import com.first.test.kafka.ProducerInstance;
import com.first.test.model.Domain;

@Service
public class ZeroxProducer {
	
	@Autowired
	ProducerKafkaConfig producerKafkaConfig;
	
	@Autowired
	ProducerInstance producerInstance;

	private final Logger logger = LoggerFactory.getLogger(ZeroxProducer.class);
	
	public void runProducer(String machineId, Domain message) throws Exception {

		final Producer<String, Domain> producer =  producerInstance.createProducer();
		try {
			final ProducerRecord<String, Domain> record = new ProducerRecord<String, Domain>(producerKafkaConfig.getTopicName(), machineId, message);
			producer.send(record, new ProducerCallback());
			producer.flush();
		} finally {
			
			//producer.close();

		}

	}
	
	public void runProducerCustomeTopic(String machineId, Domain message, String topicName) throws Exception {

		final Producer<String, Domain> producer =  producerInstance.createProducer();
		
		try {
			logger.info("Ready to Publish Data "+message.toString());
			final ProducerRecord<String, Domain> record = new ProducerRecord<String, Domain>(topicName, machineId, message);
			producer.send(record, new ProducerCallback());
			producer.flush();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			//producer.close();

		}

	}

}
