package self.nesl.komicaviewer.model.komica.sora;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.HashMap;

import self.nesl.komicaviewer.model.Host;
import self.nesl.komicaviewer.model.Picture;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.util.UrlUtils;
import self.nesl.komicaviewer.util.Utils;

import static self.nesl.komicaviewer.util.Utils.getStyleMap;
import static self.nesl.komicaviewer.util.ProjectUtils.installThreadTag;
import static self.nesl.komicaviewer.util.Utils.parseChiToEngWeek;
import static self.nesl.komicaviewer.util.Utils.parseJpnToEngWeek;
import static self.nesl.komicaviewer.util.ProjectUtils.parseTime;
import static self.nesl.komicaviewer.util.Utils.print;

public class SoraPost extends Post{
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

    public SoraPost newInstance(Bundle bundle){
       return new SoraPost(
                bundle.getString(COLUMN_POST_URL),
               bundle.getString(COLUMN_POST_ID),
                new Element("<html>").html(bundle.getString(COLUMN_THREAD))
        ).parse();
    }

    public SoraPost parse(){
        try {
            installDetail();
        } catch (NullPointerException e) {
            try {
                install2catDetail();
            }catch (NullPointerException | StringIndexOutOfBoundsException e2){
                installAnimeDetail();
            }
        }

        setQuote();
        setPictures();
        installPictureUrls();
        setTitle();
        return this;
    }

    public SoraPost(String postUrl,String postId, Element thread) {
        super(postUrl,postId,thread);
    }

    public void setPictures(){
        try {

            Element thumbImg=getPostEle().selectFirst("img");
            int[] size = new int[]{0, 0};

            String style=thumbImg.attr("style");
            if(style.length()>0) {
                String[] sizeStrs = new String[]{"width", "height"};
                for (int i = 0; i < 2; i++) {

                    String sizeStr = getStyleMap(style).get(sizeStrs[i])[0];
                    if (sizeStr.contains("px")) {
                        sizeStr = sizeStr.replace("px", "");
                        size[i] = Integer.parseInt(sizeStr);
                    } else if (sizeStr.contains("%")) {

                    }
                }
            }

            UrlUtils urlUtils=new UrlUtils(this.getUrl());

            this.addPic(new Picture(
                    thumbImg.parent().attr("href"),
                    thumbImg.attr("src"),
                    urlUtils.getProtocol()+"://"+urlUtils.getHost(),
                    0,
                    0,
                    size[0],
                    size[1]
            ));
        } catch (NullPointerException ignored) {
        }
    }

    public void installDetail(){ // 綜合: https://sora.komica.org
            this.setTitle(getPostEle().select("span.title").text());
            String[] post_detail = getPostEle().selectFirst("div.post-head span.now").text().split(" ID:");
            this.setTime(parseTime(parseChiToEngWeek(post_detail[0].trim())));
            this.setPoster(post_detail[1]);
    }

    public void install2catDetail(){ // 新番捏他: https://2cat.komica.org/~tedc21thc/new
            Element detailEle=getPostEle().selectFirst(String.format("label[for='%s']", getPostId()));
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

    public void installAnimeDetail(){ // 動畫: https://2cat.komica.org/~tedc21thc/anime/ 比起 2cat 沒有label[for="3273507"]
        String detailStr=getPostEle().ownText();
        detailStr=detailStr.length()==0?getPostEle().text():detailStr;
        String[] post_detail =detailStr.split(" ID:");
        this.setTime(parseTime(parseChiToEngWeek(post_detail[0].substring(post_detail[0].indexOf("[")+1).trim())));
        this.setPoster(post_detail[1].substring(0,post_detail[1].indexOf("]")));
    }

    public void addPost(Element reply_ele) {
        String reply_id = reply_ele.selectFirst(".qlink").text().replace("No.", "");

        Bundle bundle =new Bundle();
        bundle.putString(SoraPost.COLUMN_POST_URL,getUrl());
        bundle.putString(COLUMN_POST_ID,reply_id);
        bundle.putString(COLUMN_THREAD,reply_ele.html());
        SoraPost reply = newInstance(bundle);

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

    public void installPreview(Post parent,String target_id) {
        Post target=target_id.equals(this.getPostId())? this : parent.getPost(target_id);
        String context = String.format(">>%s(%s)<br>",target_id, target.getIntroduction(10, null));
        this.getQuoteElement().prepend("<font color=#2bb1ff>" + context);
        Elements resquote=this.getQuoteElement().select("span.resquote");
        resquote.next("br").remove();
        resquote.remove();
    }

    public void setTitle() {
        String title=this.getTitle(0);
        if(title.length()>0){
            this.getQuoteElement().prepend(String.format("[%s]<br>",title));
        }
    }

    public void setQuote(){
        this.setQuoteElement(getPostEle().selectFirst("div.quote"));
    }

    public void installPictureUrls(){
        for (Picture pic:getPics()) {
            String html="<br><a href=\"{}\">{}</a><img src=\"{}\">"
                    .replace("{}",pic.getOriginalUrl());
            getQuoteElement().append(html);
        }
    }

    @Override
    public void download(Bundle bundle, OnResponse onResponse) {
        String url=getUrl();
        print(this,"AndroidNetworking",url);
        AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                Element thread= installThreadTag(Jsoup.parse(response).body().getElementById("threads")).selectFirst("div.thread");
                Element threadpost = thread.selectFirst("div.threadpost");

                Bundle bundle =new Bundle();
                bundle.putString(SoraPost.COLUMN_POST_URL,url);
                bundle.putString(COLUMN_POST_ID,threadpost.attr("id").substring(1));
                bundle.putString(COLUMN_THREAD,threadpost.html());
                SoraPost subPost = newInstance(bundle);
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

    @Override
    public String getIntroduction(int words, String[] rank) {
        String ind = getQuote().replaceAll(">>(No\\.)*[0-9]{6,} *(\\(.*\\))*", "");
        ind = ind.replaceAll(">+.+\n", "");
        if (words!=0 && ind.length() > words) {
            ind = ind.substring(0, words + 1) + "...";
        }
        return ind.trim();
    }
}