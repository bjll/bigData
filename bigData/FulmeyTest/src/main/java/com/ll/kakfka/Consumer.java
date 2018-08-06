package com.ll.kakfka;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import org.apache.flume.source.SyslogUDPSource.syslogHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;



public class Consumer {
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "node2:9092");
		props.put("group.id", "test-consumer-group");
		props.put("fetch.max.bytes", 1024);// 为了便于测试，这里设置一次fetch// 请求取得的数据最大值为1KB,默认是5MB
		props.put("enable.auto.commit", false);// 设置提交偏移量（true 自动提交 false 手动提交）
	    props.put("auto.commit.interval.ms", "1000");// 自动确认offset的时间间隔 
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		final KafkaConsumer<String, String> consumer = new KafkaConsumer(props);
        Runtime.getRuntime().addShutdownHook(new  Thread(new  Runnable() {  //在jvm 宕机时才开始调用
			public void run() {
				if(consumer!=null){
					consumer.close();
				}
			}
		}));
		// 订阅主题.可以订阅多个主题
		consumer.subscribe(Arrays.asList("fkshTopic"));
		try {
			int minCommitSize = 10;// 最少处理10 条消息后才进行提交
			int icount = 0;// 消息计算器
			while (true) {
				// 等待拉取消息
				ConsumerRecords<String, String> records = consumer.poll(1000);
				for (ConsumerRecord<String, String> record : records) {
					// 简单打印出消息内容,模拟业务处理
					System.out.printf("partition = %d, offset = %d,key= %s value = %s%n", record.partition(),record.offset(), record.key(), record.value());
					icount++;
				}
				 // 在业务逻辑处理成功后提交偏移量
		        if (icount >= minCommitSize){
		            consumer.commitAsync(new OffsetCommitCallback() {
		                public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
		                    if (null == exception) {
		                    System.out.println("提交成功");
		                    } else {
		                        System.out.println("发生了异常");
		                    }
		                }
		            });
		            icount=0; // 重置计数器
		        }
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			consumer.close();
		}
	}
}
