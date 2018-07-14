package mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
/**
 * reducer  方法
 * @author Chris
 *
 */
public class WordReduce extends Reducer<Text, IntWritable, Text, IntWritable> {
	//这里写业务处理逻辑
    @Override
    protected void reduce(Text text, Iterable<IntWritable> values,Context context) throws IOException, InterruptedException {
    	///统计单词中的个数  
    	int sum=0;
    	for (IntWritable count:values) {
			sum+=count.get();
		}
    	//输出单词总个数
    	context.write(text, new IntWritable(sum));
    }
}
