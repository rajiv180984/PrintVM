package com.first.test.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerCallback implements Callback {
	private final Logger logger = LoggerFactory.getLogger(ZeroxProducer.class);

	@Override
	public void onCompletion(RecordMetadata recordMetadata, Exception exp) {

		if (exp != null) {
			logger.error("Failed to process Message " + recordMetadata, exp);
			logger.info("1" + exp.getClass() + " \n ");
			if (exp.getClass().equals(KafkaException.class) || exp.getClass().equals(AuthorizationException.class) || exp.getClass().equals(TimeoutException.class)) {
				throw new KafkaException("Kafka Services is not avilable", exp);
			} else {

				// integrate Error Topic
				// Step read the record and create Error record for Error topic
			}

		}
	}
}
