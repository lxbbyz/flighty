package com.flighty.projectDirectory.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * 描述：爬虫测试
 *
 * @author zhoushan
 * @create 2019/05/11 0:06
 */
public class Reptile {

    public static void main(String[] args)throws Exception {
//        Document documentDemo = (Document) getMain("https://91lz.com/");

        //提取网页链接
//        Elements scriptElements = documentDemo.getElementsByTag("a");
//        for(Element link:scriptElements){
//            String XqHref = link.attr("href");
//            if(XqHref.indexOf("xiangqing")>0){
//                System.out.println("提取出的链接网页详情链接-->"+XqHref);
//            }
//        }

        Document documentDemo = (Document) postMain("https://91lz.com/restful/pcArticle/articleDetail");
        System.out.println("爬到的数据-->"+documentDemo.toString());
//        Elements scriptDemo = documentDemo.getElementsByTag("script");

    }

    //发送获取数据url(GET)
    public static Object getMain(String url) throws IOException {
        Connection tempConn = Jsoup.connect(url);
        //模拟浏览器的请求头
        tempConn.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
        //开始连接HTTP请求。
        Connection.Response demo = tempConn.ignoreContentType(true).method(Connection.Method.GET)
                .execute();
        Document documentDemo = demo.parse();

        return documentDemo;
    }

    //发送获取数据url(POST)
    public static Object postMain(String url) throws IOException {
        Connection tempConn = Jsoup.connect(url);
        //模拟浏览器的请求头
        tempConn.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
        tempConn.data("id","c945feefd2cc424ba5a5d05573935aba");
        //开始连接HTTP请求。
        Connection.Response demo = tempConn.ignoreContentType(true).method(Connection.Method.POST)
                .execute();
        Document documentDemo = demo.parse();

        return documentDemo;
    }
}
