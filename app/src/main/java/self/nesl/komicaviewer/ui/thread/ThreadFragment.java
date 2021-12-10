package self.nesl.komicaviewer.ui.thread;

import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.SampleListFragment;
import self.nesl.komicaviewer.ui.SampleViewModel;

public class ThreadFragment extends SampleListFragment<Post, Post> {
    private ThreadViewModel threadViewModel;
    private PostListAdapter headAdapter;
    private PostListAdapter adapter;

    @Override
    protected void initAdapter(){
        super.initAdapter();
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
        if(adapter == null){
            adapter = new PostListAdapter();
            adapter.setOnReplyToClickListener((replyTo, list) -> {
                Toast.makeText(getContext(), replyTo.getId(), Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putSerializable(ReplyDialog.COLUMN_POST,replyTo);
                bundle.putParcelableArrayList(ReplyDialog.COLUMN_POST_LIST, new ArrayList<>(list));
                ReplyDialog.newInstance(bundle).show(getChildFragmentManager(), "ReplyDialog");
            });
        }
        return adapter;
    }

    @Override
    protected int getMaxPage(){ return 0; }
}
