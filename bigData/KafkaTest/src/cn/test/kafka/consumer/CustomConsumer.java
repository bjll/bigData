package cn.test.kafka.consumer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;


public class CustomConsumer  implements ConsumerRebalanceListener  {
	public static void main(String[] args) {
		Properties props = new Properties();
		//1.-------配置消费者的属性----------------
		// 定义kakfa 服务的地址，不需要将所有broker指定上 
		props.put("bootstrap.servers", "192.168.148.129:9092");
		// 制定consumer group 
		props.put("group.id", "test-consumer-group");
		// 是否自动确认offset 
		props.put("enable.auto.commit", "true");
		// 自动确认offset的时间间隔 
		props.put("auto.commit.interval.ms", "1000");
		// key的反序列化类
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		// value的反序列化类 
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		// 2.------------------定义consumer 
		final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props); //出于线程安全的考虑 设置为final 类型
        Runtime.getRuntime().addShutdownHook(new  Thread(new  Runnable() {  //在jvm 宕机时才开始调用
			@Override
			public void run() {
				if(consumer!=null){
					consumer.close();
				}
				
			}
		}));
		// 消费者订阅的topic, 可同时订阅多个 
		consumer.subscribe(Arrays.asList("first"));//订阅主题
		//consumer.assign(arg0);//手动订阅Topic 并且指定消费分区
		//consumer.commit*()//提交消费者已经完成的offset
        //3.拉消息
		while (true) {
			// 读取数据，读取超时时间为100ms  这之所以这样做是因为kafka 是以时间窗为单位的 在某个时间内可能不止一条数据也可能是某些数据的集合
			ConsumerRecords<String, String> records = consumer.poll(100); //拉取消息的最大时间间隔是100 毫秒
			//offset  偏移量
			//key : 根据键进行哈希取值分到不同的分区
			for (ConsumerRecord<String, String> record : records)
				System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
		}
		
	}
    /**
     *调用时机是 在consumer 停止 拉去数据之后，rebalance 开始之前
     *可以在这个方法方法中手动提交offset 防止被重复消费
     */
	@Override
	public void onPartitionsAssigned(Collection<TopicPartition> arg0) {
		
		
	}
    /**
     *在拉去数据数据之前 rebalance执行之后 可以在这里设置 offset的值 
     */
	@Override
	public void onPartitionsRevoked(Collection<TopicPartition> arg0) {
		
		
	}

}
