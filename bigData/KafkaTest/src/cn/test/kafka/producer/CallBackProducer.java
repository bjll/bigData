package cn.test.kafka.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.internals.BufferPool;
import org.apache.kafka.clients.producer.internals.RecordAccumulator;

public class CallBackProducer {

	public static void main(String[] args) throws InterruptedException {
		Properties props = new Properties();
		// Kafka服务端的主机名和端口号
		props.put("bootstrap.servers", "linux01:9092,linux02:9092,linux03:9092");
		// 等待所有副本节点的应答
		props.put("acks", "all");
		// 消息发送最大尝试次数
		props.put("retries", 0);
		// 一批消息处理大小
		props.put("batch.size", 16384);
		// 增加服务端请求延时
		props.put("linger.ms", 1);
		// 发送缓存区内存大小
		props.put("buffer.memory", 33554432);
		// key序列化
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		// value序列化
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		// 自定义分区
//		props.put("partitioner.class", "com.atguigu.kafka.CustomPartitioner");

		KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props);
		
		for (int i = 0; i < 50; i++) {
			Thread.sleep(500);
			//第二个参数是 消息回调函数 每一个消息都会有回调函数
			kafkaProducer.send(new ProducerRecord<String, String>("test1", "hh" + i), new Callback() {
				@Override
				public void onCompletion(RecordMetadata metadata, Exception exception) {
                 //返回发送的元数据信息
					if (metadata != null) {

						System.out.println(metadata.partition() + "---" + metadata.offset());
					}
				}
			});
		}
		kafkaProducer.close();
	}
}
