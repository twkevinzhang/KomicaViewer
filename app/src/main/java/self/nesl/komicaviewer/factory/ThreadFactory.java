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
    private Post thread;
    private Request req;

    public ThreadFactory(Post thread) {
        this.thread = thread;
    }

    public Request createRequest(Bundle bundle) {
        for (String host : ThreadListFactory.SORA_SET) {
            if (thread.getUrl().contains(host)){
                Log.e("match", "SoraPostListRequest "+thread.getUrl() +" " + host);
                req =  SoraThreadRequest.create(thread, bundle);
            }
        }

        for (String host : ThreadListFactory._2CAT_SET) {
            if (thread.getUrl().contains(host)){
                Log.e("match", "_2catThreadRequest "+thread.getUrl() +" " + host);
                req =  _2catThreadRequest.create(thread, bundle);
            }
        }
        return req;
    }

    @Override
    public KThread parse(String response) {
        Parser<KThread> parser = null;
        for (String host : ThreadListFactory.SORA_SET) {
            if (thread.getUrl().contains(host)){
                Log.e("match", "SoraThreadParser "+thread.getUrl() +" " + host);
                parser= new SoraThreadParser(req.getUrl(), Jsoup.parse(response));
            }
        }

        for (String host : ThreadListFactory._2CAT_SET) {
            if (thread.getUrl().contains(host)){
                Log.e("match", "_2catThreadParser "+thread.getUrl() +" " + host);
                parser =  new _2catThreadParser(req.getUrl(), Jsoup.parse(response));
            }
        }
        return parser.parse();
    }
}
