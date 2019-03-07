package com.ll.test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.jmx.Agent;

import com.ll.test.entity.CsdnVo;
import com.mchange.lang.StringUtils;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

public class CsdnCrawler extends BreadthCrawler {
	/**
	 * 
	 * @param crawlPath
	 *            伯克利DB使用的文件夹(会在项目的目录下自动生成)
	 * @param autoParse
	 *            是否根据设置的正则自动探测新URL
	 */
	//文件写入的队列
	private static LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<String>();
	//往数据库插入信息的对列
	private static LinkedBlockingQueue<CsdnVo> csdnInfoQueen = new LinkedBlockingQueue<CsdnVo>();
	
	private static Writer out;// 写入文件的流

	public CsdnCrawler(String crawlPath, boolean autoParse) {
		
		super(crawlPath, autoParse);
		/* start page */
		this.addSeed("https://item.jd.com/7437788.html");// 添加种子地址
		/* fetch url like http://news.hfut.edu.cn/show-xxxxxxhtml */
		this.addRegex("https://item.jd.com/.*");
		/* do not fetch jpg|png|gif */
		this.addRegex("-.*\\.(jpg|png|gif).*");
		// 设置的线程数
		setThreads(20);
		getConf().setTopN(10000);
		getConf().setExecuteInterval(0);// 线程执行间隔
	
	}
	/**
	 * 这是执行解析的方法
	 */
	public void visit(Page page, CrawlDatums next) {
		try {
			System.err.println(page.url());
			System.err.println(page.select("span[class=p-price]>span:nth-child(2)").first().text());
			//这里可能出现 404 界面  还没有做处理
//			if (page.matchUrl("https://blog.csdn.net/.*/article/details/.*")) {
//				String url = page.url();// 文章地址
//				String title = page.select("h1[class=title-article]").first().text();// 标题
//				if(title.contains("'")||title.contains("‘")){
//					title=title.replaceAll("'", "");//防止标题中出现 '
//				}
//				String publish_time = page.select("div[class=article-info-box]").first().select("span[class=time]").first()
//						.text();// 发布时间
//				String read_count = page.select("div[class=article-info-box]").first().select("span[class=read-count]")
//						.first().text();// 发布
//				//linkedBlockingQueue.put((url + "\t" + title + "\t" + time + "\t" + read_count).toString());
//				if(read_count!=null&&!"".equals(read_count)){
//					read_count=read_count.split("[：]")[1];
//				}
//				System.err.println((url + "\t" + title + "\t" + publish_time + "\t" + read_count).toString());
//				csdnInfoQueen.put(new CsdnVo(title, url, publish_time, read_count));
//			}
		} catch (Exception e) {
			   System.err.println("error url::::"+page.url());
			   e.printStackTrace();
		}

	}
	public static void main(String[] args) {
		try {
			/*File file = new File("E:\\test.txt");
			out = new FileWriter(file);
			CnsThread  cnsThread=new  CnsThread(linkedBlockingQueue,out);
			cnsThread.start();*/
			/*CnsThread  cnsThread=new  CnsThread(csdnInfoQueen);
			cnsThread.start();
			CnsThread  cnsThread1=new  CnsThread(csdnInfoQueen);
			cnsThread1.start();*/
			CsdnCrawler csdnCrawler = new CsdnCrawler("test", true);
			csdnCrawler.start(5);// 迭代次数
			// 3 https://blog.csdn.net/
			//4. https://blog.csdn.net/J_java1/article/details/83273782
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
