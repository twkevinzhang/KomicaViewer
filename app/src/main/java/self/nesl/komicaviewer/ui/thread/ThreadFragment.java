package self.nesl.komicaviewer.ui.thread;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.SampleListFragment;
import self.nesl.komicaviewer.ui.SampleViewModel;

public class ThreadFragment extends SampleListFragment<Post, Post> {
    private ThreadViewModel threadViewModel;
    private PostListAdapter headAdapter;
    private PostListAdapter adapter;

    @Override
    protected void setAdapter(){
        headAdapter = new PostListAdapter();
        rvLst.setAdapter(new ConcatAdapter(headAdapter, getAdapter()));
    }

    @Override
    protected void initObserver() {
        super.initObserver();
        getViewModel().detail().observe(getViewLifecycleOwner(), detail -> {
            headAdapter.add(detail);
        });
    }

    @Override
    protected SampleViewModel<Post, Post> getViewModel() {
        if(threadViewModel == null)
            threadViewModel = ViewModelProviders.of(this).get(ThreadViewModel.class);
        return threadViewModel;
    }

    @Override
    protected SampleAdapter<Post, ? extends RecyclerView.ViewHolder> getAdapter() {
        if(adapter == null)
            adapter = new PostListAdapter();
        return adapter;
    }

    @Override
    protected int getMaxPage(){ return 0; }
}
