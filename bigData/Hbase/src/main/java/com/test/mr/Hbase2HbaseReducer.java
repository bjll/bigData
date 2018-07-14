package com.test.mr;

import java.io.IOException;


import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.NullWritable;


public class Hbase2HbaseReducer extends TableReducer<ImmutableBytesWritable, Put, NullWritable> {
	@Override
	protected void reduce(ImmutableBytesWritable immutableBytesWritable, Iterable<Put> puts,Context context)
			throws IOException, InterruptedException {
		//开始执行写入Hbase 的逻辑
		for (Put put : puts) {
			context.write(NullWritable.get(), put);
		}
	}
}
