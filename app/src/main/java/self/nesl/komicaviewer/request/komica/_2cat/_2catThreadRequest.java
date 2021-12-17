package self.nesl.komicaviewer.request.komica._2cat;
import android.os.Bundle;

import org.jsoup.Jsoup;

import java.util.List;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.parser.komica._2cat._2catThreadParser;
import self.nesl.komicaviewer.parser.komica.sora.SoraThreadParser;
import self.nesl.komicaviewer.request.Request;
import self.nesl.komicaviewer.request.komica.sora.SoraPostListRequest;

public class _2catThreadRequest extends Request<List<Post>> {

    public static _2catThreadRequest create(Post thread, Bundle bundle) {
        String url = thread.getUrl();
        return new _2catThreadRequest(url);
    }

    public _2catThreadRequest(String url) {
        super(url);
    }

    @Override
    public List<Post> parse(String response) {
        return new _2catThreadParser(getUrl(), Jsoup.parse(response)).parse();
    }
}
