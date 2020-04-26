package self.nesl.komicaviewer.ui.local;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.adapter.PostlistAdapter;
import self.nesl.komicaviewer.db.PostDB;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.ui.post.PostViewModel;
import self.nesl.komicaviewer.ui.post.ReplyDialog;

import static self.nesl.komicaviewer.Const.IS_TEST;
import static self.nesl.komicaviewer.Const.POST_URL;
import static self.nesl.komicaviewer.util.Util.print;

public class LocalFragment extends Fragment {

    private LocalViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LocalViewModel.class);
        if (getArguments() != null) {
            mViewModel.setTable(getArguments().getString("tableName"));
        }
        mViewModel.update();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_post, container, false);
        final RecyclerView lst = v.findViewById(R.id.rcLst);
        final TextView txtMsg = v.findViewById(R.id.txtMsg);
        final PostlistAdapter adapter = new PostlistAdapter(this, new PostlistAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(Post post) {
                Bundle bundle = new Bundle();
                bundle.putString("postUrl", (IS_TEST)?POST_URL:post.getUrl());
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_nav_local_to_nav_post,bundle);
            }
        });

        // lst
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        lst.setLayoutManager(linearLayoutManager);
        lst.setAdapter(adapter);

        // data and adapter
        mViewModel.getPostlist().observe(getViewLifecycleOwner(), new Observer<ArrayList<Post>>() {
            @Override
            public void onChanged(ArrayList<Post> postlist) {
                assert postlist != null;
                adapter.addAllPost(postlist);
                adapter.notifyDataSetChanged();
            }
        });

        // SwipeRefreshLayout
        final SwipeRefreshLayout cateSwipeRefreshLayout = v.findViewById(R.id.refresh_layout);
        cateSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                mViewModel.update();
                adapter.notifyDataSetChanged();
                cateSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return v;
    }

}
