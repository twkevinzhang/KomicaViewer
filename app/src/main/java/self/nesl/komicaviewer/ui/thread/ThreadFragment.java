package self.nesl.komicaviewer.ui.thread;

import static self.nesl.komicaviewer.ui.thread.ReplyDialog.COLUMN_POST;
import static self.nesl.komicaviewer.ui.thread.ReplyDialog.COLUMN_POST_LIST;

import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.SampleListFragment;
import self.nesl.komicaviewer.ui.SampleViewModel;
import self.nesl.komicaviewer.ui.render.PostRender;
import self.nesl.komicaviewer.ui.viewholder.PostViewHolder;

public class ThreadFragment extends SampleListFragment<Post, Post> {
    private ThreadViewModel threadViewModel;
    private PostListAdapter headAdapter;
    private PostListAdapter adapter;

    @Override
    protected void initAdapter(){
        super.initAdapter();
        headAdapter = new PostListAdapter(getChildFragmentManager());
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
        if(adapter == null){
            adapter = new PostListAdapter(getChildFragmentManager());
        }
        return adapter;
    }

    @Override
    protected int getMaxPage(){ return 0; }
}
