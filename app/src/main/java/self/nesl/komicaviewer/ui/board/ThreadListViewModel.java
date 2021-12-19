package self.nesl.komicaviewer.ui.board;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Collections;
import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.repository.Repository;
import self.nesl.komicaviewer.repository.ThreadListRepository;
import self.nesl.komicaviewer.ui.SampleViewModel;

public class ThreadListViewModel extends SampleViewModel<Board, Post> {
    public static final String COLUMN_THREAD_LIST = "board";

    private Repository<List<Post>> threadListRepository;
    private Board parent;
    private MutableLiveData<List<Post>> _list = new MutableLiveData<>(Collections.emptyList());
    private MutableLiveData<Board> _detail = new MutableLiveData<>();
    private MutableLiveData<Boolean> _loading = new MutableLiveData<>();
    private int currentPage = 0;

    public void setArgs(Bundle bundle) {
        parent = (Board) bundle.getSerializable(COLUMN_THREAD_LIST);
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
    public LiveData<Board> detail() {
        return _detail;
    }

    @Override
    public void clearChildren() {
        currentPage =0;
        _list.postValue(Collections.emptyList());
    }

    @Override
    public void loadChildren() {
        currentPage += 1;
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE, currentPage);
        setRequest(bundle);
        _loading.postValue(true);
        threadListRepository.get((list)-> {
            _list.postValue(list);
            _loading.postValue(false);
        });
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