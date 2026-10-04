package com.first.test.config;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "file:${configpath}-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
@ConfigurationProperties
@Configuration("producerKafkaConfig")
public class ProducerKafkaConfig implements Serializable {

	private static final long serialVersionUID = 1436876234515569044L;

	@Value("${kafka.topic.name}")
	private String topicName;

	@Value("${kafka.bootstrap.servers}")
	private String bootstrapServers;

	private String[] brokerList;
	
	private String[] zookeeperList;
	
	@Value("${zookeeper.url}")
	private String zookeeperURL;

	@Value("${kafka.buffer.memory}")
	private String bufferMemory;
	
	@Value("${kafka.retries}")
	private String retries;

	@Value("${kafka.acks}")
	private String ack;

	@Value("${kafka.batch.size}")
	private String batchsize;

	@Value("${kafka.linger.ms}")
	private String lingerLatency;

	@Value("${kafka.autoOffsetReset}")
	private String autoOffsetReset;

	@Value("${kafka.processingGuarantee}")
	private String processingGuarantee;

	@Value("${zookeeper.sessionTimeOutInMs}")
	private String zKsessionTimeOutInMs;
	
	@Value("${zookeeper.connectionTimeOutInMs}")
	private String zKconnectionTimeOutInMs;
	
	@Value("${kafka.topic.noOfPartitions}")
	private int noOfPartitions;
	
	@Value("${kafka.topic.noOfReplication}")
	private int noOfReplication;
	
	@Value("${kafka.client.id}")
	private String clientId;
	
	@Value("${kafka.jaas.path}")
	private String kafkaJaasPath;
	
	@Value("${kafka.security.protocol}")
	private String securityProtocol;
	
	@Value("${kafka.cluster.SecurityProtocol}")
	private String clusterSecurityProtocol;
	
	@Value("${kafka.sun.security.krb5.debug}")
	private String securityKrb5Debug;
	
	
	@Value("${kafka.sasl.kerberos.service.name}")
	private String saslKerberosServiceName;
	
	@Value("${kafka.sasl.kerberos}")
	private String saslKerberos;
	
	@Value("${kafka.partner.topic}")
	private String partnerTopic;
	
	public String getBootstrapServers() {
		return bootstrapServers;
	}

