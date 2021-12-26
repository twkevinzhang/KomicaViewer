package self.nesl.komicaviewer.ui.board;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.SampleListFragment;
import self.nesl.komicaviewer.ui.SampleViewModel;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.thread.ThreadFragment;

public class ThreadListFragment extends SampleListFragment<Board, Post> {
    public static final String COLUMN_BOARD = "board";
    private ThreadListViewModel threadListViewModel;
    private ThreadListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        threadListViewModel = ViewModelProviders.of(this).get(ThreadListViewModel.class);
        if (getArguments() != null){
            Board parent = (Board) getArguments().getSerializable(COLUMN_BOARD);
            threadListViewModel.setBoard(parent);
        }
    }

    @Override
    protected void initAdapter() {
        super.initAdapter();
        NavController navController = Navigation.findNavController(root);
        adapter.setOnClickListener((view, thread) -> {
            Bundle bundle = new Bundle();
            bundle.putString(ThreadFragment.COLUMN_POST_URL, thread.getUrl());
            navController.navigate(R.id.action_nav_board_to_nav_post, bundle);
        });
    }

    @Override
    protected SampleViewModel<Board, Post> getViewModel() {
        return threadListViewModel;
    }

    @Override
    protected SampleAdapter<Post> getAdapter() {
        if(adapter == null)
            adapter = new ThreadListAdapter();
        return adapter;
    }
}

