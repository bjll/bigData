package cn.test.kafka.producer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
public class CustomProducer {
	public static void main(String[] args) {
		//1-----------配置文件---------------------------
		Properties props = new Properties();
		// Kafka服务端的主机名和端口号 可以是多个
		props.put("bootstrap.servers", "192.168.148.129:9092");
		// 等待所有副本节点的应答ack是判别请求是否为完整的条件（就是是判断是不是成功发送了）。我们指定了“all”将会阻塞消息，这种设置性能最低，但是是最可靠的
		props.put("acks", "all");
		// 消息发送最大尝试次数
		props.put("retries", 0);
		// 一批消息处理大小 与linger.ms 进行搭配  大小16 KB
		props.put("batch.size", 16384);
		// 请求延时  0:代表没有延迟  1:代表消息在1 毫秒之后发出去
		props.put("linger.ms", 1);
		// 发送缓存区内存大小  32M
		props.put("buffer.memory", 33554432);
		//数据发送之前一定要进行序列化----------------------
		// key序列化
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		// value序列化
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		//设置分区
		props.put("partitioner.class", "cn.test.kafka.patitioner.CustomPartitioner");
		//2------------------实例化 KafkaProducer----------------
		KafkaProducer<String, String> producer = new KafkaProducer<>(props);
		//增加拦截器：  拦截器的次序是按照list 的先后顺序进行执行的
		List <String> list=new ArrayList<String>();
		list.add("cn.test.kafka.interceptor.TimeInterceptor");
		list.add("cn.test.kafka.interceptor.CounterIntercepter");
		props.put(org.apache.kafka.clients.producer.ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, list);
		//3.---------------发送消息 ---------------------------
		for (int i = 0; i < 50; i++) {
			//ProducerRecord:参数：topic【消息主题】 key【消息的key值，通常用于消息的分区】 value【消息体】   
			//发送方式为异步发送，添加消息到缓冲区等待发送，并立即返回。
			//生产者将单个的消息批量在一起发送来提高效率。 
			//由于send调用是异步的，它将为分配消息的此消息的RecordMetadata返回一个Future。
			//如果future调用get()，则将阻塞，直到相关请求完成并返回该消息的metadata，或抛出发送异常
			producer.send(new ProducerRecord<String, String>("first", Integer.toString(i), "hello world-" + i));
		}

		producer.close();
	}
}
