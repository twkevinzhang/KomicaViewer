package self.nesl.komicaviewer.parser;

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

public class DocToPostParser {
    public DocToPostParser() {
    }

    public Post toPost(Document doc, Board board) {
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

        //get pic_url
        Element ele = threadpost.getElementsByTag("img").first();
        if (ele != null) {
            pic_url = ele.parent().attr("href");
            if (pic_url == null || pic_url.length() <= 0) pic_url = ele.attr("src");
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

            // reply title
            title = reply_ele.select("span.title").text();
            name = reply_ele.select("span.name").text();
            String now=reply_ele.select("span.now").text();

            reply.setQuoteHtml(reply_quote_html)
                    .setPicUrl(reply_picurl)
                    .setTimeStr(now)
                    .setTitle(title)
                    .setPosterName(name)
                    .setBoard(board);

            // 如果reply有target
            try {
                // 如果沒有找到任何qlink，將會回傳「size為0」的可迭代Elements，不會拋出NullPointerException()
                Elements eles = reply_ele.select("span.resquote").select("a.qlink");
                if (eles.size() <= 0) throw new NullPointerException();
                for (Element reply_target : eles) {
                    String reply_target_id = reply_target.attr("href").replace("#r", "");
                    Post target = getTarget(replies_arr, reply_target_id);

                    // 回應目標內文預覽
                    String context = reply_target.text() + " (" + target.getIntroduction(10, null) + ") ";
                    reply_target.text("");
                    reply_target.prepend("<font color=#2bb1ff>" + context);

                    reply_quote_html = reply_ele.selectFirst("div.quote").html();
                    reply.setQuoteHtml(reply_quote_html);

                    if (reply_target_id.equals(post_id)) throw new NullPointerException();

                    replies_arr = addReplyToTarget(replies_arr, reply_target_id, reply);
                }
            } catch (NullPointerException e) {
                replies_arr.add(reply);
            }
        }

        return new Post(post_id)
                .setTitle(title)
                .setPosterName(name)
                .setPoster_id(user_id)
                .setPicUrl(pic_url)
                .setQuoteHtml(quote_html)
                .setRepliesTree(replies_arr)
                .setBoard(board);
    }

    static Post getTarget(ArrayList<Post> replies_arr, String reply_target_id) {
        Post targtet = null;
        for (Post reply : replies_arr) {
            if (targtet != null) break;
            if (reply.getId().equals(reply_target_id)) {
                targtet = reply;
                break;
            } else {
                targtet = getTarget(reply.getRepliesTree(), reply_target_id);
            }
        }
        return targtet;
    }

    static ArrayList<Post> addReplyToTarget(ArrayList<Post> replies_arr, String reply_target_id, Post insert_reply) {
        boolean isChanged = false;
        for (Post reply : replies_arr) {
            if (isChanged) break;
            if (reply.getId().equals(reply_target_id)) {
                reply.addReply(insert_reply);
                isChanged = true;
            } else {
                addReplyToTarget(reply.getRepliesTree(), reply_target_id, insert_reply);
            }
        }
        return replies_arr;
    }
}
