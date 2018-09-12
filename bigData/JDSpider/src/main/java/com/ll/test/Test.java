package com.ll.test;


import java.util.HashMap;
import java.util.Map;

import com.ll.test.util.HttpClientUtils;

public class Test {
  public static void main(String[] args) {
	  
	 String  url="https://search.jd.com/s_new.php?keyword=手机&enc=utf-8&wq=%E6%89%8B%E6%9C%BA&page=1&scrolling=y";
	 try {
		Map header=new HashMap();
		//header.put("cookie", "xtest=2215.cf6b6759; ipLoc-djd=1-72-2799-0; __jda=122270672.1536723199595888157505.1536723200.1536723200.1536723200.1; __jdb=122270672.1.1536723199595888157505|1.1536723200; __jdc=122270672; __jdv=122270672|direct|-|none|-|1536723199597; shshshfpb=18a901d12b13c46a8a16395eedeb996754526c615c06568d95b9777f1e; __jdu=1536723199595888157505; shshshfp=81efbe503845dcb0ecf5e436d342c435; shshshfpa=b966b4f7-5c9b-edd2-124c-1b9dae9cbb24-1536723199; shshshsID=57d73eeae801828f2ae7d378825cc45c_1_1536723199946; qrsc=1; rkv=V0600; 3AB9D23F7A4B3C9B=NKJ2EVPBWQLRFPOBVR2KTDJZ5JG7RJ5ICJR2NTRUYVCGHHSSRJAQE63AAYV6JTSE4QHMRTPM5PB7K7PRBIWYE56AEI");
		header.put("referer", "https://search.jd.com/Search");
		header.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
		String result=HttpClientUtils.sendGet(url, header, null);
		System.err.println(result);
	} catch (Exception e) {
		// TODO: handle exception
	}
  }
}

