package self.nesl.komicaviewer.model.komica;

import android.content.Context;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import self.nesl.komicaviewer.model.Post;

import static self.nesl.komicaviewer.util.util.parseChiToEngWeek;
import static self.nesl.komicaviewer.util.util.print;

public class SoraPost extends Post {
    private String fsub;
    private String fcom;
    private static final String boardId = "sora";

//    komica.org (
//            [綜合,男性角色,短片2,寫真],
//            [新番捏他,新番實況,漫畫,動畫,萌,車],
//            [四格],
//            [女性角色,歡樂惡搞,GIF,Vtuber],
//            [蘿蔔,鋼普拉,影視,特攝,軍武,中性角色,遊戲速報,飲食,小說,遊戲王,奇幻/科幻,電腦/消費電子,塗鴉王國,新聞,布袋戲,紙牌,網路遊戲]
//            )

    public SoraPost(String post_id, Element thread) {
        super(post_id, thread);

        //get picUrl,thumbnailUrl
        try {
            this.setPicUrls(new String[]{thread.selectFirst("a.file-thumb").attr("href")});
            this.setThumbnailUrl(thread.selectFirst("img").attr("src"));
        } catch (NullPointerException ignored) {
        }

        // now: post_detail = "2019/12/15(日) 10:35:11.776 ID:ivN31vZw"
        try {
            String[] post_detail = thread.selectFirst("div.post-head").selectFirst("span.now").text().split(" ID:");
            this.setTimeStr(post_detail[0]);
            post_detail[0] = parseChiToEngWeek(post_detail[0]);
            this.setTime(new SimpleDateFormat("yyyy/MM/dd(EEE) HH:mm:ss.SSS", Locale.ENGLISH).parse(post_detail[0]));
            this.setPoster(post_detail[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //get quote
        this.setQuoteElement(thread.selectFirst("div.quote"));

        //get title,name,now
        this.setPoster(thread.select("span.name").text());
        String title = thread.select("span.title").text();
        if(title.length()!=0){
            this.setTitle(title);
            installTitle(title);
        }
    }

    @Override
    public String getIntroduction(int words, String[] rank) {
        String ind = getQuote().replaceAll(">>(No\\.)*[0-9]{6,} *(\\(.*\\))*", "");
        ind = ind.replaceAll(">+.+\n", "");
        if (ind.length() > words + 1) {
            ind = ind.substring(0, words + 1) + "...";
        }
        return ind.trim();
    }

    @Override
    public String getUrl() {
        return this.getBoardUrl() + "/pixmicat.php?res=" + getPostId();
    }

    public void addPost(Element reply_ele) {
        String reply_id = reply_ele.selectFirst("span.qlink").text().replace("No.", "");
        SoraPost reply = new SoraPost(reply_id, reply_ele);
        reply.setBoardUrl(this.getBoardUrl());

        Elements eles = reply_ele.select("span.resquote").select("a.qlink");
        if (eles.size() <= 0) {
            // is main
            reply.installPreview(this,this.getPostId());
            this.addPost(this.getPostId(), reply);

        } else {
            for (Element resquote : eles) {
                String resquote_id = resquote.attr("href").replace("#r", "");
                SoraPost target = (SoraPost) this.getPost(resquote_id);
                reply.installPreview(this,resquote_id);
                target.addPost(target.getPostId(), reply);
            }
        }
    }

    private void installPreview(SoraPost parent,String target_id) {
        Post target=target_id.equals(this.getPostId())? this : parent.getPost(target_id);
        String context = String.format(">>%s(%s)<br>",target_id, target.getIntroduction(10, null));
        this.getQuoteElement().prepend("<font color=#2bb1ff>" + context);
//        print("this");
        Elements resquote=this.getQuoteElement().select("span.resquote");
        resquote.next("br").remove();
        resquote.remove();
    }

    private void installTitle(String title) {
        this.getQuoteElement().prepend(String.format("[%s]<br>",title));
    }
}