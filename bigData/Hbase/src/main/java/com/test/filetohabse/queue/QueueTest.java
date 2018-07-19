package com.test.filetohabse.queue;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.hadoop.hbase.client.Put;



/**
 * 这是java 队列的测试(测试数据在test.txt) 读取100000(行)数据 七列 20秒（不分区。 分区是9-10秒）   （ 2个线程读）
 * 20万七列      分区 region 20秒    不分区 是30秒到31 秒     
 * 40万 7列 分区 (5000提交一次) 45 秒   10000提交一次 41 秒（2个线程读）   20000一次 38秒  50000 一次 是45 秒
 * 
 * 在生产端  进行数据分割  107790条数据 16列   19 秒（本地测试）
 * @author Chris
 */
public class QueueTest {

	public static void main(String[] args) {
	   LinkedBlockingQueue<Put>  linkedBlockingQueue = new LinkedBlockingQueue<Put>();//定义一个队列
	   new Producer(linkedBlockingQueue).start();//启动生产者
	   new Consumer(linkedBlockingQueue,"test").start();//起动消费者
   }
}
