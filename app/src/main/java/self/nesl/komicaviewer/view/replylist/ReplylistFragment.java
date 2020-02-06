package self.nesl.komicaviewer.view.replylist;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.adapter.PostAdapter;
import self.nesl.komicaviewer.model.Post;

public class ReplylistFragment extends Fragment {

    private ReplylistViewModel mViewModel;
    Post post;

    public ReplylistFragment() {
        // Required empty public constructor
    }

    public static ReplylistFragment newInstance(Post post) {
        ReplylistFragment fragment = new ReplylistFragment();
        Bundle args = new Bundle();
        args.putSerializable("post", post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = (Post) getArguments().getSerializable("post");
            mViewModel = ViewModelProviders.of(this).get(ReplylistViewModel.class);
            mViewModel.setPost(post);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_postlist, container, false);
        mViewModel.loadKomicaPost(post.getParentBoard());

        final RecyclerView lst = v.findViewById(R.id.lst);
        final FloatingActionMenu fab_menu = v.findViewById(R.id.fab_menu_list);


        // fab openUrl
        final FloatingActionButton fab_openUrl = new FloatingActionButton(getActivity());
        fab_openUrl.setButtonSize(FloatingActionButton.SIZE_MINI);
        fab_openUrl.setLabelText(getString(R.string.fab_open_url));
        fab_openUrl.setImageResource(R.drawable.ic_edit);
        fab_menu.addMenuButton(fab_openUrl);
        fab_openUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu.close(true);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(post.getLink()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    getContext().startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    getContext().startActivity(intent);
                }
            }
        });

        // fab addToFavorite
        final FloatingActionButton fab_addToFavorite = new FloatingActionButton(getActivity());
        fab_addToFavorite.setButtonSize(FloatingActionButton.SIZE_MINI);
        fab_addToFavorite.setLabelText(getString(R.string.fab_add_to_favorite));
        fab_addToFavorite.setImageResource(R.drawable.ic_edit);
        fab_menu.addMenuButton(fab_addToFavorite);
//        fab_addToFavorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fab_menu.close(true);
//                PostDB.switchTable(StaticString.FAVORITE_TABLE_NAME);
//                PostDB.addPost(post);
//            }
//        });

        // fab to_top
        final FloatingActionButton fab_p_to_top = new FloatingActionButton(getActivity());
        fab_p_to_top.setButtonSize(FloatingActionButton.SIZE_MINI);
        fab_p_to_top.setLabelText(getString(R.string.fab_to_top));
        fab_p_to_top.setImageResource(R.drawable.ic_edit);
        fab_menu.addMenuButton(fab_p_to_top);
        fab_p_to_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu.close(true);
                lst.scrollToPosition(0);
            }
        });

        // fab to_last
        final FloatingActionButton fab_p_to_last = new FloatingActionButton(getActivity());
        fab_p_to_last.setButtonSize(FloatingActionButton.SIZE_MINI);
        fab_p_to_last.setLabelText(getString(R.string.fab_to_last));
        fab_p_to_last.setImageResource(R.drawable.ic_edit);
        fab_menu.addMenuButton(fab_p_to_last);
        fab_p_to_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu.close(true);
                lst.scrollToPosition(lst.getAdapter().getItemCount() - 1);
            }
        });

        // is FB Style
//        final boolean isFBStyle = Settings.getBoolean(StaticString.PREFERENCES_NAME, false);
        final boolean isFBStyle = false;

        // lst
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        lst.setLayoutManager(layoutManager);
        final PostAdapter adapter = new PostAdapter(getContext());
        lst.setAdapter(adapter);

        lst.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if(dy<0){
//                    fab_menu.showMenuButton(true);
//                }else{
//                    fab_menu.hideMenuButton(true);
//                }
//                if(!recyclerView.canScrollVertically(-1)){
//                    // 如果不能向上滑動，到頂了
//                    fab_menu.showMenuButton(true);
//                }
            }

        });

        // data and adapter
        mViewModel.getPost().observe(this, new Observer<Post>() {
            @Override
            public void onChanged(@Nullable final Post post) {
                assert post != null;
                ArrayList<Post> arr2 = new ArrayList<Post>();
                arr2.add(0, post);
                arr2.addAll(post.getReplyAll());

                if (!isFBStyle) {
                    adapter.setPostlist(arr2);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        // SwipeRefreshLayout
        final SwipeRefreshLayout cateSwipeRefreshLayout = v.findViewById(R.id.refresh_layout);
        cateSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.loadKomicaPost(post.getParentBoard());
                adapter.notifyDataSetChanged();
                cateSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return v;
    }

}
