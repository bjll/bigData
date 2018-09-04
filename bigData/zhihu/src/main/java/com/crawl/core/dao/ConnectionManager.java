package com.crawl.core.dao;

import com.crawl.core.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DB Connection管理
 */
public class ConnectionManager {
	private static Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
	private static Connection conn;
	public static Connection getConnection(){
		//获取数据库连接
		try {
			if(conn == null || conn.isClosed()){
                conn = createConnection();
            }
            else{
                return conn;
            }
		} catch (SQLException e) {
			logger.error("SQLException",e);
			e.printStackTrace();
		}
		return conn;
	}
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver") ;//加载驱动
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void close(){
		if(conn != null){
			//logger.info("关闭连接中");
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("SQLException",e);
				e.printStackTrace();
			}
		}
	}
	public static Connection createConnection(){
		String host = Config.dbHost;
		String user = Config.dbUsername;
		String password = Config.dbPassword;
		String dbName = Config.dbName;
		String url="jdbc:mysql://" + host + ":3306/" + dbName + "?characterEncoding=utf8";
		logger.info("-----connect info------"+url);
		Connection con=null;
		try{
			con = DriverManager.getConnection(url,user,password);//建立mysql的连接
			logger.debug("success!");
		}catch(SQLException e2){
			logger.error("SQLException",e2);
			e2.printStackTrace();
		}catch (Exception e) {
			logger.error("Exception",e);
			e.printStackTrace();
		}
		return con;
	}
	public static void main(String [] args) throws Exception{
		getConnection();
		getConnection();
		close();
	}
}
