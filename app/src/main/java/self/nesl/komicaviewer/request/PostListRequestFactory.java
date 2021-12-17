package self.nesl.komicaviewer.request;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import self.nesl.komicaviewer.models.category.Category;
import self.nesl.komicaviewer.models.category.KomicaCategory;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.request.komica._2cat._2catThreadListRequest;
import self.nesl.komicaviewer.request.komica._2cat._2catThreadRequest;
import self.nesl.komicaviewer.request.komica.sora.SoraPostListRequest;
import self.nesl.komicaviewer.request.komica.sora.SoraThreadListRequest;

public class PostListRequestFactory {
    private Post thread;

    public PostListRequestFactory(Post thread) {
        this.thread = thread;
    }

    public Request<List<Post>> createRequest(Bundle bundle) {
        Request<List<Post>> request = null;

        for (String host : ThreadListRequestFactory.SORA_SET) {
            if (thread.getUrl().contains(host)){
                Log.e("match", "SoraPostListRequest "+thread.getUrl() +" " + host);
                request =  SoraPostListRequest.create(thread, bundle);
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
