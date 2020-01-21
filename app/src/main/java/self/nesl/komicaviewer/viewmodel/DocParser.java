package self.nesl.komicaviewer.viewmodel;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;

import self.nesl.komicaviewer.model.Board;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.Web;

public class DocParser {


    public ArrayList<Post> tableToKomicaPostlist(Document doc, Board board) {
        ArrayList<Post> postlist = new ArrayList<Post>();

        for (Element row : doc.selectFirst("table").getElementsByTag("tr")) {
            Elements td_eles = row.getElementsByTag("td");

            //如果不是標題列
            if (td_eles.size() <= 0) continue;

            //如果有多的列(新番實況版的刪除勾選列)
            int index = 0;
            if (td_eles.size() >= 6) index += 1;

            String post_id = td_eles.get(index).text();
            String title = td_eles.get(index + 1).text();
            String name = td_eles.get(index + 2).text();

            String s = td_eles.get(index + 3).text();
            int replyCount = 0;
            if (s.length() != 0) {
                replyCount = Integer.parseInt(s);
            }

            s = td_eles.get(index + 4).text();
            String post_time_str = null;
            String poster_id = null;

            if (s.length() != 0) {
                String[] arr = s.split("ID:");
                post_time_str = arr[0];
                poster_id = arr[1];
            }


            Post post = new Post(post_id);
            post.setTitle(title)
                    .setPosterName(name)
                    .setReplyCount(replyCount)
                    .setTimeStr(post_time_str)
                    .setPoster_id(poster_id)
                    .setBoard(board);
            postlist.add(post);
        }
        return postlist;
    }

