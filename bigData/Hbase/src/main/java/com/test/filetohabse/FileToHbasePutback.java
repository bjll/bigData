package com.test.filetohabse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import com.test.util.HbaseUtil;



/**
 * 通过读取文件直接放入Hbase 中
 * @author Chris
 *
 */
public class FileToHbasePutback extends  Thread {
	public static Configuration conf;
 
	public static void main(String[] args) {
	/*	long beginTime=System.currentTimeMillis();
		HbaseUtil  hbaseUtil=new  HbaseUtil();
		conf=HBaseConfiguration.create();
		try {
			//hbaseUtil.createTbale("test", new String[]{"info"}, conf);
			//读取文件
			File file=new java.io.File("E://test1.txt");
			//FileInputStream  fileInputStream=new  FileInputStream(file);
			FileReader m=new FileReader(file);
			BufferedReader reader=new BufferedReader(m);//读取文件
			int i=0;
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
			while (true) {
				i++;
				String line=reader.readLine();
				if(line!=null && !line.equals("")){
					 //获取每行按照\t 分割的数组
					 String [] infoArr=line.toString().split("\t");
					 String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();//生成UUID作为rowkey
				     rowKey=uuid;
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
				}else{
					break;//停止循环
				}
			}
			hbaseUtil.addRowData("test", conf, putslist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println((System.currentTimeMillis()-beginTime)/1000);*/
	}
	@Override
	public void run() {
		long beginTime=System.currentTimeMillis();
		HbaseUtil  hbaseUtil=new  HbaseUtil();
		conf=HBaseConfiguration.create();
		try {
			//hbaseUtil.createTbale("test", new String[]{"info"}, conf);
			//读取文件
			File file=new java.io.File("E://test1.txt");
			//FileInputStream  fileInputStream=new  FileInputStream(file);
			FileReader m=new FileReader(file);
			BufferedReader reader=new BufferedReader(m);//读取文件
			int i=0;
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
			while (true) {
				i++;
				String line=reader.readLine();
				if(line!=null && !line.equals("")){
					 //获取每行按照\t 分割的数组
					 String [] infoArr=line.toString().split("\t");
					 String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();//生成UUID作为rowkey
				     rowKey=uuid;
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
				}else{
					break;//停止循环
				}
			}
			hbaseUtil.addRowData("test", conf, putslist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println((System.currentTimeMillis()-beginTime)/1000);
	}
}
