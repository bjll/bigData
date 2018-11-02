package com.ll.test.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ll.test.constant.SysConstant;

/**
 * Http请求的工具类
 * 
 * @author Chris 参考地址：https://www.cnblogs.com/lyy-2016/p/6388663.html
 */
public class HttpClientUtils {
	private final static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

	private final static String GET_METHOD = "GET";
	private final static String POST_METHOD = "POST";
	
	//private final static CloseableHttpClient client = HttpClients.createDefault();

	/**
	 * GET请求
	 * 
	 * @param url
	 *            请求url
	 * @param headers
	 *            头部参数设置
	 * @param params
	 *            参数
	 * @return
	 */
	public static String sendGet(String url, Map<String, String> headers, Map<String, String> params) {
		// 创建HttpClient对象
		CloseableHttpClient client = HttpClients.createDefault();
		StringBuilder reqUrl = new StringBuilder(url);
		// 获取行响应的内容字符串
		String result = "";
		/*
		 * 设置param参数
		 */
		if (params != null && params.size() > 0) {
			reqUrl.append("?");
			for (Entry<String, String> param : params.entrySet()) {
				reqUrl.append(param.getKey() + "=" + param.getValue() + "&");
			}
			url = reqUrl.subSequence(0, reqUrl.length() - 1).toString();
		}
		logger.info("URL:::::"+url);
		logger.debug("[url:" + url + ",method:" + GET_METHOD + "]");
		HttpGet httpGet = new HttpGet(url);
		/**
		 * 设置头部
		 */
		logger.debug("Header\n");
		if (headers != null && headers.size() > 0) {
			for (Entry<String, String> header : headers.entrySet()) {
				httpGet.addHeader(header.getKey(), header.getValue());
				logger.debug(header.getKey() + " : " + header.getValue());
			}
		}
		CloseableHttpResponse response = null;
		try {
			response = client.execute(httpGet);// 获取闲响应的信息
			/**
			 * 请求成功
			 */
			if (response.getStatusLine().getStatusCode() == 200) {
				
				HttpEntity entity = response.getEntity();
				
				result = EntityUtils.toString(entity, SysConstant.DEFAULT_CHARSET);
				
			}
		} catch (IOException e) {
			logger.error("网络请求出错，请检查原因");
		} finally {
			// 关闭资源
			try {
				if (response != null) {
					response.close();
				}
				client.close();
			} catch (IOException e) {
				logger.error("网络关闭错误错，请检查原因");
			}
		}
		return result;
	}

	/**
	 * POST请求
	 * 
	 * @param url
	 *            请求url
	 * @param headers
	 *            头部
	 * @param params
	 *            参数
	 * @return
	 */
	public static String sendPost(String url, Map<String, String> headers, Map<String, String> params) {
		CloseableHttpClient client = HttpClients.createDefault();
		String result = "";
		HttpPost httpPost = new HttpPost(url);
		/**
		 * 设置参数
		 */
		if (params != null && params.size() > 0) {
			List<NameValuePair> paramList = new ArrayList();
			for (Entry<String, String> param : params.entrySet()) {
				paramList.add(new BasicNameValuePair(param.getKey(), param.getValue()));
			}
			logger.debug("[url: " + url + ",method: " + POST_METHOD + "]");
			// 模拟表单提交
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, SysConstant.DEFAULT_CHARSET);
				httpPost.setEntity(entity);
			} catch (UnsupportedEncodingException e) {
				logger.error("不支持的编码");
				e.printStackTrace();
			}
			/**
			 * 设置头部
			 */
			if (headers != null && headers.size() > 0) {
				logger.debug("Header\n");
				if (headers != null && headers.size() > 0) {
					for (Entry<String, String> header : headers.entrySet()) {
						httpPost.addHeader(header.getKey(), header.getValue());
						logger.debug(header.getKey() + " : " + header.getValue());
					}
				}
			}
			CloseableHttpResponse response = null;
			try {
				response = client.execute(httpPost);
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, SysConstant.DEFAULT_CHARSET);
			} catch (IOException e) {
				logger.error("网络请求出错，请检查原因");
			} finally {
				try {
					if (response != null) {
						response.close();
					}
					client.close();
				} catch (IOException e) {
					logger.error("网络关闭错误");
				}
			}
		}
		return result;
	}
	/**
	 * post请求发送json
	 * 
	 * @param url
	 * @param json
	 * @param headers
	 * @return
	 */
	public static String senPostJson(String url, String json, Map<String, String> headers) {
		CloseableHttpClient client = HttpClients.createDefault();
		String result = "";
		HttpPost httpPost = new HttpPost(url);
		StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
		httpPost.setEntity(stringEntity);
		logger.debug("[url: " + url + ",method: " + POST_METHOD + ", json: " + json + "]");
		/**
		 * 设置头部
		 */
		if (headers != null && headers.size() > 0) {
			logger.debug("Header\n");
			if (headers != null && headers.size() > 0) {
				for (Entry<String, String> header : headers.entrySet()) {
					httpPost.addHeader(header.getKey(), header.getValue());
					logger.debug(header.getKey() + " : " + header.getValue());
				}
			}
		}
		CloseableHttpResponse response = null;
		try {
			response = client.execute(httpPost);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, SysConstant.DEFAULT_CHARSET);
		} catch (IOException e) {
			logger.error("网络请求出错，请检查原因");
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				client.close();
			} catch (IOException e) {
				logger.error("网络关闭错误");
			}
		}
		return result;
	}

	/**
	 * 通过Driver的方式抓取页面的动态数据
	 * 
	 * @param url
	 *            传递的地址
	 * @param headers
	 *            头部信息设置
	 * @param params
	 *            请求的参数设置
	 * @return
	 */
	public static String getHtmlStrByDriver(String url, Map<String, String> headers, Map<String, String> params) {
		// 返回的抓取的字符串
		String result = "";
		WebDriver driver = null;
		try {
			// 设置Driver
			driver = MyWebDriver.createWebDriver();
			// 请求的url
			StringBuilder reqUrl = new StringBuilder(url);
			// 设置参数
			if (params != null && params.size() > 0) {
				reqUrl.append("?");
				for (Entry<String, String> param : params.entrySet()) {
					reqUrl.append(param.getKey() + "=" + param.getValue() + "&");
				}
				url = reqUrl.subSequence(0, reqUrl.length() - 1).toString();
			}
			logger.info("----URL------" + reqUrl);
			driver.get(reqUrl.toString());
			//====================================这一部分是解决动态加载的内容=========================
			// 这里开始执行JS 完成下拉的操作
			driver.findElement(By.xpath("//*[@id=\"categorys-2014\"]/div/a")).sendKeys(Keys.DOWN);
			//Thread.sleep(1000);
			driver.findElement(By.xpath("//*[@id=\"categorys-2014\"]/div/a")).sendKeys(Keys.DOWN);
			//Thread.sleep(1000);
			driver.findElement(By.xpath("//*[@id=\"categorys-2014\"]/div/a")).sendKeys(Keys.DOWN);
			Thread.sleep(1000);
			// 滚动条相关设置滚动条距离
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
			Thread.sleep(1000);
			((JavascriptExecutor) driver).executeScript("scrollTo(0, 0)");
			Thread.sleep(1000);
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
			Thread.sleep(1000);
			driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);//页面加载超出10秒就会抛出异常
			//====================================这一部分是解决动态加载的内容=========================
			result=driver.getPageSource().toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if (driver != null) {
				driver.quit();// 停止掉driver
			}
		}
		return result;
	}
}
