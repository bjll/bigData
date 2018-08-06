package com.ll.flumeclient;

import java.nio.charset.Charset;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;

/**
 * Flume 测试的客户端
 * 
 * @author lilin
 *
 */
public class FlumeRPCClient {
	/**
	 * 创建一个默认的flume rpc client
	 * 
	 * @param hostname
	 * @param port
	 * @return
	 */
	public static RpcClient createClient(String hostname, Integer port) {

		
		return RpcClientFactory.getDefaultInstance(hostname, port);

	}
	/**
	 * 关闭客户端
	 * 
	 * @param client
	 */
	public static void closeClient(RpcClient client) {
		client.close();
	}
	/**
	 * 发送 数据到flume 如果发生异常 关闭客户端
	 * 
	 * @param client flume的客户端
	 * @param data 发送的参数
	 */
	public static void sendData(RpcClient client, String data) {
		// Create a Flume Event object that encapsulates the sample data
		Event event = EventBuilder.withBody(data, Charset.forName("UTF-8"));
		// Send the event
		try {
			client.append(event);
		} catch (EventDeliveryException e) {
		}
	}

}
