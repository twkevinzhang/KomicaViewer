package self.nesl.komicaviewer.parser.komica.sora;

import static self.nesl.komicaviewer.util.ProjectUtils.filterReplies;
import static self.nesl.komicaviewer.util.ProjectUtils.installThreadTag;

import android.util.Pair;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.parser.Parser;
import self.nesl.komicaviewer.request.KThread;

public class SoraThreadParser implements Parser<KThread> {
    private String url;
    private Element root;
    private List<Post> posts;

    public SoraThreadParser(String url, Element source) {
        this.url = url;
        this.root = source;
        posts = new ArrayList<>();
    }

    public KThread parse() {
        return new KThread(parseHead(), parseComments());
    }

    private Post parseHead(){
        return getPostParser(url, root.selectFirst("div.threadpost")).parse();
    }

    private List<Post> parseComments(){
        Element thread = installThreadTag(root.selectFirst("#threads")).selectFirst("div.thread");
        for (Element reply_ele : thread.select("div.reply")) {
            String postId = reply_ele.attr("id").substring(1); // #r12345678
            String postUrl = url + "#r" + postId;
            Post post= getPostParser(postUrl, reply_ele).parse();
            posts.add(post);
        }
        injectCount(posts);
        String threadId = new SoraPostParser.DefaultSoraUrlTool(url).getSoraId();
        removeReplyTo(posts, threadId);
        return posts;
    }

    private static void injectCount(List<Post> posts){
        for (Post post: posts) {
            long count = filterReplies(post.getId(), posts).count();
            post.setReplyCount((int) count);
        }
    }

    private static void removeReplyTo(List<Post> posts, String threadId){
        for (Post post: posts) {
            if(threadId.equals(post.getReplyTo()))
                post.setReplyTo(null);
        }
    }

    protected Parser<Post> getPostParser(String url, Element source) {
        return new SoraPostParser(url, source);
    }
}
