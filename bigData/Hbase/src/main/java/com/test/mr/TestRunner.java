package com.test.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
//https://edu.aliyun.com/a/100463  一个mr 写入两张表
public class TestRunner  implements Tool{
  Configuration  configuration;
	public Configuration getConf() {
		return  this.configuration;
	}

	public void setConf(Configuration conf) {
		this.configuration=HBaseConfiguration.create(conf);
	}

	public int run(String[] arg0) throws Exception {
		//开始组装job
		Configuration  configuration=this.getConf();
		Job job=Job.getInstance(configuration);
		job.setJarByClass(TestRunner.class);
		//配置job
		Scan  scn=new  Scan();
		scn.setCacheBlocks(false);
		scn.setCaching(500);
		//设置mapper
		TableMapReduceUtil.initTableMapperJob("test", scn, hbase2hbaseMapper.class, ImmutableBytesWritable.class, Put.class, job);
		//这是reduce
		TableMapReduceUtil.initTableReducerJob("test2", Hbase2HbaseReducer.class, job);
		//设置reduce的数量
		job.setNumReduceTasks(1);
		boolean isSuccess=job.waitForCompletion(true);
		return isSuccess==true?0:1;
	}
	//执行的方法
	public static void main(String[] args) {
		Configuration  conf=HBaseConfiguration.create();
		try {
			int status=ToolRunner.run(conf, new TestRunner(), args);
			System.exit(status);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
