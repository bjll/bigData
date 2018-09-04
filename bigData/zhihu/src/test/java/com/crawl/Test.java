package com.crawl;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
/**
 * 这个页面下拉 的例子是模仿的百度 实测可用
 * @author lilin
 */
public class Test {
   
	public static WebDriver driver;
	public static void main(String[] args) throws Exception {
 
		System.setProperty("webdriver.chrome.driver", "D:\\chromedriver.exe");
		driver = new ChromeDriver();
		
		driver.get("http://www.baidu.com/");
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//*[@id=\"u1\"]/a[1]")).click();
		Thread.sleep(2000);
		/**
		 * 用sendKeys(Keys.DOWN)方法下拉加载页面
		 * 可能会需要多次下拉才能加载全部内容
		 * 以百度新闻为例
		 */
		driver.findElement(By.xpath("//*[@id=\"channel-all\"]/div/ul/li[1]/a")).sendKeys(Keys.DOWN);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"channel-all\"]/div/ul/li[1]/a")).sendKeys(Keys.DOWN);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"channel-all\"]/div/ul/li[1]/a")).sendKeys(Keys.DOWN);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"channel-all\"]/div/ul/li[1]/a")).sendKeys(Keys.DOWN);
		Thread.sleep(1000);
		((JavascriptExecutor)driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
		Thread.sleep(2000);
		((JavascriptExecutor)driver).executeScript("scrollTo(0, 0)");
		Thread.sleep(2000);
		((JavascriptExecutor)driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

}
