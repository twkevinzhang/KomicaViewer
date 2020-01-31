package self.nesl.komicaviewer.view.postlist;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.StaticString;
import self.nesl.komicaviewer.adapter.PostlistAdapter;
import self.nesl.komicaviewer.model.Board;
import self.nesl.komicaviewer.model.Post;

public class PostlistFragment extends Fragment {
    private PostlistViewModel postlistViewModel;
    private boolean isLoading;
    private static Board parentBoard;

    public PostlistFragment() {
        // Required empty public constructor
    }

    public static PostlistFragment newInstance(Board parentBoard) {
        PostlistFragment fragment = new PostlistFragment();
        Bundle args = new Bundle();
        args.putSerializable("board", parentBoard);
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
        View v=inflater.inflate(R.layout.fragment_list, container, false);
        final RecyclerView lst = v.findViewById(R.id.lst);
        final TextView txtListMsg=v.findViewById(R.id.txtListMsg);
//        final FloatingActionMenu fab_menu_list = v.findViewById(R.id.fab_menu_list);

        // fab post
//        final FloatingActionButton fab_post = new FloatingActionButton(getActivity());
//        fab_post.setButtonSize(FloatingActionButton.SIZE_MINI);
//        fab_post.setLabelText(getString(R.string.fab_post));
//        fab_post.setImageResource(R.drawable.ic_edit);
//        fab_menu_list.addMenuButton(fab_post);
//        fab_post.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fab_menu_list.close(true);
//            }
//        });
//
//        // fab_menu
//        fab_menu_list.hideMenuButton(false);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                fab_menu_list.showMenuButton(true);
//                fab_menu_list.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_from_bottom));
//                fab_menu_list.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom));
//            }
//        }, 300);


        // lst
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        lst.setLayoutManager(layoutManager);
        final PostlistAdapter adapter=new PostlistAdapter(getContext());
        lst.setAdapter(adapter);

        lst.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page=0;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1)){
                    // 如果不能向下滑動，到底了
                    if (isLoading) {
                        txtListMsg.setText("急三小");
                        return;
                    }
                    isLoading = true;
                    page+=1;
                    txtListMsg.setText("載入中"+page);
                    postlistViewModel.loadKomicaPostlist(page);


                }else if(!recyclerView.canScrollVertically(-1)){
                    // 如果不能向上滑動，到頂了
                    txtListMsg.setText("到頂了(不能向上滑動)");

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

        // data and adapter
        if(parentBoard.getWeb().getName().equals(StaticString.KOMICA_NAME)){
            postlistViewModel.loadKomicaPostlist(0);
            postlistViewModel.getPostlist().observe(this, new Observer<ArrayList<Post>>() {
                int start,end=0;

                @Override
                public void onChanged(@Nullable final ArrayList<Post> ps) {

                    if (ps!=null && ps.size()!=0) {
                        adapter.addMultiPostToList(ps);
                        start=end;
                        end=start+ps.size();
                        adapter.notifyItemRangeInserted(start, end);
                        isLoading=false;
                        txtListMsg.setText("onChanged,getItemCount:"+adapter.getItemCount());
                    }
                }
            });
        }

        // SwipeRefreshLayout
        final SwipeRefreshLayout cateSwipeRefreshLayout = v.findViewById(R.id.refresh_layout);
        cateSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setPostlist(new ArrayList<Post>());
                postlistViewModel.loadKomicaPostlist(0);
                adapter.notifyDataSetChanged();
                cateSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return v;
    }

    public interface OnFragmentInteractionListener {
        // Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
