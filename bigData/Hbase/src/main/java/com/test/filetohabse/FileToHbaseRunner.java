package com.test.filetohabse;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
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
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.test.hdfs2hbae.Hdfs2HbaseMapper;
import com.test.hdfs2hbae.Hdfs2HbaseReducer;
import com.test.hdfs2hbae.Hdfs2HbaseRunner;
import com.test.util.HbaseUtil;

public class FileToHbaseRunner implements Tool {
	Configuration configuration;
	//定义表名
    final static String TABLE_NAME="test";
    //定义列族名
    final static String[] COLUMN_FAMILY={"info"};
	public Configuration getConf() {
		return this.configuration;
	}
	/**
	 * 设置相关配置信息
	 */
	public void setConf(Configuration conf) {
		this.configuration = HBaseConfiguration.create(conf);
	}
	/**
	 * 设置job的相关信息
	 * @return 0 代表成功  1  代表失败
	 */
	public int run(String[] arg0) throws Exception {
		//开始组装job
		Configuration configuration = this.getConf();
		Job job = Job.getInstance(configuration);
		job.setJarByClass(FileToHbaseRunner.class);
		Path path=new Path("file:///E:/test.txt");//读取文件的路径
		FileInputFormat.addInputPath(job, path);
		//设置Mapper
		job.setMapperClass(FileToHbaseMapper.class);
		job.setMapOutputKeyClass(ImmutableBytesWritable.class);
		job.setMapOutputValueClass(Put.class);

		//设置reduce
		TableMapReduceUtil.initTableReducerJob(TABLE_NAME, FileToHbaseReduce.class, job);
		//设置reduce的数量
		job.setNumReduceTasks(1);
		boolean isSuccess = job.waitForCompletion(true);
		return isSuccess == true ? 0 : 1;
	}
	/**
	 * 这是测试的方法
	 * @param 
	 */
	public static void main(String[] args) {
		long beginTime=System.currentTimeMillis();//开始的运行时间
		Configuration conf = HBaseConfiguration.create();
		try {
			FileToHbaseRunner  fileToHbaseRunner=new  FileToHbaseRunner();
			//创建表  表名 jobInfo  列族名 info
			//hbaseUtil.createTbale(TABLE_NAME,COLUMN_FAMILY,conf);
			//执行Map reducer
			int status = ToolRunner.run(conf, new FileToHbaseRunner(), args);
			//如果是正常的执行程序则查出所有的数据
			/*if(status==0){
				fileToHbaseRunner.getAllData(TABLE_NAME);
			}*/
			System.err.println((System.currentTimeMillis()-beginTime)/1000);
			System.exit(status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
