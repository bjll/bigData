package com.test.mr;

import java.io.IOException;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * https://www.cnblogs.com/mowei/p/6782132.html
 * https://www.cnblogs.com/qingyunzong/p/8681490.html
 * @author Chris
 */
public class hbase2hbaseMapper extends TableMapper<ImmutableBytesWritable, Put> {
    @Override
    protected void map(ImmutableBytesWritable key, Result value,Mapper<ImmutableBytesWritable, Result, ImmutableBytesWritable, Put>.Context context)
    		throws IOException, InterruptedException {
        Put  put=new  Put(key.get());//获取键值其获取的也就是rowid
        for (Cell cell:value.rawCells()) {//循环遍历每一个单元格
			//这里开始进行处理业务的逻辑把需要的内容放到表里面去
        	if("info".equals(Bytes.toString(CellUtil.cloneFamily(cell)))){//info 列族
        		//name 列
        		if("age".equals(Bytes.toString(CellUtil.cloneQualifier(cell)))){
        			put.add(cell);
        		}else if("color".equals(Bytes.toString(CellUtil.cloneQualifier(cell)))){
        			put.add(cell);
        		}
        	}
		}
    	//将数据写出
        context.write(key, put);
    }
}
