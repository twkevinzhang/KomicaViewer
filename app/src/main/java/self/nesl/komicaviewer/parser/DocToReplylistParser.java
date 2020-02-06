package self.nesl.komicaviewer.parser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import self.nesl.komicaviewer.model.Board;
import self.nesl.komicaviewer.model.Post;

public class DocToReplylistParser {
    private Element thread;
    private Board board;
    private ArrayList<Post> replies_arr=new ArrayList<>();
    public DocToReplylistParser(Element thread, Board board) {
        this.thread=thread;
        this.board=board;
    }

    public Post toPost() {
        //get main_post
        Element threadpost = thread.getElementById("threads").getElementsByClass("threadpost").first();
        Post main_post=func(threadpost,threadpost.attr("id").substring(1));

        //get replies
        Elements replies = thread.getElementById("threads").getElementsByClass("reply");
        for (Element reply_ele : replies) {
            String reply_id = reply_ele.getElementsByClass("qlink").first().text().replace("No.", "");
            Post reply = func(reply_ele,reply_id);

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

                    reply.setQuoteHtml(reply_ele.selectFirst("div.quote").html());

//                if (reply_target_id.equals(main_post_id)) throw new NullPointerException();

                    replies_arr = addReplyToTarget(replies_arr, reply_target_id, reply);
                }
            } catch (NullPointerException e) {
                replies_arr.add(reply);
            }
        }
        main_post.setReplyTree(replies_arr);
        return main_post;
    }

    static Post getTarget(ArrayList<Post> replies_arr, String reply_target_id) {
        Post targtet = null;
        for (Post reply : replies_arr) {
            if (targtet != null) break;
            if (reply.getId().equals(reply_target_id)) {
                targtet = reply;
                break;
            } else {
                targtet = getTarget(reply.getReplyTree(), reply_target_id);
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
                addReplyToTarget(reply.getReplyTree(), reply_target_id, insert_reply);
            }
        }
        return replies_arr;
    }

    Post func(Element post_ele,String id){
        Post post=new Post(id);
        post.setBoard(board);

        //get pic_url
        String pic_url = null;
        Element ele = post_ele.getElementsByTag("img").first();
        if (ele != null) {
            pic_url = ele.parent().attr("href");
            if (pic_url == null || pic_url.length() <= 0) pic_url = ele.attr("src");
        }
        post.setPicUrl(pic_url);

        //get title,name,now
        post.setTitle(post_ele.select("span.title").text());
        post.setPosterName(post_ele.select("span.name").text());
        post.setTimeStr(post_ele.select("span.now").text());

        //get quote
        String quote_html = post_ele.getElementsByClass("quote").first().html();
        post.setQuoteHtml(quote_html);

        //綜2youtube


        return post;
    }
}
