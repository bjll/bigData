package com.ll.test.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import com.google.common.collect.Lists;
import com.ll.test.dao.GoodsInfoDao;
import com.ll.test.entity.GoodsInfo;
import com.ll.test.util.HttpClientUtils;

/**
 * 这个类主要是用来处理爬虫的逻辑
 * 
 * @author Chris
 */
@Service
public class SpiderService {
	private static Logger logger = LoggerFactory.getLogger(SpiderService.class);
	@Autowired
	private GoodsInfoDao goodsInfoDao;
	// http协议
	private static String HTTPS_PROTOCOL = "https:";

	public void spiderData(String url, Map<String, String> params) {
		String html = HttpClientUtils.getHtmlStrByDriver(url, null, params);
		if (!StringUtils.isBlank(html)) {
			List<GoodsInfo> goodsInfos = parseHtml(html);
			goodsInfoDao.saveBatch(goodsInfos);
		}
	}
	/**
	 * 解析html
	 * 
	 * @param html
	 */
	private List<GoodsInfo> parseHtml(String html) {
		// 商品集合
		List<GoodsInfo> goods =new ArrayList<GoodsInfo>();
		/**
		 * 获取dom并解析
		 */
		Document document = Jsoup.parse(html);
		Elements elements = document.select("ul[class=gl-warp clearfix]").select("li[class=gl-item]");
		System.err.println("elmentSize:::::" + elements.size());
		for (Element element : elements) {
			try {
				String goodsId = element.attr("data-sku");// 商品ID
				String goodsName = element.select("div[class=p-name p-name-type-2]").select("em").text();// 商品名称
				String goodsPrice = element.select("div[class=p-price]").select("strong").select("i").text();// 商品价格
				//因为图片的地址可能不是在同意一个属性中 这里需要进行判断
				String imgUrl = element.select("div[class=p-img]").select("a").select("img").attr("src");// 图片地址
				if (StringUtils.isBlank(imgUrl)) {
					imgUrl = HTTPS_PROTOCOL+ element.select("div[class=p-img]").select("a").select("img").attr("data-lazy-img");
				} else if (StringUtils.isBlank(imgUrl)) {
					imgUrl = HTTPS_PROTOCOL+ element.select("div[class=p-img]").select("a").select("img").attr("source-data-lazy-img");
				}
				String evaluationsNum = element.select("div[class=p-commit]").select("a").text();// 评价数
				String merchantName = element.select("div[class=p-shop]").select("a").text();// 商家名称
				String merchantUrl = HTTPS_PROTOCOL + element.select("div[class=p-shop]").select("a").attr("href");// 商家地址
				//说明是京东自营
				if(StringUtils.isBlank(merchantName)&&StringUtils.isBlank(merchantUrl)){
					merchantUrl=merchantName="自营";
				}
				GoodsInfo goodsInfo = new GoodsInfo(goodsId, goodsName, HTTPS_PROTOCOL + imgUrl, goodsPrice,
						evaluationsNum, merchantUrl, merchantName);
				goods.add(goodsInfo);
				String jsonStr = JSON.toJSONString(goodsInfo);
				logger.info("成功爬取【" + goodsName + "】的基本信息 ");
				logger.info(jsonStr);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return goods;
	}
}
