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
 * 通过读取文件直接放入Hbase 中
 * @author lilin
 *
 */
public class FileToHbasePut extends  Thread {
	public static Configuration conf;
    private String filePath="";//文件路径
    private String  tableName="";//表的名称
    private String[] familyColum={};//列族名
    private Connection  connection=null;
    private Table table =null;
    HbaseUtil  hbaseUtil=new  HbaseUtil();
    public  FileToHbasePut(){
	   
    }
    public  FileToHbasePut(String filePath,String  tableName,String[]cf){
 	   this.filePath=filePath;
 	   this.tableName=tableName;
 	   this.familyColum=cf;
 	   conf=HBaseConfiguration.create();
    }
    //如果创建表 每个文件4 万数据 则用时 8 到  10 秒
    //不创建表则用时是 5 秒左右
    //8万是15  16  秒
	public static void main(String[] args) {
		FileToHbasePut fileToHbasePut=new FileToHbasePut("E://test1.txt", "test",new String[]{"info"});
		fileToHbasePut.start();
		FileToHbasePut fileToHbasePutTow=new FileToHbasePut("E://test1.txt", "test2",new String[]{"info"});
		fileToHbasePutTow.start();
	}
	@Override
	public void run() {
		long beginTime=System.currentTimeMillis();
		try {
			connection=ConnectionFactory.createConnection(conf);//创建一个连接
		    table = connection.getTable(TableName.valueOf(tableName));
			//hbaseUtil.createTbale(this.tableName, this.familyColum, this.conf);
			//读取文件
			File file=new java.io.File(this.filePath);
			//FileInputStream  fileInputStream=new  FileInputStream(file);
			FileReader m=new FileReader(file);
			BufferedReader reader=new BufferedReader(m);//读取文件
			//存放put 的list
			List  putslist=new ArrayList();
			String positionName="";
			 //公司名称
			String companyName="";
			 //薪资范围
			String salary="";
			 //工作地点
			String workPlace="";
			 //rowkey
			String rowKey="";
			int  i=1;
			while (true) {
				String line=reader.readLine();
				if(line!=null && !line.equals("")){
					 //获取每行按照\t 分割的数组
					 String [] infoArr=line.toString().split("\t");
					// String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();//生成UUID作为rowkey
				     rowKey=""+i;
				     Put  put=new  Put(Bytes.toBytes(rowKey));
					 //职位名称
					 positionName=infoArr[0];
					 //公司名称
					 companyName=infoArr[1];
					 //薪资范围
					 salary=infoArr[2];
					 //工作地点
					 workPlace=infoArr[3];
					 put.addColumn(Bytes.toBytes("info"),Bytes.toBytes("positionName") ,Bytes.toBytes(positionName));
					 put.addColumn(Bytes.toBytes("info"),Bytes.toBytes("companyName") ,Bytes.toBytes(companyName));
					 put.addColumn(Bytes.toBytes("info"),Bytes.toBytes("salary") ,Bytes.toBytes(salary));
					 put.addColumn(Bytes.toBytes("info"),Bytes.toBytes("positionName") ,Bytes.toBytes(positionName));
					 put.addColumn(Bytes.toBytes("info"),Bytes.toBytes("workPlace") ,Bytes.toBytes(workPlace));
					 putslist.add(put);
				     if(i%5000==0){//每5000 条提交一次
				    	  table.put(putslist);
				    	  putslist.clear();
				     }
				}else{
					break;//停止循环
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(table!=null){
				 try {
					table.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(connection!=null){
				 try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.err.println("表名:"+this.tableName+"用时："+(System.currentTimeMillis()-beginTime)/1000);
	}
}
