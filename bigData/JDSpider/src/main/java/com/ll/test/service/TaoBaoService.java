package com.ll.test.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ll.test.dao.GoodsInfoDao;
import com.ll.test.entity.TaoBaoGoodsInfo;
import com.ll.test.util.HttpClientUtils;

/**
 * 这个方法是抓取淘宝时的 分析
 * 
 * @author Chris
 *
 */
@Service
public class TaoBaoService {
	@Autowired
	private GoodsInfoDao goodsInfoDao;
	
	/**
	 * 查询商品的详细信息
	 * 
	 * @param url
	 *            查询的地址
	 * @param paramter
	 *            传递的参数
	 * @return
	 */
	
	public List<TaoBaoGoodsInfo> getGoodsInfoByParamter(String url, Map paramter) {
		List<TaoBaoGoodsInfo> goodsList = new ArrayList<TaoBaoGoodsInfo>();// 商品信息
		//url = "https://s.taobao.com/search?data-key=s&data-value=44&ajax=true&q=女装&bcoffset=3&ntoffset=3&s=44";
		String html = HttpClientUtils.sendGet(url, null, paramter);
		//如果为空直接返回
		if(StringUtils.isBlank(html)){
			return goodsList;
		}
		JSONObject  jsonObject=JSONObject.parseObject(html);
		//获取到符服装的相列表信息
		JSONObject itemlist=(JSONObject) (JSONObject.parseObject(jsonObject.get("mods").toString()).get("itemlist"));
		//分析逻辑加入到集合中
		JSONObject auctionsArr=(JSONObject)itemlist.get("data");
		JSONArray goodsArry=(JSONArray)auctionsArr.get("auctions");
		Map map=null;
		for (int i = 0; i < goodsArry.size(); i++) {
			map=(Map) goodsArry.get(i);
			String raw_title=(String) map.get("raw_title");
			String view_fee=(String) map.get("view_fee");
			String item_loc=(String) map.get("item_loc");
			String comment_count=(String) map.get("comment_count");
			String detail_url=(String) map.get("detail_url");
			goodsList.add(new TaoBaoGoodsInfo(raw_title, view_fee, item_loc, comment_count, detail_url));
		}
		return goodsList;
	}
	/**
	 * 处理抓取到的信息
	 */
    public void  dealInfo(String url, Map paramter){
    	List<TaoBaoGoodsInfo> goodsList=getGoodsInfoByParamter(url, paramter);
    	if(goodsList!=null&&!goodsList.isEmpty()){
    		goodsInfoDao.saveInfo(goodsList);
    	}
    }
/*	public static void main(String[] args) {
		TaoBaoService  taoBaoService=new TaoBaoService();
		String url="https://s.taobao.com/search";
		Integer bcoffset=6;
		Integer ntoffset=6;
		for (int i = 1; i <50; i++) {
			 Map<String, String> params = new HashMap<String, String>();
			 params.put("data-key", "s");
			 params.put("data-value", (i-1)*44+"");
			 params.put("ajax", "true");
			 params.put("q", "女装");
			 params.put("s", (i-1)*44+"");
			 bcoffset=bcoffset-3;
			 params.put("bcoffset",(bcoffset)+"" );
			 ntoffset=ntoffset-3;
			 params.put("ntoffset", ntoffset+"");
			System.err.println(taoBaoService.getGoodsInfoByParamter(url, params).size());
		}
	}*/
}
