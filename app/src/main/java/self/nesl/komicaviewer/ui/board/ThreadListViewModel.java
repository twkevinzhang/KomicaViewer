package self.nesl.komicaviewer.ui.board;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.Collections;
import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.repository.Repository;
import self.nesl.komicaviewer.repository.ThreadListRepository;
import self.nesl.komicaviewer.ui.SampleViewModel;

public class ThreadListViewModel extends SampleViewModel<Board, Post> {
    private Repository<List<Post>> threadListRepository;
    private Board parent;
    private MutableLiveData<Integer> currentPage = new MutableLiveData<>(0);
    private MutableLiveData<Board> _detail = new MutableLiveData<>();
    private MutableLiveData<Boolean> _loading = new MutableLiveData<>();
    private MutableLiveData<String> _error = new MutableLiveData<>();
    private LiveData<List<Post>> _result = Transformations.switchMap(currentPage, page -> {
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE, page);
        setRequest(bundle);
        _error.postValue(null);
        _loading.postValue(true);
        return threadListRepository.get();
    });
    private LiveData<List<Post>> _list = Transformations.map(_result, result -> {
        _loading.postValue(false);
        if(result == null){
            _error.postValue("result == null");
            return Collections.emptyList();
        }
        return result;
    });

    public ThreadListViewModel(@NonNull Application application) {
        super(application);
    }

    public void setBoard(Board board) {
        this.parent=board;
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
    public LiveData<Board> detail() {
        return _detail;
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
    public LiveData<String> error() {
        return _error;
    }

    @Override
    public LiveData<Boolean> loading() {
        return _loading;
    }

    @Override
    public void loadDetail(Bundle bundle) {
        _detail.postValue(parent);
    }

    private void setRequest(Bundle bundle){
        this.threadListRepository = new ThreadListRepository(parent, bundle);
    }
}