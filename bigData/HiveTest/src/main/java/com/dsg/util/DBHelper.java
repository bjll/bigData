package com.dsg.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
	private static Connection connToHive = null;
	private static Connection connToMySQL = null;

	private DBHelper() {
	}
	/**
	 * 获取Hive的连接 如果不为空则创建
	 * @param url  hive的连接地址
	 * @param userName  所在节点的用户名
	 * @param passWord  所在节点的用户密码
	 * @return
	 * @throws SQLException
	 */
	public static Connection getHiveConn(String  url,String userName,String passWord) throws SQLException {
		if (connToHive == null) {
			try {
				Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver");
			} catch (ClassNotFoundException err) {
				err.printStackTrace();
				System.exit(1);
			}
			// hadoop3 为集群hive所在节点的IP地址
			connToHive = DriverManager.getConnection(url, userName, passWord);
		}
		return connToHive;
	}
	/**
	 * 获取mysql的连接 如果不为空则创建
	 * @param url  hive的连接地址
	 * @param userName  所在节点的用户名
	 * @param passWord  所在节点的用户密码
	 * @return
	 * @throws SQLException
	 */
	public static Connection getMySQLConn(String  url,String userName,String passWord) throws SQLException {
		if (connToMySQL == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException err) {
				err.printStackTrace();
				System.exit(1);
			}
			// hadoop2为集群mysql安装IP地址
			connToMySQL = DriverManager.getConnection(url,userName,passWord); // 编码不要写成UTF-8
		}
		return connToMySQL;
	}
	/**
	 * 关闭hive 连接
	 * @throws SQLException
	 */
	public static void closeHiveConn() throws SQLException {
		if (connToHive != null) {
			connToHive.close();
		}
	}
    /**
     * 关闭mysql 连接
     * @throws SQLException
     */
	public static void closeMySQLConn() throws SQLException {
		if (connToMySQL != null) {
			connToMySQL.close();
		}
	}
}
