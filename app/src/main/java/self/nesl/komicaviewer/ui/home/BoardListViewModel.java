package self.nesl.komicaviewer.ui.home;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Collections;
import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.category.Category;
import self.nesl.komicaviewer.repository.BoardListRepository;
import self.nesl.komicaviewer.repository.Repository;
import self.nesl.komicaviewer.ui.SampleViewModel;

public class BoardListViewModel extends SampleViewModel<Category, Board> {
    static int unloadedPage = 0;
    private MutableLiveData<List<Board>> _list = new MutableLiveData<>(Collections.emptyList());
    private MutableLiveData<Category> _detail = new MutableLiveData<>();
    private MutableLiveData<Boolean> _loading = new MutableLiveData<>();
    private MutableLiveData<String> _error = new MutableLiveData<>();
    private Category category;
    private Repository<List<Board>> boardListRepository;
    private int currentPage = unloadedPage;

    public BoardListViewModel(@NonNull Application application) {
        super(application);
    }

    public void setCategory(Category category) {
        this.category=category;
        boardListRepository= new BoardListRepository(category);
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public LiveData<List<Board>> children() {
        return _list;
    }

    @Override
    public LiveData<Category> detail() {
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
            boardListRepository.get((list)-> {
                _list.postValue(list);
                _loading.postValue(false);
            });
        }
    }

    @Override
    public LiveData<String> error() {
        return _error;
    }

    @Override
    public void loadDetail(Bundle bundle) {
        _detail.postValue(category);
    }

    @Override
    public LiveData<Boolean> loading() {
        return _loading;
    }
}