package com.example.verifydemo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Created by apple on 2018/2/7.
 */

public class ReceVerUrl {
    private String url;
    private String Rurl;

    public ReceVerUrl(String url) {
        this.url = url;
    }
    public String getHtml(String url){
        //解析url获取Document对象
        try{
            org.jsoup.nodes.Document  document = Jsoup.connect(url).get();
            //获取网页源码文本内容
            System.out.println(document.toString());
            //获取指定class的内容指定tag的元素
            //org.jsoup.select.Elements liElements = ((Element) document).getElementsByClass("content").get(0).getElementsByTag("li");
            //org.jsoup.select.Elements liElements = ((Element) document).getElementsByClass("body_ ").get(0).getElementsByTag("script");
            org.jsoup.select.Elements liElements = ((Element) document).getElementsByClass("page-captcha zh").get(0).getElementsByTag("script");
            if(liElements.get(0)==null){
                System.out.println("获取jsul地址失败！");
            }else{
                Rurl=liElements.attr("src").toString();
                //System.out.println(Rurl);
            }
        }catch(IOException e){
            System.out.println("解析出错！");
            e.printStackTrace();
        }catch(IndexOutOfBoundsException e){
            System.out.println("越界异常！");
            //e.printStackTrace();
        }
        return Rurl;
    }

}
