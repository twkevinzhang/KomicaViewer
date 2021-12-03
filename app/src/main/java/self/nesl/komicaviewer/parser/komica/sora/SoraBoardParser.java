package self.nesl.komicaviewer.parser.komica.sora;

import static self.nesl.komicaviewer.util.ProjectUtils.installThreadTag;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.parser.Parser;

public class SoraBoardParser implements Parser<List<Post>> {
    private String url;
    private Element root;
    private List<Post> posts;

    public SoraBoardParser(String url, Element source){
        this.url=url;
        this.root = source;
        posts= new ArrayList<>();
    }

    @Override
    public List<Post> parse() {
        // get post secret name
//        String fsub = getElement().selectFirst("#fsub").attr("name");
//        String fcom = getElement().selectFirst("#fcom").attr("name");

        for (Element thread : getThreads()) {
            Element threadpost = thread.selectFirst("div.threadpost");

            String postId = threadpost.attr("id").substring(1);
            String postUrl = url + SoraPostParser.UrlTool.suffix + postId;
            Post post = getPostParser(postUrl, threadpost).parse();
            setReplyCount(thread, post);
            posts.add(post);
        }
        return posts;
    }

    protected void setReplyCount(Element thread, Post post){
        int replyCount = 0;
        try {
            String s = thread.selectFirst("span.warn_txt2").text();
            s = s.replaceAll("\\D", "");
            replyCount = Integer.parseInt(s);
        } catch (Exception ignored) {
        }
        replyCount += thread.getElementsByClass("reply").size();
        post.setReplyCount(replyCount);
    }

    protected Parser<Post> getPostParser(String url, Element source) {
        return new SoraPostParser(url, source);
    }

    protected Elements getThreads() {
        return installThreadTag(root.selectFirst("#threads")).select("div.thread");
    }
}
