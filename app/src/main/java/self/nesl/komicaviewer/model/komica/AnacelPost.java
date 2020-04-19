package self.nesl.komicaviewer.model.komica;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import self.nesl.komicaviewer.model.Post;

import static self.nesl.komicaviewer.util.util.print;

public class AnacelPost extends Post {
    public AnacelPost() { }

    public AnacelPost(String post_id, Element threadpost) {
        super(post_id, threadpost);

        //get picUrl,thumbnailUrl
        try {
            this.setPicUrls(new String[]{threadpost.selectFirst("a.file-thumb").attr("href")});
            this.setThumbnailUrl(threadpost.selectFirst("img").attr("src"));
        } catch (Exception ignored) {}

        //get title,name,now
        this.setTitle(threadpost.select("span.title").text());
        this.setPoster(threadpost.select("span.name").text());
        this.setTimeStr(threadpost.select("span.now").text());

        //get quote
        this.setQuoteElement(threadpost.selectFirst("div.quote"));
    }

    public void addPost(Element reply_ele){
        String reply_id = reply_ele.selectFirst("span.qlink").text().replace("No.", "");
        AnacelPost reply = new AnacelPost(reply_id, reply_ele);

        Elements eles = reply_ele.select("span.resquote").select("a.qlink");
        if (eles.size() <= 0) {
            // is main
            reply.installPreview(this.getPostId());
            this.addPost(this.getPostId(), reply);

        }else{
            for(Element resquote :eles){
                String resquote_id = resquote.attr("href").replace("#r", "");
                reply.installPreview(resquote_id);
                AnacelPost target= (AnacelPost) this.getPost(resquote_id);
                target.addPost(target.getPostId(),reply);
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