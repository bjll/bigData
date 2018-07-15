package cn.test.kafka.stream;

import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.apache.kafka.streams.processor.TopologyBuilder;

import kafka.consumer.KafkaStream;



public class TestStream {
   public static void main(String[] args) {
	   //数据的来源主题
	   String fromTopic="test1";//存放有垃圾数据的主题
	   //处理后的数据去向
	   String toTopic="test2";//过滤垃圾后的主题
	   //设置参数
	   Properties  properties=new  Properties();
	   //指定唯一的ID
	   properties.put(StreamsConfig.APPLICATION_ID_CONFIG,"logProcess");//名称自定义
	   //集群的节点名称
	   properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "");
	   //实例化StreamConfig
	   StreamsConfig  streamsConfig=new  StreamsConfig(properties);
	   //构建拓扑
	   TopologyBuilder  topologyBuilder=new  TopologyBuilder();
	   //数据在哪里来
	   topologyBuilder
	           .addSource("SOURCE", fromTopic)  //SOURCE 所在位置名字自定义
	           .addProcessor("PROCESSOR", new  ProcessorSupplier<byte[], byte[]>() {
				@Override
				public Processor<byte[], byte[]> get() {
					return new LogProcessor();//根据自己定义的流式处理的规则进行处理 也就是 LogProcessor 类的规则
				}
			}, "SOURCE")  //取得数据的来源也就是从 test1 中进行去数据
	           //将处理的结果放入另一个主题
	         .addSink("SINK", toTopic,"PROCESSOR" ); //SINK  所在位置名字自定义
	   //根据 StreamConfig 对象和构建拓扑的Bulider对象实例化KafkaStream 
	    KafkaStreams  kafkaStreams=new  KafkaStreams(topologyBuilder, properties);
}
}
