package com.ll.kakfka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Producer {
  public static void main(String[] args) {
	  KafkaProducer<String, String> producer = new KafkaProducer<>(new  Properties());
	  producer.send(new ProducerRecord<String, String>("first", "1", "hello world-"));
}
}
