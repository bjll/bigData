package com.ll.test.storn;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.Utils;

/**
 * Strom 单词统计拓扑
 * 
 * @author lilin
 */
public class WordCountTopology {
	public static void main(String[] args) {
		//在main方法中，会去将spout和bolts组合起来，构建成一个拓扑
		TopologyBuilder builder = new TopologyBuilder();

		// 这里的第一个参数的意思，就是给这个spout设置一个名字
		// 第二个参数的意思，就是创建一个spout的对象
		// 第三个参数的意思，就是设置spout的executor（也就是线程）有几个
		builder.setSpout("RandomSentence", new RandomSentenceSpout(), 2);
		builder.setBolt("SplitSentence", new SplitSentence(), 5)
		.setNumTasks(10)//任务数
		.shuffleGrouping("RandomSentence");//随机分组
		builder.setBolt("WordCount", new WordCount(), 10)
		.setNumTasks(20)
		// 这个很重要，就是说，相同的单词，从SplitSentence发射出来时，一定会进入到下游的指定的同一个task中
		// 只有这样子，才能准确的统计出每个单词的数量
		// 比如你有个单词，hello，下游task1接收到3个hello，task2接收到2个hello
		// 5个hello，全都进入一个task
		.fieldsGrouping("SplitSentence", new Fields("word")); //这里采用的是按照字段分组
		Config config = new Config();
		// 说明是在命令行执行，打算提交到storm集群上去
		if(args != null && args.length > 0) {
		config.setNumWorkers(3); //集群中的Worke机器数
		try {
			StormSubmitter.submitTopology(args[0], config, builder.createTopology());  
		} catch (Exception e) {
			e.printStackTrace();
		}
		}else {
			// 说明是在eclipse里面本地运行
			//TaskParallelism  最大的任务并行数
			config.setMaxTaskParallelism(20);  
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("WordCountTopology", config, builder.createTopology());  
	
			Utils.sleep(20000); 
	
			cluster.shutdown();//集群关闭
		}
	}
}
