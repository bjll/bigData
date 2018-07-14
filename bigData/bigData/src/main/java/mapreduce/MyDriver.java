package mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.CombineFileInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.tukaani.xz.FinishableOutputStream;

//测试 提交任务
public class MyDriver {
   public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
	   //总结起来就是321  原则   3个类   2 个输出类型  1 个配置文件
	  //1.获取配置信息得到job 对象
	   Configuration  configuration=new  Configuration();
	   Job  job=Job.getInstance(configuration);
	  //2.指定本程序的jar
	   job.setJarByClass(MyDriver.class);
	   //3.执定本次业务的使用的map和reducer的类
	   job.setMapperClass(WordCountMapper.class);
	   job.setReducerClass(WordReduce.class);
	   //多个小文件上传时的处理
	   //4 执行mapper的数据输出类型
	   job.setMapOutputKeyClass(Text.class);
	   job.setMapOutputValueClass(IntWritable.class);
	   //设置小文件上传的时的分片数
	   job.setInputFormatClass(CombineTextInputFormat.class);
	   CombineTextInputFormat.setMaxInputSplitSize(job, 4*1024*1024);//最大是4M
	   CombineTextInputFormat.setMinInputSplitSize(job, 2*1024*1024);//最小是2M
	   //5，最终数据的输出类型 k 和v
	   job.setOutputKeyClass(Text.class);
	   job.setOutputValueClass(IntWritable.class);
	   //6 执行job 原始文件所在的类
	   FileInputFormat.setInputPaths(job, new Path(args[0]));
	   FileOutputFormat.setOutputPath(job, new Path(args[1]));
	   //7提交job 交给yarn去运行
	   boolean result=job.waitForCompletion(true);
	   System.exit(result?0:1);
}
}
