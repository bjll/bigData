package com.test.hdfs2hbae;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class Hdfs2HbaseMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {
	public Hdfs2HbaseMapper(){
		System.err.println("--我是构造方法------Hdfs2HbaseMapper");
	}
	@Override
	protected void map(LongWritable key, Text value, Context context)throws IOException, InterruptedException {
		 String [] strArr=value.toString().split("\t");
		 String rowKey=strArr[0];
		 String name=strArr[1];
		 String color=strArr[2];
		 //初始化RowKye这个key 是要写出的
		 ImmutableBytesWritable immutableBytesWritable=new ImmutableBytesWritable(Bytes.toBytes(rowKey));
		 Put  put=new Put(Bytes.toBytes(rowKey));
		 //列族名。列名，列值
		 put.addColumn(Bytes.toBytes("info"),Bytes.toBytes("name") ,Bytes.toBytes(name));
		 put.addColumn(Bytes.toBytes("info"),Bytes.toBytes("color") ,Bytes.toBytes(color));
		 context.write(immutableBytesWritable, put);//写出去
	}
}
