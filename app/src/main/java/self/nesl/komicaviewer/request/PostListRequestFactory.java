package self.nesl.komicaviewer.request;

import android.os.Bundle;

import java.util.List;

import self.nesl.komicaviewer.models.category.Category;
import self.nesl.komicaviewer.models.category.KomicaCategory;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.request.komica.sora.SoraPostListRequest;

public class PostListRequestFactory {
    private Post thread;

    public PostListRequestFactory(Post thread) {
        this.thread = thread;
    }

    public Request<List<Post>> createRequest(Bundle bundle) {
        Request<List<Post>> request = null;
        if (thread.getUrl().contains("sora")) {
            request = SoraPostListRequest.create(thread, bundle);
        }
        return request;
    }
}
