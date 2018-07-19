package com.test.util;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
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

/**
 * 这个类是Hbase的util 类
 * @author Chris
 */
public class HbaseUtil {
	/**
	 * 创建表的方法
	 * 
	 * @param tableName
	 *            创建的表的名称
	 * @param family
	 *            需要创建的列族名称
	 * @throws Exception
	 *             创建时的异常
	 */
	public void createTbale(String tableName, String[] family, Configuration configuration) throws Exception {
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
			admin.createTable(hTableDescriptor.build());// 创建表创建分区
			// 关闭连接释放资源
			admin.close();
			connection.close();
			System.err.println("创建成功");
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
	public  void  addRowData(String tableName,Configuration conf,List<Put>puts) throws Exception{
		Connection  connection=ConnectionFactory.createConnection(conf);//创建一个连接
		Table table = connection.getTable(TableName.valueOf(tableName));
        table.put(puts);//批量加入
        table.close();
        connection.close();
	}
	/**
	 * 删除表的操作
	 * @param tableNamme 删除表的名称
	 * @param configuration 配置文件
	 */
	public boolean DeleteTable(String tableNamme,Configuration  configuration){
		try {
			//1.初始化操作
			Connection  connection=ConnectionFactory.createConnection(configuration);//创建一个连接
			Admin admin = connection.getAdmin();
			TableName  tableName=TableName.valueOf(tableNamme);
			//2.判断表是否存在
			if(!admin.tableExists(tableName)){
				System.err.println("表不存存在");
				return  false;
			}else{
				admin.disableTable(tableName);
				admin.deleteTable(tableName);
				return  true;
			}
		} catch (Exception e) {
			return  false;
		}
	}
	/**
	 * 创建表的方法
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
			admin.createTable(hTableDescriptor.build(),getSplitKeys());// 创建表创建分区
			// 关闭连接释放资源
			admin.close();
			connection.close();
			System.err.println("创建分区表成功");
		 } catch (Exception e) {
			 e.printStackTrace();
			 System.exit(1);
		}
	}
	//创建分区
    public byte[][] getSplitKeys() {
		String[] keys = new String[] {"4000|","8000|","12000|","16000|","20000|","24000|", "26000|", "28000|","32000|","36000|" };
		byte[][] splitKeys = new byte[keys.length][];
		TreeSet<byte[]> rows = new TreeSet<byte[]>(Bytes.BYTES_COMPARATOR);//升序排序
		for (int i = 0; i < keys.length; i++) {
			rows.add(Bytes.toBytes(keys[i]));
		}
		Iterator<byte[]> rowKeyIter = rows.iterator();
		int i=0;
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
     * @return
     */
    public static  byte[][] getSplitKeysByRandom() {  
        String[] keys = new String[] { "10|", "20|", "30|", "40|", "50|",  
                "60|", "70|", "80|", "90|" };  
        byte[][] splitKeys = new byte[keys.length][];  
        TreeSet<byte[]> rows = new TreeSet<byte[]>(Bytes.BYTES_COMPARATOR);//升序排序  
        for (int i = 0; i < keys.length; i++) {  
            rows.add(Bytes.toBytes(keys[i]));  
        }  
        Iterator<byte[]> rowKeyIter = rows.iterator();  
        int i=0;  
        while (rowKeyIter.hasNext()) {  
            byte[] tempRow = rowKeyIter.next();  
            rowKeyIter.remove();  
            splitKeys[i] = tempRow;  
            i++;  
        }  
        return splitKeys;  
    }
    /**
     * 获取两位随机数
     * @return
     */
    public static String getRandomNumber(){  
        String ranStr = Math.random()+"";  
        int pointIndex = ranStr.indexOf(".");  
        return ranStr.substring(pointIndex+1, pointIndex+3);  
    } 
    public static void main(String[] args) {
		System.err.println(UUID.randomUUID().toString().replaceAll("-",""));
	}
}
