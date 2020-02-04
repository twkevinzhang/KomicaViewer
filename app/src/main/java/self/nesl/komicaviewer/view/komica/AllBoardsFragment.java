package self.nesl.komicaviewer.view.komica;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.adapter.BoardlistAdapter;
import self.nesl.komicaviewer.model.Board;
import self.nesl.komicaviewer.model.Web;

/**
 * A placeholder fragment containing a simple view.
 */

// 設定資料顯示方式
public class AllBoardsFragment extends Fragment {
    private static Web mWeb;
    private KomicaViewModel boardlistViewModel;

    public static AllBoardsFragment newInstance( Web web) {
        AllBoardsFragment fragment = new AllBoardsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("web", web);
        fragment.setArguments(bundle);
        return fragment;
    }

    public AllBoardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWeb = (Web) getArguments().getSerializable("web");
        }
        boardlistViewModel = ViewModelProviders.of(this).get(KomicaViewModel.class);
        boardlistViewModel.setWeb(mWeb);

        boardlistViewModel.loadAllBoards();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        final TextView textView = root.findViewById(R.id.txtListMsg);
        final RecyclerView lst = root.findViewById(R.id.lst);

        textView.setText(mWeb.getName());

        // lst
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        lst.setLayoutManager(layoutManager);
        final BoardlistAdapter adapter = new BoardlistAdapter(getActivity());
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
