package self.nesl.komicaviewer.ui.thread;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
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
import self.nesl.komicaviewer.models.KThread;
import self.nesl.komicaviewer.ui.IViewModel;
import self.nesl.komicaviewer.ui.SampleViewModel;

public class ThreadViewModel extends AndroidViewModel implements IViewModel {
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
    private LiveData<KThread> _kThread = Transformations.map(_result, kThread -> {
        _loading.postValue(false);
        if(kThread == null){
            _error.postValue("kThread == null");
            return null;
        }
        return kThread;
    });


    public ThreadViewModel(@NonNull Application application) {
        super(application);
    }

    public void setThreadUrl(String threadUrl) {
        dao= AppDatabase.getInstance(getApplication()).postDao();
        this.threadUrl= threadUrl;
        refresh();
    }

    public LiveData<KThread> thread() {
        return _kThread;
    }

    @Override
    public LiveData<Boolean> loading() {
        return _loading;
    }

    public LiveData<String> error(){
        return _error;
    }

    public void refresh() {
        repo.postValue(new ThreadRepository(threadUrl, dao));
    }
}
