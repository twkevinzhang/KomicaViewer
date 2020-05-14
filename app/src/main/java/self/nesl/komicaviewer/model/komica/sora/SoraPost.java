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
import static self.nesl.komicaviewer.util.Utils.getStyleMap;
import static self.nesl.komicaviewer.util.ProjectUtils.installThreadTag;
import static self.nesl.komicaviewer.util.Utils.parseChiToEngWeek;
import static self.nesl.komicaviewer.util.Utils.parseJpnToEngWeek;
import static self.nesl.komicaviewer.util.ProjectUtils.parseTime;
import static self.nesl.komicaviewer.util.Utils.print;

public class SoraPost extends Post{
    private String fsub;
    private String fcom;

    public static final String COLUMN_BOARD_URL="board_url";
    public static final String COLUMN_POST_ID="post_id";
    public static final String COLUMN_THREAD="thread";

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
                bundle.getString(COLUMN_BOARD_URL),
                bundle.getString(COLUMN_POST_ID),
                new Element("<html>").html(bundle.getString(COLUMN_THREAD))
        ).parse();
    }

    public SoraPost parse(){
        setPictures();

        try {
            installDefaultDetail();
        } catch (NullPointerException e) {
            print(new Object(){}.getClass(),"use install2catDetail()");
            install2catDetail();
        }
        setQuote();
        setTitle();
        return this;
    }

    private SoraPost(String boardUrl,String post_id, Element thread) {
        super(boardUrl, post_id,thread);
        this.setUrl(boardUrl + "/pixmicat.php?res=" + post_id);
    }

    public void setPictures(){
        //get picUrl,thumbnailUrl
        try {
            Element thumbImg=getPostEle().selectFirst("img");
            this.addPic(new Picture(
                    thumbImg.parent().attr("href"),
                    thumbImg.attr("src"),
                    super.getBoardUrl(),
                    0,
                    0,
                    Integer.parseInt(getStyleMap(thumbImg.attr("style")).get("width")[0].replace("px","")),
                    Integer.parseInt(getStyleMap(thumbImg.attr("style")).get("height")[0].replace("px",""))
            ));
        } catch (NullPointerException ignored) {
        }
    }

    public void installDefaultDetail(){
        this.setTitle(getPostEle().select("span.title").text());
        String[] post_detail = getPostEle().selectFirst("div.post-head span.now").text().split(" ID:");
        this.setTime(parseTime(parseChiToEngWeek(post_detail[0].trim())));
        this.setPoster(post_detail[1]);
    }

    public void install2catDetail(){
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

    public void addPost(Element reply_ele,SoraPost postModel) {
        String reply_id = reply_ele.selectFirst(".qlink").text().replace("No.", "");

        Bundle bundle =new Bundle();
        bundle.putString(COLUMN_BOARD_URL,this.getBoardUrl());
        bundle.putString(COLUMN_POST_ID,reply_id);
        bundle.putString(COLUMN_THREAD,reply_ele.html());
        SoraPost reply = postModel.newInstance(bundle);

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

    @Override
    public void download(Bundle bundle, OnResponse onResponse) {
        download(bundle, onResponse,this);
    }

    public void download(Bundle bundle, OnResponse onResponse,SoraPost postModel) {
        print(new Object(){}.getClass(),"AndroidNetworking",getUrl());
        AndroidNetworking.get(getUrl()).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                Element thread= installThreadTag(Jsoup.parse(response).body().getElementById("threads")).selectFirst("div.thread");
                Element threadpost = thread.selectFirst("div.threadpost");
                // 語言缺陷
                // https://stackoverflow.com/questions/508639/why-must-delegation-to-a-different-constructor-happen-first-in-a-java-constructo
                // this(boardUrl,threadpost.attr("id").substring(1), threadpost);


                Bundle bundle =new Bundle();
                bundle.putString(COLUMN_BOARD_URL,getBoardUrl());
                bundle.putString(COLUMN_POST_ID,threadpost.attr("id").substring(1));
                bundle.putString(COLUMN_THREAD,threadpost.html());
                SoraPost subPost = postModel.newInstance(bundle);
                for (Element reply_ele : thread.select("div.reply")) {
                    subPost.addPost(reply_ele,postModel);
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