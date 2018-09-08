package com.ll.test.entity;

/**
 * 商品的信息实体类
 * 
 * @author lilin
 */
public class GoodsInfo {

	private Integer id; // 数据库主键ID

	private String goodsId;// 商品ID

	private String goodsName;// 商品名称

	private String imgUrl;// 商品图片的URL 地址

	private String goodsPrice;// 商品价格

	private String evaluationsNum;// 评价数

	private String merchantUrl;// 商家地址
	
	private String merchantName;//商家名称

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getEvaluationsNum() {
		return evaluationsNum;
	}

	public void setEvaluationsNum(String evaluationsNum) {
		this.evaluationsNum = evaluationsNum;
	}

	public String getMerchantUrl() {
		return merchantUrl;
	}

	public void setMerchantUrl(String merchantUrl) {
		this.merchantUrl = merchantUrl;
	}
	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public GoodsInfo(String goodsId, String goodsName, String imgUrl, String goodsPrice) {
		super();
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.imgUrl = imgUrl;
		this.goodsPrice = goodsPrice;
	}

	public GoodsInfo(String goodsId, String goodsName, String imgUrl, String goodsPrice, String evaluationsNum,
			String merchantUrl,String merchantName) {
		super();
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.imgUrl = imgUrl;
		this.goodsPrice = goodsPrice;
		this.merchantUrl = merchantUrl;
		this.evaluationsNum = evaluationsNum;
		this.merchantName=merchantName;
	}
	
}
