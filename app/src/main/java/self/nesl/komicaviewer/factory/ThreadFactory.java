package self.nesl.komicaviewer.factory;

import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;

import self.nesl.komicaviewer.parser.Parser;
import self.nesl.komicaviewer.parser.komica._2cat._2catThreadParser;
import self.nesl.komicaviewer.parser.komica.sora.SoraThreadParser;
import self.nesl.komicaviewer.request.DefaultRequest;
import self.nesl.komicaviewer.models.KThread;
import self.nesl.komicaviewer.request.Request;

public class ThreadFactory implements Factory<KThread> {
    private String url;
    private Request req;

    public ThreadFactory(String url) {
        this.url = url;
    }

    public Request createRequest(Bundle bundle) {
        req =  new DefaultRequest(url);
        return req;
    }

    @Override
    public KThread parse(String response) {
        Parser<KThread> parser = null;
        for (String host : ThreadListFactory.SORA_SET) {
            if (url.contains(host)){
                Log.e("ThreadFactory", "matched SoraThreadParser, "+url +" " + host);
                parser= new SoraThreadParser(req.getUrl(), Jsoup.parse(response));
            }
        }

        for (String host : ThreadListFactory._2CAT_SET) {
            if (url.contains(host)){
                Log.e("ThreadFactory", "matched _2catThreadParser, "+url +" " + host);
                parser =  new _2catThreadParser(req.getUrl(), Jsoup.parse(response));
            }
        }
        return parser.parse();
    }
}
