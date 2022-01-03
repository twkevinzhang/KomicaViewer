package self.nesl.komicaviewer.ui.thread;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
    static int unloadedPage = 0;

    private Repository<KThread> threadRepository;
    private MutableLiveData<List<Post>> _list = new MutableLiveData<>(Collections.emptyList());
    private MutableLiveData<Post> _detail = new MutableLiveData<>();
    private MutableLiveData<Boolean> _loading = new MutableLiveData<>();
    private MutableLiveData<String> _error = new MutableLiveData<>();
    Boolean isTree = false;
    private int currentPage = unloadedPage;

    public ThreadViewModel(@NonNull Application application) {
        super(application);
    }

    public void setThreadUrl(String threadUrl) {
        PostDao dao= AppDatabase.getInstance(getApplication()).postDao();
        threadRepository =  new ThreadRepository(threadUrl, dao);
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
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
        currentPage = unloadedPage;
        _list.postValue(Collections.emptyList());
        nextChildren();
    }

    @Override
    public void nextChildren() {
        if(currentPage == unloadedPage){
            currentPage = 1;
            _loading.postValue(true);
            threadRepository.get(thread-> {
                if(thread != null){
                    _list.postValue(thread.getComments());
                }else{
                    _error.postValue("thread is null.");
                }
                _loading.postValue(false);
            });
        }
    }

    @Override
    public void loadDetail(Bundle bundle) {
        threadRepository.get(thread-> {
            if(thread != null)
                _detail.postValue(thread.getHeadPost());
        });
    }

    @Override
    public LiveData<Boolean> loading() {
        return _loading;
    }

    public LiveData<String> error(){
        return _error;
    }
}
