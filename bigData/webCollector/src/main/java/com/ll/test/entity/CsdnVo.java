package com.ll.test.entity;

/**
 * Csdn 数据的 实体类
 * 
 * @author Chris
 *
 */
public class CsdnVo {

	private String title;// 标题
	private String url;// 链接地址
	private String publish_time;// 发布时间
	private String read_count;// 阅读数
    /**
     *无参构造函数
     */
	public CsdnVo() {

	}
    /**
     * 参数构造函数
     * @param title
     * @param url
     * @param publish_time
     * @param read_count
     */
	public CsdnVo(String title, String url, String publish_time, String read_count) {
		super();
		this.title = title;
		this.url = url;
		this.publish_time = publish_time;
		this.read_count = read_count;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}

	public String getRead_count() {
		return read_count;
	}

	public void setRead_count(String read_count) {
		this.read_count = read_count;
	}
	/**
	 * 用\t分开  方便以后解析
	 */
	@Override
	public String toString() {
		return url+"\t"+title+"\t"+publish_time+"\t"+read_count;
	}

}
