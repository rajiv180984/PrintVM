
package com.first.test.kafka;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.acl.AccessControlEntry;
import org.apache.kafka.common.acl.AclBinding;
import org.apache.kafka.common.acl.AclOperation;
import org.apache.kafka.common.acl.AclPermissionType;
import org.apache.kafka.common.config.TopicConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.first.test.config.ProducerKafkaConfig;

import kafka.admin.AdminUtils;
import kafka.security.auth.Resource;
import kafka.security.auth.ResourceType;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;

//@Component
public class KafkaAdminUtil {
	private final Logger logger = LoggerFactory.getLogger(KafkaAdminUtil.class);

	@Autowired
	ProducerKafkaConfig producerKafkaConfig;

	@PostConstruct
	public void init() {
		ZkClient zkClient = null;
		ZkUtils zkUtils = null;
		try {
			String zookeeperHosts = producerKafkaConfig.getZookeeperURL(); // If multiple zookeeper then -> String
																			// zookeeperHosts =
																			// "192.168.20.1:2181,192.168.20.2:2181";
			int sessionTimeOutInMs = Integer.parseInt(producerKafkaConfig.getzKsessionTimeOutInMs());
			int connectionTimeOutInMs = Integer.parseInt(producerKafkaConfig.getzKconnectionTimeOutInMs());

			zkClient = new ZkClient(zookeeperHosts, sessionTimeOutInMs, connectionTimeOutInMs,
					ZKStringSerializer$.MODULE$);
			zkUtils = new ZkUtils(zkClient, new ZkConnection(zookeeperHosts), false);
			
			String topicName = producerKafkaConfig.getTopicName();
			int noOfPartitions = producerKafkaConfig.getNoOfPartitions();
			int noOfReplication = producerKafkaConfig.getNoOfReplicatio();
			Properties topicConfiguration = new Properties();

			if (AdminUtils.topicExists(zkUtils, topicName)) {
				logger.info(topicName + " topic Exist in Kafka ");
			} else {
				
				Properties props = new Properties();
				
				props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafkabroker.mydomain.com:9094");
			    props.put("security.protocol","SASL_SSL");
			    props.put("ssl.truststore.location", "C:\\Temp\\mydomain.root.jks" );
			    props.put("ssl.truststore.password","my_truststore_password");
			    props.put("sasl.mechanism","GSSAPI");
			    props.put("sasl.kerberos.service.name","kafka_admin_username");
			    
				AdminClient adminClient = AdminClient.create(props);
				
				
			    // generate ACLs
				AccessControlEntry accessControlEntry =	new AccessControlEntry("MY_TESTID", "*", AclOperation.READ, AclPermissionType.ALLOW) ;
			    AclBinding newTopicReadAcl = new AclBinding(new org.apache.kafka.common.resource.Resource(org.apache.kafka.common.resource.ResourceType.TOPIC, "TEST-Topic"), accessControlEntry);
			    
			   /* AclBinding newTopicDescribeAcl = new AclBinding( new Resource(ResourceType.TOPIC, "TestTopic"),
			            new AccessControlEntry("MY_TESTID", "*", AclOperation.DESCRIBE, AclPermissionType.ALLOW) );
			    AclBinding newGroupReadAcl = new AclBinding( new Resource(ResourceType.GROUP, "TestGroup"),
			            new AccessControlEntry("MY_TESTID", "*", AclOperation.READ, AclPermissionType.ALLOW) );*/
			    Collection<AclBinding> aclList = Arrays.asList(newTopicReadAcl);
			    adminClient.createAcls(aclList);


			    // create topic 
			    int numPartitions = 6;
			    short replicasFactor = 2;
			    NewTopic newTopic = new NewTopic("Demo.JavaAdminClientTest", numPartitions, replicasFactor);
			    Map<String, String> configMap = new HashMap<>();
			    configMap.put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_COMPACT);
			    configMap.put(TopicConfig.COMPRESSION_TYPE_CONFIG, "gzip");
			    newTopic.configs(configMap);
			    List<NewTopic> topics = Arrays.asList(newTopic);
			    adminClient.createTopics( topics );
				logger.error("Kafka Topic does not exist");
				throw new KafkaException("Kakfa Topic does not exist");
				// To-Do creat new topic
				// AdminUtils.createTopic(zkUtils, topicName, noOfPartitions, noOfReplication,
				// topicConfiguration, Enforced$.MODULE$);
			}

		} catch (Exception ex) {

			logger.error("fafka Failure", ex);
		} finally {
			if (zkClient != null) {
				zkClient.close();
			}
		}
	}
}