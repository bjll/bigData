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
	 //模拟不同的agent
	private static String[] ua = {"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36 OPR/37.0.2178.32",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586",
            "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
            "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0)",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 BIDUBrowser/8.3 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.277.400 QQBrowser/9.4.7658.400",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 UBrowser/5.6.12150.8 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36 TheWorld 7",
            };
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
					spiderService.spiderData(SysConstant.BASE_URL, params,null);
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
	 /**
     * 爬取京东的任务(不用浏览器的的的驱动 直接分析接口API)
     */
	public void spiderJDData() {
		logger.info("爬虫开始....");
		// 执行任务的开始时间
		long startTime = System.currentTimeMillis();
		// 使用现线程池提交任务
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		// 引入countDownLatch进行线程同步，使主线程等待线程池的所有任务结束，便于计时
		// CountDownLatch 里面的计数器是线程的个数
		final CountDownLatch countDownLatch = new CountDownLatch(200);
		//这个地址必须有否则获取不到内容
		String refererUrl="https://search.jd.com/Search";//上一个页面的链接地址
		Map<String,String> hearderMap=new HashMap<String,String>();//模拟请求的头部信息
		hearderMap.put("referer", refererUrl);
	    // 设置URL 需要的参数
		final Map<String, String> params = new HashMap<String, String>();
		params.put("keyword", "男装");
		params.put("enc", "utf-8");
		params.put("wc", "男装");
		params.put("scrolling", "y");
		for (int i = 1; i <201; i++) {
			int b=(int)(Math.random()*10);//产生0-10的整数随机数
			params.put("page", i + "");
			hearderMap.put("user-agent", ua[b].toString());//设置不同的user-agent 模拟不同的浏览器请求
			executorService.submit(new Runnable() {
				public void run() {
					spiderService.spiderData(SysConstant.BASE_URL, params,hearderMap);
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
}
