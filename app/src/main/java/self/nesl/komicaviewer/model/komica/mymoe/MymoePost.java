package self.nesl.komicaviewer.model.komica.mymoe;

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

import static self.nesl.komicaviewer.util.ProjectUtil.installThreadTag;
import static self.nesl.komicaviewer.util.ProjectUtil.parseTime;
import static self.nesl.komicaviewer.util.Util.getStyleMap;
import static self.nesl.komicaviewer.util.Util.parseChiToEngWeek;
import static self.nesl.komicaviewer.util.Util.print;

public class MymoePost extends Post implements KomicaPost {
    String id2;

    public MymoePost() {
    }


    public MymoePost(String boardUrl, String post_id, Element thread) {
        String[] strs = post_id.split(" ");
        super.setBoardUrl(boardUrl);
        super.setPostId(strs[0]);
        if (strs.length > 1) setId2(strs[1]);

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

        installDetail(thread);

        //get quote
        this.setQuoteElement(thread.selectFirst("div.quote"));

        //get title
        String title=this.getTitle(0);
        if(title.length()>0)installTitle(title);


    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public void installDetail(Element thread){
        this.setTitle(thread.select("span.title").text());
        Element detailEle = thread.selectFirst("span.now");
        this.setTime(parseTime( detailEle.attr("title") ));
        this.setPoster(detailEle.selectFirst("span.trip_id").text().replace("ID:",""));
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
        MymoePost reply = new MymoePost(this.getBoardUrl(), reply_id, reply_ele);

        Elements eles = reply_ele.select("span.resquote a.qlink");
        if (eles.size() <= 0) {
            // is main
            reply.installPreview(this, this.getPostId());
            this.addPost(this.getPostId(), reply);

        } else {
            for (Element resquote : eles) {
                String resquote_id = resquote.attr("href").replace("#r", "");
                MymoePost target = (MymoePost) this.getPost(resquote_id);
                reply.installPreview(this, resquote_id);
                target.addPost(target.getPostId(), reply);
            }
        }
    }

    @Override
    public void installPreview(Post parent, String target_id) {

    }

    @Override
    public void installTitle(String title) {
        this.getQuoteElement().prepend(String.format("[%s]<br>",title));
    }

    @Override
    public void download(Bundle bundle, OnResponse onResponse) {
        print(new Object() {
        }.getClass(), "AndroidNetworking", getUrl());
        AndroidNetworking.get(getUrl()).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                Element thread = installThreadTag(Jsoup.parse(response).body().getElementById("threads"));
                Element threadpost = thread.selectFirst("div.threadpost");
                // 語言缺陷
                // https://stackoverflow.com/questions/508639/why-must-delegation-to-a-different-constructor-happen-first-in-a-java-constructo
                // this(boardUrl,threadpost.attr("id").substring(1), threadpost);
                MymoePost subPost = new MymoePost(getBoardUrl(), threadpost.attr("id").substring(1), threadpost);
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
