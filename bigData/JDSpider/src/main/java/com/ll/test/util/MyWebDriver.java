package com.ll.test.util;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Created by lilin
 */
/**
 * 抓取动态数据的 驱动类
 * @author Chris
 *
 */
public class MyWebDriver {
	//驱动的路径
    private final static String CHROME_DRIVER_PATH = "D:\\chromedriver.exe";
   
    public static WebDriver createWebDriver() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        ChromeOptions  chromeOptions=new ChromeOptions();
        chromeOptions.addArguments(new String[]{"--headless","--disable-gpu"});//设置浏览器不弹窗
        return new ChromeDriver(chromeOptions);
    }
}
