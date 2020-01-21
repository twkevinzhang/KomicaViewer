package self.nesl.forumviewer.ui.board_komica;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.adapter.BoardlistAdapter;
import self.nesl.komicaviewer.komica.KomicaAllBoardsVM;
import self.nesl.komicaviewer.komica.KomicaTop50BoardsVM;
import self.nesl.komicaviewer.komica.MyViewModel;
import self.nesl.komicaviewer.model.Board;
import self.nesl.komicaviewer.model.Web;

/**
 * A placeholder fragment containing a simple view.
 */

// 設定資料顯示方式
public class KomicaChildFragment extends Fragment {

    private static int fragment_no =0;
    private static Web mWeb;
    private MyViewModel boardlistViewModel;

    public static KomicaChildFragment newInstance(int index,Web web) {
        KomicaChildFragment fragment = new KomicaChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("fragment_no", index);
        bundle.putSerializable("web", web);
        fragment.setArguments(bundle);
        return fragment;
    }

    public KomicaChildFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWeb = (Web)getArguments().getSerializable("web");
            fragment_no=getArguments().getInt("fragment_no");
        }

        switch (fragment_no){
            case 0:
                boardlistViewModel = ViewModelProviders.of(this).get(KomicaAllBoardsVM.class);
                boardlistViewModel.setWeb(mWeb);
                boardlistViewModel.loadAllBoards();
            case 1:
                boardlistViewModel = ViewModelProviders.of(this).get(KomicaTop50BoardsVM.class);
                boardlistViewModel.setWeb(mWeb);
                boardlistViewModel.loadTop50Boards();
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list,container, false);
        final TextView textView = root.findViewById(R.id.txtListMsg);
        final RecyclerView lst = root.findViewById(R.id.lst);

        textView.setText(fragment_no+"");


        // lst
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        lst.setLayoutManager(layoutManager);
        final BoardlistAdapter adapter=new BoardlistAdapter(getActivity());
        lst.setAdapter(adapter);

        // data and adapter
        boardlistViewModel.getBoards().observe(getActivity(), new Observer<ArrayList<Board>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<Board> bs) {
                adapter.setBoardlist(bs);
                adapter.notifyDataSetChanged();
            }
        });

        // SwipeRefreshLayout
        final SwipeRefreshLayout cateSwipeRefreshLayout = root.findViewById(R.id.refresh_layout);
        cateSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                boardlistViewModel.loadAllBoards();
                adapter.notifyDataSetChanged();
                cateSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return root;
    }
}