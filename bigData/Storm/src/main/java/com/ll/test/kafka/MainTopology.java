package com.ll.test.kafka;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;

import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;
/**
 * Storm 与kafka 整合demo
 * @author Chris
 *@category kafka version :/kafka_2.11-1.1.0
 * zk   version: zookeeper-3.4.10
 * storm  version apache-storm-1.2.2
 */
public class MainTopology {
	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();
		// 配置zookeeper 主机:端口号
		//BrokerHosts brokerHosts = new ZkHosts("node1:2181,node2:2181,node3:2181");
		KafkaSpoutConfig.Builder<String, String> kafkaBuilder = KafkaSpoutConfig.builder("node1:9092,node2:9092,node3:9092", "testcheng");
		// setting consumer-group
		kafkaBuilder.setGroupId("test-consumer-group");
		
		// 创建kafkaspoutConfig
		KafkaSpoutConfig<String, String> build = kafkaBuilder.build();

		// 通过kafkaspoutConfig获得kafkaspout
		KafkaSpout<String, String> kafkaSpout = new KafkaSpout<String, String>(build);

		// 设置5个线程接收数据
		builder.setSpout("kafkaSpout", kafkaSpout,2);
		// 设置2个线程处理数据
		builder.setBolt("printBolt", new PrintBolt(), 2).shuffleGrouping("kafkaSpout");

		Config config = new Config();
		if (args.length > 0) {
			System.err.println("----cluster------");
			// 集群提交模式
			config.setDebug(true);
			StormSubmitter.submitTopology(args[0], config, builder.createTopology());
		} else {
			System.err.println("-------");
			// 本地测试模式
			config.setDebug(false);
			// 设置2个进程
			config.setNumWorkers(2);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("kafkaSpout", config, builder.createTopology());
			Thread.currentThread().sleep(10000 * 6);
		}
	}
}
