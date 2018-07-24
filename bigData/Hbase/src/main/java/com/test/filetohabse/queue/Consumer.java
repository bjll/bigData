package com.test.filetohabse.queue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.PUT;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;

/**
 * 消费者端 用来存储Hbase 中
 * 
 * @author lilin
 */
public class Consumer extends Thread {
	// 配置相关
	private Configuration conf;
	private Connection connection = null;
	private Table table = null;
	private String tableName = "";
	// 取数据的队列
	private LinkedBlockingQueue<List<Put>> b;

	// 存放表的集合
	// List<Put> addList = new ArrayList<Put>();
	// 计数器
	int count = 0;
	// 是否退出线程
	boolean isRunning = true;
    //临时集合
	/**
	 * 
	 * @param b
	 *            需要传入的对列
	 * @param tableName
	 *            需要传入的表名
	 */
	public Consumer(LinkedBlockingQueue<List<Put>> b, String tableName) {
		this.b = b;
		this.conf = HBaseConfiguration.create();
		this.tableName = tableName;// 表的名称
	}
   
	public void run() {
		try {
			long beginTime = System.currentTimeMillis();
			//connection = ConnectionFactory.createConnection(conf);// 创建一个连接
			//table = connection.getTable(TableName.valueOf(tableName));
			while (isRunning) {
				List<Put> addList = b.poll(1,TimeUnit.SECONDS);
				if (addList != null) {
					// List<Put> tempList=new ArrayList();
					 //tempList=addList;
					//table.put(tempList); 
					sleep(1000);
					
					System.err.println("---"+addList.size());
					// tempList=null;
				} else {
					isRunning = false;
				}
			}
			//System.err.println("插入数据用时-------" + (System.currentTimeMillis() - beginTime) / 1000 + "秒");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (table != null) {
				try {
					table.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
