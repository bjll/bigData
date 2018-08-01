package com.ll.test.storn;

import java.util.Map;
import java.util.Random;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * spout，继承一个基类，实现接口，这个里面主要是负责从数据源获取数据
 * @author lilin
 */
public class RandomSentenceSpout extends BaseRichSpout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(RandomSentenceSpout.class);
	private SpoutOutputCollector collector;//发射器
	private Random random;//生产随机数的对象
	/**
	 * 在open方法初始化的时候，会传入进来一个东西，叫做SpoutOutputCollector
	 * 这个SpoutOutputCollector就是用来发射数据出去的
	 * storm的上下文对象:topologyContext
	 */
	public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector collector) {
        this.collector=collector;
        this.random = new Random();
	}

	/**
	 * 这个方法会一直循环(只要Strom 不关闭) 可以不断发射最新的数据出去，形成一个数据流
	 */
	public void nextTuple() {
         try {
			Thread.currentThread().sleep(1000);//模拟和数据生产的过程
		} catch (Exception e) {
		   LOGGER.error("----ERROE-----");	
		}
        String[] sentences = new String[]{"the cow jumped over the moon", "an apple a day keeps the doctor away","four score and seven years ago", "snow white and the seven dwarfs", "i am at two with nature"};
 		String sentence = sentences[random.nextInt(sentences.length)];
 		LOGGER.info("【发射句子】sentence=" + sentence);  
 		//tuple是最小的数据单位，无限个tuple组成的流就是一个stream
 		collector.emit(new Values(sentence)); 
	}
	/**
	 * 很重要，这个方法是定义一个你发射出去的每个tuple中的每个field的名称是什么
	 */
	public void declareOutputFields(OutputFieldsDeclarer fieldsDeclarer) {
		fieldsDeclarer.declare(new Fields("sentence")); //这里定义的名字也就是在bolt 中取数据时用的名字
	}

}
