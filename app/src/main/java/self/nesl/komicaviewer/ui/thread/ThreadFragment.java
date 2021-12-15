package self.nesl.komicaviewer.ui.thread;
import static self.nesl.komicaviewer.util.ProjectUtils.filterRepliesList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ConcatAdapter;

import java.text.MessageFormat;
import java.util.List;

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
        getViewModel().children().observe(getViewLifecycleOwner(), list -> {
            commentAdapter.setAllComments(list);
            boolean isTree= threadViewModel.isTree;
            if(isTree){
                List<Post> replies = filterRepliesList(null, list);
                getAdapter().addAll(replies);
            }else{
                getAdapter().addAll(list);
            }

            txtMsg.setText(MessageFormat.format(
                    "onChanged," +
                            "adapter.getItemCount:{0}," +
                            "arr.length:{1}",
                    getAdapter().getItemCount(), list.size())
            );
            refresh.setRefreshing(false);
        });

        getViewModel().detail().observe(getViewLifecycleOwner(), detail -> {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(detail.getTitle());
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
            commentAdapter = new CommentListAdapter();
            commentAdapter.addSwitcher();
            commentAdapter.setOnSwitchListener((buttonView, isChecked) -> {
                threadViewModel.isTree = isChecked;
                super.refresh();
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
