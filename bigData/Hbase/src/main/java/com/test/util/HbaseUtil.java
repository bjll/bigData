package com.test.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;

import com.sun.tools.classfile.StackMap_attribute.stack_map_frame;

/**
 * 这个类是Hbase的util 类
 * 
 * @author Chris
 */
public class HbaseUtil {
	/**
	 * 创建表的方法(不是预分区的操作)
	 * 
	 * @param tableName
	 *            创建的表的名称
	 * @param family
	 *            需要创建的列族名称
	 * @throws Exception
	 *             创建时的异常
	 */
	public void createTbale(String tableName, String[] family, Configuration configuration) throws Exception {
		Connection connection = null;
		Admin admin = null;
		try {
			if (configuration == null) {
				configuration = HBaseConfiguration.create();// 设置配置文件
			}
			connection = ConnectionFactory.createConnection(configuration);// 创建一个连接
			admin = connection.getAdmin();
			TableName tname = TableName.valueOf(tableName);// 转换
			// 如果表名存在返回
			if (admin.tableExists(tname)) {
				return;
			}
			// 开始创建表.这里的表名需要进行序列化
			TableDescriptorBuilder hTableDescriptor = TableDescriptorBuilder.newBuilder(tname);// 创建表

			ColumnFamilyDescriptor columnFamilyDescriptor = null;

			for (String cf : family) {
				columnFamilyDescriptor = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(cf)).build();
				hTableDescriptor.setColumnFamily(columnFamilyDescriptor);
			}
			admin.createTable(hTableDescriptor.build());// 创建表创建分区
			System.err.println("创建成功");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			// 关闭连接释放资源
			if (connection != null) {
				connection.close();
			}
			if (admin != null) {
				admin.close();
			}
		}
	}
	/**
	 * 创建表的方法（预分区）
	 * 
	 * @param tableName
	 *            创建ragion分区表
	 * @param family
	 *            需要创建的列族名称
	 * @throws Exception
	 *             创建时的异常
	 */
	public void createTbaleBySplit(String tableName, String[] family, Configuration configuration) throws Exception {
		try {
			configuration = HBaseConfiguration.create();// 设置配置文件
			Connection connection = ConnectionFactory.createConnection(configuration);// 创建一个连接
			// HBaseAdmin admin=(HBaseAdmin) connection.getAdmin();//旧版本的API
			Admin admin = connection.getAdmin();
			TableName tname = TableName.valueOf(tableName);// 转换
			// 如果表名存在就删除
			if (admin.tableExists(tname)) {
				admin.disableTable(tname);
				admin.deleteTable(tname);
			}
			// 开始创建表.这里的表名需要进行序列化
			TableDescriptorBuilder hTableDescriptor = TableDescriptorBuilder.newBuilder(tname);// 创建表

			ColumnFamilyDescriptor columnFamilyDescriptor = null;

			for (String cf : family) {
				columnFamilyDescriptor = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(cf)).build();
				hTableDescriptor.setColumnFamily(columnFamilyDescriptor);
			}
			System.err.println(getSplitKeys());
			admin.createTable(hTableDescriptor.build(), getSplitKeys());// 创建表创建分区
			// 关闭连接释放资源
			admin.close();
			connection.close();
			System.err.println("创建分区表成功");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	/**
	 * 根据表的名称获取所有数据
	 * 
	 * @param tableName
	 *            表名称
	 * @throws Exception
	 *             异常
	 */
	public void getAllData(String tableName, Configuration configuration) throws Exception {
		configuration = HBaseConfiguration.create();// 设置配置文件
		Connection connection = ConnectionFactory.createConnection(configuration);// 创建一个连接
		Table table = connection.getTable(TableName.valueOf(tableName));// 获取一个表
		// 得到用于扫描 region的对象
		Scan scan = new Scan();
		ResultScanner resultScanner = table.getScanner(scan);
		for (Result result : resultScanner) {
			Cell[] cells = result.rawCells();
			for (Cell cell : cells) {
				System.err.println(Bytes.toString(result.getRow()) + "--行键");
				System.err.println("列族----" + Bytes.toString(CellUtil.cloneFamily(cell)));
				System.err.println("列----" + Bytes.toString(CellUtil.cloneQualifier(cell)));
				System.err.println("值----" + Bytes.toString((CellUtil.cloneValue(cell))));
				System.err.println("时间戳----" + cell.getTimestamp());
			}
		}
		resultScanner.close();
		table.close();
		connection.close();
	}

	/**
	 * 表中插入数据
	 */
	public void addRowData(String tableName, Configuration conf, List<Put> puts) throws Exception {
		Connection connection = ConnectionFactory.createConnection(conf);// 创建一个连接
		Table table = connection.getTable(TableName.valueOf(tableName));
		table.put(puts);// 批量加入
		table.close();
		connection.close();
	}

	/**
	 * 删除表的操作
	 * 
	 * @param tableNamme
	 *            删除表的名称
	 * @param configuration
	 *            配置文件
	 */
	public boolean DeleteTable(String tableNamme, Configuration configuration) {
		try {
			// 1.初始化操作
			Connection connection = ConnectionFactory.createConnection(configuration);// 创建一个连接
			Admin admin = connection.getAdmin();
			TableName tableName = TableName.valueOf(tableNamme);
			// 2.判断表是否存在
			if (!admin.tableExists(tableName)) {
				System.err.println("表不存存在");
				return false;
			} else {
				admin.disableTable(tableName);
				admin.deleteTable(tableName);
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}
	// 创建分区 分去键必须是有序的
	public byte[][] getSplitKeys() {
		String[] keys = new String[] { "4000|", "8000|", "12000|", "16000|", "20000|", "24000|", "26000|", "28000|",
				"32000|", "36000|" };
		byte[][] splitKeys = new byte[keys.length][];
		TreeSet<byte[]> rows = new TreeSet<byte[]>(Bytes.BYTES_COMPARATOR);// 升序排序
		for (int i = 0; i < keys.length; i++) {
			rows.add(Bytes.toBytes(keys[i]));
		}
		Iterator<byte[]> rowKeyIter = rows.iterator();
		int i = 0;
		while (rowKeyIter.hasNext()) {
			byte[] tempRow = rowKeyIter.next();
			rowKeyIter.remove();
			splitKeys[i] = tempRow;
			i++;
		}
		return splitKeys;
	}

	/**
	 * 这里RowKey的命名规则是两位随机数加+UUID
	 * 
	 * @return
	 */
	public static byte[][] getSplitKeysByRandom() {
		String[] keys = new String[] { "10|", "20|", "30|", "40|", "50|", "60|", "70|", "80|", "90|" };
		byte[][] splitKeys = new byte[keys.length][];
		//生成分区键数组的时候一定要保证是有序的 可以用TreeSet  也可以自定义排序规则
		TreeSet<byte[]> rows = new TreeSet<byte[]>(Bytes.BYTES_COMPARATOR);// 升序排序
		for (int i = 0; i < keys.length; i++) {
			rows.add(Bytes.toBytes(keys[i]));
		}
		//这里的rows  已经是有序的了
		Iterator<byte[]> rowKeyIter = rows.iterator();
		int i = 0;
		while (rowKeyIter.hasNext()) {
			byte[] tempRow = rowKeyIter.next();
			rowKeyIter.remove();
			splitKeys[i] = tempRow;
			i++;
		}
		return splitKeys;
	}
	/**
	 *创建分区
	 * @param regions 要分区的个数
	 * @return
	 */
    public  static byte[][] genSplitKeys(int  regions){
    	//存放分区键的数组 70M 是100万条数据
    	//如果一个region 维护 1 个G的数据  则可根据  数据的条数来估算 region 的个数
    	//假设有10000万亿 条数据 则计算region 个数的如下：
    	//10000万亿/100万*70/1024M  就是 regions的值
    	String[] keys=new String[regions];
    	//生产分区号的逻辑
		return null;
    	
    }
	/**
	 * 获取两位随机数
	 * 
	 * @return
	 */
	public static String getRandomNumber() {
		String ranStr = Math.random() + "";
		int pointIndex = ranStr.indexOf(".");
		return ranStr.substring(pointIndex + 1, pointIndex + 3);
	}

	public static void main(String[] args) {
		System.err.println(UUID.randomUUID().toString().replaceAll("-", ""));
	}

	/**
	 * 判断表是否存在的方法
	 * 
	 * @param tableName
	 *            传入的表名
	 * @param configuration
	 *            配置文件的设置
	 * @throws IOException
	 *             在判断的时候抛出异常 在业务逻辑层进行相关的处理
	 */
	public static boolean isExistTbale(String tableName, Configuration configuration) throws IOException {
		Connection connection = ConnectionFactory.createConnection(configuration);// 创建一个连接
		// HBaseAdmin admin=(HBaseAdmin) connection.getAdmin();//旧版本的API
		Admin admin = connection.getAdmin();
		boolean result = admin.tableExists(TableName.valueOf(tableName));
		// 执行关闭的操作 释放资源
		admin.close();
		connection.close();
		return result;
	}

	/**
	 * 初始化命名空间
	 * 
	 * @param namaSpaceName
	 *            命名空间名称
	 * @param conf
	 *            传入的配置文件
	 * @throws Exception
	 */
	public static void initNameSpace(String namaSpaceName, Configuration conf) throws Exception {
		Connection connection = null;
		Admin admin = null;
		try {
			//如果没有就采用默认的
			if (conf == null) {
				conf = HBaseConfiguration.create();
			}
			connection = ConnectionFactory.createConnection(conf);
			admin = connection.getAdmin();
			NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(namaSpaceName)
					.addConfiguration("CREAT_TIME", String.valueOf(System.currentTimeMillis()))
					.addConfiguration("AUTHOR", "LL").build();
			admin.createNamespace(namespaceDescriptor);//创建命名空间
		} finally {
			if (admin != null) {
				admin.close();
			}
			if (connection != null) {
				connection.close();
			}
		}

	}
	/**
	 * 生成rowKey 的方法
	 * @param  参数要根据自己的实际业务需求进行 添加 拼接生成RowKey
	 * @param reginCode 分区码
	 * @return
	 */
	public  static String  genRowKey(String reginCode,String ...strings){
		return null;
		
	}
}
