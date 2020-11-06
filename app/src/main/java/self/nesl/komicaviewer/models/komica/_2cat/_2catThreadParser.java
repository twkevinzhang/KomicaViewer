package self.nesl.komicaviewer.models.komica._2cat;

import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.models.po.Post;
import self.nesl.komicaviewer.models.komica.sora.SoraThreadParser;
import self.nesl.komicaviewer.util.UrlUtils;

import static self.nesl.komicaviewer.util.Utils.print;

public class _2catThreadParser extends SoraThreadParser {

    public _2catThreadParser(Element element, String postUrl, String postId) {
        super(element,postUrl,postId);
    }

    @Override
    public Post parse() {
        Element threadpost = post.getOrigin().selectFirst("div.reply.threadpost");
        _2catPostParser subPost = new _2catPostParser(threadpost, new UrlUtils(url).getLastPathSegment(), threadpost.attr("id").substring(1));
        for (Element reply_ele : post.getOrigin().select("div[class=\"reply\"][id^='r']")) {
            subPost.addPost(reply_ele);
        }
        return subPost.parse();
    }
}
