package self.nesl.komicaviewer.ui.home;

import static self.nesl.komicaviewer.ui.board.ThreadListViewModel.COLUMN_THREAD_LIST;
import static self.nesl.komicaviewer.ui.home.BoardListViewModel.COLUMN_HOST;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.repository.CategoryRepository;

public class BoardListFragment extends Fragment {
    private BoardListViewModel boardListViewModel;
    private BoardListAdapter adapter;

    private SwipeRefreshLayout refresh;
    private RecyclerView rvLst;
    private TextView txtMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boardListViewModel = ViewModelProviders.of(this).get(BoardListViewModel.class);
        if (getArguments() != null){
            boardListViewModel.setArgs(getArguments());
        }else{
            CategoryRepository repo = new CategoryRepository();
            repo.getAll(categories -> {
                Bundle args = new Bundle();
                args.putSerializable(COLUMN_HOST, categories.get(0));
                boardListViewModel.setArgs(args);
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_posts, container, false);
        rvLst = v.findViewById(R.id.rcLst);
        txtMsg = v.findViewById(R.id.txtMsg);
        refresh = v.findViewById(R.id.refresh_layout);
        initAdapter();
        initObserver();
        initRefresh();
        loadPage();
        return v;
    }

    private void initAdapter() {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        adapter = new BoardListAdapter();
        adapter.setOnClickListener((view, board) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(COLUMN_THREAD_LIST, board);
            navController.navigate(R.id.action_nav_home_to_nav_board, bundle);
        });
        rvLst.setAdapter(adapter);
    }

    private void initObserver() {
        boardListViewModel.detail().observe(getViewLifecycleOwner(), host -> {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(host.getTitle());
        });
        boardListViewModel.children().observe(getViewLifecycleOwner(), boards -> {
            adapter.addAll(boards);
            refresh.setRefreshing(false);
        });
    }

    private void initRefresh() {
        refresh.setOnRefreshListener(() -> {
            loadPage();
        });
    }

    private void loadPage() {
        refresh.setRefreshing(true);
        boardListViewModel.loadChildren(null);
    }
}
