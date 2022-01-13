package self.nesl.komicaviewer.ui.thread;
import static self.nesl.komicaviewer.util.ProjectUtils.filterRepliesList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.MessageFormat;
import java.util.List;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.SampleListFragment;
import self.nesl.komicaviewer.ui.SampleViewModel;
import self.nesl.komicaviewer.ui.gallery.Poster;
import self.nesl.komicaviewer.util.Utils;

public class ThreadFragment extends Fragment {
    public static final String COLUMN_POST_URL = "post_URL";
    private ThreadViewModel threadViewModel;
    private HeadPostAdapter headAdapter;
    private ReplyListAdapter repliesAdapter;
    protected View root;
    protected SwipeRefreshLayout refresh;
    protected RecyclerView rvLst;
    protected TextView txtMsg;

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
    final public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_refresh_list, container, false);
        rvLst = v.findViewById(R.id.rcLst);
        txtMsg = v.findViewById(R.id.txtMsg);
        refresh = v.findViewById(R.id.refresh_layout);
        root =v;
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initRefresh();
        initObserver();
        initAdapter();
    }

    protected void initRefresh() {
        refresh.setOnRefreshListener(this::refresh);
    }

    protected void refresh(){
        repliesAdapter.clear();
        threadViewModel.refresh();
    }

    protected void initObserver() {
        threadViewModel.thread().observe(getViewLifecycleOwner(), thread -> {
            // Head Post
            Post headPost = thread.getHeadPost();
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(headPost.getTitle());
            headAdapter.add(headPost);

            // replies
            List<Post> list = thread.getReplies();
            repliesAdapter.setAllReplies(list);
            boolean isTree= threadViewModel.isTree;
            if(isTree){
                List<Post> replies = Utils.concat(
                        filterRepliesList(null, list),
                        filterRepliesList(headPost.getId(), list)
                );
                repliesAdapter.addAll(replies);
                repliesAdapter.setOnImageClickListener(ReplyListAdapter.onImageClickListener(getContext(), Poster.toPosterList(replies)));
            }else{
                repliesAdapter.addAll(list);
                repliesAdapter.setOnImageClickListener(ReplyListAdapter.onImageClickListener(getContext(), Poster.toPosterList(list)));
            }

            txtMsg.setText(MessageFormat.format(
                    "onChanged," +
                            "adapter.getItemCount:{0}," +
                            "arr.length:{1}",
                    repliesAdapter.getItemCount(), list.size())
            );
            refresh.setRefreshing(false);
        });

        threadViewModel.error().observe(getViewLifecycleOwner(), error -> {
            if(error != null)
                txtMsg.setText(error);
            refresh.setRefreshing(false);
        });
    }

    protected void initAdapter(){
        repliesAdapter = new ReplyListAdapter();
        repliesAdapter.addSwitcher();
        repliesAdapter.setOnSwitchListener((buttonView, isChecked) -> {
            threadViewModel.isTree = isChecked;
            refresh();
        });
        repliesAdapter.setOnLinkClickListener(ReplyListAdapter.onLinkClickListener(getActivity()));
        repliesAdapter.setOnAllReplyClickListener(ReplyListAdapter.onAllReplyClickListener(getChildFragmentManager()));
        repliesAdapter.setOnReplyToClickListener(ReplyListAdapter.onReplyToClickListener(getChildFragmentManager()));
        headAdapter = new HeadPostAdapter(getActivity());
        rvLst.setAdapter(new ConcatAdapter(headAdapter, repliesAdapter));
    }
}
