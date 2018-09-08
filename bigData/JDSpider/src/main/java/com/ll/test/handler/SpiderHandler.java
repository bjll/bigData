package com.ll.test.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.ll.test.constant.SysConstant;
import com.ll.test.service.SpiderService;
import com.ll.test.service.TaoBaoService;


/**
 * 抓取任务的调度器
 * 
 * @author Chris
 *
 */
@Component
public class SpiderHandler {
	@Autowired
	private SpiderService spiderService;  //京东
	@Autowired
	private TaoBaoService taoBaoService;//淘宝
	private static final Logger logger = LoggerFactory.getLogger(SpiderHandler.class);
    /**
     * 爬取京东的任务
     */
	public void spiderData() {
		logger.info("爬虫开始....");
		// 执行任务的开始时间
		long startTime = System.currentTimeMillis();
		// 使用现线程池提交任务
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		// 引入countDownLatch进行线程同步，使主线程等待线程池的所有任务结束，便于计时
		// CountDownLatch 里面的计数器是线程的个数
		final CountDownLatch countDownLatch = new CountDownLatch(100);
		for (int i = 1; i <201; i += 2) {
			// 设置URL 需要的参数
			final Map<String, String> params = new HashMap<String, String>();
			params.put("keyword", "牛奶");
			params.put("enc", "utf-8");
			params.put("wc", "牛奶");
			params.put("page", i + "");
			executorService.submit(new Runnable() {
				public void run() {
					spiderService.spiderData(SysConstant.BASE_URL, params);
					countDownLatch.countDown();
				}
			});
		}
		try {
			countDownLatch.await();// 唤醒主线程
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executorService.shutdown();// 关闭线程池 否则会一直占用资源
		logger.info("爬虫结束....");
		System.err.println("[开始时间:" + (startTime - System.currentTimeMillis()) + "ms]");
	}
   /**
    * 爬取淘宝的任务
    */
	public void spiderDataTaoBao() {
		logger.info("爬虫开始....");
		// 执行任务的开始时间
		long startTime = System.currentTimeMillis();
		// 使用现线程池提交任务
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		// 引入countDownLatch进行线程同步，使主线程等待线程池的所有任务结束，便于计时
		// CountDownLatch 里面的计数器是线程的个数
		final CountDownLatch countDownLatch = new CountDownLatch(100);
		String url="https://s.taobao.com/search";
		Integer bcoffset=6;
		Integer ntoffset=6;
		for (int i = 1; i <101; i++) {
			 Map<String, String> params = new HashMap<String, String>();
			 params.put("data-key", "s");
			 params.put("data-value", (i-1)*44+"");
			 params.put("ajax", "true");
			 params.put("q", "男装");
			 params.put("s", (i-1)*44+"");
			 bcoffset=bcoffset-3;
			 params.put("bcoffset",(bcoffset)+"" );
			 ntoffset=ntoffset-3;
			 params.put("ntoffset", ntoffset+"");
			 executorService.submit(new Runnable() {
					public void run() {
						taoBaoService.dealInfo(url, params);
						countDownLatch.countDown();
					}
				});
		}
		try {
			countDownLatch.await();// 唤醒主线程
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executorService.shutdown();// 关闭线程池 否则会一直占用资源
		logger.info("爬虫结束....");
		System.err.println("[用时:" + (startTime - System.currentTimeMillis()) + "ms]");
	}
}
