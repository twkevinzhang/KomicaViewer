package self.nesl.komicaviewer.parser.komica.sora;

import static self.nesl.komicaviewer.util.ProjectUtils.installThreadTag;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.parser.Parser;

public class SoraBoardParser implements Parser<List<Post>> {
    private String boardUrl;

    public SoraBoardParser(String source) {
        super(new Post(boardUrl, boardUrl), element);
        this.boardUrl = boardUrl;
    }

    public SoraBoardParser(String boardUrl, Element element) {
        super(new Post(boardUrl, boardUrl), element);
        this.boardUrl = boardUrl;
    }

    // maybe override
    public Class<? extends Parser> getPostParserClass() {
        return SoraPostParser.class;
    }

    public List<Post> parse() {
        //get post secret name
//        String fsub = getElement().selectFirst("#fsub").attr("name");
//        String fcom = getElement().selectFirst("#fcom").attr("name");

        post.setTitle(post.getOrigin().selectFirst("title").text());

        for (Element thread : getThreads()) {
            Element threadpost = thread.selectFirst("div.threadpost");

            String postId = threadpost.attr("id").substring(1);
            Post post = null;
            try {
                post = getPostParserClass().getDeclaredConstructor(new Class[]{
                        Element.class, String.class, String.class
                }).newInstance(
                        threadpost, boardUrl, postId
                ).parse();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            //get replyCount
            int replyCount = 0;
            try {
                String s = thread.selectFirst("span.warn_txt2").text();
                s = s.replaceAll("\\D", "");
                replyCount = Integer.parseInt(s);
            } catch (Exception ignored) {
            }
            replyCount += thread.getElementsByClass("reply").size();
            post.setReplyCount(replyCount);

            this.post.addPost(this.post.getPostId(), post);
        }
        return post;
    }

    public Elements getThreads() {
        return installThreadTag(post.getOrigin().selectFirst("#threads")).select("div.thread");
    }
}
