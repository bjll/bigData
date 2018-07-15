package com.test.filetohabse.queue;


import java.util.concurrent.LinkedBlockingQueue;

/**
 * 这是java 队列的测试(测试数据在test.txt)
 * 读取100000(行)数据 七列  20秒
 * @author Chris
 */
public class QueueTest {
	
   public static void main(String[] args) {
	   LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();//定义一个队列
	   new Producer(linkedBlockingQueue).start();//启动生产者
	   new Consumer(linkedBlockingQueue).start();//起订消费者
   }
}
