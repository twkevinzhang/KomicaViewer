package self.nesl.komicaviewer.ui.thread;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.Collections;
import java.util.List;

import self.nesl.komicaviewer.db.AppDatabase;
import self.nesl.komicaviewer.db.dao.PostDao;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.repository.Repository;
import self.nesl.komicaviewer.repository.ThreadRepository;
import self.nesl.komicaviewer.request.KThread;
import self.nesl.komicaviewer.ui.SampleViewModel;

public class ThreadViewModel extends SampleViewModel<Post, Post> {
    Boolean isTree = false;
    PostDao dao;
    String threadUrl;
    private MutableLiveData<Boolean> _loading = new MutableLiveData<>();
    private MutableLiveData<String> _error = new MutableLiveData<>();
    private MutableLiveData<Repository<KThread>> repo = new MutableLiveData<>();
    private LiveData<KThread> _result = Transformations.switchMap(repo, repo -> {
        _error.postValue(null);
        _loading.postValue(true);
       return repo.get();
    });
    private LiveData<Post> _detail = Transformations.map(_result, kThread -> {
        return kThread.getHeadPost();
    });
    private LiveData<List<Post>> _list = Transformations.map(_result, kThread -> {
        _loading.postValue(false);
        if(kThread == null){
            _error.postValue("kThread == null");
            return Collections.emptyList();
        }
        return kThread.getComments();
    });


    public ThreadViewModel(@NonNull Application application) {
        super(application);
    }

    public void setThreadUrl(String threadUrl) {
        dao= AppDatabase.getInstance(getApplication()).postDao();
        this.threadUrl= threadUrl;
        repo.postValue(new ThreadRepository(threadUrl, dao));
    }

    @Override
    public int getCurrentPage() {
        return 0;
    }

    @Override
    public LiveData<List<Post>> children() {
        return _list;
    }

    @Override
    public LiveData<Post> detail() {
        return _detail;
    }

    @Override
    public void refreshChildren() {
        repo.postValue(new ThreadRepository(threadUrl, dao));
    }

    @Override
    public void nextChildren() {
    }

    @Override
    public void loadDetail(Bundle bundle) {
    }

    @Override
    public LiveData<Boolean> loading() {
        return _loading;
    }

    public LiveData<String> error(){
        return _error;
    }
}
