package self.nesl.komicaviewer.model.komica;

import android.content.Context;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.util.UrlUtil;

import static self.nesl.komicaviewer.util.ProjectUtil.installThreadTag;

public class SoraBoard extends Post {
    private String fsub;
    private String fcom;
    private Context context;

    public SoraBoard(){}

    public SoraBoard(Document doc,String url){
        String host=new UrlUtil(url).getHost();
        this.setPostId(host);
        this.setPostEle(doc);

        //get post secret name
        fsub = doc.getElementById("fsub").attr("name");
        fcom = doc.getElementById("fcom").attr("name");

        Element threads=installThreadTag(doc.body().getElementById("threads"));
        for (Element thread : threads.select("div.thread")) {
            Element threadpost=thread.selectFirst("div.threadpost");
            SoraPost post=new SoraPost(url,threadpost.attr("id").substring(1), threadpost);

            //get replyCount
            int replyCount=0;
            try {
                String s=thread.selectFirst("span.warn_txt2").text();
                s = s.replaceAll("\\D", "");
                replyCount= Integer.parseInt(s);
            } catch (Exception ignored) {}
            replyCount += thread.getElementsByClass("reply").size();
            post.setReplyCount(replyCount);

            this.addPost(host, post);
        }
    }

    @Override
    public String getIntroduction(int words, String[] rank) {
        return getQuoteElement().text().trim();
    }

    @Override
    public SoraBoard parseDoc(Document document, String url) {
        return new SoraBoard(document,url);
    }

    @Override
    public String getUrl() {
        return getBoardUrl();
    }

}