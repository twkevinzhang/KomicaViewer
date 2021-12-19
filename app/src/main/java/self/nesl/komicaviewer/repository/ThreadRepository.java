package self.nesl.komicaviewer.repository;

import java.util.List;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.request.KThread;
import self.nesl.komicaviewer.request.OnResponse;
import self.nesl.komicaviewer.request.Request;
import self.nesl.komicaviewer.request.ThreadRequestFactory;

public class ThreadRepository implements Repository<KThread> {
    private Request<KThread> request;

    public ThreadRepository(Post threadInfo){
        this.request= new ThreadRequestFactory(threadInfo).createRequest(null);;
    }

    @Override
    public void get(OnResponse<KThread> onResponse) {
        request.fetch(onResponse);
    }
}
