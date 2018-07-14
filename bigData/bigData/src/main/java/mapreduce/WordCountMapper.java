package mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * 我的第一个大数据程序
 * 
 * @author lilin 统计文件里的单词出现次数 第一个参数是 输入一行数据的key 输入的一行数据 输出的数据 单词的统计数
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	Text k=new  Text();
	IntWritable v= new IntWritable(1);
	//map 每一行数据都会执行一次  这里写业务处理逻辑
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		// 1. 将一行内容转换为String
		String line = value.toString();
		// 切割 空格
		String[] words = line.split(" ");
		// 循环写出到下一一个阶段
		for (String word : words) {
			k.set(word);
			context.write(k, v);
		}
	}
}
