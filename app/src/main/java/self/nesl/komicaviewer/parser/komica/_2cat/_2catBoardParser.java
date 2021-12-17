package self.nesl.komicaviewer.parser.komica._2cat;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static self.nesl.komicaviewer.util.Utils.print;

import java.util.List;
import java.util.regex.Pattern;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.parser.komica.sora.SoraBoardParser;
import self.nesl.komicaviewer.parser.komica.sora.SoraPostParser;

public class _2catBoardParser extends SoraBoardParser {

    public _2catBoardParser(String boardUrl, Element element) {
        super(boardUrl, element);
    }

    @Override
    public List<Post> parse() {
        // get post secret name
//        String fsub = getElement().selectFirst("#fsub").attr("name");
//        String fcom = getElement().selectFirst("#fcom").attr("name");

        for (Element thread : getThreads()) {
            Element threadpost = thread.selectFirst("div.threadpost");

            String postId = threadpost.attr("id").substring(1);
            String postUrl = url + "/?res=" + postId;
            Post post = parsePost(postUrl, threadpost);
            setReplyCount(thread, post);
            posts.add(post);
        }
        return posts;
    }

    @Override
    protected Post parsePost(String url, Element source) {
        return new _2catPostParser(url, source).parse();
    }

    @Override
    public Elements getThreads(){
        return root.select("div.threadpost");
    }
}
