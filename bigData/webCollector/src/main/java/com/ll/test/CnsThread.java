package com.ll.test;

import java.io.Writer;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.ll.test.entity.CsdnVo;
import com.ll.test.util.DBHelper;

public class CnsThread extends Thread {
	private LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<String>();
	// 往数据库插入信息的对列
	private static LinkedBlockingQueue<CsdnVo> csdnInfoQueen = new LinkedBlockingQueue<CsdnVo>();
	private Writer out;
	DBHelper dbHelper;

	// 写入文件的构造方法
	public CnsThread(LinkedBlockingQueue<String> linkedBlockingQueue, Writer out) {
		this.linkedBlockingQueue = linkedBlockingQueue;
		this.out = out;
	}

	// 往数据库插入的信息
	public CnsThread(LinkedBlockingQueue<CsdnVo> csdnInfoQueen) {
		this.csdnInfoQueen = csdnInfoQueen;
		// 初始化数据库信息
		dbHelper = new DBHelper();
		dbHelper.getConn();// 获取操作连接
	}

	/*
	 * @Override public void run() { long begeinTime=System.currentTimeMillis();
	 * try { // 10 秒读不到数据就认为，没有数据了 while (true) { String str =
	 * linkedBlockingQueue.poll(100, TimeUnit.SECONDS);
	 * if(str!=null&&!"".equals(str)){ out.write(str + "\n"); }else{ break; }
	 * 
	 * } } catch (Exception e) { e.printStackTrace(); } finally { if (out !=
	 * null) { try { out.flush(); out.close(); } catch (Exception e2) {
	 * e2.printStackTrace(); } } linkedBlockingQueue = null; }
	 * System.err.println((begeinTime-System.currentTimeMillis())/1000); }
	 */
	@Override
	public void run() {
		long begeinTime = System.currentTimeMillis();
		try {
			// 10 秒读不到数据就认为，没有数据了
			while (true) {
				CsdnVo csdnVo = csdnInfoQueen.poll(50, TimeUnit.SECONDS);
				if (csdnVo != null) {
					// 执行数据库插入操作 使用线程安全的 StringBuffer
					StringBuffer sql = new StringBuffer("insert into csdn_data values( '");
					sql.append(csdnVo.getUrl())
					.append(" ', '")
					.append(csdnVo.getTitle())
					.append("', '")
					.append(csdnVo.getPublish_time())
					.append("','")
					.append(csdnVo.getRead_count())
					.append("' )");
					System.err.println("----SQL-----" + sql.toString());
					dbHelper.query(sql.toString());
				} else {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			csdnInfoQueen = null;
			if (dbHelper != null) {
				dbHelper.close();// 关闭数据库操作
			}
		}
		System.err.println((begeinTime-System.currentTimeMillis())/1000);
	}
}
