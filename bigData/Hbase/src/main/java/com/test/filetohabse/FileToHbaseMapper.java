package com.test.filetohabse;

import java.io.IOException;
import java.util.UUID;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class FileToHbaseMapper  extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put>{
	/**
	 *读取文件内容过的map方法
	 *每行的数据按照Tab键分割(\t)
	 * 职位名称    公司名称   薪资   地点
	 * 中高级.net工程师  	北京恒赢智航科技有限公司	13000-17000	上海  (行数据示例)
	 */
	@Override
	protected void map(LongWritable key, Text value, Context context)throws IOException, InterruptedException {
		 //获取每行按照\t 分割的数组
		 String [] infoArr=value.toString().split("\t");
		 String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();//生成UUID作为rowkey
		 String rowKey=uuid;
		 //职位名称
		 String positionName=infoArr[0];
		 //公司名称
		 String companyName=infoArr[1];
		 //薪资范围
		 String salary=infoArr[2];
		 //工作地点
		 String workPlace=infoArr[3];
		 //初始化RowKye这个key 是要写出的
		 ImmutableBytesWritable immutableBytesWritable=new ImmutableBytesWritable(Bytes.toBytes(rowKey));
		 Put  put=new Put(Bytes.toBytes(rowKey));
		 //列族名 列名，列值
		 put.addColumn(Bytes.toBytes("info"),Bytes.toBytes("positionName") ,Bytes.toBytes(positionName));
		 put.addColumn(Bytes.toBytes("info"),Bytes.toBytes("companyName") ,Bytes.toBytes(companyName));
		 put.addColumn(Bytes.toBytes("info"),Bytes.toBytes("salary") ,Bytes.toBytes(salary));
		 put.addColumn(Bytes.toBytes("info"),Bytes.toBytes("workPlace") ,Bytes.toBytes(workPlace));
		 //将内容写出去
		 context.write(immutableBytesWritable, put);
	}
}
