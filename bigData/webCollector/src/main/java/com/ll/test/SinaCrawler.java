package com.ll.test;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

/**
 * 新浪新闻爬取
 * 
 * @author Chris
 *
 */
public class SinaCrawler extends BreadthCrawler {
	/**
	 * 
	 * @param crawlPath
	 *            伯克利DB使用的文件夹(会在项目的目录下自动生成)
	 * @param autoParse
	 *            是否根据设置的正则自动探测新URL
	 */
	List list = new ArrayList();

	public SinaCrawler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
		/* start page */
		this.addSeed("https://news.sina.com.cn/");// 添加种子地址
		/* fetch url like http://news.hfut.edu.cn/show-xxxxxxhtml */
		//http://mil.news.sina.com.cn/jssd/2018-09-05/doc-ihitesuy5959938.shtml
//		this.addRegex("http://mil.news.sina.com.cn/china/.*/.*.shtml");
//		/* do not fetch jpg|png|gif */
//		this.addRegex("-.*\\.(jpg|png|gif).*");
//		this.addRegex("http://mil.news.sina.com.cn/jssd/.*/.*.shtml");
//		this.addRegex("http://mil.news.sina.com.cn/world/.*/.*.shtml");
		this.addRegex("http://news.sina.com.cn/.*/.*/.*.shtml");
		// 设置的线程数
		setThreads(10);
		getConf().setTopN(100);
		getConf().setExecuteInterval(1000);// 线程执行间隔
	}
	/**
	 * 这是执行解析的方法
	 */
	public void visit(Page page, CrawlDatums next) {
		System.err.println("URL---"+page.url());
//		if (page.matchUrl("http://mil.news.sina.com.cn/china/.*/.*.shtml")) {
//			//如果匹配到URL那么就跳转带这个网页里去取数据
//			String  title=page.select("h1[class=main-title]").text();
//			System.err.println(title);
//		}else if(page.matchUrl("http://mil.news.sina.com.cn/jssd/.*/.*.shtml")){
//			String  title=page.select("h1[class=main-title]").text();
//			System.err.println("----深度-----"+title);
//		}else if(page.matchUrl("http://mil.news.sina.com.cn/world/.*/.*.shtml")){
//			String  title=page.select("h1[class=main-title]").text();
//			System.err.println("----国际-----"+title);
//		}
		//http://ent.sina.com.cn/s/h/2018-09-05/doc-ihitesuy5281589.shtml
		if(page.matchUrl("http://news.sina.com.cn/.*/.*/.*.shtml")){
			String  title=page.select("h1[class=main-title]").text();
			System.err.println(title);
		}
	}
	public static void main(String[] args) {
		try {
			SinaCrawler csdnCrawler = new SinaCrawler("test", true);
			csdnCrawler.start(3);// 迭代次数
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
