package self.nesl.komicaviewer.factory;

import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.parser.Parser;
import self.nesl.komicaviewer.parser.komica._2cat._2catThreadParser;
import self.nesl.komicaviewer.parser.komica.sora.SoraThreadParser;
import self.nesl.komicaviewer.request.KThread;
import self.nesl.komicaviewer.request.Request;
import self.nesl.komicaviewer.request.komica._2cat._2catThreadRequest;
import self.nesl.komicaviewer.request.komica.sora.SoraThreadRequest;

public class ThreadFactory implements Factory<KThread> {
    private String url;
    private Request req;

    public ThreadFactory(String url) {
        this.url = url;
    }

    public Request createRequest(Bundle bundle) {
        for (String host : ThreadListFactory.SORA_SET) {
            if (url.contains(host)){
                Log.e("match", "SoraPostListRequest "+url +" " + host);
                req =  SoraThreadRequest.create(url);
            }
        }

        for (String host : ThreadListFactory._2CAT_SET) {
            if (url.contains(host)){
                Log.e("match", "_2catThreadRequest "+url +" " + host);
                req =  _2catThreadRequest.create(url);
            }
        }
        return req;
    }

    @Override
    public KThread parse(String response) {
        Parser<KThread> parser = null;
        for (String host : ThreadListFactory.SORA_SET) {
            if (url.contains(host)){
                Log.e("match", "SoraThreadParser "+url +" " + host);
                parser= new SoraThreadParser(req.getUrl(), Jsoup.parse(response));
            }
        }

        for (String host : ThreadListFactory._2CAT_SET) {
            if (url.contains(host)){
                Log.e("match", "_2catThreadParser "+url +" " + host);
                parser =  new _2catThreadParser(req.getUrl(), Jsoup.parse(response));
            }
        }
        return parser.parse();
    }
}
