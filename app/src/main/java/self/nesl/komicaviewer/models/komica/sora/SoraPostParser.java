package self.nesl.komicaviewer.models.komica.sora;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import self.nesl.komicaviewer.models.Parser;
import self.nesl.komicaviewer.models.po.Post;
import self.nesl.komicaviewer.util.UrlUtils;


import static self.nesl.komicaviewer.util.Utils.parseChiToEngWeek;
import static self.nesl.komicaviewer.util.Utils.parseJpnToEngWeek;
import static self.nesl.komicaviewer.util.ProjectUtils.parseTime;
import static self.nesl.komicaviewer.util.Utils.print;

public class SoraPostParser extends Parser {
//    komica.org (
//            [綜合,男性角色,短片2,寫真],
//            [新番捏他,新番實況,漫畫,動畫,萌,車],
//            [四格],
//            [女性角色,歡樂惡搞,GIF,Vtuber],
//            [蘿蔔,鋼普拉,影視,特攝,軍武,中性角色,遊戲速報,飲食,小說,遊戲王,奇幻/科幻,電腦/消費電子,塗鴉王國,新聞,布袋戲,紙牌,網路遊戲]
//            )

    public String boardUrl;
    public static String path="/pixmicat.php?res=";

    public SoraPostParser(Element element,String postUrl) {
        super(new Post(postUrl,getId(postUrl)),element);
        this.post.setShow(this.post.getShow().selectFirst("div.quote"));
    }

    protected SoraPostParser(Post post,Element element) {
        super(post,element);
        this.post.setShow(this.post.getShow().selectFirst("div.quote"));
    }

    public SoraPostParser(Element element,String boardUrl,String postId) {
        super(new Post(boardUrl+path+postId,postId),element);
        this.boardUrl=boardUrl;
        this.post.setShow(this.post.getShow().selectFirst("div.quote"));
    }

    private static String getId(String postUrl){
        String temp=postUrl.substring(postUrl.indexOf(path)+path.length());
        String r="#r";
        if(postUrl.contains(r)){
            temp=temp.substring(temp.indexOf(r)+r.length());
        }
        return temp;
    }

    public Post parse() {
        setPicture();
        setDetail();
        setQuote();
        installPicture();
        setTitle();
        setDescription();
        return post;
    }

    public void setDetail() {
        try {
            installDefaultDetail();
        } catch (NullPointerException e) {
            try {
                install2catDetail();
            } catch (NullPointerException | StringIndexOutOfBoundsException e2) {
                installAnimeDetail();
            }
        }
    }

    public void setPicture() {
        try {
            Element thumbImg = post.getOrigin().selectFirst("img");
            String originalUrl = thumbImg.parent().attr("href");
            String url=new UrlUtils(originalUrl,new UrlUtils(boardUrl,null).getHasProtocolHost()).getUrl();
            post.setPictureUrl(url);
        } catch (NullPointerException ignored) {
        }
    }

    public void installDefaultDetail() { // 綜合: https://sora.komica.org
        post.setTitle(post.getOrigin().select("span.title").text());
        String[] post_detail = post.getOrigin().selectFirst("div.post-head span.now").text().split(" ID:");
        post.setTime(parseTime(parseChiToEngWeek(post_detail[0].trim())));
        post.setPoster(post_detail[1]);
    }

    public void install2catDetail() { // 新番捏他: https://2cat.komica.org/~tedc21thc/new
        Element detailEle = post.getOrigin().selectFirst(String.format("label[for='%s']", post.getPostId()));
        Element titleEle = detailEle.selectFirst("span.title");
        if (titleEle != null) {
            post.setTitle(titleEle.text().trim());
            titleEle.remove();
        }

        String s = detailEle.text().trim();
        String[] post_detail = s.substring(1, s.length() - 1).split(" ID:");
        post.setTime(parseTime(parseJpnToEngWeek(post_detail[0].trim())));
        post.setPoster(post_detail[1]);
    }

    public void installAnimeDetail() { // 動畫: https://2cat.komica.org/~tedc21thc/anime/ 比起 2cat.komica 沒有label[for="3273507"]
        String detailStr = post.getOrigin().ownText();
        detailStr = detailStr.length() == 0 ? post.getOrigin().text() : detailStr;
        String[] post_detail = detailStr.split(" ID:");
        post.setTime(parseTime(parseChiToEngWeek(post_detail[0].substring(post_detail[0].indexOf("[") + 1).trim())));
        post.setPoster(post_detail[1].substring(0, post_detail[1].indexOf("]")));
    }

    public void addPost(Element reply_ele) {
        String reply_id = reply_ele.selectFirst(".qlink").text().replace("No.", "");

        SoraPostParser replyParser = null;
        try {
            replyParser=getClass().getDeclaredConstructor(new Class[]{
                    Element.class,String.class
            }).newInstance(
                    reply_ele,post.getUrl()+"#r"+reply_id
            );
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Elements eles = reply_ele.select("span.resquote a.qlink");
        if (eles.size() <= 0) {
            // is main
            replyParser.installPreview(post, post.getPostId());
            post.addPost(post.getPostId(), replyParser.parse());

        } else {
            for (Element resquote : eles) {
                String resquote_id = resquote.attr("href").replace("#r", "");
                Post target = post.getPost(resquote_id);
                replyParser.installPreview(post, resquote_id);
                target.addPost(target.getPostId(), replyParser.parse());
            }
        }
    }

    void installPreview(Post parent, String target_id) {
        Post target = target_id.equals(post.getPostId()) ? post : parent.getPost(target_id);
        String context = String.format(">>%s(%s)<br>", target_id, target.getDescription(10));
        post.getShow().prepend("<font color=#2bb1ff>" + context);
        Elements resquote = post.getShow().select("span.resquote");
        resquote.next("br").remove();
        resquote.remove();
    }

    public void setTitle() {
        String title = post.getTitle(0);
        if (title!=null && title.length() > 0) {
            post.getShow().prepend(String.format("[%s]<br>", title));
        }
    }

    public void setQuote() {
        post.setQuote(post.getOrigin().selectFirst("div.quote"));
    }

    public void installPicture() {
        String url = post.getPictureUrl();
        if (url != null) {
            if(url.endsWith(".webm")){
                post.getShow().append(MessageFormat.format("<br><a href=\"{0}\">{0}</a><br><video src=\"{0}\" type=\"video/webm\">",url));
            }else{
                post.getShow().append(MessageFormat.format("<br><a href=\"{0}\">{0}</a><br><img src=\"{0}\">",url));
            }

        }
    }

    public void setDescription() {
        String ind = post.getQuoteText().replaceAll(">>(No\\.)*[0-9]{6,} *(\\(.*\\))*", "");
        ind = ind.replaceAll(">+.+\n", "");
        post.setDescription(ind.trim());
    }
}
