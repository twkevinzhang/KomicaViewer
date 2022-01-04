package self.nesl.komicaviewer.ui.collection;

import android.app.Application;

import androidx.annotation.NonNull;
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
import self.nesl.komicaviewer.repository.Repository;
import self.nesl.komicaviewer.ui.PagingViewModel;

public class CollectionViewModel extends AndroidViewModel implements PagingViewModel<Post> {
    private Repository<List<Post>> mRepository;
    private MutableLiveData<Integer> currentPage = new MutableLiveData<>(0);
    private MutableLiveData<Boolean> _loading = new MutableLiveData<>();
    private MutableLiveData<String> _error = new MutableLiveData<>();
    private LiveData<List<Post>> _result = Transformations.switchMap(currentPage, page -> {
        _error.postValue(null);
        _loading.postValue(true);
        return mRepository.get();
    });
    private LiveData<List<Post>> _list = Transformations.map(_result, result -> {
        _loading.postValue(false);
        if(result == null){
            _error.postValue("result == null");
            return Collections.emptyList();
        }
        return result;
    });

    public CollectionViewModel(@NonNull Application application) {
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

    @Override
    public LiveData<String> error(){
        return _error;
    }
}
