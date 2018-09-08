package com.ll.test.entity;

/**
 * 淘宝链接的信息实体类
 * 
 * @author Chris
 *
 */
public class LinkInfo {
	private Integer id;// 存入数据库的id
	private String linkName;// 连接名字
	private String hrefUrl;// 地址

	public LinkInfo(Integer id, String linkName, String hrefUrl) {
		super();
		this.id = id;
		this.linkName = linkName;
		this.hrefUrl = hrefUrl;
	}

	public LinkInfo() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getHrefUrl() {
		return hrefUrl;
	}

	public void setHrefUrl(String hrefUrl) {
		this.hrefUrl = hrefUrl;
	}

	@Override
	public String toString() {
		return "LinkInfo [id=" + id + ", linkName=" + linkName + ", hrefUrl=" + hrefUrl + "]";
	}

}
