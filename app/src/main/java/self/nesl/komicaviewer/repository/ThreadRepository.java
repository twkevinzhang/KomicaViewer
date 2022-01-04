package self.nesl.komicaviewer.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import self.nesl.komicaviewer.db.dao.PostDao;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.factory.Factory;
import self.nesl.komicaviewer.request.KThread;
import self.nesl.komicaviewer.factory.ThreadFactory;
import self.nesl.komicaviewer.request.Request;

public class ThreadRepository implements Repository<KThread> {
    private Factory<KThread> factory;
    private PostDao dao;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public ThreadRepository(String url, PostDao dao){
        this.factory= new ThreadFactory(url);
        this.dao= dao;
    }

    @Override
    public LiveData<KThread> get() {
        Request req= factory.createRequest(null);
        MutableLiveData<KThread> liveData = new MutableLiveData<>();
        if(req != null) {
            req.fetch(response -> {
                try {
                    KThread thread = factory.parse(response);
                    Post head = thread.getHeadPost();
                    head.setReadAt(System.currentTimeMillis());
                    mExecutor.execute(() -> dao.insertItems(head));
                    liveData.postValue(thread);
                } catch (NullPointerException exception) {
                    liveData.postValue(null);
                }
            });
        }else{
            liveData.postValue(null);
        }
        return liveData;
    }
}
