package com.test.hdfs2hbae;

import java.io.IOException;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.NullWritable;

/**
 * 从Hdfs到Hbase
 * 
 * @author Chris
 */
public class Hdfs2HbaseReducer extends TableReducer<ImmutableBytesWritable, Put, NullWritable> {
	public Hdfs2HbaseReducer(){
		System.err.println("---我是构造方法----Hdfs2HbaseReducer");
	}
	@Override
	protected void reduce(ImmutableBytesWritable immutableBytesWritable, Iterable<Put> puts, Context context)
			throws IOException, InterruptedException {
		for (Put put : puts) {
			context.write(NullWritable.get(), put);
		}
	}
}
