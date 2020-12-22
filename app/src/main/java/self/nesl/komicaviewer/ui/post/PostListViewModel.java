package self.nesl.komicaviewer.ui.post;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Collections;
import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.repository.BoardRepository;
import self.nesl.komicaviewer.repository.PostRepository;
import self.nesl.komicaviewer.request.PostListRequestFactory;
import self.nesl.komicaviewer.request.Request;
import self.nesl.komicaviewer.ui.SampleViewModel;

public class PostListViewModel extends SampleViewModel<Post, Post> {
    public static final String COLUMN_POST = "post";

    private PostRepository postRepository;
    private Post thread;
    private MutableLiveData<List<Post>> _list = new MutableLiveData<>();
    private MutableLiveData<Post> _detail = new MutableLiveData<>();

    public PostListViewModel() {
        this.postRepository = new PostRepository();
        _list.postValue(Collections.emptyList());
    }

    public void setArgs(Bundle bundle) {
        thread = (Post) bundle.getSerializable(COLUMN_POST);
        Request<List<Post>> req = new PostListRequestFactory(thread).createRequest(null);
        postRepository.setRequest(req);
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
    public void loadChildren(Bundle bundle) {
        postRepository.getAll(_list::postValue);
    }

    @Override
    public void loadDetail(Bundle bundle) {
        _detail.postValue(thread);
    }
}