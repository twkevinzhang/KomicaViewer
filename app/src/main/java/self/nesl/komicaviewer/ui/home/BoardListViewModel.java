package self.nesl.komicaviewer.ui.home;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Collections;
import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.category.Category;
import self.nesl.komicaviewer.models.category.KomicaCategory;
import self.nesl.komicaviewer.repository.BoardRepository;
import self.nesl.komicaviewer.repository.CategoryRepository;
import self.nesl.komicaviewer.request.BoardListRequestFactory;
import self.nesl.komicaviewer.request.Request;
import self.nesl.komicaviewer.ui.SampleViewModel;

public class BoardListViewModel extends SampleViewModel<Category, Board> {
    public static String COLUMN_HOST = "board list request { url }";
    private MutableLiveData<List<Board>> _list = new MutableLiveData<>();
    private MutableLiveData<Category> _detail = new MutableLiveData<>();
    private Category category;
    private BoardRepository boardRepository;

    public BoardListViewModel() {
        this.boardRepository = new BoardRepository();
        _list.postValue(Collections.emptyList());
    }

    public void setArgs(Bundle bundle) {
        category = (Category) bundle.getSerializable(COLUMN_HOST);
        Request<List<Board>> req= new BoardListRequestFactory(category).createRequest(null);
        boardRepository.setRequest(req);
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
    public void loadChildren(Bundle bundle) {
        boardRepository.getAll(_list::postValue);
    }

    @Override
    public void loadDetail(Bundle bundle) {
        _detail.postValue(category);
    }
}