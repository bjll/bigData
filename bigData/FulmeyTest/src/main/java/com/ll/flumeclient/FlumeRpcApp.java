package com.ll.flumeclient;

import org.apache.flume.api.RpcClient;

import java.util.Random;

/**
 * 向flume监听的端口发送数据
 *
 */
public class FlumeRpcApp {

	public static void main(String[] args) {

        RpcClient client = FlumeRPCClient.createClient("node1", 41414);
        Random _rand = new Random();
        //随机发送句子
        String[] sentences = new String[]{ "the cow jumped over the moon", "an apple a day keeps the doctor away",
                "four score and seven years ago", "snow white and the seven dwarfs", "i am at two with nature"
                ,"难道 没有 中文a 吗"};
        for (int i=0;i<100;i++){
            String data =sentences[_rand.nextInt(sentences.length)];
            FlumeRPCClient.sendData(client,data);
            System.out.println("sendData--------->"+data);
        }
        client.close();
    }
}
