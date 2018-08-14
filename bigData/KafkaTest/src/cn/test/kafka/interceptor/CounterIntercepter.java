package cn.test.kafka.interceptor;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class CounterIntercepter  implements ProducerInterceptor<String, String>{
    private  long successCount=0;
    private  long errorCount=0;
    //一些配置信息
	@Override
	public void configure(Map<String, ?> map) {
	}
    //拦截器结束时调用
	@Override
	public void close() {
		System.err.println("成功的个数-----"+successCount);
		System.err.println("失败的个数-----"+errorCount);
	}
    //kafka 集群ack 完成后的结果 metadata  保存时元数据的信息   若是执行成功则Exception  会返回null 值 
	//在执行过程中出现错误  exception  就不会是空值
	@Override
	public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
		 if(exception==null){
			 if(metadata!=null&&"test".equals(metadata.topic())&&metadata.partition()==0){
				 //在主题名称为test 并且分区为 1 的数据进行处理
				 System.err.println("doSomeThing");
			 }
			 metadata.offset();//这里可以通过API 去一些数据
			successCount++; 
		 }else{
			 errorCount++;
		 }
		
	}
   /**
    *这个方法是进行消息过滤的操作
    */
	@Override
	public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
		//对数据不做处理  返回原有的数据
		return  record;
	}

}
