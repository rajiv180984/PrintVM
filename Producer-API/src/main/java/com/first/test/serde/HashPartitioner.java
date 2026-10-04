package com.first.test.serde;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashPartitioner implements Partitioner {
	private Logger logger = LoggerFactory.getLogger(HashPartitioner.class);

	@Override
	public void configure(Map<String, ?> configs) {
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
		int numPartitions = partitions.size();
		logger.info(" Partition of Topic :" + numPartitions);
		if (keyBytes != null) {
			int hashCode = 0;
			if (key instanceof Integer || key instanceof Long) {
				hashCode = (int) key;
			} else {
				hashCode = key.hashCode();
			}
			hashCode = hashCode & 0x7fffffff;
			return hashCode % numPartitions;
		} else {
			return 0;
		}
	}

	@Override
	public void close() {
	}

}
