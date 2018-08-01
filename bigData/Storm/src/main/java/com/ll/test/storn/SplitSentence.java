package com.ll.test.storn;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SplitSentence  extends BaseRichBolt {

	private static final Logger LOGGER = LoggerFactory.getLogger(SplitSentence.class);
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;//发射器
	/**
	 *在数据处理端进入的第一个方法
	 *可以在里面做一些初始化的操作
	 */
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        LOGGER.info("-----prepare-----");    	
		this.collector=outputCollector;
		
	}
    /**
     * 就是说，每次接收到一条数据后，就会交给这个executor方法来执行
     * 处理数据的逻辑
     */
	public void execute(Tuple tuple) {
		String sentence = tuple.getStringByField("sentence"); 
		String[] words = sentence.split(" "); 
		for(String word : words) {
			collector.emit(new Values(word));//把接收到的数据在发出去
		}
	}
	/*
	 * 定义发出字段的类型
	 */
	public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
		outputFieldsDeclarer.declare(new Fields("word"));   
	}

	

}
