package self.nesl.komicaviewer.ui.thread;
import static self.nesl.komicaviewer.util.ProjectUtils.filterRepliesList;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ConcatAdapter;

import java.text.MessageFormat;
import java.util.List;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.SampleListFragment;
import self.nesl.komicaviewer.ui.SampleViewModel;
import self.nesl.komicaviewer.ui.gallery.Poster;

public class ThreadFragment extends SampleListFragment<Post, Post> {
    public static final String COLUMN_POST_URL = "post_URL";
    private ThreadViewModel threadViewModel;
    private HeadPostAdapter headAdapter;
    private ReplyListAdapter repliesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        threadViewModel = ViewModelProviders.of(this).get(ThreadViewModel.class);
        if (getArguments() != null){
            String url= getArguments().getString(COLUMN_POST_URL);
            threadViewModel.setThreadUrl(url);
        }
    }

    @Override
    protected void initAdapter(){
        super.initAdapter();
        headAdapter = new HeadPostAdapter(getActivity());
        rvLst.setAdapter(new ConcatAdapter(headAdapter, getAdapter()));
    }

    @Override
    protected void initObserver() {
        getViewModel().children().observe(getViewLifecycleOwner(), list -> {
            repliesAdapter.setAllReplies(list);
            boolean isTree= threadViewModel.isTree;
            if(isTree){
                List<Post> replies = filterRepliesList(null, list);
                getAdapter().addAll(replies);
                repliesAdapter.setOnImageClickListener(ReplyListAdapter.onImageClickListener(getContext(), Poster.toPosterList(replies)));
            }else{
                getAdapter().addAll(list);
                repliesAdapter.setOnImageClickListener(ReplyListAdapter.onImageClickListener(getContext(), Poster.toPosterList(list)));
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

        getViewModel().error().observe(getViewLifecycleOwner(), error -> {
            if(error != null)
                txtMsg.setText(error);
            refresh.setRefreshing(false);
        });
    }

    @Override
    protected SampleViewModel<Post, Post> getViewModel() {
        return threadViewModel;
    }

    @Override
    protected SampleAdapter<Post> getAdapter() {
        if(repliesAdapter == null){
            repliesAdapter = new ReplyListAdapter();
            repliesAdapter.addSwitcher();
            repliesAdapter.setOnSwitchListener((buttonView, isChecked) -> {
                threadViewModel.isTree = isChecked;
                super.refresh();
            });
            repliesAdapter.setOnLinkClickListener(ReplyListAdapter.onLinkClickListener(getActivity()));
            repliesAdapter.setOnAllReplyClickListener(ReplyListAdapter.onAllReplyClickListener(getChildFragmentManager()));
            repliesAdapter.setOnReplyToClickListener(ReplyListAdapter.onReplyToClickListener(getChildFragmentManager()));
        }
        return repliesAdapter;
    }
}
