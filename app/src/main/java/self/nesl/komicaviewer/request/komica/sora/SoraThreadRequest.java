package self.nesl.komicaviewer.request.komica.sora;

import android.os.Bundle;

import org.jsoup.Jsoup;

import java.util.List;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.parser.komica.sora.SoraThreadParser;
import self.nesl.komicaviewer.request.KThread;
import self.nesl.komicaviewer.request.Request;

public class SoraThreadRequest extends Request<KThread> {
    public SoraThreadRequest(String url) {
        super(url);
    }

    public static SoraThreadRequest create(Post thread, Bundle bundle) {
        String url = thread.getUrl();
        return new SoraThreadRequest(url);
    }

    @Override
    public KThread parse(String response) {
        return new SoraThreadParser(getUrl(), Jsoup.parse(response)).parse();
    }
}
