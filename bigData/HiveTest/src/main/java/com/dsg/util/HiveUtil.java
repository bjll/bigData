package com.dsg.util;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Hive 数据处理工具类
 * @author lilin
 */
public class HiveUtil {
	/**
	 * 创建表 
	 * @param sql 执行建表的sql 语句
	 * @param connp:创建好的连接
	 * @return
	 * @throws SQLException
	 */
    public static boolean createTable(String sql,Connection connp) throws SQLException {  
        try {
			Connection conn =connp;
			Statement stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery(sql);
			return  res.next();
		} catch (Exception e) {
			return  false;
		}  
    }  
    /**
     * 查询语句
     * @param sql  查询的sql语句
     * @param connp 连接
     * @return 查询的结果
     * @throws SQLException
     */
    public static ResultSet queryData(String sql,Connection connp) throws SQLException {  
        Connection conn =connp;  
        Statement stmt = conn.createStatement();  
        ResultSet res = stmt.executeQuery(sql);  
        return res;  
    }  
	/**
	 * 加载数据
	 * @param sql  加载数据的sql
	 * @param connp
	 * @return
	 * @throws SQLException
	 */
	public static boolean loadData(String sql,Connection connp) throws SQLException {
		try {
			Connection conn = connp;
			Statement stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery(sql);
			return res.next();
		} catch (Exception e) {
			return false;
		}
    }  
    // 把数据存储到 MySQL 中  
    public static void hiveToMySQL(ResultSet res,Connection connp) throws SQLException {  
        Connection conn = connp;  
        Statement stmt = conn.createStatement();  
        while (res.next()) {  
            String rdate = res.getString(1);  
            String time = res.getString(2);  
            String type = res.getString(3);  
            String relateclass = res.getString(4);  
            String information = res.getString(5) + res.getString(6)+ res.getString(7);  
            StringBuffer sql = new StringBuffer();  
            sql.append("insert into hadooplog values(0,'");  
            sql.append(rdate + "','");  
            sql.append(time + "','");  
            sql.append(type + "','");  
            sql.append(relateclass + "','");  
            sql.append(information + "')");  
            int i = stmt.executeUpdate(sql.toString());  
        }  
    }  
}
