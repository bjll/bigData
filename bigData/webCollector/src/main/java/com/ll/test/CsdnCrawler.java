package com.ll.test;



import java.util.ArrayList;
import java.util.List;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

public class CsdnCrawler extends BreadthCrawler {
   /**
    * 
    * @param crawlPath 伯克利DB使用的文件夹(会在项目的目录下自动生成)
    * @param autoParse 是否根据设置的正则自动探测新URL
    */
	List list=new ArrayList();
	public CsdnCrawler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
		/* start page */
		this.addSeed("https://blog.csdn.net/");// 添加种子地址
		/* fetch url like http://news.hfut.edu.cn/show-xxxxxxhtml */
		this.addRegex("https://blog.csdn.net/.*/article/details/.*");
		/* do not fetch jpg|png|gif */
		this.addRegex("-.*\\.(jpg|png|gif).*");
		//设置的线程数
		setThreads(10);
        getConf().setTopN(100);
        getConf().setExecuteInterval(1000);//线程执行间隔
	}
    /**
     * 这是执行解析的方法
     */
	public void visit(Page page, CrawlDatums next) {
		 if (page.matchUrl("https://blog.csdn.net/.*/article/details/.*")) {
	            String title =page.select("h1[class=title-article]").first().text();
	            System.err.println(title);
	            list.add(title);
	            //这里可以执行业务逻辑操作
	        }
	}
	public static void main(String[] args) {
		try {
			CsdnCrawler csdnCrawler = new CsdnCrawler("test", true);
			csdnCrawler.start(3);//迭代次数
			csdnCrawler.list.size();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
