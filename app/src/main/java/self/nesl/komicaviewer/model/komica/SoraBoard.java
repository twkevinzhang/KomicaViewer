package self.nesl.komicaviewer.model.komica;

import android.content.Context;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.MalformedURLException;
import java.net.URL;

import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.util.MyURL;

import static self.nesl.komicaviewer.util.Util.print;

public class SoraBoard extends Post {
    private String fsub;
    private String fcom;
    private Context context;

    public SoraBoard(){}

    public SoraBoard(Document doc,String url){
        String host=new MyURL(url).getHost();
        this.setPostId(host);
        this.setPostEle(doc);

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
            post.setBoardUrl(url);

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
    public String getUrl() {
        return getBoardUrl();
    }

}