package com.test;

public class Test extends Thread  {
	private String name="";
	public  Test(){
		
	}
	public  Test(String name){
		this.name=name;
	}
	public void run() {
		for (int i = 0; i < 10; i++) {
			System.err.println(name+""+i);
		}
		
	}
  public static void main(String[] args) {
	  Test  test=new  Test("test");
	  test.start();
	  Test  test1=new  Test("test2");
	  test1.start();
  }
}
