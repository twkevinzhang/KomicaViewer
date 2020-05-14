package self.nesl.komicaviewer.ui.post;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.adapter.PostlistAdapter;
import self.nesl.komicaviewer.model.Post;

// nav_post
public class PostFragment extends Fragment {
    private PostViewModel postViewModel;
    public static final String COLUMN_POST_URL = "postUrl";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        if (getArguments() != null) {
            postViewModel.setPostUrl(getArguments().getString(COLUMN_POST_URL));
            postViewModel.update();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_post, container, false);
        final RecyclerView lst = v.findViewById(R.id.rcLst);
        final TextView txtMsg = v.findViewById(R.id.txtMsg);
        final PostlistAdapter adapter = new PostlistAdapter(new PostlistAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(Post post) {
                new ReplyDialog(post,getFragmentManager());
            }
        });

        // lst
        lst.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        lst.setAdapter(adapter);

        // data and adapter
        postViewModel.getPost().observe(getViewLifecycleOwner(), new Observer<Post>() {
            @Override
            public void onChanged(Post post) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(post.getTitle(0));
                adapter.addAllPost(post.getReplies());
                adapter.addThreadpost(post);
                adapter.notifyDataSetChanged();
            }
        });

        // SwipeRefreshLayout
        final SwipeRefreshLayout cateSwipeRefreshLayout = v.findViewById(R.id.refresh_layout);
        cateSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                postViewModel.update();
                adapter.notifyDataSetChanged();
                cateSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return v;
    }
}
