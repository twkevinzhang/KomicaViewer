package self.nesl.komicaviewer.ui.board;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.adapter.PostlistAdapter;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.ui.post.PostFragment;
import static self.nesl.komicaviewer.Const.IS_TEST;
import static self.nesl.komicaviewer.Const.POST_URL;
import static self.nesl.komicaviewer.util.Utils.print;

public class BoardFragment extends Fragment{
    public static final String COLUMN_BOARD="board";
    private BoardViewModel boardViewModel;
    private Post board;
    private int page = 0;

    public static BoardFragment newInstance(Bundle bundle) {
        BoardFragment fragment = new BoardFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boardViewModel = ViewModelProviders.of(this).get(BoardViewModel.class);
        if (getArguments() != null) {
            board = (Post)getArguments().getSerializable(BoardFragment.COLUMN_BOARD);
            boardViewModel.setBoardUrl(board.getUrl());
            boardViewModel.load(page);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post, container, false);
        final RecyclerView lst = v.findViewById(R.id.rcLst);
        final TextView txtMsg=v.findViewById(R.id.txtMsg);
        final PostlistAdapter adapter = new PostlistAdapter(this, new PostlistAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(Post post) {
                Bundle bundle = new Bundle();
                bundle.putString(PostFragment.COLUMN_POST_URL, (IS_TEST)?POST_URL:post.getUrl());
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_nav_board_to_nav_post,bundle);
            }
        });

        // title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(board.getTitle(0));

        // SwipeRefreshLayout
        final SwipeRefreshLayout cateSwipeRefreshLayout = v.findViewById(R.id.refresh_layout);
        cateSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                boardViewModel.load(0);
                adapter.notifyDataSetChanged();
                cateSwipeRefreshLayout.setRefreshing(false);
            }
        });

        // data and adapter
        boardViewModel.getPostlist().observe(getViewLifecycleOwner(), new Observer<ArrayList<Post>>() {
            int start, end = 0;
            @Override
            public void onChanged(ArrayList<Post> posts) {
                assert posts != null;
                adapter.addAllPost(posts);
                start = end;
                end = start + posts.size();
                adapter.notifyItemRangeInserted(start, end);
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
                if (!recyclerView.canScrollVertically(1)) {
                    // 如果不能向下滑動，到底了
                    if (cateSwipeRefreshLayout.isRefreshing()) {
                        txtMsg.setText("急三小");
                        return;
                    }
                    cateSwipeRefreshLayout.setRefreshing(true);
                    page += 1;
                    txtMsg.setText("載入中" + page);
                    boardViewModel.load(page);

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
}

