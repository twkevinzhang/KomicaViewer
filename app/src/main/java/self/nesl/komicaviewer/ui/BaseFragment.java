package self.nesl.komicaviewer.ui;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import self.nesl.komicaviewer.ui.board.BoardFragment;
import self.nesl.komicaviewer.ui.board.BoardViewModel;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public abstract class BaseFragment extends Fragment {
    private BaseViewModel viewModel;
    private int page = 0;
    private boolean canLoad;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post, container, false);
        final RecyclerView lst = v.findViewById(R.id.rcLst);
        final TextView txtMsg=v.findViewById(R.id.txtMsg);
        PostlistAdapter adapter=createAdapter();

        // SwipeRefreshLayout
        final SwipeRefreshLayout cateSwipeRefreshLayout = v.findViewById(R.id.refresh_layout);
        cateSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                viewModel.load(0);
                adapter.notifyDataSetChanged();
                cateSwipeRefreshLayout.setRefreshing(false);
            }
        });

        // data and adapter
        viewModel.getPost().observe(getViewLifecycleOwner(), new Observer<Post>() {
            int start, end = 0;
            @Override
            public void onChanged(Post post) {
                assert post != null;
                setTitle(post);
                ArrayList posts=post.getReplies();
                adapter.addAllPost(posts);
                if(canLoad){
                    start = end;
                    end = start + posts.size();
                    adapter.notifyItemRangeInserted(start, end);
                }else {
                    adapter.addThreadpost(post);
                    adapter.notifyDataSetChanged();
                }

                txtMsg.setText("onChanged,getItemCount: " + adapter.getItemCount());
                cateSwipeRefreshLayout.setRefreshing(false);
            }
        });

        // lst
        lst.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        lst.setAdapter(adapter);
        lst.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!canLoad)return;
                if (!recyclerView.canScrollVertically(1)) {
                    // 如果不能向下滑動，到底了
                    if (cateSwipeRefreshLayout.isRefreshing()) {
                        txtMsg.setText("急三小");
                        return;
                    }
                    cateSwipeRefreshLayout.setRefreshing(true);
                    page += 1;
                    txtMsg.setText("載入中" + page);
                    viewModel.load(page);

                } else if (!recyclerView.canScrollVertically(-1)) {
                    txtMsg.setText("到頂了(不能向上滑動)");
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if(dy<0){
//                    fab_menu_list.showMenuButton(true);
//                }else{
//                    fab_menu_list.hideMenuButton(true);
//                }
//                if(!recyclerView.canScrollVertically(-1)){
//                    // 如果不能向上滑動，到頂了
//                    fab_menu_list.showMenuButton(true);
//                }
            }
        });



        return v;
    }

    public void init(BaseViewModel viewModel,boolean canLoad){
        this.viewModel=viewModel;
        this.canLoad=canLoad;
    }

    abstract public PostlistAdapter createAdapter();
    abstract public void setTitle(Post post);
}
