package com.ll.test.entity;

/**
 * 淘宝的商品信息
 * 
 * @author Chris
 *
 */
public class TaoBaoGoodsInfo {
	private String raw_title;// 名称
	private String view_fee;// 价格
	private String item_loc;// 地址
	private String comment_count;// 评论数
	private String detail_url;// 详情地址

	public TaoBaoGoodsInfo() {
		super();
	}

	public TaoBaoGoodsInfo(String raw_title, String view_fee, String item_loc, String comment_count,
			String detail_url) {
		super();
		this.raw_title = raw_title;
		this.view_fee = view_fee;
		this.item_loc = item_loc;
		this.comment_count = comment_count;
		this.detail_url = detail_url;
	}

	public String getRaw_title() {
		return raw_title;
	}

	public void setRaw_title(String raw_title) {
		this.raw_title = raw_title;
	}

	public String getView_fee() {
		return view_fee;
	}

	public void setView_fee(String view_fee) {
		this.view_fee = view_fee;
	}

	public String getItem_loc() {
		return item_loc;
	}

	public void setItem_loc(String item_loc) {
		this.item_loc = item_loc;
	}

	public String getComment_count() {
		return comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

	public String getDetail_url() {
		return detail_url;
	}

	public void setDetail_url(String detail_url) {
		this.detail_url = detail_url;
	}

	@Override
	public String toString() {
		return "TaoBaoGoodsInfo [raw_title=" + raw_title + ", view_fee=" + view_fee + ", item_loc=" + item_loc
				+ ", comment_count=" + comment_count + ", detail_url=" + detail_url + "]";
	}

}
