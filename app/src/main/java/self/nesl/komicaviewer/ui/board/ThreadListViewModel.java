package self.nesl.komicaviewer.ui.board;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.repository.BoardRepository;
import self.nesl.komicaviewer.repository.PostRepository;
import self.nesl.komicaviewer.request.Request;
import self.nesl.komicaviewer.request.ThreadListRequestFactory;
import self.nesl.komicaviewer.ui.SampleViewModel;

public class ThreadListViewModel extends SampleViewModel<Board, Post> {
    public static final String COLUMN_THREAD_LIST = "board";

    private PostRepository postRepository;
    private Board parent;
    private MutableLiveData<List<Post>> _list = new MutableLiveData<>();
    private MutableLiveData<Board> _detail = new MutableLiveData<>();

    public ThreadListViewModel() {
        this.postRepository = new PostRepository();
        _list.postValue(Collections.emptyList());
    }

    public void setArgs(Bundle bundle) {
        parent = (Board) bundle.getSerializable(COLUMN_THREAD_LIST);
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
    public void loadChildren(Bundle bundle) {
        setRequest(bundle);
        postRepository.getAll(_list::postValue);
    }

    @Override
    public void loadDetail(Bundle bundle) {
        _detail.postValue(parent);
    }

    private void setRequest(Bundle bundle){
        Request<List<Post>> req = new ThreadListRequestFactory(parent).createRequest(bundle);
        postRepository.setRequest(req);
    }
}