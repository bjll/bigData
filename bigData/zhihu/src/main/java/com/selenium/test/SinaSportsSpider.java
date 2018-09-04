package com.selenium.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

/**
 * Created by lilin on 2018/3/6.
 * 这是浏览器没有关闭的版本 在后续可能会关闭浏览器不让其弹出来
 */
public class SinaSportsSpider implements Spider {
	private final static String TARGET_URL = "http://sports.sina.com.cn/basketball/nba/";
	private final static String FILE_NAME = "E:\\SinaSportsNews.txt";

	public void run() throws InterruptedException {
		// 拼接字符串
		StringBuilder allNewses = new StringBuilder();
		// 设置Driver
		WebDriver driver = MyWebDriver.createWebDriver();
		driver.get(TARGET_URL);
		//页面下拉几次 加载出所有的数据
		driver.findElement(By.xpath("//*[@id=\"firstScreennew\"]/div/div/a")).sendKeys(Keys.DOWN);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"firstScreennew\"]/div/div/a")).sendKeys(Keys.DOWN);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"firstScreennew\"]/div/div/a")).sendKeys(Keys.DOWN);
		Thread.sleep(1000);
		//滚动条相关设置滚动条距离
		((JavascriptExecutor)driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
		Thread.sleep(2000);
		((JavascriptExecutor)driver).executeScript("scrollTo(0, 0)");
		Thread.sleep(2000);
		((JavascriptExecutor)driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
		Thread.sleep(2000);
		WebDriver newsDriver = MyWebDriver.createWebDriver();
		// 超过10秒即为超时，会抛出Exception
		newsDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		Document document = Jsoup.parse(driver.getPageSource());
		List<Element> liTags = document.getElementsByClass("feed-card-item");
		System.err.println("----liTags---"+liTags.size());
		List<String>  hrefList=new ArrayList<String>();
		for (int i = 0; i < liTags.size(); i++) {
			Element aTag = liTags.get(i).getElementsByTag("a").first();
			// 遍历单条新闻
			String href = aTag.attr("href");
			// 筛选出新闻的url
			if (href.contains("sports.sina.com.cn") && href.contains("shtml")) {
				System.out.println(href);
				hrefList.add(href);
				allNewses.append(href + "\n");
				try {
					newsDriver.get(href);
				} catch (Exception e) {
					// 加载页面超时，执行js手动停止页面加载
					((JavascriptExecutor) newsDriver).executeScript("window.stop()");
				} finally {
					Document newsDocument = Jsoup.parse(newsDriver.getPageSource());
					String title = newsDocument.getElementsByClass("main-title").text();

					Element dateAndSource = newsDocument.getElementsByClass("date-source").get(0);
					String date = dateAndSource.getElementsByTag("span").get(0).text();
					String source = dateAndSource.getElementsByTag("a").get(0).text();

					allNewses.append(title + "\n");
					allNewses.append(date + " " + source + "\n");

					Element article = newsDocument.getElementById("artibody");
					for (Element p : article.getElementsByTag("p")) {
						allNewses.append(p.text().trim() + "\n");
					}
				}
			}
		}
		Thread.sleep(3000);
		driver.quit();
		newsDriver.quit();
		MyFileWriter.writeString(FILE_NAME, allNewses.toString());
	}
	public static void main(String[] args) {
		try {
			new SinaSportsSpider().run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
