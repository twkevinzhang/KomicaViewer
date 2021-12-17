package self.nesl.komicaviewer.ui.thread;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Collections;
import java.util.List;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.repository.PostRepository;
import self.nesl.komicaviewer.request.PostListRequestFactory;
import self.nesl.komicaviewer.request.Request;
import self.nesl.komicaviewer.ui.SampleViewModel;

public class ThreadViewModel extends SampleViewModel<Post, Post> {
    public static final String COLUMN_POST = "post";
    static int unloadedPage = 0;

    private PostRepository postRepository;
    Post thread;
    private MutableLiveData<List<Post>> _list = new MutableLiveData<>(Collections.emptyList());
    private MutableLiveData<Post> _detail = new MutableLiveData<>();
    private MutableLiveData<Boolean> _loading = new MutableLiveData<>();
    Boolean isTree = false;
    private int currentPage = unloadedPage;

    public ThreadViewModel() {
        this.postRepository = new PostRepository();
    }

    public void setArgs(Bundle bundle) {
        thread = (Post) bundle.getSerializable(COLUMN_POST);
        Request<List<Post>> req = new PostListRequestFactory(thread).createRequest(null);
        postRepository.setRequest(req);
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
    public LiveData<Post> detail() {
        return _detail;
    }

    @Override
    public void clearChildren() {
        currentPage = unloadedPage;
        _list.postValue(Collections.emptyList());
    }

    @Override
    public void loadChildren() {
        if(currentPage == unloadedPage){
            currentPage = 1;
            _loading.postValue(true);
            postRepository.getAll((list)-> {
                _list.postValue(list);
                _loading.postValue(false);
            });
        }
    }

    @Override
    public void loadDetail(Bundle bundle) {
        _detail.postValue(thread);
    }

    @Override
    public LiveData<Boolean> loading() {
        return _loading;
    }
}