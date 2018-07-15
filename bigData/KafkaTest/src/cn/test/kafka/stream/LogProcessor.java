package cn.test.kafka.stream;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;

public final class LogProcessor  implements Processor<byte[], byte[]> {
	private  ProcessorContext   context;
    //关闭的时候调用 
	@Override
	public void close() {
		
	}
   //初始化的方法
	@Override
	public void init(ProcessorContext context) {
		//执行初始化方法是获取上下文
		System.err.println("----执行初始化的方法----");
		this.context=context;
		
	}
    //每一条数据都会调用(实例是过滤掉 >>>> 的垃圾数据  例如 aaa?>>>>sss  过滤后就是取sss)
	//取到的是发送的原始数据
	@Override
	public void process(byte[] key, byte[] value) {
		String  test=new String(value);
		if(test.contains(">>>>")){
			test=test.split(">>>>")[1];
		}
		//写出数据写入到上下文中
		context.forward(key, test.getBytes());
	}
    // 周期性的调用
	@Override
	public void punctuate(long arg0) {
		
		
	}



}
