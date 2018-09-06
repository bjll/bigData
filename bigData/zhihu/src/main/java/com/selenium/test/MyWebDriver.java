package com.selenium.test;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Created by deng on 2017/5/16.
 */
public class MyWebDriver {
    private final static String CHROME_DRIVER_PATH = "D:\\chromedriver.exe";

    public static WebDriver createWebDriver() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        ChromeOptions  chromeOptions=new ChromeOptions();
        chromeOptions.addArguments(new String[]{"--headless","--disable-gpu"});//设置浏览器不弹窗
        return new ChromeDriver(chromeOptions);
    }
}
