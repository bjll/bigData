package cn.test.kafka.patitioner;

import java.util.Map;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
/**
 * kafka  分区
 * @author lilin
 *
 */
public class CustomPartitioner implements Partitioner {

	@Override
	public void configure(Map<String, ?> configs) {
		
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        return  1;
	}

	@Override
	public void close() {
		
	}
}


