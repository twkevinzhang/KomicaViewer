package self.nesl.komicaviewer.model.komica.sora;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import self.nesl.komicaviewer.model.Picture;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.KomicaPost;

import static self.nesl.komicaviewer.util.Util.getStyleMap;
import static self.nesl.komicaviewer.util.ProjectUtil.installThreadTag;
import static self.nesl.komicaviewer.util.Util.parseChiToEngWeek;
import static self.nesl.komicaviewer.util.Util.parseJpnToEngWeek;
import static self.nesl.komicaviewer.util.ProjectUtil.parseTime;
import static self.nesl.komicaviewer.util.Util.print;

public class SoraPost extends Post implements KomicaPost {
    private String fsub;
    private String fcom;

//    komica.org (
//            [綜合,男性角色,短片2,寫真],
//            [新番捏他,新番實況,漫畫,動畫,萌,車],
//            [四格],
//            [女性角色,歡樂惡搞,GIF,Vtuber],
//            [蘿蔔,鋼普拉,影視,特攝,軍武,中性角色,遊戲速報,飲食,小說,遊戲王,奇幻/科幻,電腦/消費電子,塗鴉王國,新聞,布袋戲,紙牌,網路遊戲]
//            )

    public SoraPost(){}

    public SoraPost(String boardUrl,String post_id, Element thread) {
        super(boardUrl, post_id);
        this.setUrl(boardUrl + "/pixmicat.php?res=" + post_id);

        //get picUrl,thumbnailUrl
        try {
            Element thumbImg=thread.selectFirst("img");
            this.addPic(new Picture(
                    thumbImg.parent().attr("href"),
                    thumbImg.attr("src"),
                    boardUrl,
                    0,
                    0,
                    Integer.parseInt(getStyleMap(thumbImg.attr("style")).get("width")[0].replace("px","")),
                    Integer.parseInt(getStyleMap(thumbImg.attr("style")).get("height")[0].replace("px",""))
            ));
        } catch (NullPointerException ignored) {
        }

        try {
            installDefaultDetail(thread);
        } catch (NullPointerException e) {
            print(new Object(){}.getClass(),"use install2catDetail()");
            install2catDetail(thread,post_id);
        }

        //get quote
        this.setQuoteElement(thread.selectFirst("div.quote"));

        //get title
        String title=this.getTitle(0);
        if(title.length()>0)installTitle(title);
    }

    private void installDefaultDetail(Element thread){
        this.setTitle(thread.select("span.title").text());
        String[] post_detail = thread.selectFirst("div.post-head span.now").text().split(" ID:");
        this.setTime(parseTime(parseChiToEngWeek(post_detail[0].trim())));
        this.setPoster(post_detail[1]);
    }

    public void install2catDetail(Element thread, String post_id){
        Element detailEle=thread.selectFirst(String.format("label[for='%s']", post_id));
        Element titleEle=detailEle.selectFirst("span.title");
        if(titleEle!=null){
            this.setTitle(titleEle.text().trim());
            titleEle.remove();
        }

        String s=detailEle.text().trim();
        String[] post_detail=s.substring(1,s.length()-1).split(" ID:");
        this.setTime(parseTime(parseJpnToEngWeek(post_detail[0].trim())));
        this.setPoster(post_detail[1]);
    }

    @Override
    public String getIntroduction(int words, String[] rank) {
        String ind = getQuote().replaceAll(">>(No\\.)*[0-9]{6,} *(\\(.*\\))*", "");
        ind = ind.replaceAll(">+.+\n", "");
        if (words!=0 && ind.length() > words) {
            ind = ind.substring(0, words + 1) + "...";
        }
        return ind.trim();
    }

    @Override
    public void addPost(Element reply_ele) {
        String reply_id = reply_ele.selectFirst(".qlink").text().replace("No.", "");
        SoraPost reply = new SoraPost(this.getBoardUrl(),reply_id, reply_ele);

        Elements eles = reply_ele.select("span.resquote a.qlink");
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

    @Override
    public void installPreview(Post parent,String target_id) {
        Post target=target_id.equals(this.getPostId())? this : parent.getPost(target_id);
        String context = String.format(">>%s(%s)<br>",target_id, target.getIntroduction(10, null));
        this.getQuoteElement().prepend("<font color=#2bb1ff>" + context);
        Elements resquote=this.getQuoteElement().select("span.resquote");
        resquote.next("br").remove();
        resquote.remove();
    }

    @Override
    public void installTitle(String title) {
        this.getQuoteElement().prepend(String.format("[%s]<br>",title));
    }

    @Override
    public void download(Bundle bundle, OnResponse onResponse) {
        print(new Object(){}.getClass(),"AndroidNetworking",getUrl());
        AndroidNetworking.get(getUrl()).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                Element thread= installThreadTag(Jsoup.parse(response).body().getElementById("threads")).selectFirst("div.thread");
                Element threadpost = thread.selectFirst("div.threadpost");
                // 語言缺陷
                // https://stackoverflow.com/questions/508639/why-must-delegation-to-a-different-constructor-happen-first-in-a-java-constructo
                // this(boardUrl,threadpost.attr("id").substring(1), threadpost);
                SoraPost subPost = new SoraPost(getBoardUrl(),threadpost.attr("id").substring(1), threadpost);
                for (Element reply_ele : thread.select("div.reply")) {
                    subPost.addPost(reply_ele);
                }
                onResponse.onResponse(subPost);
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }
}