package com.ll.test.demo;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.ram.RamCrawler;
import cn.edu.hfut.dmic.webcollector.util.ExceptionUtils;

public class DemoRedirectCrawler extends RamCrawler{
	public DemoRedirectCrawler(String keyword, int pageNum) throws Exception {
		for (int pageIndex = 1; pageIndex <= pageNum; pageIndex++) {
			String url = createBingUrl(keyword, pageIndex);
			addSeedAndReturn(url);
		}
	}

	public void visit(Page page, CrawlDatums next) {
		// If the http status code is 301 or 302,
		// you have to obtain the redirected url, which is "Location" header of
		// the http response
		// and add it to subsequent tasks by applying "next.add(redirectedUrl)"
		// Since the page may contains metadata,
		// you have to copy it to the added task by "xxxx.meta(page.copyMeta())"
		if (page.code() == 301 || page.code() == 302) {
			return;
		}
		System.out.println("this page is not redirected: " + page.url());
	}

	public static void main(String[] args) throws Exception {
		DemoRedirectCrawler crawler = new DemoRedirectCrawler("网络爬虫", 3);
		crawler.start();
	}

	/**
	 * construct the Bing Search url by the search keyword and the pageIndex
	 * 
	 * @param keyword
	 * @param pageIndex
	 * @return the constructed url
	 * @throws Exception
	 */
	public static String createBingUrl(String keyword, int pageIndex) throws Exception {
		int first = pageIndex * 10 - 9;
		keyword = URLEncoder.encode(keyword, "utf-8");
		return String.format("http://cn.bing.com/search?q=%s&first=%s", keyword, first);
	}
}
