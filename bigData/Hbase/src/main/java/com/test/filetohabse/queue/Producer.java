package com.test.filetohabse.queue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 生产者的类 用于从文件中读取数据
 * @author lilin
 *
 */
public class Producer extends Thread {
	private LinkedBlockingQueue<String> b;// 定义一个队列
	private  FileReader m;
	private  BufferedReader reader;
	public Producer() {
	}
	public Producer(LinkedBlockingQueue<String> b) {
		init();
		this.b = b;
	}
    //这里是执行生产的过程
	@Override
	public void run() {
		try {
			String line="";
			while (true) {
				line=reader.readLine();
				if(line==null||"".equals(line)){
					break;
				}
				//执行操作
				b.offer(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(m!=null){
				try {
					m.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 *从文件中读取数据的方法
	 */
	public  void  init() {
		try {
			File file=new File("E:\\test.txt");
			m=new FileReader(file);
			reader=new BufferedReader(m);//读取文件
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(m!=null){
				try {
					m.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