    public ArrayList<Post> jsonToKomicaPostlist(JSONArray json_arr, Board board) throws JSONException {
        // json_arr="[{"id":"3358865","no":"974434","res_count":0,"sub":"✠ 無標題 ✠","com":"✠ 無內文 ✠","name":"加賴fb852","time":"2019-12-19 01:54:18","th":"https:\/\/imgs.moe\/h.jpg","src":"https:\/\/imgs.moe\/h.jpg","posts":[]},{"id":"3358842","no":"974432","res_count":0,"sub":"明天要去看","com":"明天要去看","name":"✠ 無名 ✠","time":"2019-12-18 22:34:33","th":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/thumb\/1576679673298s.jpg","src":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/src\/1576679673298.jpg","posts":[]},{"id":"3358841","no":"974431","res_count":0,"sub":"十點整 呀哈囉","com":"十點整 呀哈囉","name":"✠ 無名 ✠","time":"2019-12-18 22:17:08","th":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/thumb\/1576678627984s.jpg","src":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/src\/1576678627984.jpg","posts":[]},{"id":"3358834","no":"974429","res_count":0,"sub":"https:\/\/video.twimg.com\/ext_tw_v...","com":"https:\/\/video.twimg.com\/ext_tw_video\/1203470318490","name":"✠ 無名 ✠","time":"2019-12-18 21:12:46","th":null,"src":null,"posts":[]},{"id":"3358826","no":"974428","res_count":0,"sub":"是誰舉報害影片消失了","com":"是誰舉報害影片消失了","name":"✠ 無名 ✠","time":"2019-12-18 18:22:13","th":null,"src":null,"posts":[]},{"id":"3358803","no":"974427","res_count":3,"sub":"真的很好笑 有人老愛挑骨架的毛病講","com":"真的很好笑 有人老愛挑骨架的毛病講\n我用自己的邏輯去分析人體結構 \n定出規則加以遵守 只花了三天就練","name":"✠ 無名 ✠","time":"2019-12-19 15:51:46","th":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/thumb\/1576636908202s.jpg","src":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/src\/1576636908202.jpg","posts":[{"com":"✠ 無內文 ✠","name":"✠ 無名 ✠","th":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/thumb\/1576676744215s.jpg","src":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/src\/1576676744215.jpg"},{"com":"骨架就是一門學問沒錯　但每次有人跟我說骨架我就很火大　\n我總是不明白原因　氣的原因就是　難道這些人就","name":"✠ 無名 ✠","th":null,"src":null},{"com":"最新！","name":"✠ 無名 ✠","th":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/thumb\/1576741905899s.jpg","src":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/src\/1576741905899.jpg"}]},{"id":"3358794","no":"974426","res_count":0,"sub":"沒人愛你","com":"沒人愛你","name":"✠ 無名 ✠","time":"2019-12-18 07:49:38","th":null,"src":null,"posts":[]},{"id":"3358767","no":"974425","res_count":0,"sub":"✠ 無標題 ✠","com":"✠ 無內文 ✠","name":"✠ 無名 ✠","time":"2019-12-17 23:23:38","th":null,"src":null,"posts":[]},{"id":"3358764","no":"974424","res_count":0,"sub":"十點整 呀哈囉","com":"十點整 呀哈囉","name":"✠ 無名 ✠","time":"2019-12-17 22:37:05","th":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/thumb\/1576593424995s.jpg","src":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/src\/1576593424995.jpg","posts":[]},{"id":"3358708","no":"974423","res_count":0,"sub":"✠ 無標題 ✠","com":"✠ 無內文 ✠","name":"✠ 無名 ✠","time":"2019-12-16 22:35:28","th":null,"src":null,"posts":[]},{"id":"3358706","no":"974422","res_count":0,"sub":"十點整 大家晚上好","com":"十點整 大家晚上好","name":"✠ 無名 ✠","time":"2019-12-16 22:21:06","th":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/thumb\/1576506066264s.jpg","src":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/src\/1576506066264.jpg","posts":[]},{"id":"3358663","no":"974421","res_count":0,"sub":"連漫畫版也一堆5毛了,留言態度26式侵略性","com":"連漫畫版也一堆5毛了,留言態度26式侵略性\n\n內容則清一色說西方古代多差,古代華夏多好..","name":"✠ 無名 ✠","time":"2019-12-15 22:48:35","th":null,"src":null,"posts":[]},{"id":"3358648","no":"974420","res_count":0,"sub":"台湾デート+ LINE fb742","com":"台湾デート+ LINE fb742\nTyphoon outside friendship overn","name":"台湾デート+ LINE fb742","time":"2019-12-15 19:53:54","th":"https:\/\/imgs.moe\/h.jpg","src":"https:\/\/imgs.moe\/h.jpg","posts":[]},{"id":"3358640","no":"974419","res_count":0,"sub":"ひみつの小学生","com":"✠ 無內文 ✠","name":"✠ 無名 ✠","time":"2019-12-15 14:52:59","th":null,"src":null,"posts":[]},{"id":"3358622","no":"974418","res_count":0,"sub":"小番茄極品專線加賴fb852","com":"小番茄極品專線加賴fb852\n全台外約S全套優惠多P加節伴遊\n外送範圍：台北-林口-台中-新竹-彰化","name":"加賴fb852","time":"2019-12-15 04:05:54","th":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/thumb\/1576353953629s.jpg","src":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/src\/1576353953629.jpg","posts":[]},{"id":"3358611","no":"974417","res_count":0,"sub":"十點整 大家晚上好","com":"十點整 大家晚上好","name":"✠ 無名 ✠","time":"2019-12-14 23:34:11","th":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/thumb\/1576337651057s.jpg","src":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/src\/1576337651057.jpg","posts":[]},{"id":"3358608","no":"974416","res_count":0,"sub":"成功的遊戲需要有潮讚的ost","com":"成功的遊戲需要有潮讚的ost","name":"✠ 無名 ✠","time":"2019-12-14 23:23:51","th":null,"src":null,"posts":[]},{"id":"3358552","no":"974412","res_count":0,"sub":"十點整 呀哈囉","com":"十點整 呀哈囉","name":"✠ 無名 ✠","time":"2019-12-13 22:32:53","th":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/thumb\/1576247573724s.jpg","src":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/src\/1576247573724.png","posts":[]},{"id":"3358515","no":"974411","res_count":0,"sub":"✠ 無標題 ✠","com":"✠ 無內文 ✠","name":"✠ 無名 ✠","time":"2019-12-13 14:10:15","th":null,"src":null,"posts":[]},{"id":"3358467","no":"974398","res_count":1,"sub":"十點整 大家晚上好","com":"十點整 大家晚上好","name":"✠ 無名 ✠","time":"2019-12-13 01:19:02","th":"https:\/\/imgs.moe\/h.jpg","src":"https:\/\/imgs.moe\/h.jpg","posts":[{"com":"好 再見","name":"✠ 無名 ✠","th":null,"src":null}]},{"id":"3358460","no":"974397","res_count":0,"sub":"加賴fb852**技術特別好","com":"加賴fb852**技術特別好\n好消息~桃子出來~老點趕快\n人妻奶水單親媽媽\/F奶\n桃子 162\/50","name":"加賴fb852","time":"2019-12-12 22:10:31","th":"https:\/\/imgs.moe\/h.jpg","src":"https:\/\/imgs.moe\/h.jpg","posts":[]},{"id":"3358343","no":"974396","res_count":0,"sub":"家中最狂「隱藏武器」！平常是桌子 拆開變球棒＋盾牌","com":"2019-11-28 19:33聯合報 記者劉小川／台北即時報導\n\n周星馳電影裡面說，折凳是七大武器","name":"https:\/\/udn.com\/news\/story\/7270\/4193713","time":"2019-12-12 06:50:00","th":null,"src":null,"posts":[]},{"id":"3358329","no":"974395","res_count":0,"sub":"✠ 無標題 ✠","com":"✠ 無內文 ✠","name":"✠ 無名 ✠","time":"2019-12-11 22:21:09","th":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/thumb\/1576074069201s.jpg","src":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/src\/1576074069201.jpg","posts":[]},{"id":"3358328","no":"974394","res_count":0,"sub":"十點整 大家晚上好","com":"十點整 大家晚上好","name":"✠ 無名 ✠","time":"2019-12-11 22:00:45","th":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/thumb\/1576072845051s.jpg","src":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/src\/1576072845051.jpg","posts":[]},{"id":"3358287","no":"974393","res_count":0,"sub":"各位島民們~一同加入開大車當媽寶的行列吧","com":"各位島民們~一同加入開大車當媽寶的行列吧","name":"✠ 無名 ✠","time":"2019-12-11 10:25:41","th":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/thumb\/1576031141812s.jpg","src":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/src\/1576031141812.jpg","posts":[]},{"id":"3358281","no":"974392","res_count":0,"sub":"瓜最新","com":"瓜最新","name":"✠ 無名 ✠","time":"2019-12-11 08:43:16","th":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/thumb\/1576024996555s.jpg","src":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/src\/1576024996555.jpg","posts":[]},{"id":"3358271","no":"974391","res_count":0,"sub":"十點整 大家晚上好","com":"十點整 大家晚上好","name":"✠ 無名 ✠","time":"2019-12-10 22:00:29","th":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/thumb\/1575986429279s.jpg","src":"https:\/\/mymoe.b-cdn.net\/my\/alleyneblade\/queensblade\/src\/1575986429279.jpg","posts":[]},{"id":"3358163","no":"974388","res_count":0,"sub":"十點整 呀哈囉","com":"十點整 呀哈囉","name":"✠ 無名 ✠","time":"2019-12-09 22:19:52","th":"https:\/\/imgs.moe\/h.jpg","src":"https:\/\/imgs.moe\/h.jpg","posts":[]}]"

        ArrayList<Post> postlist = new ArrayList<Post>();
        for (int i = 0; i < json_arr.length(); i++) {
            JSONObject o = json_arr.getJSONObject(i);
            Post post = new Post(o.getString("no"));

            post.setReplyCount(Integer.parseInt(o.getString("res_count")))
                    .setId2(o.getString("id"))
                    .setTitle(o.getString("sub"))
                    .setQuoteHtml(o.getString("com"))
                    .setTimeStr(o.getString("time"))
                    .setThumbnailUrl(o.getString("th"))
                    .setPicUrl(o.getString("src"))
                    .setBoard(board);

            postlist.add(post);
        }
        return postlist;
    }

