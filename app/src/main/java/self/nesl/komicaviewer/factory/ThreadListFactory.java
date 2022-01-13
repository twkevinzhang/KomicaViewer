package self.nesl.komicaviewer.factory;

import android.os.Bundle;

import org.jsoup.Jsoup;

import java.util.Arrays;
import java.util.List;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.parser.Parser;
import self.nesl.komicaviewer.parser.komica._2cat._2catBoardParser;
import self.nesl.komicaviewer.parser.komica.sora.SoraBoardParser;
import self.nesl.komicaviewer.request.Request;
import self.nesl.komicaviewer.request.komica._2cat._2catRequest;
import self.nesl.komicaviewer.request.komica.sora.SoraThreadListRequest;

public class ThreadListFactory implements Factory<List<Post>> {
    static List<String> SORA_SET = Arrays.asList(
        "komica.org",  // 綜合、新番捏他、動畫
        "komica.dbfoxtw.me",  // 人外
        "anzuchang.com",  // Idolmaster
        "komica.yucie.net", // 格鬥遊戲
        "kagaminerin.org", // 3D STG
        "strange-komica.com",  // 魔物獵人
        "gzone-anime.info", // TYPE-MOON
        "komica2.net" // komica2
    );

    static List<String> _2CAT_SET = Arrays.asList(
        "2cat.org"  // 碧藍幻想
    );

    private String url;
    private Request request;

    public ThreadListFactory(String url) {
        this.url = url;
    }

    public Request createRequest(Bundle bundle) {
        for (String host : SORA_SET) {
            if (url.contains(host))
                request =  SoraThreadListRequest.create(url, bundle);
        }

        for (String host : _2CAT_SET) {
            if (url.contains(host))
                request =  _2catRequest.create(url, bundle);
        }

        return request;
    }

    @Override
    public List<Post> parse(String response) {
        Parser<List<Post>> parser = null;
        for (String host : SORA_SET) {
            if (url.contains(host)){
                String boardUrl = new SoraThreadListRequest.UrlTool(request.getUrl()).removePageQuery();
                parser= new SoraBoardParser(boardUrl, Jsoup.parse(response));
            }
        }

        for (String host : _2CAT_SET) {
            if (url.contains(host)){
                String boardUrl = new _2catRequest.UrlTool(request.getUrl()).removePageQuery();
                parser= new _2catBoardParser(boardUrl, Jsoup.parse(response));
            }
        }

        return parser.parse();
    }
}
