package self.nesl.komicaviewer.model.komica;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.util.util;

import static self.nesl.komicaviewer.util.util.print;

public class SoraPost extends Post {
    private String fsub;
    private String fcom;
    private static final String boardId = "sora";

    public SoraPost() {
    }

    public SoraPost(String post_id, Element thread) {
        super(post_id, thread);

        //get picUrl,thumbnailUrl
        try {
            this.setPicUrls(new String[]{thread.selectFirst("a.file-thumb").attr("href")});
            this.setThumbnailUrl(thread.selectFirst("img").attr("src"));
        } catch (Exception ignored) {}

        //get title,name,now
        this.setTitle(thread.select("span.title").text());
        this.setPoster(thread.select("span.name").text());

        // post_detail = "2019/12/15(日) 10:35:11.776 ID:ivN31vZw"
        try {
            String[] post_detail = thread.selectFirst("div.post-head").selectFirst("span.now").text().split(" ID:");
            this.setTimeStr(post_detail[0]);
            this.setPoster(post_detail[1]);
        } catch (Exception ignored) {}

        //get quote
        this.setQuoteElement(thread.selectFirst("div.quote"));
    }

    public SoraPost(Document doc) {
        super(boardId, doc);
        //get post secret name
        fsub = doc.getElementById("fsub").attr("name");
        fcom = doc.getElementById("fcom").attr("name");

        Element threads = doc.getElementById("threads");

        //如果找不到thread標籤，就是2cat.komica.org，要用addThreadTag()改成標準綜合版樣式
        if (threads.selectFirst("div.thread") == null) {
            print("thread is null");
            //將thread加入threads中，變成標準綜合版樣式
            Element thread = threads.appendElement("div").addClass("thread");
            for (Element div : threads.children()) {
                thread.appendChild(div);
                if (div.tagName().equals("hr")) {
                    threads.appendChild(thread);
                    thread = threads.appendElement("div").addClass("thread");
                }
            }
        }

        for (Element thread : threads.select("div.thread")) {
            Element threadpost=thread.selectFirst("div.threadpost");
            SoraPost post=new SoraPost(threadpost.attr("id").substring(1), threadpost);

            //get replyCount
            int replyCount=0;
            try {
                String s=thread.selectFirst("span.warn_txt2").text();
                s = s.replaceAll("\\D", "");
                replyCount= Integer.parseInt(s);
            } catch (Exception ignored) {}
            replyCount += thread.getElementsByClass("reply").size();
            post.setReplyCount(replyCount);

            this.addPost(boardId, post);
        }
    }

    public void addPost(Element reply_ele) {
        String reply_id = reply_ele.selectFirst("span.qlink").text().replace("No.", "");
        SoraPost reply = new SoraPost(reply_id, reply_ele);

        Elements eles = reply_ele.select("span.resquote").select("a.qlink");
        if (eles.size() <= 0) {
            // is main
            reply.installPreview(this.getPostId());
            this.addPost(this.getPostId(), reply);

        } else {
            for (Element resquote : eles) {
                String resquote_id = resquote.attr("href").replace("#r", "");
                reply.installPreview(resquote_id);
                SoraPost target = (SoraPost) this.getPost(resquote_id);
                target.addPost(target.getPostId(), reply);
            }
        }
    }

    @Override
    public String getIntroduction(int words, String[] rank) {
        String ind = getQuote().trim().replaceAll(">>(No\\.)*[0-9]{6,} *(\\(.*\\))*", "");
        ind = ind.replaceAll(">+.+\n", "");
        if (ind.length() > words + 1) {
            ind = ind.substring(0, words + 1) + "...";
        }
        return ind.trim();
    }

    private void installPreview(String target_id) {
        Post target;
        if (target_id.equals(this.getPostId())) {
            target = this;
        } else {
            target = this.getPost(target_id);
        }

        try {
            Element resquote = this.getQuoteElement().selectFirst("a.qlink");
            String context = resquote.text() + " (" + target.getIntroduction(10, null) + ") ";
//          quote_ele.text("");
            resquote.prepend("<font color=#2bb1ff>" + context);
            print("installPreview! target: " + target_id);
        } catch (NullPointerException ignored) {
        }
    }
}