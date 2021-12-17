package self.nesl.komicaviewer.parser.komica._2cat;

import org.jsoup.nodes.Element;

import static self.nesl.komicaviewer.util.Utils.print;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.parser.Parser;
import self.nesl.komicaviewer.parser.komica.sora.SoraPostParser;
import self.nesl.komicaviewer.parser.komica.sora.SoraThreadParser;

public class _2catThreadParser implements Parser<List<Post>> {
    private String url;
    private Element root;
    private List<Post> posts;

    public _2catThreadParser(String url, Element source) {
        this.url = url;
        this.root = source;
        posts = new ArrayList<>();
    }

    @Override
    public List<Post> parse() {
        List<Post> list= new ArrayList<>();
        for (Element reply_ele : root.select("div[class=\"reply\"][id^='r']")) {
            String replyId = reply_ele.id(); // r123456
            String url1 = url + "#" + replyId;
            list.add(new _2catPostParser(url1, reply_ele).parse());
        }
        return list;
    }
}
