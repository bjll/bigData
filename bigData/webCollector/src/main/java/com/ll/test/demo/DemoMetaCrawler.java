package com.ll.test.demo;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.ram.RamCrawler;

public class DemoMetaCrawler extends RamCrawler {
	/*
	 * 实际使用时建议按照DemoTypeCrawler的方式操作，该教程目的为阐述meta的原理
	 * 
	 * 可以往next中添加希望后续爬取的任务，任务可以是URL或者CrawlDatum
	 * 爬虫不会重复爬取任务，从2.20版之后，爬虫根据CrawlDatum的key去重，而不是URL
	 * 因此如果希望重复爬取某个URL，只要将CrawlDatum的key设置为一个历史中不存在的值即可 例如增量爬取，可以使用
	 * 爬取时间+URL作为key。 meta 保证抽取到的是 是指定类型的数据 新版本中，可以直接通过
	 * page.select(css选择器)方法来抽取网页中的信息，等价于
	 * page.getDoc().select(css选择器)方法，page.getDoc()获取到的是Jsoup中的
	 * Document对象，细节请参考Jsoup教程
	 * 
	 * 该Demo爬虫需要应对豆瓣图书的三种页面： 1）标签页（taglist，包含图书列表页的入口链接）
	 * 2）列表页（booklist，包含图书详情页的入口链接） 3）图书详情页（content）
	 */
	public void visit(Page page, CrawlDatums next) {

		String type = page.meta("type");// 获取类型
		// 如果是列表页，抽取内容页链接，放入后续任务中
		if (type.equals("taglist")) {
			// 可以确定抽取到的链接都指向内容页
			// 因此为这些链接添加附加信息（meta）：type=content
			next.addAndReturn(page.links("table.tagCol td>a")).meta("type", "booklist");
		} else if (type.equals("booklist")) {
			next.addAndReturn(page.links("div.info>h2>a")).meta("type", "content");
		} else if (type.equals("content")) {
			// 处理内容页，抽取书名和豆瓣评分
			String title = page.select("h1>span").first().text();
			String score = page.select("strong.ll.rating_num").first().text();
			// System.error.println("title:" + title + "\tscore:" + score);
			System.err.println("title:" + title + "\tscore:" + score);
		}

	}

	public static void main(String[] args) throws Exception {
		DemoMetaCrawler crawler = new DemoMetaCrawler();
		// meta是CrawlDatum的附加信息，爬虫内核并不使用meta信息
		// 在解析页面时，往往需要知道当前页面的类型（例如是列表页还是内容页）或一些附加信息（例如页号）
		// 然而根据当前页面的信息（内容和URL）并不一定能够轻易得到这些信息
		// 例如当在解析页面 https://book.douban.com/tag/ 时，需要知道该页是目录页还是内容页
		// 虽然用正则可以解决这个问题，但是较为麻烦
		// 当我们将一个新链接（CrawlDatum）提交给爬虫时，链接指向页面的类型有时是确定的（例如在很多任务中，种子页面就是列表页）
		// 如果在提交CrawlDatum时，直接将链接的类型信息（type）存放到meta中，那么在解析页面时，
		// 只需取出链接（CrawlDatum）中的类型信息（type）即可知道当前页面类型
		CrawlDatum seed = new CrawlDatum("https://book.douban.com/tag/").meta("type", "taglist");
		crawler.addSeed(seed);

		/* 可以设置每个线程visit的间隔，这里是毫秒 */
		// crawler.setVisitInterval(1000);
		/* 可以设置http请求重试的间隔，这里是毫秒 */
		// crawler.setRetryInterval(1000);
		crawler.setThreads(30);
		crawler.start(3);
		// /*设置每次迭代中爬取数量的上限*/
		crawler.getConf().setTopN(1000);
		/*
		 * 设置是否为断点爬取，如果设置为false，任务启动前会清空历史数据。
		 * 如果设置为true，会在已有crawlPath(构造函数的第一个参数)的基础上继
		 * 续爬取。对于耗时较长的任务，很可能需要中途中断爬虫，也有可能遇到 死机、断电等异常情况，使用断点爬取模式，可以保证爬虫不受这些因素
		 * 的影响，爬虫可以在人为中断、死机、断电等情况出现后，继续以前的任务 进行爬取。断点爬取默认为false
		 */
		crawler.setResumable(true);
	}

}
