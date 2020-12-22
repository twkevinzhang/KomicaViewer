package self.nesl.komicaviewer.request.komica.sora;

import android.os.Bundle;

import org.jsoup.Jsoup;

import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.parser.komica.sora.SoraBoardParser;
import self.nesl.komicaviewer.parser.komica.sora.SoraThreadParser;
import self.nesl.komicaviewer.request.Request;

public class SoraPostListRequest extends Request<List<Post>> {
    public static String POST_ID = "post_id";

    public SoraPostListRequest(String url) {
        super(url);
    }

    public static SoraPostListRequest create(Post thread, Bundle bundle) {
        String url = thread.getUrl();
        return new SoraPostListRequest(url);
    }

    @Override
    public List<Post> parse(String response) {
        return new SoraThreadParser(getUrl(), Jsoup.parse(response)).parse();
    }
}
