package self.nesl.komicaviewer.view.postlist;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.os.Handler;

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
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.adapter.PostlistAdapter;
import self.nesl.komicaviewer.model.Board;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.view.post.PostActivity;

public class PostlistFragment extends Fragment {
    private PostlistViewModel postlistViewModel;
    private boolean isLoading;
    private static Board parentBoard;

    private MaterialSearchBar searchBar;

    public PostlistFragment() {
        // Required empty public constructor
    }

    public static PostlistFragment newInstance(Bundle args) {
        PostlistFragment fragment = new PostlistFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parentBoard = (Board) getArguments().getSerializable("board");
            postlistViewModel = ViewModelProviders.of(this).get(PostlistViewModel.class);
            postlistViewModel.setBoard(parentBoard);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_postlist, container, false);
        final RecyclerView lst = v.findViewById(R.id.lst);
        final TextView txtListMsg = v.findViewById(R.id.txtListMsg);
        final FloatingActionMenu fab_menu = v.findViewById(R.id.fab_menu_list);

        // fab openUrl
        FloatingActionButton fab_openUrl = new FloatingActionButton(getActivity());
        fab_openUrl.setButtonSize(FloatingActionButton.SIZE_MINI);
        fab_openUrl.setLabelText(getString(R.string.fab_open_url));
        fab_openUrl.setImageResource(R.drawable.ic_edit);
        fab_menu.addMenuButton(fab_openUrl);
        fab_openUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu.close(true);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(parentBoard.getLink()));
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
        FloatingActionButton fab_addToFavorite = new FloatingActionButton(getActivity());
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

        // fab post
        final FloatingActionButton fab_post = new FloatingActionButton(getActivity());
        fab_post.setButtonSize(FloatingActionButton.SIZE_MINI);
        fab_post.setLabelText(getString(R.string.fab_post));
        fab_post.setImageResource(R.drawable.ic_edit);
        fab_menu.addMenuButton(fab_post);
        fab_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu.close(true);
                Intent intent = new Intent(getContext(), PostActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("board", parentBoard);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

        // fab_menu
        fab_menu.hideMenuButton(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fab_menu.showMenuButton(true);
                fab_menu.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_from_bottom));
                fab_menu.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom));
            }
        }, 300);


        // lst
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        lst.setLayoutManager(layoutManager);
        final PostlistAdapter adapter = new PostlistAdapter(getContext());
        lst.setAdapter(adapter);

        // data and adapter
        postlistViewModel.loadPostlist(0);
        parentBoard = postlistViewModel.getBoard();
        postlistViewModel.getPostlist().observe(this, new Observer<ArrayList<Post>>() {
            int start, end = 0;

            @Override
            public void onChanged(@Nullable final ArrayList<Post> ps) {
                assert ps != null && ps.size() != 0;
                adapter.addMultiPostToList(ps);
                start = end;
                end = start + ps.size();
                adapter.notifyItemRangeInserted(start, end);
                isLoading = false;
                txtListMsg.setText("onChanged,getItemCount:" + adapter.getItemCount());
            }
        });

        lst.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    // 如果不能向下滑動，到底了
                    if (isLoading) {
                        txtListMsg.setText("急三小");
                        return;
                    }
                    isLoading = true;
                    page += 1;
                    txtListMsg.setText("載入中" + page);
                    postlistViewModel.loadPostlist(page);


                } else if (!recyclerView.canScrollVertically(-1)) {
                    // 如果不能向上滑動，到頂了
                    txtListMsg.setText("到頂了(不能向上滑動)");

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy<0){
                    fab_menu.showMenuButton(true);
                }else{
                    fab_menu.hideMenuButton(true);
                }
                if(!recyclerView.canScrollVertically(-1)){
                    // 如果不能向上滑動，到頂了
                    fab_menu.showMenuButton(true);
                }
            }
        });

        // SwipeRefreshLayout
        final SwipeRefreshLayout cateSwipeRefreshLayout = v.findViewById(R.id.refresh_layout);
        cateSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setPostlist(new ArrayList<Post>());
                postlistViewModel.loadPostlist(0);
                adapter.notifyDataSetChanged();
                cateSwipeRefreshLayout.setRefreshing(false);
            }
        });

        // Search bar
        searchBar = v.findViewById(R.id.searchBar);
        searchBar.inflateMenu(R.menu.search_bar);
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {

            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

            }

            @Override
            public void onButtonClicked(int buttonCode) {
                switch (buttonCode) {
                    case MaterialSearchBar.BUTTON_NAVIGATION:
                        ((PostlistActivity) getActivity()).openDrawer();
                        break;
                    case MaterialSearchBar.BUTTON_SPEECH:
                        break;
                    case MaterialSearchBar.BUTTON_BACK:
                        searchBar.disableSearch();
                        break;
                }
            }

        });

        return v;
    }

    public interface OnFragmentInteractionListener {
        // Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
