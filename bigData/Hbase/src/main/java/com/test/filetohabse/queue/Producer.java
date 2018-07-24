package com.test.filetohabse.queue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import com.test.util.HbaseUtil;

/**
 * 生产者的类 用于从文件中读取数据
 * 
 * @author lilin
 *
 */
public class Producer extends Thread {
	// 生产队列
	private LinkedBlockingQueue<List<Put>> b;
	// 文件读取
	private FileReader m;
    //文件缓存
	private BufferedReader reader;
	// 计数器
	int count = 0;
	// 发送 list
	//List<Job> list = new ArrayList<Job>();
	Put put;
	// 职位名称
	private String positionName = "";
	// 公司名称
	private String companyName = "";
	// 薪资范围
	private String salary = "";
	// 工作地点
	private String workPlace = "";
	//RowKey
	String rowKey="";
	List<Put> listPuts=new ArrayList<Put>();
	
	List<Put> tempList=new ArrayList<Put>();
	public Producer(LinkedBlockingQueue<List<Put>> b) {
		init();
		this.b = b;
	}
	/**
	 * 生产数据
	 */
	@Override
	public void run() {
		try {
			String line = "";// 读取一行信息
			while (true) {
				line = reader.readLine();
				if (line == null || "".equals(line)) {
					tempList.clear();
					tempList.addAll(listPuts);
					b.put(tempList);
					listPuts.clear();
					break;
				}
				String[] infoArr = line.toString().split("\t");
				rowKey=HbaseUtil.getRandomNumber()+UUID.randomUUID().toString().replaceAll("-", "");
				put = new Put(Bytes.toBytes(rowKey));
				// 职位名称
				positionName = infoArr[0];
				// 公司名称
				companyName = infoArr[1];
				// 薪资范围
				salary = infoArr[2];
				// 工作地点
				workPlace = infoArr[3];
				
				put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("positionName"), Bytes.toBytes(positionName));
				put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("companyName"), Bytes.toBytes(companyName));
				put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("salary"), Bytes.toBytes(salary));
				put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("workPlace"), Bytes.toBytes(workPlace));
				
				put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("positionName"), Bytes.toBytes(positionName));
				put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("companyName"), Bytes.toBytes(companyName));
				put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("salary"), Bytes.toBytes(salary));
				put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("workPlace"), Bytes.toBytes(workPlace));
				
				put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("positionName"), Bytes.toBytes(positionName));
				put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("companyName"), Bytes.toBytes(companyName));
				put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("salary"), Bytes.toBytes(salary));
				put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("workPlace"), Bytes.toBytes(workPlace));
				
				put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("positionName"), Bytes.toBytes(positionName));
				put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("companyName"), Bytes.toBytes(companyName));
				put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("salary"), Bytes.toBytes(salary));
				put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("workPlace"), Bytes.toBytes(workPlace));
				//put.setDurability(Durability.ASYNC_WAL);//异步进行写入
				listPuts.add(put);
				count ++;
				if(count%5000==0){
					tempList.clear();
					tempList.addAll(listPuts);
					b.put(tempList);
					listPuts.clear();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (m != null) {
				try {
					m.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 从文件中读取数据的方法
	 */
	public void init() {
		try {
			File file = new File("E:\\test.txt");
			m = new FileReader(file);
			reader = new BufferedReader(m);// 读取文件
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String test = "java/(AI)人工智能/UI设计师签约实习生  	中青才智教育投资(北京)有限公司	7000-14000	北京	java/(AI)人工智能/UI设计师签约实习生  	中青才智教育投资(北京)有限公司	7000-14000	北京	java/(AI)人工智能/UI设计师签约实习生  	中青才智教育投资(北京)有限公司	7000-14000	北京	java/(AI)人工智能/UI设计师签约实习生  	中青才智教育投资(北京)有限公司	7000-14000	北京";
		System.err.println(test.split("\t").length);
	}
}
