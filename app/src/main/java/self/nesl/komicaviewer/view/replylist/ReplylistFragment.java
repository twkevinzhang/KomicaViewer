package self.nesl.komicaviewer.view.replylist;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import self.nesl.komicaviewer.adapter.ReplylistAdapter;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.view.post.PostActivity;

import static self.nesl.komicaviewer.StaticString.TEST_POST_ID;

public class ReplylistFragment extends Fragment {

    private ReplylistViewModel replylistViewModel;
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
            replylistViewModel = ViewModelProviders.of(this).get(ReplylistViewModel.class);
            replylistViewModel.setPost(post);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_postlist, container, false);
        replylistViewModel.loadReplylist(post.getParentBoard());

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

        // fab reply
        final FloatingActionButton fab_reply = new FloatingActionButton(getActivity());
        fab_reply.setButtonSize(FloatingActionButton.SIZE_MINI);
        fab_reply.setLabelText(getString(R.string.fab_reply));
        fab_reply.setImageResource(R.drawable.ic_edit);
        fab_menu.addMenuButton(fab_reply);
        fab_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu.close(true);
                Intent intent=new Intent(getContext(), PostActivity.class);
                // todo test
                intent.putExtra("masterPostId",TEST_POST_ID);
//                intent.putExtra("masterPostId",post.getId());
                intent.putExtra("board",post.getParentBoard());
                startActivity(intent);
            }
        });

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

        // lst
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        lst.setLayoutManager(layoutManager);
        final ReplylistAdapter adapter = new ReplylistAdapter(getContext());
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
        replylistViewModel.getPost().observe(this, new Observer<Post>() {
            @Override
            public void onChanged(@Nullable final Post post) {
                assert post != null;
                ArrayList<Post> arr2 = post.getReplyAll();
                arr2.add(0, post);
                adapter.setPostlist(arr2);
                adapter.notifyDataSetChanged();
            }
        });

        // SwipeRefreshLayout
        final SwipeRefreshLayout cateSwipeRefreshLayout = v.findViewById(R.id.refresh_layout);
        cateSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setPostlist(new ArrayList<Post>());
                replylistViewModel.loadReplylist(post.getParentBoard());
                adapter.notifyDataSetChanged();
                cateSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return v;
    }

}
