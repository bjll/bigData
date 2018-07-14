package com.test.hbaseapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;
/**
 * 这里使用的Api 都是Hbase2.0的新版Api(老的API 可以参考文档)
 * @author lilin
 *对表的操作都是由Admin 进行操作的  对表中数据的操作都是由Table 对象进行操作的
 *有些表的关闭操作不要忘记
 */
public class TestHbase  {
	public static Configuration conf;

	public static void main(String[] args)  throws Exception{
          //System.err.println(isExits("test"));
		 createTbale("test",new String[]{"info"});
		 //addRowData("test","1","info","age","10");
		// getRow("test","1");
		 //getAllData("test");
		 //delMutiData("test","1");
	}
	//表名是否存在
	public  static boolean   isExits(String  tableName) throws IOException{
		conf=HBaseConfiguration.create();//设置配置文件
		Connection  connection=ConnectionFactory.createConnection(conf);//创建一个连接
		HBaseAdmin admin=(HBaseAdmin) connection.getAdmin();
		return admin.tableExists(TableName.valueOf(tableName));
	}
	/**
	 * 创建表的方法
	 * @param tableName 创建的表的名称
	 * @param family 需要创建的列族名称
	 * @throws Exception  创建时的异常
	 */
	public static  void  createTbale(String tableName,String[] family ) throws  Exception{
		conf=HBaseConfiguration.create();//设置配置文件
		Connection  connection=ConnectionFactory.createConnection(conf);//创建一个连接
		//HBaseAdmin admin=(HBaseAdmin) connection.getAdmin();//旧版本的API
		Admin admin = connection.getAdmin();
		TableName tname = TableName.valueOf(tableName);//转换
		if(admin.tableExists(tname)){
			admin.disableTable(tname);
			admin.deleteTable(tname);
		}
		//开始创建表.这里的表明需要进行序列化
		TableDescriptorBuilder hTableDescriptor=TableDescriptorBuilder.newBuilder(tname);//创建表
		
		ColumnFamilyDescriptor columnFamilyDescriptor=null;
		
		for (String cf: family) {
			columnFamilyDescriptor=ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(cf)).build();
			hTableDescriptor.setColumnFamily(columnFamilyDescriptor);
		}
		admin.createTable(hTableDescriptor.build());//创建表
		System.err.println("创建成功");
		System.exit(0);//0  带表正常退出  非零代表非正常退出
	}
	/**
	 * 表中插入数据
	 */
	public   static void  addRowData(String tableName,String rowKey,String columnFamily,String colum,String value) throws Exception{
	   	//创建Htabale  对象
		conf=HBaseConfiguration.create();//设置配置文件
		Connection  connection=ConnectionFactory.createConnection(conf);//创建一个连接
		Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(colum), Bytes.toBytes(value));
        table.put(put);
        table.close();
	}
	//删除行
	public   static void deleteRow(String row) throws IOException {
		conf=HBaseConfiguration.create();//设置配置文件
		Connection  connection=ConnectionFactory.createConnection(conf);//创建一个连接
        Table table = connection.getTable(TableName.valueOf("test"));
        Delete delete = new Delete(Bytes.toBytes(row));
        table.delete(delete);
        table.close();
    }
	//得到某一行的数据
	public  static void  getRow(String tableName,String rowKey) throws  Exception{
		conf=HBaseConfiguration.create();//设置配置文件
		Connection  connection=ConnectionFactory.createConnection(conf);//创建一个连接
        Table table = connection.getTable(TableName.valueOf("test"));//获取一个表
        Get  get=new  Get(Bytes.toBytes(rowKey));//得到Get  对象
       //get.setMaxVersions();//显示所有的版本
        get.readAllVersions();//新版本的写法
       // get.addColumn(family, qualifier);//这是获取某一行指定列族和列的数据的方法family代表列族 qualifier 哪一个列
        Result  result=table.get(get);//获取到读取结果
        System.err.println(result);//keyvalues={1/info:age/1529996729278/Put/vlen=2/seqid=0}
        for (Cell  cell:result.rawCells()) {
			System.err.println(Bytes.toString(result.getRow())+"--行键");
			System.err.println("列族----"+Bytes.toString(CellUtil.cloneFamily(cell)));
			System.err.println("列----"+Bytes.toString(CellUtil.cloneQualifier(cell)));
			System.err.println("值----"+Bytes.toString((CellUtil.cloneValue(cell))));
			System.err.println("时间戳----"+cell.getTimestamp());
		}
        table.close();
	}
	//获取所有的数据
	public static   void  getAllData(String  tableName) throws  Exception {
		conf=HBaseConfiguration.create();//设置配置文件
		Connection  connection=ConnectionFactory.createConnection(conf);//创建一个连接
        Table table = connection.getTable(TableName.valueOf("test"));//获取一个表
        //得到用于扫描 region的对象
        Scan  scan=new Scan();
        ResultScanner  resultScanner=table.getScanner(scan);
        for (Result  result:resultScanner) {
			Cell[] cells=result.rawCells();
			for (Cell cell : cells) {
				System.err.println(Bytes.toString(result.getRow())+"--行键");
				System.err.println("列族----"+Bytes.toString(CellUtil.cloneFamily(cell)));
				System.err.println("列----"+Bytes.toString(CellUtil.cloneQualifier(cell)));
				System.err.println("值----"+Bytes.toString((CellUtil.cloneValue(cell))));
				System.err.println("时间戳----"+cell.getTimestamp());
			}
		}
        table.close();
	}
	//批量删除操作
	public static  void  delMutiData(String tableName,String...rows) throws Exception{
		conf=HBaseConfiguration.create();//设置配置文件
		Connection  connection=ConnectionFactory.createConnection(conf);//创建一个连接
        Table table = connection.getTable(TableName.valueOf(tableName));//获取一个表
        List<Delete> delList=new  ArrayList<Delete>();
        for (String row: rows) {
        	Delete  delete=new  Delete(Bytes.toBytes(row));
        	delList.add(delete);
		}
        table.delete(delList);//执行批量删除
        table.close();//关闭
	}
}
