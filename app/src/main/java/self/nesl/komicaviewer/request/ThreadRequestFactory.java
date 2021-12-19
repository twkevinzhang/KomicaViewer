package self.nesl.komicaviewer.request;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.request.komica._2cat._2catThreadRequest;
import self.nesl.komicaviewer.request.komica.sora.SoraThreadRequest;

public class ThreadRequestFactory {
    private Post thread;

    public ThreadRequestFactory(Post thread) {
        this.thread = thread;
    }

    public Request<KThread> createRequest(Bundle bundle) {
        Request<KThread> request = null;

        for (String host : ThreadListRequestFactory.SORA_SET) {
            if (thread.getUrl().contains(host)){
                Log.e("match", "SoraPostListRequest "+thread.getUrl() +" " + host);
                request =  SoraThreadRequest.create(thread, bundle);
            }
        }

        for (String host : ThreadListRequestFactory._2CAT_SET) {
            if (thread.getUrl().contains(host)){
                Log.e("match", "_2catThreadRequest "+thread.getUrl() +" " + host);
                request =  _2catThreadRequest.create(thread, bundle);
            }
        }

        return request;
    }
}
