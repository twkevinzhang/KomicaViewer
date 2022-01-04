package self.nesl.komicaviewer.ui.home;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.Collections;
import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.category.Category;
import self.nesl.komicaviewer.repository.BoardListRepository;
import self.nesl.komicaviewer.repository.CategoryListRepository;
import self.nesl.komicaviewer.repository.Repository;
import self.nesl.komicaviewer.ui.SampleViewModel;

public class BoardListViewModel extends SampleViewModel<Category, Board> {
    private MutableLiveData<Category> category = new MutableLiveData<>();

    private MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    private MutableLiveData<String> _error = new MutableLiveData<>();
    private LiveData<Category> _detail = Transformations.switchMap(category, category->{
        if(category == null) {
            return new CategoryListRepository().get(0);
        }else{
            return new MutableLiveData<>(category);
        }
    });
    private LiveData<Repository<List<Board>>> repo = Transformations.map(_detail, category->{
        return new BoardListRepository(category);
    });
    private LiveData<List<Board>> _result = Transformations.switchMap(repo, repo -> {
        _error.postValue(null);
        _loading.postValue(true);
        return repo.get();
    });
    private LiveData<List<Board>> _list = Transformations.map(_result, result -> {
        _loading.postValue(false);
        if(result == null){
            _error.postValue("result == null");
            return Collections.emptyList();
        }
        return result;
    });

    public BoardListViewModel(@NonNull Application application) {
        super(application);
    }

    public void setCategory(Category category) {
        this.category.postValue(category);
    }

    @Override
    public int getCurrentPage() {
        return 0;
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
        setCategory(this.category.getValue());
    }

    @Override
    public void nextChildren() {
    }

    @Override
    public LiveData<String> error() {
        return _error;
    }

    @Override
    public void loadDetail(Bundle bundle) {
    }

    @Override
    public LiveData<Boolean> loading() {
        return _loading;
    }
}