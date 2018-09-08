package com.ll.test;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ll.test.handler.SpiderHandler;

/**
 * 主启动类
 * 
 * @author Chris
 */
@SpringBootApplication
public class App {
	@Autowired
	private SpiderHandler spiderHandler;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(App.class, args);
	}
   /**
    * 在项目启动的时候就执行任务去抓取数据
    */
	@PostConstruct
	public void task() {
		//spiderHandler.spiderData();
		spiderHandler.spiderDataTaoBao();
	}
}
