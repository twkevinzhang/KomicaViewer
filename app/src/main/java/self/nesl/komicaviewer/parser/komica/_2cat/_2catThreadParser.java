package self.nesl.komicaviewer.parser.komica._2cat;

import org.jsoup.nodes.Element;

import static self.nesl.komicaviewer.util.Utils.print;

import java.util.ArrayList;
import java.util.List;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.parser.Parser;
import self.nesl.komicaviewer.models.KThread;

public class _2catThreadParser implements Parser<KThread> {
    private String url;
    private Element root;

    public _2catThreadParser(String url, Element source) {
        this.url = url;
        this.root = source;
    }

    @Override
    public KThread parse() {
        return new KThread(parseHead(), parseReplies());
    }

    private Post parseHead(){
        return new _2catPostParser(url, root.selectFirst("div.threadpost")).parse();
    }

    private List<Post> parseReplies(){
        List<Post> list= new ArrayList<>();
        for (Element reply_ele : root.select("div[class=\"reply\"][id^='r']")) {
            String replyId = reply_ele.id(); // r123456
            String url1 = url + "#" + replyId;
            list.add(new _2catPostParser(url1, reply_ele).parse());
        }
        return list;
    }
}
