package com.test.filetohabse.queue;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * 消费者端 用来存储Hbase 中
 * 5000 提交一次是20  10000 提交一次是21秒
 * @author lilin
 */
public class Consumer extends Thread {
	//配置相关
	public Configuration conf;
	private Connection connection = null;
	private Table table = null;
	
	//取数据的队列
	private LinkedBlockingQueue<String> b;
	//批量提交时的list
	private List<Put> putslist = new ArrayList<Put>();
	
	//职位名称
	private String positionName = "";
	// 公司名称
	private String companyName = "";
	// 薪资范围
	private String salary = "";
	// 工作地点
	private String workPlace = "";
	//职位名称
	private String positionNameTwo = "";
	// 公司名称
	private String companyNameTwo = "";
	// 薪资范围
	private String salaryTwo = "";
	// 工作地点
	private String workPlaceTwo = "";
	//要插入的Rowkey
	private String rowKey = "";
	//计数器
	private int count = 1;
	//读取的每行数据
	private String line = "";
	//获取的uuid
	private String uuid="";
	//擦拆分后的数组
	private String[] infoArr ={};
	public Consumer(LinkedBlockingQueue<String> b) {
		init();// 调用初始化方法
		this.b = b;
	}
	public void run() {
		long beginTime = System.currentTimeMillis();
		try {
			while (true) {
				line = b.poll();//返回并移除
				if (count >100000) {
					break;
				}
				if (line != null && !line.equals("")) {
					// 获取每行按照\t 分割的数组
					String[] infoArr = line.toString().split("\t");
					uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();// 生成UUID作为rowkey
					rowKey = uuid;
					Put put = new Put(Bytes.toBytes(rowKey));
					// 职位名称
					positionName = infoArr[0];
					// 公司名称
					companyName = infoArr[1];
					// 薪资范围
					salary = infoArr[2];
					// 工作地点
					workPlace = infoArr[3];
					// 职位名称
					positionNameTwo = infoArr[4];
					// 公司名称
					companyNameTwo =infoArr[5];
					// 薪资范围
					salaryTwo = infoArr[6];
					// 工作地点
					workPlaceTwo= infoArr[7];
					put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("positionName"), Bytes.toBytes(positionName));
					put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("companyName"), Bytes.toBytes(companyName));
					put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("salary"), Bytes.toBytes(salary));
					put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("workPlace"), Bytes.toBytes(workPlace));
					put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("positionName1"), Bytes.toBytes(positionNameTwo));
					put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("companyName1"), Bytes.toBytes(companyNameTwo));
					put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("salary1"), Bytes.toBytes(salaryTwo));
					put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("workPlace1"), Bytes.toBytes(workPlaceTwo));
					putslist.add(put);
					if ((count) % 5000 == 0) {// 每5000 条提交一次
						table.put(putslist);
						putslist.clear();
					}
					count++;
				}
			}
			System.err.println("耗时" + (System.currentTimeMillis() - beginTime) / 1000 + "秒");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
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
	/**
	 * 初始化配置促创建连接和表
	 */
	public void init() {
		// 创建一个连接
		try {
			conf = HBaseConfiguration.create();
			connection = ConnectionFactory.createConnection(conf);
			table = connection.getTable(TableName.valueOf("test"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
