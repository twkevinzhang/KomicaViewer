package self.nesl.komicaviewer.model.komica;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

import self.nesl.komicaviewer.model.Picture;
import self.nesl.komicaviewer.model.Post;

import static self.nesl.komicaviewer.util.Util.getStyleMap;
import static self.nesl.komicaviewer.util.Util.installThreadTag;
import static self.nesl.komicaviewer.util.Util.parseChiToEngWeek;
import static self.nesl.komicaviewer.util.Util.parseJpnToEngWeek;
import static self.nesl.komicaviewer.util.Util.parseTime;
import static self.nesl.komicaviewer.util.Util.print;

public class SoraPost extends Post {
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
        super(post_id, thread);
        this.setBoardUrl(boardUrl);

        //get picUrl,thumbnailUrl
        try {
            Element imgEle=thread.selectFirst("img");
            String thumbUrl = imgEle.attr("src");

            print(null,thumbUrl);
            this.addPic(new Picture(
                    new StringBuilder(thumbUrl).deleteCharAt(thumbUrl.lastIndexOf(".")-1).toString(),
                    thumbUrl,
                    boardUrl,
                    0,
                    0,
                    Integer.parseInt(getStyleMap(imgEle.attr("style")).get("width")[0].replace("px","")),
                    Integer.parseInt(getStyleMap(imgEle.attr("style")).get("height")[0].replace("px",""))
            ));
        } catch (NullPointerException ignored) {
        }

        // now: post_detail = "2019/12/15(日) 10:35:11.776 ID:ivN31vZw"
        String[] post_detail;
        try {
            installDefaultDetail(thread);
        } catch (NullPointerException e) {
            install2catDetail(thread,post_id);
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

    void installDefaultDetail(Element thread){
        String[] post_detail = thread.selectFirst("div.post-head span.now").text().split(" ID:");
        this.setTime(parseTime(parseChiToEngWeek(post_detail[0].trim())));
        this.setPoster(post_detail[1]);
    }

    void install2catDetail(Element thread,String post_id){
        String[] post_detail=new String[5];
        Element detailEle=thread.selectFirst(String.format("label[for='%s']", post_id));
        Element titleEle=detailEle.selectFirst("span.title");
        if(titleEle!=null){
            this.setTitle(titleEle.text().trim());
            titleEle.remove();
        }
        // s = "[20/04/30(木)18:15 ID:CRz.V7Mw/Zplu]"
        String s=detailEle.text().trim();
        post_detail=s.substring(1,s.length()-1).split(" ID:");
        this.setTime(parseTime(parseJpnToEngWeek(post_detail[0].trim())));
        this.setPoster(post_detail[1]);
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

    private void installPreview(SoraPost parent,String target_id) {
        Post target=target_id.equals(this.getPostId())? this : parent.getPost(target_id);
        String context = String.format(">>%s(%s)<br>",target_id, target.getIntroduction(10, null));
        this.getQuoteElement().prepend("<font color=#2bb1ff>" + context);
        Elements resquote=this.getQuoteElement().select("span.resquote");
        resquote.next("br").remove();
        resquote.remove();
    }

    private void installTitle(String title) {
        this.getQuoteElement().prepend(String.format("[%s]<br>",title));
    }

    public SoraPost parseDoc(Document doc,String boardUrl) {
        Element thread= installThreadTag(doc.body().getElementById("threads")).selectFirst("div.thread");
        Element threadpost = thread.selectFirst("div.threadpost");
        SoraPost subPost = new SoraPost(boardUrl,threadpost.attr("id").substring(1), threadpost);
        for (Element reply_ele : thread.select("div.reply")) {
            subPost.addPost(reply_ele);
        }
        return subPost;
    }
}