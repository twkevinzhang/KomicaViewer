package self.nesl.komicaviewer.repository;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.factory.Factory;
import self.nesl.komicaviewer.request.KThread;
import self.nesl.komicaviewer.request.OnResponse;
import self.nesl.komicaviewer.factory.ThreadFactory;

public class ThreadRepository implements Repository<KThread> {
    private Factory<KThread> factory;

    public ThreadRepository(String url){
        this.factory= new ThreadFactory(url);
    }

    @Override
    public void get(OnResponse<KThread> onResponse) {
        factory.createRequest(null).fetch(response -> {
            onResponse.onResponse(factory.parse(response));
        });
    }
}
