package com.test.filetohabse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import com.test.util.HbaseUtil;

/**
 * 通过分区
 * 
 * @author Chris
 *
 */
public class FileToHbasePutSplit extends Thread {
	public Configuration conf;
	private String filePath = "";// 文件路径
	private static String tableName = "test";// 表的名称
	private String[] familyColum = {};// 列族名
	private Connection connection = null;
	private Table table = null;
	private int start;// 开始读的行数
	private int end;// 结束的行数
	HbaseUtil hbaseUtil = new HbaseUtil();
	static List fileList = new ArrayList();// 这个用来存储 所有的文件信息

	public FileToHbasePutSplit() {

	}

	public FileToHbasePutSplit(String filePath, String tableName, String[] cf, int start, int end) {
		this.filePath = filePath;
		this.familyColum = cf;
		this.conf = HBaseConfiguration.create();
		this.start = start;
		this.end = end;
	}

	public static void main(String[] args) {
		// 执行初始化
		setUp();
		System.setProperty("HADOOP_USER_NAME", "root");
		FileToHbasePutSplit fileToHbasePut = new FileToHbasePutSplit("", "test", new String[] { "info" }, 0, 40000);
		fileToHbasePut.start();
		FileToHbasePutSplit fileToHbasePut1 = new FileToHbasePutSplit("", "test", new String[] { "info" }, 40000,80000);
		fileToHbasePut1.start();
	}

	@Override
	public void run() {
		long beginTime = System.currentTimeMillis();
		try {
			connection = ConnectionFactory.createConnection(conf);// 创建一个连接
			table = connection.getTable(TableName.valueOf(tableName));
			List putslist = new ArrayList();
			String positionName = "";
			// 公司名称
			String companyName = "";
			// 薪资范围
			String salary = "";
			// 工作地点
			String workPlace = "";
			// rowkey
			String rowKey = "";
			System.err.println("第一条数据信息是：" + fileList.get(start));
			for (int j = start; j < end; j++) {
				String line = String.valueOf(fileList.get(j));
				if (line != null && !line.equals("")) {
					// 获取每行按照\t 分割的数组
					String[] infoArr = line.toString().split("\t");
					// String uuid = UUID.randomUUID().toString().replace("-",
					// "").toLowerCase();//生成UUID作为rowkey
					rowKey = "" + j;
					Put put = new Put(Bytes.toBytes(rowKey));
					// 职位名称
					positionName = infoArr[1];
					// 公司名称
					companyName = infoArr[2];
					// 薪资范围
					salary = infoArr[3];
					// 工作地点
					workPlace = infoArr[3];
					put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("positionName"), Bytes.toBytes(positionName));
					put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("companyName"), Bytes.toBytes(companyName));
					put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("salary"), Bytes.toBytes(salary));
					put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("positionName"), Bytes.toBytes(positionName));
					put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("workPlace"), Bytes.toBytes(workPlace));
					putslist.add(put);
					if ((j + 1) % 5000 == 0) {// 每5000 条提交一次
						table.put(putslist);
						putslist.clear();
					}
				}
			}
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
		System.err.println("表名:" + this.tableName + "用时：" + (System.currentTimeMillis() - beginTime) / 1000);
	}

	/**
	 * 
	 * @param filePath
	 *            文件的路径
	 */
	public static void setUp() {
		try {
			File file = new java.io.File("E://test1.txt");
			FileReader m = new FileReader(file);
			BufferedReader reader = new BufferedReader(m);// 读取文件
			while (true) {
				String line = reader.readLine();// i 相当于ID
				if (line != null && !line.equals("")) {
					fileList.add(line);
				} else {
					break;
				}
			}
		} catch (Exception e) {

		}
		System.err.println("-----listsSize----" + fileList.size());
	}
}