	public void setBootstrapServers(String bootstrapServer) {
		this.bootstrapServers = bootstrapServer;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String[] getBrokerList() {
		return bootstrapServers.split(",");
	}

	public String getZookeeperURL() {
		return zookeeperURL;
	}

	public void setZookeeperURL(String zookeeperURL) {
		this.zookeeperURL = zookeeperURL;
	}

	public String getRetries() {
		return retries;
	}

	public void setRetries(String retries) {
		this.retries = retries;
	}

	public String getAck() {
		return ack;
	}

	public void setAck(String ack) {
		this.ack = ack;
	}

	public String getBatchsize() {
		return batchsize;
	}

	public void setBatchsize(String batchsize) {
		this.batchsize = batchsize;
	}

	public String getLingerLatency() {
		return lingerLatency;
	}

	public void setLingerLatency(String lingerLatency) {
		this.lingerLatency = lingerLatency;
	}

	public String getAutoOffsetReset() {
		return autoOffsetReset;
	}

	public void setAutoOffsetReset(String autoOffsetReset) {
		this.autoOffsetReset = autoOffsetReset;
	}

	public String getProcessingGuarantee() {
		return processingGuarantee;
	}

	public void setProcessingGuarantee(String processingGuarantee) {
		this.processingGuarantee = processingGuarantee;
	}

	public String[] getZookeeperList() {
		return zookeeperURL.split(",");
	}

	public void setZookeeperList(String[] zookeeperList) {
		this.zookeeperList = zookeeperURL.split(",");
	}

	
	public String getzKsessionTimeOutInMs() {
		return zKsessionTimeOutInMs;
	}

	public void setzKsessionTimeOutInMs(String zKsessionTimeOutInMs) {
		this.zKsessionTimeOutInMs = zKsessionTimeOutInMs;
	}

	public String getzKconnectionTimeOutInMs() {
		return zKconnectionTimeOutInMs;
	}

	public void setzKconnectionTimeOutInMs(String zKconnectionTimeOutInMs) {
		this.zKconnectionTimeOutInMs = zKconnectionTimeOutInMs;
	}

	public int getNoOfPartitions() {
		return noOfPartitions;
	}

	public void setNoOfPartitions(int noOfPartitions) {
		this.noOfPartitions = noOfPartitions;
	}

	public int getNoOfReplicatio() {
		return noOfReplication;
	}

	public void setNoOfReplicatio(int noOfReplicatio) {
		this.noOfReplication = noOfReplicatio;
	}

	
	public int getNoOfReplication() {
		return noOfReplication;
	}

	public void setNoOfReplication(int noOfReplication) {
		this.noOfReplication = noOfReplication;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	
	public String getBufferMemory() {
		return bufferMemory;
	}

	public void setBufferMemory(String bufferMemory) {
		this.bufferMemory = bufferMemory;
	}

	public String getKafkaJaasPath() {
		return kafkaJaasPath;
	}

	public void setKafkaJaasPath(String kafkaJaasPath) {
		this.kafkaJaasPath = kafkaJaasPath;
	}

	public String getSecurityProtocol() {
		return securityProtocol;
	}

	public void setSecurityProtocol(String securityProtocol) {
		this.securityProtocol = securityProtocol;
	}

	public String getClusterSecurityProtocol() {
		return clusterSecurityProtocol;
	}

	public void setClusterSecurityProtocol(String clusterSecurityProtocol) {
		this.clusterSecurityProtocol = clusterSecurityProtocol;
	}

	public String getSecurityKrb5Debug() {
		return securityKrb5Debug;
	}

	public void setSecurityKrb5Debug(String securityKrb5Debug) {
		this.securityKrb5Debug = securityKrb5Debug;
	}

	public void setBrokerList(String[] brokerList) {
		this.brokerList = brokerList;
	}

	
	public String getSaslKerberosServiceName() {
		return saslKerberosServiceName;
	}

	public void setSaslKerberosServiceName(String saslKerberosServiceName) {
		this.saslKerberosServiceName = saslKerberosServiceName;
	}

	public String getSaslKerberos() {
		return saslKerberos;
	}

	public void setSaslKerberos(String saslKerberos) {
		this.saslKerberos = saslKerberos;
	}

	
	public String getPartnerTopic() {
		return partnerTopic;
	}

	public void setPartnerTopic(String partnerTopic) {
		this.partnerTopic = partnerTopic;
	}

	@Override
	public String toString() {
		return "ProducerKafkaConfig [topicName=" + topicName + ", bootstrapServers=" + bootstrapServers
				+ ", brokerList=" + Arrays.toString(brokerList) + ", zookeeperList=" + Arrays.toString(zookeeperList)
				+ ", zookeeperURL=" + zookeeperURL + ", bufferMemory=" + bufferMemory + ", retries=" + retries
				+ ", ack=" + ack + ", batchsize=" + batchsize + ", lingerLatency=" + lingerLatency
				+ ", autoOffsetReset=" + autoOffsetReset + ", processingGuarantee=" + processingGuarantee
				+ ", zKsessionTimeOutInMs=" + zKsessionTimeOutInMs + ", zKconnectionTimeOutInMs="
				+ zKconnectionTimeOutInMs + ", noOfPartitions=" + noOfPartitions + ", noOfReplication="
				+ noOfReplication + ", clientId=" + clientId + ", kafkaJaasPath=" + kafkaJaasPath
				+ ", securityProtocol=" + securityProtocol + ", clusterSecurityProtocol=" + clusterSecurityProtocol
				+ ", securityKrb5Debug=" + securityKrb5Debug + "]";
	}

	

	
	
}
