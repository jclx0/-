package com.zaj;
//用jsoup解析Url、文件、字符串
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
//使用了爬虫框架WebMagic
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Selectable;

import java.io.BufferedWriter;//字符缓冲输出流
import java.io.FileWriter;//按字符向流中写入数据
import java.io.IOException;//IO异常
import java.time.LocalDate;
import java.util.*;
/**
 * @author zaj
 * @date 2021/1/21
 */
public class test implements  PageProcessor {
    LocalDate of = LocalDate.of(2020, 04, 20);


    public static String url = "https://s.weibo.com/weibo?q=%E6%96%B0%E5%86%A0%E7%96%AB%E6%83%85&typeall=1&suball=1&timescope=custom:2019-12-12-0:";//请求url
    public static int maxpage = 8000; //标识最大页数

    public static FileWriter wr = null;
    public static BufferedWriter out = null;
    static {
        try {
            wr = new FileWriter("疫情2.txt");//文件保存路径
        } catch (IOException e) {
            e.printStackTrace();//异常捕获
        }
        out = new BufferedWriter(wr);
    }
    public static int count = 1; //当前获取了多少数据
    public static int thispage = 1171; //标识当前请求到第几页

    @Override
    public void process(Page page) {
        int j =0;
        List<Selectable> list = page.getHtml().css("#pl_feedlist_index div.card-wrap").nodes();
        //获取当前页面所有帖子
        for(Selectable crad:list){
            Document doc = Jsoup.parse(crad.toString());
            //获取编号 获取发帖人
            String persion = doc.select("a.name").first().attr("nick-name");
            System.out.println("编号："+count+"发帖人："+persion);
            //获取发帖人微博
            String weibo = crad.css("div[class=avator]").links().get();
            //获取帖子内容
            String text = doc.select("p[node-type=feed_list_content]").text();
            String text_full = doc.select("p[node-type=feed_list_content_full]").text();
            String content = text_full.equals("")?text:text_full;
            //获取时间
            String time = doc.select("p[class=from]").first().text();
            //获取相关（点赞数、转发数、评论数）
            String zan = doc.select("div[class=card-act]").text();


            try {
                out.write("编号："+count);
                out.newLine();
                out.write("作者："+persion);
                out.newLine();
                out.write("链接："+weibo);
                out.newLine();
                out.write("内容："+content);
                out.newLine();
                out.write("时间："+time);
                out.newLine();
                out.write("相关："+zan);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            count++;
        }
        j+=1;
        int k =0;
        //在第一页请求完成后把第2-200页加入到任务队列
        //这个1171是因为之前爬到一半微博瘫痪了，链接断了，所以之后接着爬的时候从1171开始
		//if语句用来变化时间
		if(thispage==1171){
            for(int i =1172;i<=maxpage;i++){

                if(i%300 ==0){
                   of = of.plusDays(-(k+=1));
                }
                page.addTargetRequest(url+of+"-21&Refer=g&page"+i);
            }
        }
        System.out.println("第"+thispage+"页 请求完成");
        thispage++;
        //每请求一次 随机让线程休眠0-5秒
        Random random = new Random();
        int anInt = random.nextInt(5);
        try {
            Thread.sleep(anInt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private Site site = Site.me()
            .setCharset("utf8")   //设置编码
            .setTimeOut(10*1000)  //设置超时时间，单位是ms
            .setRetrySleepTime(3000) //设置重试的间隔时间
            .setRetryTimes(3)  //设置重试的次数
            //下面的内容参考了https://juejin.cn/post/6844904084080689165里的思想
			//添加抓包获取的cookie信息                  SINAGLOBAL                  2469238537631.08.1599038421965
            .addCookie(".weibo.com", "SINAGLOBAL","2469238537631.08.1599038421965")
            .addCookie(".weibo.com", "_s_tentry","-")
            .addCookie(".weibo.com", "Apache","4018340941764.511.1611214448955")
            .addCookie(".weibo.com", "ULV","1611214448960:2:1:1:4018340941764.511.1611214448955:1609043529782")
            .addCookie("s.weibo.com", "WBtopGlobal_register_version","2021012115")
            .addCookie(".weibo.com", "login_sid_t","eceeb23e84de1f397fe82ec4816f7a5e")
            .addCookie(".weibo.com", "cross_origin_proto","SSL")
            .addCookie(".weibo.com", "UOR","www.google.com,weibo.com,www.baidu.com")
            .addCookie(".weibo.com", "ALF","1642751143")
            .addCookie(".weibo.com", "wvr","6")
            .addCookie(".weibo.com", "SSOLoginState","1611283681")
            .addCookie(".weibo.com", "SUB","_2A25NDkyxDeRhGeFL7lAY9inPyzmIHXVu8VT5rDV8PUJbkNANLXD1kW1NfeMWijGZ_I55GNV7zH4m9j_eRd8Kdk00")
            .addCookie(".weibo.com", "SUBP","0033WrSXqPxfM725Ws9jqgMF55529P9D9WFfFSa1")
            .addCookie("s.weibo.com", "WBStorage","8daec78e6a891122|undefined")
            .addCookie(".weibo.com", "webim_unReadCount","%7B%22time%22%3A1611290371066%2C%22dm_pub_total%22%3A3%2C%22chat_group_client%22%3A0%2C%22chat_group_notice%22%3A0%2C%22allcountNum%22%3A5%2C%22msgbox%22%3A0%7D")
            //添加请求头，伪装浏览器请求
            .addHeader("User-Agent",
                    "ozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80" +
                            " Safari/537.36 Core/1.47.516.400 QQBrowser/9.4.8188.400")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
            .addHeader("Accept-Encoding", "gzip, deflate, sdch")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
            .addHeader("Connection", "keep-alive")
            .addHeader("Referer", "https://s.weibo.com");
    ;
    @Override
    public Site getSite() {
        return site;
    }
    //主函数，执行爬虫
    public static void main(String[] args) {
        //请求微博关键词搜索界面第一页
        Spider.create(new test())
                .addUrl(url+1)
                // .addPipeline(new ExcelPipeline())
                // .thread(5)  //表示开启5个线程来完成任务
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(1000*1000)))//设置布隆过滤器，最多对100w数据进行去重
                .run();
    }





}
