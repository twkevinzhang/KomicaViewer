package self.nesl.komicaviewer.ui.sora;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.adapter.PostlistAdapter;
import self.nesl.komicaviewer.model.Post;

import static self.nesl.komicaviewer.util.util.getHasHttpUrl;
import static self.nesl.komicaviewer.util.util.print;

public class SoraFragment extends Fragment {

    private SoraViewModel soraViewModel;
    private static String boardUrl;
    private boolean isLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soraViewModel = ViewModelProviders.of(this).get(SoraViewModel.class);
        if (getArguments() != null) {
            boardUrl = getResources().getString(getArguments().getInt("boardUrlId"));
        }
        soraViewModel.setBoardUrl(boardUrl);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        final RecyclerView lst = v.findViewById(R.id.rcLst);
        final PostlistAdapter adapter = new PostlistAdapter(getContext(),this);
        final TextView txtMsg=v.findViewById(R.id.txtMsg);

        // data and adapter
        soraViewModel.load(0);
        soraViewModel.getPostlist().observe(this, new Observer<ArrayList<Post>>() {
            int start, end = 0;
            @Override
            public void onChanged(ArrayList<Post> posts) {
                assert posts != null;
                adapter.addAllPost(posts);
                start = end;
                end = start + posts.size();
                adapter.notifyItemRangeInserted(start, end);
                isLoading = false;
            }
        });

        // lst
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        lst.setLayoutManager(layoutManager);
        lst.setAdapter(adapter);
        lst.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    // 如果不能向下滑動，到底了
                    if (isLoading) {
                        txtMsg.setText("急三小");
                        return;
                    }
                    isLoading = true;
                    page += 1;
                    txtMsg.setText("載入中" + page);
                    soraViewModel.load(page);

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

        // SwipeRefreshLayout
        final SwipeRefreshLayout cateSwipeRefreshLayout = v.findViewById(R.id.refresh_layout);
        cateSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                isLoading=true;
                soraViewModel.load(0);
                adapter.notifyDataSetChanged();
                isLoading=false;
                cateSwipeRefreshLayout.setRefreshing(isLoading);
            }
        });

        return v;
    }
}

