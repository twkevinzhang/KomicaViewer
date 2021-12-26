package self.nesl.komicaviewer.ui.local;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.Collections;
import java.util.List;

import self.nesl.komicaviewer.db.dao.PostDao;
import self.nesl.komicaviewer.db.AppDatabase;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.repository.HistoryRepository;
import self.nesl.komicaviewer.repository.RepositoryAsLive;
import self.nesl.komicaviewer.ui.PagingViewModel;

public class HistoryViewModel extends AndroidViewModel implements PagingViewModel<Post> {
    private RepositoryAsLive<List<Post>> mRepository;
    private MutableLiveData<Integer> currentPage = new MutableLiveData<>(0);
    private MutableLiveData<Boolean> _loading = new MutableLiveData<>();
    private LiveData<List<Post>> _list = Transformations.switchMap(currentPage, page -> {
        _loading.postValue(false);
        return mRepository.get();
    });

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        PostDao dao = AppDatabase.getInstance(application).postDao();
        mRepository = new HistoryRepository(dao);
    }

    @Override
    public int getCurrentPage() {
        return currentPage.getValue();
    }

    @Override
    public LiveData<List<Post>> children() {
        return _list;
    }

    @Override
    public LiveData<Boolean> loading() {
        return _loading;
    }

    @Override
    public void refreshChildren() {
        currentPage.postValue(0);
    }

    @Override
    public void nextChildren() {
        currentPage.postValue(currentPage.getValue() +1);
    }
}
