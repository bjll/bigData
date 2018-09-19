package com.ll.test.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * 数据库操作的相关信息
 * 
 * @author Chris
 *
 */
public class DBHelper {
	// 数据库连接对象
	private static Connection conn = null;

	private String driver = ""; // 驱动

	private String url = ""; // 连接字符串

	private String username = ""; // 用户名

	private String password = ""; // 密码
	
	
	private static Map<String, Object> dbMap = new HashMap<String, Object>();//存放数据库信息的Map
	// 获得连接对象
	public Connection getConn() {
		if (conn == null) {
			init();
			driver=String.valueOf(dbMap.get("db.oracle.driver"));
			url=String.valueOf(dbMap.get("db.oracle.url"));
			username=String.valueOf(dbMap.get("db.oracle.username"));
			password=String.valueOf(dbMap.get("db.oracle.password"));
			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(url, username, password);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}

	// 执行查询语句
	public void query(String sql, boolean isSelect) throws SQLException {
		PreparedStatement pstmt=null;
		try {
			pstmt =conn.prepareStatement(sql);
			// 建立一个结果集，用来保存查询出来的结果
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
				System.out.println(name);
			}
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null){
				pstmt.close();
			}
		}
	}

	public void query(String sql) throws SQLException {
		PreparedStatement pstmt=null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.execute();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null){
				pstmt.close();
			}
		}
		
	}
	// 关闭连接
	public void close() {
		try {
			if(conn!=null){
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			// https://blog.csdn.net/wuyankaiboke/article/details/80575903
			// Oracle基础 2018年06月05日 08:30:13 阅读数：3638
			DBHelper dbHelper = new DBHelper();
			dbHelper.getConn();
			dbHelper.query(
					"insert into csdn_data values('https://blog.csdn.net/wuyankaiboke/article/details/80575903','Oracle','2018年06月05日 08:30:13','阅读数：3638')");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * 执行初始化的方法
	 */
	private  void  init(){
		Properties prop = new Properties();
		try {
			InputStream in =DBHelper.class.getClassLoader().getResourceAsStream("db.properties");
			 prop.load(in);     ///加载属性列表
			 Iterator<String> it=prop.stringPropertyNames().iterator();
			 while (it.hasNext()) {
				 String key=it.next();
				 dbMap.put(key, prop.get(key));
			}
			 in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
