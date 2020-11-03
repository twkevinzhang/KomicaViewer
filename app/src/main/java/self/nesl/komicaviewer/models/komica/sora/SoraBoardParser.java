package self.nesl.komicaviewer.models.komica.sora;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.InvocationTargetException;

import self.nesl.komicaviewer.models.Parser;
import self.nesl.komicaviewer.models.po.Post;

import static self.nesl.komicaviewer.util.ProjectUtils.installThreadTag;
import static self.nesl.komicaviewer.util.Utils.print;

public class SoraBoardParser extends Parser {
    private String boardUrl;

    public SoraBoardParser(String boardUrl, Element element) {
        super(new Post(boardUrl,boardUrl),element);
        this.boardUrl=boardUrl;
    }

    // maybe override
    public Class<? extends Parser> getPostParserClass() {
        return SoraPostParser.class;
    }

    public Post parse() {
        //get post secret name
//        String fsub = getElement().selectFirst("#fsub").attr("name");
//        String fcom = getElement().selectFirst("#fcom").attr("name");

        post.setTitle(post.getOrigin().selectFirst("title").text());

        for (Element thread : getThreads()) {
            Element threadpost = thread.selectFirst("div.threadpost");

            String postId = threadpost.attr("id").substring(1);
            Post post=null;
            try {
                post= getPostParserClass().getDeclaredConstructor(new Class[]{
                        Element.class,String.class,String.class
                }).newInstance(
                        threadpost,boardUrl,postId
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
