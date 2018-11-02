package com.ll.test.demo;

import com.google.gson.JsonObject;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.plugin.net.OkHttpRequester;
import cn.edu.hfut.dmic.webcollector.util.ExceptionUtils;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class DemoPostCrawler  extends BreadthCrawler {
	/**
     * 
     * 假设我们要爬取三个链接 1)http://www.A.com/index.php 需要POST，并需要POST表单数据username:John
     * 2)http://www.B.com/index.php?age=10 需要POST，数据直接在URL中 ，不需要附带数据 3)http://www.C.com/
     * 需要GET
     */
    public DemoPostCrawler(final String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);

        addSeed(new CrawlDatum("http:192.168.1.139:8080/bdmp")
                .meta("method", "POST"));
       /* addSeed(new CrawlDatum("http://www.B.com/index.php")
                .meta("method", "POST"));
        addSeed(new CrawlDatum("http://www.C.com/index.php")
                .meta("method", "GET"));*/

        setRequester(new OkHttpRequester(){
            @Override
            public Request.Builder createRequestBuilder(CrawlDatum crawlDatum) {
                Request.Builder requestBuilder = super.createRequestBuilder(crawlDatum);
                String method = crawlDatum.meta("method");

                // 默认就是GET方式，直接返回原来的即可
                if(method.equals("GET")){
                    return requestBuilder;
                }

                if(method.equals("POST")){
                    RequestBody requestBody;
                    String username = crawlDatum.meta("account");
                    System.err.println(username);
                    String password=crawlDatum.meta("pwd");
                    System.err.println(password);
                    // 如果没有表单数据username，POST的数据直接在URL中
                    if(username == null){
                        requestBody = RequestBody.create(null, new byte[]{});
                    }else{
                        // 根据meta构建POST表单数据
                        requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("account", username)
                                .addFormDataPart("pwd", password)
                                .build();
                    }
                    return requestBuilder.post(requestBody);
                }

                //执行这句会抛出异常
                ExceptionUtils.fail("wrong method: " + method);
                return null;
            }
        });


    }

//    @Override
//    public Page getResponse(CrawlDatum crawlDatum) throws Exception {
//        HttpRequest request = new HttpRequest(crawlDatum.url());
//
//        request.setMethod(crawlDatum.meta("method"));
//        String outputData = crawlDatum.meta("outputData");
//        if (outputData != null) {
//            request.setOutputData(outputData.getBytes("utf-8"));
//        }
//        return request.responsePage();
//        /*
//        //通过下面方式可以设置Cookie、User-Agent等http请求头信息
//        request.setCookie("xxxxxxxxxxxxxx");
//        request.setUserAgent("WebCollector");
//        request.addHeader("xxx", "xxxxxxxxx");
//         */
//    }

    public void visit(Page page, CrawlDatums next) {
        String jsonObject = page.jsonObject().toString();
        System.out.println("JSON信息：" + jsonObject);
    }

    /**
     *
     * @param args 参数
     * @throws Exception 异常
     */
    public static void main(String[] args) throws Exception {

        DemoPostCrawler crawler = new DemoPostCrawler("json_crawler", true);
        crawler.start(1);
    }

}