    public Post func2(Element thread,Board board) {
        //傳入標準格式(綜合 threads)
        Post post = null;

        Element threadpost = thread.getElementsByClass("threadpost").first();

        //declare
        String post_id = threadpost.attr("id").substring(1);
        String pic_url = null;
        String tumb_url = null;
        String user_id = null;

        //get pic_url
        Element ele = threadpost.selectFirst("a.file-thumb");
        if (ele != null) {
            pic_url = ele.attr("href");
        }

        //get tumb_url
        ele = threadpost.selectFirst("img");
        if (ele != null) {
            tumb_url = ele.attr("src");
        }


        //get title,name
        String title = threadpost.select("span.title").text();
        String name = threadpost.select("span.name").text();

        //get post detail
//            ele=post_head.select("span.now").first();
//            String post_detail = null;
//            if(ele!=null){
//                post_detail = ele.text();
//                //post_detail = "2019/12/15(日) 10:35:11.776 ID:ivN31vZw"
//                post_detail=post_detail.replace("\\((.+)\\)"," ");
//                Log.e("DP","post_detail:"+post_detail);
//
//
//                String[] post_detail_arr= post_detail.split(" ");
//                String post_time_str = post_detail_arr[0].substring(0,10)+" "+post_detail_arr[1];
//                user_id = post_detail_arr[2].substring(3);
//                SimpleDateFormat ft = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
//                try {
//                    post_time=ft.parse(post_time_str);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }

        //get quote
        String quote_html = thread.getElementsByClass("quote").first().html();

        //get replyCount
        int replyCount = 0;
        ele = threadpost.select("span.warn_txt2").first();
        if (ele != null) {
            String s = ele.text();
            s = s.replaceAll("\\D","");
            replyCount = Integer.parseInt(s);
        }
        replyCount += thread.getElementsByClass("reply").size();

        try {
            post = new Post(post_id)
                    .setBoard(board)
                    .setTitle(title)
                    .setPosterName(name)
                    .setPoster_id(user_id)
                    .setPicUrl(pic_url)
                    .setThumbnailUrl(tumb_url)
                    .setQuoteHtml(quote_html)
                    .setReplyCount(replyCount);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return post;
    }

    public Element addThreadTag(Element threads) {
        //將thread加入threads中，變成標準綜合版樣式
        Element thread = threads.appendElement("div").addClass("thread");
        for (Element div : threads.children()) {
            thread.appendChild(div);
            if (div.tagName().equals("hr")) {
                threads.appendChild(thread);
                thread = threads.appendElement("div").addClass("thread");
            }
        }
        return threads;
    }

    public ArrayList<Post> homepageToKomicaPostlist(Document doc, Board board) {
        ArrayList<Post> postlist = new ArrayList<Post>();
        Element threads = doc.getElementById("threads");

        //如果找不到thread標籤，就是2cat，要用addThreadTag()改成標準綜合版樣式
        if (threads.getElementsByClass("thread").first() == null) {
            Log.e("DP", "thread is null");
            threads = addThreadTag(threads);
        }

        //2cat
        //將label替換成div.post-head (loss date&time)
//        for (Element thread2 : threads.getElementsByClass("thread")) {
//            Element threadpost = thread2.getElementsByClass("threadpost").first();
//            Element post_head=threadpost.appendElement("div").addClass("post-head");
//            for(Element ele: threadpost.getElementsByTag("label").first().children()){
//                post_head.appendChild(ele);
//            }
//        }

        for (Element thread : threads.getElementsByClass("thread")) {
            postlist.add(func2(thread,board));
        }
        return postlist;
    }

    public ArrayList<Post> searchResultToKomicaPostlist(Document doc, Board board) {
        ArrayList<Post> postlist = new ArrayList<Post>();

        for (Element row : doc.getElementById("search_result").getElementsByClass("threadpost")) {
            String title=row.selectFirst("span.title").text();
            String name=row.selectFirst("span.name").text();
            String post_id=row.selectFirst("a").text();
            String quote_html=row.selectFirst("div.quote").html();

            Post post = new Post(post_id);
            post.setTitle(title)
                    .setPosterName(name)
                    .setQuoteHtml(quote_html)
                    .setBoard(board);
            postlist.add(post);
        }
        return postlist;
    }

    public Post toKomicaPost(Document doc,Board board) {
        Element threadpost = doc.getElementById("threads").getElementsByClass("threadpost").first();
        Elements replies = doc.getElementById("threads").getElementsByClass("reply");

        //declare
        String post_id = threadpost.attr("id").substring(1);
        Date post_time = null;
        String pic_url = null;
        String user_id = null;
        String title = null;
        String name = null;
        String quote = null;
        ArrayList<Post> replies_arr = new ArrayList<Post>();
        ArrayList<Post> allreplies_arr = new ArrayList<Post>();

        //get pic_url
        Element ele = threadpost.getElementsByTag("img").first();
        if (ele != null){
            pic_url = ele.parent().attr("href");
            if(pic_url==null || pic_url.length()<=0)pic_url=ele.attr("src");
        }

        //get title,name
        title = threadpost.select("span.title").text();
        name = threadpost.select("span.name").text();

        //get post detail
//            ele=post_head.select("span.now").first();
//            String post_detail = null;
//            if(ele!=null){
//                post_detail = ele.text();
//                //post_detail = "2019/12/15(日) 10:35:11.776 ID:ivN31vZw"
//                post_detail=post_detail.replace("\\((.+)\\)"," ");
//                Log.e("DP","post_detail:"+post_detail);
//
//
//                String[] post_detail_arr= post_detail.split(" ");
//                String post_time_str = post_detail_arr[0].substring(0,10)+" "+post_detail_arr[1];
//                user_id = post_detail_arr[2].substring(3);
//                SimpleDateFormat ft = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
//                try {
//                    post_time=ft.parse(post_time_str);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }

        //get quote
        String quote_html = threadpost.getElementsByClass("quote").first().html();
        quote = Jsoup.clean(quote_html, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));

        //get replies
        for (Element reply_ele : replies) {
            String reply_id = reply_ele.getElementsByClass("qlink").first().text().replace("No.", "");
            Post reply = new Post(reply_id);

            // reply pic
            String reply_picurl = null;
            ele = reply_ele.getElementsByTag("img").first();
            if (ele != null) reply_picurl = ele.parent().attr("href");

            // reply quote
            String reply_quote_html = reply_ele.selectFirst("div.quote").html();
            String reply_quote = Jsoup.clean(reply_quote_html, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false)).replace("&gt;", ">");

            reply.setQuoteHtml(reply_quote_html)
                    .setPicUrl(reply_picurl)
                    .setBoard(board);

            // 如果reply有target
            try {
                // 如果沒有找到任何qlink，將會回傳「size為0」的可迭代Elements，不會拋出NullPointerException()
                Elements eles=reply_ele.select("span.resquote").select("a.qlink");
                if(eles.size()<=0)throw new NullPointerException();
                for (Element reply_target : eles) {
                    String reply_target_id = reply_target.attr("href").replace("#r", "");
                    Post target = getTarget(replies_arr, reply_target_id);

                    // 回應目標內文預覽
                    String context = reply_target.text()+" ("+target.getIntroduction(10,null)+") ";
                    reply_target.text("");
                    reply_target.prepend("<font color=#2bb1ff>"+context);

                    reply_quote_html = reply_ele.selectFirst("div.quote").html();
                    reply.setQuoteHtml(reply_quote_html);

                    if(reply_target_id.equals(post_id))throw new NullPointerException();

                    replies_arr = addReplyToTarget(replies_arr, reply_target_id, reply);
                }
            } catch (NullPointerException e) {
                replies_arr.add(reply);
            }

            allreplies_arr.add(reply);
        }

        return new Post(post_id)
                .setTitle(title)
                .setPosterName(name)
                .setPoster_id(user_id)
                .setPicUrl(pic_url)
                .setQuoteHtml(quote_html)
                .setAllRepliesArr(allreplies_arr)
                .setRepliesArr(replies_arr)
                .setBoard(board);
    }

    static Post getTarget(ArrayList<Post> replies_arr, String reply_target_id) {
        Post targtet = null;
        for (Post reply : replies_arr) {
            if(targtet!=null)break;
            if (reply.getId().equals(reply_target_id)) {
                targtet= reply;
                break;
            } else {
                targtet= getTarget(reply.getReplieArr(), reply_target_id);
            }
        }
        return targtet;
    }

    static ArrayList<Post> addReplyToTarget(ArrayList<Post> replies_arr, String reply_target_id, Post insert_reply) {
        boolean isChanged=false;
        for (Post reply : replies_arr) {
            if(isChanged)break;
            if (reply.getId().equals(reply_target_id)) {
                reply.addReply(insert_reply);
                isChanged=true;
            } else {
                addReplyToTarget(reply.getReplieArr(), reply_target_id, insert_reply);
            }
        }
        return replies_arr;
    }
}
