package com.first.test.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.first.test.config.ProducerKafkaConfig;
import com.first.test.model.Domain;
import com.first.test.serde.GenericSerializer;
import com.first.test.serde.HashPartitioner;

@Service
public class ProducerInstance {
	private Logger logger = LoggerFactory.getLogger(ProducerInstance.class);
	@Autowired
	ProducerKafkaConfig producerKafkaConfig;
	
	@Value("${spring.profiles.active}")
	private String env;
	
	Producer<String, Domain> kafkaProducer = null;

	@Autowired
	public Producer<String, Domain> createProducer() {
		if (kafkaProducer == null) {
			Properties props = getKafkaConfig();
			props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
					(new GenericSerializer<Domain>()).getClass().getName());
			props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
			props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, HashPartitioner.class);
			kafkaProducer = new KafkaProducer<String, Domain>(props);
		}
		return kafkaProducer;
	}

	private Properties getKafkaConfig() {
		
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerKafkaConfig.getBootstrapServers());
		props.put(ProducerConfig.CLIENT_ID_CONFIG, producerKafkaConfig.getClientId());

		props.put(ProducerConfig.ACKS_CONFIG, producerKafkaConfig.getAck());
		props.put(ProducerConfig.RETRIES_CONFIG, producerKafkaConfig.getRetries());

		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, producerKafkaConfig.getBufferMemory());
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, producerKafkaConfig.getBatchsize());

		if(!env.equalsIgnoreCase("LOCAL")) {
			logger.info("=================== ENV Name ============="+env);
			System.setProperty("java.security.auth.login.config", producerKafkaConfig.getKafkaJaasPath());//Provide jaas file path.
			System.setProperty("javax.security.auth.useSubjectCredsOnly", "true"); 
			System.setProperty("sun.security.krb5.debug", producerKafkaConfig.getSecurityKrb5Debug()); 	
			
			logger.info("===== Kafka Producer Env :"+ env);
			//props.put(ProducerConfig.SECURITY_PROTOCOL_CONFIG, producerKafkaConfig.getSecurityProtocol());
			 props.put("security.protocol", "SASL_PLAINTEXT");
			 props.put("kafka.cluster.SecurityProtocol", producerKafkaConfig.getClusterSecurityProtocol());
			 props.put("sasl.kerberos.service.name",  producerKafkaConfig.getSaslKerberosServiceName()); 
			 props.put("sasl.kerberos", "sasl.kerberos.service.namekafka");		
			 
			 //////////////////
		
			 props.put("security.inter.broker.protocol", "SASL_PLAINTEXT");
			 props.put("sasl.mechanism.inter.broker.protocol", "PLAIN");
			 props.put("sasl.enabled.mechanisms", "PLAIN");
			 
			/* props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
			 props.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule username="<api-key>" password="<api-secret>";");*/
		}
		logger.info(" Properties  ============="+ producerKafkaConfig.toString());
		return props;
	}

}
