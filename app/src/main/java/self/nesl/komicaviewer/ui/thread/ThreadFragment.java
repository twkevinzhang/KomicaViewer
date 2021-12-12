package self.nesl.komicaviewer.ui.thread;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ConcatAdapter;

import java.text.MessageFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.SampleListFragment;
import self.nesl.komicaviewer.ui.SampleViewModel;

public class ThreadFragment extends SampleListFragment<Post, Post> {
    private ThreadViewModel threadViewModel;
    private HeadPostAdapter headAdapter;
    private CommentListAdapter commentAdapter;

    @Override
    protected void initAdapter(){
        super.initAdapter();
        headAdapter = new HeadPostAdapter(getActivity());
        rvLst.setAdapter(new ConcatAdapter(headAdapter, getAdapter()));
    }

    @Override
    protected void initObserver() {
        super.initObserver();
        threadViewModel.isTree.observe(getViewLifecycleOwner(), isTree->{
            super.refresh();
        });

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
    protected SampleAdapter<Post> getAdapter() {
        if(commentAdapter == null){
            commentAdapter = new CommentListAdapter(true);
            commentAdapter.setOnSwitchListener((buttonView, isChecked) -> {
                threadViewModel.isTree.postValue(isChecked);
            });
            commentAdapter.setOnLinkClickListener(CommentListAdapter.onLinkClickListener(getActivity()));
            commentAdapter.setOnAllReplyClickListener(CommentListAdapter.onAllReplyClickListener(getChildFragmentManager()));
            commentAdapter.setOnReplyToClickListener(CommentListAdapter.onReplyToClickListener(getChildFragmentManager()));
        }
        return commentAdapter;
    }

    @Override
    protected int getMaxPage(){ return 0; }
}
