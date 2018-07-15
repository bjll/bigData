package cn.test.kafka.interceptor;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/*
 * kafaka的时间戳拦截器
 */
public class TimeInterceptor  implements ProducerInterceptor<String, String>  {

	@Override
	public void configure(Map<String, ?> arg0) {
		
		
	}
	//关闭interceptor，主要用于执行一些资源清理工作
	@Override
	public void close() {
		
		
	}

	@Override
	public void onAcknowledgement(RecordMetadata arg0, Exception arg1) {
		
		
	}
    /**
     * 生产者发送消息  在这里做一次拦截 现在还没有到 集群
     */
	@Override
	public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
		return new ProducerRecord(
				record.topic(), 
				record.partition(), 
				record.timestamp(), record.key(),
				System.currentTimeMillis() + "," + record.value().toString());
	}

}
