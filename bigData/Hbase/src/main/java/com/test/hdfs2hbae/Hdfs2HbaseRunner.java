package com.test.hdfs2hbae;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 从HDFS 读取数据到HBSE
 * 
 * @author Chris
 *
 */
public class Hdfs2HbaseRunner implements Tool {
	Configuration configuration;

	public Configuration getConf() {
		return this.configuration;
	}

	public void setConf(Configuration conf) {
		this.configuration = HBaseConfiguration.create(conf);
	}

	public int run(String[] arg0) throws Exception {
		// 开始组装job
		Configuration configuration = this.getConf();
		Job job = Job.getInstance(configuration);
		job.setJarByClass(Hdfs2HbaseRunner.class);
		Path path=new Path("hdfs://node1:9000/test");//读取文件的路径
		FileInputFormat.addInputPath(job, path);
		//设置Mapper
		job.setMapperClass(Hdfs2HbaseMapper.class);
		job.setMapOutputKeyClass(ImmutableBytesWritable.class);
		job.setMapOutputValueClass(Put.class);
		/*job.setOutputKeyClass(ImmutableBytesWritable.class);
	    job.setOutputValueClass(Put.class);*/
		//设置reduce
		TableMapReduceUtil.initTableReducerJob("test", Hdfs2HbaseReducer.class, job);
		// 设置reduce的数量
		job.setNumReduceTasks(1);
		boolean isSuccess = job.waitForCompletion(true);
		return isSuccess == true ? 0 : 1;
	}

	// 执行的方法
	public static void main(String[] args) {
		Configuration conf = HBaseConfiguration.create();
		try {
			int status = ToolRunner.run(conf, new Hdfs2HbaseRunner(), args);
			System.exit(status);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
