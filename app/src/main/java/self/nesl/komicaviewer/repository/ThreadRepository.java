package self.nesl.komicaviewer.repository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import self.nesl.komicaviewer.db.dao.PostDao;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.factory.Factory;
import self.nesl.komicaviewer.request.KThread;
import self.nesl.komicaviewer.request.OnResponse;
import self.nesl.komicaviewer.factory.ThreadFactory;

public class ThreadRepository implements Repository<KThread> {
    private Factory<KThread> factory;
    private PostDao dao;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public ThreadRepository(String url, PostDao dao){
        this.factory= new ThreadFactory(url);
        this.dao= dao;
    }

    @Override
    public void get(OnResponse<KThread> onResponse) {
        factory.createRequest(null).fetch(response -> {
            try {
                KThread thread = factory.parse(response);
                Post head = thread.getHeadPost();
                head.setReadAt(System.currentTimeMillis());
                mExecutor.execute(() -> dao.insertItems(head));
                onResponse.onResponse(thread);
            }catch (NullPointerException exception){
                onResponse.onResponse(null);
            }
        });
    }
}
