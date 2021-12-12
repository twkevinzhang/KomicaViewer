package self.nesl.komicaviewer.ui.board;

import static self.nesl.komicaviewer.ui.thread.ThreadViewModel.COLUMN_POST;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.SampleListFragment;
import self.nesl.komicaviewer.ui.SampleViewModel;
import self.nesl.komicaviewer.ui.SampleAdapter;

public class ThreadListFragment extends SampleListFragment<Board, Post> {
    private ThreadListViewModel threadListViewModel;
    private ThreadListAdapter adapter;

    @Override
    protected void initAdapter() {
        super.initAdapter();
        NavController navController = Navigation.findNavController(root);
        adapter.setOnClickListener((view, thread) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(COLUMN_POST, thread);
            navController.navigate(R.id.action_nav_board_to_nav_post, bundle);
        });
    }

    @Override
    protected SampleViewModel<Board, Post> getViewModel() {
        if(threadListViewModel == null)
            threadListViewModel = ViewModelProviders.of(this).get(ThreadListViewModel.class);
        return threadListViewModel;
    }

    @Override
    protected SampleAdapter<Post> getAdapter() {
        if(adapter == null)
            adapter = new ThreadListAdapter();
        return adapter;
    }
}

